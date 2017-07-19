package br.com.contatos.controlador.contato;

import android.content.Context;

import java.util.List;

import br.com.contatos.modelo.entidade.contato.Contato;
import br.com.contatos.modelo.repositorio.contato.IRepositorioContato;
import br.com.contatos.modelo.repositorio.contato.RepositorioContato;

/**
 * Created by JEAN on 02/06/2017.
 */

public class ControladorContato implements IControladorContato {

    private IRepositorioContato repositorioContato;

    public ControladorContato(Context context){
        this.repositorioContato = new RepositorioContato(context);
    }

    @Override
    public long adicionarContato(Contato contato) {
        return repositorioContato.adicionarContato(contato);
    }

    @Override
    public List<Contato> buscarListaContatos(Long idUsuario) {
        return repositorioContato.buscarListaContatos(idUsuario);
    }

    @Override
    public Contato recuperaContato(long idContato) {
        return repositorioContato.recuperaContato(idContato);
    }

    @Override
    public boolean atualizaContato(Contato contato) {
        return repositorioContato.atualizaContato(contato) > 0 ? true : false;
    }

    @Override
    public List<Contato> buscarListaContatosNaoSincronizados(Long idUsuario) {
        return repositorioContato.buscarListaContatosNaoSincronizados(idUsuario);
    }

    @Override
    public void atualizaStatusSincronizacao(long idContato, int statusSincronizacao){
        repositorioContato.atualizaStatusSincronizacao(idContato,statusSincronizacao);
    }

    @Override
    public long recuperaIdentificadoNovoContato(long idUsuario) {
        return repositorioContato.recuperaIdentificadoNovoContato(idUsuario) + 1;
    }
}
