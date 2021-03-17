package ecivil.ejb.bo;

import javax.ejb.Stateless;

import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;

@Stateless
public class AlterarSenhaPortalInternoBO {

	public void validarCampos(UsuarioPortalInterno usuario, UsuarioPortalInterno usuarioLogado) throws Exception {
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();
		validarObrigatoriedadeSenha(usuario, eCivilException);
		validarObrigatoriedadeNovaSenha(usuario, eCivilException);
		validarObrigatoriedadeConfirmarSenha(usuario, eCivilException);
		validarIgualdadeSenhas(usuario, eCivilException);
		validarSenhaExistente(usuario, usuarioLogado, eCivilException);
		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}

	public void validarObrigatoriedadeNovaSenha(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiNovaSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.nova.senha.obrigatorio");
		}
	}

	public void validarObrigatoriedadeConfirmarSenha(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiConfirmarSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.confirmar.nova.senha.usuario.obrigatorio");
		}
	}

	public void validarObrigatoriedadeSenha(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.senha.usuario.obrigatorio");
		}
	}

	public void validarIgualdadeSenhas(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiNovaSenha() && usuario.possuiConfirmarSenha()) {
			if (!usuario.getNovaSenha().equals(usuario.getConfirmarSenha())) {
				eCivilException.adicionaErroNaLista("msg.campo.nova.senha.diferente.confirmar.senha");
			}
		}
	}

	public void validarSenhaExistente(UsuarioPortalInterno usuario, UsuarioPortalInterno usuarioLogado, ECivilException eCivilException) throws Exception {
		if (usuario.possuiSenha() && usuarioLogado.possuiSenha()) {
			if (!usuarioLogado.getSenha().equals(usuario.retornaSenhaCriptografada(usuario.getSenha()))) {
				eCivilException.adicionaErroNaLista("msg.campo.senha.atual.nao.confere");
			}
		}
	}

}
