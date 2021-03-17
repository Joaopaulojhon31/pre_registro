package ecivil.ejb.bo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import ecivil.ejb.dao.UsuarioPortalExternoDAO;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.exception.ECivilException;

@Stateless
public class AlterarSenhaPortalExternoBO {
	
	@EJB
	private UsuarioPortalExternoDAO usuarioPortalDAO;
	
	public void validarCampos(UsuarioPortalExterno usuarioParaEdicao, UsuarioPortalExterno usuarioLogadoPortal) throws Exception {
		ECivilException centralInformacaoException = new ECivilException();
		centralInformacaoException.instanciaListaErros();
		validarObrigatoriedadeSenha(usuarioParaEdicao, centralInformacaoException);
		validarObrigatoriedadeNovaSenha(usuarioParaEdicao, centralInformacaoException);
		validarObrigatoriedadeConfirmarSenha(usuarioParaEdicao, centralInformacaoException);
		validarIgualdadeSenhas(usuarioParaEdicao, centralInformacaoException);
		validarSenhaExistente(usuarioParaEdicao, usuarioLogadoPortal, centralInformacaoException);
		if (centralInformacaoException.possuiListaErros()) {
			throw centralInformacaoException;
		}
	}

	public void validarObrigatoriedadeNovaSenha(UsuarioPortalExterno usuarioParaEdicao, ECivilException centralInformacaoException) throws Exception {
		if (!usuarioParaEdicao.possuiNovaSenha()) {
			centralInformacaoException.adicionaErroNaLista("msg.campo.nova.senha.obrigatorio");
		}
	}

	public void validarObrigatoriedadeConfirmarSenha(UsuarioPortalExterno usuarioParaEdicao, ECivilException centralInformacaoException) throws Exception {
		if (!usuarioParaEdicao.possuiConfirmarSenha()) {
			centralInformacaoException.adicionaErroNaLista("msg.campo.confirmar.nova.senha.usuario.obrigatorio");
		}
	}

	public void validarObrigatoriedadeSenha(UsuarioPortalExterno usuarioParaEdicao, ECivilException centralInformacaoException) throws Exception {
		if (!usuarioParaEdicao.possuiSenha()) {
			centralInformacaoException.adicionaErroNaLista("msg.campo.senha.usuario.obrigatorio");
		}
	}

	public void validarIgualdadeSenhas(UsuarioPortalExterno usuarioParaEdicao, ECivilException centralInformacaoException) throws Exception {
		if (usuarioParaEdicao.possuiNovaSenha() && usuarioParaEdicao.possuiConfirmarSenha()) {
			if (!usuarioParaEdicao.getNovaSenha().equals(usuarioParaEdicao.getConfirmarSenha())) {
				centralInformacaoException.adicionaErroNaLista("msg.campo.nova.senha.diferente.confirmar.senha");
			}
		}
	}

	public void validarSenhaExistente(UsuarioPortalExterno usuarioParaEdicao, UsuarioPortalExterno usuarioLogado, ECivilException centralInformacaoException) throws Exception {
		if (usuarioParaEdicao.possuiSenha() && usuarioLogado.possuiSenha()) {
			String senhaCriptografada = usuarioParaEdicao.retornaSenhaCriptografada(usuarioParaEdicao.getSenha());
			usuarioParaEdicao.setSenha(senhaCriptografada);
			if (!usuarioLogado.getSenha().equals(usuarioParaEdicao.getSenha())) {
				centralInformacaoException.adicionaErroNaLista("msg.campo.senha.atual.nao.confere");
			}
		}
	}

	public void alterarSenha(UsuarioPortalExterno usuarioParaEdicao, UsuarioPortalExterno usuarioLogadoPortal) throws Exception {
		validarCampos(usuarioParaEdicao, usuarioLogadoPortal);
		usuarioLogadoPortal.setSenha(usuarioParaEdicao.retornaSenhaCriptografada(usuarioParaEdicao.getNovaSenha()));
		usuarioParaEdicao.setSenha(usuarioLogadoPortal.getSenha());
		usuarioParaEdicao.setVersao(usuarioLogadoPortal.getVersao());
		usuarioPortalDAO.atualiza(usuarioParaEdicao);
	}
}
