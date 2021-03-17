package ecivil.adm.converter;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.util.DataUtil;

@SuppressWarnings("rawtypes")
@FacesConverter(value = "dataConverter")
public class DataConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		if (StringUtils.isEmpty(valor)) {
			return null;
		}
		return DataUtil.converteDataStringParaDate(valor);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (!(obj instanceof Date)) {
			return "";
		}
		return DataUtil.converteDateParaString_YYYYMMDD_TRACO((Date) obj);
	}

}