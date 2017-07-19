package br.com.contatos.visao.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import br.com.contatos.R;
import br.com.contatos.controlador.login.ControladorLogin;
import br.com.contatos.controlador.login.IControladorLogin;
import br.com.contatos.controlador.usuario.ControladorUsuario;
import br.com.contatos.controlador.usuario.IControladorUsuario;
import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.infraestrutura.util.Util;
import br.com.contatos.infraestrutura.util.UtilValidacao;
import br.com.contatos.infraestrutura.webservice.ServiceGenerator;
import br.com.contatos.infraestrutura.webservice.usuario.UsuarioService;
import br.com.contatos.modelo.entidade.usuario.Usuario;
import br.com.contatos.visao.principal.PrincipalActivity;
import br.com.contatos.visao.usuario.CadastroUsuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Activity responsavel por gerenciar a açoes relacionada ao login na aplicaçao
 *
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout txtEmailLayout;
    private EditText        txtEmail;

    private TextInputLayout txtSenhaLayout;
    private EditText        txtSenha;

    private IControladorLogin controladorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmailLayout = (TextInputLayout) findViewById(R.id.txtEmailLayout);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        txtSenhaLayout = (TextInputLayout) findViewById(R.id.txtSenhaLayout);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        controladorLogin = new ControladorLogin(this);
    }


    @Override
    protected void onResume() {
        long idUsuarioLogado = controladorLogin.verificarExistenciaUsuarioLogado();

        if (idUsuarioLogado > 0){
            chamaTelaPrincipal(idUsuarioLogado,false);
        }

        super.onResume();
    }

    /**
     * Função: Realizar login na aplicaçao
     *         Primeiro pesquisa se o registro do usuário existe localmente.
     *         Se não exitir localmente realiza a pesquisa no webservice.
     *
     * @param v
     */
    public void realizarLogin(View v){
        if (validaInformacoes()){
            String email = txtEmail.getText().toString();
            String senha = txtSenha.getText().toString();
            long idUsuario = controladorLogin.logar(email,senha);

            if (idUsuario != Constantes.REGISTRO_NAO_ENCONTRADO){
                chamaTelaPrincipal(idUsuario,false);
            }else
            if (Util.verificaConectado(getApplicationContext())){
                pesquisaUsuarioRemotamente(email,senha);
            } else{
                exibeMensagemLoginSemSucesso();
            }
        }
    }

    /**
     * Funçao: Realiza a pesquisa do usuário no webservice
     *
     * @param email
     * @param senha
     */
    private void pesquisaUsuarioRemotamente(String email, String senha){
        UsuarioService service = ServiceGenerator.createService(UsuarioService.class);

        Call<Usuario> call = service.logar(email,senha);

        call.enqueue(new Callback<Usuario>() {

            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful()){
                    Usuario usuario = response.body();

                    if (usuario !=  null && usuario.getCodigo() > 0){
                        insereUsuarioLocalmente(usuario);
                    }else{
                        exibeMensagemLoginSemSucesso();
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro: " + t.getCause(),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    /**
     * Função: realiza a pesquisa pelo usuario na base  de dados local
     *
     * @param usuario
     */
    public void insereUsuarioLocalmente(Usuario usuario){
        IControladorUsuario controladorUsuario = new ControladorUsuario(this);
        usuario.setLogado(Constantes.LOGADO);

        long idUsuario = controladorUsuario.cadastraUsuario(usuario);
        if (idUsuario != -1) {
            chamaTelaPrincipal(usuario.getCodigo(),true);
        }
    }

    /**
     * Função: Exibe a mensagem indicando que o login não foi realizado.
     *
     */
    private void exibeMensagemLoginSemSucesso(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(getString(R.string.login));
        dialogo.setMessage(getString(R.string.loginSemSucesso));
        dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        dialogo.create().show();
    }

    /**
     *
     * Função: redirecionar a execução para a tela principal
     *
     * @param idUsuario
     */
    private void chamaTelaPrincipal(long idUsuario,boolean finalizouCadastro){
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(Constantes.ID_USUARIO,idUsuario);
        intent.putExtra(Constantes.FINALIZOULOGIN,true);
        intent.putExtra(Constantes.FINALIZOUCADASTRO,finalizouCadastro);
        startActivity(intent);
        finish();
    }

    /**
     * Função: verificar se o e-mail e senha foram digitados corretamente
     *
     * @return
     */
    public boolean validaInformacoes(){
        boolean informacaoValida = true;

        if (UtilValidacao.verificaCampoTextoNaoVazio(txtEmail.getText().toString())){
            txtEmailLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtEmailLayout.setError(getString(R.string.emailNaoPreenchido));
        }

        if (UtilValidacao.verificaCampoTextoNaoVazio(txtSenha.getText().toString())){
            txtSenhaLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtSenhaLayout.setError(getString(R.string.senhaNaoPreenchida));
        }

        return informacaoValida;
    }

    /**
     * Funçao: Chama a tela para cadastrar usuário
     *
     * @param v
     */
    public void cadastrarUsuario(View v){
        if (Util.verificaConectado(getApplicationContext())){
            Intent intent = new Intent(this, CadastroUsuario.class);
            startActivity(intent);
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
}
