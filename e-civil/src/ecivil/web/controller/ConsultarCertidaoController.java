package ecivil.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.relatorio.RelatorioReciboCertidao;
import ecivil.ejb.util.Util;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;
import web.util.Paginacao;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class ConsultarCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;

	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private RelatorioReciboCertidao relatorioReciboCertidao;
	
	private List<PedidoCertidao> listaPedidoCertidao;
	private PedidoCertidao pedidoCertidao;
	private Paginacao paginacao;
	private String protocolo;

	@PostConstruct
	public void inicializa() throws Exception {
		setPaginacao(new Paginacao());
		recuperaPedidoCertidao();
	}

	public void recuperaPedidoCertidao() {
		try {
			setListaPedidoCertidao(new ArrayList<>());
			paginacao.limpaPaginacao();
			paginacao.setTotalRegistros(pedidoCertidaoBO.recuperaTotalPedidoCertidaoUsuario(getUsuarioLogadoPortal()));
			recuperaPedidoCertidaoPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível buscar os pedidos de certidão nesse momento.");
		}
	}

	public void recuperaPedidoCertidaoPaginado() {
		if (paginacao.getTotalRegistros() > 0) {
			setListaPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoUsuario(getUsuarioLogadoPortal(), getPaginacao()));
		} else {
			setListaPedidoCertidao(new ArrayList<>());
		}
	}
	
	public void pesquisarPorProtocolo() {
		try {
			if (!Util.ehStringValida(getProtocolo())) {
				Mensagem.errorSemBundle("Favor informar o protocolo para realizar a pesquisa.");
				return;
			}
			
			setListaPedidoCertidao(null);
			PedidoCertidao pedidoPesquisado = pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocoloEUsuarioExterno(getProtocolo(), getUsuarioLogadoPortal());
			if (pedidoPesquisado != null) {
				setListaPedidoCertidao(Arrays.asList(pedidoPesquisado));
				paginacao.setTotalRegistros(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar a certidão pelo protocolo informado nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void limpar() {
		setProtocolo(null);
		setListaPedidoCertidao(null);
		recuperaPedidoCertidao();
	}
	
	public String buscarDadosCartorio() {
		try {
			if (getPedidoCertidao() == null) {
				return "";
			}
			getPedidoCertidao().setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getPedidoCertidao().getCodCorregedoriaRequisitada()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados do cartório nesse momento.");
		}
		return "";
	}
	
	public void setaPedidoCertidao(PedidoCertidao pedidoCertidao) {
		try {
			setPedidoCertidao(pedidoCertidaoBO.recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo()));
			atualizarDataHoraVisualizacaoRequisitante();
			buscarDadosCartorio();
		} catch (Exception e) {
			Mensagem.errorSemBundle(e.getMessage());
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
	
	public void emitirBoleto() {
		try {
			if (getPedidoCertidao() == null) {
				Mensagem.errorSemBundle("Não foi possível emitir o boleto nesse momento.");
				return;
			}
			byte[] boleto = pedidoCertidaoBO.emitirBoleto(getPedidoCertidao(), getUsuarioLogadoPortal());
			super.exibirPDF(boleto, "Boleto_Certidao.pdf");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle("Não foi possível emitir o boleto nesse momento - " + e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível emitir o boleto nesse momento.");
		}
	}
	
	public void downloadReciboCertidao() {
		try {
			super.downloadPDF(relatorioReciboCertidao.geraRelatorioReciboCertidao(getPedidoCertidao()), montaNomeArquivo("Recibo", getPedidoCertidao().getProtocolo()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível fazer download da certidão nesse momento.");
		}
	}
	
	public void downloadCertidao() {
		try {
			if (getPedidoCertidao().getAnexoCertidao() == null || getPedidoCertidao().getAnexoCertidao().getCertidao() == null) {
				Mensagem.errorSemBundle("Documento da certidão não encontrado.");
				return;
			}
			super.downloadPDF(pedidoCertidaoBO.downloadCertidaoDigital(getPedidoCertidao()), "Certidao.pdf");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível fazer download da certidão nesse momento.");
		}
	}
	
	public void solicitarCertidaoNegativa() {
		try {
			pedidoCertidaoBO.salvarSolicitacaoCertidaoNegativa(getPedidoCertidao());
			recuperaPedidoCertidao();
			exibeMensagemSucessoSolicitacaoNegativa();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível realizar a solicitação da certidão negativa nesse momento.");
		}
	}
	
	private void exibeMensagemSucessoSolicitacaoNegativa() {
		if (getPedidoCertidao().isSaidaViaCorreios()) {
			Mensagem.infoSemBundle("Solicitação de certidão negativa realizada com sucesso. Favor aguardar o cartório calcular o valor do frete para prosseguir com o pagamento de sua solicitação.");
		} else {
			Mensagem.infoSemBundle("Solicitação de certidão negativa realizada com sucesso. Nesse momento já está disponível a emissão do boleto para pagamento da certidão negativa.");
		}
	}

	public boolean exibeReciboCertidao() {
		return getPedidoCertidao() != null && (getPedidoCertidao().isSituacaoColetaNoCartorio() || getPedidoCertidao().isSituacaoPedidoFinalizado());
	}
	
	public boolean exibeDownloadCertidao() {
		return getPedidoCertidao() != null && getPedidoCertidao().isSaidaDigital() && getPedidoCertidao().isSituacaoPedidoFinalizado();
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
	
	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}
	
	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	
}
