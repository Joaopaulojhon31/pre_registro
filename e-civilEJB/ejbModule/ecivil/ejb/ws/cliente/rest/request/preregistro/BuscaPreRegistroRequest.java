package ecivil.ejb.ws.cliente.rest.request.preregistro;

public class BuscaPreRegistroRequest {
	
	private String cpfMae;
	private String cnsCartório;
	
	public String getCpfMae() {
		return cpfMae;
	}
	public void setCpfMae(String cpfMae) {
		this.cpfMae = cpfMae;
	}
	public String getCnsCartório() {
		return cnsCartório;
	}
	public void setCnsCartório(String cnsCartório) {
		this.cnsCartório = cnsCartório;
	}
}
