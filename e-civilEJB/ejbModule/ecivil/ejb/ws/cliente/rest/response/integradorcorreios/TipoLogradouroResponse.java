package ecivil.ejb.ws.cliente.rest.response.integradorcorreios;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TipoLogradouroResponse {

	private String nome;
	private String codigoRfb;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigoRfb() {
		return codigoRfb;
	}

	public void setCodigoRfb(String codigoRfb) {
		this.codigoRfb = codigoRfb;
	}

}
