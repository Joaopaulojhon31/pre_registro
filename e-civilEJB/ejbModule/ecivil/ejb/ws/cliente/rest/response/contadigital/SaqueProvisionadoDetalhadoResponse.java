package ecivil.ejb.ws.cliente.rest.response.contadigital;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ecivil.ejb.enumeration.EnumSituacaoSolicitacaoSaque;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaqueProvisionadoDetalhadoResponse {

	private String codigoCorregedoria;
	private String dataSolicitacao;
	private BigDecimal saqueProvisionado;
	private String idSaqueProvisionado;
	private String codigoSituacao;
	
	public String descricaoSituacao() {
		return EnumSituacaoSolicitacaoSaque.descricaoSituacaoPorCodigo(getCodigoSituacao());
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public String getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(String dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}
	
	public BigDecimal getSaqueProvisionado() {
		return saqueProvisionado;
	}

	public void setSaqueProvisionado(BigDecimal saqueProvisionado) {
		this.saqueProvisionado = saqueProvisionado;
	}

	public String getIdSaqueProvisionado() {
		return idSaqueProvisionado;
	}

	public void setIdSaqueProvisionado(String idSaqueProvisionado) {
		this.idSaqueProvisionado = idSaqueProvisionado;
	}

	public String getCodigoSituacao() {
		return codigoSituacao;
	}

	public void setCodigoSituacao(String codigoSituacao) {
		this.codigoSituacao = codigoSituacao;
	}
	
}
