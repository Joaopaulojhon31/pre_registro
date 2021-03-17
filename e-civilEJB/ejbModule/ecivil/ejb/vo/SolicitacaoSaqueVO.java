package ecivil.ejb.vo;

import java.math.BigDecimal;

import ecivil.ejb.util.Util;

public class SolicitacaoSaqueVO {

	private String codigoCorregedoria;
	private String dataSolicitacao;
	private BigDecimal valorSolicitacao;
	private String idSaqueProvisionado;
	private String nomeDistritoECartorio;
	private String nomeCartorio;
	private String distrito;
	private String municipio;
	private String telefone;
	private String email;
	private String nomeTitularConta;
	private String cpfCnpjTitularConta;
	private String codigoBanco;
	private String nomeBanco;
	private String agencia;
	private String conta;
	private String tipoConta;
	private boolean saqueConfirmado;
	
	public String cpfCnpjTitularFormatado() {
		if (!Util.ehStringValida(getCpfCnpjTitularConta())) {
			return null;
		}
		
		if (getCpfCnpjTitularConta().length() == 11) {
			return Util.addMaskCPF(getCpfCnpjTitularConta());
		}
		
		return Util.addMaskCnpj(getCpfCnpjTitularConta());
	}
	
	public String recuperaBancoAgenciaConta() {
		String codigoBanco = Util.ehStringValida(getCodigoBanco()) ? getCodigoBanco() : "";
		String nomeBanco = Util.ehStringValida(getNomeBanco()) ? getNomeBanco() : "";
		String agencia = Util.ehStringValida(getAgencia()) ? getAgencia() : "";
		String conta = Util.ehStringValida(getConta()) ? getConta() : "";
		return codigoBanco + " - " + nomeBanco + " / " + agencia + " / " + conta;
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

	public BigDecimal getValorSolicitacao() {
		return valorSolicitacao;
	}

	public void setValorSolicitacao(BigDecimal valorSolicitacao) {
		this.valorSolicitacao = valorSolicitacao;
	}

	public String getIdSaqueProvisionado() {
		return idSaqueProvisionado;
	}

	public void setIdSaqueProvisionado(String idSaqueProvisionado) {
		this.idSaqueProvisionado = idSaqueProvisionado;
	}

	public String getNomeDistritoECartorio() {
		return nomeDistritoECartorio;
	}

	public void setNomeDistritoECartorio(String nomeDistritoECartorio) {
		this.nomeDistritoECartorio = nomeDistritoECartorio;
	}

	public String getNomeCartorio() {
		return nomeCartorio;
	}

	public void setNomeCartorio(String nomeCartorio) {
		this.nomeCartorio = nomeCartorio;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	
	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isSaqueConfirmado() {
		return saqueConfirmado;
	}

	public void setSaqueConfirmado(boolean saqueConfirmado) {
		this.saqueConfirmado = saqueConfirmado;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof SolicitacaoSaqueVO) {
			return ((SolicitacaoSaqueVO) obj).getIdSaqueProvisionado().equals(this.idSaqueProvisionado);
		}
		return false;
	}

}
