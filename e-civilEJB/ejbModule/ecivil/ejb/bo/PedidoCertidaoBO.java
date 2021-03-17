package ecivil.ejb.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.certificacao.digital.AssinaturaDigitalPDF;
import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.HistoricoPedidoCertidaoDAO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.dao.PedidoCertidaoDAO;
import ecivil.ejb.entidade.AliquotaISSQN;
import ecivil.ejb.entidade.AnexoCertidao;
import ecivil.ejb.entidade.HistoricoPedidoCertidao;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.SituacaoPedidoCertidao;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.entidade.VinculoBoleto;
import ecivil.ejb.enumeration.EnumCodigoAto;
import ecivil.ejb.enumeration.EnumMotivoRejeicao;
import ecivil.ejb.enumeration.EnumSimNao;
import ecivil.ejb.enumeration.EnumSituacaoSolicitacaoEstorno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.imagem.ImagemViaUnica;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.StringUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.FiltroPesquisaPedidoCertidaoVO;
import ecivil.ejb.ws.cliente.rest.ContaDigitalWS;
import ecivil.ejb.ws.cliente.rest.IntegradorCRCWS;
import ecivil.ejb.ws.cliente.rest.RecompeWS;
import ecivil.ejb.ws.cliente.rest.request.contadigital.ContaDigitalRequest;
import ecivil.ejb.ws.cliente.rest.request.recompe.AtoResponseVo;
import ecivil.ejb.ws.cliente.rest.request.recompe.RegistroBoletoRequestVo;
import ecivil.ejb.ws.cliente.rest.response.integradorcrc.DadosCertidaoResponse;
import ecivil.ejb.ws.cliente.rest.response.recompe.RegistroBoletoResponseVo;
import web.util.Paginacao;

@Stateless
public class PedidoCertidaoBO {

	@EJB
	private PedidoCertidaoDAO pedidoCertidaoDAO;
	
	@EJB
	private HistoricoPedidoCertidaoDAO historicoPedidoCertidaoDAO;

	@EJB
	private SituacaoPedidoCertidaoBO situacaoPedidoCertidaoBO;
	
	@EJB
	private HistoricoPedidoCertidaoBO historicoPedidoCertidaoBO;
	
	@EJB
	private ValoresPedidoCertidaoBO valoresPedidoCertidaoBO;
	
	@EJB
	private TipoCertidaoBO tipoCertidaoBO;

	@EJB
	private UsuarioPortalExternoBO usuarioPortalExternoBO;
	
	@EJB
	private AliquotaISSQNBO aliquotaISSQNBO;
	
	@EJB
	private CertidaoNegativaBO certidaoNegativaBO;
	
	@EJB
	private DataDAO dataDAO;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private EmailBO emailBO;

	@EJB
	private IntegradorCRCWS integradorCRCWS;
	
	@EJB
	private RecompeWS recompeWS;
	
	@EJB
	private ContaDigitalWS contaDigitalWS;

	private void setaSituacaoPedidoCertidao(PedidoCertidao pedidoCertidao, String codSituacaoPedidoCertidao, UsuarioPortalInterno usuario) {
		pedidoCertidao.setSituacao(situacaoPedidoCertidaoBO.recuperaSituacaoPedidoCertidaoPorCod(codSituacaoPedidoCertidao));
		historicoPedidoCertidaoBO.setaHistoricoPedidoCertidao(pedidoCertidao, usuario);
	}
	
	public void atualizarDataHoraUltimaVisualizacaoPedido(PedidoCertidao pedidoCertidao, String quem) {
		if (pedidoCertidao.getListaHistoricoPedidoCertidaoInicializada().size() > 0) {
			Date dataAtual = dataDAO.retornaDataBanco();
			
			for (HistoricoPedidoCertidao historicoPedidoCertidao : pedidoCertidao.getListaHistoricoPedidoCertidaoInicializada()) {
				if ("REQUISITADO".equals(quem) && historicoPedidoCertidao.getDataHoraVisualizacaoRequisitado() == null) {
					historicoPedidoCertidao.setDataHoraVisualizacaoRequisitado(dataAtual);
					historicoPedidoCertidaoDAO.atualiza(historicoPedidoCertidao);
				} else if ("REQUISITANTE".equals(quem) && historicoPedidoCertidao.getDataHoraVisualizacaoRequisitante() == null) {
					historicoPedidoCertidao.setDataHoraVisualizacaoRequisitante(dataAtual);
					historicoPedidoCertidaoDAO.atualiza(historicoPedidoCertidao);
				} 
			}
			
			//pedidoCertidaoDAO.atualiza(pedidoCertidao);
		}
		//return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public synchronized String geraProtocolo() {
		StringBuilder protocolo = new StringBuilder();
		protocolo.append("CER");
		protocolo.append(String.valueOf(LocalDate.now().getYear()).substring(2, 4));
		protocolo.append(StringUtil.completaComZeroAEsquerda(pedidoCertidaoDAO.recuperaSequencialProtocoloCertidao(), 8));
		return protocolo.toString();
	}

