package br.com.projeto.visao.bean.usuario;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;

import br.com.projeto.controlador.usuario.ControladorUsuario;
import br.com.projeto.controlador.usuario.IControladorUsuario;
import br.com.projeto.modelo.entidade.usuario.Usuario;

@ManagedBean(name="usuario")
@ViewScoped
public class UsuarioBean {

	private String nome;
	private String email;
	private String senha;
	private String senhaConfirmacao;

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
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
	
	public String getSenhaConfirmacao() {
		return senhaConfirmacao;
	}
	
	public void setSenhaConfirmacao(String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
	}
	
	/**
	 * Objetivo: cadastrar um novo usuário. Caso o cadastro
	 *           seja realizado com sucesso, o usuário é direcionado para a pagina principal
	 */
	public void cadastrar(){
		
		if (!this.senha.equals(this.senhaConfirmacao)){
			exibeMensagem("Senha e Senha de Confirmação não são iguais.");
		}else{
			
			IControladorUsuario controlador = new ControladorUsuario();
			

			if (!controlador.verificaEmailCadastrado(this.email)){
				Usuario usuario = new Usuario();
				usuario.setNome(this.nome);
				usuario.setEmail(this.email);
				usuario.setSenha(this.senha);
				
				
				long idUsuarioInserido = controlador.adicionaUsuario(usuario);
				
				if (idUsuarioInserido > 0){
					//Insere  o id do usuário na seção para poder recuperar nas outras paginas
					FacesContext contexto = FacesContext.getCurrentInstance();
					HttpSession session = (HttpSession) contexto.getExternalContext().getSession(false);
					session.setAttribute("ID_USUARIO", idUsuarioInserido);
					session.setAttribute("NOME_USUARIO",this.nome);
					
					try {
						Faces.redirect("index.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				exibeMensagem("E-mail já cadastrado.");
			}
		}
	}
	
	
	private void exibeMensagem(String conteudo){
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, null,conteudo);
		
		context.addMessage("formularioCadastro:senhaConfirmacao", mensagem);
		
	}
	
	/**
	 * Objetivo: esse metódo é disparado a partir da ação
	 *           do botão voltar. Através dese método o usuário é
	 *           direcionado para a tela de login. 
	 */
	public void voltar(){
		try {
			Faces.redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
