package ecivil.adm.controller.contadigital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.ContaAlternativaBO;
import ecivil.ejb.entidade.ContaAlternativa;
import ecivil.ejb.enumeration.EnumSituacaoSolicitacaoSaque;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.SolicitacaoSaqueExcelUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.SolicitacaoSaqueVO;
import ecivil.ejb.ws.cliente.rest.request.contadigital.AlterarSaqueProvisionadoRequest;
import ecivil.ejb.ws.cliente.rest.request.contadigital.FiltroSaqueProvisionadoRequest;
import ecivil.ejb.ws.cliente.rest.response.contadigital.SaqueProvisionadoAgrupadoResponse;
import ecivil.ejb.ws.cliente.rest.response.contadigital.SaqueProvisionadoDetalhadoResponse;
import ecivil.ejb.ws.cliente.rest.response.contadigital.SaqueProvisionadoResponse;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.Paginacao;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class ConfirmarSaqueController extends BaseController implements Serializable {

	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private ContaAlternativaBO contaAlternativaBO;

	private List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO;
	private SolicitacaoSaqueVO solicitacaoSaqueVO;
	private FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest;
	
	private String codigoCorregedoria;
	private Date dataInicioSolicitacao;
	private Date dataFimSolicitacao;
	private String cpfCnpjOficial;
	private String codigoSituacaoSolicitacaoSaque;
	
	private boolean exibirDadosDetalhados;
	private boolean marcaTodos;
	private Paginacao paginacao;
	
	@PostConstruct
	public void inicializa() {
		try {
			super.validaServentiaSelecionada();
			setPaginacao(new Paginacao());
			setCodigoSituacaoSolicitacaoSaque(EnumSituacaoSolicitacaoSaque.EM_ABERTO.getCodigo());
			pesquisaSolicitacaoSaque();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pesquisaSolicitacaoSaque() {
		try {
			setSolicitacaoSaqueVO(null);
			setListaSolicitacaoSaqueVO(new ArrayList<>());
			getPaginacao().limpaPaginacao();
			validaCamposFiltros();
			setFiltroSaqueProvisionadoRequest(montaObjetoRequestFiltroSaqueProvisionado());
			pesquisaSolicitacaoSaquePaginado();
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar as solicitações de saque nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void pesquisaSolicitacaoSaquePaginado() {
		try {
			setaPaginacaoFiltroSaqueProvisionado();
			SaqueProvisionadoResponse response = recuperaSolicitacoesSaque();
			getPaginacao().setTotalRegistros(response.getTotalRegistros());
			setListaSolicitacaoSaqueVO(converteRespostaSolicitacaoSaque(response));
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar as solicitações de saque nesse momento. Tente novamente mais tarde.");
		}
	}

	private SaqueProvisionadoResponse recuperaSolicitacoesSaque() throws ECivilException {
		if (isExibirDadosDetalhados()) {
			return contaDigitalWS.consultarSaqueProvisonadoDetalhado(getFiltroSaqueProvisionadoRequest());
		}
		return contaDigitalWS.consultarSaqueProvisonadoAgrupado(getFiltroSaqueProvisionadoRequest());
	}
	
	private List<SolicitacaoSaqueVO> converteRespostaSolicitacaoSaque(SaqueProvisionadoResponse response) throws ECivilException {
		if (response == null || response.getTotalRegistros() == null || response.getTotalRegistros() == 0) {
			return null;
		}
		
		if (isExibirDadosDetalhados()) {
			return montaListaSolitacaSaqueDetalhado(response.getSaqueProvisionadoDetalhadoResponse());
		} else {
			return montaListaSolitacaSaqueAgrupado(response.getSaqueProvisionadoAgrupadoResponse());
		}
	}

	private List<SolicitacaoSaqueVO> montaListaSolitacaSaqueDetalhado(List<SaqueProvisionadoDetalhadoResponse> saqueProvisionadoDetalhado) throws ECivilException {
		List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO = new ArrayList<>();
		for (SaqueProvisionadoDetalhadoResponse response : saqueProvisionadoDetalhado) {
			SolicitacaoSaqueVO solicitacaoSaqueVO = new SolicitacaoSaqueVO();
			solicitacaoSaqueVO.setCodigoCorregedoria(response.getCodigoCorregedoria());
			solicitacaoSaqueVO.setDataSolicitacao(response.getDataSolicitacao());
			solicitacaoSaqueVO.setValorSolicitacao(response.getSaqueProvisionado());
			solicitacaoSaqueVO.setIdSaqueProvisionado(response.getIdSaqueProvisionado());
			setaDadosCartorioRecompe(solicitacaoSaqueVO);
			listaSolicitacaoSaqueVO.add(solicitacaoSaqueVO);
		}
		return listaSolicitacaoSaqueVO;
	}
	
	private List<SolicitacaoSaqueVO> montaListaSolitacaSaqueAgrupado(List<SaqueProvisionadoAgrupadoResponse> saqueProvisionadoAgrupado) throws ECivilException {
		List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO = new ArrayList<>();
		for (SaqueProvisionadoAgrupadoResponse response : saqueProvisionadoAgrupado) {
			SolicitacaoSaqueVO solicitacaoSaqueVO = new SolicitacaoSaqueVO();
			solicitacaoSaqueVO.setCodigoCorregedoria(response.getCodigoCorregedoria());
			solicitacaoSaqueVO.setValorSolicitacao(response.getTotalSaqueProvisionado());
			solicitacaoSaqueVO.setIdSaqueProvisionado(response.getIdSaqueProvisionado());
			setaDadosCartorioRecompe(solicitacaoSaqueVO);
			listaSolicitacaoSaqueVO.add(solicitacaoSaqueVO);
		}
		return listaSolicitacaoSaqueVO;
	}

	private void setaDadosCartorioRecompe(SolicitacaoSaqueVO solicitacaoSaqueVO) throws ECivilException {
		CartorioResponse cartorioRecompe = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(solicitacaoSaqueVO.getCodigoCorregedoria());
		
		if (cartorioRecompe == null) {
			return;
		}
		
		solicitacaoSaqueVO.setNomeDistritoECartorio(cartorioRecompe.getNomeDistritoECartorio());
		solicitacaoSaqueVO.setNomeCartorio(cartorioRecompe.getNomeCartorio());
		solicitacaoSaqueVO.setDistrito(cartorioRecompe.getDistrito());
		solicitacaoSaqueVO.setMunicipio(cartorioRecompe.getNomeMunicipio());
		solicitacaoSaqueVO.setTelefone(cartorioRecompe.getTelefone());
		solicitacaoSaqueVO.setEmail(cartorioRecompe.getEmail());
		setaDadosBancariosCartorio(solicitacaoSaqueVO, cartorioRecompe);
	}
	
	private void setaDadosBancariosCartorio(SolicitacaoSaqueVO solicitacaoSaqueVO, CartorioResponse cartorioRecompe) {
		ContaAlternativa contaAlternativa = contaAlternativaBO.recuperaContaAlternativaPorCodCorregedoria(solicitacaoSaqueVO.getCodigoCorregedoria());
		
		if (contaAlternativa == null) {
			setaDadosBancariosCartorioRecompe(solicitacaoSaqueVO, cartorioRecompe);
		} else {
			setaDadosBancariosCartorioContaAlternativa(solicitacaoSaqueVO, contaAlternativa);
		}
	}

	private void setaDadosBancariosCartorioRecompe(SolicitacaoSaqueVO solicitacaoSaqueVO, CartorioResponse cartorioRecompe) {
		solicitacaoSaqueVO.setNomeTitularConta(cartorioRecompe.getNomeTitularConta());
		solicitacaoSaqueVO.setCpfCnpjTitularConta(cartorioRecompe.getCpfCnpjTitularConta());
		solicitacaoSaqueVO.setCodigoBanco(cartorioRecompe.getCodigoBanco());
		solicitacaoSaqueVO.setNomeBanco(cartorioRecompe.getNomeBanco());
		solicitacaoSaqueVO.setAgencia(cartorioRecompe.getAgencia());
		solicitacaoSaqueVO.setConta(cartorioRecompe.getConta());
		solicitacaoSaqueVO.setTipoConta(cartorioRecompe.getTipoConta());			
	}
	
	private void setaDadosBancariosCartorioContaAlternativa(SolicitacaoSaqueVO solicitacaoSaqueVO, ContaAlternativa contaAlternativa) {
		solicitacaoSaqueVO.setNomeTitularConta(contaAlternativa.getNomeTitular());
		solicitacaoSaqueVO.setCpfCnpjTitularConta(contaAlternativa.getCpfCnpjTitular());
		solicitacaoSaqueVO.setCodigoBanco(contaAlternativa.getCodigoBanco());
		solicitacaoSaqueVO.setNomeBanco(contaAlternativa.getNomeBanco());
		solicitacaoSaqueVO.setAgencia(contaAlternativa.getAgencia());
		solicitacaoSaqueVO.setConta(contaAlternativa.getConta());
		solicitacaoSaqueVO.setTipoConta(contaAlternativa.getTipoConta().name());		
	}
	
	private void validaCamposFiltros() throws ECivilException {
		if (!Util.ehStringValida(getCpfCnpjOficial())) {
			return;
		}
		
		if (!Util.isValidCPF(getCpfCnpjOficial()) && !Util.isValidCNPJ(getCpfCnpjOficial())) {
			throw new ECivilException("O CPF/CNPJ informado é inválido.");
		}
	}

	private FiltroSaqueProvisionadoRequest montaObjetoRequestFiltroSaqueProvisionado() {
		FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest = new FiltroSaqueProvisionadoRequest();
		filtroSaqueProvisionadoRequest.setCodigoCorregedoria(getCodigoCorregedoria());
		filtroSaqueProvisionadoRequest.setDataInicio(DataUtil.converteDateParaString_DDMMYYY_BARRA(getDataInicioSolicitacao()));
		filtroSaqueProvisionadoRequest.setDataFim(DataUtil.converteDateParaString_DDMMYYY_BARRA(getDataFimSolicitacao()));
		filtroSaqueProvisionadoRequest.setCpfCnpjOficial(getCpfCnpjOficial());
		filtroSaqueProvisionadoRequest.setCodSituacao(getCodigoSituacaoSolicitacaoSaque());
		return filtroSaqueProvisionadoRequest;
	}
	
	private void setaPaginacaoFiltroSaqueProvisionado() {
		getFiltroSaqueProvisionadoRequest().setPrimeiroRegistro(getPaginacao().getPrimeiroRegistro());
		getFiltroSaqueProvisionadoRequest().setRegistrosPorPagina(getPaginacao().getRegistrosPorPagina());
	}
	
	public void marcaTodosRegistrosSaqueConfirmado() {
		setaFiltrosResultadoConformePesquisa();
		if (getListaSolicitacaoSaqueVO() == null || getListaSolicitacaoSaqueVO().isEmpty()) {
			return;
		}
		getListaSolicitacaoSaqueVO().stream().forEach(solicitacao -> solicitacao.setSaqueConfirmado(isMarcaTodos()));
	}
	
	public void confirmarRealizacaoSaque() {
		if (getFiltroSaqueProvisionadoRequest().getCodSituacao().equals(EnumSituacaoSolicitacaoSaque.EM_ABERTO.getCodigo())) {
			confirmarLancamentoTransferencia();
		} else if (getFiltroSaqueProvisionadoRequest().getCodSituacao().equals(EnumSituacaoSolicitacaoSaque.TRANSFERENCIA_LANCADA.getCodigo())) {
			confirmarTransferencia();
		} else {
			Mensagem.errorSemBundle("Não foi possível realizar a confirmação de transferência nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void confirmarLancamentoTransferencia() {
		try {
			if (!exibeBtnConfirmarSolicitacaoSaque()) {
				return;
			}
			contaDigitalWS.alteraSituacaoTransferenciaLancada(montaObjetoAlterarSaqueProvisionadoRequest());
			setaFiltrosResultadoConformePesquisa();
			pesquisaSolicitacaoSaque();
			setMarcaTodos(Boolean.FALSE);
			Mensagem.infoSemBundle("Solicitações de saque lançadas com sucesso!");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível confirmar o lançamento da(s) transfência(s) nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void confirmarTransferencia() {
		try {
			if (!exibeBtnConfirmarSolicitacaoSaque()) {
				return;
			}
			contaDigitalWS.alteraSituacaoTransferenciaConfirmada(montaObjetoAlterarSaqueProvisionadoRequest());
			setaFiltrosResultadoConformePesquisa();
			pesquisaSolicitacaoSaque();
			setMarcaTodos(Boolean.FALSE);
			Mensagem.infoSemBundle("Solicitações de saque confirmadas com sucesso!");
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível confirmar a realização da(s) transfência(s) nesse momento. Tente novamente mais tarde.");
		}
	}
	
	private AlterarSaqueProvisionadoRequest montaObjetoAlterarSaqueProvisionadoRequest() {
		String listaIdSaqueProvisionado = getListaSolicitacaoSaqueVO().stream().filter(SolicitacaoSaqueVO::isSaqueConfirmado).map(SolicitacaoSaqueVO::getIdSaqueProvisionado).collect(Collectors.joining(";"));
		AlterarSaqueProvisionadoRequest aterarSaqueProvisionadoRequest = new AlterarSaqueProvisionadoRequest();
		aterarSaqueProvisionadoRequest.setIdsSaqueProvisionado(listaIdSaqueProvisionado);
		return aterarSaqueProvisionadoRequest;
	}

	public void verificaSelecaoConfirmarSaque(String idModal) {
		setaFiltrosResultadoConformePesquisa();

		if (!exibeBtnConfirmarSolicitacaoSaque()) {
			return;
		}
		
		SolicitacaoSaqueVO solicitacaoSaqueVO = getListaSolicitacaoSaqueVO().stream().filter(SolicitacaoSaqueVO::isSaqueConfirmado).findFirst().orElse(null);
		if (solicitacaoSaqueVO == null) {
			Mensagem.errorSemBundle("Favor selecionar os registros que deseja confirmar o saque.");
		} else {
			abrirModal(idModal);
		}
	}
	
	public void marcaSaqueConfirmadoSolicitacaoSaqueModal(String idModal) {
		if (getSolicitacaoSaqueVO() == null) {
			return;
		}
		getSolicitacaoSaqueVO().setSaqueConfirmado(Boolean.TRUE);
		abrirModal(idModal);
	}
	
	public List<SolicitacaoSaqueVO> recuperaListaSaqueConfirmado() {
		if (!exibeBtnConfirmarSolicitacaoSaque()) {
			return null;
		}
		return getListaSolicitacaoSaqueVO().stream().filter(SolicitacaoSaqueVO::isSaqueConfirmado).collect(Collectors.toList());
	}

	public List<CartorioResponse> recuperaListaCartorio() {
		try {
			return cartorioLookUp.recuperaListaCartorio();
		} catch (ECivilException e) {
			Mensagem.errorSemBundle("Não foi possível recuperar os Cartórios nesse momento. Tente novamente mais tarde.");
			return null;
		}
	}
	
	public EnumSituacaoSolicitacaoSaque[] recuperaListaSituacaoSolicSaque() {
		return EnumSituacaoSolicitacaoSaque.values();
	}
	
	public void gerarExcel() {
		try {
			List<SolicitacaoSaqueVO> listaSolicitacaoSaqueExcel = recuperaListaSolicitacaoSaqueExcel();
			if (listaSolicitacaoSaqueExcel == null) {
				Mensagem.errorSemBundle("Não foram encontrado resultados com os filtros informados.");
				return;
			}
			SolicitacaoSaqueExcelUtil.gerarPlanilhaExcel(listaSolicitacaoSaqueExcel, getFiltroSaqueProvisionadoRequest());
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível gerar o Excel das solicitações de saque nesse momento.");
		}
	}
	
	private List<SolicitacaoSaqueVO> recuperaListaSolicitacaoSaqueExcel() throws ECivilException {
		SaqueProvisionadoResponse saqueProvisionadoResponse = null;

		if (isExibirDadosDetalhados()) {
			saqueProvisionadoResponse = contaDigitalWS.consultarSaqueProvisonadoDetalhado(montaObjetoRequestFiltroSaqueProvisionadoGerarExcel());
		} else {
			saqueProvisionadoResponse = contaDigitalWS.consultarSaqueProvisonadoAgrupado(montaObjetoRequestFiltroSaqueProvisionadoGerarExcel());
		}
		
		return converteRespostaSolicitacaoSaque(saqueProvisionadoResponse);
	}

	private FiltroSaqueProvisionadoRequest montaObjetoRequestFiltroSaqueProvisionadoGerarExcel() {
		FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequestExcel = new FiltroSaqueProvisionadoRequest();
		filtroSaqueProvisionadoRequestExcel.setCodigoCorregedoria(getFiltroSaqueProvisionadoRequest().getCodigoCorregedoria());
		filtroSaqueProvisionadoRequestExcel.setDataInicio(getFiltroSaqueProvisionadoRequest().getDataInicio());
		filtroSaqueProvisionadoRequestExcel.setDataFim(getFiltroSaqueProvisionadoRequest().getDataFim());
		filtroSaqueProvisionadoRequestExcel.setCpfCnpjOficial(getFiltroSaqueProvisionadoRequest().getCpfCnpjOficial());
		filtroSaqueProvisionadoRequestExcel.setCodSituacao(getFiltroSaqueProvisionadoRequest().getCodSituacao());
		filtroSaqueProvisionadoRequestExcel.setPrimeiroRegistro(0);
		filtroSaqueProvisionadoRequestExcel.setRegistrosPorPagina(getPaginacao().getTotalRegistros());
		return filtroSaqueProvisionadoRequestExcel;
	}
	
	public void setaSolicitacaoSaqueModal(SolicitacaoSaqueVO solicitacaoSaqueVO) {
		setaFiltrosResultadoConformePesquisa();
		setSolicitacaoSaqueVO(solicitacaoSaqueVO);
	}
	
	public void setaFiltrosResultadoConformePesquisa() {
		setCodigoCorregedoria(getFiltroSaqueProvisionadoRequest().getCodigoCorregedoria());
		setDataInicioSolicitacao(DataUtil.converteDataStringParaDate(getFiltroSaqueProvisionadoRequest().getDataInicio()));
		setDataFimSolicitacao(DataUtil.converteDataStringParaDate(getFiltroSaqueProvisionadoRequest().getDataFim()));
		setCpfCnpjOficial(getFiltroSaqueProvisionadoRequest().getCpfCnpjOficial());
		setCodigoSituacaoSolicitacaoSaque(getFiltroSaqueProvisionadoRequest().getCodSituacao());
	}
	
	public boolean existeRegistrosEncontrados() {
		return getListaSolicitacaoSaqueVO() != null && !getListaSolicitacaoSaqueVO().isEmpty();
	}
	
	public boolean exibeBtnConfirmarSolicitacaoSaque() {
		return getListaSolicitacaoSaqueVO() != null && !getListaSolicitacaoSaqueVO().isEmpty() && 
				(ehSituacaoSolicitacaoSaqueEmAberto() || ehSituacaoSolicitacaoSaqueTransferenciaLancada());
	}
	
	public boolean ehSituacaoSolicitacaoSaqueEmAberto() {
		return EnumSituacaoSolicitacaoSaque.EM_ABERTO.getCodigo().equals(getFiltroSaqueProvisionadoRequest().getCodSituacao());
	}
	
	public boolean ehSituacaoSolicitacaoSaqueTransferenciaLancada() {
		return EnumSituacaoSolicitacaoSaque.TRANSFERENCIA_LANCADA.getCodigo().equals(getFiltroSaqueProvisionadoRequest().getCodSituacao());
	}
	
	public void limpar() {
		setListaSolicitacaoSaqueVO(new ArrayList<>());
		setCodigoCorregedoria(null);
		setDataInicioSolicitacao(null);
		setDataFimSolicitacao(null);
		setCpfCnpjOficial(null);
		setCodigoSituacaoSolicitacaoSaque(EnumSituacaoSolicitacaoSaque.EM_ABERTO.getCodigo());
		setExibirDadosDetalhados(Boolean.FALSE);
		getPaginacao().limpaPaginacao();
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public List<SolicitacaoSaqueVO> getListaSolicitacaoSaqueVO() {
		return listaSolicitacaoSaqueVO;
	}

	public void setListaSolicitacaoSaqueVO(List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO) {
		this.listaSolicitacaoSaqueVO = listaSolicitacaoSaqueVO;
	}
	
	public SolicitacaoSaqueVO getSolicitacaoSaqueVO() {
		return solicitacaoSaqueVO;
	}

	public void setSolicitacaoSaqueVO(SolicitacaoSaqueVO solicitacaoSaqueVO) {
		this.solicitacaoSaqueVO = solicitacaoSaqueVO;
	}

	public FiltroSaqueProvisionadoRequest getFiltroSaqueProvisionadoRequest() {
		return filtroSaqueProvisionadoRequest;
	}

	public void setFiltroSaqueProvisionadoRequest(FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest) {
		this.filtroSaqueProvisionadoRequest = filtroSaqueProvisionadoRequest;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public Date getDataInicioSolicitacao() {
		return dataInicioSolicitacao;
	}

	public void setDataInicioSolicitacao(Date dataInicioSolicitacao) {
		this.dataInicioSolicitacao = dataInicioSolicitacao;
	}

	public Date getDataFimSolicitacao() {
		return dataFimSolicitacao;
	}

	public void setDataFimSolicitacao(Date dataFimSolicitacao) {
		this.dataFimSolicitacao = dataFimSolicitacao;
	}
	
	public String getCpfCnpjOficial() {
		return cpfCnpjOficial;
	}

	public void setCpfCnpjOficial(String cpfCnpjOficial) {
		this.cpfCnpjOficial = cpfCnpjOficial;
	}
	
	public String getCodigoSituacaoSolicitacaoSaque() {
		return codigoSituacaoSolicitacaoSaque;
	}

	public void setCodigoSituacaoSolicitacaoSaque(String codigoSituacaoSolicitacaoSaque) {
		this.codigoSituacaoSolicitacaoSaque = codigoSituacaoSolicitacaoSaque;
	}

	public boolean isExibirDadosDetalhados() {
		return exibirDadosDetalhados;
	}

	public void setExibirDadosDetalhados(boolean exibirDadosDetalhados) {
		this.exibirDadosDetalhados = exibirDadosDetalhados;
	}

	public boolean isMarcaTodos() {
		return marcaTodos;
	}

	public void setMarcaTodos(boolean marcaTodos) {
		this.marcaTodos = marcaTodos;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

}
