package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "cnj", schema = "ecivil")
public class CNJ {

	@Id
	@Column(name = "cnj_id")
	private Integer cnjId;
		
	@Column(name = "cnj_nomecartorio")
	private String cnjNomeCartorio;

	@Column(name = "cnj_cns")
	private String cnjCns;
	
	@Column(name = "cnj_nomeoficial")
	private String cnjNomeOficial;
	
	@Column(name = "cnj_email")
	private String cnjEmail; 
	
	@Column(name = "cnj_distrito")
	private String cnjDistrito;
	
	@Column(name = "cnj_cpf")
	private String cnjCpf; 
	
	@Column(name = "cnj_idmunicipio")
	private Integer cnjIdMunicipio;
	
	@Column(name = "cnj_status")
	private Integer cnjStatus;
	
	@Column(name = "cnj_cdcorregedoria")
	private String cnjCdCorregedoria;
	
	@Column(name = "cnj_banconome")
	private String cnjBancoNome;
	
	@Column(name = "cnj_banconumero")
	private String cnjBancoNumero;
	
	@Column(name = "cnj_bancoagencia")
	private String cnjBancoAgencia;
	
	@Column(name = "cnj_bancoconta")
	private String cnjBancoConta;
	
	@Column(name = "cnj_bancocontatipo")
	private String cnjBancoContaTipo;
	
	@Column(name = "cnj_banconometitular")
	private String cnjBancoNomeTitular;
	
	@Column(name = "cnj_bancodocumentotitular")
	private String cnjBancoDocumentoTitular;
	
	@Column(name = "cnj_idcartosoft")
	private Integer cnjIdCartosoft;
	
	@Column(name = "cnj_telefone")
	private String cnjTelefone;
	
	@Column(name = "cnj_logradourotipo")
	private String cnjLogradouroTipo;
	
	
	@Column(name = "cnj_logradouronome")
	private String cnjLogradouroNome;
	
	@Column(name = "cnj_logradouronumero")
	private String cnjLogradouroNumero;
	
	@Column(name = "cnj_logradourocomplemento")
	private String cnjLogradouroComplemento;
	
	@Column(name = "cnj_logradourobairro")
	private String cnjLogradouroBairro;
	
	@Column(name = "cnj_logradourocep")
	private String cnjLogradouroCep;
	
	@Column(name = "cnj_crcnacionalhabilitado")
	private String cnjCrcNacionalHabilitado;
	
