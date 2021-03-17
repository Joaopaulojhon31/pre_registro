package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.dao.GrupoUsuarioDAO;
import ecivil.ejb.entidade.GrupoUsuario;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PesquisarGrupoUsuarioInternoController extends BaseController implements Serializable {

	@EJB
	private GrupoUsuarioDAO grupoUsuarioDAO;

	private GrupoUsuario grupoUsuario;
	private List<GrupoUsuario> listaGrupoUsuario;

	@PostConstruct
	public void inicalizar() {
		if (getGrupoUsuario() == null) {
			setGrupoUsuario(new GrupoUsuario());
		}
		pesquisar();
	}

	public boolean isExibirListaGrupoUsuario() {
		return getListaGrupoUsuario() != null && getListaGrupoUsuario().size() > 0;
	}

	public void pesquisar() {
		try {
			setListaGrupoUsuario(grupoUsuarioDAO.recuperarGruposUsuarios(getGrupoUsuario()));
			if (getListaGrupoUsuario() == null || getListaGrupoUsuario().size() == 0) {
				Mensagem.errorSemBundle("Nenhum resultado encontrado.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível pesquisar pelos Grupos Usuários nesse momento. Tente novamente mais tarde.");
		}
	}

	public String selecionarGrupoUsuario(GrupoUsuario grupoUsuarioSelecionado) {
		JSFUtil.put("grupoUsuario", grupoUsuarioSelecionado);
		return EnumPaginas.CADASTRAR_GRUPO_USUARIO_INTERNO.getUrl();
	}

	public String novo() {
		return EnumPaginas.CADASTRAR_GRUPO_USUARIO_INTERNO.getUrl();
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public List<GrupoUsuario> getListaGrupoUsuario() {
		return listaGrupoUsuario;
	}

	public void setListaGrupoUsuario(List<GrupoUsuario> listaGrupoUsuario) {
		this.listaGrupoUsuario = listaGrupoUsuario;
	}
	
}
