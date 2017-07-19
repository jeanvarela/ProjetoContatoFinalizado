package br.com.projeto.controlador.login;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.com.projeto.modelo.entidade.usuario.Usuario;

public class LoginControladorTest {

	@Test
	public void logar(){
		String email = "jean.varela@hotmail.com";
		String senha = "jean";
		
		IControladorLogin controladorLogin = new ControladorLogin();
		Usuario usuarioLogado =  controladorLogin.logar(email, senha);
	
		assertNotNull(usuarioLogado);
	}
}
