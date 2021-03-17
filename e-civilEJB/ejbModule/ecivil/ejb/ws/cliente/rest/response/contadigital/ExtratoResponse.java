package ecivil.ejb.ws.cliente.rest.response.contadigital;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtratoResponse {

	private Integer totalRegistros;
	private ContaDigitalResponse contaDigitalResponse;
	private List<DetalheExtratoResponse> detalheExtratoResponse;

	public ExtratoResponse() {
	}

	public ExtratoResponse(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public ContaDigitalResponse getContaDigitalResponse() {
		return contaDigitalResponse;
	}

	public void setContaDigitalResponse(ContaDigitalResponse contaDigitalResponse) {
		this.contaDigitalResponse = contaDigitalResponse;
	}

	public List<DetalheExtratoResponse> getDetalheExtratoResponse() {
		return detalheExtratoResponse;
	}

	public void setDetalheExtratoResponse(List<DetalheExtratoResponse> detalheExtratoResponse) {
		this.detalheExtratoResponse = detalheExtratoResponse;
	}

}
