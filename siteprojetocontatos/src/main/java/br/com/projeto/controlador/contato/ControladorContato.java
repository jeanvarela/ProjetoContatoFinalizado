package br.com.projeto.controlador.contato;

import java.util.List;

import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.modelo.repositorio.contato.IRepositorioContato;
import br.com.projeto.modelo.repositorio.contato.RepositorioContato;

public class ControladorContato implements IControladorContato {

	private IRepositorioContato repositorioContato;
	
	public ControladorContato() {
		this.repositorioContato = new RepositorioContato();
	}

	/**
	 * Função: inserir um novo contato. Recebe como paramentro o objeto contato
	 * e retorna um objeto contato com o identificador.
	 */
	@Override
	public Contato insereContato(Contato contato) {
		return (Contato) this.repositorioContato.salvar(contato);
	}

	/**
	 * Função: recuperar a lista de contatos de um determinado usuário.
	 * 
	 * Parametros: @idUsuario: o identificador do usuário
	 *             @criterioBusca: a string de busca
	 *             @tipoBusca: indica se a busca é por nome ou por celular. Valor 1 indica 
	 *                         pesquisar por NOME e valor 2 indica pesquisa por celular.    
	 */
	@Override
	public List<Contato> recuperaContatos(Long idUsuario, String criterioBusca, int tipoBusca) {
		return this.repositorioContato.recuperaContatos(idUsuario, criterioBusca, tipoBusca);
	}

	/**
	 * Função: recuperar a lista de todos os contatos de um determinado usuário.
	 */
	@Override
	public List<Contato> recuperaContatoPorUsuario(Long idUsuario) {
		return repositorioContato.recuperaContatos(idUsuario);
	}

	/**
	 * Função: recuperar um determinado contato.
	 */
	@Override
	public Contato recuperaContato(long idContato) {
		return repositorioContato.recuperaContato(idContato);
	}

	/**
	 * Função: atualizar as informaçoes de um determinado contato.
	 */
	@Override
	public Contato atualizaContato(Contato contato) {
		return (Contato) repositorioContato.editar(contato);
	}

}
