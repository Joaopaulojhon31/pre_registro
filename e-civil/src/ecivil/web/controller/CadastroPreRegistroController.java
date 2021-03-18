package ecivil.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.HistoricoPreRegistroBO;
import ecivil.ejb.dao.MunicipioDAO;
import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.dao.RelacaoBairroServentiaDAO;
import ecivil.ejb.dao.UfDAO;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.RelacaoBairroServentia;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;
import ecivil.ejb.entidade.Uf;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.IntegradorCorreiosWS;
import ecivil.ejb.ws.cliente.rest.MigracaoRecompeWS;
import ecivil.ejb.ws.cliente.rest.response.integradorcorreios.ConsultaCepResponse;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import ecivil.web.util.Mensagem;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class CadastroPreRegistroController extends BaseController implements Serializable {

	@EJB
	private UfDAO ufDAO;

	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;

	@EJB
	private RelacaoBairroServentiaDAO RelacaoBairroServentiaDAO;

	@EJB
	private MunicipioDAO municipioDAO;

	@EJB
	private MigracaoRecompeWS migracaoRecompeWS;

	@EJB
	private IntegradorCorreiosWS integradorCorreiosWS;

	public CartorioLookUp getCartorioLookUp() {
		return cartorioLookUp;
	}

	public void setCartorioLookUp(CartorioLookUp cartorioLookUp) {
		this.cartorioLookUp = cartorioLookUp;
	}

	@EJB
	private PreRegistroDAO preRegistroDAO;

	@EJB
	private CartorioLookUp cartorioLookUp;

	private List<CartorioResponse> listaCartorio;
	private List<String> listaPessoaDeclarantePreRegistro;
	private List<String> listaPaisResidenciaMaisProximaServentia;
	private PreRegistro cadastroPreRegistro;
	private ConsultaCepResponse cepResponse;
	private List<Municipio> listaNaturalidadeMunicipiosCrianca;
	private List<Municipio> listaNaturalidadeMunicipiosMae;
	private List<Municipio> listaResidenciaMunicipiosMae;
	private List<Municipio> listaNaturalidadeMunicipiosPai;
	private List<Municipio> listaResidenciaMunicipiosPai;
	private List<PreRegistro> listaPreRegistroPorCpfMae;
	private List<RelacaoBairroServentia> listaServentiasProximas;
	private VisualizarPreRegistroController visualizaPreRegistro;
	private boolean telaRedirecionadaVisualizaPreRegistro;
	private boolean verificaCheckBoxConfirmarPreRegistro;

	@PostConstruct
	public void inicializa() throws Exception {
		setTelaRedirecionadaVisualizaPreRegistro(false);
		setVerificaCheckBoxConfirmarPreRegistro(false);
		if (VisualizarPreRegistroController.isRedirecionaPreRegistro()) {
			setCadastroPreRegistro(preRegistroDAO.buscaPreregistroEmAbertoPorCpfMae(getUsuarioLogadoPortal().getCpf()));
			VisualizarPreRegistroController.setRedirecionaPreRegistro(false);
			recuperaTodasListasMunicipios();
			preencheListasStatusPai();
			setTelaRedirecionadaVisualizaPreRegistro(true);
			recuperaListaServentiasProximas();

		} else {
			setCadastroPreRegistro(new PreRegistro());
			getListaUf();
			getCadastroPreRegistro().setOpcaoRegistro("Residência dos Pais");
			getCadastroPreRegistro().setPaisResidenciaServentiaEscolhida("Mãe");
			getCadastroPreRegistro().setStatusPai("Declarado");
			preencheListasStatusPai();
		}

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

	public PreRegistroDAO getPreRegistroDAO() {
		return preRegistroDAO;
	}

	public void setPreRegistroDAO(PreRegistroDAO preRegistroDAO) {
		this.preRegistroDAO = preRegistroDAO;
	}

	public List<Municipio> getListaNaturalidadeMunicipiosPai() {
		return listaNaturalidadeMunicipiosPai;
	}

	public void setListaNaturalidadeMunicipiosPai(List<Municipio> listaNaturalidadeMunicipiosPai) {
		this.listaNaturalidadeMunicipiosPai = listaNaturalidadeMunicipiosPai;
	}

	public List<PreRegistro> getListaPreRegistroPorCpfMae() {
		return listaPreRegistroPorCpfMae;
	}

	public void setListaPreRegistroPorCpfMae(List<PreRegistro> listaPreRegistroPorCpfMae) {
		this.listaPreRegistroPorCpfMae = listaPreRegistroPorCpfMae;
	}

	public List<Municipio> getListaResidenciaMunicipiosPai() {
		return listaResidenciaMunicipiosPai;
	}

	public void setListaResidenciaMunicipiosPai(List<Municipio> listaResidenciaMunicipiosPai) {
		this.listaResidenciaMunicipiosPai = listaResidenciaMunicipiosPai;
	}

	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}

	public MunicipioDAO getMunicipioDAO() {
		return municipioDAO;
	}

	public void setMunicipioDAO(MunicipioDAO municipioDAO) {
		this.municipioDAO = municipioDAO;
	}

	public UfDAO getUfDAO() {
		return ufDAO;
	}

	public void setUfDAO(UfDAO ufDAO) {
		this.ufDAO = ufDAO;
	}

	public List<Uf> getListaUf() {
		return ufDAO.recuperaListaUF();
	}

	public PreRegistro getCadastroPreRegistro() {
		return cadastroPreRegistro;
	}

	public void setCadastroPreRegistro(PreRegistro cadastroPreRegistro) {
		this.cadastroPreRegistro = cadastroPreRegistro;
	}

	public VisualizarPreRegistroController getVisualizaPreRegistro() {
		return visualizaPreRegistro;
	}

	public List<String> getListaPessoaDeclarantePreRegistro() {
		return listaPessoaDeclarantePreRegistro;
	}

	public void setListaPessoaDeclarantePreRegistro(List<String> listaPessoaDeclarantePreRegistro) {
		this.listaPessoaDeclarantePreRegistro = listaPessoaDeclarantePreRegistro;
	}

	public void setVisualizaPreRegistro(VisualizarPreRegistroController visualizaPreRegistro) {
		this.visualizaPreRegistro = visualizaPreRegistro;
	}

	public boolean isTelaRedirecionadaVisualizaPreRegistro() {
		return telaRedirecionadaVisualizaPreRegistro;
	}

	public void setTelaRedirecionadaVisualizaPreRegistro(boolean telaRedirecionadaVisualizaPreRegistro) {
		this.telaRedirecionadaVisualizaPreRegistro = telaRedirecionadaVisualizaPreRegistro;
	}

	public List<String> getListaPaisResidenciaMaisProximaServentia() {
		return listaPaisResidenciaMaisProximaServentia;
	}

	public void setListaPaisResidenciaMaisProximaServentia(List<String> listaPaisResidenciaMaisProximaServentia) {
		this.listaPaisResidenciaMaisProximaServentia = listaPaisResidenciaMaisProximaServentia;
	}

	public List<RelacaoBairroServentia> getListaServentiasProximas() {
		return listaServentiasProximas;
	}

	public void setListaServentiasProximas(List<RelacaoBairroServentia> listaServentiasProximas) {
		this.listaServentiasProximas = listaServentiasProximas;
	}

	public boolean isVerificaCheckBoxConfirmarPreRegistro() {
		return verificaCheckBoxConfirmarPreRegistro;
	}

	public void setVerificaCheckBoxConfirmarPreRegistro(boolean verificaCheckBoxConfirmarPreRegistro) {
		this.verificaCheckBoxConfirmarPreRegistro = verificaCheckBoxConfirmarPreRegistro;
	}

	public void gravaDadosPreRegistro() throws ECivilException {
		if (verificaPreenchimentoCamposCadastroPreRegistro()) {
			if (isTelaRedirecionadaVisualizaPreRegistro()) {
				cadastroPreRegistro.setDataAlteracaoSolicitacao(getPreRegistroDAO().retornaDataBanco());
				gravaCodigoCnsCartorioPreRegistro();
				historicoPreRegistroBO.setaHistoricoPreRegistro(cadastroPreRegistro,
						getUsuarioLogadoPortal().getId());
				setCadastroPreRegistro(new PreRegistro());
				setTelaRedirecionadaVisualizaPreRegistro(false);
				Mensagem.infoSemBundle("Pré-Registro atualizado com sucesso.");

			} else {
				if (verificaExistenciaPreRegistroPorCpfMae()) {
					Mensagem.errorSemBundle("Já existe um Pré-Registro em aberto para o CPF da mãe que foi inserido.");
				} else {
					gravaInformacoesComplementaresPreRegistro();
					historicoPreRegistroBO.setaHistoricoPreRegistro(cadastroPreRegistro,
							getUsuarioLogadoPortal().getId());
					setCadastroPreRegistro(new PreRegistro());
					Mensagem.infoSemBundle("Cadastro do Pré-Registro efetuado com sucesso.");
				}
			}
		}
	}

	public void gravaCodigoCnsCartorioPreRegistro() throws ECivilException {
		if (!cadastroPreRegistro.getCodCorregedoriaCartorio().isEmpty()) {
			cadastroPreRegistro.setCnsCartorio(migracaoRecompeWS
					.recuperaCartorioPorCodigoCorregedoria(cadastroPreRegistro.getCodCorregedoriaCartorio()).getCns());
		}
	}

	public void gravaInformacoesComplementaresPreRegistro() throws ECivilException {
		if (cadastroPreRegistro.getTipoDeclarantePreRegistro().equals("Mãe")) {
			cadastroPreRegistro.setNomeDeclarantePreRegistro(cadastroPreRegistro.getNomeMae());
			cadastroPreRegistro.setCpfDeclarantePreRegistro(cadastroPreRegistro.getCpfMae());
			cadastroPreRegistro.setContatoDeclarantePreRegistro(cadastroPreRegistro.getContatoMae());
		} else if (cadastroPreRegistro.getTipoDeclarantePreRegistro().equals("Pai")) {
			cadastroPreRegistro.setNomeDeclarantePreRegistro(cadastroPreRegistro.getNomePai());
			cadastroPreRegistro.setCpfDeclarantePreRegistro(cadastroPreRegistro.getCpfPai());
			cadastroPreRegistro.setContatoDeclarantePreRegistro(cadastroPreRegistro.getContatoPai());
		}
		cadastroPreRegistro.setSituacaoSolicitacao(SituacaoSolicitacaoUI.INICIAL);
		gravaCodigoCnsCartorioPreRegistro();
		cadastroPreRegistro.setIdUsuarioPortalExterno(getUsuarioLogadoPortal().getId());
		cadastroPreRegistro.setDataInicioSolicitacao(getPreRegistroDAO().retornaDataBanco());
		cadastroPreRegistro.setDataAlteracaoSolicitacao(getPreRegistroDAO().retornaDataBanco());
	}

	public boolean verificaPreenchimentoCamposCadastroPreRegistro() {
		if (!verificaPreenchimentoCamposCrianca()) {
			return false;
		} else if (!verificaPreenchimentoCamposMae()) {
			return false;
		} else if (!verificaPreenchimentoCamposPai()) {
			return false;
		} else if (!verificaPreenchimentoCamposDeclarante()) {
			return false;
		} else if (!verificaPreenchimentoCamposCartorio()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean verificaPreenchimentoCamposCrianca() {
		if (cadastroPreRegistro.getNaturalidadeUf() == null) {
			Mensagem.errorSemBundle("O campo Naturalidade-UF da criança está vazio.");
			return false;
		} else if (cadastroPreRegistro.getNaturalidade() == null) {
			Mensagem.errorSemBundle("O campo Naturalidade-Município da criança está vazio.");
			return false;
		} else if (cadastroPreRegistro.getPrevisaoNascimento().isEmpty()) {
			Mensagem.errorSemBundle("O campo Previsão de nascimento da criança está vazio.");
			return false;
		}
		return true;

	}

	private boolean verificaPreenchimentoCamposMae() {
		if (cadastroPreRegistro.getNomeMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nome da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getCpfMae() == null) {
			Mensagem.errorSemBundle("O campo CPF da mãe está vazio.");
			return false;
		} else if (!Util.isValidCPF(cadastroPreRegistro.getCpfMae())) {
			Mensagem.errorSemBundle("O campo CPF da mãe é inválido.");
			return false;
		} else if (cadastroPreRegistro.getContatoMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Telefone de Contato da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getNacionalidadeMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Nacionalidade da mãe está vazio.");
			return false;
		} else if (Util.isNullOrEmpty(cadastroPreRegistro.getResidenciaCEPMae())) {
			Mensagem.errorSemBundle("O campo CEP mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getResidenciaUfMae() == null) {
			Mensagem.errorSemBundle("O campo Residência-UF da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getResidenciaMunicipioMae() == null) {
			Mensagem.errorSemBundle("O campo Residência-Município da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getResidenciaBairroMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Residência-Bairro da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getResidenciaLogradouroMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Residência-Logradouro da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getResidenciaNumeroMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Residência-Número da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getProfissaoMae().isEmpty()) {
			Mensagem.errorSemBundle("O campo Profissão da mãe está vazio.");
			return false;
		} else if (cadastroPreRegistro.getDataNascimentoMae() == null) {
			Mensagem.errorSemBundle("O campo Data de Nascimento da mãe está vazio.");
			return false;
		}
		cadastroPreRegistro.setIdadeMae(Util.CalculaIdade(cadastroPreRegistro.getDataNascimentoMae()));

		return true;
	}

	private boolean verificaPreenchimentoCamposPai() {
		if (cadastroPreRegistro.getStatusPai().equals("Declarado")) {
			if (cadastroPreRegistro.getNomePai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Nome do Pai está vazio.");
				return false;
			} else if (!Util.isValidCPF(cadastroPreRegistro.getCpfPai()) && cadastroPreRegistro.getCpfPai() != null) {
				Mensagem.errorSemBundle("O campo CPF do Pai é inválido");
				return false;
			} else if (cadastroPreRegistro.getNacionalidadePai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Nacionalidade do pai está vazio.");
				return false;
			} else if (Util.isNullOrEmpty(cadastroPreRegistro.getResidenciaCEPpai())) {
				Mensagem.errorSemBundle("O campo CEP do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getResidenciaUfPai() == null) {
				Mensagem.errorSemBundle("O campo Residência-UF do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getResidenciaMunicipioPai() == null) {
				Mensagem.errorSemBundle("O campo Residência-Município do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getResidenciaBairroPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Residência-Bairro do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getResidenciaLogradouroPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Residência-Logradouro do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getResidenciaNumeroPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Residência-Número do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getProfissaoPai().isEmpty()) {
				Mensagem.errorSemBundle("O campo Profissão do pai está vazio.");
				return false;
			} else if (cadastroPreRegistro.getDataNascimentoPai() == null) {
				Mensagem.errorSemBundle("O campo Data de Nascimento do pai está vazio.");
				return false;
			}
			cadastroPreRegistro.setIdadePai(Util.CalculaIdade(cadastroPreRegistro.getDataNascimentoPai()));
		} else if (cadastroPreRegistro.getStatusPai().equals("Suposto")) {
			if (!Util.isValidCPF(cadastroPreRegistro.getCpfPai()) && cadastroPreRegistro.getCpfPai() != null) {
				Mensagem.errorSemBundle("O campo CPF do Pai é inválido");
				return false;
			}
			if (cadastroPreRegistro.getDataNascimentoPai() != null) {
				cadastroPreRegistro.setIdadePai(Util.CalculaIdade(cadastroPreRegistro.getDataNascimentoPai()));
			}
		}
		return true;
	}

	private boolean verificaPreenchimentoCamposDeclarante() {
		if (cadastroPreRegistro.getTipoDeclarantePreRegistro().equals("Outro")) {

			if (cadastroPreRegistro.getNomeDeclarantePreRegistro().isEmpty()) {
				Mensagem.errorSemBundle("O campo Nome do declarante está vazio.");
				return false;
			} else if (!Util.isValidCPF(cadastroPreRegistro.getCpfDeclarantePreRegistro())
					&& cadastroPreRegistro.getCpfDeclarantePreRegistro() != null) {
				Mensagem.errorSemBundle("O campo CPF do declarante está vazio.");
				return false;
			} /*
				 * else if (cadastroPreRegistro.getContatoDeclarantePreRegistro()==null) {
				 * Mensagem.errorSemBundle("O campo Telefone do declarante está vazio."); return
				 * false; }
				 */
		}
		return true;

	}

	private boolean verificaPreenchimentoCamposCartorio() {

		if (cadastroPreRegistro.getOpcaoRegistro() == null) {
			Mensagem.errorSemBundle("O campo Opção de Registro está vazio.");
			return false;
		} else if (cadastroPreRegistro.getOpcaoRegistro().equals("Residência dos Pais")
				&& Util.isNullOrEmpty(cadastroPreRegistro.getCodCorregedoriaCartorio())) {
			Mensagem.errorSemBundle("O campo de escolha do cartório está vazio.");
			return false;
		} else if (cadastroPreRegistro.getOpcaoRegistro().equals("Local de Nascimento -Unidade Interligada")) {
			cadastroPreRegistro.setPaisResidenciaServentiaEscolhida("");
			cadastroPreRegistro.setCodCorregedoriaCartorio("");
		}
		return true;
	}

	public boolean verificaExistenciaPreRegistroPorCpfMae() {
		if ((preRegistroDAO.buscaPreregistroEmAbertoPorCpfMae(cadastroPreRegistro.getCpfMae())) != null) {
			return true;
		}
		return false;
	}

	public void recuperaListaMunicipioPorUfNaturalidadeCrianca(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosCrianca(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public void recuperaListaMunicipioPorUfNaturalidadeMae(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosMae(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public void recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaMae(String siglaUf) {
		if (siglaUf != null) {
			setListaResidenciaMunicipiosMae(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
			for (int i = 0; i < listaResidenciaMunicipiosMae.size(); i++) {
				listaResidenciaMunicipiosMae.get(i)
						.setNome(Util.normalizaTexto(listaResidenciaMunicipiosMae.get(i).getNome().toUpperCase()));
			}
		}
	}

	public void recuperaListaMunicipioPorUfNaturalidadePai(String siglaUf) {
		if (siglaUf != null) {

			setListaNaturalidadeMunicipiosPai(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
		}
	}

	public void recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPai(String siglaUf) {
		if (siglaUf != null) {

			setListaResidenciaMunicipiosPai(municipioDAO.listaTodosMunicipioPorSiglaUf(siglaUf));
			for (int i = 0; i < listaResidenciaMunicipiosPai.size(); i++) {
				listaResidenciaMunicipiosPai.get(i)
						.setNome(Util.normalizaTexto(listaResidenciaMunicipiosPai.get(i).getNome().toUpperCase()));
			}
		}
	}

	public void recuperaListaServentiasProximas() throws ECivilException {
		if (cadastroPreRegistro.getPaisResidenciaServentiaEscolhida().equals("Mãe")) {
			recuperaListaServentiaPorMunicipioMae();
		} else if (cadastroPreRegistro.getPaisResidenciaServentiaEscolhida().equals("Pai")) {
			recuperaListaServentiaPorMunicipioPai();
		}
	}

	public void recuperaListaServentiaPorMunicipioMae() throws ECivilException {
		if (cadastroPreRegistro.getOpcaoRegistro() != null) {

			if (cadastroPreRegistro.getOpcaoRegistro().equals("Residência dos Pais")
					&& cadastroPreRegistro.getResidenciaMunicipioMae() != null) {
				setListaServentiasProximas(RelacaoBairroServentiaDAO
						.listaServentiasProximasResidenciaPais(cadastroPreRegistro.getResidenciaBairroMae()));
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
					if (cepResponse == null)
						consultaCEPMae();

					setListaCartorio(

							migracaoRecompeWS
									.recuperaListaCartorioPorCodigoMunicipio(
											municipioDAO
													.recuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull(
															cepResponse.getMunicipio().getCodigoRfb())
													.getCodigoRecompe()));
				}
			} else {
				cadastroPreRegistro.setServentia("");
				setListaCartorio(new ArrayList<CartorioResponse>());
			}
		}
	}

	public void recuperaListaServentiaPorMunicipioPai() throws ECivilException {
		if (cadastroPreRegistro.getOpcaoRegistro() != null) {

			if (cadastroPreRegistro.getOpcaoRegistro().equals("Residência dos Pais")
					&& cadastroPreRegistro.getResidenciaMunicipioPai() != null) {
				setListaServentiasProximas(RelacaoBairroServentiaDAO
						.listaServentiasProximasResidenciaPais(cadastroPreRegistro.getResidenciaBairroPai()));
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
					if (cepResponse.equals(null))
						consultaCEPPai();

					setListaCartorio(

							migracaoRecompeWS
									.recuperaListaCartorioPorCodigoMunicipio(
											municipioDAO
													.recuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull(
															cepResponse.getMunicipio().getCodigoRfb())
													.getCodigoRecompe()));
				}
			} else {
				cadastroPreRegistro.setServentia("");
				setListaCartorio(new ArrayList<CartorioResponse>());
			}
		}
	}

	public void consultaCEPMae() throws ECivilException {
		cepResponse = new ConsultaCepResponse();
		if (cadastroPreRegistro.getResidenciaCEPMae() != null
				&& cadastroPreRegistro.getResidenciaCEPMae().trim().length() == 8) {
			try {
				cepResponse = integradorCorreiosWS.consultarCep(cadastroPreRegistro.getResidenciaCEPMae());
			} catch (ECivilException e) {
				Mensagem.errorSemBundle(e.getMensagemErro());
			} catch (Exception e) {
				Mensagem.errorSemBundle("Não foi possível buscar os dados do endereço a partir do CEP nesse momento.");
			}
		} else
			Mensagem.errorSemBundle("CEP inexistente.");

		if (cepResponse.getMunicipio() != null) {
			cadastroPreRegistro.setResidenciaUfMae(cepResponse.getMunicipio().getUf().getSigla());
			recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaMae(cepResponse.getMunicipio().getUf().getSigla());
			cadastroPreRegistro.setResidenciaMunicipioMae(cepResponse.getMunicipio().getNome());
			cadastroPreRegistro.setResidenciaBairroMae(cepResponse.getBairro());
			cadastroPreRegistro.setResidenciaLogradouroMae(cepResponse.getLogradouro());
			recuperaListaServentiasProximas();
		} else {
			cadastroPreRegistro.setResidenciaUfMae("");
			cadastroPreRegistro.setResidenciaMunicipioMae("");
			cadastroPreRegistro.setResidenciaBairroMae("");
			cadastroPreRegistro.setResidenciaLogradouroMae("");
		}

	}

	public void consultaCEPPai() {

		if (cadastroPreRegistro.getResidenciaCEPpai() != null
				&& cadastroPreRegistro.getResidenciaCEPpai().trim().length() == 8) {
			try {
				cepResponse = integradorCorreiosWS.consultarCep(cadastroPreRegistro.getResidenciaCEPpai());
			} catch (ECivilException e) {
				Mensagem.errorSemBundle(e.getMensagemErro());
			} catch (Exception e) {
				Mensagem.errorSemBundle("Não foi possível buscar os dados do endereço a partir do CEP nesse momento.");
			}
		} else
			Mensagem.errorSemBundle("CEP inexistente.");

		if (cepResponse != null) {
			cadastroPreRegistro.setResidenciaUfPai(cepResponse.getMunicipio().getUf().getSigla());
			recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPai(cepResponse.getMunicipio().getUf().getSigla());
			cadastroPreRegistro.setResidenciaMunicipioPai(cepResponse.getMunicipio().getNome());
			cadastroPreRegistro.setResidenciaBairroPai(cepResponse.getBairro());
			cadastroPreRegistro.setResidenciaLogradouroPai(cepResponse.getLogradouro());
		} else {
			cadastroPreRegistro.setResidenciaUfPai(null);
			cadastroPreRegistro.setResidenciaMunicipioPai(null);
			cadastroPreRegistro.setResidenciaBairroPai("");
			cadastroPreRegistro.setResidenciaLogradouroPai("");
		}

	}

	public void recuperaTodasListasMunicipios() throws ECivilException {
		recuperaListaMunicipioPorUfNaturalidadeCrianca(cadastroPreRegistro.getNaturalidadeUf());
		recuperaListaMunicipioPorUfNaturalidadeMae(cadastroPreRegistro.getNaturalidadeUfMae());
		recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaMae(cadastroPreRegistro.getResidenciaUfMae());
		recuperaListaServentiasProximas();
		recuperaListaMunicipioPorUfNaturalidadePai(cadastroPreRegistro.getNaturalidadeUfPai());
		recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPai(cadastroPreRegistro.getResidenciaUfPai());
	}

	public void preencheListasStatusPai() throws ECivilException {
		preecheListaDeclarantePreRegistro();
		preecheListaPaisResidenciaProximaServentia();
		recuperaListaServentiasProximas();
	}

	public void preecheListaDeclarantePreRegistro() {
		setListaPessoaDeclarantePreRegistro(new ArrayList<String>());
		if (cadastroPreRegistro.getStatusPai().equals("Declarado")) {
			listaPessoaDeclarantePreRegistro.add("Mãe");
			listaPessoaDeclarantePreRegistro.add("Pai");
			listaPessoaDeclarantePreRegistro.add("Outro");
		} else {
			listaPessoaDeclarantePreRegistro.add("Mãe");
			listaPessoaDeclarantePreRegistro.add("Outro");
		}
	}

	public void preecheListaPaisResidenciaProximaServentia() {
		setListaPaisResidenciaMaisProximaServentia(new ArrayList<String>());
		if (cadastroPreRegistro.getStatusPai().equals("Declarado")) {
			listaPaisResidenciaMaisProximaServentia.add("Mãe");
			listaPaisResidenciaMaisProximaServentia.add("Pai");
		} else {
			listaPaisResidenciaMaisProximaServentia.add("Mãe");
			cadastroPreRegistro.setPaisResidenciaServentiaEscolhida("Mãe");
		}
	}

	public void aproveitarDadosResidenciaMae() throws ECivilException {
		cadastroPreRegistro.setResidenciaCEPpai(cadastroPreRegistro.getResidenciaCEPMae());
		cadastroPreRegistro.setResidenciaUfPai(cadastroPreRegistro.getResidenciaUfMae());
		recuperaListaMunicipioMaiusculoSemAcentoPorUfResidenciaPai(cadastroPreRegistro.getResidenciaUfPai());
		cadastroPreRegistro.setResidenciaMunicipioPai(cadastroPreRegistro.getResidenciaMunicipioMae());
		cadastroPreRegistro.setResidenciaBairroPai(cadastroPreRegistro.getResidenciaBairroMae());
		cadastroPreRegistro.setResidenciaLogradouroPai(cadastroPreRegistro.getResidenciaLogradouroMae());
		cadastroPreRegistro.setResidenciaDistritoPai(cadastroPreRegistro.getResidenciaDistritoMae());
		cadastroPreRegistro.setResidenciaNumeroPai(cadastroPreRegistro.getResidenciaNumeroMae());
		recuperaListaServentiasProximas();
	}

	public boolean exibeButtonExibirModalConfirmarPreRegistro() {
		if (cadastroPreRegistro.getOpcaoRegistro() != null) {
			if (cadastroPreRegistro.getOpcaoRegistro().equals("Local de Nascimento -Unidade Interligada")) {
				return true;
			}
		}
		return false;
	}

	public void desabilitaBotaoManterServentiaPreRegistro() {
		setVerificaCheckBoxConfirmarPreRegistro(false);
	}
}
