package br.com.projeto.visao.bean.logout;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Faces;

import br.com.projeto.util.context.FacesContextUtil;

@ManagedBean(name="logout")
@ViewScoped
public class LogoutBean {
	
	/**
	 * Função: metodo utilizado para  sair do sistema. Primeiramente
	 *         é retirado o identificador e o nome do usuário da
	 *         seção e depois o usuario é redirecionado para a tela de login. 
	 */
	public void sair(){
		FacesContextUtil.getSession().removeAttribute("ID_USUARIO");
		FacesContextUtil.getSession().removeAttribute("NOME_USUARIO");
		
		try {			
			Faces.redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
