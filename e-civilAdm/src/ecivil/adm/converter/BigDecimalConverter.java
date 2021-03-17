package ecivil.adm.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter(value = BigDecimalConverter.CONVERTER_NAME)
public class BigDecimalConverter implements Converter {

	public final static String CONVERTER_NAME = "bigDecimalConverter";

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		if (valor != null && !valor.equals("")) {
			return new BigDecimal(valor.replaceAll("\\.", "").replaceAll(",", "\\."));
		}
		return null;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object valor) {
		if (valor == null || ((valor instanceof String) && ((String) valor).trim().equals(""))) {
			return "";
		}
		if (valor instanceof BigDecimal) {
			DecimalFormat formater = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("pt", "BR"));
			formater.applyPattern("###,##0.00");
			return formater.format(valor);
		}
		return "";
	}
}