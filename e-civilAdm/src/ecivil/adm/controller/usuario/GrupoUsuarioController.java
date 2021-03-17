package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.dao.GrupoUsuarioDAO;
import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.entidade.UsuarioGrupoUsuario;

@Named
@ViewScoped
public class GrupoUsuarioController extends BaseController implements Serializable {

	private static final long serialVersionUID = -977168740125134067L;

	private List<GrupoUsuario> listaGrupoUsuario;

	@EJB
	private GrupoUsuarioDAO grupoUsuarioDAO;

	private UsuarioGrupoUsuario usuarioGrupoUsuario;

	private GrupoUsuario grupoUsuario;

	public List<GrupoUsuario> getListaGrupoUsuario() {
		return listaGrupoUsuario;
	}

	public void setListaGrupoUsuario(List<GrupoUsuario> listaGrupoUsuario) {
		this.listaGrupoUsuario = listaGrupoUsuario;
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public UsuarioGrupoUsuario getUsuarioGrupoUsuario() {
		return usuarioGrupoUsuario;
	}

	public void setUsuarioGrupoUsuario(UsuarioGrupoUsuario usuarioGrupoUsuario) {
		this.usuarioGrupoUsuario = usuarioGrupoUsuario;
	}

	@PostConstruct
	public void inicalizar() {
		try {
			if (grupoUsuario == null) {
				setGrupoUsuario(new GrupoUsuario());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pesquisarGrupoUsuario() throws Exception {

		listaGrupoUsuario = grupoUsuarioDAO.recuperarGrupoUsuariosRetirandoLista(grupoUsuario, usuarioGrupoUsuario.getUsuario().recuperarListaSegGrupoUsuarios());
		if (listaGrupoUsuario == null || listaGrupoUsuario.size() == 0) {
			Mensagem.error("msg.nenhum.registro.encontrado");
		}
	}

	public void instanciarGrupoUsuario() {
		if (grupoUsuario == null) {
			grupoUsuario = new GrupoUsuario();
		}
	}

	public void pesquisar() {
		try {
			pesquisarGrupoUsuario();
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível pesquisar os grupos de usuário nesse momento.");
			e.printStackTrace();
		}
	}

	public void selecionarGrupoUsuario(GrupoUsuario grupoUsuario) {
		// instanciarUsuarioGrupoUsuario ();
		usuarioGrupoUsuario.setGrupoUsuario(grupoUsuario);
	}

	public void instanciarUsuarioGrupoUsuario() {
		if (getUsuarioGrupoUsuario() == null) {
			usuarioGrupoUsuario = new UsuarioGrupoUsuario();
		}
	}

	public void limpar() {
		if (grupoUsuario != null) {
			grupoUsuario.setDescricao("");
		}
	}

	public void setarUsuarioGrupoUsuario(UsuarioGrupoUsuario usuarioGrupoUsuario) {
		setUsuarioGrupoUsuario(usuarioGrupoUsuario);
		instanciarGrupoUsuario();
		limpar();
		pesquisar();
	}

	public boolean isExibirListaGrupoUsuario() {
		return listaGrupoUsuario != null && listaGrupoUsuario.size() > 0;
	}
}
