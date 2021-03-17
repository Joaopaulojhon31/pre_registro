package ecivil.ejb.ws.cliente.rest.request.contadigital;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FiltroExtratoRequest {

	private String codigoCorregedoria;
	private String dataInicio;
	private String dataFim;
	private String protocolo;
	private Integer primeiroRegistro;
	private Integer registrosPorPagina;

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public Integer getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public void setPrimeiroRegistro(Integer primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public Integer getRegistrosPorPagina() {
		return registrosPorPagina;
	}

	public void setRegistrosPorPagina(Integer registrosPorPagina) {
		this.registrosPorPagina = registrosPorPagina;
	}

}
