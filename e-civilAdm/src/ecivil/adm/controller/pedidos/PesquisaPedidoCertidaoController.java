package ecivil.adm.controller.pedidos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.bo.SituacaoPedidoCertidaoBO;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.SituacaoPedidoCertidao;
import ecivil.ejb.entidade.UsuarioServentia;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.FiltroPesquisaPedidoCertidaoVO;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.JSFUtil;
import web.util.Paginacao;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class PesquisaPedidoCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	@EJB
	private SituacaoPedidoCertidaoBO situacaoPedidoCertidaoBO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;

	private List<PedidoCertidao> listaPedidoCertidao;
	private List<CartorioResponse> listaCartorio;
	private PedidoCertidao pedidoCertidao;
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
		inicializaSituacaoPedidoCertidao();
		inicializaFiltroPesquisaCertidao();
		setaCorregedoriaUsuarioLogado();
		pesquisarPedidosCertidao();
	}
	
	private void inicializaListaCartorio() {
		try {
			if (getUsuarioLogadoPortal().isAdmin() || getUsuarioLogadoPortal().isPermissaoConsulta()) {
				setListaCartorio(cartorioLookUp.recuperaListaCartorio());
				return;
			}
			List<String> listaCodCorregedoria = getUsuarioLogadoPortal().getUsuarioServentiaAtiva().stream().map(UsuarioServentia::getCodigoCorregedoria).collect(Collectors.toList());
			setListaCartorio(cartorioLookUp.recuperaListaCartorioPorCodigoCorregedoria(listaCodCorregedoria));
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
		if (getUsuarioLogadoPortal().isAdmin() || getUsuarioLogadoPortal().isPermissaoConsulta()) {
			return;
		}
		
		if (getUsuarioLogadoPortal().getUsuarioServentiaAtiva().isEmpty()) {
			getFiltroPesquisaCertidao().getListaCorregedoriaRequisitadaInicializada().add("");
		}
		
		getFiltroPesquisaCertidao().getListaCorregedoriaRequisitadaInicializada().addAll(getUsuarioLogadoPortal().recuperaListaCodigoCorregedoriaUsuario());
	
		if (getFiltroPesquisaCertidao().getListaCorregedoriaRequisitadaInicializada().size() == 1) {
			getFiltroPesquisaCertidao().setCorregedoriaRequisitada(getFiltroPesquisaCertidao().getListaCorregedoriaRequisitada().get(0));
		}
	}

	public void pesquisarPedidosCertidao() {
		try {
			setListaPedidoCertidao(new ArrayList<>());
			paginacao.limpaPaginacao();
			paginacao.setTotalRegistros(pedidoCertidaoBO.pesquisarTotalPedidosCertidao(getFiltroPesquisaCertidao()));
			pesquisarPedidosCertidaoPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar os pedidos de certidão nesse momento.");
		}
	}

	public void pesquisarPedidosCertidaoPaginado() {
		if (paginacao.getTotalRegistros() > 0) {
			setListaPedidoCertidao(pedidoCertidaoBO.pesquisarPedidosCertidao(getFiltroPesquisaCertidao(), getPaginacao()));
		} else {
			setListaPedidoCertidao(new ArrayList<>());
		}
		setPesquisaRealizada(Boolean.TRUE);
	}
	
	public void setaRequisitante(PedidoCertidao pedidoCertidao) {
		try {
			if (Util.ehStringValida(pedidoCertidao.getCodCorregedoriaRequisitante())) {
				pedidoCertidao.setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitante()));
			}
			setPedidoCertidao(pedidoCertidao);
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados do requisitante nesse momento.");
		}
	}
	
	public String navegaDadosPedido(PedidoCertidao pedidoCertidao) {
		super.setarObjetoNoFlash(getFiltroPesquisaCertidao(), "filtroPesquisaCertidao");
		super.setarObjetoNoFlash(pedidoCertidao.getProtocolo(), "protocoloPedidoCertidao");
		return EnumPaginas.DADOS_PEDIDO_CERTIDAO.getUrl();
	}
	
	public String recuperaNomeDistritoECartorio(String codCorregedoria) {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codCorregedoria);
			return cartorio != null ? cartorio.getNomeDistritoECartorio() : "";
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível recuperar a(s) serventias do usuário nesse momento. Tente novamente mais tarde."); 
			e.printStackTrace();
		}
		return "";
	}
	
	public void limpar() {
		setFiltroPesquisaCertidao(new FiltroPesquisaPedidoCertidaoVO());
		paginacao.limpaPaginacao();
		setPesquisaRealizada(Boolean.FALSE);
		setListaPedidoCertidao(new ArrayList<>());
		setaCorregedoriaUsuarioLogado();
	}
	
	public boolean desabilitaComboSelecionarServentia() {
		return !getUsuarioLogadoPortal().isAdmin() && !getUsuarioLogadoPortal().isPermissaoConsulta() && getUsuarioLogadoPortal().possuiApenasUmaServentia();
	}
	
	public boolean exibeColunaServentiaTabela() {
		return getUsuarioLogadoPortal().isAdmin() || getUsuarioLogadoPortal().isPermissaoConsulta() || !getUsuarioLogadoPortal().possuiApenasUmaServentia();
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
