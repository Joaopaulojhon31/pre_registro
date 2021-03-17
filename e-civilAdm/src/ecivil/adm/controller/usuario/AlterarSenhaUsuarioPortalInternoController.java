package ecivil.adm.controller.usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.AlterarSenhaPortalInternoBO;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class AlterarSenhaUsuarioPortalInternoController extends BaseController {

	@EJB
	private AlterarSenhaPortalInternoBO alterarSenhaPortalInternoBO;

	@EJB
	private UsuarioPortalInternoDAO usuarioPortalDAO;
	
	private UsuarioPortalInterno usuario;

	@PostConstruct
	public void inicializar() {
		if (usuario == null) {
			usuario = new UsuarioPortalInterno();
		}
	}

	public void gravar() {
		try {
			alterarSenhaPortalInternoBO.validarCampos(getUsuario(), getUsuarioLogadoPortal());
			getUsuarioLogadoPortal().setSenha(getUsuario().retornaSenhaCriptografada(getUsuario().getNovaSenha()));
			usuarioPortalDAO.atualiza(getUsuarioLogadoPortal());
			Mensagem.infoSemBundle("Senha alterada com sucesso");
		} catch (ECivilException e) {
			Mensagem.listaErros(e.getListaErros());
		} catch (Exception e) {
			Mensagem.error("Não foi possível alterar sua senha nesse momento. Tente novamente mais tarde.");
		}
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public UsuarioPortalInterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalInterno usuario) {
		this.usuario = usuario;
	}

}
