package ecivil.adm.controller.contadigital;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.ContaAlternativaBO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.ContaAlternativa;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.SolicitacaoSaqueVO;
import ecivil.ejb.ws.cliente.rest.request.contadigital.ContaDigitalRequest;
import ecivil.ejb.ws.cliente.rest.response.contadigital.SaqueProvisionadoResponse;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class SolicitarSaqueController extends BaseController implements Serializable {

	private static final Integer QTD_SAQUE_PROVISIONADO_CONSULTA = 5;
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private ContaAlternativaBO contaAlternativaBO;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private BigDecimal valorSaque;
	private BigDecimal totalDisponivelSaque;
	private boolean consultaSaldoRealizada;
	private SolicitacaoSaqueVO solicitacaoSaqueVO;
	private SaqueProvisionadoResponse saqueProvisionadoResponse;
	
	@PostConstruct
	public void inicializa() {
		try {
			super.validaServentiaSelecionada();
			recuperaSaldoDisponivel();
			recuperaUltimasSolicitacoes();
			setConsultaSaldoRealizada(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void recuperaSaldoDisponivel() throws ECivilException {
		setTotalDisponivelSaque(contaDigitalWS.consultaSaldo(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado()));
	}
	
	private void recuperaUltimasSolicitacoes() throws ECivilException {
		setSaqueProvisionadoResponse(contaDigitalWS.consultarUltimosSaqueProvisionado(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado(), QTD_SAQUE_PROVISIONADO_CONSULTA));
	}
	
	public void preparaSolicitacaoSaque(String idModal) {
		try {
			validaValorSaquePreenchido();
			recuperaDadosBancarios();
			abrirModal(idModal);
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível realizar a solicitação de saque nesse momento. Tente novamente mais tarde.");
		}
	}

	private void recuperaDadosBancarios() throws ECivilException {
		CartorioResponse cartorioRecompe = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
		if (cartorioRecompe == null) {
			throw new ECivilException("Não foi possível recuperar os dados bancários do cartório nesse momento. Tente novamente mais tarde.");
		}
		setaDadosBancariosCartorio(cartorioRecompe);
	}
	
	private void setaDadosBancariosCartorio(CartorioResponse cartorioRecompe) {
		ContaAlternativa contaAlternativa = contaAlternativaBO.recuperaContaAlternativaPorCodCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
		setSolicitacaoSaqueVO(new SolicitacaoSaqueVO());
		if (contaAlternativa == null) {
			setaDadosBancariosCartorioRecompe(cartorioRecompe);
		} else {
			setaDadosBancariosCartorioContaAlternativa(contaAlternativa);
		}
	}
	
	private void setaDadosBancariosCartorioRecompe(CartorioResponse cartorioRecompe) {
		getSolicitacaoSaqueVO().setNomeTitularConta(cartorioRecompe.getNomeTitularConta());
		getSolicitacaoSaqueVO().setCpfCnpjTitularConta(cartorioRecompe.getCpfCnpjTitularConta());
		getSolicitacaoSaqueVO().setCodigoBanco(cartorioRecompe.getCodigoBanco());
		getSolicitacaoSaqueVO().setNomeBanco(cartorioRecompe.getNomeBanco());
		getSolicitacaoSaqueVO().setAgencia(cartorioRecompe.getAgencia());
		getSolicitacaoSaqueVO().setConta(cartorioRecompe.getConta());
		getSolicitacaoSaqueVO().setTipoConta(cartorioRecompe.getTipoConta());			
	}
	
	private void setaDadosBancariosCartorioContaAlternativa(ContaAlternativa contaAlternativa) {
		getSolicitacaoSaqueVO().setNomeTitularConta(contaAlternativa.getNomeTitular());
		getSolicitacaoSaqueVO().setCpfCnpjTitularConta(contaAlternativa.getCpfCnpjTitular());
		getSolicitacaoSaqueVO().setCodigoBanco(contaAlternativa.getCodigoBanco());
		getSolicitacaoSaqueVO().setNomeBanco(contaAlternativa.getNomeBanco());
		getSolicitacaoSaqueVO().setAgencia(contaAlternativa.getAgencia());
		getSolicitacaoSaqueVO().setConta(contaAlternativa.getConta());
		getSolicitacaoSaqueVO().setTipoConta(contaAlternativa.getTipoConta().name());		
	}

	public void solicitarSaque() {
		try {
			validaValorSaquePreenchido();
			contaDigitalWS.adicionarSaqueProvisionado(montaObjetoContaDigitalRequest());
			setValorSaque(null);
			Mensagem.infoSemBundle("Solicitação de saque realizada com sucesso");
			recuperaSaldoDisponivel();
			recuperaUltimasSolicitacoes();
		} catch (ECivilException e) {
			if (Util.ehStringValida(e.getMensagemErro())) {
				Mensagem.errorSemBundle(e.getMensagemErro());
			} else {
				Mensagem.errorSemBundle("Não foi possível realizar a solicitação de saque nesse momento. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível realizar a solicitação de saque nesse momento. Tente novamente mais tarde.");
		}
		fechaModalStatusDialog();
	}
	
	private ContaDigitalRequest montaObjetoContaDigitalRequest() {
		ContaDigitalRequest contaDigitalRequest = new ContaDigitalRequest();
		contaDigitalRequest.setCodigoCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
		contaDigitalRequest.setValor(getValorSaque());
		contaDigitalRequest.setCpfCnpjOperacao(getUsuarioLogadoPortal().getCpf());
		return contaDigitalRequest;
	}
	
	private void validaValorSaquePreenchido() throws ECivilException {
		if (getValorSaque() == null || getValorSaque().compareTo(new BigDecimal(0)) <= 0) {
			throw new ECivilException("Favor preencher o valor desejado para realização de saque.");
		} 
		
		BigDecimal valorSaqueMinimo = new BigDecimal(parametroDAO.buscaValorParametro(Parametro.VALOR_MINIMO_SAQUE));
		if (getValorSaque().compareTo(valorSaqueMinimo) < 0) {
			throw new ECivilException("O valor minimo para solicitação de saque é de: R$ " + valorSaqueMinimo);
		}		
	}

	public boolean desabilitaBtnSolicitarSaque() {
		return getTotalDisponivelSaque() == null || getTotalDisponivelSaque().compareTo(new BigDecimal(0)) <= 0;
	}
	
	public boolean existeSaldoDiponivel() {
		return isConsultaSaldoRealizada() && getTotalDisponivelSaque().compareTo(new BigDecimal(0)) >= 1;
	}
	
	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public BigDecimal getValorSaque() {
		return valorSaque;
	}

	public void setValorSaque(BigDecimal valorSaque) {
		this.valorSaque = valorSaque;
	}

	public BigDecimal getTotalDisponivelSaque() {
		return totalDisponivelSaque;
	}

	public void setTotalDisponivelSaque(BigDecimal totalDisponivelSaque) {
		this.totalDisponivelSaque = totalDisponivelSaque;
	}

	public boolean isConsultaSaldoRealizada() {
		return consultaSaldoRealizada;
	}

	public void setConsultaSaldoRealizada(boolean consultaSaldoRealizada) {
		this.consultaSaldoRealizada = consultaSaldoRealizada;
	}

	public SolicitacaoSaqueVO getSolicitacaoSaqueVO() {
		return solicitacaoSaqueVO;
	}

	public void setSolicitacaoSaqueVO(SolicitacaoSaqueVO solicitacaoSaqueVO) {
		this.solicitacaoSaqueVO = solicitacaoSaqueVO;
	}

	public SaqueProvisionadoResponse getSaqueProvisionadoResponse() {
		return saqueProvisionadoResponse;
	}

	public void setSaqueProvisionadoResponse(SaqueProvisionadoResponse saqueProvisionadoResponse) {
		this.saqueProvisionadoResponse = saqueProvisionadoResponse;
	}
	
}
