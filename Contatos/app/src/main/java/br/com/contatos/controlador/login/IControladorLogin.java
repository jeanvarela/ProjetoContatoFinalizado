package br.com.contatos.controlador.login;

/**
 * Created by JEAN on 31/05/2017.
 */

public interface IControladorLogin {

    public long logar(String email, String senha);

    public void registraLogin(long idUsuario, int logado);

    public long verificarExistenciaUsuarioLogado();
}
