package br.com.projeto.modelo.repositorio.usuario;

import java.util.List;

import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.util.dao.IGenericDAO;

public interface IRepositorioUsuario extends IGenericDAO<Usuario>{
	
	public List<Usuario> logar(String login, String senha);
	
	public List<Usuario> recuperaUsuarioPorEmail(String email); 

}
