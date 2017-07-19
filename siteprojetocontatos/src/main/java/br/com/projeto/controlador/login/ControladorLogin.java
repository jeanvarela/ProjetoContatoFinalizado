package br.com.projeto.controlador.login;

import java.util.List;

import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.modelo.repositorio.usuario.IRepositorioUsuario;
import br.com.projeto.modelo.repositorio.usuario.RepositorioUsuario;

public class ControladorLogin implements IControladorLogin{
	
	private IRepositorioUsuario repositorioUsuario;
	
	public ControladorLogin() {
		this.repositorioUsuario  = new RepositorioUsuario();
	}

	/**
	 * Objetivo: verificar se a combinação email/senha existem no banco de dados
	 * 
	 * Parametro: login -> login de acesso do usuário
	 *            senha -> senha de acesso do usuário
	 *             
	 * Retorno: Retorna um boolean indicando se a combinação email/senha existe.
	 *          Se essa combinação existe é retornado true, caso contrário retorna false.
	 */
	@Override
	public Usuario logar(String login, String senha) {
		List<Usuario> listaUsuarios = this.repositorioUsuario.logar(login, senha);
		
		if (listaUsuarios != null &&  listaUsuarios.size() > 0){
			return listaUsuarios.get(0);
		}
				
		return null;
	}
	
	@Override
	public Usuario recuperarUsuario(String login, String senha) {
		List<Usuario> listaUsuarios = this.repositorioUsuario.logar(login, senha);
		
		if (listaUsuarios != null &&  listaUsuarios.size() > 0){
			return listaUsuarios.get(0);
		}
				
		return null;
	}

}
