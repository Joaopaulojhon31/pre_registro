package ecivil.ejb.entidade;

import java.math.BigDecimal;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import ecivil.ejb.enumeration.EnumMotivoRejeicao;
import ecivil.ejb.enumeration.EnumTipoSaidaCertidao;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.response.integradorcrc.BuscarCertidaoResponse;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@SuppressWarnings("serial")
@Entity
@SequenceGenerator(name = "seq_id_pedido_certidao", sequenceName = "ecivil.seq_id_pedido_certidao", allocationSize = 1)
@Table(schema = "ecivil", name = "pedido_certidao")
public class PedidoCertidao implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_pedido_certidao")
	@Column(name = "id_pedido_certidao")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_situacao_pedido_certidao")
	private SituacaoPedidoCertidao situacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_certidao")
	private TipoCertidao tipoCertidao;

	@ManyToOne
	@JoinColumn(name = "id_municipio")
	private Municipio municipio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_interno_requisitante")
	private UsuarioPortalInterno usuarioInternoRequisitante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_externo")
	private UsuarioPortalExterno usuarioExterno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco_usuario_externo")
	private EnderecoUsuarioExterno enderecoUsuarioExterno;

	@Column(name = "codigo_corregedoria_requisitante")
	private String codCorregedoriaRequisitante;

	@Column(name = "codigo_corregedoria_requisitada")
	private String codCorregedoriaRequisitada;

	@Column(name = "protocolo")
	private String protocolo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_pedido")
	private Date dataPedido;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_alteracao")
	private Date dataAlteracao;

	@Column(name = "tipo_saida")
	private String tipoSaida;

	@Column(name = "nome_primeira_pessoa")
	private String nomePrimeiraPessoa;

	@Column(name = "nome_segunda_pessoa")
	private String nomeSegundaPessoa;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fato")
	private Date dataFato;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_registro")
	private Date dataRegistro;

	@Column(name = "observacao")
	private String observacao;

	@Column(name = "pedido_via_formulario")
	private boolean pedidoViaFormulario;

	@Column(name = "possui_averbacao")
	private boolean possuiAverbacao;

	@Column(name = "valor_certidao")
	private BigDecimal valorCertidao;

	@Column(name = "valor_frete")
	private BigDecimal valorFrete;

	@Column(name = "taxa_manuntencao")
	private BigDecimal taxaManuntencao;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "motivo_rejeicao")
	private String motivoRejeicao;
	
	@Column(name = "motivo_rejeicao_arquivo")
	private String motivoRejeicaoArquivo;

	@Column(name = "codigo_rastreio")
	private String codigoRastreio;
	
	@Column(name = "id_fato_crc")
	private Long idFatoCrc;

	@OneToOne(mappedBy = "pedidoCertidao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private CertidaoNegativa certidaoNegativa;
	
	@OneToOne(mappedBy = "pedidoCertidao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private AnexoCertidao anexoCertidao;
	
	@OneToOne(mappedBy = "pedidoCertidao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private SolicitacaoEstorno solicitacaoEstorno;
	
	@OneToMany(mappedBy = "pedidoCertidao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VinculoBoleto> listaVinculoBoleto;

	@OrderBy("dataInicio ASC")
	@OneToMany(mappedBy = "pedidoCertidao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HistoricoPedidoCertidao> listaHistoricoPedidoCertidao;
	
	@OneToMany(mappedBy = "pedidoCertidao", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ValoresPedidoCertidao> listaValoresPedidoCertidao;
	
	@Version
	@Column(name = "versao")
	private Long versao;

	@Transient
	private BuscarCertidaoResponse resultadoBuscaCertidao;

	@Transient
	private CartorioResponse cartorio;
	
	@Transient
	private String nomePessoaPesquisa;
	
	@Transient
	private boolean nomeIdentico;
	
	@Transient
	private String cpf;
	
	@Transient
	private Municipio municipioPesquisa;
	
	@Transient
	private String nomeAssociado;
	
	@Transient
	private BigDecimal aliquotaISSQNRequisitante;
	
	@Transient
	private BigDecimal aliquotaISSQNRequisitado;

	@Transient
	private String ano;

	@Transient
	private EnumMotivoRejeicao enumMotivoRejeicao;

	@Transient
	private byte[] anexo;
	
	@Transient
	private String nomeDestinatario;
	
	@Transient
	private String emailDestinatario;

	public String recuperaDescricaoTipoSaidaCertidao() {
		return EnumTipoSaidaCertidao.recuperaDescricaoTipoSaidaPorCodigo(getTipoSaida());
	}

	public String getDescricaoTipoSaida() {
		EnumTipoSaidaCertidao enumTipoSaidaCertidao = EnumTipoSaidaCertidao.recuperaEnumTipoSaidaPorCodigo(getTipoSaida());
		return enumTipoSaidaCertidao != null ? enumTipoSaidaCertidao.getDescricao() : "";
	}

	public String descricaoPossuiAverbacao() {
		return possuiAverbacao ? "Sim" : "Não";
	}

	public String dataPedidoFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataPedido());
	}
	
	public String dataUltimaAlteracaoFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataAlteracao());
	}
	
	public VinculoBoleto recuperaVinculoBoletoAtivo() {
		if (getListaVinculoBoleto() == null) {
			return null;
		}
		return getListaVinculoBoleto().stream().filter(VinculoBoleto::isAtivo).findFirst().orElse(null);
	}
	
	public HistoricoPedidoCertidao recuperaHistoricoPedidoCertidaoEmAberto() {
		return getListaHistoricoPedidoCertidaoInicializada().stream().filter(HistoricoPedidoCertidao::isEmAberto).findFirst().orElse(null);
	}
	
	public ValoresPedidoCertidao recuperaValoresCertidaoRequisitado() {
		if (getListaValoresPedidoCertidaoInicializada().isEmpty() || !Util.ehStringValida(getCodCorregedoriaRequisitada())) {
			return null;
		}
		return getListaValoresPedidoCertidaoInicializada().stream().filter(valor -> valor.getCodCorregedoria().equals(getCodCorregedoriaRequisitada())).findFirst().orElse(null);
	}
	
	public ValoresPedidoCertidao recuperaValoresCertidaoRequisitante() {
		if (getListaValoresPedidoCertidaoInicializada().isEmpty() || !Util.ehStringValida(getCodCorregedoriaRequisitante())) {
			return null;
		}
		return getListaValoresPedidoCertidaoInicializada().stream().filter(valor -> valor.getCodCorregedoria().equals(getCodCorregedoriaRequisitante())).findFirst().orElse(null);
	}
	
	public String getCodCorregedoriaRequisitanteFormatado() {
		return Util.formataCodigCartorio(getCodCorregedoriaRequisitante());
	}
	
	public boolean isPedidoCertidaoNegativa() {
		return getCertidaoNegativa() != null;
	}

	public boolean isSituacaoEmAnalise() {
		return getSituacao() != null && (SituacaoPedidoCertidao.COD_EM_ANALISE.equals(getSituacao().getCodigo()));
	}

	public boolean isSituacaoAguardandoPagamento() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_AGUARDANDO_PAGAMENTO.equals(getSituacao().getCodigo());
	}

	public boolean isSituacaoProntoParaEnvio() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_PRONTO_PARA_ENVIO.equals(getSituacao().getCodigo());
	}
	
	public boolean isSituacaoPagamentoAprovadoAguardandoLiberacao() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_PAGAMENTO_APROVADO_AGUARDANDO_LIBERACAO.equals(getSituacao().getCodigo());
	}

	public boolean isSituacaoPedidoFinalizado() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_FINALIZADO.equals(getSituacao().getCodigo());
	}

	public boolean isSituacaoPedidoRejeitado() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_REJEITADO.equals(getSituacao().getCodigo());
	}

	public boolean isSituacaoPedidoArquivado() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_ARQUIVADO.equals(getSituacao().getCodigo());
	}

	public boolean isSituacaoColetaNoCartorio() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_COLETA_NO_CARTORIO.equals(getSituacao().getCodigo());
	}
	
	public boolean isSituacaoAguardandoValidacaoArquivo() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_AGUARDANDO_VALIDACAO_ARQUIVO.equals(getSituacao().getCodigo());
	}
	
	public boolean isSituacaoArquivoNaoAceito() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_ARQUIVO_NAO_ACEITO.equals(getSituacao().getCodigo());
	}
	
	public boolean isSituacaoSolicitacaoEstorno() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_SOLICITACAO_ESTORNO.equals(getSituacao().getCodigo());
	}
	
	public boolean isSituacaoEstornoRealizado() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_ESTORNO_REALIZADO.equals(getSituacao().getCodigo());
	}
	
	public boolean isSituacaoEstornoRejeitado() {
		return getSituacao() != null && SituacaoPedidoCertidao.COD_ESTORNO_REJEITADO.equals(getSituacao().getCodigo());
	}

	public boolean isCertidaoCasamento() {
		return getTipoCertidao() != null && getTipoCertidao().isCertidaoCasamento();
	}

	public boolean isCertidaoNascimentoOuObto() {
		return getTipoCertidao() != null && (getTipoCertidao().isCertidaoNascimento() || getTipoCertidao().isCertidaoObto());
	}

	public boolean isSaidaDigital() {
		return EnumTipoSaidaCertidao.DIGITAL.getCodigo().equals(getTipoSaida());
	}

	public boolean isSaidaViaCorreios() {
		return EnumTipoSaidaCertidao.CORREIO.getCodigo().equals(getTipoSaida());
	}

	public boolean isSaidaBuscaPresencial() {
		return EnumTipoSaidaCertidao.PRESENCIAL.getCodigo().equals(getTipoSaida());
	}

	public boolean isMotivoRejeicaoInformarMotivo() {
		return getEnumMotivoRejeicao() != null && getEnumMotivoRejeicao().equals(EnumMotivoRejeicao.INFORMAR_MOTIVO);
	}

	public boolean isPedidoDeCartorioParaCartorio() {
		return Util.ehStringValida(getCodCorregedoriaRequisitante());
	}
	
	public boolean possuiRegistrosBuscaCertidao() {
		return getResultadoBuscaCertidao() != null && getResultadoBuscaCertidao().possuiResultadoBusca();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SituacaoPedidoCertidao getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPedidoCertidao situacao) {
		this.situacao = situacao;
	}

	public TipoCertidao getTipoCertidao() {
		return tipoCertidao;
	}

	public void setTipoCertidao(TipoCertidao tipoCertidao) {
		this.tipoCertidao = tipoCertidao;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public UsuarioPortalInterno getUsuarioInternoRequisitante() {
		return usuarioInternoRequisitante;
	}

	public void setUsuarioInternoRequisitante(UsuarioPortalInterno usuarioInternoRequisitante) {
		this.usuarioInternoRequisitante = usuarioInternoRequisitante;
	}

	public UsuarioPortalExterno getUsuarioExterno() {
		return usuarioExterno;
	}

	public void setUsuarioExterno(UsuarioPortalExterno usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	public EnderecoUsuarioExterno getEnderecoUsuarioExterno() {
		return enderecoUsuarioExterno;
	}

	public void setEnderecoUsuarioExterno(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		this.enderecoUsuarioExterno = enderecoUsuarioExterno;
	}

	public String getCodCorregedoriaRequisitante() {
		return codCorregedoriaRequisitante;
	}

	public void setCodCorregedoriaRequisitante(String codCorregedoriaRequisitante) {
		this.codCorregedoriaRequisitante = codCorregedoriaRequisitante;
	}

	public String getCodCorregedoriaRequisitada() {
		return codCorregedoriaRequisitada;
	}

	public void setCodCorregedoriaRequisitada(String codCorregedoriaRequisitada) {
		this.codCorregedoriaRequisitada = codCorregedoriaRequisitada;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getTipoSaida() {
		return tipoSaida;
	}

	public void setTipoSaida(String tipoSaida) {
		this.tipoSaida = tipoSaida;
	}

	public String getNomePrimeiraPessoa() {
		return nomePrimeiraPessoa;
	}

	public void setNomePrimeiraPessoa(String nomePrimeiraPessoa) {
		this.nomePrimeiraPessoa = nomePrimeiraPessoa;
	}

	public String getNomeSegundaPessoa() {
		return nomeSegundaPessoa;
	}

	public void setNomeSegundaPessoa(String nomeSegundaPessoa) {
		this.nomeSegundaPessoa = nomeSegundaPessoa;
	}

	public Date getDataFato() {
		return dataFato;
	}

	public void setDataFato(Date dataFato) {
		this.dataFato = dataFato;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isPedidoViaFormulario() {
		return pedidoViaFormulario;
	}

	public void setPedidoViaFormulario(boolean pedidoViaFormulario) {
		this.pedidoViaFormulario = pedidoViaFormulario;
	}

	public boolean isPossuiAverbacao() {
		return possuiAverbacao;
	}

	public void setPossuiAverbacao(boolean possuiAverbacao) {
		this.possuiAverbacao = possuiAverbacao;
	}

	public BigDecimal getValorCertidao() {
		return valorCertidao;
	}

	public void setValorCertidao(BigDecimal valorCertidao) {
		this.valorCertidao = valorCertidao;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getTaxaManuntencao() {
		return taxaManuntencao;
	}

	public void setTaxaManuntencao(BigDecimal taxaManuntencao) {
		this.taxaManuntencao = taxaManuntencao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getMotivoRejeicao() {
		return motivoRejeicao;
	}

	public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}
	
	public String getMotivoRejeicaoArquivo() {
		return motivoRejeicaoArquivo;
	}

	public void setMotivoRejeicaoArquivo(String motivoRejeicaoArquivo) {
		this.motivoRejeicaoArquivo = motivoRejeicaoArquivo;
	}

	public String getCodigoRastreio() {
		return codigoRastreio;
	}

	public void setCodigoRastreio(String codigoRastreio) {
		this.codigoRastreio = codigoRastreio;
	}
	
	public Long getIdFatoCrc() {
		return idFatoCrc;
	}

	public void setIdFatoCrc(Long idFatoCrc) {
		this.idFatoCrc = idFatoCrc;
	}

	public CertidaoNegativa getCertidaoNegativa() {
		return certidaoNegativa;
	}

	public void setCertidaoNegativa(CertidaoNegativa certidaoNegativa) {
		this.certidaoNegativa = certidaoNegativa;
	}
	
	public AnexoCertidao getAnexoCertidao() {
		return anexoCertidao;
	}

	public void setAnexoCertidao(AnexoCertidao anexoCertidao) {
		this.anexoCertidao = anexoCertidao;
	}
	
	public SolicitacaoEstorno getSolicitacaoEstorno() {
		return solicitacaoEstorno;
	}

	public void setSolicitacaoEstorno(SolicitacaoEstorno solicitacaoEstorno) {
		this.solicitacaoEstorno = solicitacaoEstorno;
	}

	public List<VinculoBoleto> getListaVinculoBoletoInicializada() {
		if (listaVinculoBoleto == null) {
			listaVinculoBoleto = new ArrayList<>();
		}
		return listaVinculoBoleto;
	}
	
	public List<VinculoBoleto> getListaVinculoBoleto() {
		return listaVinculoBoleto;
	}

	public void setListaVinculoBoleto(List<VinculoBoleto> listaVinculoBoleto) {
		this.listaVinculoBoleto = listaVinculoBoleto;
	}
	
	public List<HistoricoPedidoCertidao> getListaHistoricoPedidoCertidaoInicializada() {
		if (listaHistoricoPedidoCertidao == null) {
			listaHistoricoPedidoCertidao = new ArrayList<>();
		}
		return listaHistoricoPedidoCertidao;
	}
	
	public List<HistoricoPedidoCertidao> getListaHistoricoPedidoCertidao() {
		return listaHistoricoPedidoCertidao;
	}

	public void setListaHistoricoPedidoCertidao(List<HistoricoPedidoCertidao> listaHistoricoPedidoCertidao) {
		this.listaHistoricoPedidoCertidao = listaHistoricoPedidoCertidao;
	}
	
	public List<ValoresPedidoCertidao> getListaValoresPedidoCertidaoInicializada() {
		if (listaValoresPedidoCertidao == null) {
			listaValoresPedidoCertidao = new ArrayList<>();
		}
		return listaValoresPedidoCertidao;
	}

	public List<ValoresPedidoCertidao> getListaValoresPedidoCertidao() {
		return listaValoresPedidoCertidao;
	}

	public void setListaValoresPedidoCertidao(List<ValoresPedidoCertidao> listaValoresPedidoCertidao) {
		this.listaValoresPedidoCertidao = listaValoresPedidoCertidao;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public BuscarCertidaoResponse getResultadoBuscaCertidao() {
		return resultadoBuscaCertidao;
	}

	public void setResultadoBuscaCertidao(BuscarCertidaoResponse resultadoBuscaCertidao) {
		this.resultadoBuscaCertidao = resultadoBuscaCertidao;
	}

	public CartorioResponse getCartorio() {
		return cartorio;
	}

	public void setCartorio(CartorioResponse cartorio) {
		this.cartorio = cartorio;
	}
	
	public String getNomePessoaPesquisa() {
		return nomePessoaPesquisa;
	}

	public void setNomePessoaPesquisa(String nomePessoaPesquisa) {
		this.nomePessoaPesquisa = nomePessoaPesquisa;
	}
	
	public boolean isNomeIdentico() {
		return nomeIdentico;
	}

	public void setNomeIdentico(boolean nomeIdentico) {
		this.nomeIdentico = nomeIdentico;
	}

	public BigDecimal getAliquotaISSQNRequisitante() {
		return aliquotaISSQNRequisitante;
	}

	public void setAliquotaISSQNRequisitante(BigDecimal aliquotaISSQNRequisitante) {
		this.aliquotaISSQNRequisitante = aliquotaISSQNRequisitante;
	}

	public BigDecimal getAliquotaISSQNRequisitado() {
		return aliquotaISSQNRequisitado;
	}

	public void setAliquotaISSQNRequisitado(BigDecimal aliquotaISSQNRequisitado) {
		this.aliquotaISSQNRequisitado = aliquotaISSQNRequisitado;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public EnumMotivoRejeicao getEnumMotivoRejeicao() {
		return enumMotivoRejeicao;
	}

	public void setEnumMotivoRejeicao(EnumMotivoRejeicao enumMotivoRejeicao) {
		this.enumMotivoRejeicao = enumMotivoRejeicao;
	}

	public byte[] getAnexo() {
		return anexo;
	}

	public void setAnexo(byte[] anexo) {
		this.anexo = anexo;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}	

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public Municipio getMunicipioPesquisa() {
		return municipioPesquisa;
	}

	public void setMunicipioPesquisa(Municipio municipioPesquisa) {
		this.municipioPesquisa = municipioPesquisa;
	}

	public String getNomeAssociado() {
		return nomeAssociado;
	}

	public void setNomeAssociado(String nomeAssociado) {
		this.nomeAssociado = nomeAssociado;
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
		if (obj instanceof PedidoCertidao) {
			return ((PedidoCertidao) obj).getId().equals(this.id);
		}
		return false;
	}

}
