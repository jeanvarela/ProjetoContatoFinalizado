package br.com.projeto.controlador.contato;

import java.util.List;

import br.com.projeto.modelo.entidade.contato.Contato;

public interface IControladorContato {

	/**
	 * Função: inserir um novo contato. Recebe como paramentro o objeto contato
	 * e retorna um objeto contato com o identificador.
	 */
	public Contato insereContato(Contato contato);
	
	/**
	 * Função: recuperar a lista de contatos de um determinado usuário.
	 * 
	 * Parametros: @idUsuario: o identificador do usuário
	 *             @criterioBusca: a string de busca
	 *             @tipoBusca: indica se a busca é por nome ou por celular. Valor 1 indica 
	 *                         pesquisar por NOME e valor 2 indica pesquisa por celular.    
	 */
	public List<Contato> recuperaContatos(Long idUsuario, String criterioBusca, int tipoBusca);
	
	/**
	 * Função: recuperar a lista de todos os contatos de um determinado usuário.
	 */
	public List<Contato> recuperaContatoPorUsuario(Long idUsuario);
	
	/**
	 * Função: recuperar um determinado contato.
	 */
	public Contato recuperaContato(long idContato);
	
	/**
	 * Função: atualizar as informaçoes de um determinado contato.
	 */
	public Contato atualizaContato(Contato contato);
}
