package ecivil.web.controller.usuario;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.RecuperacaoSenhaBO;
import ecivil.ejb.bo.UsuarioPortalExternoBO;
import ecivil.ejb.dao.UsuarioPortalExternoDAO;
import ecivil.ejb.exception.ECivilException;
import ecivil.web.controller.BaseController;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class RecuperarSenhaUsuarioPortalExternoController extends BaseController implements Serializable {
	
	@EJB
	private RecuperacaoSenhaBO recuperacaoSenhaBO;

	@EJB
	private UsuarioPortalExternoDAO usuarioPortalDAO;

	@EJB
	private UsuarioPortalExternoBO usuarioPortalBO;
	
	private String cpf;

	public void recuperarSenha() {
		try {
			recuperacaoSenhaBO.gerarNovaSenhaPortaExterno(cpf);
			Mensagem.infoSemBundle("Senha alterada com sucesso! Acesse seu e-mail para mais informações.");
		} catch (ECivilException exception) {
			Mensagem.listaErros(exception.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar sua senha nesse momento. Tente novamente mais tarde.");
		}
	}

	public String voltar() {
		return EnumPaginas.LOGIN.getUrl();
	}
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
