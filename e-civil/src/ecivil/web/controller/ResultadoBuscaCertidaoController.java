package ecivil.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.EnderecoUsuarioExternoBO;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.entidade.EnderecoUsuarioExterno;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.enumeration.EnumSimNao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.ws.cliente.rest.response.integradorcrc.DadosCertidaoResponse;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;
import web.util.JSFUtil;
import web.util.VerificaRecaptcha;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class ResultadoBuscaCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;

	@EJB
	private EnderecoUsuarioExternoBO enderecoUsuarioExternoBO;

	@EJB
	private CartorioLookUp cartorioLookUp;

	private PedidoCertidao pedidoCertidao;
	private DadosCertidaoResponse certidao;
	private List<EnderecoUsuarioExterno> listaEnderecoUsuario;
	private int pagina;
	//	private Paginacao paginacao;

	@PostConstruct
	public void inicializa() throws Exception {
		inicializaCertidao();
		inicializaListaEnderecoEntrega();
		setPagina(0);
		//		inicializaPaginacao();
	}

	private void inicializaCertidao() throws Exception {
		setPedidoCertidao(recuperarPedidoCertidaoNoFlash());
		if (getPedidoCertidao() == null) {
			redireciona(EnumPaginas.PRINCIPAL.getUrl());
		}
	}

	private void inicializaListaEnderecoEntrega() {
		if (getPedidoCertidao().isSaidaViaCorreios()) {
			setListaEnderecoUsuario(enderecoUsuarioExternoBO
					.recuperaListaEnderecoUsuarioExterno(getPedidoCertidao().getUsuarioExterno()));
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
			getPedidoCertidao()
					.setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(certidao.getCodCorregedoria()));
			setCertidao(certidao);
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
			pedidoCertidaoBO.salvarNovoPedidoCertidao(getPedidoCertidao(), getCertidao(), EnumSimNao.N);
			pedidoCertidaoBO.atualizarDataHoraUltimaVisualizacaoPedido(getPedidoCertidao(), "REQUISITANTE");
			setarPedidoCertidaolNoFlash(getPedidoCertidao());
			return EnumPaginas.SOLICITACAO_CERTIDAO_REALIZADA.getUrl();
		} catch (ECivilException e) {
			if (e.possuiListaErros()) {
				Mensagem.listaErrosSemBundle(e.getListaErros());
			} else {
				Mensagem.errorSemBundle(e.getMensagemErro());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível salvar os dados da sua solicitação nesse momento.");
		}

		return "";
	}

	public void verificaRECAPTCHA(String gRecaptchaResponse) throws ECivilException, IOException{
		if(!VerificaRecaptcha.verificar(gRecaptchaResponse)) {
			throw new ECivilException("É obrigatório confirmar que você não é um robô.");
		}
	}
	
	public void proximaPaginaCertidao() {
		if (getPedidoCertidao().getResultadoBuscaCertidao() == null
				|| getPedidoCertidao().getResultadoBuscaCertidao().getDadosCertidao() == null
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
			pedidoCertidaoBO.buscarCertidaoIndexada(getPedidoCertidao(), getPedidoCertidao().getNomePessoaPesquisa(),
					getPedidoCertidao().getNomeAssociado(), getPedidoCertidao().getMunicipioPesquisa(),
					getPedidoCertidao().getCpf(), getPedidoCertidao().isNomeIdentico(), getPagina());
			//			pedidoCertidaoBO.buscarCertidaoIndexada(getPedidoCertidao(), getPedidoCertidao().getNomePessoaPesquisa(), getPaginacao().getPrimeiroRegistro());
			//			getPaginacao().setTotalRegistros(getPedidoCertidao().getResultadoBuscaCertidao().getTotalRegistros());
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle(
					"Não foi possível recuperar as certidões pesquisada nesse momento. Tente novamente mais tarde.");
		}
	}

	public boolean exibirSolicitarCertidao(DadosCertidaoResponse dadosCertidaoResponse) {
		return dadosCertidaoResponse.podeGerarPedidoCertidao();
	}

	public boolean exibePaginaAnterior() {
		return getPagina() > 0;
	}

	public boolean exibeProximaPagina() {
		return getPedidoCertidao() != null && getPedidoCertidao().getResultadoBuscaCertidao() != null
				&& getPedidoCertidao().getResultadoBuscaCertidao().possuiResultadoBusca();
	}

	public String direcionarEditarDadosUsuario() {
		return EnumPaginas.CADASTRO_USUARIO_PORTAL_EXTERNO.getUrl();
	}

	public String navegaPedidoSegundaVia() throws Exception {
		setarPedidoCertidaolNoFlash(getPedidoCertidao());
		return EnumPaginas.FORMULARIO_PEDIDO_CERTIDAO.getUrl();
	}

	public String voltar() throws Exception {
		setarPedidoCertidaolNoFlash(getPedidoCertidao());
		return EnumPaginas.BUSCAR_CERTIDAO.getUrl();
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

	public List<EnderecoUsuarioExterno> getListaEnderecoUsuario() {
		return listaEnderecoUsuario;
	}

	public void setListaEnderecoUsuario(List<EnderecoUsuarioExterno> listaEnderecoUsuario) {
		this.listaEnderecoUsuario = listaEnderecoUsuario;
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
