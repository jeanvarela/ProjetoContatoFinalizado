package br.com.contatos.visao.usuario;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import br.com.contatos.R;
import br.com.contatos.controlador.usuario.ControladorUsuario;
import br.com.contatos.controlador.usuario.IControladorUsuario;
import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.infraestrutura.util.Util;
import br.com.contatos.infraestrutura.util.UtilValidacao;
import br.com.contatos.infraestrutura.webservice.ServiceGenerator;
import br.com.contatos.infraestrutura.webservice.usuario.UsuarioService;
import br.com.contatos.modelo.entidade.usuario.Usuario;
import br.com.contatos.visao.login.LoginActivity;
import br.com.contatos.visao.principal.PrincipalActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroUsuario extends AppCompatActivity {

    private TextInputLayout txtNomeLayout;
    private EditText        txtNome;

    private TextInputLayout txtEmailLayout;
    private EditText        txtEmail;

    private TextInputLayout txtSenhaLayout;
    private EditText        txtSenha;

    private TextInputLayout txtSenhaConfirmacaoLayout;
    private EditText        txtSenhaConfirmacao;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtNomeLayout = (TextInputLayout) findViewById(R.id.txtNomeLayout);
        txtNome = (EditText) findViewById(R.id.txtNome);

        txtEmailLayout = (TextInputLayout) findViewById(R.id.txtEmailLayout);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        txtSenhaLayout = (TextInputLayout) findViewById(R.id.txtSenhaLayout);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        txtSenhaConfirmacaoLayout = (TextInputLayout) findViewById(R.id.txtSenhaConfirmacaoLayout);
        txtSenhaConfirmacao = (EditText) findViewById(R.id.txtSenhaConfirmacao);
    }

    public void cadastrarUsuario(View v){
        if (validarInformacoes()){
            usuario = new Usuario();
            usuario.setNome(txtNome.getText().toString());
            usuario.setEmail(txtEmail.getText().toString());
            usuario.setSenha(txtSenha.getText().toString());

            if (Util.verificaConectado(getApplicationContext())){
               verificaEmailExistente();
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

    /**
     * Created by JEAN on 21/05/2017.
     * <p>
     * Função: validar  se os campos foram preenchidos de maneira correta
     *
     * @return Retorna 'true' se os campos foram preenchidos de maneira correta, caso contrário retorna 'false'.
     */
    private boolean validarInformacoes(){

        boolean informacaoValida = true;

        if (UtilValidacao.verificaCampoTextoNaoVazio(txtNome.getText().toString())){
            txtNomeLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtNomeLayout.setError(getString(R.string.nomeNaoPreenchido));
        }

        String email = txtEmail.getText().toString();
        if (UtilValidacao.verificaCampoTextoNaoVazio(email)){

            if (UtilValidacao.validaEmail(email)){
                txtEmailLayout.setErrorEnabled(false);
            }else{
                informacaoValida = false;
                txtEmailLayout.setError(getString(R.string.emailInvalido));
            }
        }else{
            informacaoValida = false;
            txtEmailLayout.setError(getString(R.string.emailNaoPreenchido));
        }

        String senha = txtSenha.getText().toString() ;
        if (UtilValidacao.verificaCampoTextoNaoVazio(senha)){
            txtSenhaLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtSenhaLayout.setError(getString(R.string.senhaNaoPreenchida));
        }

        String senhaConfirmacao = txtSenhaConfirmacao.getText().toString() ;
        if (UtilValidacao.verificaCampoTextoNaoVazio(senhaConfirmacao)){
            txtSenhaConfirmacaoLayout.setErrorEnabled(false);
        }else{
            informacaoValida = false;
            txtSenhaConfirmacaoLayout.setError(getString(R.string.senhaConfirmacaoNaoPreenchida));
        }

        if (senha.equals(senhaConfirmacao)){
            txtSenhaConfirmacaoLayout.setErrorEnabled(false);
        }else {
            informacaoValida = false;
            txtSenhaConfirmacaoLayout.setError(getString(R.string.senhaDiferenteSenhaConfirmacao));
        }

        return informacaoValida;
    }

    /**
     * Função: metódo utilizado para chamar a tela principal, é passado id do usuáro que acabo de ser
     *         cadastrado;
     * @param
     */
    private void chamaTelaPrincipal(){
        Intent intent = new Intent(this, PrincipalActivity.class);
        intent.putExtra(Constantes.ID_USUARIO,usuario.getCodigo());
        startActivity(intent);
        finish();
    }

    private void verificaEmailExistente(){
        UsuarioService service = ServiceGenerator.createService(UsuarioService.class);

        Call<Boolean> call = service.verificaEmailExitente(usuario.getEmail());

        final Usuario usuarioNovo = new Usuario();

        call.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()){
                    Boolean emailExiste = response.body();

                    if (!emailExiste){
                        try {
                            enviaUsuarioParaServidor();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        exibeMensagemEmailExistente();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    private void enviaUsuarioParaServidor() throws IOException {
        UsuarioService service = ServiceGenerator.createService(UsuarioService.class);

        Call<Long> call = service.cadastrarUsuario(usuario);


        call.enqueue(new Callback<Long>() {

            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {

                if (response.isSuccessful()){
                    Long idUsuarioInserido = response.body();

                    if (idUsuarioInserido != null && idUsuarioInserido > 0) {
                        gravaUsuarioLocalmente(idUsuarioInserido);
                    }
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro: " + t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void gravaUsuarioLocalmente(long codigoUsuario){
        usuario.setCodigo(codigoUsuario);
        usuario.setSincronizado(Constantes.SINCRONIZADO);

        IControladorUsuario controladorUsuario = new ControladorUsuario(getApplicationContext());
        long idUsuarioInserido = controladorUsuario.cadastraUsuario(usuario);

        if (idUsuarioInserido != -1){
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle(getString(R.string.cadastro));
            dialogo.setMessage(getString(R.string.cadastroRealizadoComSucesso));
            dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    chamaTelaPrincipal();
                }
            });
            dialogo.create().show();
        }
    }

    /**
     * Função: mensagem exibida para indicar que o e-mail ja é cadastrado para outro usuario
     */
    private void exibeMensagemEmailExistente(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(getString(R.string.cadastro));
        dialogo.setMessage(getString(R.string.emailExistente));
        dialogo.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        dialogo.create().show();
    }

    public void chamarTelaLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
