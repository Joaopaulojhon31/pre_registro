package ecivil.ejb.vo;

import java.math.BigDecimal;

import ecivil.ejb.util.Util;

public class DadosBancariosVO {

	private String nomeTitularConta;
	private String cpfCnpjTitularConta;
	private String codigoBanco;
	private String nomeBanco;
	private String agencia;
	private String conta;
	private String tipoConta;
	private BigDecimal saldoDisponivel;

	public String cpfCnpjTitularFormatado() {
		if (!Util.ehStringValida(getCpfCnpjTitularConta())) {
			return null;
		}

		if (getCpfCnpjTitularConta().length() == 11) {
			return Util.addMaskCPF(getCpfCnpjTitularConta());
		}

		return Util.addMaskCnpj(getCpfCnpjTitularConta());
	}

	public String recuperaDescricaoTipoConta() {
		if (!Util.ehStringValida(getTipoConta())) {
			return null;
		}

		if (getTipoConta().equalsIgnoreCase("P")) {
			return "Poupança";
		}

		if (getTipoConta().equalsIgnoreCase("C")) {
			return "Conta Corrente";
		}

		return null;
	}

	public String getNomeTitularConta() {
		return nomeTitularConta;
	}

	public void setNomeTitularConta(String nomeTitularConta) {
		this.nomeTitularConta = nomeTitularConta;
	}

	public String getCpfCnpjTitularConta() {
		return cpfCnpjTitularConta;
	}

	public void setCpfCnpjTitularConta(String cpfCnpjTitularConta) {
		this.cpfCnpjTitularConta = cpfCnpjTitularConta;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

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

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public BigDecimal getSaldoDisponivel() {
		return saldoDisponivel;
	}

	public void setSaldoDisponivel(BigDecimal saldoDisponivel) {
		this.saldoDisponivel = saldoDisponivel;
	}

}
