package br.com.contatos.modelo.repositorio.usuario;

import java.util.List;

import br.com.contatos.modelo.entidade.usuario.Usuario;

/**
 * Created by JEAN on 21/05/2017.
 */

public interface IRepositorioUsuario {

    public long inserirUsuario(Usuario usuario);

    public void registraLogin(long idUsuario, int logado);

    public Usuario recuperaUsuario(String email, String senha);

    public Usuario recuperaUsuarioLogado();

    public List<Usuario> recuperaUsuarios();
}
