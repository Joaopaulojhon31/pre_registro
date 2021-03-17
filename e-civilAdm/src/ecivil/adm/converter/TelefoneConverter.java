package ecivil.adm.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ecivil.ejb.util.Util;

@SuppressWarnings("rawtypes")
@FacesConverter(value = "telefoneConverter")
public class TelefoneConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent componente, String valor) {
		if (valor == null || valor.equals("")) {
			return null;
		}
		return Util.removeMask(valor);
	}

	public String getAsString(FacesContext context, UIComponent componente, Object obj) {
		String telefone = (String) obj;
		
		if (telefone == null) {
			return null;
		}

		if (telefone.length() == 10 || telefone.length() == 11) {
			return Util.addMascaraTelefoneCelular(telefone);
		}
		
		return telefone;
	}

}
