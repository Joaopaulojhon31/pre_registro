package ecivil.ejb.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@Embeddable
public class Endereco implements Serializable {

	@Column(name = "nome_logradouro")
	private String nomeLogradouro;

	@ManyToOne
	@JoinColumn(name = "id_tipo_logradouro")
	private TipoLogradouro tipoLogradouro;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "numero_logradouro")
	private String numeroLogradouro;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "cep")
	private String cep;

	public boolean possuiNomeLogradouro() {
		return StringUtils.isNotEmpty(getNomeLogradouro());
	}

	public boolean possuiTipoLogradouro() {
		return getTipoLogradouro() != null;
	}

	public boolean possuiNumeroLogradouro() {
		return StringUtils.isNotEmpty(getNumeroLogradouro());
	}

	public boolean possuiBairro() {
		return StringUtils.isNotEmpty(getBairro());
	}

	public boolean possuiCep() {
		return StringUtils.isNotEmpty(getCep());
	}
	
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNomeLogradouro() {
		return nomeLogradouro;
	}

	public void setNomeLogradouro(String nomeLogradouro) {
		this.nomeLogradouro = nomeLogradouro;
	}

	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumeroLogradouro() {
		return numeroLogradouro;
	}

	public void setNumeroLogradouro(String numeroLogradouro) {
		this.numeroLogradouro = numeroLogradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

}
