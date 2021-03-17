package ecivil.ejb.ws.cliente.rest.request.recompe;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtoResponseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigo;
	private BigDecimal tfj;
	private BigDecimal valorFinal;
	private BigDecimal emolumento;
	private BigDecimal recompe;
	private String inicioVigencia;
	private String fimVigencia;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public BigDecimal getTfj() {
		return tfj;
	}

	public void setTfj(BigDecimal tfj) {
		this.tfj = tfj;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

	public BigDecimal getEmolumento() {
		return emolumento;
	}

	public void setEmolumento(BigDecimal emolumento) {
		this.emolumento = emolumento;
	}

	public BigDecimal getRecompe() {
		return recompe;
	}

	public void setRecompe(BigDecimal recompe) {
		this.recompe = recompe;
	}

	public String getInicioVigencia() {
		return inicioVigencia;
	}

	public void setInicioVigencia(String inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public String getFimVigencia() {
		return fimVigencia;
	}

	public void setFimVigencia(String fimVigencia) {
		this.fimVigencia = fimVigencia;
	}

}
