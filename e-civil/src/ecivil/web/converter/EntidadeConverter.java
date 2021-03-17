package ecivil.web.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ecivil.ejb.dao.BaseDAO;
import ecivil.ejb.dao.EntidadeConverterDAO;
import ecivil.ejb.entidade.Entidade;
import web.util.JSFUtil;

@SuppressWarnings("rawtypes")
@FacesConverter(value = EntidadeConverter.nome)
public class EntidadeConverter extends BaseDAO implements Converter {

	public static final String nome = "entidadeConverter";

	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
			try {
				Object obj = getAttributesFrom(component).get(value);
				if (obj != null) {
					String classe = getAttributesFrom(component).get(value).toString().split("@")[0];
					return recuperaEntidade(classe, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value != null && !"".equals(value)) {
			Entidade entidade = (Entidade) value;

			Long codigo = entidade.getId();
			if (codigo != null) {
				// adiciona item como atributo do componente
				this.addAttribute(component, entidade);
				return String.valueOf(codigo);
			}
		}
		return (String) value;
	}

	private <T> T recuperaEntidade(String classe, String id) {
		Object bean = JSFUtil.retornaBean(EntidadeConverterDAO.NOME);
		return ((EntidadeConverterDAO) bean).obterEntidade((String) JSFUtil.getSession().getAttribute("seccional"), classe, new Long(id));
	}

	protected void addAttribute(UIComponent component, Entidade o) {
		// codigo da entidade como chave neste caso
		String key = o.getId().toString();
		this.getAttributesFrom(component).put(key, o);
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}
}
