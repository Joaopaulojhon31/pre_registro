package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.CadastroGrupoUsuarioBO;
import ecivil.ejb.dao.GrupoUsuarioDAO;
import ecivil.ejb.dao.PermissaoDAO;
import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.entidade.GrupoUsuarioPermissao;
import ecivil.ejb.entidade.Permissao;
import ecivil.ejb.exception.ECivilException;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class CadastrarGrupoUsuarioInternoController extends BaseController implements Serializable {

	@EJB
	private PermissaoDAO permissaoDAO;

	@EJB
	private GrupoUsuarioDAO grupoUsuarioDAO;

	@EJB
	private CadastroGrupoUsuarioBO cadastroGrupoUsuarioBO;

	private GrupoUsuario grupoUsuario;
	private List<Permissao> listaPermissoes;
	
	@PostConstruct
	public void incicializar() {
		grupoUsuario = (GrupoUsuario) JSFUtil.get("grupoUsuario");
		instanciarGrupoUsuario();
		recuperarPermissoes();
	}
	
	@Override
	public List<EnumPaginas> listBreadCrumb() {
		List<EnumPaginas> bread = super.listBreadCrumb();
		bread.add(EnumPaginas.PESQUISAR_GRUPO_USUARIO_INTERNO);
		return bread;
	}

	private void recuperarPermissoes() {
		try {
			listaPermissoes = permissaoDAO.recuperarPermissoesRetirandoLista(getGrupoUsuario().recuperarListaSeqPermissoes());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.error("msg.erro.recuperar.permissoes", e.getMessage());
		}
	}

	private void instanciarGrupoUsuario() {
		if (getGrupoUsuario() == null) {
			grupoUsuario = new GrupoUsuario();
			grupoUsuario.setGrupoUsuarioPermissoes(new HashSet<GrupoUsuarioPermissao>());
		}
	}

	public void adicionarPermissao(Permissao permissao) {
		GrupoUsuarioPermissao grupoUsuarioPermissao = grupoUsuario.criarGrupoUsuarioPermissao(permissao);
		grupoUsuario.getGrupoUsuarioPermissoes().add(grupoUsuarioPermissao);
		recuperarPermissoes();
	}

	public void removerGrupoUsuarioPermissao(GrupoUsuarioPermissao grupoUsuarioPermissaos) {
		grupoUsuario.getGrupoUsuarioPermissoes().remove(grupoUsuarioPermissaos);
		recuperarPermissoes();
	}

	public void gravar() {
		try {
			cadastroGrupoUsuarioBO.validarCampos(getGrupoUsuario());
			grupoUsuarioDAO.atualiza(getGrupoUsuario());
			limpar();
			Mensagem.infoSemBundle("Grupo Usuário gravado com sucesso.");
		} catch (ECivilException e) {
			e.printStackTrace();
			Mensagem.listaErrosSemBundle(e.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível gravar o grupo de usuário nesse momento. Tente novamente mais tarde.");
		}
	}

	public void limpar() {
		grupoUsuario = new GrupoUsuario();
		grupoUsuario.setGrupoUsuarioPermissoes(new HashSet<GrupoUsuarioPermissao>());
		recuperarPermissoes();
	}

	public void excluir() {
		try {
			if (getGrupoUsuario().possuiSeqGrupoUsuario()) {
				grupoUsuario.setExcluido(Boolean.TRUE);
				grupoUsuarioDAO.atualiza(getGrupoUsuario());
				limpar();
				Mensagem.infoSemBundle("Grupo Usuário excluído com sucesso.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível excluir o grupo de usuário nesse momento. Tente novamente mais tarde.");
		}
	}

	public boolean isExibirExcluir() {
		return getGrupoUsuario() != null && getGrupoUsuario().possuiSeqGrupoUsuario();
	}

	public String voltar() {
		return EnumPaginas.PESQUISAR_GRUPO_USUARIO_INTERNO.getUrl();
	}
	
	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public List<Permissao> getListaPermissoes() {
		return listaPermissoes;
	}

	public void setListaPermissoes(List<Permissao> listaPermissoes) {
		this.listaPermissoes = listaPermissoes;
	}
	
}
