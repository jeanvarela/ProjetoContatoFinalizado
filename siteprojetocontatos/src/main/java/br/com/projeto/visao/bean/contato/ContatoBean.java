package br.com.projeto.visao.bean.contato;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

import br.com.projeto.controlador.contato.ControladorContato;
import br.com.projeto.controlador.contato.IControladorContato;
import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.modelo.entidade.usuario.Usuario;
import br.com.projeto.util.constante.Constantes;
import br.com.projeto.util.context.FacesContextUtil;

@ManagedBean(name="contato")
@ViewScoped
public class ContatoBean   implements Serializable {

	private MapModel emptyModel;
    
    private String title;
     
    private double lat;
     
    private double lng;
    
    private long   idContato;
    
    private String acao;
    
    private Contato contato;
    
    private String nome;
    
    private String telefone;
    
    private String localizacao;
    
    private IControladorContato controladorContato;
    
    private String tituloBotao;
    
    private String centerGeoMap = "41.850033, -87.6500523";
    
    public ContatoBean(){
    }
 
    @PostConstruct
    public void init() {
        emptyModel = new DefaultMapModel();
        
        /**
         * Recupera o código do contato da seção. Caso o código do contato
         * existir na seção, a variável @idContato e inicializada com esse identificador,
         * caso contrário o @idContato é inicializado com 0.
         */
        Map<String,String> facesContext = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String parametroRecebido = facesContext.get("codigoContato");
        this.idContato = (parametroRecebido != null) ? new Long(parametroRecebido):0;
        
        
        if (idContato > 0){
        	controladorContato = new ControladorContato();
			
			this.contato = controladorContato.recuperaContato(idContato);
			this.nome = contato.getNome();
			this.telefone = contato.getTelefone();
			this.localizacao = contato.getDescricaoLocalizacao();
			this.acao = "Atualizar";
			
			if (contato.getLatitude() != 0 || contato.getLongitude() != 0){
				this.lat = contato.getLatitude();
				this.lng = contato.getLongitude();
			}else{
				inicializaPosicaoPadrao();
			}			
        }else{
        	this.acao = "Cadastrar";
        	inicializaPosicaoPadrao();
        }       
    }
    
    /**
     * Posição inicial do contato.  
     */
    private void inicializaPosicaoPadrao(){
    	 this.lat = -8.0183248;
         this.lng = -34.9697257;
    }

    public String getTituloBotao() {
		return tituloBotao;
	}
    
    public void setTituloBotao(String tituloBotao) {
		this.tituloBotao = tituloBotao;
	}
    
    public MapModel getEmptyModel() {
        return emptyModel;
    }
     
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public double getLat() {
        return lat;
    }
 
    public void setLat(double lat) {
        this.lat = lat;
    }
 
    public double getLng() {
        return lng;
    }
 
    public void setLng(double lng) {
        this.lng = lng;
    }
     
    public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public String getLocalizacao() {
		return localizacao;
	}
	
	public void selecinarLocalizacao(){
		
	}
	
	public void setCenterGeoMap(String centerGeoMap) {
		this.centerGeoMap = centerGeoMap;
	}
	
	public String getCenterGeoMap() {
		return centerGeoMap;
	}
 	
	public void setIdContato(long idContato) {
		this.idContato = idContato;
	}
	
	public long getIdContato() {
		return idContato;
	}
	
	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	public String getAcao() {
		return acao;
	}
	
	/**
	 * Função: Método utilizado para gravar/atualizar registro do contato.
	 *         Se o @idContato (identificador do contato) for igual a 0,
	 *         indica que um novo contato será criado. Caso contrário o 
	 *         contato é atualizado. 
	 */
	public void cadastrar(){
        IControladorContato controladorContato = new ControladorContato();
        
        if (idContato == 0){
           HttpSession secao = FacesContextUtil.getSession();
      	   long idUsuario = (Long) secao.getAttribute("ID_USUARIO");
            
           Usuario usuario = new Usuario();
           usuario.setCodigo(idUsuario);	
        	
       	   Contato contatoInserir = new Contato();
           contatoInserir.setDescricaoLocalizacao(this.localizacao);
           contatoInserir.setLatitude(this.lat);
           contatoInserir.setLongitude(this.lng);
           contatoInserir.setNome(this.nome);
           contatoInserir.setTelefone(this.telefone);
           contatoInserir.setSincronizado(Constantes.NAO_SINCRONIZADO_CRIADO);
           contatoInserir.setUsuario(usuario);           
      
           controladorContato.insereContato(contatoInserir);
           addDetailMessage("Inserido com sucesso");
        }else{
        	contato.setDescricaoLocalizacao(this.localizacao);
        	contato.setLatitude(this.lat);
        	contato.setLongitude(this.lng);
        	contato.setNome(this.nome);
        	contato.setTelefone(this.telefone);
        	contato.setSincronizado(Constantes.NAO_SINCRONIZADO_ALTERADO);
        	
        	controladorContato.atualizaContato(contato);        	
        	addDetailMessage("Atualizado com sucesso");
        }
        
       
	}
	
	/**
	 * Envia mensagem que será aexibida na tela de listagem de contato.
	 */
	private void addDetailMessage(String message) {
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
		 FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	      chamaTelaListaContatos();
	}

	public void cancelar(){
		chamaTelaListaContatos();
	}
	
	private void chamaTelaListaContatos(){
		try {
			Faces.redirect("pages/cadastro/contato/listaContato.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
