package br.com.projeto.visao.bean.contato;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;

import br.com.projeto.controlador.contato.ControladorContato;
import br.com.projeto.controlador.contato.IControladorContato;
import br.com.projeto.infraestrutura.util.Util;
import br.com.projeto.modelo.entidade.contato.Contato;
import br.com.projeto.util.context.FacesContextUtil;
import br.com.projeto.util.enumeracao.TipoBuscaContato;

@ManagedBean(name="contatoLista")
@ViewScoped
public class ContatoListBean implements Serializable  {

	private static final long serialVersionUID = 1L;

	private IControladorContato controladorContatos;
	
	private List<Contato> listaContatos;
	
	private String criterioBusca;
	
	private Long idUsuario;
	
	private DataTable  dataTable;
	
	public ContatoListBean(){		
		HttpSession secao = FacesContextUtil.getSession();
		idUsuario = (Long) secao.getAttribute("ID_USUARIO");

		controladorContatos = new ControladorContato();
		this.listaContatos = controladorContatos.recuperaContatoPorUsuario(idUsuario);
	}
	
    public DataTable getDataTable() {
	        return dataTable;
	}
	
	public void setDataTable(DataTable dataTable) {
	        this.dataTable = dataTable;
	}

	
	public List<Contato> getListaContatos() {
		return listaContatos;
	}
	
	public void setListaContatos(List<Contato> listaContatos) {
		this.listaContatos = listaContatos;
	}
	
	public void setCriterioBusca(String criterioBusca) {
		this.criterioBusca = criterioBusca;
	}
	
	public String getCriterioBusca() {
		return criterioBusca;
	}
	
	/**
	 * Função: método utilizado para realizar a busca de contatos de um determinado usuário.
	 *         A busca é realizada pelo nome ou pelo número  do telefone.
	 */
	public void procurarContato() {
		String criterioFormatado =  Util.retiraMascaraTelefone(this.criterioBusca);
		criterioFormatado = Util.converterStringParaMiniscula(criterioFormatado);
		
		if (criterioFormatado.isEmpty()){
			this.listaContatos = controladorContatos.recuperaContatoPorUsuario(idUsuario);
		}else{
			boolean telefoneDigitado = Util.isNumeroTelefone(criterioFormatado);
			
			if (telefoneDigitado){
				this.listaContatos = controladorContatos.recuperaContatos(idUsuario,criterioFormatado, TipoBuscaContato.PESQUISA_CELULAR.getValor());
			}else{
				this.listaContatos = controladorContatos.recuperaContatos(idUsuario, criterioFormatado, TipoBuscaContato.PESQUISA_NOME.getValor());			
			}
		}
	}
}
