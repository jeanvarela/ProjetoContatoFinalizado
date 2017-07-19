package br.com.projeto.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.projeto.controlador.login.ControladorLogin;
import br.com.projeto.controlador.login.IControladorLogin;
import br.com.projeto.controlador.usuario.ControladorUsuario;
import br.com.projeto.controlador.usuario.IControladorUsuario;
import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.util.constante.Constantes;


@Path("/usuario")
public class UsuarioWS {

    @GET
    @Path("/logar/{email}/{senha}")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public Usuario logar(@PathParam("email") String login,@PathParam("senha") String senha){
     	IControladorLogin controladorLogin = new  ControladorLogin();

    	return controladorLogin.recuperarUsuario(login, senha);
    }
    
    @GET
    @Path("/verificaEmail/{email}")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public boolean verificarEmailCadastrado(@PathParam("email") String email) {
     	IControladorUsuario controladoUsuario = new ControladorUsuario();
     	
     	return controladoUsuario.verificaEmailCadastrado(email);
    }
    
    
    @POST
    @Path("/cadastrarUsuario")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public Long cadastrarUsuario(Usuario usuario) {
       	IControladorUsuario controladorUsuario = new ControladorUsuario();
		
    		//controladorUsuario.verificaEmailCadastrado(usuario.getEmail());
    		usuario.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
    		
    		return controladorUsuario.adicionaUsuario(usuario);
   
    }
    
}
