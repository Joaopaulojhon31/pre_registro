package ecivil.ejb.ws.cliente.rest.response.recompe;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistroBoletoResponseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;
	private String mensagens;
	private String codBanco;
	private String numeroConvenioContrato;
	private String nomeBeneficiario;
	private String cpfCnpjBeneficiario;
	private String logradouroBeneficiario;
	private String nuLogradouroBeneficiario;
	private String complementoLogradouroBeneficiario;
	private String bairroBeneficiario;
	private String cepBeneficiario;
	private String municipioBeneficiario;
	private String ufBeneficiario;
	private String numeroTitulo;
	private String nossoNumero;
	private BigDecimal valor;
	private String dataEmissao;
	private String dataVencimento;
	private String codigoBarras;
	private String linhaDigitavel;
	private String boletoPdf;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMensagens() {
		return mensagens;
	}

	public void setMensagens(String mensagens) {
		this.mensagens = mensagens;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getNumeroConvenioContrato() {
		return numeroConvenioContrato;
	}

	public void setNumeroConvenioContrato(String numeroConvenioContrato) {
		this.numeroConvenioContrato = numeroConvenioContrato;
	}

	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getCpfCnpjBeneficiario() {
		return cpfCnpjBeneficiario;
	}

	public void setCpfCnpjBeneficiario(String cpfCnpjBeneficiario) {
		this.cpfCnpjBeneficiario = cpfCnpjBeneficiario;
	}

	public String getLogradouroBeneficiario() {
		return logradouroBeneficiario;
	}

	public void setLogradouroBeneficiario(String logradouroBeneficiario) {
		this.logradouroBeneficiario = logradouroBeneficiario;
	}

	public String getNuLogradouroBeneficiario() {
		return nuLogradouroBeneficiario;
	}

	public void setNuLogradouroBeneficiario(String nuLogradouroBeneficiario) {
		this.nuLogradouroBeneficiario = nuLogradouroBeneficiario;
	}

	public String getComplementoLogradouroBeneficiario() {
		return complementoLogradouroBeneficiario;
	}

	public void setComplementoLogradouroBeneficiario(String complementoLogradouroBeneficiario) {
		this.complementoLogradouroBeneficiario = complementoLogradouroBeneficiario;
	}

	public String getBairroBeneficiario() {
		return bairroBeneficiario;
	}

	public void setBairroBeneficiario(String bairroBeneficiario) {
		this.bairroBeneficiario = bairroBeneficiario;
	}

	public String getCepBeneficiario() {
		return cepBeneficiario;
	}

	public void setCepBeneficiario(String cepBeneficiario) {
		this.cepBeneficiario = cepBeneficiario;
	}

	public String getMunicipioBeneficiario() {
		return municipioBeneficiario;
	}

	public void setMunicipioBeneficiario(String municipioBeneficiario) {
		this.municipioBeneficiario = municipioBeneficiario;
	}

	public String getUfBeneficiario() {
		return ufBeneficiario;
	}

	public void setUfBeneficiario(String ufBeneficiario) {
		this.ufBeneficiario = ufBeneficiario;
	}

	public String getNumeroTitulo() {
		return numeroTitulo;
	}

	public void setNumeroTitulo(String numeroTitulo) {
		this.numeroTitulo = numeroTitulo;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}

	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}

	public String getBoletoPdf() {
		return boletoPdf;
	}

	public void setBoletoPdf(String boletoPdf) {
		this.boletoPdf = boletoPdf;
	}

}
