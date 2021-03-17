package ecivil.ejb.ws.cliente.rest.request.integradorcrc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuscarCertidaoRequest {

	private String tipoFato;
	private String codCorregedoria;
	private Integer idMunicipio;
	private String nomeAssociado;
	private String cpf;
	private String dataInicio;
	private String dataFim;
	private String[] particulasNomeProcurado;
	private Integer primeiroRegistro;
	private Integer registrosPorPagina;
	private boolean nomeIdentico;
	
	public String getTipoFato() {
		return tipoFato;
	}

	public void setTipoFato(String tipoFato) {
		this.tipoFato = tipoFato;
	}

	public String getCodCorregedoria() {
		return codCorregedoria;
	}

	public void setCodCorregedoria(String codCorregedoria) {
		this.codCorregedoria = codCorregedoria;
	}

	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
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

	public String[] getParticulasNomeProcurado() {
		return particulasNomeProcurado;
	}

	public void setParticulasNomeProcurado(String[] particulasNomeProcurado) {
		this.particulasNomeProcurado = particulasNomeProcurado;
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

	public boolean isNomeIdentico() {
		return nomeIdentico;
	}

	public void setNomeIdentico(boolean nomeIdentico) {
		this.nomeIdentico = nomeIdentico;
	}

	public String getNomeAssociado() {
		return nomeAssociado;
	}

	public void setNomeAssociado(String nomeAssociado) {
		this.nomeAssociado = nomeAssociado;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
