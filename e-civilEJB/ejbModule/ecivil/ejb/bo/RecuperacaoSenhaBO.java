package ecivil.ejb.bo;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.dao.UsuarioPortalExternoDAO;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.Util;

@Stateless
public class RecuperacaoSenhaBO {

	private static final String MENSAGEM_VALIDACAO_CPF_NAO_INFORMADO = "msg.validacao.cpfNaoInformado";
	private static final String MENSAGEM_VALIDACAO_CPF_INVALIDO = "msg.campo.cpf.invalido";
	private static final String MENSAGEM_VALIDACAO_CPF_NAO_CADASTRADO = "msg.validacao.cpfNaoCadastrado";
	private static final String MENSAGEM_VALIDACAO_TEMPO_ESPERA_PARA_NOVA_SENHA = "msg.aguardar.nova.solicitacao.senha";
	private static final Integer QUANTIDADE_CARACTERES_SENHA = 5;

	@EJB
	private transient UsuarioPortalInternoDAO usuarioPortalInternoDao;

	@EJB
	private transient UsuarioPortalExternoDAO usuarioPortalExternoDAO;

	@EJB
	private DataDAO dataDAO;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private EmailBO emailBO;

	public void gerarNovaSenhaPortalInterno(String cpf) throws ECivilException {
		Date dataAtual = dataDAO.retornaDataBanco();
		validarUsuarioPortalInterno(cpf);
		String novaSenha = Util.generateRandomString(QUANTIDADE_CARACTERES_SENHA);
		UsuarioPortalInterno usuario = usuarioPortalInternoDao.recuperarUsuario(cpf);
		usuario.setSenha(CriptografiaUtil.retornaCriptografadoMD5(novaSenha));
		usuario.setDataAlteracaoSenha(dataAtual);
		usuarioPortalInternoDao.atualiza(usuario);
		emailBO.enviaEmailRecuperacaoSenha(usuario.getEmail(), usuario.getNome(), novaSenha);
	}

	public String gerarNovaSenhaPortaExterno(String cpf) throws ECivilException {
		Date dataAtual = dataDAO.retornaDataBanco();
		validarUsuarioPortalExterno(cpf);
		String novaSenha = Util.generateRandomString(QUANTIDADE_CARACTERES_SENHA);
		UsuarioPortalExterno usuario = usuarioPortalExternoDAO.recuperarUsuarioPorCpf(cpf);
		usuario.setSenha(CriptografiaUtil.retornaCriptografadoMD5(novaSenha));
		usuario.setDataAlteracaoSenha(dataAtual);
		usuarioPortalExternoDAO.atualiza(usuario);
		emailBO.enviaEmailRecuperacaoSenha(usuario.getEmail(), usuario.getNome(), novaSenha);
		return usuario.getEmail();
	}

	private void validarUsuarioPortalInterno(String cpf) throws ECivilException {
		ECivilException exception = new ECivilException();
		exception.instanciaListaErros();
		validarCPFInformado(cpf, exception);
		validarCPF(cpf, exception);
		validarUsuarioPortalInterno(cpf, exception);
		if (exception.possuiListaErros()) {
			throw exception;
		}
	}

	private void validarUsuarioPortalExterno(String cpf) throws ECivilException {
		ECivilException exception = new ECivilException();
		exception.instanciaListaErros();
		validarCPFInformado(cpf, exception);
		validarCPF(cpf, exception);
		validarUsuarioPortalExterno(cpf, exception);
		if (exception.possuiListaErros()) {
			throw exception;
		}
	}

	private void validarCPFInformado(String cpf, ECivilException exception) {
		exception.instanciaListaErros();
		if (StringUtils.isBlank(cpf)) {
			exception.adicionaErroNaLista(MENSAGEM_VALIDACAO_CPF_NAO_INFORMADO);
		}
	}

	private void validarCPF(String cpf, ECivilException exception) {
		if (StringUtils.isNotBlank(cpf) && !Util.isValidCPF(cpf)) {
			exception.adicionaErroNaLista(MENSAGEM_VALIDACAO_CPF_INVALIDO);
		}
	}

	private void validarUsuarioPortalInterno(String cpf, ECivilException exception) {
		if (exception.possuiListaErros()) {
			return;
		}

		UsuarioPortalInterno usuarioPortalInterno = usuarioPortalInternoDao.recuperarUsuario(cpf);

		if (usuarioPortalInterno == null) {
			exception.adicionaErroNaLista(MENSAGEM_VALIDACAO_CPF_NAO_CADASTRADO);
		}

		Date dataAtual = dataDAO.retornaDataBanco();

		if (usuarioPortalInterno.getDataAlteracaoSenha() != null
				&& DataUtil.adicionaMinutosData(usuarioPortalInterno.getDataAlteracaoSenha(), Integer.valueOf(parametroDAO.buscaValorParametro(Parametro.TEMPO_ESPERA_NOVA_SENHA))).after(dataAtual)) {
			
			exception.adicionaErroNaLista(MENSAGEM_VALIDACAO_TEMPO_ESPERA_PARA_NOVA_SENHA, parametroDAO.buscaValorParametro(Parametro.TEMPO_ESPERA_NOVA_SENHA), usuarioPortalInterno.getDataAlteracaoSenha());
		}
	}

	private void validarUsuarioPortalExterno(String cpf, ECivilException exception) {
		if (exception.possuiListaErros()) {
			return;
		}

		UsuarioPortalExterno usuarioPortalExterno = usuarioPortalExternoDAO.recuperarUsuarioPorCpf(cpf);

		if (usuarioPortalExterno == null) {
			exception.adicionaErroNaLista(MENSAGEM_VALIDACAO_CPF_NAO_CADASTRADO);
		}

		Date dataAtual = dataDAO.retornaDataBanco();

		if (usuarioPortalExterno.getDataAlteracaoSenha() != null
				&& DataUtil.adicionaMinutosData(usuarioPortalExterno.getDataAlteracaoSenha(), Integer.valueOf(parametroDAO.buscaValorParametro(Parametro.TEMPO_ESPERA_NOVA_SENHA))).after(dataAtual)) {
			
			exception.adicionaErroNaLista(MENSAGEM_VALIDACAO_TEMPO_ESPERA_PARA_NOVA_SENHA, parametroDAO.buscaValorParametro(Parametro.TEMPO_ESPERA_NOVA_SENHA), usuarioPortalExterno.getDataAlteracaoSenha());
		}
	}

}
