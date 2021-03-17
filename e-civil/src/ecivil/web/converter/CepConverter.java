package ecivil.web.converter;

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter(value = CepConverter.CONVERTER_NAME)
public class CepConverter implements Converter, Serializable {

	private static final long serialVersionUID = 3311640079334968599L;

	public static final String CONVERTER_NAME = "cepConverter";

	private static Pattern CEP_FORMATADO_PATTERN = Pattern.compile("^\\d{5}\\-\\d{3}$");
	private static Pattern CEP_NAO_FORMATADO_PATTERN = Pattern.compile("^\\d{8}$");

	public static boolean isCepFormatadoValido(String cep) {
		return CEP_FORMATADO_PATTERN.matcher(cep).matches();
	}

	public static boolean isCepValido(String cep) {
		return cep != null && CEP_NAO_FORMATADO_PATTERN.matcher(cep).matches();
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || "".equals(value.trim())) {
			return null;
		}
		value = value.trim();
		if (isCepFormatadoValido(value)) {
			return value.replaceAll("[^0-9]", "");
		}
		return value;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		if (!(value instanceof String)) {
			throw new ConverterException("CEP deve ser do tipo String.");
		}
		String cep = (String) value;
		cep = cep.trim();
		if (!isCepValido(cep)) {
			return null;
		}
		StringBuilder cepFormatado = new StringBuilder(cep);
		cepFormatado.insert(5, "-");
		return cepFormatado.toString();
	}

}