package br.com.projeto.repositorio.usuario;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.modelo.repositorio.usuario.IRepositorioUsuario;
import br.com.projeto.modelo.repositorio.usuario.RepositorioUsuario;

public class RepositorioUsuarioTest {

	@Test
	@Ignore
	public void validaLoginSenha(){
		String email = "jean.varela@hotmail.com";
		String senha = "jean";
		
		IRepositorioUsuario repositorioUsuario = new RepositorioUsuario();
		List<Usuario> usuario = repositorioUsuario.logar(email, senha);
	
		assertNotNull(usuario);		
	}
	
	@Test
	public void  verificaEmailExistente(){
		String email = "jean.varela@hotmail.com";
		
		IRepositorioUsuario repositorioUsuario = new RepositorioUsuario();
		List<Usuario> listaUsuarios = repositorioUsuario.recuperaUsuarioPorEmail(email);
		
		assertNotNull(listaUsuarios);
	}
}
