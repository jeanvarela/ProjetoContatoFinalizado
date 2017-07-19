package br.com.projeto.modelo.repositorio.contato;

import java.util.List;

import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.util.dao.IGenericDAO;

public interface IRepositorioContato  extends IGenericDAO<Contato> {
	
	/**
	 * Função: recuperar os contatos de um usuário.
	 */
	public List<Contato> recuperaContatos(Long idUsuario, String criterioBusca, int tipoBusca);
	
	/**
	 * Função: recuperar a lista de contatos de um determinado usuário.
	 */
	public List<Contato> recuperaContatos(Long idUsuario);

	/**
	 * Função: recuperar um contato.
	 */
	public Contato recuperaContato(Long idContato);
}
