package ecivil.web.controller.usuario;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.AlterarSenhaPortalExternoBO;
import ecivil.ejb.dao.UsuarioPortalExternoDAO;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.web.controller.BaseController;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class AlterarSenhaUsuarioPortalExternoController extends BaseController {

	private UsuarioPortalExterno usuario;

	@EJB
	private AlterarSenhaPortalExternoBO alterarSenhaPortalExternoBO;

	@EJB
	private UsuarioPortalExternoDAO usuarioPortalDAO;

	public UsuarioPortalExterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalExterno usuario) {
		this.usuario = usuario;
	}

	@PostConstruct
	public void inicializar() {
		if (usuario == null) {
			usuario = new UsuarioPortalExterno();
		}
	}

	public void gravar() {
		try {
			alterarSenhaPortalExternoBO.validarCampos(getUsuario(), getUsuarioLogadoPortal());
			getUsuarioLogadoPortal().setSenha(getUsuario().retornaSenhaCriptografada(getUsuario().getNovaSenha()));
			usuarioPortalDAO.atualiza(getUsuarioLogadoPortal());
			Mensagem.info("msg.senha.alterada.com.sucesso");
		} catch (ECivilException e) {
			Mensagem.listaErros(e.getListaErros());
		} catch (Exception e) {
			Mensagem.error("msg.erro.alterar.senha", e.getMessage());
		}
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

}
