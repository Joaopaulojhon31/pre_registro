package ecivil.ejb.bo;

import javax.ejb.Stateless;

import ecivil.ejb.entidade.Permissao;
import ecivil.ejb.exception.ECivilException;

@Stateless
public class CadastroPermissaoBO {
	
	public void validarCampos(Permissao permissao) throws Exception {
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();
		validarObrigatoriedadeDescricao(permissao, eCivilException);
		validarObrigatoriedadeRecursos(permissao, eCivilException);
		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}

	public void validarObrigatoriedadeDescricao(Permissao permissao, ECivilException eCivilException) throws Exception {
		if (!permissao.possuiDescricao()) {
			eCivilException.adicionaErroNaLista("Favor informar o ome da permissão.");
		}
	}

	public void validarObrigatoriedadeRecursos(Permissao permissao, ecivil.ejb.exception.ECivilException eCivilException) throws Exception {
		if (!permissao.possuiListaRecursos()) {
			eCivilException.adicionaErroNaLista("Favor informar os recursos da permissão.");
		}

		if (permissao.possuiListaRecursos() && !permissao.possuiTodosRecursosPreenchidos()) {
			eCivilException.adicionaErroNaLista("Favor verificar os campos de recurso. Existem campos não preenchidos.");
		}
	}

}
