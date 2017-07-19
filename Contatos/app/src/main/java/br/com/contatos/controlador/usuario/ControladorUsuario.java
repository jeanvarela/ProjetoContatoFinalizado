package br.com.contatos.controlador.usuario;

import android.content.Context;

import java.util.ResourceBundle;

import br.com.contatos.modelo.entidade.usuario.Usuario;
import br.com.contatos.modelo.repositorio.usuario.IRepositorioUsuario;
import br.com.contatos.modelo.repositorio.usuario.RepositorioUsuario;

/**
 * Created by JEAN on 21/05/2017.
 */

public class ControladorUsuario implements IControladorUsuario {

    private IRepositorioUsuario repositorioUsuario;

    public ControladorUsuario(Context contexto){
        repositorioUsuario = new RepositorioUsuario(contexto);
    }

    @Override
    public long cadastraUsuario(Usuario usuario) {
        return repositorioUsuario.inserirUsuario(usuario);
    }
}
