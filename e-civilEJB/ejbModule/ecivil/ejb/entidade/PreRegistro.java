package ecivil.ejb.entidade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ecivil.ejb.dao.EntidadeConverterDAO;
import ecivil.ejb.util.DataUtil;


@Entity
@Table(name = "pre_registro", schema = "ecivil")
@SequenceGenerator(name = "seq_id_pre_registro", sequenceName = "seq_id_pre_registro", schema = "ecivil", allocationSize = 1)
public class PreRegistro extends EntidadeConverterDAO {

	//DADOS DA SOLICITAÇÃO
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_pre_registro")
	private Long id;

	@Column(name = "codigo_hash")
	private String codigoHash;
	
	@Column(name = "numero_solicitacao")
	private String numeroSolicitacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio_solicitacao")
	private Date dataInicioSolicitacao; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim_solicitacao")
	private Date dataFimSolicitacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_alteracao_solicitacao")
	private Date dataAlteracaoSolicitacao; 
	
	@Column(name = "situacao_solicitacao")
	private String situacaoSolicitacao;

	@Transient
	private PreRegistroXML xml;
	
	@Column(name = "pais_residencia_serventia_escolhida") 
	private String paisResidenciaServentiaEscolhida;
	
	@OrderBy("dataHoraAlteracao ASC")
	@OneToMany(mappedBy = "preRegistro", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<HistoricoPreRegistro> listaHistoricoPreRegistro;
	

	//DADOS UI
	@Column(name = "municipio_ui")
	private String municipioUi; //MUNICÍO UNIDADE INTERLIGADA;
	
	@Column(name = "uf_ui")
	private String ufUi; //UF UNIDADE INTERLIGADA;
	
	@Column(name = "nome_ui")
	private String nomeUi; //NOME UNIDADE INTERLIGADA;
	
	@Column(name = "bairro_ui")
	private String bairroUi; //BAIRRO UNIDADE INTERLIGADA;
	
	@Column(name = "endereco_ui")
	private String logradouroUi; //ENDEREÇO UNIDADE INTERLIGADA;
	
	@Column(name = "codigo_ui")
	private String codigoUi; //CÓDIGO UNIDADE INTERLIGADA;
	
	
	//DADOS CRIANÇAS
	@Column(name = "naturalidade_UF")
	private String naturalidadeUf;
	
	@Column(name = "naturalidade")
	private String naturalidade;
	
	@Column(name = "quantidade_criancas")
	private String quantidadeCriancas;
	
	
	//DADOS CRIANÇA 1
	@Column(name = "nome_crianca1")
	private String nomeCrianca1;
	
	@Column(name = "sexo_crianca1")
	private String sexoCrianca1;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_nascimento_crianca1")
	private Date dataNascimentoCrianca1;
	
	@Column(name = "hora_nascimento_crianca1")
	private String horaNascimentoCrianca1;
	
	@Column(name = "dnv_crianca1")
	private String dnvCrianca1;
	
	
	//DADOS CRIANÇA2
	@Column(name = "nome_crianca2")
	private String nomeCrianca2;
	
	@Column(name = "sexo_crianca2")
	private String sexoCrianca2;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_nascimento_crianca2")
	private Date dataNascimentoCrianca2;

	@Column(name = "hora_nascimento_crianca2")
	private String horaNascimentoCrianca2;
	
	@Column(name = "dnv_crianca2")
	private String dnvCrianca2;
	
	
	//DADOS CRIANÇA 3
	@Column(name = "nome_crianca3")
	private String nomeCrianca3;
	
	@Column(name = "sexo_crianca3")
	private String sexoCrianca3;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_nascimento_crianca3")
	private Date dataNascimentoCrianca3;
	
	@Column(name = "hora_nascimento_crianca3")
	private String horaNascimentoCrianca3;
	
	@Column(name = "dnv_crianca3")
	private String dnvCrianca3;
	
	
	//DADOS CRIANÇA 4
	@Column(name = "nome_crianca4")
	private String nomeCrianca4;
	
	@Column(name = "sexo_crianca4")
	private String sexoCrianca4;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_nascimento_crianca4")
	private Date dataNascimentoCrianca4;
	
	@Column(name = "hora_nascimento_crianca4")
	private String horaNascimentoCrianca4;
	
	@Column(name = "dnv_crianca4")
	private String dnvCrianca4;
	
	
	//DADOS CRIANÇA 5
	@Column(name = "nome_crianca5")
	private String nomeCrianca5;
	
	@Column(name = "sexo_crianca5")
	private String sexoCrianca5;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_nascimento_crianca5")
	private Date dataNascimentoCrianca5;
	
	@Column(name = "hora_nascimento_crianca5")
	private String horaNascimentoCrianca5;
	
	@Column(name = "dnv_crianca5")
	private String dnvCrianca5;	
	
	
	//DADOS MÃE
	@Column(name = "nome_mae")
	private String nomeMae;
	
	@Column(name = "cpf_mae")
	private String cpfMae;
	
	@Column(name = "naturalidade_uf_mae")
	private String naturalidadeUfMae;
	
	@Column(name = "naturalidade_mae")
	private String naturalidadeMae;
	
	@Column(name = "naturalidade_distrito_mae")
	private String naturalidadeDistritoMae;
	
	@Column(name = "nacionalidade_mae")
	private String nacionalidadeMae;
	
	@Column(name = "res_uf_mae")
	private String residenciaUfMae;
	
	@Column(name = "res_municipio_mae")
	private String residenciaMunicipioMae;
	
	@Column(name = "res_distrito_mae")
	private String residenciaDistritoMae;
	
	@Column(name = "res_bairro_mae")
	private String residenciaBairroMae;
	
	@Column(name = "res_logradouro_mae")
	private String residenciaLogradouroMae;
	
	@Column(name = "res_numero_mae")
	private String residenciaNumeroMae;
	
	@Column(name = "res_complemento_mae")
	private String residenciaComplementoMae;
	
	@Column(name = "res_CEP_mae")
	private String residenciaCEPMae;
	
	@Column(name = "profissao_mae")
	private String profissaoMae;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento_mae")
	private Date dataNascimentoMae;
	
	@Column(name = "documento_mae")
	private String documentoMae;
	
	@Column(name = "tipo_documento_mae")
	private String tipoDocumentoMae;
	
	@Column(name = "filiacao_pai_mae")
	private String filiacaoPaiMae;
	
	@Column(name = "pai_mae_falecido")
	private Boolean paiMaeFalecido;
	
	@Column(name = "filiacao_mae_mae")
	private String filiacaoMaeMae;
	
	@Column(name = "mae_mae_falecida")
	private Boolean maeMaeFalecida;
	
	@Column(name = "idade_mae")
	private Integer idadeMae;
	

	//DADOS PAI
	@Column(name = "nome_pai")
	private String nomePai;
	
	@Column(name = "cpf_pai")
	private String cpfPai;
	
	@Column(name = "naturalidade_uf_pai")
	private String naturalidadeUfPai;
	
	@Column(name = "naturalidade_pai")
	private String naturalidadePai;
	
	@Column(name = "naturalidade_distrito_pai")
	private String naturalidadeDistritoPai;
	
	@Column(name = "nacionalidade_pai")
	private String nacionalidadePai;
	
	@Column(name = "res_uf_pai")
	private String residenciaUfPai;
	
	@Column(name = "res_municipio_pai")
	private String residenciaMunicipioPai;
	
	@Column(name = "res_distrito_pai")
	private String residenciaDistritoPai;
	
	@Column(name = "res_bairro_pai")
	private String residenciaBairroPai;
	
	@Column(name = "res_logradouro_pai")
	private String residenciaLogradouroPai;
	
	@Column(name = "res_numero_pai")
	private String residenciaNumeroPai;
	
	@Column(name = "res_complemento_pai")
	private String residenciaComplementoPai;
	
	@Column(name = "res_CEP_pai")
	private String residenciaCEPpai;
	
	@Column(name = "profissao_pai")
	private String profissaoPai;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento_pai")
	private Date dataNascimentoPai;
	
	@Column(name = "documento_pai")
	private String documentoPai;
	
	@Column(name = "tipo_documento_pai")
	private String tipoDocumentoPai;
	
	@Column(name = "filiacao_pai_pai")
	private String filiacaoPaiPai;
	
	@Column(name = "pai_pai_falecido")
	private Boolean paiPaiFalecido;
	
	@Column(name = "filiacao_mae_pai")
	private String filiacaoMaePai;
	
	@Column(name = "mae_pai_falecida")
	private Boolean maePaiFalecida;
	
	@Column(name = "status_pai")
	private String statusPai;
	
	@Column(name = "idade_pai")
	private Integer idadePai;
	

	//DADOS CARTÓRIO
	@Column(name = "opcao_registro")
	private String opcaoRegistro;
	
	@Column(name = "serventia")
	private String serventia;
	
	@Column(name = "cns_cartorio")
	private String cnsCartorio;
	
	@Column(name = "cd_corregedoria_cartorio")
	private String codCorregedoriaCartorio;
	
	
	//DADOS DECLARANTE
	@Column(name = "tipo_declarante")
	private String tipoDeclarante;
	
	@Column(name = "tipo_declarante_pre_registro")
	private String tipoDeclarantePreRegistro;
	
	@Column(name = "tipo_declarante_solicitacao")
	private String tipoDeclaranteNaSolicitacao;
	
	@Column(name = "tipo_declaracao")
	private String tipoDeclaracao;
	
	@Column(name = "nome_declarante")
	private String nomeDeclarante;
	
	@Column(name = "doc_declarante")
	private String docDeclarante;
	
	@Column(name = "tipo_doc_declarante")
	private String tipoDocDeclarante;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento_declarante")
	private Date dataNascimentoDeclarante;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento_crianca_declarante")
	private Date dataNascimentoCriancaDeclarante;
	
	@Column(name = "profissao_declarante")
	private String profissaoDeclarante;
	
	@Column(name = "nacionalidade_declarante")
	private String nacionalidadeDeclarante;
	
	@Column(name = "uf_residencia_declarante")
	private String ufResidenciaDeclarante;
	
	@Column(name = "municipio_residencia_declarante")
	private String municipioResidenciaDeclarante;
	
	@Column(name = "bairro_residencia_declarante")
	private String bairroResidenciaDeclarante;
	
	@Column(name = "logradouro_residencia_declarante")
	private String logradouroResidenciaDeclarante;
	
	@Column(name = "numero_residencia_declarante")
	private String numeroResidenciaDeclarante;
	
	@Column(name = "complemento_residencia_declarante")
	private String complementoResidenciaDeclarante;
	
	@Column(name = "nome_declarante_pre_registro")
	private String nomeDeclarantePreRegistro;
	
	@Column(name = "cpf_declarante_pre_registro")
	private String cpfDeclarantePreRegistro;
	
	@Column(name = "contato_declarante_pre_registro")
	private String contatoDeclarantePreRegistro;
	
	@Column(name = "dados_processo")
	private String dadosProcesso;
	
	@Column(name = "dados_procuracao")
	private String dadosProcuracao;
	
	@Column(name = "dados_quarto_hospedagem_mae")
	private String dadosQuartoHospedagemMae;
	
	
	//DADOS DO CONTATO
	@Column(name = "nome_contato")
	private String nomeContato;
	
	@Column(name = "tel_fixo_contato")
	private String telFixoContato;
	
	@Column(name = "tel_celular_contato")
	private String telCelularContato;
	
	@Column(name = "contato_mae")
	private String contatoMae;
	
	@Column(name = "contato_pai")
	private String contatoPai;
	
	
	//GETTERS E SETTERS
	
	public List<HistoricoPreRegistro> getListaHistoricoPreRegistroInicializada() {
		if (listaHistoricoPreRegistro == null) {
			listaHistoricoPreRegistro = new ArrayList<>();
		}
		return listaHistoricoPreRegistro;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigoHash() {
		return codigoHash;
	}
	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}
	public String getNumeroSolicitacao() {
		return numeroSolicitacao;
	}
	public void setNumeroSolicitacao(String numeroSolicitacao) {
		this.numeroSolicitacao = numeroSolicitacao;
	}
	public Date getDataInicioSolicitacao() {
		return dataInicioSolicitacao;
	}
	public void setDataInicioSolicitacao(Date dataInicioSolicitacao) {
		this.dataInicioSolicitacao = dataInicioSolicitacao;
	}
	public Date getDataFimSolicitacao() {
		return dataFimSolicitacao;
	}
	public void setDataFimSolicitacao(Date dataFimSolicitacao) {
		this.dataFimSolicitacao = dataFimSolicitacao;
	}
	public String getMunicipioUi() {
		return municipioUi;
	}
	public void setMunicipioUi(String municipioUi) {
		this.municipioUi = municipioUi;
	}
	public String getUfUi() {
		return ufUi;
	}
	public void setUfUi(String ufUi) {
		this.ufUi = ufUi;
	}
	public String getNomeUi() {
		return nomeUi;
	}
	public void setNomeUi(String nomeUi) {
		this.nomeUi = nomeUi;
	}
	public String getBairroUi() {
		return bairroUi;
	}
	public void setBairroUi(String bairroUi) {
		this.bairroUi = bairroUi;
	}
	public String getLogradouroUi() {
		return logradouroUi;
	}
	public void setLogradouroUi(String logradouroUi) {
		this.logradouroUi = logradouroUi;
	}
	public String getCodigoUi() {
		return codigoUi;
	}
	public void setCodigoUi(String coCd) {
		this.codigoUi = coCd;
	}
	public String getQuantidadeCriancas() {
		return quantidadeCriancas;
	}
	public void setQuantidadeCriancas(String quantidadeCriancas) {
		this.quantidadeCriancas = quantidadeCriancas;
	}
	public String getNomeCrianca1() {
		return nomeCrianca1;
	}
	public void setNomeCrianca1(String nomeCrianca1) {
		this.nomeCrianca1 = nomeCrianca1;
	}
	public String getSexoCrianca1() {
		return sexoCrianca1;
	}
	public void setSexoCrianca1(String sexoCrianca1) {
		this.sexoCrianca1 = sexoCrianca1;
	}
	public Date getDataNascimentoCrianca1() {
		return dataNascimentoCrianca1;
	}
	public void setDataNascimentoCrianca1(Date dataNascimentoCrianca1) {
		this.dataNascimentoCrianca1 = dataNascimentoCrianca1;
	}
	
	public String getDnvCrianca1() {
		return dnvCrianca1;
	}
	public void setDnvCrianca1(String dnvCrianca1) {
		this.dnvCrianca1 = dnvCrianca1;
	}
	public String getNaturalidadeUf() {
		return naturalidadeUf;
	}
	public void setNaturalidadeUf(String naturalidadeUf) {
		this.naturalidadeUf = naturalidadeUf;
	}
	public String getNaturalidade() {
		return naturalidade;
	}
	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}
	public String getNomeCrianca2() {
		return nomeCrianca2;
	}
	public void setNomeCrianca2(String nomeCrianca2) {
		this.nomeCrianca2 = nomeCrianca2;
	}
	public String getSexoCrianca2() {
		return sexoCrianca2;
	}
	public void setSexoCrianca2(String sexoCrianca2) {
		this.sexoCrianca2 = sexoCrianca2;
	}
	public String getNomeCrianca3() {
		return nomeCrianca3;
	}
	public void setNomeCrianca3(String nomeCrianca3) {
		this.nomeCrianca3 = nomeCrianca3;
	}
	public String getSexoCrianca3() {
		return sexoCrianca3;
	}
	public void setSexoCrianca3(String sexoCrianca3) {
		this.sexoCrianca3 = sexoCrianca3;
	}
	public String getNomeCrianca4() {
		return nomeCrianca4;
	}
	public void setNomeCrianca4(String nomeCrianca4) {
		this.nomeCrianca4 = nomeCrianca4;
	}
	public String getSexoCrianca4() {
		return sexoCrianca4;
	}
	public void setSexoCrianca4(String sexoCrianca4) {
		this.sexoCrianca4 = sexoCrianca4;
	}
	public String getNomeCrianca5() {
		return nomeCrianca5;
	}
	public void setNomeCrianca5(String nomeCrianca5) {
		this.nomeCrianca5 = nomeCrianca5;
	}
	public String getSexoCrianca5() {
		return sexoCrianca5;
	}
	public void setSexoCrianca5(String sexoCrianca5) {
		this.sexoCrianca5 = sexoCrianca5;
	}
	public String getNomeMae() {
		return nomeMae;
	}
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}
	public String getCpfMae() {
		return cpfMae;
	}
	public void setCpfMae(String cpfMae) {
		this.cpfMae = cpfMae;
	}
	public String getNaturalidadeUfMae() {
		return naturalidadeUfMae;
	}
	public void setNaturalidadeUfMae(String naturalidadeUfMae) {
		this.naturalidadeUfMae = naturalidadeUfMae;
	}
	public String getNaturalidadeMae() {
		return naturalidadeMae;
	}
	public void setNaturalidadeMae(String naturalidadeMae) {
		this.naturalidadeMae = naturalidadeMae;
	}
	public String getNaturalidadeDistritoMae() {
		return naturalidadeDistritoMae;
	}
	public void setNaturalidadeDistritoMae(String naturalidadeDistritoMae) {
		this.naturalidadeDistritoMae = naturalidadeDistritoMae;
	}
	public String getNacionalidadeMae() {
		return nacionalidadeMae;
	}
	public void setNacionalidadeMae(String nacionalidadeMae) {
		this.nacionalidadeMae = nacionalidadeMae;
	}
	public String getResidenciaUfMae() {
		return residenciaUfMae;
	}
	public void setResidenciaUfMae(String residenciaUfMae) {
		this.residenciaUfMae = residenciaUfMae;
	}
	public String getResidenciaMunicipioMae() {
		return residenciaMunicipioMae;
	}
	public void setResidenciaMunicipioMae(String residenciaMunicipioMae) {
		this.residenciaMunicipioMae = residenciaMunicipioMae;
	}
	public String getResidenciaDistritoMae() {
		return residenciaDistritoMae;
	}
	public void setResidenciaDistritoMae(String residenciaDistritoMae) {
		this.residenciaDistritoMae = residenciaDistritoMae;
	}
	public String getResidenciaBairroMae() {
		return residenciaBairroMae;
	}
	public void setResidenciaBairroMae(String residenciaBairroMae) {
		this.residenciaBairroMae = residenciaBairroMae;
	}
	public String getResidenciaLogradouroMae() {
		return residenciaLogradouroMae;
	}
	public void setResidenciaLogradouroMae(String residenciaLogradouroMae) {
		this.residenciaLogradouroMae = residenciaLogradouroMae;
	}
	public String getResidenciaNumeroMae() {
		return residenciaNumeroMae;
	}
	public void setResidenciaNumeroMae(String residenciaNumeroMae) {
		this.residenciaNumeroMae = residenciaNumeroMae;
	}
	public String getResidenciaComplementoMae() {
		return residenciaComplementoMae;
	}
	public void setResidenciaComplementoMae(String residenciaComplementoMae) {
		this.residenciaComplementoMae = residenciaComplementoMae;
	}
	public String getResidenciaCEPMae() {
		return residenciaCEPMae;
	}
	public void setResidenciaCEPMae(String residenciaCEPMae) {
		this.residenciaCEPMae = residenciaCEPMae;
	}
	public String getProfissaoMae() {
		return profissaoMae;
	}
	public void setProfissaoMae(String profissaoMae) {
		this.profissaoMae = profissaoMae;
	}
	public Date getDataNascimentoMae() {
		return dataNascimentoMae;
	}
	public void setDataNascimentoMae(Date dataNascimentoMae) {
		this.dataNascimentoMae = dataNascimentoMae;
	}
	public String getDocumentoMae() {
		return documentoMae;
	}
	public void setDocumentoMae(String documentoMae) {
		this.documentoMae = documentoMae;
	}
	public String getTipoDocumentoMae() {
		return tipoDocumentoMae;
	}
	public void setTipoDocumentoMae(String tipoDocumentoMae) {
		this.tipoDocumentoMae = tipoDocumentoMae;
	}
	public String getFiliacaoPaiMae() {
		return filiacaoPaiMae;
	}
	public void setFiliacaoPaiMae(String filiacaoPaiMae) {
		this.filiacaoPaiMae = filiacaoPaiMae;
	}
	public Boolean getPaiMaeFalecido() {
		return paiMaeFalecido;
	}
	public void setPaiMaeFalecido(Boolean paiMaeFalecido) {
		this.paiMaeFalecido = paiMaeFalecido;
	}
	public String getFiliacaoMaeMae() {
		return filiacaoMaeMae;
	}
	public void setFiliacaoMaeMae(String filiacaoMaeMae) {
		this.filiacaoMaeMae = filiacaoMaeMae;
	}
	public Boolean getMaeMaeFalecida() {
		return maeMaeFalecida;
	}
	public void setMaeMaeFalecida(Boolean maeMaeFalecida) {
		this.maeMaeFalecida = maeMaeFalecida;
	}
	public String getNomePai() {
		return nomePai;
	}
	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}
	public String getCpfPai() {
		return cpfPai;
	}
	public void setCpfPai(String cpfPai) {
		this.cpfPai = cpfPai;
	}
	public String getNaturalidadeUfPai() {
		return naturalidadeUfPai;
	}
	public void setNaturalidadeUfPai(String naturalidadeUfPai) {
		this.naturalidadeUfPai = naturalidadeUfPai;
	}
	public String getNaturalidadePai() {
		return naturalidadePai;
	}
	public void setNaturalidadePai(String naturalidadePai) {
		this.naturalidadePai = naturalidadePai;
	}
	public String getNaturalidadeDistritoPai() {
		return naturalidadeDistritoPai;
	}
	public void setNaturalidadeDistritoPai(String naturalidadeDistritoPai) {
		this.naturalidadeDistritoPai = naturalidadeDistritoPai;
	}
	public String getNacionalidadePai() {
		return nacionalidadePai;
	}
	public void setNacionalidadePai(String nacionalidadePai) {
		this.nacionalidadePai = nacionalidadePai;
	}
	public String getResidenciaUfPai() {
		return residenciaUfPai;
	}
	public void setResidenciaUfPai(String residenciaUfPai) {
		this.residenciaUfPai = residenciaUfPai;
	}
	public String getResidenciaMunicipioPai() {
		return residenciaMunicipioPai;
	}
	public void setResidenciaMunicipioPai(String residenciaMunicipioPai) {
		this.residenciaMunicipioPai = residenciaMunicipioPai;
	}
	public String getResidenciaDistritoPai() {
		return residenciaDistritoPai;
	}
	public void setResidenciaDistritoPai(String residenciaDistritoPai) {
		this.residenciaDistritoPai = residenciaDistritoPai;
	}
	public String getResidenciaBairroPai() {
		return residenciaBairroPai;
	}
	public void setResidenciaBairroPai(String residenciaBairroPai) {
		this.residenciaBairroPai = residenciaBairroPai;
	}
	public String getResidenciaLogradouroPai() {
		return residenciaLogradouroPai;
	}
	public void setResidenciaLogradouroPai(String residenciaLogradouroPai) {
		this.residenciaLogradouroPai = residenciaLogradouroPai;
	}
	public String getResidenciaNumeroPai() {
		return residenciaNumeroPai;
	}
	public void setResidenciaNumeroPai(String residenciaNumeroPai) {
		this.residenciaNumeroPai = residenciaNumeroPai;
	}
	public String getResidenciaComplementoPai() {
		return residenciaComplementoPai;
	}
	public void setResidenciaComplementoPai(String residenciaComplementoPai) {
		this.residenciaComplementoPai = residenciaComplementoPai;
	}
	public String getResidenciaCEPpai() {
		return residenciaCEPpai;
	}
	public void setResidenciaCEPpai(String residenciaCEPpai) {
		this.residenciaCEPpai = residenciaCEPpai;
	}
	public String getProfissaoPai() {
		return profissaoPai;
	}
	public void setProfissaoPai(String profissaoPai) {
		this.profissaoPai = profissaoPai;
	}
	public Date getDataNascimentoPai() {
		return dataNascimentoPai;
	}
	public void setDataNascimentoPai(Date dataNascimentoPai) {
		this.dataNascimentoPai = dataNascimentoPai;
	}
	public String getDocumentoPai() {
		return documentoPai;
	}
	public void setDocumentoPai(String documentoPai) {
		this.documentoPai = documentoPai;
	}
	public String getTipoDocumentoPai() {
		return tipoDocumentoPai;
	}
	public void setTipoDocumentoPai(String tipoDocumentoPai) {
		this.tipoDocumentoPai = tipoDocumentoPai;
	}
	public String getFiliacaoPaiPai() {
		return filiacaoPaiPai;
	}
	public void setFiliacaoPaiPai(String filiacaoPaiPai) {
		this.filiacaoPaiPai = filiacaoPaiPai;
	}
	public Boolean getPaiPaiFalecido() {
		return paiPaiFalecido;
	}
	public void setPaiPaiFalecido(Boolean paiPaiFalecido) {
		this.paiPaiFalecido = paiPaiFalecido;
	}
	public String getFiliacaoMaePai() {
		return filiacaoMaePai;
	}
	public void setFiliacaoMaePai(String filiacaoMaePai) {
		this.filiacaoMaePai = filiacaoMaePai;
	}
	public Boolean getMaePaiFalecida() {
		return maePaiFalecida;
	}
	public void setMaePaiFalecida(Boolean maePaiFalecida) {
		this.maePaiFalecida = maePaiFalecida;
	}
	public String getStatusPai() {
		return statusPai;
	}
	public void setStatusPai(String statusPai) {
		this.statusPai = statusPai;
	}
	public String getOpcaoRegistro() {
		return opcaoRegistro;
	}
	public void setOpcaoRegistro(String opcaoRegistro) {
		this.opcaoRegistro = opcaoRegistro;
	}
	public String getServentia() {
		return serventia;
	}
	public void setServentia(String serventia) {
		this.serventia = serventia;
	}
	public String getCnsCartorio() {
		return cnsCartorio;
	}
	public void setCnsCartorio(String cnsCartorio) {
		this.cnsCartorio = cnsCartorio;
	}
	public String getCodCorregedoriaCartorio() {
		return codCorregedoriaCartorio;
	}
	public void setCodCorregedoriaCartorio(String codCorregedoriaCartorio) {
		this.codCorregedoriaCartorio = codCorregedoriaCartorio;
	}
	public String getTipoDeclarante() {
		return tipoDeclarante;
	}
	public void setTipoDeclarante(String tipoDeclarante) {
		this.tipoDeclarante = tipoDeclarante;
	}
	public String getTipoDeclaranteNaSolicitacao() {
		return tipoDeclaranteNaSolicitacao;
	}
	public void setTipoDeclaranteNaSolicitacao(String tipoDeclaranteNaSolicitacao) {
		this.tipoDeclaranteNaSolicitacao = tipoDeclaranteNaSolicitacao;
	}
	public String getTipoDeclaracao() {
		return tipoDeclaracao;
	}
	public void setTipoDeclaracao(String tipoDeclaracao) {
		this.tipoDeclaracao = tipoDeclaracao;
	}
	public String getNomeDeclarante() {
		return nomeDeclarante;
	}
	public void setNomeDeclarante(String nomeDeclarante) {
		this.nomeDeclarante = nomeDeclarante;
	}
	public String getDocDeclarante() {
		return docDeclarante;
	}
	public void setDocDeclarante(String docDeclarante) {
		this.docDeclarante = docDeclarante;
	}
	public String getTipoDocDeclarante() {
		return tipoDocDeclarante;
	}
	public void setTipoDocDeclarante(String tipoDocDeclarante) {
		this.tipoDocDeclarante = tipoDocDeclarante;
	}
	public Date getDataNascimentoCriancaDeclarante() {
		return dataNascimentoCriancaDeclarante;
	}
	public void setDataNascimentoCriancaDeclarante(Date dataNascimentoCriancaDeclarante) {
		this.dataNascimentoCriancaDeclarante = dataNascimentoCriancaDeclarante;
	}
	public String getProfissaoDeclarante() {
		return profissaoDeclarante;
	}
	public void setProfissaoDeclarante(String profissaoDeclarante) {
		this.profissaoDeclarante = profissaoDeclarante;
	}
	public String getNacionalidadeDeclarante() {
		return nacionalidadeDeclarante;
	}
	public void setNacionalidadeDeclarante(String nacionalidadeDeclarante) {
		this.nacionalidadeDeclarante = nacionalidadeDeclarante;
	}
	public String getUfResidenciaDeclarante() {
		return ufResidenciaDeclarante;
	}
	public void setUfResidenciaDeclarante(String ufResidenciaDeclarante) {
		this.ufResidenciaDeclarante = ufResidenciaDeclarante;
	}
	public String getMunicipioResidenciaDeclarante() {
		return municipioResidenciaDeclarante;
	}
	public void setMunicipioResidenciaDeclarante(String municipioResidenciaDeclarante) {
		this.municipioResidenciaDeclarante = municipioResidenciaDeclarante;
	}
	public String getBairroResidenciaDeclarante() {
		return bairroResidenciaDeclarante;
	}
	public void setBairroResidenciaDeclarante(String bairroResidenciaDeclarante) {
		this.bairroResidenciaDeclarante = bairroResidenciaDeclarante;
	}
	public String getLogradouroResidenciaDeclarante() {
		return logradouroResidenciaDeclarante;
	}
	public void setLogradouroResidenciaDeclarante(String logradouroResidenciaDeclarante) {
		this.logradouroResidenciaDeclarante = logradouroResidenciaDeclarante;
	}
	public String getNumeroResidenciaDeclarante() {
		return numeroResidenciaDeclarante;
	}
	public void setNumeroResidenciaDeclarante(String numeroResidenciaDeclarante) {
		this.numeroResidenciaDeclarante = numeroResidenciaDeclarante;
	}
	public String getComplementoResidenciaDeclarante() {
		return complementoResidenciaDeclarante;
	}
	public void setComplementoResidenciaDeclarante(String complementoResidenciaDeclarante) {
		this.complementoResidenciaDeclarante = complementoResidenciaDeclarante;
	}
	public String getDadosProcesso() {
		return dadosProcesso;
	}
	public void setDadosProcesso(String dadosProcesso) {
		this.dadosProcesso = dadosProcesso;
	}
	public String getDadosProcuracao() {
		return dadosProcuracao;
	}
	public void setDadosProcuracao(String dadosProcuracao) {
		this.dadosProcuracao = dadosProcuracao;
	}
	public String getNomeContato() {
		return nomeContato;
	}
	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}
	public String getTelFixoContato() {
		return telFixoContato;
	}
	public void setTelFixoContato(String telFixoContato) {
		this.telFixoContato = telFixoContato;
	}
	public String getTelCelularContato() {
		return telCelularContato;
	}
	public void setTelCelularContato(String telCelularContato) {
		this.telCelularContato = telCelularContato;
	}
	public String getDadosQuartoHospedagemMae() {
		return dadosQuartoHospedagemMae;
	}
	public void setDadosQuartoHospedagemMae(String dadosQuartoHospedagemMae) {
		this.dadosQuartoHospedagemMae = dadosQuartoHospedagemMae;
	}
	public Date getDataNascimentoDeclarante() {
		return dataNascimentoDeclarante;
	}
	public void setDataNascimentoDeclarante(Date dataNascimentoDeclarante) {
		this.dataNascimentoDeclarante = dataNascimentoDeclarante;
	}
	public Date getDataAlteracaoSolicitacao() {
		return dataAlteracaoSolicitacao;
	}
	public void setDataAlteracaoSolicitacao(Date dataAlteracaoSolicitacao) {
		this.dataAlteracaoSolicitacao = dataAlteracaoSolicitacao;
	}
	public String getHoraNascimentoCrianca1() {
		return horaNascimentoCrianca1;
	}
	public void setHoraNascimentoCrianca1(String horaNascimentoCrianca1) {
		this.horaNascimentoCrianca1 = horaNascimentoCrianca1;
	}
	public String getHoraNascimentoCrianca2() {
		return horaNascimentoCrianca2;
	}
	public String getHoraNascimentoCrianca3() {
		return horaNascimentoCrianca3;
	}
	public String getHoraNascimentoCrianca4() {
		return horaNascimentoCrianca4;
	}
	public String getHoraNascimentoCrianca5() {
		return horaNascimentoCrianca5;
	}
	public void setHoraNascimentoCrianca2(String horaNascimentoCrianca2) {
		this.horaNascimentoCrianca2 = horaNascimentoCrianca2;
	}
	public void setHoraNascimentoCrianca3(String horaNascimentoCrianca3) {
		this.horaNascimentoCrianca3 = horaNascimentoCrianca3;
	}
	public void setHoraNascimentoCrianca4(String horaNascimentoCrianca4) {
		this.horaNascimentoCrianca4 = horaNascimentoCrianca4;
	}
	public void setHoraNascimentoCrianca5(String horaNascimentoCrianca5) {
		this.horaNascimentoCrianca5 = horaNascimentoCrianca5;
	}
	public String getTipoDeclarantePreRegistro() {
		return tipoDeclarantePreRegistro;
	}
	public void setTipoDeclarantePreRegistro(String tipoDeclarantePreRegistro) {
		this.tipoDeclarantePreRegistro = tipoDeclarantePreRegistro;
	}
	public String getNomeDeclarantePreRegistro() {
		return nomeDeclarantePreRegistro;
	}
	public void setNomeDeclarantePreRegistro(String nomeDeclarantePreRegistro) {
		this.nomeDeclarantePreRegistro = nomeDeclarantePreRegistro;
	}
	public String getCpfDeclarantePreRegistro() {
		return cpfDeclarantePreRegistro;
	}
	public void setCpfDeclarantePreRegistro(String cpfDeclarantePreRegistro) {
		this.cpfDeclarantePreRegistro = cpfDeclarantePreRegistro;
	}
	public String getContatoDeclarantePreRegistro() {
		return contatoDeclarantePreRegistro;
	}
	public void setContatoDeclarantePreRegistro(String contatoDeclarantePreRegistro) {
		this.contatoDeclarantePreRegistro = contatoDeclarantePreRegistro;
	}
	public String getContatoMae() {
		return contatoMae;
	}
	public void setContatoMae(String contatoMae) {
		this.contatoMae = contatoMae;
	}
	public String getContatoPai() {
		return contatoPai;
	}
	public void setContatoPai(String contatoPai) {
		this.contatoPai = contatoPai;
	}
	public String getSituacaoSolicitacao() {
		return situacaoSolicitacao;
	}
	public void setSituacaoSolicitacao(String situacaoSolicitacao) {
		this.situacaoSolicitacao = situacaoSolicitacao;
	}
	public PreRegistroXML getXml() {
		return xml;
	}
	public void setXml(PreRegistroXML xml) {
		this.xml = xml;
	}
	public Date getDataNascimentoCrianca2() {
		return dataNascimentoCrianca2;
	}
	public void setDataNascimentoCrianca2(Date dataNascimentoCrianca2) {
		this.dataNascimentoCrianca2 = dataNascimentoCrianca2;
	}
	public String getDnvCrianca2() {
		return dnvCrianca2;
	}
	public void setDnvCrianca2(String dnvCrianca2) {
		this.dnvCrianca2 = dnvCrianca2;
	}
	public Date getDataNascimentoCrianca3() {
		return dataNascimentoCrianca3;
	}
	public void setDataNascimentoCrianca3(Date dataNascimentoCrianca3) {
		this.dataNascimentoCrianca3 = dataNascimentoCrianca3;
	}
	public String getDnvCrianca3() {
		return dnvCrianca3;
	}
	public void setDnvCrianca3(String dnvCrianca3) {
		this.dnvCrianca3 = dnvCrianca3;
	}
	public Date getDataNascimentoCrianca4() {
		return dataNascimentoCrianca4;
	}
	public void setDataNascimentoCrianca4(Date dataNascimentoCrianca4) {
		this.dataNascimentoCrianca4 = dataNascimentoCrianca4;
	}
	
	public String getDnvCrianca4() {
		return dnvCrianca4;
	}
	public void setDnvCrianca4(String dnvCrianca4) {
		this.dnvCrianca4 = dnvCrianca4;
	}
	public Date getDataNascimentoCrianca5() {
		return dataNascimentoCrianca5;
	}
	public void setDataNascimentoCrianca5(Date dataNascimentoCrianca5) {
		this.dataNascimentoCrianca5 = dataNascimentoCrianca5;
	}
	public String getDnvCrianca5() {
		return dnvCrianca5;
	}
	public void setDnvCrianca5(String dnvCrianca5) {
		this.dnvCrianca5 = dnvCrianca5;
	}
	public void setIdadeMae(Integer idadeMae) {
		this.idadeMae = idadeMae;
	}
	public void setIdadePai(Integer idadePai) {
		this.idadePai = idadePai;
	}
	public int getIdadeMae() {
		return idadeMae;
	}
	public void setIdadeMae(int idadeMae) {
		this.idadeMae = idadeMae;
	}
	public int getIdadePai() {
		return idadePai;
	}
	public void setIdadePai(int idadePai) {
		this.idadePai = idadePai;
	}
	
	
	//MÉTODOS
	public String dataInicioSolicitacaoFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataInicioSolicitacao());
	}
	public String dataNascimentoMaeFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoMae());
	}
	public String dataNascimentoPaiFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoPai());
	}
	public String dataNascimentoCrianca1Formatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoCrianca1());
	}
	public String dataNascimentoCrianca2Formatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoCrianca2());
	}
	public String dataNascimentoCrianca3Formatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoCrianca3());
	}
	public String dataNascimentoCrianca4Formatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoCrianca4());
	}
	public String dataNascimentoCrianca5Formatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataNascimentoCrianca5());
	}
	
	public boolean isPreRegistroFilhoUnico() {
		if (quantidadeCriancas == null) {
			return false;
		}
		else if (quantidadeCriancas.equals("Não")) {
			return true;
		}
		else return false;
	}
	
	public boolean isPreRegistroGemeos() {
		if (quantidadeCriancas == null) {
			return false;
		}
		else if (quantidadeCriancas.equals("Gêmeos")) {
			return true;
		}
		else return false;
	}
	
	public boolean isPreRegistroTrigemeos() {
		if (quantidadeCriancas == null) {
			return false;
		}
		else if (quantidadeCriancas.equals("Trigêmeos")) {
			return true;
		}
		else return false;
	}
	
	public boolean isPreRegistroQuadrigemeos() {
		if (quantidadeCriancas == null) {
			return false;
		}
		else if (quantidadeCriancas.equals("Quadrigêmeos")) {
			return true;
		}
		else return false;
	}
	
	public boolean isPreRegistroQuintuplos() {
		if (quantidadeCriancas == null) {
			return false;
		}
		else if (quantidadeCriancas.equals("Quíntuplos")) {
			return true;
		}
		else return false;
	}

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof PreRegistro) {
			return ((PreRegistro) obj).getId().equals(this.id);
		}
		return false;
	}
	public String getPaisResidenciaServentiaEscolhida() {
		return paisResidenciaServentiaEscolhida;
	}
	public void setPaisResidenciaServentiaEscolhida(String paisResidenciaServentiaEscolhida) {
		this.paisResidenciaServentiaEscolhida = paisResidenciaServentiaEscolhida;
	}
	public List<HistoricoPreRegistro> getListaHistoricoPreRegistro() {
		return listaHistoricoPreRegistro;
	}
	public void setListaHistoricoPreRegistro(List<HistoricoPreRegistro> listaHistoricoPreRegistro) {
		this.listaHistoricoPreRegistro = listaHistoricoPreRegistro;
	}
}
