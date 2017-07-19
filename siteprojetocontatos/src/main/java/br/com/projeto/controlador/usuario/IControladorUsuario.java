package br.com.projeto.controlador.usuario;

import br.com.projeto.modelo.entidade.usuario.Usuario;

public interface IControladorUsuario {
	
	public long adicionaUsuario(Usuario usuario);

	public boolean atualizarUsuario(Usuario usuario);
	
	public boolean verificaEmailCadastrado(String email);
}
