package ecivil.adm.controller.pedidos;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.adm.util.UploadUtils;
import ecivil.ejb.bo.AliquotaISSQNBO;
import ecivil.ejb.bo.CertidaoNegativaBO;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.bo.SituacaoPedidoCertidaoBO;
import ecivil.ejb.bo.UsuarioPortalExternoBO;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.enumeration.EnumMotivoRejeicao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.exception.UploadException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.FiltroPesquisaPedidoCertidaoVO;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class DadosPedidoCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	@EJB
	private SituacaoPedidoCertidaoBO situacaoPedidoCertidaoBO;
	
	@EJB
	private CertidaoNegativaBO certidaoNegativaBO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private AliquotaISSQNBO aliquotaISSQNBO;
	
	@EJB
	private UsuarioPortalExternoBO usuarioPortalExternoBO;

	private PedidoCertidao pedidoCertidao;
	private CartorioResponse cartorioRequisitante;
	private FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao;

	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		recuperaPedidoCertidao();
		validaPedidoCertidao();
		if (getPedidoCertidao() != null) {
			setaFiltroPedidoCertidao();
			inicializaCartorio();
			inicializaCartorioRequisitante();
			atualizarDataHoraVisualizacaoRequisitado();
		}
	}

	private void recuperaPedidoCertidao() {
		String protocolo = (String) JSFUtil.get("protocoloPedidoCertidao");
		if (Util.ehStringValida(protocolo)) {
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(protocolo));
		}
	}

	private void validaPedidoCertidao() {
		if (getPedidoCertidao() == null) {
			redireciona(EnumPaginas.PESQUISAR_PEDIDOS_CERTIDAO.getUrlJsf());
		}
	}
	
	private void setaFiltroPedidoCertidao() {
		setFiltroPesquisaCertidao((FiltroPesquisaPedidoCertidaoVO) JSFUtil.get("filtroPesquisaCertidao"));		
	}

	private void inicializaCartorio() {
		try {
			getPedidoCertidao().setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getPedidoCertidao().getCodCorregedoriaRequisitada()));
		} catch (ECivilException e) {
			e.printStackTrace();
		}
	}
	
	private void inicializaCartorioRequisitante() {
		try {
			if (Util.ehStringValida(getPedidoCertidao().getCodCorregedoriaRequisitante())) {
				setCartorioRequisitante(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getPedidoCertidao().getCodCorregedoriaRequisitante()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel recuperar os dados do requisitante nesse momento.");
		}
	}
	
	private void atualizarDataHoraVisualizacaoRequisitado () {
		try {
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(getPedidoCertidao().getProtocolo()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Erro ao atualizar a data e hora da �ltima visualiza��o.");
		}
	}
	
	public void confirmarPedido() {
		try {
			if (getPedidoCertidao().isSaidaViaCorreios() && getPedidoCertidao().getValorFrete() == null) {
				Mensagem.errorSemBundle("Favor informar o valor do frete para poder confirmar o pedido de certid�o.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.confirmaPedido(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Pedido confirmado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel confirmar o pedido nesse momento.");
		}
	}
	
	public void confirmarPedidoCertidaoNegativa() {
		try {
			if (getPedidoCertidao().isSaidaViaCorreios() && getPedidoCertidao().getValorFrete() == null) {
				Mensagem.errorSemBundle("Favor informar o valor do frete para poder confirmar o pedido de certid�o negativa.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.confirmaPedidoCertidaoNegativa(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Pedido de Certid�o Negativa confirmado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel confirmar o pedido de Certid�o Negativa nesse momento.");
		}
	}
	
	
	
	public void rejeitarPedido() {
		try {
			setPedidoCertidao(pedidoCertidaoBO.rejeitarPedido(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Pedido rejeitado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel rejeitar o pedido nesse momento.");
		}
	}
	
	public void salvarEAnexarCertidaoNegativa() {
		try {
			if (getPedidoCertidao().getAnexo() == null) {
				Mensagem.errorSemBundle("Favor anexar o PDF da certid�o negativa antes de finalizar o pedido.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.salvarEAnexarCertidaoNegativa(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Certid�o negativa anexada com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel anexar a certid�o negativa nesse momento.");
		}
	}
	
	public void liberarColetaCertidao() {
		try {
			if (getPedidoCertidao().isSaidaViaCorreios() && getPedidoCertidao().getValorFrete() == null) {
				Mensagem.errorSemBundle("Favor informar o valor do frete para poder confirmar o pedido de certid�o.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.liberaColetaCertidaoCidadao(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Pedido de certid�o liberado para coleta do cidad�o.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel liberar a certid�o para coleta nesse momento.");
		}
	}
	
	public void salvarEAnexarXmlCertidao() {
		try {
			if (getPedidoCertidao().getAnexo() == null) {
				Mensagem.errorSemBundle("Favor anexar o XML da certid�o antes de finalizar o pedido.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.salvarEAnexarXmlCertidao(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("XML da certid�o anexado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel anexar o XML da certid�o nesse momento.");
		}
	}
	
	public void finalizarPedido() {
		try {
			setPedidoCertidao(pedidoCertidaoBO.finalizarPedido(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Pedido de certid�o finalizado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi poss�vel finalizar o pedido nesse momento.");
		}
	}
	
	public void salvarCodigoRastreio() {
		try {
			if (!Util.ehStringValida(getPedidoCertidao().getCodigoRastreio())) {
				Mensagem.errorSemBundle("Favor informar o c�digo de rastreio da certid�o.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.salvarCodigoRastreio(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("C�digo de rastreio da certid�o salvo com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi salvar o c�digo de rastreio da certid�o nesse momento.");
		}
	}
	
	public void realizaUploadAnexoCertidao(FileUploadEvent event) {
		try {
			getPedidoCertidao().setAnexo(null);
			UploadUtils.validaUploadPdfExtensaoETamanho(event);
			getPedidoCertidao().setAnexo(event.getFile().getContents());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void realizaUploadXmlCertidao(FileUploadEvent event) {
		try {
			getPedidoCertidao().setAnexo(null);
			if (!event.getFile().getFileName().toUpperCase().endsWith("XML.DSIG")) {
				throw new UploadException("S� � permitido anexar arquivos com exten��o XML.DSIG");
			}
			getPedidoCertidao().setAnexo(event.getFile().getContents());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rejeitarSolicitacaoEstorno() {
		try {
			if (!Util.ehStringValida(getPedidoCertidao().getSolicitacaoEstorno().getDescricaoRejeicao())) {
				Mensagem.errorSemBundle("Favor informar o motivo da rejei��o do estorno.");
				return;
			}
			setPedidoCertidao(pedidoCertidaoBO.rejeitarEstorno(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Solicita��o de estorno rejeitada com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi rejeitar a solicita��o de estorno nesse momento.");
		}
	}
	
	public void realizarEstorno() {
		try {
			setPedidoCertidao(pedidoCertidaoBO.realizarEstorno(getPedidoCertidao(), getUsuarioLogadoPortal()));
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITADO");
			Mensagem.infoSemBundle("Estorno realizado com sucesso.");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("N�o foi realizar o estorno nesse momento.");
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
		
		if (getPedidoCertidao().isPedidoDeCartorioParaCartorio()) {
			return getPedidoCertidao().recuperaValoresCertidaoRequisitado().getValorTotal();
		}
		
		return getPedidoCertidao().getValorTotal();
	}
	
	public BigDecimal recuperaAliquotaISSQN() {
		if (getPedidoCertidao() == null) {
			return null;
		}
		return aliquotaISSQNBO.recuperaPorcentagemAliquotaISSQN(getPedidoCertidao().getCodCorregedoriaRequisitada());
	}
	
	public void exibirAnexoCertidao() {
		super.exibirPDF(getPedidoCertidao().getAnexo(), "Anexo_Certidao");
	}
	
	public void removerAnexoCertidao() {
		getPedidoCertidao().setAnexo(null);
	}
	
	public EnumMotivoRejeicao[] listaMotivoRejeicao(){
		return EnumMotivoRejeicao.values();
	}
	
	public boolean exibeHistoricoPedidoCertidao() {
		return getPedidoCertidao() != null && getPedidoCertidao().getListaHistoricoPedidoCertidaoInicializada().size() > 0;
	}
	
	public boolean exibirDadosFrete() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSaidaViaCorreios() && getPedidoCertidao().isSituacaoEmAnalise() && exibeBotoesAcoesPedidoCertidao();
	}

	public boolean exibeConfirmarRejeitarPedido() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSituacaoEmAnalise() && !getPedidoCertidao().isPedidoCertidaoNegativa();
	}
	
	public boolean exibeConfirmarRejeitarPedidoCertidaoNegativa() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSituacaoEmAnalise() && getPedidoCertidao().isPedidoCertidaoNegativa();
	}
	
	public boolean exibeConfirmarPedidoCertidaoNegativa() {
		return getPedidoCertidao() != null && getPedidoCertidao().isPedidoCertidaoNegativa() && getPedidoCertidao().isSituacaoEmAnalise();
	}
	
	public boolean exibeInformarCodigoRastreio() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSaidaViaCorreios() && getPedidoCertidao().isSituacaoProntoParaEnvio();
	}
	
	public boolean exibeAnexarXMLCertidao() {
		return getPedidoCertidao() != null && getPedidoCertidao().isPedidoDeCartorioParaCartorio() && (getPedidoCertidao().isSituacaoProntoParaEnvio()
				|| getPedidoCertidao().isSituacaoPagamentoAprovadoAguardandoLiberacao() || getPedidoCertidao().isSituacaoArquivoNaoAceito());
	}
	
	public boolean exibeAnalisarEstorno() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSituacaoSolicitacaoEstorno();
	}
	
	public boolean exibeBtnFinalizarPedido() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSituacaoColetaNoCartorio();
	}
	
	public boolean exibeLiberarColetaCertidao() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSaidaBuscaPresencial() && getPedidoCertidao().isSituacaoPagamentoAprovadoAguardandoLiberacao();
	}
	
	public boolean exibeAnexarCertidaoNegativa() {
		return exibeAnexarCertidao() && getPedidoCertidao().isPedidoCertidaoNegativa();
	}
	
	public boolean exibeAnexarCertidao() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSaidaDigital() && getPedidoCertidao().isSituacaoPagamentoAprovadoAguardandoLiberacao();
	}
	
	public boolean exibePerguntaAverbacao() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSituacaoEmAnalise() && !getPedidoCertidao().isPedidoCertidaoNegativa() && exibeBotoesAcoesPedidoCertidao();
	}
	
	public boolean exibeMotivoRejeicaoEstorno() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSituacaoEstornoRejeitado() && getPedidoCertidao().getSolicitacaoEstorno() != null && Util.ehStringValida(getPedidoCertidao().getSolicitacaoEstorno().getDescricaoRejeicao());
	}
	
	public boolean exibeDescricaoSolicitacaoEstorno() {
		return getPedidoCertidao() != null && (getPedidoCertidao().isSituacaoEstornoRealizado() || getPedidoCertidao().isSituacaoEstornoRejeitado())
				&& getPedidoCertidao().getSolicitacaoEstorno() != null && Util.ehStringValida(getPedidoCertidao().getSolicitacaoEstorno().getDescricaoSolicitacao());
	}
	
	public boolean exibeBotoesAcoesPedidoCertidao() {
		return getUsuarioLogadoPortal().isAdmin() || getUsuarioLogadoPortal().isPermissaoFuncionario() || getUsuarioLogadoPortal().isPermissaoOficial();
	}
	
	public String voltar() {
		super.setarObjetoNoFlash(getFiltroPesquisaCertidao(), "filtroPesquisaCertidao");
		return EnumPaginas.PESQUISAR_PEDIDOS_CERTIDAO.getUrl();
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

	public FiltroPesquisaPedidoCertidaoVO getFiltroPesquisaCertidao() {
		return filtroPesquisaCertidao;
	}

	public void setFiltroPesquisaCertidao(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		this.filtroPesquisaCertidao = filtroPesquisaCertidao;
	}
	
}
