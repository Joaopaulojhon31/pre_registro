package ecivil.ejb.exception;

import javax.ejb.ApplicationException;
import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault
@ApplicationException(rollback = true)
public class UploadException extends Exception {

	private String mensagemErro;

	public UploadException() {
		super();
	}
	
	public UploadException(String mensagemErro) {
		super();
		this.mensagemErro = mensagemErro;
	}
	
	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

}
