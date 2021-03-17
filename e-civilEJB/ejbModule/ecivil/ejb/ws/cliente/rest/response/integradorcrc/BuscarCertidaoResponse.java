package ecivil.ejb.ws.cliente.rest.response.integradorcrc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuscarCertidaoResponse {

	private Integer totalRegistros;
	private List<DadosCertidaoResponse> dadosCertidao;
	
	public boolean possuiResultadoBusca() {
		return getDadosCertidao() != null && !getDadosCertidao().isEmpty();
//		return getTotalRegistros() != null && getTotalRegistros() > 0;
	}
	
	public BuscarCertidaoResponse() {
	}

	public BuscarCertidaoResponse(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public List<DadosCertidaoResponse> getDadosCertidao() {
		return dadosCertidao;
	}

	public void setDadosCertidao(List<DadosCertidaoResponse> dadosCertidao) {
		this.dadosCertidao = dadosCertidao;
	}

}
