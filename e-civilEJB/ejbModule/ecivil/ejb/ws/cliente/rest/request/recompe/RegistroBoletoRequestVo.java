package ecivil.ejb.ws.cliente.rest.request.recompe;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistroBoletoRequestVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigoCarteira;
	private String dataEmissao;
	private String dataVencimento;
	private String valor;
	private String nomePagador;
	private String cpfCnpjPagador;
	private String logradouroPagador;
	private String numeroLogradouroPagador;
	private String complementoLogradouroPagador;
	private String cepPagador;
	private String bairroPagador;
	private String municipioPagador;
	private String ufPagador;
	private String emailPgador;
	
	public String getCodigoCarteira() {
		return codigoCarteira;
	}

	public void setCodigoCarteira(String codigoCarteira) {
		this.codigoCarteira = codigoCarteira;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getNomePagador() {
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}

	public String getCpfCnpjPagador() {
		return cpfCnpjPagador;
	}

	public void setCpfCnpjPagador(String cpfCnpjPagador) {
		this.cpfCnpjPagador = cpfCnpjPagador;
	}

	public String getLogradouroPagador() {
		return logradouroPagador;
	}

	public void setLogradouroPagador(String logradouroPagador) {
		this.logradouroPagador = logradouroPagador;
	}

	public String getNumeroLogradouroPagador() {
		return numeroLogradouroPagador;
	}

	public void setNumeroLogradouroPagador(String numeroLogradouroPagador) {
		this.numeroLogradouroPagador = numeroLogradouroPagador;
	}

	public String getComplementoLogradouroPagador() {
		return complementoLogradouroPagador;
	}

	public void setComplementoLogradouroPagador(String complementoLogradouroPagador) {
		this.complementoLogradouroPagador = complementoLogradouroPagador;
	}

	public String getCepPagador() {
		return cepPagador;
	}

	public void setCepPagador(String cepPagador) {
		this.cepPagador = cepPagador;
	}

	public String getBairroPagador() {
		return bairroPagador;
	}

	public void setBairroPagador(String bairroPagador) {
		this.bairroPagador = bairroPagador;
	}

	public String getMunicipioPagador() {
		return municipioPagador;
	}

	public void setMunicipioPagador(String municipioPagador) {
		this.municipioPagador = municipioPagador;
	}

	public String getUfPagador() {
		return ufPagador;
	}

	public void setUfPagador(String ufPagador) {
		this.ufPagador = ufPagador;
	}

	public String getEmailPgador() {
		return emailPgador;
	}

	public void setEmailPgador(String emailPgador) {
		this.emailPgador = emailPgador;
	}
	
}
