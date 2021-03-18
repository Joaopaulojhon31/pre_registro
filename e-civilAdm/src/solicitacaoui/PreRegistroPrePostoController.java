package solicitacaoui;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.controller.PrincipalController;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.CnjBO;
import ecivil.ejb.bo.HistoricoPreRegistroBO;
import ecivil.ejb.bo.PrepostoBO;
import ecivil.ejb.bo.UIBO;
import ecivil.ejb.dao.MunicipioDAO;
import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.dao.RelacaoBairroServentiaDAO;
import ecivil.ejb.dao.UfDAO;
import ecivil.ejb.entidade.CNJ;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.RelacaoBairroServentia;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;
import ecivil.ejb.entidade.UI;
import ecivil.ejb.entidade.Uf;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.IntegradorCorreiosWS;
import ecivil.ejb.ws.cliente.rest.MigracaoRecompeWS;
import ecivil.ejb.ws.cliente.rest.response.integradorcorreios.ConsultaCepResponse;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class PreRegistroPrePostoController extends BaseController implements Serializable {

	@EJB
	private UfDAO ufDAO;

	@EJB
	private MunicipioDAO municipioDAO; 
	
	@EJB
	private RelacaoBairroServentiaDAO RelacaoBairroServentiaDAO;

	@EJB
	private MigracaoRecompeWS migracaoRecompeWS;

	@EJB
	private IntegradorCorreiosWS integradorCorreiosWS;

	@EJB
	private PreRegistroDAO preRegistroDAO;
	
	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;

	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private UIBO uiBO;
	
	@EJB	
	private PrepostoBO prepostoBO;
	
	@EJB
	private CnjBO cnjBO;

	private PreRegistro preRegistroPrePosto;
	private UI dadosUnidadeInterligadaPrePosto;
	private CNJ serventiaPrePosto;
	private PrincipalController principalController;
	private ConsultaCepResponse cepResponse;
	private List<CartorioResponse> listaCartorio;
	private List<Municipio> listaNaturalidadeMunicipiosDeclarante;
	private List<String> listaPaisResidenciaMaisProximaServentia;
	private List<Municipio> listaNaturalidadeMunicipiosCrianca;
	private List<String> listaPessoaDeclarante;
	private List<String> listaPessoaTipoDeclaracao;
	private List<RelacaoBairroServentia> listaServentiasProximas;
	private List<Municipio> listaNaturalidadeMunicipiosMae;
	private List<Municipio> listaResidenciaMunicipiosMae;
	private List<Municipio> listaNaturalidadeMunicipiosPai;
	private List<Municipio> listaResidenciaMunicipiosPai;
	private List<PreRegistro> listaPreRegistroPorCpfMae;
	private String tipoDeclaranteOutro;
	private String cpfMaeBuscaPreRegistro;
	private boolean disabilitaCamposCPFMae;
	private boolean disabilitaCamposServentia;
	private boolean exibeModalBuscaPreRegistro;
	private boolean buscaEncontradaPreRegistroPrePosto;

	@PostConstruct
	public void inicializa() throws Exception {
		setPreRegistroPrePosto(new PreRegistro());
		setPrincipalController(new PrincipalController());
		getListaUf();
		setListaResidenciaMunicipiosMae(new ArrayList<Municipio>());
		preRegistroPrePosto.setOpcaoRegistro("Residência dos Pais");
		preRegistroPrePosto.setPaisResidenciaServentiaEscolhida("Mãe");
		preRegistroPrePosto.setStatusPai("1");
		preencheListasStatusPai();
		setDisabilitaCamposCPFMae(false);
		setDisabilitaCamposServentia(false);
		setExibeModalBuscaPreRegistro(true);
		setBuscaEncontradaPreRegistroPrePosto(false);
	}

	public PreRegistro getPreRegistroPrePosto() {
		return preRegistroPrePosto;
	}

	public void setPreRegistroPrePosto(PreRegistro preRegistroPrePosto) {
		this.preRegistroPrePosto = preRegistroPrePosto;
	}

	public CartorioLookUp getCartorioLookUp() {
		return cartorioLookUp;
	}

	public void setCartorioLookUp(CartorioLookUp cartorioLookUp) {
		this.cartorioLookUp = cartorioLookUp;
	}

	public UfDAO getUfDAO() {
		return ufDAO;
	}

	public void setUfDAO(UfDAO ufDAO) {
		this.ufDAO = ufDAO;
	}

	public MunicipioDAO getMunicipioDAO() {
		return municipioDAO;
	}

	public void setMunicipioDAO(MunicipioDAO municipioDAO) {
		this.municipioDAO = municipioDAO;
	}

	public MigracaoRecompeWS getMigracaoRecompeWS() {
		return migracaoRecompeWS;
	}

	public void setMigracaoRecompeWS(MigracaoRecompeWS migracaoRecompeWS) {
		this.migracaoRecompeWS = migracaoRecompeWS;
	}

	public PreRegistroDAO getPreRegistroDAO() {
		return preRegistroDAO;
	}

	public void setPreRegistroDAO(PreRegistroDAO preRegistroDAO) {
		this.preRegistroDAO = preRegistroDAO;
	}

	public List<Municipio> getListaNaturalidadeMunicipiosDeclarante() {
		return listaNaturalidadeMunicipiosDeclarante;
	}

	public void setListaNaturalidadeMunicipiosDeclarante(List<Municipio> listaNaturalidadeMunicipiosDeclarante) {
		this.listaNaturalidadeMunicipiosDeclarante = listaNaturalidadeMunicipiosDeclarante;
	}

	public List<Municipio> getListaNaturalidadeMunicipiosCrianca() {
		return listaNaturalidadeMunicipiosCrianca;
	}

	public void setListaNaturalidadeMunicipiosCrianca(List<Municipio> listaNaturalidadeMunicipiosCrianca) {
		this.listaNaturalidadeMunicipiosCrianca = listaNaturalidadeMunicipiosCrianca;
	}

	public List<Municipio> getListaNaturalidadeMunicipiosMae() {
		return listaNaturalidadeMunicipiosMae;
	}

	public void setListaNaturalidadeMunicipiosMae(List<Municipio> listaNaturalidadeMunicipiosMae) {
		this.listaNaturalidadeMunicipiosMae = listaNaturalidadeMunicipiosMae;
	}

	public List<Municipio> getListaResidenciaMunicipiosMae() {
		return listaResidenciaMunicipiosMae;
	}

	public void setListaResidenciaMunicipiosMae(List<Municipio> listaResidenciaMunicipiosMae) {
		this.listaResidenciaMunicipiosMae = listaResidenciaMunicipiosMae;
	}

	public List<Municipio> getListaNaturalidadeMunicipiosPai() {
		return listaNaturalidadeMunicipiosPai;
	}

	public void setListaNaturalidadeMunicipiosPai(List<Municipio> listaNaturalidadeMunicipiosPai) {
		this.listaNaturalidadeMunicipiosPai = listaNaturalidadeMunicipiosPai;
	}

	public List<Municipio> getListaResidenciaMunicipiosPai() {
		return listaResidenciaMunicipiosPai;
	}

	public void setListaResidenciaMunicipiosPai(List<Municipio> listaResidenciaMunicipiosPai) {
		this.listaResidenciaMunicipiosPai = listaResidenciaMunicipiosPai;
	}

	public List<PreRegistro> getListaPreRegistroPorCpfMae() {
		return listaPreRegistroPorCpfMae;
	}

	public void setListaPreRegistroPorCpfMae(List<PreRegistro> listaPreRegistroPorCpfMae) {
		this.listaPreRegistroPorCpfMae = listaPreRegistroPorCpfMae;
	}

	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}

	public List<Uf> getListaUf() {
		return ufDAO.recuperaListaUF();
	}

	public String getTipoDeclaranteOutro() {
		return tipoDeclaranteOutro;
	}

	public void setTipoDeclaranteOutro(String tipoDeclaranteOutro) {
		this.tipoDeclaranteOutro = tipoDeclaranteOutro;
	}

	public PrincipalController getPrincipalController() {
		return principalController;
	}

	public void setPrincipalController(PrincipalController principalController) {
		this.principalController = principalController;
	}

	public String getCpfMaeBuscaPreRegistro() {
		return cpfMaeBuscaPreRegistro;
	}

	public void setCpfMaeBuscaPreRegistro(String cpfMaeBuscaPreRegistro) {
		this.cpfMaeBuscaPreRegistro = cpfMaeBuscaPreRegistro;
	}

	public ConsultaCepResponse getCepResponse() {
		return cepResponse;
	}

	public void setCepResponse(ConsultaCepResponse cepResponse) {
		this.cepResponse = cepResponse;
	}

	public List<RelacaoBairroServentia> getListaServentiasProximas() {
		return listaServentiasProximas;
	}

	public void setListaServentiasProximas(List<RelacaoBairroServentia> listaServentiasProximas) {
		this.listaServentiasProximas = listaServentiasProximas;
	}

	public boolean isExibeModalBuscaPreRegistro() {
		return exibeModalBuscaPreRegistro;
	}

	public void setExibeModalBuscaPreRegistro(boolean exibeModalBuscaPreRegistro) {
		this.exibeModalBuscaPreRegistro = exibeModalBuscaPreRegistro;
	}

	public boolean isDisabilitaCamposCPFMae() {
		return disabilitaCamposCPFMae;
	}

	public void setDisabilitaCamposCPFMae(boolean travaCamposCPFMae) {
		this.disabilitaCamposCPFMae = travaCamposCPFMae;
	}

	public boolean isDisabilitaCamposServentia() {
		return disabilitaCamposServentia;
	}

	public void setDisabilitaCamposServentia(boolean disabilitaCamposServentia) {
		this.disabilitaCamposServentia = disabilitaCamposServentia;
	}

	public List<String> getListaPaisResidenciaMaisProximaServentia() {
		return listaPaisResidenciaMaisProximaServentia;
	}

	public void setListaPaisResidenciaMaisProximaServentia(List<String> listaPaisResidenciaMaisProximaServentia) {
		this.listaPaisResidenciaMaisProximaServentia = listaPaisResidenciaMaisProximaServentia;
	}

	public List<String> getListaPessoaDeclarante() {
		return listaPessoaDeclarante;
	}

	public void setListaPessoaDeclarante(List<String> listaPessoaDeclarante) {
		this.listaPessoaDeclarante = listaPessoaDeclarante;
	}

	public List<String> getListaPessoaTipoDeclaracao() {
		return listaPessoaTipoDeclaracao;
	}

	public void setListaPessoaTipoDeclaracao(List<String> listaPessoaTipoDeclaracao) {
		this.listaPessoaTipoDeclaracao = listaPessoaTipoDeclaracao;
	}
	
	public CNJ getServentiaPrePosto() {
		return serventiaPrePosto;
	}

	public void setServentiaPrePosto(CNJ serventiaPrePosto) {
		this.serventiaPrePosto = serventiaPrePosto;
	}

	public UI getDadosUnidadeInterligadaPrePosto() {
		return dadosUnidadeInterligadaPrePosto;
	}

	public void setDadosUnidadeInterligadaPrePosto(UI dadosUnidadeInterligadaPrePosto) {
		this.dadosUnidadeInterligadaPrePosto = dadosUnidadeInterligadaPrePosto;
	}

	public UIBO getUiBO() {
		return uiBO;
	}

	public void setUiBO(UIBO uiBO) {
		this.uiBO = uiBO;
	}

	public boolean isBuscaEncontradaPreRegistroPrePosto() {
		return buscaEncontradaPreRegistroPrePosto;
	}

	public void setBuscaEncontradaPreRegistroPrePosto(boolean buscaEncontradaPreRegistroPrePosto) {
		this.buscaEncontradaPreRegistroPrePosto = buscaEncontradaPreRegistroPrePosto;
	}

	public void recuperaListaMunicipioPorUfNaturalidadeDeclarante(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosDeclarante(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public String gravaDadosPreRegistroPrePosto() throws ECivilException {
		if (verificaPreenchimentoCamposPreRegistroPrePosto()) {
			if (verificaExistenciaPreRegistroPrePostoPorCpfMae() && !buscaEncontradaPreRegistroPrePosto) {
				Mensagem.errorSemBundle("Já existe um registro em aberto para o CPF da mãe que foi inserido.");

			} else {
				if (Util.isNullOrEmpty(preRegistroPrePosto.getSituacaoSolicitacao())) {
					preRegistroPrePosto.setSituacaoSolicitacao(SituacaoSolicitacaoUI.EM_PREPARACAO);
				}
				preRegistroPrePosto.setDataInicioSolicitacao(getPreRegistroDAO().retornaDataBanco());
				preRegistroPrePosto.setDataAlteracaoSolicitacao(getPreRegistroDAO().retornaDataBanco());
				gravaInformacoesServentiaPrePosto();
				gravaInformacoesUnidadeInterligadaPrePosto();
				GerarCodigoHash();
				historicoPreRegistroBO.setaHistoricoPreRegistro(preRegistroPrePosto,getUsuarioLogadoPortal().getId());
				DeclaracoesPreRegistroPrePostoController.setPreRegistroDeclaracoesPrePosto(preRegistroPrePosto);
				return principalController.direcionaDeclaracoesPreRegistroPrePosto();
			}
		}
		return "";
	}
	
	public void gravaInformacoesServentiaPrePosto() throws ECivilException {
		
		if (preRegistroPrePosto.getOpcaoRegistro().equals("Local de Nascimento -Unidade Interligada")) {
			serventiaPrePosto=prepostoBO.recuperaServentiaPreposto(getUsuarioLogadoPortal().getCpf());
			preRegistroPrePosto.setCnsCartorio(serventiaPrePosto.getCnjCns());
			preRegistroPrePosto.setCodCorregedoriaCartorio(serventiaPrePosto.getCnjCdCorregedoria());
		} else
			gravaCodigoCnsCartorioPreRegistroPrePosto();

	}
	public void gravaInformacoesUnidadeInterligadaPrePosto() {
		dadosUnidadeInterligadaPrePosto=uiBO.recuperaUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf());
		serventiaPrePosto=prepostoBO.recuperaServentiaPreposto(getUsuarioLogadoPortal().getCpf());
		preRegistroPrePosto.setCnsCartorio(serventiaPrePosto.getCnjCns());
		preRegistroPrePosto.setCodCorregedoriaCartorio(serventiaPrePosto.getCnjCdCorregedoria());
		preRegistroPrePosto.setBairroUi(dadosUnidadeInterligadaPrePosto.getBairroUI());
		preRegistroPrePosto.setNomeUi(dadosUnidadeInterligadaPrePosto.getNomeHospital());
		preRegistroPrePosto.setLogradouroUi(dadosUnidadeInterligadaPrePosto.getEnderecoUI());
		preRegistroPrePosto.setMunicipioUi(uiBO.recuperaMunicipioUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()));
		preRegistroPrePosto.setUfUi(uiBO.recuperaUfUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()));
		preRegistroPrePosto.setCodigoUI(Long.valueOf((dadosUnidadeInterligadaPrePosto.getIdUI())));
	}
	
	public void gravaCodigoCnsCartorioPreRegistroPrePosto() throws ECivilException {
		if (!preRegistroPrePosto.getCodCorregedoriaCartorio().isEmpty()) {
			preRegistroPrePosto.setCnsCartorio(migracaoRecompeWS
					.recuperaCartorioPorCodigoCorregedoria(preRegistroPrePosto.getCodCorregedoriaCartorio()).getCns());
		}
	}

	public boolean verificaPreenchimentoCamposPreRegistroPrePosto() {
		if (!verificaPreenchimentoCamposCriancaPrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposMaePrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposPaiPrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCartorioPrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposContatoPrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposDeclaranteoPrePosto()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean verificaPreenchimentoCamposCriancaPrePosto() {
		if (preRegistroPrePosto.getQuantidadeCriancas().equals("1")) {
			if (!verificaPreechimentoCamposNaoGemeos()) {
				return false;
			}
		} else if (preRegistroPrePosto.getQuantidadeCriancas().equals("2")) {
			if (!verificaPreechimentoCamposGemeos()) {
				return false;
			}
		} else if (preRegistroPrePosto.getQuantidadeCriancas().equals("3")) {
			if (!verificaPreechimentoCamposTrigemeos()) {
				return false;
			}
		} else if (preRegistroPrePosto.getQuantidadeCriancas().equals("4")) {
			if (!verificaPreechimentoCamposQuadrigemeos()) {
				return false;
			}
		} else if (preRegistroPrePosto.getQuantidadeCriancas().equals("5")) {
			if (!verificaPreechimentoCamposQuintuplos()) {
				return false;
			}
		}
		if (!verificaPreenchimentoNaturalidadeCriancaPrePosto()) {
			return false;
		}
		return true;
	}

	private boolean verificaPreechimentoCamposNaoGemeos() {
		if (!verificaPreenchimentoCamposCrianca1PrePosto()) {
			return false;
		}
		return true;
	}

	private boolean verificaPreechimentoCamposGemeos() {
		if (!verificaPreenchimentoCamposCrianca1PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca2PrePosto()) {
			return false;
		}
		return true;
	}

	private boolean verificaPreechimentoCamposTrigemeos() {
		if (!verificaPreenchimentoCamposCrianca1PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca2PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca3PrePosto()) {
			return false;
		}
		return true;
	}

	private boolean verificaPreechimentoCamposQuadrigemeos() {
		if (!verificaPreenchimentoCamposCrianca1PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca2PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca3PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca4PrePosto()) {
			return false;
		}
		return true;
	}

	private boolean verificaPreechimentoCamposQuintuplos() {
		if (!verificaPreenchimentoCamposCrianca1PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca2PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca3PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca4PrePosto()) {
			return false;
		} else if (!verificaPreenchimentoCamposCrianca5PrePosto()) {
			return false;
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposCrianca1PrePosto() {
		if (preRegistroPrePosto.getNomeCrianca1().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome da criança está vazio.");
			return false;
		} else if (preRegistroPrePosto.getSexoCrianca1() == null) {
			Mensagem.errorSemBundle("O campo Sexo da criança está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDataNascimentoCrianca1() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento da criança está vazio.");
			return false;
		} else if (!verificaHoraNascimentoValidaPrePosto(preRegistroPrePosto.getHoraNascimentoCrianca1())) {
			Mensagem.errorSemBundle("O campo Hora do Nascimento da criança é inválido.");
			return false;
		} else if (preRegistroPrePosto.getDnvCrianca1().length() < 11) {
			Mensagem.errorSemBundle("O campo DNV da criança é inválido.");
			return false;

		}
		return true;

	}

	private boolean verificaPreenchimentoCamposCrianca2PrePosto() {
		if (preRegistroPrePosto.getNomeCrianca2().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome do segundo gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getSexoCrianca2() == null) {
			Mensagem.errorSemBundle("O campo Sexo do segundo gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDataNascimentoCrianca2() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento do segundo gemelar está vazio.");
			return false;
		} else if (!verificaHoraNascimentoValidaPrePosto(preRegistroPrePosto.getHoraNascimentoCrianca2())) {
			Mensagem.errorSemBundle("O campo Hora do Nascimento do segundo gemelar é inválido.");
			return false;
		} else if (preRegistroPrePosto.getDnvCrianca2().length() < 11) {
			Mensagem.errorSemBundle("O campo DNV do segundo gemelar é inválido.");
			return false;

		}
		return true;

	}

	private boolean verificaPreenchimentoCamposCrianca3PrePosto() {
		if (preRegistroPrePosto.getNomeCrianca3().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome do terceiro gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getSexoCrianca3() == null) {
			Mensagem.errorSemBundle("O campo Sexo do terceiro gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDataNascimentoCrianca3() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento do terceiro gemelar está vazio.");
			return false;
		} else if (!verificaHoraNascimentoValidaPrePosto(preRegistroPrePosto.getHoraNascimentoCrianca3())) {
			Mensagem.errorSemBundle("O campo Hora do Nascimento do terceiro gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDnvCrianca3().length() < 11) {
			Mensagem.errorSemBundle("O campo DNV do terceiro gemelar está vazio.");
			return false;
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposCrianca4PrePosto() {
		if (preRegistroPrePosto.getNomeCrianca4().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome do quarto gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getSexoCrianca4() == null) {
			Mensagem.errorSemBundle("O campo Sexo do quarto gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDataNascimentoCrianca4() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento do quarto gemelar está vazio.");
			return false;
		} else if (!verificaHoraNascimentoValidaPrePosto(preRegistroPrePosto.getHoraNascimentoCrianca4())) {
			Mensagem.errorSemBundle("O campo Hora do Nascimento do quarto gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDnvCrianca4().length() < 11) {
			Mensagem.errorSemBundle("O campo DNV do quarto gemelar está vazio.");
			return false;
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposCrianca5PrePosto() {
		if (preRegistroPrePosto.getNomeCrianca5().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome do quinto gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getSexoCrianca5() == null) {
			Mensagem.errorSemBundle("O campo Sexo do quinto gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDataNascimentoCrianca5() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento do quinto gemelar está vazio.");
			return false;
		} else if (!verificaHoraNascimentoValidaPrePosto(preRegistroPrePosto.getHoraNascimentoCrianca5())) {
			Mensagem.errorSemBundle("O campo Hora do Nascimento do quinto gemelar está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDnvCrianca5().length() < 11) {
			Mensagem.errorSemBundle("O campo DNV do quinto gemelar está vazio.");
			return false;
		}
		return true;
	}

	public boolean verificaPreenchimentoNaturalidadeCriancaPrePosto() {
		if (preRegistroPrePosto.getNaturalidadeUf() == null) {
			Mensagem.errorSemBundle("O campo Naturalidade-UF da criança está vazio.");
			return false;
		} else if (preRegistroPrePosto.getNaturalidade() == null) {
			Mensagem.errorSemBundle("O campo Naturalidade-Município da criança está vazio.");
			return false;
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposMaePrePosto() {
		if (preRegistroPrePosto.getNomeMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getCpfMae() == null) {
			Mensagem.errorSemBundle("O campo CPF da mãe está vazio.");
			return false;
		} else if (!Util.isValidCPF(preRegistroPrePosto.getCpfMae())) {
			Mensagem.errorSemBundle("O campo CPF da mãe é inválido.");
			return false;
		} else if (Util.isNullOrEmpty(preRegistroPrePosto.getNacionalidadeMae())) {
			Mensagem.errorSemBundle("O campo Nacionalidade da mãe está vazio.");
			return false;
		}else if (preRegistroPrePosto.getNaturalidadeUfMae()==null) {
			Mensagem.errorSemBundle("O campo Naturalidade - UF da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getNaturalidadeMae()==null) {
			Mensagem.errorSemBundle("O campo Naturalidade - Município da mãe está vazio.");
			return false;
		}else if (Util.isNullOrEmpty(preRegistroPrePosto.getResidenciaCEPMae())) {
			Mensagem.errorSemBundle("O campo CEP mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getResidenciaUfMae() == null) {
			Mensagem.errorSemBundle("O campo Residência-UF da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getResidenciaMunicipioMae() == null) {
			Mensagem.errorSemBundle("O campo Residência-Município da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getResidenciaBairroMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Residência-Bairro da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getResidenciaLogradouroMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Residência-Logradouro da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getResidenciaNumeroMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Residência-Número da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getProfissaoMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Profissão da mãe está vazio.");
			return false;
		} else if (preRegistroPrePosto.getDataNascimentoMae() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento da mãe está vazio.");
			return false;
		}
		preRegistroPrePosto.setIdadeMae(Util.CalculaIdade(preRegistroPrePosto.getDataNascimentoMae()));

		return true;
	}

	private boolean verificaPreenchimentoCamposPaiPrePosto() {
		if (preRegistroPrePosto.getStatusPai().equals("1")) {
			if (!Util.isValidCPF(preRegistroPrePosto.getCpfPai()) && preRegistroPrePosto.getCpfPai() != null) {
				Mensagem.errorSemBundle("O campo CPF do Pai é inválido");
				return false;
			} else if (Util.isNullOrEmpty(preRegistroPrePosto.getNacionalidadePai())) {
				Mensagem.errorSemBundle("O campo Nacionalidade do pai está vazio.");
				return false;
			}else if (preRegistroPrePosto.getNaturalidadeUfPai()==null) {
				Mensagem.errorSemBundle("O campo Naturalidade - UF do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getNaturalidadePai()==null) {
				Mensagem.errorSemBundle("O campo Naturalidade - Município do pai está vazio.");
				return false;
			} else if (Util.isNullOrEmpty(preRegistroPrePosto.getResidenciaCEPpai())) {
				Mensagem.errorSemBundle("O campo CEP do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getResidenciaUfPai() == null) {
				Mensagem.errorSemBundle("O campo Residência-UF do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getResidenciaMunicipioPai() == null) {
				Mensagem.errorSemBundle("O campo Residência-Município do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getResidenciaBairroPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Residência-Bairro do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getResidenciaLogradouroPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Residência-Logradouro do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getResidenciaNumeroPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Residência-Número do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getProfissaoPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Profissão do pai está vazio.");
				return false;
			} else if (preRegistroPrePosto.getDataNascimentoPai() == null) {
				Mensagem.errorSemBundle("O campo Data de Nascimento do pai está vazio.");
				return false;
			}
			preRegistroPrePosto.setIdadePai(Util.CalculaIdade(preRegistroPrePosto.getDataNascimentoPai()));
		} else if (preRegistroPrePosto.getStatusPai().equals("3")) {
			if (!Util.isValidCPF(preRegistroPrePosto.getCpfPai()) && preRegistroPrePosto.getCpfPai() != null) {
				Mensagem.errorSemBundle("O campo CPF do Pai é inválido");
				return false;
			}
			if (preRegistroPrePosto.getDataNascimentoPai() != null) {
				preRegistroPrePosto.setIdadePai(Util.CalculaIdade(preRegistroPrePosto.getDataNascimentoPai()));
			}
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposCartorioPrePosto() {

		if (preRegistroPrePosto.getOpcaoRegistro() == null) {
			Mensagem.errorSemBundle("O campo Opção de Registro está vazio.");
			return false;
		} else if (preRegistroPrePosto.getOpcaoRegistro().equals("Residência dos Pais")
				&& preRegistroPrePosto.getCodCorregedoriaCartorio() == null) {
			Mensagem.errorSemBundle("O campo de escolha da serventia está vazio.");
			return false;
		} else if (preRegistroPrePosto.getOpcaoRegistro().equals("Local de Nascimento -Unidade Interligada")) {
			preRegistroPrePosto.setPaisResidenciaServentiaEscolhida("");
			preRegistroPrePosto.setCodCorregedoriaCartorio("");
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposContatoPrePosto() {
		if (preRegistroPrePosto.getNomeContato().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome do Contato está vazio.");
			return false;
		} else if (preRegistroPrePosto.getTelCelularContato().isEmpty()) {
			Mensagem.errorSemBundle("O campo Telefone do Contato está vazio.");
			return false;
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposDeclaranteoPrePosto() {
		if (preRegistroPrePosto.getTipoDeclarante() == null) {
			Mensagem.errorSemBundle("O campo Declarante está vazio.");
			return false;
		} else if (preRegistroPrePosto.getTipoDeclarante() == "Outro") {
			if (getTipoDeclaranteOutro() == null) {
				Mensagem.errorSemBundle("O campo Pessoa Declarante está vazio.");
				return false;
			} else if (preRegistroPrePosto.getTipoDeclaracao() == null) {
				Mensagem.errorSemBundle("O campo Tipo de Declaração está vazio.");
				return false;
			}

		}
		return true;
	}

	public boolean verificaExistenciaPreRegistroPrePostoPorCpfMae() {
		if ((preRegistroDAO.buscaPreregistroEmAbertoPorCpfMaePrePosto(preRegistroPrePosto.getCpfMae())) != null) {
			return true;
		}
		return false;
	}

	public void buscaPreRegistroExistentePorCpfMae(String cpfMae) throws ECivilException {
		if (Util.isValidCPF(cpfMae)) {
			if (preRegistroDAO.buscaPreregistroEmAbertoPorCpfMaePrePosto(cpfMae) != null) {
				preRegistroPrePosto = preRegistroDAO.buscaPreregistroEmAbertoPorCpfMaePrePosto(cpfMae);
				recuperaTodasListasMunicipios();
				setDisabilitaCamposCPFMae(true);
				setExibeModalBuscaPreRegistro(false);
				setDisabilitaCamposServentia(true);
				setBuscaEncontradaPreRegistroPrePosto(true);
				preencheListasStatusPai();
				preRegistroPrePosto.setSituacaoSolicitacao(SituacaoSolicitacaoUI.EM_PREPARACAO);
				historicoPreRegistroBO.setaHistoricoPreRegistro(preRegistroPrePosto,getUsuarioLogadoPortal().getId());
			} else {
				preRegistroPrePosto.setCpfMae(cpfMae);
				setDisabilitaCamposCPFMae(true);
				setExibeModalBuscaPreRegistro(false);
				Mensagem.infoSemBundle("Não existe registro em aberto para esse CPF informado.");
			}
		} else
			Mensagem.errorSemBundle("O CPF informado é inválido.");

	}

	public void recuperaTodasListasMunicipios() throws ECivilException {
		recuperaListaMunicipioPorUfNaturalidadeCriancaPrePosto(preRegistroPrePosto.getNaturalidadeUf());
		recuperaListaMunicipioPorUfNaturalidadeMaePrePosto(preRegistroPrePosto.getNaturalidadeUfMae());
		recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaMaePrePosto(preRegistroPrePosto.getResidenciaUfMae());
		recuperaListaServentiasProximas();
		recuperaListaMunicipioPorUfNaturalidadePaiPrePosto(preRegistroPrePosto.getNaturalidadeUfPai());
		recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPaiPrePosto(preRegistroPrePosto.getResidenciaUfPai());
		recuperaListaMunicipioPorUfNaturalidadeDeclarante(preRegistroPrePosto.getUfResidenciaDeclarante());
	}

	public void recuperaListaMunicipioPorUfNaturalidadeCriancaPrePosto(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosCrianca(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public void recuperaListaMunicipioPorUfNaturalidadeMaePrePosto(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosMae(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public void recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaMaePrePosto(String siglaUf) {
		if (siglaUf != null) {
			setListaResidenciaMunicipiosMae(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
			for (int i = 0; i < listaResidenciaMunicipiosMae.size(); i++) {
				listaResidenciaMunicipiosMae.get(i)
						.setNome(Util.normalizaTexto(listaResidenciaMunicipiosMae.get(i).getNome().toUpperCase()));
			}
		}
	}
	
	public void recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaDeclarantePrePosto(String siglaUf) {
		if (siglaUf != null) {
			setListaNaturalidadeMunicipiosDeclarante(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
			for (int i = 0; i < listaNaturalidadeMunicipiosDeclarante.size(); i++) {
				listaNaturalidadeMunicipiosDeclarante.get(i)
						.setNome(Util.normalizaTexto(listaNaturalidadeMunicipiosDeclarante.get(i).getNome().toUpperCase()));
			}
		}
	}

	public void recuperaListaMunicipioPorUfNaturalidadePaiPrePosto(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosPai(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public void recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPaiPrePosto(String siglaUf) {
		if (siglaUf != null) {
			setListaResidenciaMunicipiosPai(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
			for (int i = 0; i < listaResidenciaMunicipiosPai.size(); i++) {
				listaResidenciaMunicipiosPai.get(i)
						.setNome(Util.normalizaTexto(listaResidenciaMunicipiosPai.get(i).getNome().toUpperCase()));
			}
		}
	}

	public void recuperaListaServentiaPorMunicipioPrePosto() throws ECivilException {
		if (preRegistroPrePosto.getOpcaoRegistro() != null) {

			if (preRegistroPrePosto.getOpcaoRegistro().equals("Residência dos Pais")
					&& preRegistroPrePosto.getResidenciaMunicipioMae() != null) {
				setListaCartorio(
						migracaoRecompeWS
								.recuperaListaCartorioPorCodigoMunicipio(municipioDAO
										.recuperaMunicipioPorNome(
												Util.transformar_Em_Maisculo_Primeira_Letra_Cada_Palavra(
														preRegistroPrePosto.getResidenciaMunicipioMae()))
										.getCodigoRecompe()));
			} else {
				preRegistroPrePosto.setServentia("");
			}
		}
	}
	
	public void GerarCodigoHash() {
		String formulaHash=preRegistroPrePosto.getId()+"CRYPTOREC";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        BigInteger hash = new BigInteger(1, md.digest(formulaHash.getBytes()));
        preRegistroPrePosto.setCodigoHash(hash.toString(16));
    }

	public boolean verificaHoraNascimentoValidaPrePosto(String horaNascimento) {
		int hora = 0, minuto = 0;
		if (horaNascimento == null) {
			return false;
		}
		if (horaNascimento.length() == 5) {
			hora = Integer.parseInt(horaNascimento.substring(0, 2));
			minuto = Integer.parseInt(horaNascimento.substring(3, 5));
		} else {
			return false;
		}
		if (hora > 23) {
			return false;
		} else if (minuto > 59) {
			return false;
		}
		return true;
	}

	public boolean exibeModalBuscaPreRegistro() {
		if (preRegistroPrePosto.getCpfMae() == null) {
			return true;
		}
		return false;
	}

	public void consultaCEPMaePrePosto() throws ECivilException {
		cepResponse = new ConsultaCepResponse();
		if (preRegistroPrePosto.getResidenciaCEPMae() != null
				&& preRegistroPrePosto.getResidenciaCEPMae().trim().length() == 8) {
			try {
				cepResponse = integradorCorreiosWS.consultarCep(preRegistroPrePosto.getResidenciaCEPMae());
			} catch (ECivilException e) {
				Mensagem.errorSemBundle(e.getMensagemErro());
			} catch (Exception e) {
				Mensagem.errorSemBundle("Não foi possível buscar os dados do endereço a partir do CEP nesse momento.");
			}
		} else
			Mensagem.errorSemBundle("CEP inexistente.");

		if (cepResponse.getMunicipio() != null) {
			preRegistroPrePosto.setResidenciaUfMae(cepResponse.getMunicipio().getUf().getSigla());
			recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaMaePrePosto(
					cepResponse.getMunicipio().getUf().getSigla());
			preRegistroPrePosto.setResidenciaMunicipioMae(cepResponse.getMunicipio().getNome());
			preRegistroPrePosto.setResidenciaBairroMae(cepResponse.getBairro());
			preRegistroPrePosto.setResidenciaLogradouroMae(cepResponse.getLogradouro());
			recuperaListaServentiasProximas();
		} else {
			preRegistroPrePosto.setResidenciaUfMae("");
			preRegistroPrePosto.setResidenciaMunicipioMae("");
			preRegistroPrePosto.setResidenciaBairroMae("");
			preRegistroPrePosto.setResidenciaLogradouroMae("");
		}

	}

	public void aproveitarDadosResidenciaMae() throws ECivilException {
		preRegistroPrePosto.setResidenciaCEPpai(preRegistroPrePosto.getResidenciaCEPMae());
		preRegistroPrePosto.setResidenciaUfPai(preRegistroPrePosto.getResidenciaUfMae());
		recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPaiPrePosto(preRegistroPrePosto.getResidenciaUfPai());
		preRegistroPrePosto.setResidenciaMunicipioPai(preRegistroPrePosto.getResidenciaMunicipioMae());
		preRegistroPrePosto.setResidenciaBairroPai(preRegistroPrePosto.getResidenciaBairroMae());
		preRegistroPrePosto.setResidenciaLogradouroPai(preRegistroPrePosto.getResidenciaLogradouroMae());
		preRegistroPrePosto.setResidenciaDistritoPai(preRegistroPrePosto.getResidenciaDistritoMae());
		preRegistroPrePosto.setResidenciaNumeroPai(preRegistroPrePosto.getResidenciaNumeroMae());
		recuperaListaServentiasProximas();
	}

	public void consultaCEPPaiPrePosto() throws ECivilException {

		if (preRegistroPrePosto.getResidenciaCEPpai() != null
				&& preRegistroPrePosto.getResidenciaCEPpai().trim().length() == 8) {
			try {
				cepResponse = integradorCorreiosWS.consultarCep(preRegistroPrePosto.getResidenciaCEPpai());
			} catch (ECivilException e) {
				Mensagem.errorSemBundle(e.getMensagemErro());
			} catch (Exception e) {
				Mensagem.errorSemBundle("Não foi possível buscar os dados do endereço a partir do CEP nesse momento.");
			}
		} else
			Mensagem.errorSemBundle("CEP inexistente.");

		if (cepResponse != null) {
			preRegistroPrePosto.setResidenciaUfPai(cepResponse.getMunicipio().getUf().getSigla());
			recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPaiPrePosto(
					cepResponse.getMunicipio().getUf().getSigla());
			preRegistroPrePosto.setResidenciaMunicipioPai(cepResponse.getMunicipio().getNome());
			preRegistroPrePosto.setResidenciaBairroPai(cepResponse.getBairro());
			preRegistroPrePosto.setResidenciaLogradouroPai(cepResponse.getLogradouro());
			recuperaListaServentiasProximas();
		} else {
			preRegistroPrePosto.setResidenciaUfPai(null);
			preRegistroPrePosto.setResidenciaMunicipioPai(null);
			preRegistroPrePosto.setResidenciaBairroPai("");
			preRegistroPrePosto.setResidenciaLogradouroPai("");
		}
	}
	
	public void consultaCEPDeclarantePrePosto() throws ECivilException {
		cepResponse = new ConsultaCepResponse();
		if (preRegistroPrePosto.getCepDeclarante() != null
				&& preRegistroPrePosto.getCepDeclarante().trim().length() == 8) {
			try {
				cepResponse = integradorCorreiosWS.consultarCep(preRegistroPrePosto.getCepDeclarante());
			} catch (ECivilException e) {
				Mensagem.errorSemBundle(e.getMensagemErro());
			} catch (Exception e) {
				Mensagem.errorSemBundle("Não foi possível buscar os dados do endereço a partir do CEP nesse momento.");
			}
		} else
			Mensagem.errorSemBundle("CEP inexistente.");

		if (cepResponse.getMunicipio() != null) {
			preRegistroPrePosto.setUfResidenciaDeclarante(cepResponse.getMunicipio().getUf().getSigla());
			recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaDeclarantePrePosto(
					cepResponse.getMunicipio().getUf().getSigla());
			preRegistroPrePosto.setMunicipioResidenciaDeclarante(cepResponse.getMunicipio().getNome());
			preRegistroPrePosto.setBairroResidenciaDeclarante(cepResponse.getBairro());
			preRegistroPrePosto.setLogradouroResidenciaDeclarante(cepResponse.getLogradouro());
		} else {
			preRegistroPrePosto.setUfResidenciaDeclarante("");
			preRegistroPrePosto.setMunicipioResidenciaDeclarante("");
			preRegistroPrePosto.setBairroResidenciaDeclarante("");
			preRegistroPrePosto.setLogradouroResidenciaDeclarante("");
		}

	}

	public void recuperaListaServentiasProximas() throws ECivilException {
		if (preRegistroPrePosto.getPaisResidenciaServentiaEscolhida().equals("Mãe")) {
			recuperaListaServentiaPorMunicipioMae();
		} else if (preRegistroPrePosto.getPaisResidenciaServentiaEscolhida().equals("Pai")) {
			recuperaListaServentiaPorMunicipioPai();
		}
	}

	public void recuperaListaServentiaPorMunicipioMae() throws ECivilException {
		if (preRegistroPrePosto.getOpcaoRegistro() != null) {

			if (preRegistroPrePosto.getOpcaoRegistro().equals("Residência dos Pais")
					&& preRegistroPrePosto.getResidenciaMunicipioMae() != null) {
				setListaServentiasProximas(RelacaoBairroServentiaDAO
						.listaServentiasProximasResidenciaPais(preRegistroPrePosto.getResidenciaBairroMae()));
				int tamanhoListaServentias = getListaServentiasProximas().size();
				List<CartorioResponse> listaExibeServentiasProximas = new ArrayList<CartorioResponse>();
				if (tamanhoListaServentias > 0) {
					for (int i = 0; i < tamanhoListaServentias; i++) {
						CartorioResponse cartorioResponse = new CartorioResponse();
						cartorioResponse.setBairro(getListaServentiasProximas().get(i).getBairro());
						cartorioResponse.setCodigoCorregedoria(
								getListaServentiasProximas().get(i).getCodigoCorregedoriaServentia());
						cartorioResponse.setNomeDistritoECartorio(getListaServentiasProximas().get(i).getServentia());
						listaExibeServentiasProximas.add(cartorioResponse);
					}
					setListaCartorio(listaExibeServentiasProximas);
				} else {
					if (cepResponse == null || cepResponse.getCep() == null)
						consultaCEPMaePrePosto();
					try {
						setListaCartorio(
								migracaoRecompeWS
										.recuperaListaCartorioPorCodigoMunicipio(
												municipioDAO
														.recuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull(
																cepResponse.getMunicipio().getCodigoRfb())
														.getCodigoRecompe()));
					} catch (Exception e) {

					}

				}
			}
		} else {
			preRegistroPrePosto.setServentia("");
			setListaCartorio(new ArrayList<CartorioResponse>());
		}
	}

	

	public void recuperaListaServentiaPorMunicipioPai() throws ECivilException {
		if (preRegistroPrePosto.getOpcaoRegistro() != null) {

			if (preRegistroPrePosto.getOpcaoRegistro().equals("Residência dos Pais")
					&& preRegistroPrePosto.getResidenciaMunicipioPai() != null) {
				setListaServentiasProximas(RelacaoBairroServentiaDAO
						.listaServentiasProximasResidenciaPais(preRegistroPrePosto.getResidenciaBairroPai()));
				int tamanhoListaServentias = getListaServentiasProximas().size();
				List<CartorioResponse> listaExibeServentiasProximas = new ArrayList<CartorioResponse>();
				if (tamanhoListaServentias > 0) {
					for (int i = 0; i < tamanhoListaServentias; i++) {
						CartorioResponse cartorioResponse = new CartorioResponse();
						cartorioResponse.setBairro(getListaServentiasProximas().get(i).getBairro());
						cartorioResponse.setCodigoCorregedoria(
								getListaServentiasProximas().get(i).getCodigoCorregedoriaServentia());
						cartorioResponse.setNomeDistritoECartorio(getListaServentiasProximas().get(i).getServentia());
						listaExibeServentiasProximas.add(cartorioResponse);
					}
					setListaCartorio(listaExibeServentiasProximas);
				} else {
					if (cepResponse == null || cepResponse.getCep() == null)
						consultaCEPPaiPrePosto();
					try {
						setListaCartorio(
								migracaoRecompeWS
										.recuperaListaCartorioPorCodigoMunicipio(
												municipioDAO
														.recuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull(
																cepResponse.getMunicipio().getCodigoRfb())
														.getCodigoRecompe()));
					} catch (Exception e) {

					}
				}
			} else {
				preRegistroPrePosto.setServentia("");
				setListaCartorio(new ArrayList<CartorioResponse>());
			}
		}
	}

	public void preecheListaPaisResidenciaProximaServentia() {
		setListaPaisResidenciaMaisProximaServentia(new ArrayList<String>());
		if (preRegistroPrePosto.getStatusPai().equals("1")) {
			listaPaisResidenciaMaisProximaServentia.add("Mãe");
			listaPaisResidenciaMaisProximaServentia.add("Pai");
		} else {
			listaPaisResidenciaMaisProximaServentia.add("Mãe");
			preRegistroPrePosto.setPaisResidenciaServentiaEscolhida("Mãe");
		}
	}

	public void preencheListasStatusPai() throws ECivilException {
		preecheListaDeclarante();
		preecheListaPaisResidenciaProximaServentia();
		recuperaListaServentiasProximas();
	}

	public void preecheListaDeclarante() {
		setListaPessoaDeclarante(new ArrayList<String>());
		if (preRegistroPrePosto.getStatusPai().equals("1")) {
			listaPessoaDeclarante.add("Mãe");
			listaPessoaDeclarante.add("Pai");
			listaPessoaDeclarante.add("Outro");
		} else {
			listaPessoaDeclarante.add("Mãe");
			listaPessoaDeclarante.add("Outro");
		}
	}

	public void preecheListaPessoaTipoDeclaracao() {
		setListaPessoaTipoDeclaracao(new ArrayList<String>());
		if (tipoDeclaranteOutro.equals("3")) {
			listaPessoaTipoDeclaracao.add("Representando o pai");
		} else if (tipoDeclaranteOutro.equals("4")) {
			listaPessoaTipoDeclaracao.add("Representando a mãe");
		} else if (tipoDeclaranteOutro.equals("9")) {
			listaPessoaTipoDeclaracao.add("Assistido(a) pelo Tutor ou responsável");
			listaPessoaTipoDeclaracao.add("Autorizado pelo Juiz");
			listaPessoaTipoDeclaracao.add("Por procuração");
			listaPessoaTipoDeclaracao.add("Declarante emancipado");
			listaPessoaTipoDeclaracao.add("Mãe Presente");
			listaPessoaTipoDeclaracao.add("Mãe Ausente");
			listaPessoaTipoDeclaracao.add("Mãe ausente com documentos");
			listaPessoaTipoDeclaracao.add("Mandado");
			listaPessoaTipoDeclaracao.add("Declarante Analfabeto");
		} else {
			listaPessoaTipoDeclaracao.add("Sem restrição");
		}
	}
}
