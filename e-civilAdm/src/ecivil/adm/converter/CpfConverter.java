package ecivil.adm.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ecivil.ejb.util.Util;

@SuppressWarnings("rawtypes")
@FacesConverter(value = "cpfConverter")
public class CpfConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent componente, String valor) {
		if (valor == null || valor.equals("")) {
			return null;
		}
		return Util.removeMask(valor);
	}

	public String getAsString(FacesContext context, UIComponent componente, Object obj) {
		String cpf = (String) obj;
		if (cpf == null)
			return null;

		if (cpf.length() == 11) {
			return Util.addMaskCPF(cpf);
		}
		return cpf;
	}

}
