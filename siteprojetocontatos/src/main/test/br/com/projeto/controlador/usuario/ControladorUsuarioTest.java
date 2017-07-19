package br.com.projeto.controlador.usuario;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.modelo.repositorio.usuario.IRepositorioUsuario;
import br.com.projeto.modelo.repositorio.usuario.RepositorioUsuario;
import br.com.util.contante.Constantes;


public class ControladorUsuarioTest {
	
	private IRepositorioUsuario repositorioUsuario;
	
	@Before
	public void inicializaComponente(){
		this.repositorioUsuario = new RepositorioUsuario();
	}
	
	@Test
	public void acidionaUsuario(){
		Usuario usuario = new Usuario();
		usuario.setNome("Usuario teste");
		usuario.setEmail("teste@hotmail.com");
		usuario.setSenha("senha");
		usuario.setSincronizado(Constantes.INFORMACAO_NAO_SINCRONIZADA);
		
		long idUsuarioInserido = repositorioUsuario.salvar(usuario);
		
		assertTrue(idUsuarioInserido > 0);
	}

}
