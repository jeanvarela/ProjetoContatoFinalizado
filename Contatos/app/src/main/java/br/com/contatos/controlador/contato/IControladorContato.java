package br.com.contatos.controlador.contato;

import java.util.List;

import br.com.contatos.modelo.entidade.contato.Contato;

/**
 * Created by JEAN on 02/06/2017.
 */

public interface IControladorContato {

    public long adicionarContato(Contato contato);

    public List<Contato> buscarListaContatos(Long idUsuario);

    public Contato recuperaContato(long idContato);

    public boolean  atualizaContato(Contato contato);

    public List<Contato> buscarListaContatosNaoSincronizados(Long idUsuario);

    public void atualizaStatusSincronizacao(long idContato, int statusSincronizacao);

    public long recuperaIdentificadoNovoContato(long idUsuario);
}
