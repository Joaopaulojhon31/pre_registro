package ecivil.web.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.web.enumerator.EnumPaginas;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class PrincipalController extends BaseController implements Serializable {

	private String emailNovoUsuarioPortalExterno;

	public String direcionaInicial() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public String direcionaBuscarCertidao() {
		return EnumPaginas.BUSCAR_CERTIDAO.getUrl();
	}
	
	public String direcionaConsultarCertidao() {
		return EnumPaginas.CONSULTAR_CERTIDAO.getUrl();
	}
	
	public String direcionaValidarHashCertidao() {
		return EnumPaginas.VALIDAR_HASH_CERTIDAO.getUrl();
	}

	public String direcionaSobre() {
		return EnumPaginas.SOBRE.getUrl();
	}

	public String direcionaAlterarSenhaUsuarioPortalExterno() {
		return EnumPaginas.ALTERAR_SENHA_USUARIO_PORTAL_EXTERNO.getUrl();
	}
	
	public String direcionarCadastrarUsuarioPortalExterno() {
		return EnumPaginas.CADASTRO_USUARIO_PORTAL_EXTERNO.getUrl();
	}

	public String getEmailNovoUsuarioPortalExterno() {
		return emailNovoUsuarioPortalExterno;
	}

	public void setEmailNovoUsuarioPortalExterno(String emailNovoUsuarioPortalExterno) {
		this.emailNovoUsuarioPortalExterno = emailNovoUsuarioPortalExterno;
	}
}
