package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.UsuarioPortalInternoBO;
import ecivil.ejb.dao.GrupoUsuarioDAO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.UsuarioGrupoUsuario;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.entidade.UsuarioServentia;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.lookup.MunicipioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.MigracaoRecompeWS;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class CadastroUsuarioPortalInternoController extends BaseController implements Serializable {

	@EJB
	private UsuarioPortalInternoBO usuarioPortalInternoBO;
	
	@EJB
	private GrupoUsuarioDAO grupoUsuarioDAO;

	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private MunicipioLookUp municipioLookUp;
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@EJB
	private MigracaoRecompeWS migracaoRecompeWS;
	
	private UsuarioPortalInterno usuarioPortalInterno;
	private UsuarioGrupoUsuario usuarioGrupoUsuario;
	private List<Municipio> listaMunicipio;
	private List<CartorioResponse> listaCartorio;
	private List<GrupoUsuario> listaGrupoUsuario;

	@PostConstruct
	public void inicializar() {
		try {
			inicializaUsuario();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<EnumPaginas> listBreadCrumb() {
		List<EnumPaginas> bread = super.listBreadCrumb();
		bread.add(EnumPaginas.PESQUISAR_USUARIO_PORTAL_INTERNO);
		return bread;
	}
	
	private void inicializaUsuario() {
		setUsuarioPortalInterno((UsuarioPortalInterno) JSFUtil.get("usuarioPortalInterno"));
		if (getUsuarioPortalInterno() == null) {
			instanciarUsuarioPortalInterno();
		} else {
			getUsuarioPortalInterno().setConfirmarEmail(getUsuarioPortalInterno().getEmail());
		}		
	}
	
	private void instanciarUsuarioPortalInterno() {
		setUsuarioPortalInterno(new UsuarioPortalInterno());
	}

	public void gravar() {
		try {
			usuarioPortalInternoBO.validarCamposObrigatorios(getUsuarioPortalInterno());
			usuarioPortalInternoBO.grava(getUsuarioPortalInterno());
			Mensagem.infoSemBundle("Usuário gravado com sucesso.");
			redireciona(EnumPaginas.PESQUISAR_USUARIO_PORTAL_INTERNO.getUrlJsf());
		} catch (ECivilException e) {
			Mensagem.listaErros(e.getListaErros());
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível gravar/atualizar o usuário nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void buscarListaGrupoUsuario(UsuarioGrupoUsuario usuarioGrupoUsuario) {
		setUsuarioGrupoUsuario(usuarioGrupoUsuario);
		setListaGrupoUsuario(grupoUsuarioDAO.recuperarGrupoUsuariosRetirandoLista(null, recuperaListaGrupoUsuarioAdicionada()));
	}
	
	public String recuperaNomeServentia(String codigoCorregedoria) {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria);
			return cartorio != null ? cartorio.getNomeDistritoECartorio() : "-----";
		} catch (ECivilException e) {
			e.printStackTrace();
			return codigoCorregedoria;
		}
	}

	private List<Long> recuperaListaGrupoUsuarioAdicionada() {
		if (getUsuarioPortalInterno().getListaUsuarioGrupoUsuario() == null) {
			return null;
		}
		return getUsuarioPortalInterno().getListaUsuarioGrupoUsuario().stream()
				.filter(usuGrupo -> usuGrupo.getGrupoUsuario() != null && usuGrupo.getGrupoUsuario().getId() != null)
				.map(usuGrupo -> usuGrupo.getGrupoUsuario().getId()).collect(Collectors.toList());
	}

	public void adicionaGrupoUsuario(GrupoUsuario grupoUsuario) {
		if (getUsuarioGrupoUsuario() != null && grupoUsuario != null) {
			getUsuarioGrupoUsuario().setGrupoUsuario(grupoUsuario);
		}
	}
	
	public void adicionarNovoGrupoUsuario() {
		getUsuarioPortalInterno().instanciaUsuarioGrupoUsuario();
	}
	
	public void removerUsuarioGrupoUsuario(UsuarioGrupoUsuario usuarioGrupoUsuario) {
		getUsuarioPortalInterno().getUsuarioGrupoUsuarios().remove(usuarioGrupoUsuario);
	}
	
	public void removerUsuarioServentia(UsuarioServentia usuarioServentia) {
		usuarioPortalInternoBO.removeUsuarioServentia(getUsuarioPortalInterno(), usuarioServentia);
	}
	
	public void adicionarServentia() {
		usuarioPortalInternoBO.adicionarServentia(getUsuarioPortalInterno());
	}
	
	public void buscarListaMunicipio() {
		getUsuarioPortalInterno().setMunicipio(null);
		getUsuarioPortalInterno().setCodigoCorregedoriaCadastroUsuario(null);
		getUsuarioPortalInterno().setCodigoCorregedoriaSelecionado(null);
		setListaMunicipio(municipioLookUp.getListaMunicipiosEstadoSede());
	}
	
	public void recuperaListaCartorioPorMunicipio() {
		try {
			fechaModalStatusDialog();
			setListaCartorio(null);
			if (getUsuarioPortalInterno().getMunicipio() == null || !Util.ehStringValida(getUsuarioPortalInterno().getMunicipio().getCodigoRecompe())) {
				return;
			}
			setListaCartorio(migracaoRecompeWS.recuperaListaCartorioPorCodigoMunicipio(getUsuarioPortalInterno().getMunicipio().getCodigoRecompe()));
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível recuperar os cartórios do município selecionado nesse momento.");
		}
	}
	
	public boolean desabilitaComboCartorio() {
		return getUsuarioPortalInterno().getMunicipio() == null || !Util.ehStringValida(getUsuarioPortalInterno().getMunicipio().getCodigoRecompe()) || getListaCartorio() == null || getListaCartorio().isEmpty();
	}
	
	public void limpar() {
		setUsuarioPortalInterno(new UsuarioPortalInterno());
	}

	public boolean isExibirListaUsuarioGrupoUsuario() {
		return getUsuarioPortalInterno().getUsuarioGrupoUsuarios() != null && getUsuarioPortalInterno().getUsuarioGrupoUsuarios().size() > 0;
	}

	public String voltar() {
		return EnumPaginas.PESQUISAR_USUARIO_PORTAL_INTERNO.getUrl();
	}
	
	public UsuarioPortalInterno getUsuarioPortalInterno() {
		return usuarioPortalInterno;
	}

	public void setUsuarioPortalInterno(UsuarioPortalInterno usuarioPortalInterno) {
		this.usuarioPortalInterno = usuarioPortalInterno;
	}
	
	public UsuarioGrupoUsuario getUsuarioGrupoUsuario() {
		return usuarioGrupoUsuario;
	}

	public void setUsuarioGrupoUsuario(UsuarioGrupoUsuario usuarioGrupoUsuario) {
		this.usuarioGrupoUsuario = usuarioGrupoUsuario;
	}

	public List<Municipio> getListaMunicipio() {
		return listaMunicipio;
	}

	public void setListaMunicipio(List<Municipio> listaMunicipio) {
		this.listaMunicipio = listaMunicipio;
	}
	
	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}

	public List<GrupoUsuario> getListaGrupoUsuario() {
		return listaGrupoUsuario;
	}

	public void setListaGrupoUsuario(List<GrupoUsuario> listaGrupoUsuario) {
		this.listaGrupoUsuario = listaGrupoUsuario;
	}
	
}
