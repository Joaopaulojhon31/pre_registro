package ecivil.adm.controller.contadigital;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.SolicitacaoCreditoBO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.entidade.SolicitacaoCredito;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.Paginacao;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class AdicionarSaldoController extends BaseController implements Serializable {

	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private SolicitacaoCreditoBO solicitacaoCreditoBO;
	
	@EJB
	private ParametroDAO parametroDAO;

	private BigDecimal valorCredito;
	private BigDecimal totalDisponivelSaque;
	private CartorioResponse cartorio;
	private List<SolicitacaoCredito> listaSolicitacaoCredito;
	private Paginacao paginacao;
	private boolean consultaSaldoRealizada;

	@PostConstruct
	public void inicializa() {
		try {
			super.validaServentiaSelecionada();
			setPaginacao(new Paginacao());
			recuperaSaldoDisponivel();
			recuperaListaSolicitacaoCredito();
			setConsultaSaldoRealizada(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void recuperaSaldoDisponivel() throws ECivilException {
		setTotalDisponivelSaque(contaDigitalWS.consultaSaldo(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado()));
	}

	private void recuperaListaSolicitacaoCredito() {
		try {
			setListaSolicitacaoCredito(new ArrayList<>());
			paginacao.limpaPaginacao();
			paginacao.setTotalRegistros(solicitacaoCreditoBO.recuperaTotalSolicitacaoCredito(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado()));
			recuperaListaSolicitacaoCreditoPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar as ultimas solicitações de adição de crédito realizada nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void recuperaListaSolicitacaoCreditoPaginado() {
		if (paginacao.getTotalRegistros() > 0) {
			setListaSolicitacaoCredito(solicitacaoCreditoBO.recuperaSolicitacaoCredito(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado(), getPaginacao()));
		} else {
			setListaSolicitacaoCredito(new ArrayList<>());
		}
	}
	
	public void adicionarCredito() {
		try {
			solicitacaoCreditoBO.adicionarSolicitacaoCredito(getUsuarioLogadoPortal(), getValorCredito(), getCartorio());
			setValorCredito(null);
			recuperaListaSolicitacaoCredito();
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível realizar a solicitação de adição de crédito nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void preparaSolicitacaoAdicionarSaldo(String idModal) {
		try {
			validaValorSaquePreenchido();
			recuperaDadosBancarios();
			abrirModal(idModal);
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível realizar a solicitação de adição de crédito nesse momento. Tente novamente mais tarde.");
		}
	}

	private void recuperaDadosBancarios() throws ECivilException {
		setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado()));
		if (getCartorio() == null) {
			throw new ECivilException("Não foi possível recuperar os dados do cartório nesse momento. Tente novamente mais tarde.");
		}
	}
	
	private void validaValorSaquePreenchido() throws ECivilException {
		if (getValorCredito() == null || getValorCredito().compareTo(new BigDecimal(0)) <= 0) {
			throw new ECivilException("Favor preencher o valor desejado para adição de crédito.");
		} 
		
		BigDecimal valorSaqueMinimo = new BigDecimal(parametroDAO.buscaValorParametro(Parametro.VALOR_MINIMO_ADD_CREDITO));
		if (getValorCredito().compareTo(valorSaqueMinimo) < 0) {
			throw new ECivilException("O valor minimo para adição de crédito é de: R$ " + valorSaqueMinimo);
		}		
	}
	
	public void emitirBoleto(SolicitacaoCredito solicitacaoCredito) {
		try {
			if (solicitacaoCredito.getDataFim() != null) {
				Mensagem.errorSemBundle("Não foi é possível emitir o boleto.");
				return;
			}
			recuperaDadosBancarios();
			byte[] boleto = solicitacaoCreditoBO.imprimirBoleto(solicitacaoCredito, getCartorio());
			super.exibirPDF(boleto, montaNomeArquivo("Boleto", solicitacaoCredito.recuperaVinculoBoletoAtivo().getNumeroBoleto()));
		} catch (ECivilException e) {
			Mensagem.errorSemBundle("Não foi possível visualizar o boleto nesse momento. Tente novamente mais tarde. " + e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível visualizar o boleto nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public boolean existeSaldoDiponivel() {
		return isConsultaSaldoRealizada() && getTotalDisponivelSaque().compareTo(new BigDecimal(0)) >= 1;
	}
	
	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public BigDecimal getValorCredito() {
		return valorCredito;
	}

	public void setValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}

	public BigDecimal getTotalDisponivelSaque() {
		return totalDisponivelSaque;
	}

	public void setTotalDisponivelSaque(BigDecimal totalDisponivelSaque) {
		this.totalDisponivelSaque = totalDisponivelSaque;
	}

	public CartorioResponse getCartorio() {
		return cartorio;
	}

	public void setCartorio(CartorioResponse cartorio) {
		this.cartorio = cartorio;
	}
	
	public List<SolicitacaoCredito> getListaSolicitacaoCredito() {
		return listaSolicitacaoCredito;
	}

	public void setListaSolicitacaoCredito(List<SolicitacaoCredito> listaSolicitacaoCredito) {
		this.listaSolicitacaoCredito = listaSolicitacaoCredito;
	}
	
	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public boolean isConsultaSaldoRealizada() {
		return consultaSaldoRealizada;
	}

	public void setConsultaSaldoRealizada(boolean consultaSaldoRealizada) {
		this.consultaSaldoRealizada = consultaSaldoRealizada;
	}

}
