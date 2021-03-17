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
import ecivil.ejb.dao.PermissaoDAO;
import ecivil.ejb.entidade.Permissao;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PesquisarPermissaoUsuarioInternoController extends BaseController implements Serializable {

	@EJB
	private PermissaoDAO permissaoDAO;

	private Permissao permissao;
	private List<Permissao> listaPermissao;

	@PostConstruct
	public void inicalizar() {
		if (getPermissao() == null) {
			setPermissao(new Permissao());
		}
		pesquisar();
	}

	public boolean isExibirListaPermissao() {
		return getListaPermissao() != null && getListaPermissao().size() > 0;
	}

	public void pesquisar() {
		try {
			setListaPermissao(permissaoDAO.recuperarPermissoes(getPermissao()));
			if (getListaPermissao() == null || getListaPermissao().size() == 0) {
				Mensagem.errorSemBundle("Nenhum registro encontrado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar as permissões nesse momento. Tente novamente mais tarde.");
		}
	}

	public String selecionarPermissao(Permissao permissao) {
		JSFUtil.put("permissao", permissao);
		return EnumPaginas.CADASTRAR_PERMISSAO_USUARIO_INTERNO.getUrl();
	}

	public String novo() {
		return EnumPaginas.CADASTRAR_PERMISSAO_USUARIO_INTERNO.getUrl();
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public List<Permissao> getListaPermissao() {
		return listaPermissao;
	}

	public void setListaPermissao(List<Permissao> listaPermissao) {
		this.listaPermissao = listaPermissao;
	}
	
}
