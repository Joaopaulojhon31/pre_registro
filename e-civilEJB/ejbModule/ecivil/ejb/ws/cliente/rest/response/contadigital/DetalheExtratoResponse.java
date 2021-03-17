package ecivil.ejb.ws.cliente.rest.response.contadigital;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalheExtratoResponse {

	private String descricaoOperacao;
	private String codigoTipoOperacao;
	private BigDecimal valorOperacao;
	private BigDecimal saldo;
	private String dataOperacao;
	private String protocolo;
	
	public boolean isOperacaoEntrada() {
		return getCodigoTipoOperacao().equals("E");
	}
	
	public String getDescricaoOperacao() {
		return descricaoOperacao;
	}

	public void setDescricaoOperacao(String descricaoOperacao) {
		this.descricaoOperacao = descricaoOperacao;
	}
	
	public String getCodigoTipoOperacao() {
		return codigoTipoOperacao;
	}

	public void setCodigoTipoOperacao(String codigoTipoOperacao) {
		this.codigoTipoOperacao = codigoTipoOperacao;
	}

	public BigDecimal getValorOperacao() {
		return valorOperacao;
	}

	public void setValorOperacao(BigDecimal valorOperacao) {
		this.valorOperacao = valorOperacao;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	
}
