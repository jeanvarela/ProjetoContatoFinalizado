package br.com.projeto.modelo.repositorio.contato;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.util.enumeracao.TipoBuscaContato;

public class RepositorioContatoTest {

	@Test
	@Ignore
	public void inserir(){
		Contato contato = new Contato();
		contato.setNome("Jean Varela");
		contato.setTelefone("(81)9565-9865");
		
		IRepositorioContato repositorioContato = new RepositorioContato();
		long idContato = repositorioContato.salvar(contato);
		
		assertTrue(idContato > 0);
	}
	
	@Test
	public void buscarContatos(){
		IRepositorioContato repositorioContato = new RepositorioContato();
		
		List<Contato> contatos = repositorioContato.listar(null);
		
		assertNotNull(contatos);
		assertTrue(contatos.size() > 0);
	}
	
	@Test
	public void buscaContatoPorNome(){
		IRepositorioContato repositorioContato = new RepositorioContato();
		
		List<Contato> contatos = repositorioContato.recuperaContatos(new Long(1),"jean", TipoBuscaContato.PESQUISA_NOME.getValor());

		assertNotNull(contatos);
		assertTrue(contatos.size() > 0);
	}
}
