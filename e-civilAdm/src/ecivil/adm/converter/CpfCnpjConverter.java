package ecivil.adm.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ecivil.ejb.util.Util;

@SuppressWarnings("rawtypes")
@FacesConverter(CpfCnpjConverter.CONVERTER_NAME)
public class CpfCnpjConverter implements Converter {

	public static final String CONVERTER_NAME = "cpfCnpjConverter";

	public Object getAsObject(FacesContext context, UIComponent componente, String valor) {
		if (valor == null || valor.equals("")) {
			return null;
		}
		return Util.removeMask(valor);
	}

	public String getAsString(FacesContext context, UIComponent componente, Object obj) {
		String retornoTela = (String) obj;
		
		if (retornoTela == null) {
			return null;
		}

		if (retornoTela.length() == 11) {
			return Util.addMask("###.###.###-##", retornoTela);
		}
		
		if (retornoTela.length() == 14) {
			return Util.addMask("##.###.###/####-##", retornoTela);
		}
		
		return retornoTela;
	}

}
