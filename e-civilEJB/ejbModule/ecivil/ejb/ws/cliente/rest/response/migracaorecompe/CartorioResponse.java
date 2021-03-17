package ecivil.ejb.ws.cliente.rest.response.migracaorecompe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import ecivil.ejb.util.Util;
import ecivil.ejb.vo.DadosBancariosVO;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartorioResponse {

	private Long idCartorio;
	private String nomeCartorio;
	private String nomeDistritoECartorio;
	private String codigoCorregedoria;
	private String cns;
	private String cep;
	private String logradouro;
	private String numeroLogradouro;
	private String bairro;
	private String nomeMunicipio;
	private String codigoMunicipio;
	private String distrito;
	private String siglaUf;
	private String telefone;
	private String email;
	private String cpfCnpjTitularConta;
	private String nomeTitularConta;
	private String codigoBanco;
	private String nomeBanco;
	private String agencia;
	private String conta;
	private String tipoConta;
	
	@JsonIgnore
	private DadosBancariosVO DadosBancariosVO;
	
	public String obtemDescricaoEndereco() {
		StringBuffer sb = new StringBuffer();
		sb.append(getLogradouro());
		sb.append(", ");
		sb.append(getNumeroLogradouro());
		sb.append(", ");
		sb.append(getBairro());
		sb.append(", ");
		sb.append(getNomeMunicipio());
		sb.append(" - ");
		sb.append(getSiglaUf());
		sb.append(" - ");
		sb.append(Util.addMaskCEP(getCep()));
		return sb.toString();
	}
	
	public String getCodigoCorregedoriaFormatado() {
		return Util.formataCodigCartorio(getCodigoCorregedoria());
	}

	public String getCepFormatado() {
		return Util.addMaskCEP(getCep());
	}
	
	public Long getIdCartorio() {
		return idCartorio;
	}

	public void setIdCartorio(Long idCartorio) {
		this.idCartorio = idCartorio;
	}

	public String getNomeCartorio() {
		return nomeCartorio;
	}

	public void setNomeCartorio(String nomeCartorio) {
		this.nomeCartorio = nomeCartorio;
	}

	public String getNomeDistritoECartorio() {
		return nomeDistritoECartorio;
	}

	public void setNomeDistritoECartorio(String nomeDistritoECartorio) {
		this.nomeDistritoECartorio = nomeDistritoECartorio;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}
	
	public String getCns() {
		return cns;
	}

	public void setCns(String cns) {
		this.cns = cns;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumeroLogradouro() {
		return numeroLogradouro;
	}

	public void setNumeroLogradouro(String numeroLogradouro) {
		this.numeroLogradouro = numeroLogradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getNomeMunicipio() {
		return nomeMunicipio;
	}

	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}
	
	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getSiglaUf() {
		return siglaUf;
	}

	public void setSiglaUf(String siglaUf) {
		this.siglaUf = siglaUf;
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

	public String getCpfCnpjTitularConta() {
		return cpfCnpjTitularConta;
	}

	public void setCpfCnpjTitularConta(String cpfCnpjTitularConta) {
		this.cpfCnpjTitularConta = cpfCnpjTitularConta;
	}

	public String getNomeTitularConta() {
		return nomeTitularConta;
	}

	public void setNomeTitularConta(String nomeTitularConta) {
		this.nomeTitularConta = nomeTitularConta;
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

	public DadosBancariosVO getDadosBancariosVO() {
		return DadosBancariosVO;
	}

	public void setDadosBancariosVO(DadosBancariosVO dadosBancariosVO) {
		DadosBancariosVO = dadosBancariosVO;
	}

}
