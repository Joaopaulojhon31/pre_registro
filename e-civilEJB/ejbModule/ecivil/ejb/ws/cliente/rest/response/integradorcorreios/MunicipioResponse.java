package ecivil.ejb.ws.cliente.rest.response.integradorcorreios;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MunicipioResponse {

	private String nome;
	private String codigoRfb;
	private String codigoDne;
	private UfResponse uf;

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

	public String getCodigoDne() {
		return codigoDne;
	}

	public void setCodigoDne(String codigoDne) {
		this.codigoDne = codigoDne;
	}

	public UfResponse getUf() {
		return uf;
	}

	public void setUf(UfResponse uf) {
		this.uf = uf;
	}

}
