package ecivil.ejb.util;

import java.io.OutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import web.util.JSFUtil;

public class DownloadArquivo {

	public static synchronized void downloadFile(String nomeArquivo, byte[] arquivo, String mimeType, FacesContext facesContext) throws Exception {
		OutputStream outputStream = null;
		try {
			ExternalContext context = facesContext.getExternalContext();
			nomeArquivo = nomeArquivo.concat(".").concat(mimeType);
			
			HttpServletResponse response = (HttpServletResponse) context.getResponse();
			response.setHeader("Content-Disposition", "attachment;filename=\"" + nomeArquivo + "\"");
			response.setContentLength((int) arquivo.length);
			response.setContentType(mimeType);
	
			outputStream = JSFUtil.getResponse().getOutputStream();
			outputStream.write(arquivo, 0, arquivo.length);
			outputStream.flush();
			FacesContext.getCurrentInstance().responseComplete();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

}