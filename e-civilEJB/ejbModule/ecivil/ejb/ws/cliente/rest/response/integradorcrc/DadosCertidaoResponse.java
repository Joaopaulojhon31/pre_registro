package ecivil.ejb.ws.cliente.rest.response.integradorcrc;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ecivil.ejb.util.Util;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosCertidaoResponse {

	private Integer idRegistro;
	private String dataRegistro;
	private String dataFato;
	private String pessoaA;
	private String pessoaB;
	private String codCorregedoria;
	private String municipio;
	private String distrito;
	private String serventia;
	private String tipoFato;

	@JsonIgnore
	private BigDecimal aliquotaMunicipioRequisitado;

	@JsonIgnore
	private BigDecimal valorCertidaoSemAverbacao;

	@JsonIgnore
	private BigDecimal valorCertidaoComAverbacao;

	public boolean isCertidaoCasamento() {
		return Util.ehStringValida(getTipoFato()) && getTipoFato().equals("CA");
	}

	public boolean isCertidaoNascimento() {
		return Util.ehStringValida(getTipoFato()) && getTipoFato().equals("NA");
	}

	public boolean isCertidaoObito() {
		return Util.ehStringValida(getTipoFato()) && getTipoFato().equals("OB");
	}

	public boolean isCertidaoInterdicao() {
		return Util.ehStringValida(getTipoFato()) && getTipoFato().equals("I");
	}

	public boolean podeGerarPedidoCertidao() {
		return isCertidaoCasamento() || isCertidaoNascimento() || isCertidaoObito();
	}

	public String getCodigoCorregedoriaFormatada() {
		return Util.formataCodigCartorio(codCorregedoria);
	}

	public String descricaoTipoCertidao() {
		if (isCertidaoCasamento()) {
			return "CASAMENTO";
		}

		if (isCertidaoNascimento()) {
			return "NASCIMENTO";
		}

		if (isCertidaoObito()) {
			return "ÓBITO";
		}

		if (isCertidaoInterdicao()) {
			return "INTERDIÇÃO";
		}

		return "";
	}

	public Integer getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Integer idRegistro) {
		this.idRegistro = idRegistro;
	}

	public String getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(String dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getDataFato() {
		return dataFato;
	}

	public void setDataFato(String dataFato) {
		this.dataFato = dataFato;
	}

	public String getPessoaA() {
		return pessoaA != null ? pessoaA.toUpperCase() : null;
	}

	public void setPessoaA(String pessoaA) {
		this.pessoaA = pessoaA;
	}

	public String getPessoaB() {
		return pessoaB != null ? pessoaB.toUpperCase() : null;
	}

	public void setPessoaB(String pessoaB) {
		this.pessoaB = pessoaB;
	}

	public String getCodCorregedoria() {
		return codCorregedoria;
	}

	public void setCodCorregedoria(String codCorregedoria) {
		this.codCorregedoria = codCorregedoria;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getTipoFato() {
		return tipoFato;
	}

	public void setTipoFato(String tipoFato) {
		this.tipoFato = tipoFato;
	}

	public BigDecimal getAliquotaMunicipioRequisitado() {
		return aliquotaMunicipioRequisitado;
	}

	public void setAliquotaMunicipioRequisitado(BigDecimal aliquotaMunicipioRequisitado) {
		this.aliquotaMunicipioRequisitado = aliquotaMunicipioRequisitado;
	}

	public BigDecimal getValorCertidaoSemAverbacao() {
		return valorCertidaoSemAverbacao;
	}

	public void setValorCertidaoSemAverbacao(BigDecimal valorCertidaoSemAverbacao) {
		this.valorCertidaoSemAverbacao = valorCertidaoSemAverbacao;
	}

	public BigDecimal getValorCertidaoComAverbacao() {
		return valorCertidaoComAverbacao;
	}

	public void setValorCertidaoComAverbacao(BigDecimal valorCertidaoComAverbacao) {
		this.valorCertidaoComAverbacao = valorCertidaoComAverbacao;
	}

}
