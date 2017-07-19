package br.com.contatos.infraestrutura.webservice.contato;

import java.util.List;

import br.com.contatos.modelo.entidade.contato.Contato;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by JEAN on 22/05/2017.
 */

public interface ContatosService {

    @GET("buscarTodosContatos")
    public Call<List<Contato>> buscarContatos();

    @GET("contato/buscarContatos/{idUsuario}")
    public Call<List<Contato>> buscarContatos(@Path("idUsuario") Long idUsuario);

    @POST("contato/adicionarContato")
    Call<Contato> cadastrarContato(@Body Contato contato);

    @POST("contato/atualizarContato")
    Call<Contato> editarContato(@Body Contato contato);

    @GET("contato/buscarContatosNaoSincronizados/{idUsuario}")
    public Call<List<Contato>> buscarContatosNaoSincronizados(@Path("idUsuario") Long idUsuario);

}