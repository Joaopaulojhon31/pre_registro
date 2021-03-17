package ecivil.ejb.vo.relatorios;

import java.util.Date;

public class ReciboCertidaoVO {

	private String protocolo;
	private Date dataEmissao;
	private String nomeRequerente;
	private String tipoCertidao;
	private String nomePessoaA;
	private String nomePessoaB;
	private boolean possuiAverbacao;
	private String valorCertidao;
	private String valorAverbacao;
	private String valorEmolumentos;
	private String valorRecompe;
	private String valorTaxaFiscalizacao;
	private String valorISSQN;
	private String valorAverbacaoRequisitante;
	private String valorTransmissao;
	private String valorTotalCertidao;
	private String enderecoCartorioRequisitado;
	private boolean pedidoCartorioParaCartorio;
	private boolean exibeEnderecoRetirada;

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getNomeRequerente() {
		return nomeRequerente;
	}

	public void setNomeRequerente(String nomeRequerente) {
		this.nomeRequerente = nomeRequerente;
	}

	public String getTipoCertidao() {
		return tipoCertidao;
	}

	public void setTipoCertidao(String tipoCertidao) {
		this.tipoCertidao = tipoCertidao;
	}

	public String getNomePessoaA() {
		return nomePessoaA;
	}

	public void setNomePessoaA(String nomePessoaA) {
		this.nomePessoaA = nomePessoaA;
	}

	public String getNomePessoaB() {
		return nomePessoaB;
	}

	public void setNomePessoaB(String nomePessoaB) {
		this.nomePessoaB = nomePessoaB;
	}

	public boolean isPossuiAverbacao() {
		return possuiAverbacao;
	}

	public void setPossuiAverbacao(boolean possuiAverbacao) {
		this.possuiAverbacao = possuiAverbacao;
	}

	public String getValorCertidao() {
		return valorCertidao;
	}

	public void setValorCertidao(String valorCertidao) {
		this.valorCertidao = valorCertidao;
	}

	public String getValorAverbacao() {
		return valorAverbacao;
	}

	public void setValorAverbacao(String valorAverbacao) {
		this.valorAverbacao = valorAverbacao;
	}

	public String getValorEmolumentos() {
		return valorEmolumentos;
	}

	public void setValorEmolumentos(String valorEmolumentos) {
		this.valorEmolumentos = valorEmolumentos;
	}

	public String getValorRecompe() {
		return valorRecompe;
	}

	public void setValorRecompe(String valorRecompe) {
		this.valorRecompe = valorRecompe;
	}

	public String getValorTaxaFiscalizacao() {
		return valorTaxaFiscalizacao;
	}

	public void setValorTaxaFiscalizacao(String valorTaxaFiscalizacao) {
		this.valorTaxaFiscalizacao = valorTaxaFiscalizacao;
	}

	public String getValorISSQN() {
		return valorISSQN;
	}

	public void setValorISSQN(String valorISSQN) {
		this.valorISSQN = valorISSQN;
	}
	
	public String getValorAverbacaoRequisitante() {
		return valorAverbacaoRequisitante;
	}

	public void setValorAverbacaoRequisitante(String valorAverbacaoRequisitante) {
		this.valorAverbacaoRequisitante = valorAverbacaoRequisitante;
	}

	public String getValorTransmissao() {
		return valorTransmissao;
	}

	public void setValorTransmissao(String valorTransmissao) {
		this.valorTransmissao = valorTransmissao;
	}

	public String getValorTotalCertidao() {
		return valorTotalCertidao;
	}

	public void setValorTotalCertidao(String valorTotalCertidao) {
		this.valorTotalCertidao = valorTotalCertidao;
	}

	public String getEnderecoCartorioRequisitado() {
		return enderecoCartorioRequisitado;
	}

	public void setEnderecoCartorioRequisitado(String enderecoCartorioRequisitado) {
		this.enderecoCartorioRequisitado = enderecoCartorioRequisitado;
	}

	public boolean isPedidoCartorioParaCartorio() {
		return pedidoCartorioParaCartorio;
	}

	public void setPedidoCartorioParaCartorio(boolean pedidoCartorioParaCartorio) {
		this.pedidoCartorioParaCartorio = pedidoCartorioParaCartorio;
	}

	public boolean isExibeEnderecoRetirada() {
		return exibeEnderecoRetirada;
	}

	public void setExibeEnderecoRetirada(boolean exibeEnderecoRetirada) {
		this.exibeEnderecoRetirada = exibeEnderecoRetirada;
	}
	
}