	@Column(name = "cnj_atribuicao")
	private Integer cnjAtribuicao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "cnj_jobregistrodtatualizacao")
	private Date cnjJobRegistroDtAtualizacao;

	public Integer getCnjId() {
		return cnjId;
	}

	public String getCnjNomeCartorio() {
		return cnjNomeCartorio;
	}

	public String getCnjCns() {
		return cnjCns;
	}

	public String getCnjNomeOficial() {
		return cnjNomeOficial;
	}

	public String getCnjEmail() {
		return cnjEmail;
	}

	public String getCnjDistrito() {
		return cnjDistrito;
	}

	public String getCnjCpf() {
		return cnjCpf;
	}

	public Integer getCnjIdMunicipio() {
		return cnjIdMunicipio;
	}

	public Integer getCnjStatus() {
		return cnjStatus;
	}

	public String getCnjCdCorregedoria() {
		return cnjCdCorregedoria;
	}

	public String getCnjBancoNome() {
		return cnjBancoNome;
	}

	public String getCnjBancoNumero() {
		return cnjBancoNumero;
	}

	public String getCnjBancoAgencia() {
		return cnjBancoAgencia;
	}

	public String getCnjBancoConta() {
		return cnjBancoConta;
	}

	public String getCnjBancoContaTipo() {
		return cnjBancoContaTipo;
	}

	public String getCnjBancoNomeTitular() {
		return cnjBancoNomeTitular;
	}

	public String getCnjBancoDocumentoTitular() {
		return cnjBancoDocumentoTitular;
	}

	public Integer getCnjIdCartosoft() {
		return cnjIdCartosoft;
	}

	public String getCnjTelefone() {
		return cnjTelefone;
	}

	public String getCnjLogradouroTipo() {
		return cnjLogradouroTipo;
	}

	public String getCnjLogradouroNome() {
		return cnjLogradouroNome;
	}

	public String getCnjLogradouroNumero() {
		return cnjLogradouroNumero;
	}

	public String getCnjLogradouroComplemento() {
		return cnjLogradouroComplemento;
	}

	public String getCnjLogradouroBairro() {
		return cnjLogradouroBairro;
	}

	public String getCnjLogradouroCep() {
		return cnjLogradouroCep;
	}

	public String getCnjCrcNacionalHabilitado() {
		return cnjCrcNacionalHabilitado;
	}

	public Integer getCnjAtribuicao() {
		return cnjAtribuicao;
	}

	public Date getCnjJobRegistroDtAtualizacao() {
		return cnjJobRegistroDtAtualizacao;
	}

	public void setCnjId(Integer cnjId) {
		this.cnjId = cnjId;
	}

	public void setCnjNomeCartorio(String cnjNomeCartorio) {
		this.cnjNomeCartorio = cnjNomeCartorio;
	}

	public void setCnjCns(String cnjCns) {
		this.cnjCns = cnjCns;
	}

	public void setCnjNomeOficial(String cnjNomeOficial) {
		this.cnjNomeOficial = cnjNomeOficial;
	}

	public void setCnjEmail(String cnjEmail) {
		this.cnjEmail = cnjEmail;
	}

	public void setCnjDistrito(String cnjDistrito) {
		this.cnjDistrito = cnjDistrito;
	}

	public void setCnjCpf(String cnjCpf) {
		this.cnjCpf = cnjCpf;
	}

	public void setCnjIdMunicipio(Integer cnjIdMunicipio) {
		this.cnjIdMunicipio = cnjIdMunicipio;
	}

	public void setCnjStatus(Integer cnjStatus) {
		this.cnjStatus = cnjStatus;
	}

	public void setCnjCdCorregedoria(String cnjCdCorregedoria) {
		this.cnjCdCorregedoria = cnjCdCorregedoria;
	}

	public void setCnjBancoNome(String cnjBancoNome) {
		this.cnjBancoNome = cnjBancoNome;
	}

	public void setCnjBancoNumero(String cnjBancoNumero) {
		this.cnjBancoNumero = cnjBancoNumero;
	}

	public void setCnjBancoAgencia(String cnjBancoAgencia) {
		this.cnjBancoAgencia = cnjBancoAgencia;
	}

	public void setCnjBancoConta(String cnjBancoConta) {
		this.cnjBancoConta = cnjBancoConta;
	}

	public void setCnjBancoContaTipo(String cnjBancoContaTipo) {
		this.cnjBancoContaTipo = cnjBancoContaTipo;
	}

	public void setCnjBancoNomeTitular(String cnjBancoNomeTitular) {
		this.cnjBancoNomeTitular = cnjBancoNomeTitular;
	}

	public void setCnjBancoDocumentoTitular(String cnjBancoDocumentoTitular) {
		this.cnjBancoDocumentoTitular = cnjBancoDocumentoTitular;
	}

	public void setCnjIdCartosoft(Integer cnjIdCartosoft) {
		this.cnjIdCartosoft = cnjIdCartosoft;
	}

	public void setCnjTelefone(String cnjTelefone) {
		this.cnjTelefone = cnjTelefone;
	}

	public void setCnjLogradouroTipo(String cnjLogradouroTipo) {
		this.cnjLogradouroTipo = cnjLogradouroTipo;
	}

	public void setCnjLogradouroNome(String cnjLogradouroNome) {
		this.cnjLogradouroNome = cnjLogradouroNome;
	}

	public void setCnjLogradouroNumero(String cnjLogradouroNumero) {
		this.cnjLogradouroNumero = cnjLogradouroNumero;
	}

	public void setCnjLogradouroComplemento(String cnjLogradouroComplemento) {
		this.cnjLogradouroComplemento = cnjLogradouroComplemento;
	}

	public void setCnjLogradouroBairro(String cnjLogradouroBairro) {
		this.cnjLogradouroBairro = cnjLogradouroBairro;
	}

	public void setCnjLogradouroCep(String cnjLogradouroCep) {
		this.cnjLogradouroCep = cnjLogradouroCep;
	}

	public void setCnjCrcNacionalHabilitado(String cnjCrcNacionalHabilitado) {
		this.cnjCrcNacionalHabilitado = cnjCrcNacionalHabilitado;
	}

	public void setCnjAtribuicao(Integer cnjAtribuicao) {
		this.cnjAtribuicao = cnjAtribuicao;
	}

	public void setCnjJobRegistroDtAtualizacao(Date cnjJobRegistroDtAtualizacao) {
		this.cnjJobRegistroDtAtualizacao = cnjJobRegistroDtAtualizacao;
	}
}

