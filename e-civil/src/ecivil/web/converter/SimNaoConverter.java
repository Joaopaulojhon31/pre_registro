package ecivil.web.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ecivil.ejb.enumeration.EnumSimNao;

@FacesConverter(value = SimNaoConverter.nome)
public class SimNaoConverter implements Converter<EnumSimNao>, Serializable  {

	
		/**
	 * 
	 */
	private static final long serialVersionUID = 6525092320615183209L;
		
	public static final String nome = "simNaoConverter";

		@Override
		public EnumSimNao getAsObject(FacesContext fc, UIComponent comp, String flagConferido) {
			if ("S".equals(flagConferido)){
				return EnumSimNao.S;
			}else if("N".equals(flagConferido)){
				return EnumSimNao.N;
			}
			return null;
		}


		@Override
		public String getAsString(FacesContext fc, UIComponent comp, EnumSimNao enumProtocoloRedeSimples) {
			if (enumProtocoloRedeSimples == null || !(enumProtocoloRedeSimples instanceof EnumSimNao))
				return null;
			return ((EnumSimNao) enumProtocoloRedeSimples).toString();
		}

}