package ecivil.adm.controller.pedidos;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.CertidaoNegativaBO;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.bo.TipoCertidaoBO;
import ecivil.ejb.entidade.CertidaoNegativa;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.TipoCertidao;
import ecivil.ejb.enumeration.EnumSimNao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.ws.cliente.rest.response.integradorcrc.DadosCertidaoResponse;
import web.util.JSFUtil;
import web.util.VerificaRecaptcha;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class ResultadoBuscaCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	@EJB
	private CertidaoNegativaBO certidaoNegativaBO;
	
	@EJB
	private TipoCertidaoBO tipoCertidaoBO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	private PedidoCertidao pedidoCertidao;
	private DadosCertidaoResponse certidao;
	private CertidaoNegativa certidaoNegativa;
	private List<TipoCertidao> listaTipoCertidao;
//	private Paginacao paginacao;
	private int pagina;

	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		inicializaCertidao();
		setPagina(0);
//		inicializaPaginacao();
	}

	private void inicializaCertidao() throws Exception {
		setPedidoCertidao((PedidoCertidao) JSFUtil.get("pedidoCertidao"));
		if (getPedidoCertidao() == null) {
			redireciona(EnumPaginas.PRINCIPAL.getUrlJsf());
		}
	}
	
//	private void inicializaPaginacao() {
//		setPaginacao(new Paginacao());
//		if (getPedidoCertidao().possuiRegistrosBuscaCertidao()) {
//			getPaginacao().setTotalRegistros(getPedidoCertidao().getResultadoBuscaCertidao().getTotalRegistros());
//		}
//	}
	
	public void setaCertidaoSolicitacao(DadosCertidaoResponse certidao) {
		try {
			getPedidoCertidao().setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(certidao.getCodCorregedoria()));
			setCertidao(certidao);
			pedidoCertidaoBO.setaDemonstracaoValores(getCertidao(), getPedidoCertidao());
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível buscar os dados do cartório nesse momento.");
		}
	}

	public String salvarSolicitacaoCertidao() {
		try {
			if (getCertidao() == null) {
				return null;
			}
			verificaRECAPTCHA(JSFUtil.getRequest().getParameter("g-recaptcha-response"));
			pedidoCertidaoBO.validaPedidoCartorioParaCartorio(getPedidoCertidao(), getCertidao());
			pedidoCertidaoBO.salvarNovoPedidoCertidao(getPedidoCertidao(), getCertidao(), EnumSimNao.N);
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			super.setarObjetoNoFlash(getPedidoCertidao(), "pedidoCertidao");
			return EnumPaginas.SOLICITACAO_CERTIDAO_REALIZADA.getUrl();
		} catch (ECivilException e) {
			if (e.possuiListaErros()) {
				Mensagem.listaErrosSemBundle(e.getListaErros());
			} else {
				Mensagem.errorSemBundle(e.getMensagemErro());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível salvar os dados da sua solicitação nesse momento. Tente novamente mais tarde.");
		}

		return "";
	}
	
	public void verificaRECAPTCHA(String gRecaptchaResponse) throws ECivilException, IOException{
		if(!VerificaRecaptcha.verificar(gRecaptchaResponse)) {
			throw new ECivilException("É obrigatório confirmar que você não é um robô.");
		}
	}
	
	public void salvarSolicitacaoCertidaoNegativa() {
		try {
			if (getCertidaoNegativa().getTipoCertidao() == null) {
				Mensagem.errorSemBundle("Favor selecionar o 'Tipo de Cerdidão' para salvar os dados da certidão negativa.");
				return;
			}
			
			if (getCertidaoNegativa().getId() != null) {
				Mensagem.errorSemBundle("Os dados dessa certidão negativa já foram salvos.");
				return;
			}
			
			certidaoNegativaBO.salvarNovaSolicitacaoCertidaoNegativa(getCertidaoNegativa());
			Mensagem.infoSemBundle("Dados da certidão negativa salvo com sucesso.");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível salvar os dados da certidão negativa nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public String geraHashCertidaoNegativa(String idModal) {
		try {
			if (getCertidaoNegativa() == null) {
				setCertidaoNegativa(certidaoNegativaBO.gerarCertidaoNegativaCartorio(getPedidoCertidao(), getUsuarioLogadoPortal()));
			}
			inicializaTipoCertidao();
			abrirModal(idModal);
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível gerar o código Hash nesse momento.");
		}
		return "";
	}

	private void inicializaTipoCertidao() {
		if (getListaTipoCertidao() == null || getListaTipoCertidao().isEmpty()) {
			setListaTipoCertidao(tipoCertidaoBO.listaTodosTipoCertidao());
		}
	}
	
	public void proximaPaginaCertidao() {
		if (getPedidoCertidao().getResultadoBuscaCertidao() == null || getPedidoCertidao().getResultadoBuscaCertidao().getDadosCertidao() == null
				|| getPedidoCertidao().getResultadoBuscaCertidao().getDadosCertidao().isEmpty()) {
			
			return;
		}
		
		setPagina(getPagina() + 1);
		pesquisaCertidaoPaginado();
		
		if (!exibeProximaPagina()) {
			paginaAnteriorCertidao();
			abrirModal("modalResultadoBuscaVazio");
		}
	}
	
	public void paginaAnteriorCertidao() {
		if (getPagina() == 0) {
			return;
		}
		setPagina(getPagina() - 1);
		pesquisaCertidaoPaginado();
	}
	
	private void pesquisaCertidaoPaginado() {
		try {
			pedidoCertidaoBO.buscarCertidaoIndexada(getPedidoCertidao(), getPedidoCertidao().getNomePessoaPesquisa(), getPedidoCertidao().getNomeAssociado(),
					getPedidoCertidao().getMunicipioPesquisa(), getPedidoCertidao().getCpf(),
					getPedidoCertidao().isNomeIdentico(), getPagina());
//			pedidoCertidaoBO.buscarCertidaoIndexada(getPedidoCertidao(), getPedidoCertidao().getNomePessoaPesquisa(), getPaginacao().getPrimeiroRegistro());
//			getPaginacao().setTotalRegistros(getPedidoCertidao().getResultadoBuscaCertidao().getTotalRegistros());
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar as certidões pesquisada nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public boolean exibirSolicitarCertidao(DadosCertidaoResponse dadosCertidaoResponse) {
		return dadosCertidaoResponse.podeGerarPedidoCertidao();
	}
	
	public boolean exibePaginaAnterior() {
		return getPagina() > 0;
	}
	
	public boolean exibeProximaPagina() {
		return getPedidoCertidao() != null && getPedidoCertidao().getResultadoBuscaCertidao() != null && getPedidoCertidao().getResultadoBuscaCertidao().possuiResultadoBusca();
	}
	
	public String navegaPedidoSegundaVia() throws Exception {
		super.setarObjetoNoFlash(getPedidoCertidao(), "pedidoCertidao");
		return EnumPaginas.FORMULARIO_PEDIDO_CERTIDAO.getUrl();
	}
	
	public String voltar() throws Exception {
		super.setarObjetoNoFlash(getPedidoCertidao(), "pedidoCertidao");
		return EnumPaginas.REALIZAR_PEDIDO_CERTIDAO.getUrl();
	}

	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}
	
	public DadosCertidaoResponse getCertidao() {
		return certidao;
	}

	public void setCertidao(DadosCertidaoResponse certidao) {
		this.certidao = certidao;
	}

	public CertidaoNegativa getCertidaoNegativa() {
		return certidaoNegativa;
	}

	public void setCertidaoNegativa(CertidaoNegativa certidaoNegativa) {
		this.certidaoNegativa = certidaoNegativa;
	}

	public List<TipoCertidao> getListaTipoCertidao() {
		return listaTipoCertidao;
	}

	public void setListaTipoCertidao(List<TipoCertidao> listaTipoCertidao) {
		this.listaTipoCertidao = listaTipoCertidao;
	}

	public int getPagina() {
		return pagina;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	
//	public Paginacao getPaginacao() {
//		return paginacao;
//	}
//
//	public void setPaginacao(Paginacao paginacao) {
//		this.paginacao = paginacao;
//	}
	
}