	public void validaPedidoFormularioCertidao(PedidoCertidao pedidoCertidao) throws ECivilException {
		ECivilException listaeCivilException = new ECivilException();
		listaeCivilException.instanciaListaErros();
		validaCamposFormularioPedidoCertidao(listaeCivilException, pedidoCertidao);
		if (listaeCivilException.possuiListaErros()) {
			throw listaeCivilException;
		}
	}
	
	private void validaCamposFormularioPedidoCertidao(ECivilException listaeCivilException, PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.getCodCorregedoriaRequisitante() != null && pedidoCertidao.getCodCorregedoriaRequisitante().equals(pedidoCertidao.getCodCorregedoriaRequisitada())) {
			listaeCivilException.adicionaErroNaLista("Não é possível solicitar essa certidão pois a serventia dessa certidão é a mesma serventia do usuário logado no sistema.");
			return;
		}
		
		if (pedidoCertidao.getMunicipio() == null) {
			listaeCivilException.adicionaErroNaLista("Favor selecionar o Município da sua solicitação.");
		}
		
		if (!Util.ehStringValida(pedidoCertidao.getCodCorregedoriaRequisitada())) {
			listaeCivilException.adicionaErroNaLista("Favor selecionar o Cartório da sua solicitação.");
		}
		
		if (pedidoCertidao.getTipoCertidao() == null) {
			listaeCivilException.adicionaErroNaLista("Favor selecionar o Tipo de Cerdidão da sua solicitação.");
		}
		
		if (!Util.ehStringValida(pedidoCertidao.getNomePrimeiraPessoa()) || pedidoCertidao.getNomePrimeiraPessoa().split(" ").length <= 1) {
			listaeCivilException.adicionaErroNaLista("Favor informar o nome completo da pessoa/cônjuge 1.");
		}
		
		if (pedidoCertidao.getDataFato() == null) {
			listaeCivilException.adicionaErroNaLista("Favor informar a data do Casamento/Nascimento/Óbito");
		}
		
