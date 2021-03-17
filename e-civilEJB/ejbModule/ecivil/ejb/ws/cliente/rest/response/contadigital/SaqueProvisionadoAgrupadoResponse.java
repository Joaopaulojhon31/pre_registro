package ecivil.ejb.ws.cliente.rest.response.contadigital;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaqueProvisionadoAgrupadoResponse {

	private String codigoCorregedoria;
	private Long qtdSolicitacoes;
	private BigDecimal totalSaqueProvisionado;
	private String idSaqueProvisionado;

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public Long getQtdSolicitacoes() {
		return qtdSolicitacoes;
	}

	public void setQtdSolicitacoes(Long qtdSolicitacoes) {
		this.qtdSolicitacoes = qtdSolicitacoes;
	}

	public BigDecimal getTotalSaqueProvisionado() {
		return totalSaqueProvisionado;
	}

	public void setTotalSaqueProvisionado(BigDecimal totalSaqueProvisionado) {
		this.totalSaqueProvisionado = totalSaqueProvisionado;
	}

	public String getIdSaqueProvisionado() {
		return idSaqueProvisionado;
	}

	public void setIdSaqueProvisionado(String idSaqueProvisionado) {
		this.idSaqueProvisionado = idSaqueProvisionado;
	}
	
}
