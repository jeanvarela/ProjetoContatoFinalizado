package br.com.contatos.controlador.login;

import android.content.Context;

import br.com.contatos.infraestrutura.util.Constantes;
import br.com.contatos.modelo.entidade.usuario.Usuario;
import br.com.contatos.modelo.repositorio.usuario.IRepositorioUsuario;
import br.com.contatos.modelo.repositorio.usuario.RepositorioUsuario;

/**
 * Created by JEAN on 31/05/2017.
 */

public class ControladorLogin implements IControladorLogin {

    private IRepositorioUsuario repositorioUsuario;


    public ControladorLogin(Context context){
        this.repositorioUsuario = new RepositorioUsuario(context);
    }


    @Override
    public long logar(String email, String senha) {
        Usuario usuario = repositorioUsuario.recuperaUsuario(email,senha);

        if (usuario != null){
            registraLogin(usuario.getCodigo(),Constantes.LOGADO);
            return usuario.getCodigo();
        }

        return  Constantes.REGISTRO_NAO_ENCONTRADO;
    }

    @Override
    public void registraLogin(long idUsuario, int logado) {
        repositorioUsuario.registraLogin(idUsuario,logado);
    }

    @Override
    public long verificarExistenciaUsuarioLogado() {
        Usuario usuario = repositorioUsuario.recuperaUsuarioLogado();

        if (usuario != null && usuario.getCodigo() != null){
            return usuario.getCodigo();
        }

        return Constantes.NAO_LOGADO;
    }

}
