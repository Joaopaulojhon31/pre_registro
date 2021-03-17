package ecivil.ejb.ws.cliente.rest.response.contadigital;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaDigitalResponse {

	private String agencia;
	private String conta;
	private BigDecimal saldoUtilizavel;
	private BigDecimal saldoTotal;
	private BigDecimal saldoMinimoObrigatorio;
	private BigDecimal saqueProvisionado;
	private BigDecimal creditoProvisionado;

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public BigDecimal getSaldoUtilizavel() {
		return saldoUtilizavel;
	}

	public void setSaldoUtilizavel(BigDecimal saldoUtilizavel) {
		this.saldoUtilizavel = saldoUtilizavel;
	}

	public BigDecimal getSaldoTotal() {
		return saldoTotal;
	}

	public void setSaldoTotal(BigDecimal saldoTotal) {
		this.saldoTotal = saldoTotal;
	}

	public BigDecimal getSaldoMinimoObrigatorio() {
		return saldoMinimoObrigatorio;
	}

	public void setSaldoMinimoObrigatorio(BigDecimal saldoMinimoObrigatorio) {
		this.saldoMinimoObrigatorio = saldoMinimoObrigatorio;
	}

	public BigDecimal getSaqueProvisionado() {
		return saqueProvisionado;
	}

	public void setSaqueProvisionado(BigDecimal saqueProvisionado) {
		this.saqueProvisionado = saqueProvisionado;
	}

	public BigDecimal getCreditoProvisionado() {
		return creditoProvisionado;
	}

	public void setCreditoProvisionado(BigDecimal creditoProvisionado) {
		this.creditoProvisionado = creditoProvisionado;
	}
	
}
