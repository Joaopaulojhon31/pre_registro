package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import web.util.JSFUtil;
import web.util.Paginacao;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PesquisarUsuarioPortalInternoController extends BaseController implements Serializable {

	@EJB
	private UsuarioPortalInternoDAO usuarioPortalInternoDAO;
	
	private UsuarioPortalInterno usuario;
	private List<UsuarioPortalInterno> listaUsuarios;
	private Paginacao paginacao = new Paginacao();

	@PostConstruct
	public void inicializar() {
		if (getUsuario() == null) {
			setUsuario(new UsuarioPortalInterno());
		}
		pesquisaUsuarios();
	}

	public void pesquisaUsuarios() {
		try {
			getPaginacao().limpaPaginacao();
			getPaginacao().setTotalRegistros(usuarioPortalInternoDAO.quantidadePesquisarUaurios(getUsuario()));
			pesquisaUsuariosPaginado();
		} catch (Exception ex) {
			ex.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar pelos usuários nesse momento. Tente novamente mais tarde.");
		}
	}

	public void pesquisaUsuariosPaginado() {
		try {
			setListaUsuarios(usuarioPortalInternoDAO.pesquisarUsuario(getUsuario(), getPaginacao()));
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar pelos usuários nesse momento. Tente novamente mais tarde.");
		}
	}

	public boolean isExibeListaUsuarios() {
		return getListaUsuarios() != null && getListaUsuarios().size() > 0;
	}

	public void limpaUsuario() {
		getPaginacao().limpaPaginacao();
		setListaUsuarios(new ArrayList<>());
		if (getUsuario() != null) {
			getUsuario().setNome("");
			getUsuario().setCpf("");
			getUsuario().setEmail("");
		}
	}

	public String selecionaUsuario(UsuarioPortalInterno usuarioPortalInterno) {
		JSFUtil.put("usuarioPortalInterno", usuarioPortalInterno);
		return EnumPaginas.CADASTRAR_USUARIO_PORTAL_INTERNO.getUrl();
	}

	public String novoUsuario() {
		setUsuario(null);
		return EnumPaginas.CADASTRAR_USUARIO_PORTAL_INTERNO.getUrl();
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public UsuarioPortalInterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalInterno usuario) {
		this.usuario = usuario;
	}

	public List<UsuarioPortalInterno> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<UsuarioPortalInterno> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
}
