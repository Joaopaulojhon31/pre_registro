package ecivil.ejb.ws.cliente.rest.response.integradorcorreios;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultaCepResponse {

	private String cep;
	private String logradouro;
	private String bairro;
	private TipoLogradouroResponse tipoLogradouro;
	private MunicipioResponse municipio;

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public TipoLogradouroResponse getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouroResponse tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public MunicipioResponse getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioResponse municipio) {
		this.municipio = municipio;
	}

}
