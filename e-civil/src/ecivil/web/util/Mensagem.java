package ecivil.web.util;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;

public class Mensagem {

	public static void listaErros(List<Map<String, Object[]>> listaErros) {
		for (Map<String, Object[]> mapa : listaErros) {
			Set<String> keys = mapa.keySet();
			for (String chave : keys) {
				Object[] values = (Object[]) mapa.get(chave);
				error(chave, values);
			}
		}
	}

	public static void listaErrosSemBundle(List<Map<String, Object[]>> listaErros) {
		for (Map<String, Object[]> mapa : listaErros) {
			Set<String> keys = mapa.keySet();
			for (String chave : keys) {
				errorSemBundle(chave);
			}
		}
	}

	public static void listaAvisos(List<Map<String, Object[]>> listaErros) {
		for (Map<String, Object[]> mapa : listaErros) {
			Set<String> keys = mapa.keySet();
			for (String chave : keys) {
				Object[] values = (Object[]) mapa.get(chave);
				warn(chave, values);
			}
		}
	}

	public static void infoSemBundle(String key) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, key, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
	}
	
	public static void infoEmModal(String idMessageContainer, List<Map<String, Object[]>> listaErros) {
		String stringMessage = "Security has been correctly updated";
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, stringMessage, stringMessage);
		FacesContext.getCurrentInstance().addMessage("status", message);
	}
	
	public static void infoEntreTelas(String mensagem) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, mensagem);
		FacesContext.getCurrentInstance().addMessage("status", message);
	}

	public static void limparMensagens() {
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<FacesMessage> it = context.getMessages();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
	}

	public static void info(String key, Object... args) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, getStringFromBundle(key, args), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
	}

	public static void warn(String key, Object... args) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, getStringFromBundle(key, args), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public static void error(String key, Object... args) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, getStringFromBundle(key, args), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		FacesContext.getCurrentInstance().validationFailed();
		RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
	}

	public static void errorSemBundle(String key) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, key, null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
	}

	public static String getStringFromBundle(String key, Object... arguments) {
		return MessageFormat.format(getMessageFromResources(key), (Object[]) arguments);
	}

	public static String getStringFromBundle(String key) {
		return getMessageFromResources(key);
	}

	private static String getMessageFromResources(String message) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String mensagemFormatada = StringUtils.EMPTY;
		try {
			ResourceBundle commonResourceBundle = fc.getApplication().getResourceBundle(fc, "common-resources");
			mensagemFormatada = commonResourceBundle.getString(message);
			// Caso não exista uma referência de texto de validação comum,
			// a tradução será consultada de um conjunto do próprio módulo
		} catch (Exception e) {
			ResourceBundle moduleResourceBundle = fc.getApplication().getResourceBundle(fc, "resources");
			mensagemFormatada = moduleResourceBundle.getString(message);
		}
		return mensagemFormatada;
	}

}