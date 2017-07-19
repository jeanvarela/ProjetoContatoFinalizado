package br.com.projeto.util.context;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class FacesContextUtil {

	public static HttpSession getSession(){
		FacesContext contexto = FacesContext.getCurrentInstance();

		return (HttpSession) contexto.getExternalContext().getSession(false);
	}
}
