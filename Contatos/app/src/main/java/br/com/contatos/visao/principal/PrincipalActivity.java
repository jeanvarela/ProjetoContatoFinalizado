package br.com.contatos.visao.principal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.contatos.R;
import br.com.contatos.controlador.contato.ControladorContato;
import br.com.contatos.controlador.contato.IControladorContato;
import br.com.contatos.controlador.login.ControladorLogin;
import br.com.contatos.controlador.login.IControladorLogin;
import br.com.contatos.controlador.usuario.IControladorUsuario;
import br.com.contatos.infraestrutura.lista.adapter.contato.ContatoAdapter;
import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.infraestrutura.util.Util;
import br.com.contatos.infraestrutura.webservice.ServiceGenerator;
import br.com.contatos.infraestrutura.webservice.contato.ContatosService;
import br.com.contatos.modelo.entidade.contato.Contato;
import br.com.contatos.modelo.entidade.usuario.Usuario;
import br.com.contatos.modelo.repositorio.usuario.IRepositorioUsuario;
import br.com.contatos.modelo.repositorio.usuario.RepositorioUsuario;
import br.com.contatos.visao.contato.ContatoActivity;
import br.com.contatos.visao.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   SearchView.OnQueryTextListener,
                   MenuItemCompat.OnActionExpandListener{

    private ListView listaContados;
    private List<Contato> contatos;

    private IControladorUsuario controladorUsuario;
    private IControladorContato controladorContato;

    private long idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        idUsuario = intent.getLongExtra(Constantes.ID_USUARIO,-1);

        controladorContato = new ControladorContato(this);

        listaContados = (ListView) findViewById(R.id.listaContados);
        defineAcaoClickLista();
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        boolean finalizouLogin =  intent.getBooleanExtra(Constantes.FINALIZOULOGIN,false);
        boolean finalizouCadastro = intent.getBooleanExtra(Constantes.FINALIZOUCADASTRO,false);


        if (finalizouCadastro && Util.verificaConectado(this)){
            buscaTodosContatosRemotamente(idUsuario);
        }else{
            buscarContatoLocalmente(idUsuario ,null);
       }

        super.onResume();
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.hint_busca));
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_novo) {
            chamaTelaInserirContato();
        }else
        if (id == R.id.nav_sair){
            chamaTelaLogin();
        }else
        if (id == R.id.nav_sincronizar){
            if (Util.verificaConectado(getApplicationContext())){
                enviarContatosNaoSincronizados(idUsuario);
            }else{
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setTitle(getString(R.string.erro));
                dialogo.setMessage(getString(R.string.semConexao));
                dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });
                dialogo.create().show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void chamaTelaLogin(){
        deslogar();

        IRepositorioUsuario repositorioUsuario = new RepositorioUsuario(this);
        List<Usuario> usuarios = repositorioUsuario.recuperaUsuarios();

        for(Usuario user: usuarios){
            Log.i("Usuario",user.toString());
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }



    private void deslogar(){
        IControladorLogin controladorLogin = new ControladorLogin(this);
        controladorLogin.registraLogin(idUsuario, Constantes.NAO_LOGADO);
    }

    public void chamaTelaInserirContato(){
        Intent intent = new Intent(this, ContatoActivity.class);
        intent.putExtra(Constantes.ID_USUARIO,idUsuario);
        startActivityForResult(intent,Constantes.ID_APLICACAO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constantes.ID_APLICACAO) {
            if (resultCode == RESULT_OK){
                //whatever
            }
        }
    }

    private List<Contato> buscarContatos(){
        return controladorContato.buscarListaContatos(idUsuario);
    }

    private  void defineAcaoClickLista(){
        listaContados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Contato contato = (Contato)adapterView.getItemAtPosition(position);
                chamaTelaEdicao(contato);
            }
        });
    }

    private void chamaTelaEdicao(Contato contato){
        Intent intent = new Intent(this, ContatoActivity.class);
        intent.putExtra(Constantes.ID_USUARIO,contato.getIdUsuario());
        intent.putExtra(Constantes.ID_CONTATO,contato.getCodigo());
        startActivity(intent);
    }

    private void atualizaAdapter(List<Contato> contatos){

        if (contatos !=  null && contatos.size() > 0) {
            ContatoAdapter adapter = new ContatoAdapter(this, contatos);
            listaContados.setAdapter(adapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String conteudoBusca) {
        buscar(conteudoBusca);

        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    /**
     *
     * @param conteudoBusca
     */
    public void buscar(String conteudoBusca) {
        if (contatos != null) {
            List<Contato> listaOriginalContatos = new ArrayList<Contato>(contatos);
            boolean pesquisaTelefone  = Util.isPhone(conteudoBusca);

            if (pesquisaTelefone){
                for (int i = listaOriginalContatos.size() - 1; i >= 0; i--) {
                    Contato contato = listaOriginalContatos.get(i);
                    if (!contato.getTelefone().replace("(","").replace(")","").replace("-","").contains(conteudoBusca.toUpperCase())) {
                        listaOriginalContatos.remove(contato);
                    }
                }
            }else {
                for (int i = listaOriginalContatos.size() - 1; i >= 0; i--) {
                    Contato contato = listaOriginalContatos.get(i);
                    if (!contato.getNome().toUpperCase().contains(conteudoBusca.toUpperCase())) {
                        listaOriginalContatos.remove(contato);
                    }
                }
            }

            listaContados.setAdapter(new ContatoAdapter(this, listaOriginalContatos));
        }
    }

    private void enviarContatosNaoSincronizados(Long idUsuario){

        List<Contato> listaContatoNaoSincronizado = controladorContato.buscarListaContatosNaoSincronizados(idUsuario);

        if (listaContatoNaoSincronizado != null && listaContatoNaoSincronizado.size() > 0){ContatosService service = ServiceGenerator.createService(ContatosService.class);
            for(final Contato contato: listaContatoNaoSincronizado) {
                if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_CRIADO) {
                    cadastrarContatoRemotamente(contato);
                }else
                if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_ALTERADO){
                    atualizarContatoRemotamente(contato);
                }
            }
        }

        buscarContatoNaoSincronizadoWeb(idUsuario);
    }


    private void buscarContatoNaoSincronizadoWeb(Long idUsuario){


        ContatosService service = ServiceGenerator.createService(ContatosService.class);

        Call<List<Contato>> call = service.buscarContatosNaoSincronizados(idUsuario);

        call.enqueue(new Callback<List<Contato>>() {

            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {

                if (response.isSuccessful()){
                    List<Contato> listaContatos = response.body();
                    if (listaContatos != null && listaContatos.size() > 0) {
                        buscarContatoLocalmente(listaContatos.get(0).getIdUsuario(), listaContatos);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro: " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cadastrarContatoRemotamente(Contato contato){
        ContatosService service = ServiceGenerator.createService(ContatosService.class);
        Call<Contato> call = service.cadastrarContato(contato);
        call.enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                if (response.isSuccessful()) {
                    Contato contatoRecuperado = response.body();

                    if (contatoRecuperado != null && contatoRecuperado.getCodigo() != null && contatoRecuperado.getCodigo() > 0) {
                        atualizarStatusSicronizacao(contatoRecuperado);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro: " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void buscaTodosContatosRemotamente(Long idUsuario){
        ContatosService service = ServiceGenerator.createService(ContatosService.class);

        Call<List<Contato>> call = service.buscarContatos(idUsuario);

        call.enqueue(new Callback<List<Contato>>() {

            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {

                if (response.isSuccessful()){
                    List<Contato> listaContatos = response.body();
                    if (listaContatos != null && listaContatos.size() > 0){
                        List<Contato> contatosGravados = gravaContatosRecuperadoAposPrimeiroLogin(listaContatos.get(0).getIdUsuario(),listaContatos);
                        atualizaAdapter(contatosGravados);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
            }
        });
    }

    private void atualizarContatoRemotamente(Contato contato) {
        ContatosService service = ServiceGenerator.createService(ContatosService.class);
        Call<Contato> call = service.editarContato(contato);
        call.enqueue(new Callback<Contato>() {
            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {
                if (response.isSuccessful()) {
                    Contato contatoRecuperado = response.body();

                    if (contatoRecuperado != null && contatoRecuperado.getCodigo() != null && contatoRecuperado.getCodigo() > 0) {
                         atualizarStatusSicronizacao(contatoRecuperado);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
            }
        });
    }

    private void  atualizarStatusSicronizacao(Contato contato){
        contato.setSincronizado(Constantes.SINCRONIZADO);
        controladorContato.atualizaContato(contato);
    }


    /**
     * Função: realizar a busca de contatos no webservice.
     *
     * @param idUsuario
     */
    private void buscaContatoWebService(final Long idUsuario){
        ContatosService service = ServiceGenerator.createService(ContatosService.class);

        Call<List<Contato>> call = service.buscarContatos(idUsuario);

        call.enqueue(new Callback<List<Contato>>() {

            Long idUser = idUsuario;
            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {

                if (response.isSuccessful()){
                    List<Contato> listaContatos = response.body();
                    buscarContatoLocalmente(idUser,listaContatos);
                }
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
            }
        });
    }

    /**
     * Função: esse método tem como objetivo recuperar a lista de contatos para um determinado usuário
     *         Ele recebe como parâmetro o identificado o usuário e a lista de contatos
     *         (@listaContatosRemotos ) novo ou  editados que foram recuperado do webservice.
     *         Caso a @listaContatosRemotos seja  nula, indica que não há conexão com a internet.
     *
     * @param idUsuario
     * @param listaContatosRemotos
     */
    private void buscarContatoLocalmente(long idUsuario ,List<Contato> listaContatosRemotos){
        //Representa os contatos que foram recuperados da base de dado local
        this.contatos = controladorContato.buscarListaContatos(idUsuario);

        /**
         * Caso o retorno do webserice possua registroentão é verificado se existe contato novo ou
         * editado que foi criado/atualizado na web.
         */
        if (listaContatosRemotos != null){
            this.contatos = atualizaListaRegistros(idUsuario,contatos,listaContatosRemotos);
        }

        atualizaAdapter(contatos);
    }

    /**
     * Função: verifica se existe registro que venho do webservice e que tenha sido alterado ou criado.
     *         Para registro alterado ele é inserido e para registro alterado ele é atualizado;
     *
     * @param contatosRecuperadoLocalmente
     * @param contatosRecuperadoWebservice
     * @return
     */
    private List<Contato> atualizaListaRegistros(long idUsuario, List<Contato> contatosRecuperadoLocalmente,
                                                  List<Contato> contatosRecuperadoWebservice){

        for(Contato contato: contatosRecuperadoWebservice){
            if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_ALTERADO) {
                contato.setSincronizado(Constantes.SINCRONIZADO);
                controladorContato.atualizaContato(contato);
            }else
            if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_CRIADO) {
                contato.setSincronizado(Constantes.SINCRONIZADO);
                controladorContato.adicionarContato(contato);
            }
        }

        return controladorContato.buscarListaContatos(idUsuario);
    }

    private List<Contato> gravaContatosRecuperadoAposPrimeiroLogin(long idUsuario, List<Contato> contatosRecuperadoWebservice){

        for(Contato contato: contatosRecuperadoWebservice){
           contato.setSincronizado(Constantes.SINCRONIZADO);
           controladorContato.adicionarContato(contato);
        }

        return controladorContato.buscarListaContatos(idUsuario);
    }
}
