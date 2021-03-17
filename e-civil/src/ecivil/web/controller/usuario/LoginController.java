package ecivil.web.controller.usuario;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.bo.RecuperacaoSenhaBO;
import ecivil.ejb.bo.UsuarioPortalExternoBO;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;
import ecivil.web.controller.BaseController;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class LoginController extends BaseController implements Serializable {

	@EJB
	private RecuperacaoSenhaBO recuperacaoSenhaBO;

	@EJB
	private UsuarioPortalExternoBO usuarioPortalExternoBO;

	private String login;
	private String pass;

	private String cpf;
	private String senha;

	@PostConstruct
	public void inicializar() {
		UsuarioPortalExterno usuarioPortalExterno = (UsuarioPortalExterno) JSFUtil.get("novoUsuarioCadastrado");
		if (usuarioPortalExterno != null) {
			setLogin(usuarioPortalExterno.getCpf());
			Mensagem.infoSemBundle("Usuário cadastrado com sucesso!");
		}
	}

	public String entrar() {
		try {
			if (!Util.ehStringValida(login) || !Util.ehStringValida(pass)) {
				Mensagem.errorSemBundle("Favor preencher o usuário e a senha.");
				return null;
			}
			
			pass = CriptografiaUtil.retornaCriptografadoMD5(pass);
			login = Util.removeMask(login);
			
			UsuarioPortalExterno usuarioPortalExterno = usuarioPortalExternoBO.recuperarUsuarioPorCpf(login);
			
			if (usuarioPortalExterno != null && usuarioPortalExterno.isPendenteAtualizacao()) {
				JSFUtil.getRequest().login(login, usuarioPortalExterno.getSenha());
			} else {
				JSFUtil.getRequest().login(login, pass);
			}
			
			return EnumPaginas.PRINCIPAL.getUrl();
		}catch (Exception ex) {
			ex.printStackTrace();
			Mensagem.errorSemBundle("Login e/ou senha inválidos. Verifique novamente");
			return null;
		}
	}

	public void recuperarSenhaUsuarioPortalExterno() {
		try {
			if (StringUtils.isEmpty(login)) {
				Mensagem.errorSemBundle("Informe o seu login para recuperar sua senha ");
				return;
			}
			String email = recuperacaoSenhaBO.gerarNovaSenhaPortaExterno(Util.removeMask(login));
			Mensagem.infoSemBundle("Senha alterada com sucesso! Foi enviado um e-mail para '" + email + "' com a nova senha.");
		} catch (ECivilException exception) {
			Mensagem.listaErros(exception.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar a senha nesse momento. Tente novamente mais tarde.");
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
