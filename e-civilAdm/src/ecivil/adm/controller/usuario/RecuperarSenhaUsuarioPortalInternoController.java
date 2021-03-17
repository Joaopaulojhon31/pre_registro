package ecivil.adm.controller.usuario;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.UsuarioPortalInternoBO;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import web.util.JSFUtil;

@ViewScoped
@Named
public class RecuperarSenhaUsuarioPortalInternoController extends BaseController implements Serializable {

	private static final long serialVersionUID = 4465006029774736630L;

	private String cpf;

	@EJB
	private UsuarioPortalInternoDAO usuarioPortalDAO;

	@EJB
	private UsuarioPortalInternoBO usuarioPortalBO;

	@PostConstruct
	public void validaServentiaSelecionada() {
		setCpf((String) JSFUtil.get("cpfRecuperacao"));
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	private void validarObrigatoriedadeCampos() throws Exception {
		if (StringUtils.isEmpty(getCpf())) {
			throw new ECivilException("msg.campo.cpf.obrigatorio");
		}
	}

	public void recuperarSenha() {
		try {
			validarObrigatoriedadeCampos();
			UsuarioPortalInterno usuarioPortal = usuarioPortalDAO.recuperarUsuario(getCpf());
			validarExistenciaUsuarioPortal(usuarioPortal);
			usuarioPortalBO.alterarSenhaAleatoriamente(usuarioPortal);
			Mensagem.info("msg.senha.alterada.enviada", usuarioPortal.getEmail());
			enviarSenhaPorEmail(usuarioPortal);
		} catch (ECivilException e) {
			Mensagem.error(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.error("msg.erro.ao.recuperar.senha", e.getMessage());
		}
	}

	private void enviarSenhaPorEmail(UsuarioPortalInterno usuarioPortal) throws Exception {
//		String mensagemEmail = " Sua nova senha na Central De Informa��o � : " + usuarioPortal.getSenha();
//		MailUtil.enviaMail("Nova Senha-Central De Informa��o", usuarioPortal.getEmail(), mensagemEmail);
	}

	private void validarExistenciaUsuarioPortal(UsuarioPortalInterno usuarioPortal) throws ECivilException {
		if (usuarioPortal == null) {
			throw new ECivilException("msg.usuario.nao.cadastrado");
		}
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
}
