package br.com.contatos.visao.contato;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.contatos.R;
import br.com.contatos.controlador.contato.ControladorContato;
import br.com.contatos.controlador.contato.IControladorContato;
import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.infraestrutura.util.Mask;
import br.com.contatos.infraestrutura.util.Util;
import br.com.contatos.infraestrutura.util.UtilValidacao;
import br.com.contatos.infraestrutura.webservice.ServiceGenerator;
import br.com.contatos.infraestrutura.webservice.contato.ContatosService;
import br.com.contatos.modelo.entidade.contato.Contato;
import br.com.contatos.visao.mapa.MapaActivity;
import br.com.contatos.visao.principal.PrincipalActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContatoActivity extends AppCompatActivity {

    private TextInputLayout txtNomeLayout;
    private EditText txtNome;

    private TextInputLayout txtTelefoneLayout;
    private EditText txtTelefone;

    private TextInputLayout txtLocalizacaoLayout;
    private EditText txtLocalizacao;

    private IControladorContato controladorContato;

    private Long idUsuario;
    private Long idContato;

    private Double latitude;
    private Double longitude;

    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_contato);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtNomeLayout = (TextInputLayout) findViewById(R.id.txtNomeLayout);
        txtNome = (EditText) findViewById(R.id.txtNome);

        txtTelefoneLayout = (TextInputLayout) findViewById(R.id.txtTelefoneLayout);
        txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        txtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", txtTelefone));

        txtLocalizacaoLayout = (TextInputLayout) findViewById(R.id.txtLocalizacaoLayout);
        txtLocalizacao = (EditText) findViewById(R.id.txtLocalizacao);

        Intent intent = getIntent();
        idUsuario = intent.getLongExtra(Constantes.ID_USUARIO,0);
        idContato = intent.getLongExtra(Constantes.ID_CONTATO,0);
        controladorContato = new ControladorContato(this);
        contato = new Contato();
        contato.setCodigo(idContato);
        boolean retornouTelaLocalizacao = intent.getBooleanExtra(Constantes.RETORNOUTELALOCALIZACAO,false);
        if (retornouTelaLocalizacao){

            contato.setCodigo(intent.getLongExtra(Constantes.ID_CONTATO,Constantes.REGISTRO_NAO_ENCONTRADO));
            contato.setNome(intent.getStringExtra(Constantes.NOME));
            contato.setTelefone(intent.getStringExtra(Constantes.TELEFONE));
            contato.setDescricaoLocalizacao(intent.getStringExtra(Constantes.DESCRICAOLOCALIZACAO));

            Bundle bundle = getIntent().getExtras();
            contato.setLongitude(bundle.getDouble(Constantes.LONGITUDE));
            contato.setLatitude(bundle.getDouble(Constantes.LATITUDE));
            longitude = bundle.getDouble(Constantes.LONGITUDE);
            latitude = bundle.getDouble(Constantes.LATITUDE);

            if (contato != null && contato.getCodigo() != null && contato.getCodigo() > 0){
                alteraTitulos(contato);
            }

            inicializaValores(contato);
        }else
        if (!retornouTelaLocalizacao || idContato > 0) {

            contato = controladorContato.recuperaContato(idContato);
            if (contato != null && contato.getCodigo() > 0){
                alteraTitulos(contato);
                inicializaValores(contato);
            }
        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constantes.ID_USUARIO,idUsuario);
                setResult(RESULT_OK, returnIntent);
                finish();
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private  void alteraTitulos(Contato contatoAtualizar){
        Button botaoAtualizar = (Button) findViewById(R.id.btnCadastrarAtualizar);
        botaoAtualizar.setText(getString(R.string.atualizar));
        getSupportActionBar().setTitle(getString(R.string.atualizarContato));

        if (contatoAtualizar.getLatitude() == 0 && contatoAtualizar.getLongitude() == 0) {
            TextView labelMensagem = (TextView) findViewById(R.id.labelAlertaLocalizacaoNaoSelecionada);
            labelMensagem.setVisibility(View.VISIBLE);
        }
    }

    private  void inicializaValores(Contato contato){
        txtNome.setText(contato.getNome());
        txtTelefone.setText(contato.getTelefone());
        txtLocalizacao.setText(contato.getDescricaoLocalizacao());
    }

    public void abrirMapa(View view){
        if (Util.verificaConectado(getApplicationContext())){
            Intent intent = new Intent(this,MapaActivity.class);
            intent.putExtra(Constantes.NOME,txtNome.getText().toString());
            intent.putExtra(Constantes.TELEFONE,txtTelefone.getText().toString());
            intent.putExtra(Constantes.DESCRICAOLOCALIZACAO,txtLocalizacao.getText().toString());
            intent.putExtra(Constantes.ID_USUARIO, idUsuario);

            if (contato != null) {
                intent.putExtra(Constantes.ID_CONTATO,idContato);

                Bundle bundle = new Bundle();
                bundle.putDouble(Constantes.LATITUDE, contato.getLatitude());
                bundle.putDouble(Constantes.LONGITUDE, contato.getLongitude());
                intent.putExtras(bundle);
            }

            startActivity(intent);
            finish();
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


    public void cadastrarContato(View view) {
        if (validaInformacoes()) {
            if (contato != null && contato.getCodigo() != null && contato.getCodigo() > 0) {
                contato.setSincronizado(Constantes.NAO_SINCRONIZADO_ALTERADO);
                contato.setIdUsuario(idUsuario);
                contato.setNome(txtNome.getText().toString());
                contato.setTelefone(txtTelefone.getText().toString());
                contato.setDescricaoLocalizacao(txtLocalizacao.getText().toString());

                if (Util.verificaConectado(this)) {
                    editarContatoRemotamente(contato);
                } else {
                    editarContatoLocalmente(contato);
                }
            } else {
                contato = new Contato();
                contato.setNome(txtNome.getText().toString());
                contato.setTelefone(txtTelefone.getText().toString());
                contato.setDescricaoLocalizacao(txtLocalizacao.getText().toString());
                contato.setSincronizado(Constantes.NAO_SINCRONIZADO_CRIADO);
                contato.setIdUsuario(idUsuario);

                latitude = (latitude != null) ? latitude : new Long(0);
                contato.setLatitude(latitude);

                longitude = (longitude != null) ? longitude : new Long(0);
                contato.setLongitude(longitude);

                if (Util.verificaConectado(this)) {
                    cadastrarContatoRemotamente(contato);
                } else {
                    cadastrarContatoLocalmente(contato);
                }
            }
        }
    }

    /**
     * Função: método responsável por validar as informaçoes do contato
     *
     * @return
     */
    public boolean validaInformacoes(){
        boolean informacaoValida = true;

        if (UtilValidacao.verificaCampoTextoNaoVazio(txtNome.getText().toString())){
            txtNomeLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtNomeLayout.setError(getString(R.string.nomeContatoNaoPreenchido));
        }

        if (UtilValidacao.verificaCampoTextoNaoVazio(txtLocalizacao.getText().toString())){
            txtLocalizacaoLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtLocalizacaoLayout.setError(getString(R.string.localizacaoContatoNaoPreenchida));
        }

        if (UtilValidacao.verificaCampoTextoNaoVazio(txtTelefone.getText().toString())){
            if (UtilValidacao.validaTelefone(txtTelefone.getText().toString())){
                txtTelefoneLayout.setErrorEnabled(false);
            }else{
                informacaoValida = false;
                txtTelefoneLayout.setError(getString(R.string.telefoneInvalido));
            }
        }else{
            informacaoValida = false;
            txtTelefoneLayout.setError(getString(R.string.telefoneNaoPreenchido));
        }

        return informacaoValida;
    }

    private void editarContatoRemotamente(Contato contato){
        ContatosService service = ServiceGenerator.createService(ContatosService.class);

        Call<Contato> call = service.editarContato(contato);

        call.enqueue(new Callback<Contato>() {

            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {

                if (response.isSuccessful()){
                    Contato contato = response.body();

                    if (contato !=  null && contato.getCodigo() > 0){
                        editarContatoLocalmente(contato);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {
            }
        });
    }

    private void editarContatoLocalmente(Contato contato){
        boolean atauli = controladorContato.atualizaContato(contato);

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(getString(R.string.contato));
        dialogo.setMessage(getString(R.string.atualizacaoRealizadaComSucesso));
        dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                chamaTelaPrincipal();
            }
        });
        dialogo.create().show();
    }

    private void cadastrarContatoRemotamente(Contato contato){
        ContatosService service = ServiceGenerator.createService(ContatosService.class);

        Call<Contato> call = service.cadastrarContato(contato);

        call.enqueue(new Callback<Contato>() {

            @Override
            public void onResponse(Call<Contato> call, Response<Contato> response) {

                if (response.isSuccessful()){
                    Contato contato = response.body();

                    if (contato !=  null && contato.getCodigo() > 0){
                        cadastrarContatoLocalmente(contato);
                    }
                }            }

            @Override
            public void onFailure(Call<Contato> call, Throwable t) {

             }
        });
    }

    private void cadastrarContatoLocalmente(Contato contato){
        if (contato.getCodigo() == null){
            contato.setCodigo(controladorContato.recuperaIdentificadoNovoContato(contato.getIdUsuario()));
        }

        long idContatoAdicionado = controladorContato.adicionarContato(contato);

        if (idContatoAdicionado > 0) {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(getString(R.string.contato));
            dialogo.setMessage(getString(R.string.cadastroRealizadoComSucesso));
            dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    chamaTelaPrincipal();
                }
            });
            dialogo.create().show();
        }
    }

    private void chamaTelaPrincipal(){
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(Constantes.ID_USUARIO,contato.getIdUsuario());
        startActivity(intent);
    }

}