package br.com.projeto.modelo.repositorio.contato;

import java.util.List;

import javax.persistence.Query;

import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.util.dao.GenericDAO;
import br.com.projeto.util.enumeracao.TipoBuscaContato;

public class RepositorioContato extends GenericDAO<Contato> implements IRepositorioContato {

	/**
	 * Função: recuperar os contatos de um usuário.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Contato> recuperaContatos(Long idUsuario, String criterioBusca, int tipoBusca) {
		
		StringBuffer sql = new StringBuffer("from Contato where ");
		
		if (tipoBusca == TipoBuscaContato.PESQUISA_NOME.getValor()){
			sql.append("lower(nome) ");
		}else{
			sql.append("lower(telefone) ");
		}
		
		sql.append("like ");
		sql.append(":criterio");
		sql.append(" and ");
		sql.append("usuario_codigo = :idUsuario");
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("criterio","%"+criterioBusca+"%");
		query.setParameter("idUsuario",idUsuario);
		
		return (List<Contato>)query.getResultList();
	}


	/**
	 * Função: recuperar a lista de contatos de um determinado usuário.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Contato> recuperaContatos(Long idUsuario){
		
		StringBuffer sql = new StringBuffer("from Contato where ");
		sql.append("usuario_codigo = :idUsuario");
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("idUsuario",idUsuario);
		
		return (List<Contato>)query.getResultList();
	}
	
	/**
	 * Função: recuperar um contato.
	 */
	@Override
	public Contato recuperaContato(Long idContato){
		
		StringBuffer sql = new StringBuffer("from Contato where ");
		sql.append("codigo = :idContato");
		
		Query query = getEntityManager().createQuery(sql.toString());
		query.setParameter("idContato",idContato);
		
		return (Contato)query.getSingleResult();
	}

}
