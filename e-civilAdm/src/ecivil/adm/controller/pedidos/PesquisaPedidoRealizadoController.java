package ecivil.adm.controller.pedidos;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.ContaAlternativaBO;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.bo.SituacaoPedidoCertidaoBO;
import ecivil.ejb.entidade.AnexoCertidao;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.SituacaoPedidoCertidao;
import ecivil.ejb.entidade.SolicitacaoEstorno;
import ecivil.ejb.entidade.UsuarioServentia;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.relatorio.RelatorioReciboCertidao;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.DadosBancariosVO;
import ecivil.ejb.vo.FiltroPesquisaPedidoCertidaoVO;
import ecivil.ejb.ws.cliente.rest.ContaDigitalWS;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.JSFUtil;
import web.util.Paginacao;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class PesquisaPedidoRealizadoController extends BaseController implements Serializable {

	private final static Logger LOGGER = Logger.getLogger(PesquisaPedidoRealizadoController.class.getName());
	
	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	@EJB
	private SituacaoPedidoCertidaoBO situacaoPedidoCertidaoBO;
	
	@EJB
	private ContaAlternativaBO contaAlternativaBO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private ContaDigitalWS contaDigitalWS;
	
	@EJB
	private RelatorioReciboCertidao relatorioReciboCertidao;

	private List<PedidoCertidao> listaPedidoCertidao;
	private List<CartorioResponse> listaCartorio; 
	private PedidoCertidao pedidoCertidao;
	private CartorioResponse cartorioRequisitante;
	private List<SituacaoPedidoCertidao> listaSituacaoPedidoCertidao;
	private FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao;
	private Paginacao paginacao;
	private boolean pesquisaRealizada;

	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		setPaginacao(new Paginacao());
		setFiltroPesquisaCertidao(new FiltroPesquisaPedidoCertidaoVO());
		inicializaListaCartorio();
		inicializaFiltroPesquisaCertidao();
		inicializaSituacaoPedidoCertidao();
		setaCorregedoriaUsuarioLogado();
		pesquisarPedidosCertidao();
	}
	
	private void inicializaListaCartorio() {
		try {
			if (getUsuarioLogadoPortal().isAdmin()) {
				setListaCartorio(cartorioLookUp.recuperaListaCartorio());
				return;
			}
			List<String> listaCodCorregedoria = getUsuarioLogadoPortal().getUsuarioServentiaAtiva().stream().map(UsuarioServentia::getCodigoCorregedoria).collect(Collectors.toList());
			setListaCartorio(cartorioLookUp.recuperaListaCartorioPorCodigoCorregedoria(listaCodCorregedoria));
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível recuperar a(s) serventias do usuário nesse momento. Tente novamente mais tarde."); 
			e.printStackTrace();
		}
	}
	
	private void inicializaFiltroPesquisaCertidao() {
		FiltroPesquisaPedidoCertidaoVO filtroPesquisaPedidoCertidaoVO = (FiltroPesquisaPedidoCertidaoVO) JSFUtil.get("filtroPesquisaCertidao");
		if (filtroPesquisaPedidoCertidaoVO != null) {
			setFiltroPesquisaCertidao(filtroPesquisaPedidoCertidaoVO);
			pesquisarPedidosCertidao();
		}
	}
	
	private void inicializaSituacaoPedidoCertidao() {
		setListaSituacaoPedidoCertidao(situacaoPedidoCertidaoBO.recuperaListaSituacaoPedidoCertidao());
	}

	private void setaCorregedoriaUsuarioLogado() {
		if (getUsuarioLogadoPortal().isAdmin()) {
			return;
		}
		
		if (getUsuarioLogadoPortal().getUsuarioServentiaAtiva().isEmpty()) {
			getFiltroPesquisaCertidao().getListaCorregedoriaRequisitanteInicializada().add("");
		}
		
		getFiltroPesquisaCertidao().getListaCorregedoriaRequisitanteInicializada().addAll(getUsuarioLogadoPortal().recuperaListaCodigoCorregedoriaUsuario());
		
		if (getFiltroPesquisaCertidao().getListaCorregedoriaRequisitanteInicializada().size() == 1) {
			getFiltroPesquisaCertidao().setCorregedoriaRequisitante(getFiltroPesquisaCertidao().getListaCorregedoriaRequisitante().get(0));
		}
	}

	public void pesquisarPedidosCertidao() {
		try {
			setListaPedidoCertidao(new ArrayList<>());
			paginacao.limpaPaginacao();
			paginacao.setTotalRegistros(pedidoCertidaoBO.pesquisarTotalPedidosRealizadoPeloCartorio(getUsuarioLogadoPortal(), getFiltroPesquisaCertidao()));
			pesquisarPedidosCertidaoPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar os pedidos de certidão nesse momento.");
		}
	}

	public void pesquisarPedidosCertidaoPaginado() {
		if (paginacao.getTotalRegistros() > 0) {
			setListaPedidoCertidao(pedidoCertidaoBO.pesquisarPedidosRealizadoPeloCartorio(getUsuarioLogadoPortal(), getFiltroPesquisaCertidao(), getPaginacao()));
		} else {
			setListaPedidoCertidao(new ArrayList<>());
		}
		setPesquisaRealizada(Boolean.TRUE);
	}
	
	public void setaPedidoCertidao(PedidoCertidao pedidoCertidao, String idModal) {
		try {
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo()));
			atualizarDataHoraVisualizacaoRequisitante();
			getPedidoCertidao().setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getPedidoCertidao().getCodCorregedoriaRequisitada()));
			abrirModal(idModal);
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados do pedido de certidão nesse momento. Tente novamente mais tarde.");
		}
	}
	
	private void atualizarDataHoraVisualizacaoRequisitante () {
		try {
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(getPedidoCertidao().getProtocolo()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Erro ao atualizar a data e hora da última visualização.");
		}
	}
	
	public void setaDadosCartorioRequisitante() {
		try {
			setCartorioRequisitante(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getPedidoCertidao().getCodCorregedoriaRequisitante()));
			if (getCartorioRequisitante() == null) {
				throw new ECivilException("Não foi possível recuperar os dados bancários do cartório requisitante nesse momento. Tente novamente mais tarde.");
			}
			recuperaSaldoDisponivelCartorioRequisitante(getCartorioRequisitante());
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados do pedido de certidão nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void solicitarEstorno() {
		try {
			if (!Util.ehStringValida(getPedidoCertidao().getSolicitacaoEstorno().getDescricaoSolicitacao())) {
				Mensagem.errorSemBundle("Favor informar o motivo da solicitação de estorno.");
				return;
			}
			pedidoCertidaoBO.realizarSolicitacaoEstorno(getPedidoCertidao(), getUsuarioLogadoPortal());
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(getPedidoCertidao().getProtocolo()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			pesquisarPedidosCertidao();
			Mensagem.infoSemBundle("Estorno solicitado com sucesso.");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível solicitar o estorno nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void inicializarSolicitarEstorno() {
		getPedidoCertidao().setSolicitacaoEstorno(new SolicitacaoEstorno());
	}
	
	public void inicializaRejeitarArquivoEnviado() {
		getPedidoCertidao().setMotivoRejeicaoArquivo(null);
	}
	
	public void rejeitarArquivoEnviado() {
		try {
			if (!Util.ehStringValida(getPedidoCertidao().getMotivoRejeicaoArquivo())) {
				Mensagem.errorSemBundle("Favor informar o motivo de rejeição do arquivo.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.rejeitarArquivoEnviado(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			pesquisarPedidosCertidao();
			Mensagem.infoSemBundle("Arquivo rejeitado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível rejeitar o arquivo enviado pelo cartório nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void finalizarPedidoCertidao() {
		try {
			setPedidoCertidao(pedidoCertidaoBO.finalizarPedido(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			pesquisarPedidosCertidao();
			Mensagem.infoSemBundle("Pedido finalizado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível finalizar o pedido de certidão nesse momento. Tente novamente mais tarde.");
		}
	}
	
	private void recuperaSaldoDisponivelCartorioRequisitante(CartorioResponse cartorioResponse) throws ECivilException {
		cartorioResponse.setDadosBancariosVO(new DadosBancariosVO());
		cartorioResponse.getDadosBancariosVO().setSaldoDisponivel(contaDigitalWS.consultaSaldo(getPedidoCertidao().getCodCorregedoriaRequisitante()));
	}

	public void realizarPagamento() {
		try {
			LOGGER.info("#### Inicio realizar transferencia. Protocolo: " + getPedidoCertidao().getProtocolo());
			pedidoCertidaoBO.realizarPagamentoCartorio(getPedidoCertidao(), getUsuarioLogadoPortal());
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(getPedidoCertidao().getProtocolo()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			pesquisarPedidosCertidao();
			Mensagem.infoSemBundle("Pagamento da certidão realizado com sucesso.");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível realizar o pagamento da certidão nesse momento.");
		}
	}
	
	public String recuperaNomeDistritoECartorio(String codCorregedoria) {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codCorregedoria);
			return cartorio != null ? cartorio.getNomeDistritoECartorio() : "";
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível recuperar a(s) serventias do usuário nesse momento. Tente novamente mais tarde."); 
			e.printStackTrace();
		}
		return "";
	}
	
	public void downloadReciboCertidao() {
		try {
			super.exibirPDF(relatorioReciboCertidao.geraRelatorioReciboCertidao(getPedidoCertidao()), montaNomeArquivo("Recibo", getPedidoCertidao().getProtocolo()));
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível fazer download da certidão nesse momento.");
		}
	}
	
	public void downloadCertidao() {
		try {
			if (getPedidoCertidao().getAnexoCertidao() != null) {
				if (getPedidoCertidao().getAnexoCertidao().getTipoExtensao().equals(AnexoCertidao.TIPO_EXTENSAO_XML)) {
					super.downloadXML(getPedidoCertidao().getAnexoCertidao().getCertidao(), "XML_Certidao" + getPedidoCertidao().getProtocolo() + ".XML.DSIG");
				} else {
					super.exibirPDF(getPedidoCertidao().getAnexoCertidao().getCertidao(), montaNomeArquivo("Certidao", getPedidoCertidao().getProtocolo()));
				}
			} else {
				super.downloadXML(recuperaXMLCertidaoAntigo(), "CRCCER" + getPedidoCertidao().getProtocolo() + ".XML.DSIG");
			}
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível fazer download da certidão nesse momento.");
		}
	}
	
	private byte[] recuperaXMLCertidaoAntigo() throws IOException, ECivilException {
		return Files.readAllBytes(Paths.get("/arquivos_certidao/backup/certidao/CRCCER" + getPedidoCertidao().getProtocolo() + ".XML.DSIG"));
	}
	
	public String recuperaAliquotaISSQNEMunicipio() {
		try {
			if (getPedidoCertidao() == null || getPedidoCertidao().recuperaValoresCertidaoRequisitado().getAliquota() == null) {
				return null;
			}

			return getPedidoCertidao().recuperaValoresCertidaoRequisitado().getAliquota().toString().concat("% ")
					.concat("(").concat(cartorioLookUp.recuperaNomeMunicipioCartorio(getPedidoCertidao().getCodCorregedoriaRequisitada())).concat(")");
			
		} catch (ECivilException e) {
			e.printStackTrace();
			return null;
		}
	}

	public BigDecimal recuperaValorCertidao() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		
		if (getPedidoCertidao().isPedidoDeCartorioParaCartorio()) {
			return getPedidoCertidao().recuperaValoresCertidaoRequisitado().getValorCertidao();
		}
		
		return getPedidoCertidao().getValorCertidao();
	}
	
	public BigDecimal recuperaValorAverbacaoCertidao() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		return getPedidoCertidao().recuperaValoresCertidaoRequisitado().getValorAverbacao();
	}
	
	public BigDecimal recuperaValorTotalCertidao() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		return getPedidoCertidao().recuperaValoresCertidaoRequisitado().getValorTotal();
	}
	
	public String recuperaAliquotaISSQNEMunicipioRequisitante() {
		try {
			if (getPedidoCertidao() == null || getPedidoCertidao().recuperaValoresCertidaoRequisitante().getAliquota() == null) {
				return null;
			}

			return getPedidoCertidao().recuperaValoresCertidaoRequisitante().getAliquota().toString().concat("% ")
					.concat("(").concat(cartorioLookUp.recuperaNomeMunicipioCartorio(getPedidoCertidao().getCodCorregedoriaRequisitante())).concat(")");
			
		} catch (ECivilException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public BigDecimal recuperaValorCertidaoRequisitante() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		return getPedidoCertidao().recuperaValoresCertidaoRequisitante().getValorCertidao();
	}
	
	public BigDecimal recuperaValorAverbacaoRequisitante() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		return getPedidoCertidao().recuperaValoresCertidaoRequisitante().getValorAverbacao();
	}
	
	public BigDecimal recuperaValorTotalRequisitante() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		return getPedidoCertidao().recuperaValoresCertidaoRequisitante().getValorTotal();
	}

	public boolean exibeReciboCertidao() {
		return getPedidoCertidao() != null && (getPedidoCertidao().isSituacaoColetaNoCartorio() || getPedidoCertidao().isSituacaoPedidoFinalizado()
				|| getPedidoCertidao().isSituacaoPagamentoAprovadoAguardandoLiberacao()
				|| getPedidoCertidao().isSituacaoAguardandoValidacaoArquivo() || getPedidoCertidao().isSituacaoArquivoNaoAceito());
	}
	
	public boolean exibeBtnSolicitarEstorno() {
		return getPedidoCertidao() != null && (getPedidoCertidao().isSituacaoPagamentoAprovadoAguardandoLiberacao() || getPedidoCertidao().isSituacaoAguardandoValidacaoArquivo());
	}
	
	public boolean exibeDownloadCertidao() {
		return getPedidoCertidao() != null && (getPedidoCertidao().isSituacaoPedidoFinalizado() || getPedidoCertidao().isSituacaoAguardandoValidacaoArquivo());
	}
	
	public boolean desabilitaComboSelecionarServentia() {
		return !getUsuarioLogadoPortal().isAdmin() && getUsuarioLogadoPortal().possuiApenasUmaServentia();
	}
	
	public boolean exibeHistoricoPedidoCertidao() {
		return getPedidoCertidao() != null && getPedidoCertidao().getListaHistoricoPedidoCertidaoInicializada().size() > 0;
	}
	
	public boolean exibeMotivoSolicitacaoEstorno() {
		return getPedidoCertidao() != null && getPedidoCertidao().getSolicitacaoEstorno() != null;
	}
	
	public void limpar() {
		setFiltroPesquisaCertidao(new FiltroPesquisaPedidoCertidaoVO());
		paginacao.limpaPaginacao();
		setPesquisaRealizada(Boolean.FALSE);
		setListaPedidoCertidao(new ArrayList<>());
		setaCorregedoriaUsuarioLogado();
	}
	
	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public List<PedidoCertidao> getListaPedidoCertidao() {
		return listaPedidoCertidao;
	}

	public void setListaPedidoCertidao(List<PedidoCertidao> listaPedidoCertidao) {
		this.listaPedidoCertidao = listaPedidoCertidao;
	}
	
	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}

	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}
	
	public CartorioResponse getCartorioRequisitante() {
		return cartorioRequisitante;
	}

	public void setCartorioRequisitante(CartorioResponse cartorioRequisitante) {
		this.cartorioRequisitante = cartorioRequisitante;
	}

	public List<SituacaoPedidoCertidao> getListaSituacaoPedidoCertidao() {
		return listaSituacaoPedidoCertidao;
	}

	public void setListaSituacaoPedidoCertidao(List<SituacaoPedidoCertidao> listaSituacaoPedidoCertidao) {
		this.listaSituacaoPedidoCertidao = listaSituacaoPedidoCertidao;
	}

	public FiltroPesquisaPedidoCertidaoVO getFiltroPesquisaCertidao() {
		return filtroPesquisaCertidao;
	}

	public void setFiltroPesquisaCertidao(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		this.filtroPesquisaCertidao = filtroPesquisaCertidao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public boolean isPesquisaRealizada() {
		return pesquisaRealizada;
	}

	public void setPesquisaRealizada(boolean pesquisaRealizada) {
		this.pesquisaRealizada = pesquisaRealizada;
	}

}
