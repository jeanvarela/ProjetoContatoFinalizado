package br.com.projeto.visao.bean.login;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;

import br.com.projeto.controlador.login.ControladorLogin;
import br.com.projeto.controlador.login.IControladorLogin;
import br.com.projeto.modelo.entidade.usuario.Usuario;

@ManagedBean(name="login")
@ViewScoped
public class LoginBean {

	private String email;
	private String senha;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	/**
	 * @author JEAN
	 * 
	 * Funçao: Método utilizado para realizar o login do usuário. 
	 *         Caso o e-mail e senha existam no banco o usuárrio é  redirecionado para a 
	 *         pagina que exibe a lista de contatos. Caso contrário é exibida ume mensagem
	 *         alertando que login ou senha são inválidos.
	 *         
	 *       
	 */
	public void logar(){
		IControladorLogin controlador = new ControladorLogin();
		Usuario usuario = controlador.logar(this.email,this.senha);
		
		if (usuario != null){
			try {
				
					FacesContext contexto = FacesContext.getCurrentInstance();
					HttpSession session = (HttpSession) contexto.getExternalContext().getSession(false);
					session.setAttribute("ID_USUARIO", usuario.getCodigo());
					session.setAttribute("NOME_USUARIO",usuario.getNome());
					
					Faces.redirect("pages/cadastro/contato/listaContato.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			exibeMensagem("Login e/ou senha inválidos");
		}
	}
	
	/**
	 * @author JEAN
	 * 
	 * Função: Método utilizado para exibir uma mensagem de alerta na tela
	 */
	private void exibeMensagem(String conteudo){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, null,conteudo);
		
		context.addMessage("formLogin:senha", mensagem);
		
	}
	
}
