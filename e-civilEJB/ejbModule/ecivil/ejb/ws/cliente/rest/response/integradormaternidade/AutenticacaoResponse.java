package ecivil.ejb.ws.cliente.rest.response.integradormaternidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutenticacaoResponse {

	private String cpf;
	private String nome;
	private String email;
	private String cartorio;
	private String distrito;
	private String codigoCorregedoria;
	private String codigoPermissao;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCartorio() {
		return cartorio;
	}

	public void setCartorio(String cartorio) {
		this.cartorio = cartorio;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public String getCodigoPermissao() {
		return codigoPermissao;
	}

	public void setCodigoPermissao(String codigoPermissao) {
		this.codigoPermissao = codigoPermissao;
	}

}
