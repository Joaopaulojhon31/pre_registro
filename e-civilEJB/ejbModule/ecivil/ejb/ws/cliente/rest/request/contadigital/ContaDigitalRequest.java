package ecivil.ejb.ws.cliente.rest.request.contadigital;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaDigitalRequest {

	private String codigoCorregedoria;
	private String codigoCorregedoriaBeneficiario;
	private String codigoTipoOperacao;
	private String cpfCnpjOperacao;
	private String protocolo;
	private BigDecimal valor;

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public String getCodigoCorregedoriaBeneficiario() {
		return codigoCorregedoriaBeneficiario;
	}

	public void setCodigoCorregedoriaBeneficiario(String codigoCorregedoriaBeneficiario) {
		this.codigoCorregedoriaBeneficiario = codigoCorregedoriaBeneficiario;
	}

	public String getCodigoTipoOperacao() {
		return codigoTipoOperacao;
	}

	public void setCodigoTipoOperacao(String codigoTipoOperacao) {
		this.codigoTipoOperacao = codigoTipoOperacao;
	}
	
	public String getCpfCnpjOperacao() {
		return cpfCnpjOperacao;
	}

	public void setCpfCnpjOperacao(String cpfCnpjOperacao) {
		this.cpfCnpjOperacao = cpfCnpjOperacao;
	}
	
	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
