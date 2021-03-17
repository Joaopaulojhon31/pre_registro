package web.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.el.ELResolver;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JSFUtil {

	public static void exibiPDF(byte[] bytesRelatorio) throws Exception {
		if (bytesRelatorio != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(bytesRelatorio, 0, bytesRelatorio.length);
			outputStream.flush();
			if (outputStream != null) {
				outputStream.close();
			}
			bytesRelatorio = null;
			FacesContext.getCurrentInstance().responseComplete();
		}
	}

	public static void forward(String viewId) {
		FacesContext facesContext = getFacesContext();
		String currentViewId = facesContext.getViewRoot().getViewId();
		if (viewId != null && (!viewId.equals(currentViewId))) {
			ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
			UIViewRoot viewRoot = viewHandler.createView(facesContext, viewId);
			facesContext.setViewRoot(viewRoot);
			facesContext.renderResponse();
		}
	}

	public static void put(String chave, Object valor) {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put(chave, valor);
	}

	public static Object get(String chave) {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(chave);
	}

	public static void clear() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
	}

	public static Object retornaJSFBean(String name) {
		FacesContext c = FacesContext.getCurrentInstance();
		ELResolver elResolver = c.getApplication().getELResolver();
		return elResolver.getValue(c.getELContext(), null, name);
	}

	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}

	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static ServletContext getServletContext() {
		return (ServletContext) getExternalContext().getContext();
	}

	public static boolean possuiSessao() {
		return getSession() != null;
	}

	public static String getRealPath() {
		return getServletContext().getRealPath("/");
	}

	public static String getContextPath() {
		return getRequest().getRequestURL().toString();
	}

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	public static void redireciona(String url) throws IOException {
		getFacesContext().getExternalContext().redirect(url);
		getFacesContext().responseComplete();
	}

	public static void download(byte[] arquivo, String nomeArquivo) throws Exception {
		HttpServletResponse response = getResponse();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");
		response.setContentLength(arquivo.length);
		OutputStream out = response.getOutputStream();
		out.write(arquivo);
		out.flush();
		out.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public static Object retornaBean(String nome) {
		ELResolver elResolver = getFacesContext().getApplication().getELResolver();
		return elResolver.getValue(getFacesContext().getELContext(), null, nome);
	}

	public static void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

}
