package ecivil.ejb.ws.cliente.rest.request.contadigital;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlterarSaqueProvisionadoRequest {

	private String idsSaqueProvisionado;

	public String getIdsSaqueProvisionado() {
		return idsSaqueProvisionado;
	}

	public void setIdsSaqueProvisionado(String idsSaqueProvisionado) {
		this.idsSaqueProvisionado = idsSaqueProvisionado;
	}

}
