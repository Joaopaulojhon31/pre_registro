package ecivil.ejb.ws.cliente.rest.response.contadigital;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaqueProvisionadoResponse {

	private Integer totalRegistros;
	private List<SaqueProvisionadoDetalhadoResponse> saqueProvisionadoDetalhadoResponse;
	private List<SaqueProvisionadoAgrupadoResponse> saqueProvisionadoAgrupadoResponse;

	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public List<SaqueProvisionadoDetalhadoResponse> getSaqueProvisionadoDetalhadoResponse() {
		return saqueProvisionadoDetalhadoResponse;
	}

	public void setSaqueProvisionadoDetalhadoResponse(List<SaqueProvisionadoDetalhadoResponse> saqueProvisionadoDetalhadoResponse) {
		this.saqueProvisionadoDetalhadoResponse = saqueProvisionadoDetalhadoResponse;
	}

	public List<SaqueProvisionadoAgrupadoResponse> getSaqueProvisionadoAgrupadoResponse() {
		return saqueProvisionadoAgrupadoResponse;
	}

	public void setSaqueProvisionadoAgrupadoResponse(List<SaqueProvisionadoAgrupadoResponse> saqueProvisionadoAgrupadoResponse) {
		this.saqueProvisionadoAgrupadoResponse = saqueProvisionadoAgrupadoResponse;
	}

}
