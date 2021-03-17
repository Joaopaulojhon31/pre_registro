package ecivil.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.lowagie.text.pdf.ArabicLigaturizer;

import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.web.util.Mensagem;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class VisualizarPreRegistroController extends BaseController implements Serializable {

	@EJB
	private PreRegistroDAO preRegistroDAO;
	
	private List<PreRegistro> listaPreRegistroTelaVisualizar;
	private PreRegistro preRegistro;
	private PrincipalController principalController;	
	private UsuarioPortalExterno usuarioPortalExterno;
	private static boolean redirecionaPreRegistro;
	
	@PostConstruct
	public void inicializa() throws Exception {
		preechelistaPreRegistroVisualizar();
		setPrincipalController(new PrincipalController());
		setRedirecionaPreRegistro(false);
	}
	
	public PreRegistro getPreRegistro() {
		return preRegistro;
	}

	public void setPreRegistro(PreRegistro preRegistro) {
		this.preRegistro = preRegistro;
	}

	public PrincipalController getPrincipalController() {
		return principalController;
	}

	public void setPrincipalController(PrincipalController principalController) {
		this.principalController = principalController;
	}
	public UsuarioPortalExterno getUsuarioPortalExterno() {
		return usuarioPortalExterno;
	}

	public void setUsuarioPortalExterno(UsuarioPortalExterno usuarioPortalExterno) {
		this.usuarioPortalExterno = usuarioPortalExterno;
	}

	public List<PreRegistro> getListaPreRegistroTelaVisualizar() {
		return listaPreRegistroTelaVisualizar;
	}

	public void setListaPreRegistroTelaVisualizar(List<PreRegistro> listaPreRegistroTelaVisualizar) {
		this.listaPreRegistroTelaVisualizar = listaPreRegistroTelaVisualizar;
	}

	public static boolean isRedirecionaPreRegistro() {
		return redirecionaPreRegistro;
	}

	public static void setRedirecionaPreRegistro(boolean redirecionaPreRegistro) {
		VisualizarPreRegistroController.redirecionaPreRegistro = redirecionaPreRegistro;
	}

	public boolean exibeBotaoEditarPreRegistro(String situacao, String cpfMae) {
		if(situacao.equals("8") && cpfMae.equals(getUsuarioLogadoPortal().getCpf())) {
			return true;
		}
		return false;
	}
	
	public boolean exibeNomeVisualizarPreRegistro(String nome) {
		if(nome.equals("")) {
			return false;
		}
		return true;
	}
	public void preechelistaPreRegistroVisualizar() {
		setListaPreRegistroTelaVisualizar(preRegistroDAO.buscaListaPreregistroPorIdUsuarioPortalExternoOuCpfMaeOrdenadoPorData(getUsuarioLogadoPortal().getId(),getUsuarioLogadoPortal().getCpf()));
	}
	
	public boolean exibeServentiaVisualizarPreRegistro(String serventia) {
		if(serventia.equals("")) {
			return false;
		}
		return true;
	}
	
	public String direcionaTelaEditarPreRegistro() {		
		setRedirecionaPreRegistro(true);
		return principalController.direcionaCadastroPreRegistro();
	}
	public void excluirPreRegistro() {
		preRegistro=preRegistroDAO.buscaPreregistroEmAbertoPorCpfMae(getUsuarioLogadoPortal().getCpf());
		preRegistroDAO.remove(preRegistro);
		preechelistaPreRegistroVisualizar();
		Mensagem.infoSemBundle("Pré-Registro excluído com sucesso.");
	}
}

