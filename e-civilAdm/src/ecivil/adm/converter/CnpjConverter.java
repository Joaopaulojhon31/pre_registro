package ecivil.adm.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ecivil.ejb.util.Util;

@SuppressWarnings("rawtypes")
@FacesConverter(value = "cnpjConverter")
public class CnpjConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent componente, String valor) {
		if (valor == null || valor.equals("")) {
			return null;
		}
		return Util.removeMask(valor);
	}

	public String getAsString(FacesContext context, UIComponent componente, Object obj) {
		String cnpj = (String) obj;
		if (cnpj == null)
			return null;

		if (cnpj.length() == 14) {
			return Util.addMaskCnpj(cnpj);
		}
		return cnpj;
	}

}