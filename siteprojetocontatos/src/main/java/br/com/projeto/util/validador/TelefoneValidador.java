package br.com.projeto.util.validador;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.inputmask.InputMask;

@FacesValidator("telefoneValidador")
public class TelefoneValidador implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String telefone = (String) value;
//		
//		
//		telefone = telefone.replace("(","").replace(")","").replace("-","");
//		
//		if (telefone.length() == 0){
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,null,
//					                                      "O número do telefone do contato deve ser preenchido"));
//		}else
//		if (telefone.length() < 11){
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,null,"Número telefone inválido"));
//		}
		String submittedValue = context.getExternalContext().getRequestParameterMap().get(component.getClientId(context));

//	    if (submittedValue == null || submittedValue.isEmpty()) {
//	            return; // Let @NotNull / required="true" handle.
//	     }

	        InputMask input = (InputMask) component;
	        String mask = input.getMask();
	        StringBuilder regex = new StringBuilder();

	        for (char c : mask.toCharArray()) {
	            switch (c) {
	                case 'a': regex.append("[a-zA-Z]"); break;
	                case '9': regex.append("[0-9]"); break;
	                case '?': case '+': case '*': regex.append(c); break;
	                default: regex.append(Pattern.quote(Character.toString(c))); break;
	            }
	        }

	        if (!submittedValue.matches(regex.toString())) {
	            throw new ValidatorException(new FacesMessage(submittedValue + " does not match " + mask));
	        }
	}

}
