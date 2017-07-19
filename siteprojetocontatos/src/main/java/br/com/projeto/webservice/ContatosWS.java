package br.com.projeto.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.projeto.controlador.contato.ControladorContato;
import br.com.projeto.controlador.contato.IControladorContato;
import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.util.constante.Constantes;
import br.com.projeto.util.converter.UtilConverter;
import br.com.projeto.webservice.adapter.ContatoAdapter;
import br.com.projeto.webservice.adapter.ListaContatosAdapter;

@Path("/contato")
public class ContatosWS {
	
	
    /**
     * Funçao: método utilizado para recuperar todos os contatos de um usuário.
     *         Esse método e utilizado caso o usuário realize login em um novo aparelho.
     *         Se caso o atributo sincronizado for igual a 2(registro criado e não sincronizados)
     *         ou 3(registro atualizado e nao sincronizado) ele e atualizado para 1(registro sincronizado).
     * 
     * @param idUsuario
     * @return lista de contatos de um usuário
     */
    @GET
    @Path("/buscarContatos/{idUsuario}")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public List<ContatoAdapter> buscarContatos(@PathParam("idUsuario") Long idUsuario){
         IControladorContato controlador = new ControladorContato(); 
         List<Contato> contatos =  controlador.recuperaContatoPorUsuario(idUsuario);
         
         for (Contato contato: contatos){
        	 if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_ALTERADO ||
            	 contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_CRIADO){

        		 contato.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
	        	 controlador.atualizaContato(contato);
        	 } 
       }
         
         return UtilConverter.converteParaListaAdapter(contatos);
    }
    
    @POST
    @Path("/adicionarContato")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public ContatoAdapter adicionarContato(ContatoAdapter contato){
     	contato.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
     	
     	if (contato.getCodigo() != null &&  contato.getCodigo() > 0){
     		contato.setCodigo(null);
     	}
     	  
    	IControladorContato controlador = new ControladorContato(); 
    	Contato contatoAdicionado = controlador.insereContato(UtilConverter.converterParaContato(contato));
    	 
    	return UtilConverter.converteParaAdpater(contatoAdicionado);
    }

    @POST
    @Path("/atualizarContato")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public ContatoAdapter atualizarContato(ContatoAdapter contato){
     	contato.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
   	  
    	IControladorContato controlador = new ControladorContato(); 
    	Contato contatoAtualizado = controlador.atualizaContato(UtilConverter.converterParaContato(contato));
    	     	 
    	return  UtilConverter.converteParaAdpater(contatoAtualizado);
    }
    
  
    
    @POST
    @Path("/enviarListaContato")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public List<ContatoAdapter> enviarListaContato(ListaContatosAdapter contatos){
    	IControladorContato controlador = new ControladorContato();     	
    	List<ContatoAdapter> contatosAtualizados = new ArrayList<ContatoAdapter>();
    	List<Contato> contatosRecebidos = contatos.getContatos();
    	for(Contato contato: contatosRecebidos){
    		if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_CRIADO){
    			contato.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
    			controlador.insereContato(contato);
    			contatosAtualizados.add(UtilConverter.converteParaAdpater(contato));
    		}else{
     			contato.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
    			controlador.atualizaContato(contato);
    			contatosAtualizados.add(UtilConverter.converteParaAdpater(contato));
    		}
    	}
    	     	 
    	return contatosAtualizados;
    }
    
    /**
     * Função: método utilizado para recuperar os contatos de um determinado usuario
     *         que foram inseridos/atualizados no site e não foram sincronizados para o aplicativo
     *         mobile. Para cada usuário não sincronizado é atualizado o campo sincronizado para 1, assim
     *         indicando que o contato foi sincronizados.
     * 
     * @param idUsuario
     * @return lista de contados não sincronizados.
     */
    @GET
    @Path("/buscarContatosNaoSincronizados/{idUsuario}")
    @Consumes("application/json; charset=UTF-8")
    @Produces("application/json; charset=UTF-8")
    public List<ContatoAdapter> buscarContatosNaoSincronizados(@PathParam("idUsuario") Long idUsuario){
         IControladorContato controlador = new ControladorContato(); 
         List<Contato> contatos =  controlador.recuperaContatoPorUsuario(idUsuario);
         List<ContatoAdapter> contatosRecuperados = new ArrayList<ContatoAdapter>();
         
         for (Contato contato: contatos){
        	 if (contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_ALTERADO ||
        		 contato.getSincronizado() == Constantes.NAO_SINCRONIZADO_CRIADO){
        		 	contatosRecuperados.add(UtilConverter.converteParaAdpater(contato));
        		 	contato.setSincronizado(Constantes.INFORMACAO_SINCRONIZADA);
        		 	controlador.atualizaContato(contato);
        	 }
         }
         
         return contatosRecuperados;
    }
    

 }