		if (pedidoCertidao.isSaidaViaCorreios() && pedidoCertidao.getEnderecoUsuarioExterno() == null) {
			listaeCivilException.adicionaErroNaLista("Favor informar o endereço de entrega da Certidão.");
		}
	}

	public void salvarSolicitacaoCertidaoNegativa(PedidoCertidao pedidoCertidao) throws ECivilException {
		if (pedidoCertidao.isSaidaViaCorreios()) {
			setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_EM_ANALISE, null);
		} else {
			informaValorCertidao(pedidoCertidao);
			setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_AGUARDANDO_PAGAMENTO, null);
		}
		// TODO:
		/*
		 atualizarDataHoraUltimaVisualizacaoPedido(pedidoCertidao, "REQUISITADO");
		 ou
		 atualizarDataHoraUltimaVisualizacaoPedido(pedidoCertidao, "REQUISITANTE");
		 
		 obs: olhar em ecivil.web.controller.ConsultarCertidaoController.solicitarCertidaoNegativa()
		*/
		
		pedidoCertidao.setCertidaoNegativa(certidaoNegativaBO.montaObjetoCertidaoNegativa(pedidoCertidao, null));
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
	}
	
	public void validaPedidoCartorioParaCartorio(PedidoCertidao pedidoCertidao, DadosCertidaoResponse certidao) throws ECivilException {
		if (!Util.ehStringValida(pedidoCertidao.getCodCorregedoriaRequisitante()) || certidao == null) {
			return;
		}
		
		if (pedidoCertidao.getCodCorregedoriaRequisitante().equals(certidao.getCodCorregedoria())) {
			throw new ECivilException("Não é possível solicitar essa certidão pois a serventia dessa certidão é a mesma serventia do usuário logado no sistema.");
		}
	}

	public void salvarNovoPedidoCertidao(PedidoCertidao pedidoCertidao, DadosCertidaoResponse certidao, EnumSimNao pedidoViaFormulario) throws ECivilException {
		validaEnderecoEntregaCertidao(pedidoCertidao);
		setaCamposNovoPedidoCertidao(pedidoCertidao, certidao, pedidoViaFormulario);
		pedidoCertidaoDAO.persiste(pedidoCertidao);
		enviaEmailNovoPedidoCetidao(pedidoCertidao);
	}

	private void enviaEmailNovoPedidoCetidao(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.getUsuarioExterno() == null) {
			return;
		}
		emailBO.enviaEmailNovoPedidoCertidao(pedidoCertidao.getUsuarioExterno().getEmail(), pedidoCertidao.getUsuarioExterno().getNome(), pedidoCertidao.getProtocolo());
	}

	private void setaCamposNovoPedidoCertidao(PedidoCertidao pedidoCertidao, DadosCertidaoResponse certidao, EnumSimNao pedidoViaFormulario) {
		atriubuiSituacaoInicialPedidoCertidao(pedidoCertidao);
		pedidoCertidao.setProtocolo(geraProtocolo());
		pedidoCertidao.setDataPedido(dataDAO.retornaDataBanco());
		pedidoCertidao.setDataAlteracao(pedidoCertidao.getDataPedido());
		pedidoCertidao.setPedidoViaFormulario(pedidoViaFormulario.isSim() ? Boolean.TRUE : Boolean.FALSE);
		
		if (certidao != null) {
			pedidoCertidao.setNomePrimeiraPessoa(certidao.getPessoaA());
			pedidoCertidao.setNomeSegundaPessoa(certidao.getPessoaB());
			pedidoCertidao.setCodCorregedoriaRequisitada(certidao.getCodCorregedoria());
			pedidoCertidao.setDataRegistro(DataUtil.converteDataStringParaDate(certidao.getDataRegistro()));
			pedidoCertidao.setDataFato(DataUtil.converteDataStringParaDate(certidao.getDataFato()));
			pedidoCertidao.setTipoCertidao(tipoCertidaoBO.recuperaTipoCertidaoPorTipoFatoCRC(certidao));
			pedidoCertidao.setIdFatoCrc(certidao.getIdRegistro() != null ? Long.valueOf(certidao.getIdRegistro()) : null);
		}
	}
	
	private void atriubuiSituacaoInicialPedidoCertidao(PedidoCertidao pedidoCertidao) {
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_EM_ANALISE, null);
	}

	private void validaEnderecoEntregaCertidao(PedidoCertidao pedidoCertidao) throws ECivilException {
		if (pedidoCertidao.isSaidaViaCorreios() && pedidoCertidao.getEnderecoUsuarioExterno() == null) {
			throw new ECivilException("É obrigatório selecionar o endereço de entrega.");
		}
	}

	public void buscarCertidaoIndexada(PedidoCertidao pedidoCertidao, String nomePessoa, String nomeAssociado, Municipio municipioPesquisa, String cpf, boolean nomeIdentico, Integer primeiroRegistro) throws ECivilException {
		try {
			pedidoCertidao.setResultadoBuscaCertidao(integradorCRCWS.buscarCertidaoIndexada(nomePessoa, nomeAssociado, municipioPesquisa, cpf, nomeIdentico,  primeiroRegistro));
		} catch (ECivilException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ECivilException("Não foi possível realizar a busca da certidão nesse momento. Tente novamente mais tarde.");
		}
	}

	public int recuperaTotalPedidoCertidaoUsuario(UsuarioPortalExterno usuarioExterno) {
		return pedidoCertidaoDAO.recuperaTotalPedidoCertidaoUsuario(usuarioExterno);
	}
	
	public List<PedidoCertidao> recuperaPedidoCertidaoUsuario(UsuarioPortalExterno usuarioExterno, Paginacao paginacao) {
		return pedidoCertidaoDAO.recuperaPedidoCertidaoUsuario(usuarioExterno, paginacao);
	}

	public int pesquisarTotalPedidosCertidao(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		return pedidoCertidaoDAO.pesquisarTotalPedidosCertidao(filtroPesquisaCertidao);
	}
	
	public int pesquisarTotalSolicitacoesUI(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		return pedidoCertidaoDAO.pesquisarTotalSolicitacoesUI(filtroPesquisaCertidao);
	}

	public List<PedidoCertidao> pesquisarPedidosCertidao(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao, Paginacao paginacao) {
		return pedidoCertidaoDAO.pesquisarPedidosCertidao(filtroPesquisaCertidao, paginacao);
	}
	
	public int pesquisarTotalPedidosRealizadoPeloCartorio(UsuarioPortalInterno usuarioLogadoPortal, FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		return pedidoCertidaoDAO.pesquisarTotalPedidosRealizadoPeloCartorio(usuarioLogadoPortal.getCodigoCorregedoriaSelecionado(), filtroPesquisaCertidao);
	}

	public List<PedidoCertidao> pesquisarPedidosRealizadoPeloCartorio(UsuarioPortalInterno usuarioLogadoPortal, FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao, Paginacao paginacao) {
		return pedidoCertidaoDAO.pesquisarPedidosRealizadoPeloCartorio(usuarioLogadoPortal.getCodigoCorregedoriaSelecionado(), filtroPesquisaCertidao, paginacao);
	}

	public PedidoCertidao recuperaPedidoCertidaoPorProtocolo(String protocolo) {
		return pedidoCertidaoDAO.recuperaPedidoCertidaoPorProtocolo(protocolo);
	}
	
	public PedidoCertidao recuperaPedidoCertidaoPorProtocoloEUsuarioExterno(String protocolo, UsuarioPortalExterno usuarioExterno) {
		return pedidoCertidaoDAO.recuperaPedidoCertidaoPorProtocoloEUsuarioExterno(protocolo, usuarioExterno);
	}
	

	public void realizarSolicitacaoEstorno(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		if (!pedidoCertidao.isSituacaoAguardandoValidacaoArquivo() && !pedidoCertidao.isSituacaoPagamentoAprovadoAguardandoLiberacao()) {
			throw new ECivilException("A situação atual do pedido não permite realizar a solicitação de estorno");
		}
		
		if (pedidoCertidao.getSolicitacaoEstorno().getId() != null) {
			throw new ECivilException("Já existe uma solicitação de estorno para esse pedido.");
		}
		
		Date dataAtual = dataDAO.retornaDataBanco();
		pedidoCertidao.setDataAlteracao(dataAtual);
		pedidoCertidao.getSolicitacaoEstorno().setDataSolicitacao(dataAtual);
		pedidoCertidao.getSolicitacaoEstorno().setPedidoCertidao(pedidoCertidao);
		pedidoCertidao.getSolicitacaoEstorno().setSituacao(EnumSituacaoSolicitacaoEstorno.E);
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_SOLICITACAO_ESTORNO, usuarioPortalInterno);
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
	}
	
	public PedidoCertidao rejeitarEstorno(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		if (!pedidoCertidao.isSituacaoSolicitacaoEstorno()) {
			throw new ECivilException("A situação atual do pedido não permite rejeitar o estorno");
		}
		
		Date dataAtual = dataDAO.retornaDataBanco();
		pedidoCertidao.setDataAlteracao(dataAtual);
		pedidoCertidao.getSolicitacaoEstorno().setDataResposta(dataAtual);
		pedidoCertidao.getSolicitacaoEstorno().setSituacao(EnumSituacaoSolicitacaoEstorno.R);
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_ESTORNO_REJEITADO, usuarioPortalInterno);
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public PedidoCertidao realizarEstorno(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		if (!pedidoCertidao.isSituacaoSolicitacaoEstorno()) {
			throw new ECivilException("A situação atual do pedido não permite realizar o estorno");
		}
		
		Date dataAtual = dataDAO.retornaDataBanco();
		pedidoCertidao.setDataAlteracao(dataAtual);
		pedidoCertidao.getSolicitacaoEstorno().setDataResposta(dataAtual);
		pedidoCertidao.getSolicitacaoEstorno().setSituacao(EnumSituacaoSolicitacaoEstorno.A);
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_ESTORNO_REALIZADO, usuarioPortalInterno);
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		contaDigitalWS.realizarEstorno(montaObjetoRequestRealizarEstorno(pedidoCertidao, usuarioPortalInterno.getCpf()));
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}

	private ContaDigitalRequest montaObjetoRequestRealizarEstorno(PedidoCertidao pedidoCertidao, String cpfUsuario) {
		ContaDigitalRequest contaDigitalRequest = new ContaDigitalRequest();
		contaDigitalRequest.setCodigoCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitante());
		contaDigitalRequest.setCodigoCorregedoriaBeneficiario(pedidoCertidao.getCodCorregedoriaRequisitada());
		contaDigitalRequest.setValor(pedidoCertidao.recuperaValoresCertidaoRequisitado().getValorTotal());
		contaDigitalRequest.setCpfCnpjOperacao(cpfUsuario);
		contaDigitalRequest.setProtocolo(pedidoCertidao.getProtocolo());
		return contaDigitalRequest;
	}
	
	public PedidoCertidao salvarCodigoRastreio(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_FINALIZADO, usuarioPortalInterno);
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		emailBO.enviaEmailAlteracaoSituacaoPedidoCertidao(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public PedidoCertidao confirmaPedido(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		
		informaValorCertidao(pedidoCertidao);
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_AGUARDANDO_PAGAMENTO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		
		emailBO.enviaEmailAlteracaoSituacaoPedidoCertidao(pedidoCertidao);
		
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public PedidoCertidao confirmaPedidoCertidaoNegativa(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		informaValorCertidao(pedidoCertidao);
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_AGUARDANDO_PAGAMENTO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		emailBO.enviaEmailAlteracaoSituacaoPedidoCertidao(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}

	private void informaValorCertidao(PedidoCertidao pedidoCertidao) throws ECivilException {
		try {
			pedidoCertidao.setTaxaManuntencao(new BigDecimal(parametroDAO.buscaValorParametro(Parametro.VALOR_TAXA_MANUNTENCAO)));
			
			AtoResponseVo atoResponseVo = recuperaValoresAtoVigente(EnumCodigoAto.COD_CERTIDAO_DOCUMENTOS_ARQUIVADOS.getCodigo());
			AtoResponseVo atoResponseVoAverbacao = null;
			if (pedidoCertidao.isPossuiAverbacao()) {
				atoResponseVoAverbacao = recuperaValoresAtoVigente(EnumCodigoAto.COD_AVERBACAO_CERTIDAO_DOCUMENTOS_ARQUIVADOS.getCodigo());
			}
			
			pedidoCertidao.setValorCertidao(realizaCalculosValorCertidao(pedidoCertidao, atoResponseVo, atoResponseVoAverbacao));
			setaValorTotalCertidao(pedidoCertidao);
			valoresPedidoCertidaoBO.setaValoresPedidoCertidao(pedidoCertidao, atoResponseVo, atoResponseVoAverbacao);
			
		} catch (ECivilException e) {
			throw e;
		} catch (Exception e) {
			throw new ECivilException("Não foi possível calcular o valor da certidão nesse momento. Tente novamente mais tarde.");
		}
	}
	
	private BigDecimal realizaCalculosValorCertidao(PedidoCertidao pedidoCertidao, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao) throws ECivilException {
		if (pedidoCertidao.isPedidoDeCartorioParaCartorio()) {
			return calculaValorCertidaoDeCartorioParaCartorio(pedidoCertidao, atoResponseVo, atoResponseVoAverbacao);
		}
		return calculaValorCertidaoDePessoaParaCartorio(pedidoCertidao, atoResponseVo, atoResponseVoAverbacao);
	}
	
	private void setaValorTotalCertidao(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.isSaidaViaCorreios()) {
			pedidoCertidao.setValorTotal(pedidoCertidao.getValorCertidao().add(pedidoCertidao.getValorFrete()));
		} else {
			pedidoCertidao.setValorTotal(pedidoCertidao.getValorCertidao());
		}		
	}
	
	private BigDecimal calculaValorCertidaoDeCartorioParaCartorio(PedidoCertidao pedidoCertidao, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao) throws ECivilException {
		pedidoCertidao.setAliquotaISSQNRequisitado(aliquotaISSQNBO.recuperaPorcentagemAliquotaISSQN(pedidoCertidao.getCodCorregedoriaRequisitada()));
		pedidoCertidao.setAliquotaISSQNRequisitante(aliquotaISSQNBO.recuperaPorcentagemAliquotaISSQN(pedidoCertidao.getCodCorregedoriaRequisitante()));
		
		if (pedidoCertidao.isPossuiAverbacao()) {
			return calculaValorCertidaoCartorioComAverbacao(pedidoCertidao.getAliquotaISSQNRequisitado(), pedidoCertidao.getAliquotaISSQNRequisitante(), 
					atoResponseVo, atoResponseVoAverbacao, pedidoCertidao.getTaxaManuntencao());
		}
		
		return calculaValorCertidaoCartorioSemAverbacao(pedidoCertidao.getAliquotaISSQNRequisitado(), pedidoCertidao.getAliquotaISSQNRequisitante(), 
				atoResponseVo, pedidoCertidao.getTaxaManuntencao());
	}

	private BigDecimal calculaValorCertidaoCartorioComAverbacao(BigDecimal aliquotaIssqnCartorio1, BigDecimal aliquotaIssqnCartorio2, AtoResponseVo atoResponseVo, 
			AtoResponseVo atoResponseVoAverbacao, BigDecimal taxaManuntencao) throws ECivilException {
		
		return pedidoCertidaoDAO.calculaValorCertidaoCartorioComAverbacao(atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(),
				atoResponseVo.getTfj(), atoResponseVoAverbacao.getValorFinal(), atoResponseVoAverbacao.getRecompe(),
				atoResponseVoAverbacao.getTfj(), taxaManuntencao, aliquotaIssqnCartorio1, aliquotaIssqnCartorio2);
	}
	
	private BigDecimal calculaValorCertidaoCartorioSemAverbacao(BigDecimal aliquotaIssqnCartorio1, BigDecimal aliquotaIssqnCartorio2, AtoResponseVo atoResponseVo, BigDecimal taxaManuntencao) {
		return pedidoCertidaoDAO.calculaValorCertidaoCartorioSemAverbacao(atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(),
				atoResponseVo.getTfj(), taxaManuntencao, aliquotaIssqnCartorio1, aliquotaIssqnCartorio2);
	}

	private BigDecimal calculaValorCertidaoDePessoaParaCartorio(PedidoCertidao pedidoCertidao, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao) throws ECivilException {
		pedidoCertidao.setAliquotaISSQNRequisitado(aliquotaISSQNBO.recuperaPorcentagemAliquotaISSQN(pedidoCertidao.getCodCorregedoriaRequisitada()));
		if (pedidoCertidao.isPossuiAverbacao()) {
			return calculaValorCertidaoPessoaComAverbacao(pedidoCertidao.getAliquotaISSQNRequisitado(), atoResponseVo, atoResponseVoAverbacao, pedidoCertidao.getTaxaManuntencao());
		}
		return calculaValorCertidaoPessoaSemAverbacao(pedidoCertidao.getAliquotaISSQNRequisitado(), atoResponseVo, pedidoCertidao.getTaxaManuntencao());
	}

	private BigDecimal calculaValorCertidaoPessoaComAverbacao(BigDecimal aliquotaIssqn, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao, BigDecimal taxaManuntencao) throws ECivilException {
		return pedidoCertidaoDAO.calculaValorCertidaoPessoaComAverbacao(atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(),
				atoResponseVo.getTfj(), atoResponseVoAverbacao.getValorFinal(), atoResponseVoAverbacao.getRecompe(),
				atoResponseVoAverbacao.getTfj(), taxaManuntencao, aliquotaIssqn);
	}
	
	private BigDecimal calculaValorCertidaoPessoaSemAverbacao(BigDecimal aliquotaIssqn, AtoResponseVo atoResponseVo, BigDecimal taxaManuntencao) {
		return pedidoCertidaoDAO.calculaValorCertidaoPessoaSemAverbacao(atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(),
				atoResponseVo.getTfj(), taxaManuntencao, aliquotaIssqn);
	}

	private AtoResponseVo recuperaValoresAtoVigente(String codigoAto) throws ECivilException {
		AtoResponseVo atoResponseVo =  recompeWS.recuperaAtoVigentePorCodigo(codigoAto);
		if (atoResponseVo == null) {
			throw new ECivilException("Não foi encontrado os valores dos emolumentos para o codigo do ato informado. Código ato: " + atoResponseVo);
		}
		return atoResponseVo;
	}
	
	public PedidoCertidao finalizarPedido(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_FINALIZADO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public PedidoCertidao rejeitarArquivoEnviado(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_ARQUIVO_NAO_ACEITO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidao.setAnexoCertidao(null);
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public void realizarPagamentoCartorio(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_PAGAMENTO_APROVADO_AGUARDANDO_LIBERACAO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		contaDigitalWS.realizarTransferencia(montaObjetoRequestRealizarPagamentoCertidao(pedidoCertidao, usuarioPortalInterno.getCpf()));
	}

	private ContaDigitalRequest montaObjetoRequestRealizarPagamentoCertidao(PedidoCertidao pedidoCertidao, String cpfUsuario) {
		ContaDigitalRequest contaDigitalRequest = new ContaDigitalRequest();
		contaDigitalRequest.setCodigoCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitante());
		contaDigitalRequest.setCodigoCorregedoriaBeneficiario(pedidoCertidao.getCodCorregedoriaRequisitada());
		contaDigitalRequest.setValor(pedidoCertidao.recuperaValoresCertidaoRequisitado().getValorTotal());
		contaDigitalRequest.setCpfCnpjOperacao(cpfUsuario);
		contaDigitalRequest.setProtocolo(pedidoCertidao.getProtocolo());
		return contaDigitalRequest;
	}

	public PedidoCertidao rejeitarPedido(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		if (!pedidoCertidao.isMotivoRejeicaoInformarMotivo()) {
			pedidoCertidao.setMotivoRejeicao(EnumMotivoRejeicao.REGISTRO_NÃO_LOCALIZADO_NESTA_SERVENTIA.getDescricao());
		}
		
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_REJEITADO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public PedidoCertidao salvarEAnexarCertidaoNegativa(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		pedidoCertidao.setAnexoCertidao(montaObjetoAnexoCertidao(pedidoCertidao, AnexoCertidao.TIPO_EXTENSAO_PDF));
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_FINALIZADO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public PedidoCertidao salvarEAnexarXmlCertidao(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		pedidoCertidao.setAnexoCertidao(montaObjetoAnexoCertidao(pedidoCertidao, AnexoCertidao.TIPO_EXTENSAO_XML));
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_AGUARDANDO_VALIDACAO_ARQUIVO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}

	private AnexoCertidao montaObjetoAnexoCertidao(PedidoCertidao pedidoCertidao, String tipoExtensao) {
		AnexoCertidao anexoCertidao = new AnexoCertidao();
		anexoCertidao.setCertidao(pedidoCertidao.getAnexo());
		anexoCertidao.setDataGravacao(dataDAO.retornaDataBanco());
		anexoCertidao.setPedidoCertidao(pedidoCertidao);
		anexoCertidao.setTipoExtensao(tipoExtensao);
		return anexoCertidao;
	}

	public PedidoCertidao liberaColetaCertidaoCidadao(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		setaSituacaoPedidoCertidao(pedidoCertidao, SituacaoPedidoCertidao.COD_COLETA_NO_CARTORIO, usuarioPortalInterno);
		pedidoCertidao.setDataAlteracao(dataDAO.retornaDataBanco());
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		return recuperaPedidoCertidaoPorProtocolo(pedidoCertidao.getProtocolo());
	}
	
	public byte[] emitirBoleto(PedidoCertidao pedidoCertidao, UsuarioPortalExterno usuarioExterno) throws ECivilException {
		VinculoBoleto vinculoBoleto = pedidoCertidao.recuperaVinculoBoletoAtivo();
		
		if (vinculoBoleto == null) {
			byte[] novoBoleto = emitirNovoBoleto(pedidoCertidao, usuarioExterno);
			pedidoCertidao.setAnexo(novoBoleto);
			emailBO.enviaEmailEmissaoBoleto(pedidoCertidao);
			return novoBoleto;
		}
		
//		FIXME - Rever essa regra
//		if (vinculoBoleto.ehBoletoVencido()) {
//			vinculoBoleto.setDataFim(dataDAO.retornaDataBanco());
//			return emitirNovoBoleto(pedidoCertidao, usuarioExterno);
//		}
		
		return recompeWS.imprimirBoleto(vinculoBoleto.getNumeroBoleto());
	}

	private byte[] emitirNovoBoleto(PedidoCertidao pedidoCertidao, UsuarioPortalExterno usuarioExterno) throws ECivilException {
		UsuarioPortalExterno usuarioPortalExterno = usuarioPortalExternoBO.recuperarUsuarioPorId(usuarioExterno.getId());
		RegistroBoletoResponseVo respostaRegistroBoleto = recompeWS.registrarBoleto(montaObjetoRegistroBoletoRequestVo(pedidoCertidao, usuarioPortalExterno));
	
		pedidoCertidao.getListaVinculoBoletoInicializada().add(montaObjetoVinculoBoletoPedidoCertidao(pedidoCertidao, respostaRegistroBoleto));
		pedidoCertidaoDAO.atualiza(pedidoCertidao);
		
		if (respostaRegistroBoleto.getBoletoPdf() != null) {
			return Base64.getDecoder().decode(respostaRegistroBoleto.getBoletoPdf());
		}
		
		return null;
	}

	
	
	private VinculoBoleto montaObjetoVinculoBoletoPedidoCertidao(PedidoCertidao pedidoCertidao, RegistroBoletoResponseVo respostaRegistroBoleto) {
		VinculoBoleto vinculoBoleto = new VinculoBoleto();
		vinculoBoleto.setDataInicio(dataDAO.retornaDataBanco());
		vinculoBoleto.setDataVencimento(DataUtil.converteDataStringParaDate(respostaRegistroBoleto.getDataVencimento()));
		vinculoBoleto.setNumeroBoleto(respostaRegistroBoleto.getNumeroTitulo());
		vinculoBoleto.setPedidoCertidao(pedidoCertidao);
		return vinculoBoleto;
	}

	private RegistroBoletoRequestVo montaObjetoRegistroBoletoRequestVo(PedidoCertidao pedidoCertidao, UsuarioPortalExterno usuarioPortalExterno) {
		Date dataAtual = dataDAO.retornaDataBanco();
		RegistroBoletoRequestVo registroBoletoRequestVo = new RegistroBoletoRequestVo();
		registroBoletoRequestVo.setCodigoCarteira(parametroDAO.buscaValorParametro(Parametro.CODIGO_CARTEIRA_EMISSAO_BOLETO));
		registroBoletoRequestVo.setDataEmissao(DataUtil.formataDataRegistroBoletoBradesco(dataAtual));
		registroBoletoRequestVo.setDataVencimento(recuperaDataVencimentoBoleto(dataAtual));
		registroBoletoRequestVo.setValor(String.valueOf(pedidoCertidao.getValorTotal()));
		registroBoletoRequestVo.setNomePagador(usuarioPortalExterno.getNome());
		registroBoletoRequestVo.setCpfCnpjPagador(usuarioPortalExterno.getCpf());
		registroBoletoRequestVo.setCepPagador(usuarioPortalExterno.getEnderecoPadraoUsuarioExterno().getCep());
		registroBoletoRequestVo.setLogradouroPagador(usuarioPortalExterno.getEnderecoPadraoUsuarioExterno().getNomeLogradouro());
		registroBoletoRequestVo.setNumeroLogradouroPagador(usuarioPortalExterno.getEnderecoPadraoUsuarioExterno().getNumeroLogradouro());
		registroBoletoRequestVo.setBairroPagador(usuarioPortalExterno.getEnderecoPadraoUsuarioExterno().getBairro());
		registroBoletoRequestVo.setMunicipioPagador(usuarioPortalExterno.getEnderecoPadraoUsuarioExterno().getMunicipio().getNome());
		registroBoletoRequestVo.setUfPagador(usuarioPortalExterno.getEnderecoPadraoUsuarioExterno().getMunicipio().getUf().getSigla());
		registroBoletoRequestVo.setEmailPgador(usuarioPortalExterno.getEmail());
		return registroBoletoRequestVo;
	}
	
	private String recuperaDataVencimentoBoleto(Date dataEmissao) {
		return DataUtil.formataDataRegistroBoletoBradesco(DataUtil.adicionaDiasData(dataEmissao, Integer.valueOf(parametroDAO.buscaValorParametro(Parametro.QTD_DIAS_VENCIMENTO_BOLETO))));
	}

	public byte[] downloadCertidaoDigital(PedidoCertidao pedidoCertidao) throws Exception {
		if (pedidoCertidao.getAnexoCertidao() == null || !pedidoCertidao.getAnexoCertidao().getTipoExtensao().equals(AnexoCertidao.TIPO_EXTENSAO_PDF)) {
			throw new ECivilException("Não é possível fazer o download da certidão.");
		}
		byte[] imagem = pedidoCertidao.getAnexoCertidao().getCertidao();
		imagem = chancelaDocumentoProcessoDigital(imagem, pedidoCertidao);
		imagem = assinaProcessoDigital(imagem);
		return imagem;
	}

	private byte[] chancelaDocumentoProcessoDigital(byte[] imagem, PedidoCertidao pedidoCertidao) throws Exception {
		ImagemViaUnica imagemViaUnica = new ImagemViaUnica();
		String chancelaDocumento = "Documento assinado eletronicamente por Ordem dos Advogados do Brasil - Conselho Seccional do Distrito Federal, em 31/07/2020, às 16:30. A autenticidade deste documento pode ser verificada no site da OAB/DF, informando o protocolo :protocolo e o código de segurança :chaveSeguranca.";
		return imagemViaUnica.resize(pedidoCertidao.getAnexoCertidao().getCertidao(), chancelaDocumento);
	}

	private byte[] assinaProcessoDigital(byte[] imagem) throws Exception {
		String ceritificado = parametroDAO.buscaValorParametro(Parametro.CAMINHO_CERTIFICADO);
		String senha = parametroDAO.buscaValorParametro(Parametro.SENHA_CERTIFICADO_A1);
		String siglaUf = parametroDAO.buscaValorParametro(Parametro.SIGLA_ESTADO_SEDE);
		AssinaturaDigitalPDF assinaturaDigitalPDF = new AssinaturaDigitalPDF();
		return assinaturaDigitalPDF.assinarComCertificadoPKSC12(imagem, senha, ceritificado, "Via Digital", dataDAO.retornaDataBanco(), siglaUf);
	}

	public void setaDemonstracaoValores(DadosCertidaoResponse certidao, PedidoCertidao pedidoCertidao) throws ECivilException {
		BigDecimal taxaManuntencao = new BigDecimal(parametroDAO.buscaValorParametro(Parametro.VALOR_TAXA_MANUNTENCAO));

		AliquotaISSQN aliquota = aliquotaISSQNBO.recuperaAliquotaISSQN(certidao.getCodCorregedoria());
		certidao.setAliquotaMunicipioRequisitado(aliquota.getAliquota() != null ? aliquota.getAliquota() : null);
		
		AtoResponseVo atoResponseVo = recuperaValoresAtoVigente(EnumCodigoAto.COD_CERTIDAO_DOCUMENTOS_ARQUIVADOS.getCodigo());
		certidao.setValorCertidaoSemAverbacao(calculaValorCertidaoPessoaSemAverbacao(certidao.getAliquotaMunicipioRequisitado(), atoResponseVo, taxaManuntencao));
		
		AtoResponseVo atoResponseVoAverbacao = recuperaValoresAtoVigente(EnumCodigoAto.COD_AVERBACAO_CERTIDAO_DOCUMENTOS_ARQUIVADOS.getCodigo());
		certidao.setValorCertidaoComAverbacao(calculaValorCertidaoPessoaComAverbacao(certidao.getAliquotaMunicipioRequisitado(), atoResponseVo, atoResponseVoAverbacao, taxaManuntencao));		
	}

}