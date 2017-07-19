package br.com.projeto.modelo.repositorio.usuario;

import java.util.List;

import javax.persistence.Query;

import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.util.dao.GenericDAO;

public class RepositorioUsuario extends GenericDAO<Usuario> implements IRepositorioUsuario{

	@Override
	public List<Usuario> logar(String login, String senha) {

		Query query = getEntityManager().createQuery("from Usuario where " + 
												     "email = :emailUsuario and senha = :senhaUsuario");
		query.setParameter("emailUsuario", login);
		query.setParameter("senhaUsuario", senha);
		
		return (List<Usuario>)query.getResultList();
	}

	@Override
	public List<Usuario> recuperaUsuarioPorEmail(String email) {
		Query query = getEntityManager().createQuery("from Usuario where email = :emailUsuario");
		query.setParameter("emailUsuario", email);
		
		return (List<Usuario>) query.getResultList();
	}
	



}
