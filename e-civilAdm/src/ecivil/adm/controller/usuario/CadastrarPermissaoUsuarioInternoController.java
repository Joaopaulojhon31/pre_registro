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
import ecivil.ejb.bo.CadastroPermissaoBO;
import ecivil.ejb.dao.PermissaoDAO;
import ecivil.ejb.entidade.Permissao;
import ecivil.ejb.entidade.Recurso;
import ecivil.ejb.exception.ECivilException;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class CadastrarPermissaoUsuarioInternoController extends BaseController implements Serializable {

	@EJB
	private CadastroPermissaoBO cadastroPermissaoBO;

	@EJB
	private PermissaoDAO permissaoDAO;
	
	private Permissao permissao;

	@PostConstruct
	public void inicializar() {
		permissao = (Permissao) JSFUtil.get("permissao");
		if (permissao == null) {
			permissao = new Permissao();
			permissao.instanciarRecursos();
			permissao.adicionarRecurso();
		}
	}
	
	@Override
	public List<EnumPaginas> listBreadCrumb() {
		List<EnumPaginas> bread = super.listBreadCrumb();
		bread.add(EnumPaginas.PESQUISAR_PERMISSAO_USUARIO_INTERNO);
		return bread;
	}

	public void novoRecurso() {
		permissao.adicionarRecurso();
	}

	public void removerRecurso(Recurso recurso) {
		permissao.removerRecurso(recurso);
	}

	public void gravar() {
		try {
			cadastroPermissaoBO.validarCampos(getPermissao());
			permissaoDAO.atualiza(getPermissao());
			Mensagem.infoSemBundle("Permissão gravada com sucesso.");
			limpar();
		} catch (ECivilException e) {
			Mensagem.listaErrosSemBundle(e.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível cadastrar a permissão nesse momento. Tente novamente mais tarde.");
		}
	}

	public void limpar() {
		permissao = new Permissao();
		permissao.instanciarRecursos();
		permissao.adicionarRecurso();
	}

	public void excluir() {
		try {
			if (getPermissao().possuiIdPermissao()) {
				getPermissao().setExcluido(Boolean.TRUE);
				permissaoDAO.atualiza(getPermissao());
				limpar();
				Mensagem.infoSemBundle("Permissão excluída com sucesso.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível excluir a permissão nesse momento. Tente novamente mais tarde.");
		}
	}

	public String voltar() {
		return EnumPaginas.PESQUISAR_PERMISSAO_USUARIO_INTERNO.getUrl();
	}

	public boolean isExibirExcluir() {
		return getPermissao() != null && getPermissao().possuiIdPermissao();
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}
}
