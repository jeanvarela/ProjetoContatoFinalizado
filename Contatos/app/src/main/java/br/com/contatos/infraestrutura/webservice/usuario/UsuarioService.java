package br.com.contatos.infraestrutura.webservice.usuario;

import br.com.contatos.modelo.entidade.usuario.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author JEAN.
 *
 * Função: essa interface tem como objetivo expor os recuros do webservice usuario
 */

public interface UsuarioService {

    @POST("usuario/cadastrarUsuario")
    Call<Long> cadastrarUsuario(@Body Usuario usuario);

    @GET("usuario/verificaEmail/{email}")
    public Call<Boolean> verificaEmailExitente(@Path("email") String email);

    @GET("usuario/logar/{email}/{senha}")
    public Call<Usuario> logar(@Path("email") String email, @Path("senha") String senha);

}
