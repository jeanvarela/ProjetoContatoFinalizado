package br.com.projeto.controlador.login;

import br.com.projeto.modelo.entidade.usuario.Usuario;

public interface IControladorLogin {

	public Usuario logar(String login, String senha);
	
	public Usuario recuperarUsuario(String login, String senha);
}
