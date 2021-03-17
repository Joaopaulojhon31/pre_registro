package ecivil.ejb.bo;

import javax.ejb.Stateless;

import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.exception.ECivilException;

@Stateless
public class CadastroGrupoUsuarioBO {

	public void validarCampos(GrupoUsuario grupoUsuario) throws Exception {
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();
		validarObrigatoriedadeDescricao(grupoUsuario, eCivilException);
		validarObrigatoriedadeRecursos(grupoUsuario, eCivilException);
		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}

	public void validarObrigatoriedadeDescricao(GrupoUsuario grupoUsuario, ECivilException eCivilException) throws Exception {
		if (!grupoUsuario.possuiDescricao()) {
			eCivilException.adicionaErroNaLista("O campo Grupo Usu�rio � de preenchimento obrigat�rio.");
		}
	}

	public void validarObrigatoriedadeRecursos(GrupoUsuario grupoUsuario, ECivilException eCivilException) throws Exception {
		if (!grupoUsuario.possuiGrupoUsuarioPermissoes()) {
			eCivilException.adicionaErroNaLista("� obrigat�rio informar pelo menos uma permiss�o.");
		}
	}

}
