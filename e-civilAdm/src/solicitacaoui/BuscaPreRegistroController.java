package solicitacaoui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.bo.SituacaoPedidoCertidaoBO;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.vo.FiltroPesquisaPedidoCertidaoVO;
import web.util.Paginacao;
@SuppressWarnings("serial")
@ViewScoped
@Named
public class BuscaPreRegistroController extends BaseController implements Serializable  {


	private SituacaoSolicitacaoUI situacaoSolicitacaoUI;
	private PreRegistro solicitacaoUINascimento;
	private List<SituacaoSolicitacaoUI> listaSituacaoSolicitacaoUI;
	private FiltroPesquisaPedidoCertidaoVO filtroPesquisaSolicitacaoUI;
	private Paginacao paginacao;
	@EJB
	private SituacaoPedidoCertidaoBO situacaoPedidoCertidaoBO;
	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		setPaginacao(new Paginacao());
		if(getSolicitacaoUINascimento() ==null) {
			setSolicitacaoUINascimento(new PreRegistro());
		}
		inicializaSituacaoSolicitacaoUI();
	}

	/*private void inicializaFiltroPesquisaCertidao() {
		FiltroPesquisaSolicitacaoUIVO FiltroPesquisaSolicitacaoUIVO = (FiltroPesquisaSolicitacaoUIVO) JSFUtil.get("filtroPesquisaSolicitacao");
		if (FiltroPesquisaSolicitacaoUIVO != null) {
			setFiltroPesquisaSolicitacao(FiltroPesquisaSolicitacaoUIVO);
			pesquisarSolicitacaoUI();
		}
	}*/
	
	

	
	public FiltroPesquisaPedidoCertidaoVO getFiltroPesquisaSolicitacaoUI() {
		return filtroPesquisaSolicitacaoUI;
	}

	public void setFiltroPesquisaSolicitacaoUI(FiltroPesquisaPedidoCertidaoVO filtroPesquisaSolicitacaoUI) {
		this.filtroPesquisaSolicitacaoUI = filtroPesquisaSolicitacaoUI;
	}

	public SituacaoPedidoCertidaoBO getSituacaoPedidoCertidaoBO() {
		return situacaoPedidoCertidaoBO;
	}

	public void setSituacaoPedidoCertidaoBO(SituacaoPedidoCertidaoBO situacaoPedidoCertidaoBO) {
		this.situacaoPedidoCertidaoBO = situacaoPedidoCertidaoBO;
	}

	public PedidoCertidaoBO getPedidoCertidaoBO() {
		return pedidoCertidaoBO;
	}

	public void setPedidoCertidaoBO(PedidoCertidaoBO pedidoCertidaoBO) {
		this.pedidoCertidaoBO = pedidoCertidaoBO;
	}

	
	public List<SituacaoSolicitacaoUI> getListaSituacaoSolicitacaoUI() {
		return listaSituacaoSolicitacaoUI;
	}

	public void setListaSituacaoSolicitacaoUI(List<SituacaoSolicitacaoUI> listaSituacaoSolicitacaoUI) {
		this.listaSituacaoSolicitacaoUI = listaSituacaoSolicitacaoUI;
	}
	
	public void pesquisarSolicitacoesUI() {
		try {
			setListaSituacaoSolicitacaoUI(new ArrayList<>());
			paginacao.limpaPaginacao();
			paginacao.setTotalRegistros(pedidoCertidaoBO.pesquisarTotalSolicitacoesUI(getFiltroPesquisaSolicitacaoUI()));
//			pesquisarPedidosCertidaoPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar as solicitações de UI nesse momento.");
		}
	}
	
	/*public void listaSituacaoSolicitacaoUI() {
		
	}*/

	
	
	/*public void pesquisarSolicitacaoUI() {
		try {
			setListaSolicitacaoUINascimento(new ArrayList<>());
			paginacao.limpaPaginacao();
			paginacao.setTotalRegistros(SolicitacaoUINascimentoBO.pesquisarTotalPedidosRealizadoPeloCartorio(getUsuarioLogadoPortal(), getFiltroPesquisaCertidao()));
//			pesquisarSolucitacaoUINascimentoPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar as solicitações de Unidade Interligada nesse momento.");
		}
	}*/
	
	/*public void pesquisarSolucitacaoUINascimentoPaginado() {
		if (paginacao.getTotalRegistros() > 0) {
			setListaPedidoCertidao(solicitacaoUINascimentoBO.pesquisarTotalSolicitacoesUIRecebidasPeloCartorio(getUsuarioLogadoPortal(), getFiltroPesquisaCertidao(), getPaginacao()));
		} else {
			setListaPedidoCertidao(new ArrayList<>());
		}
		setPesquisaRealizada(Boolean.TRUE);
	}*/
	public void situacaoSolicitacaoUI() {
		System.out.println("PASSOU AQUI ");
	}
	public PreRegistro getSolicitacaoUINascimento() {
		//System.out.println(solicitacaoUINascimento.getCpfMae());
		return solicitacaoUINascimento;
	}
	public void setSolicitacaoUINascimento(PreRegistro solicitacaoUINascimento) {
		this.solicitacaoUINascimento = solicitacaoUINascimento;
	}
	public SituacaoSolicitacaoUI getSituacaoSolicitacaoUI() {
		return situacaoSolicitacaoUI;
	}
	public void setSituacaoSolicitacaoUI(SituacaoSolicitacaoUI situacaoSolicitacaoUI) {
		this.situacaoSolicitacaoUI = situacaoSolicitacaoUI;
	}
	
	private void inicializaSituacaoSolicitacaoUI() {
		setListaSituacaoSolicitacaoUI(situacaoPedidoCertidaoBO.recuperaListaSituacaoSolicitacaoUI());
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
}

