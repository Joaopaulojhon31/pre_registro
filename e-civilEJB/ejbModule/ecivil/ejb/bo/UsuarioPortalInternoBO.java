package ecivil.ejb.bo;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.GrupoUsuarioDAO;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.entidade.UsuarioGrupoUsuario;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.entidade.UsuarioServentia;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.IntegradorMaternidadeWS;
import ecivil.ejb.ws.cliente.rest.response.integradormaternidade.AutenticacaoResponse;

@Stateless
public class UsuarioPortalInternoBO {

	private static final Logger LOGGER = Logger.getLogger(UsuarioPortalInternoBO.class.getName());
	
	private static final String SENHA_PADRAO = "ecivil";

	@EJB
	private UsuarioPortalInternoDAO usuarioPortalInternoDAO;
	
	@EJB
	private GrupoUsuarioDAO grupoUsuarioDAO;
	
	@EJB
	private IntegradorMaternidadeWS integradorMaternidadeWS;
	
	@EJB
	private DataDAO dataDAO;

	public void validarCamposObrigatorios(UsuarioPortalInterno usuario) throws Exception {
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();

		validarObrigatoriedadeNome(usuario, eCivilException);
		validarObrigatoriedadeCPF(usuario, eCivilException);
		validarCpfValido(usuario, eCivilException);
		validarObrigatoriedadeEmail(usuario, eCivilException);
		validarEmailValido(usuario, eCivilException);
		validarObrigatoriedadeConfirmarEmail(usuario, eCivilException);
		validarConfirmarEmailValido(usuario, eCivilException);
		validarSenhas(usuario, eCivilException);
		validarIgualdadeEmail(usuario, eCivilException);
		validaUsuarioCadastrado(usuario, eCivilException);
		validaStatusUsuario(usuario, eCivilException);

		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}

	private void validaStatusUsuario(UsuarioPortalInterno usuario, ECivilException eCivilException) {
		if(usuario.getStatus() == null || usuario.getStatus() > 1 || usuario.getStatus() < 0) {
			eCivilException.adicionaErroNaLista("msg.campo.status.usuario.obrigatorio");
		}
	}

	private void validarSenhas(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.getId() == null) {
			validarObrigatoriedadeSenha(usuario, eCivilException);
			validarObrigatoriedadeConfirmarSenha(usuario, eCivilException);
			validarIgualdadeSenhas(usuario, eCivilException);
		}

	}

	private void validarObrigatoriedadeCPF(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiCpf()) {
			eCivilException.adicionaErroNaLista("msg.campo.cpf.usuario.obrigatorio");
		}
	}

	private void validarCpfValido(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiCpf()) {
			if (!Util.isValidCPF(usuario.getCpf())) {
				eCivilException.adicionaErroNaLista("msg.campo.cpf.invalido");
			}
		}
	}

	private void validarObrigatoriedadeNome(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiNome()) {
			eCivilException.adicionaErroNaLista("msg.campo.nome.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeEmail(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiEmail()) {
			eCivilException.adicionaErroNaLista("msg.campo.email.usuario.obrigatorio");
		}
	}

	private void validarEmailValido(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiEmail()) {
			if (!Util.isEmailValido(usuario.getEmail())) {
				eCivilException.adicionaErroNaLista("msg.campo.email.invalido");
			}
		}
	}

	private void validarObrigatoriedadeConfirmarEmail(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiConfirmarEmail()) {
			eCivilException.adicionaErroNaLista("msg.campo.confirmar.email.usuario.obrigatorio");
		}
	}

	private void validarConfirmarEmailValido(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiConfirmarEmail()) {
			if (!Util.isEmailValido(usuario.getConfirmarEmail())) {
				eCivilException.adicionaErroNaLista("msg.campo.confirmar.email.invalido");
			}
		}
	}

	public void validarObrigatoriedadeSenha(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.senha.obrigatorio");
		}
	}

	public void validarObrigatoriedadeConfirmarSenha(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiConfirmarSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.confirmar.senha.usuario.obrigatorio");
		}
	}

	private void validarIgualdadeSenhas(UsuarioPortalInterno usuario, ECivilException eCivilException) {
		if (usuario.possuiSenha() && usuario.possuiConfirmarSenha()) {
			if (!usuario.getSenha().equals(usuario.getConfirmarSenha())) {
				eCivilException.adicionaErroNaLista("msg.campo.senha.diferente.confirmar.senha");
			}
		}
	}

	private void validarIgualdadeEmail(UsuarioPortalInterno usuario, ECivilException eCivilException) {
		if (usuario.possuiEmail() && usuario.possuiConfirmarEmail()) {
			if (!usuario.getEmail().equals(usuario.getConfirmarEmail())) {
				eCivilException.adicionaErroNaLista("msg.campo.email.diferente.confirmar.email");
			}
		}
	}

	public void validaUsuarioCadastrado(UsuarioPortalInterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiCpf() && usuario.getId() == null) {
			UsuarioPortalInterno usuarioCadastrado = usuarioPortalInternoDAO.recuperarUsuario(usuario.getCpf());
			if (usuarioCadastrado != null) {
				eCivilException.adicionaErroNaLista("msg.usuario.cadastrado", Util.addMask("###.###.###-##", usuario.getCpf()));
			}
		}
	}

	public void grava(UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		try {
			if (usuarioPortalInterno.isNew()) {
				usuarioPortalInterno.setAdmin(false);
				usuarioPortalInterno.setDataInicio(dataDAO.retornaDataBanco());
				if (Util.ehStringValida(usuarioPortalInterno.getSenha())) {
					usuarioPortalInterno.setSenha(CriptografiaUtil.retornaCriptografadoMD5(usuarioPortalInterno.getSenha()));
				}
			}

			if (Boolean.TRUE.equals(usuarioPortalInterno.getResetarSenha())) {
				usuarioPortalInterno.setSenha(CriptografiaUtil.retornaCriptografadoMD5(SENHA_PADRAO));
			}

			usuarioPortalInternoDAO.atualiza(usuarioPortalInterno);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ECivilException("Erro ao tetar gravar usuário: " + ex.getMessage());
		}
	}

	public String alterarSenhaAleatoriamente(UsuarioPortalInterno usuarioPortalInterno) throws Exception {
		String novaSenha = Util.geraNumeroAleatorio(6);
		usuarioPortalInterno.setSenha(CriptografiaUtil.retornaCriptografadoMD5(novaSenha));
		usuarioPortalInternoDAO.atualiza(usuarioPortalInterno);
		return novaSenha;
	}

	@Transactional
	public UsuarioPortalInterno autenticarUsuarioWebRecivil(String cpf) throws ECivilException {
		try {
			List<AutenticacaoResponse> listaAutenticacao = recuperaUsuarioWebRecivil(cpf);
			return verificaPermissoesAcessoWebRecivil(cpf, listaAutenticacao);
 		} catch (ECivilException e) {
 			throw new ECivilException(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ECivilException("Não foi possível autenticar o usuário nesse momento no Integrador Maternidade. Tente novamente mais tarde.");
		}
	}
	
	public UsuarioPortalInterno recuperaUsuarioPorCPF(String cpf) throws Exception {
		UsuarioPortalInterno usuarioPortalInterno = usuarioPortalInternoDAO.recuperarUsuario(cpf);
		if(usuarioPortalInterno == null) {
			throw new Exception();
		}
		return usuarioPortalInterno;
	}

	private List<AutenticacaoResponse> recuperaUsuarioWebRecivil(String cpf) throws ECivilException, InterruptedException {
		for (int i = 0; i < 10; i++) {
			try {
				List<AutenticacaoResponse> listaAutenticacao = integradorMaternidadeWS.autenticarUsuarioWebRecivil(cpf);
				return listaAutenticacao;
			} catch (Exception e) {
				LOGGER.info("#### Erro ao tentar autenticar o usuário no WebRecivil - Integrador Maternidade.");
			}
			Thread.sleep(100);
		}
		throw new ECivilException("#### Erro ao tentar autenticar o usuário no WebRecivil - Integrador Maternidade.");
	}

	private UsuarioPortalInterno verificaPermissoesAcessoWebRecivil(String cpf, List<AutenticacaoResponse> listaAutenticacao) throws ECivilException {
		validaRespostaAutenticarWebRecivil(listaAutenticacao, cpf);
		return recuperaUsuarioViaWebRecivil(cpf, listaAutenticacao);
	}

	private void validaRespostaAutenticarWebRecivil(List<AutenticacaoResponse> listaAutenticacao, String cpf) throws ECivilException {
		if (listaAutenticacao == null || listaAutenticacao.isEmpty()) {
			throw new ECivilException("Não foram encontradas as permissões do usuário no WebRecivil.");
		}
		
		AutenticacaoResponse autenticaoOutroCpf = listaAutenticacao.stream().filter(aut -> aut.getCpf() == null || !aut.getCpf().equals(cpf)).findFirst().orElse(null);
		if (autenticaoOutroCpf != null) {
			throw new ECivilException("Não é possível prosseguir com a autenticação desse usuário pois o CPF informado diverge do CPF informado pelo WebRecivil.");
		}
	}
	
	private UsuarioPortalInterno recuperaUsuarioViaWebRecivil(String cpf, List<AutenticacaoResponse> listaAutenticacao) throws ECivilException {
		UsuarioPortalInterno usuarioPortalInterno = usuarioPortalInternoDAO.recuperarUsuario(cpf);
		if (usuarioPortalInterno == null) {
			return criaNovoUsuarioViaWebRecivil(cpf, listaAutenticacao);
		}
		return atualizaDadosUsuarioViaWebRecivil(usuarioPortalInterno, listaAutenticacao);
	}

	private UsuarioPortalInterno criaNovoUsuarioViaWebRecivil(String cpf, List<AutenticacaoResponse> listaAutenticacao) throws ECivilException {
		UsuarioPortalInterno usuarioPortalInterno = new UsuarioPortalInterno();
		usuarioPortalInterno.setNome(listaAutenticacao.get(0).getNome());
		usuarioPortalInterno.setCpf(cpf);
		usuarioPortalInterno.setSenha(CriptografiaUtil.retornaCriptografadoMD5(cpf));
		usuarioPortalInterno.setEmail(listaAutenticacao.get(0).getEmail());
		Date dataAtual = dataDAO.retornaDataBanco();
		usuarioPortalInterno.setDataInicio(dataAtual);
		usuarioPortalInterno.getUsuarioGrupoUsuariosInicializado().add(montaGrupoUsuarioWebRecivil(listaAutenticacao.get(0), usuarioPortalInterno));
		setaDadosUsuarioCorregedoriaWebRecivil(usuarioPortalInterno, listaAutenticacao, dataAtual);
		usuarioPortalInternoDAO.persiste(usuarioPortalInterno);
		return usuarioPortalInterno;
	}
	
	private void setaDadosUsuarioCorregedoriaWebRecivil(UsuarioPortalInterno usuarioPortalInterno, List<AutenticacaoResponse> listaAutenticacao, Date dataAtual) throws ECivilException {
		for (AutenticacaoResponse autenticacaoResponse : listaAutenticacao) {
			usuarioPortalInterno.getUsuarioServentiaInicializada().add(montaObjetoUsuarioServentiaWebRecivil(autenticacaoResponse, usuarioPortalInterno, dataAtual));
		}
	}

	private UsuarioServentia montaObjetoUsuarioServentiaWebRecivil(AutenticacaoResponse autenticacaoResponse, UsuarioPortalInterno usuarioPortalInterno, Date dataAtual) {
		UsuarioServentia usuarioServentia = new UsuarioServentia();
		usuarioServentia.setCodigoCorregedoria(autenticacaoResponse.getCodigoCorregedoria());
		usuarioServentia.setDataInicio(dataAtual);
		usuarioServentia.setUsuario(usuarioPortalInterno);
		return usuarioServentia;
	}
	
	private UsuarioGrupoUsuario montaGrupoUsuarioWebRecivil(AutenticacaoResponse autenticacaoResponse, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		return montaGrupoUsuario(recuperaGrupoUsuarioPorCodigoWebRecivil(autenticacaoResponse.getCodigoPermissao()), usuarioPortalInterno);
	}
	
	private UsuarioGrupoUsuario montaGrupoUsuario(GrupoUsuario grupoUsuario, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		UsuarioGrupoUsuario usuarioGrupoUsuario = new UsuarioGrupoUsuario();
		usuarioGrupoUsuario.setGrupoUsuario(grupoUsuario);
		usuarioGrupoUsuario.setUsuario(usuarioPortalInterno);
		return usuarioGrupoUsuario;
	}

	private GrupoUsuario recuperaGrupoUsuarioPorCodigoWebRecivil(String codigoPermissao) throws ECivilException {
		if (!Util.ehStringValida(codigoPermissao)) {
			throw new ECivilException("Código de permissão inválido");
		}
		
		if (codigoPermissao.equals("1")) {
			return grupoUsuarioDAO.recuperaGrupoUsuarioPorCodigo(GrupoUsuario.OFICIAL);
		}
		
		if (codigoPermissao.equals("3")) {
			return grupoUsuarioDAO.recuperaGrupoUsuarioPorCodigo(GrupoUsuario.FUNCIONARIO);
		}
		
		throw new ECivilException("Código de permissão não tratado");
	}

	private UsuarioPortalInterno atualizaDadosUsuarioViaWebRecivil(UsuarioPortalInterno usuarioPortalInterno, List<AutenticacaoResponse> listaAutenticacao) throws ECivilException {
		usuarioPortalInterno.setNome(listaAutenticacao.get(0).getNome());
		usuarioPortalInterno.setEmail(listaAutenticacao.get(0).getEmail());
		Date dataAtual = dataDAO.retornaDataBanco();
		verificaAdicaoNovaCorregedoria(usuarioPortalInterno, listaAutenticacao, dataAtual);
		verificaRemocaoNovaCorregedoria(usuarioPortalInterno, listaAutenticacao, dataAtual);
		verificaPermissaoUsuarioWebRecivil(usuarioPortalInterno, listaAutenticacao);
		usuarioPortalInternoDAO.atualiza(usuarioPortalInterno);
		return usuarioPortalInterno;
	}

	private void verificaAdicaoNovaCorregedoria(UsuarioPortalInterno usuarioPortalInterno, List<AutenticacaoResponse> listaAutenticacao, Date dataAtual) {
		for (AutenticacaoResponse autenticacaoResponse : listaAutenticacao) {
			boolean corregedoriaExistente = usuarioPortalInterno.getUsuarioServentiaAtiva().stream().anyMatch(usuServ -> usuServ.getCodigoCorregedoria().equals(autenticacaoResponse.getCodigoCorregedoria()));
			if (!corregedoriaExistente) {
				usuarioPortalInterno.getUsuarioServentiaInicializada().add(montaObjetoUsuarioServentiaWebRecivil(autenticacaoResponse, usuarioPortalInterno, dataAtual));
			}
		}		
	}
	
	private void verificaRemocaoNovaCorregedoria(UsuarioPortalInterno usuarioPortalInterno, List<AutenticacaoResponse> listaAutenticacao, Date dataAtual) {
		for (UsuarioServentia usuarioServentia : usuarioPortalInterno.getUsuarioServentiaAtiva()) {
			boolean corregedoriaExistente = listaAutenticacao.stream().anyMatch(aut -> aut.getCodigoCorregedoria().contentEquals(usuarioServentia.getCodigoCorregedoria()));
			if (!corregedoriaExistente) {
				usuarioServentia.setDataFim(dataAtual);
			}
		}
	}

	private void verificaPermissaoUsuarioWebRecivil(UsuarioPortalInterno usuarioPortalInterno, List<AutenticacaoResponse> listaAutenticacao) throws ECivilException {
		GrupoUsuario grupoUsuario = recuperaGrupoUsuarioPorCodigoWebRecivil(listaAutenticacao.get(0).getCodigoPermissao());
		if (grupoUsuario.getDescricao().equals(GrupoUsuario.FUNCIONARIO)) {
			verificaUsuarioGrupoUsuarioFuncionario(usuarioPortalInterno, grupoUsuario);
		} else if (grupoUsuario.getDescricao().equals(GrupoUsuario.OFICIAL)){
			verificaUsuarioGrupoUsuarioOficial(usuarioPortalInterno, grupoUsuario);
		} else {
			removeUsuarioGrupoUsuarioPorCodigo(usuarioPortalInterno, GrupoUsuario.FUNCIONARIO);
			removeUsuarioGrupoUsuarioPorCodigo(usuarioPortalInterno, GrupoUsuario.OFICIAL);
		}
	}
	
	private void verificaUsuarioGrupoUsuarioFuncionario(UsuarioPortalInterno usuarioPortalInterno, GrupoUsuario grupoUsuarioWebRecivil) throws ECivilException {
		boolean possuiGrupoFuncionario = usuarioPortalInterno.getUsuarioGrupoUsuariosInicializado().stream()
				.anyMatch(usuGrupo -> usuGrupo.getGrupoUsuario().getDescricao().equals(GrupoUsuario.FUNCIONARIO));
		
		if (possuiGrupoFuncionario) {
			return;
		}
		removeUsuarioGrupoUsuarioPorCodigo(usuarioPortalInterno, GrupoUsuario.OFICIAL);
		usuarioPortalInterno.getUsuarioGrupoUsuariosInicializado().add(montaGrupoUsuario(grupoUsuarioWebRecivil, usuarioPortalInterno));
	}
	
	private void verificaUsuarioGrupoUsuarioOficial(UsuarioPortalInterno usuarioPortalInterno, GrupoUsuario grupoUsuarioWebRecivil) throws ECivilException {
		boolean possuiGrupoOficial = usuarioPortalInterno.getUsuarioGrupoUsuariosInicializado().stream()
				.anyMatch(usuGrupo -> usuGrupo.getGrupoUsuario().getDescricao().equals(GrupoUsuario.OFICIAL));
		
		if (possuiGrupoOficial) {
			return;
		}
		removeUsuarioGrupoUsuarioPorCodigo(usuarioPortalInterno, GrupoUsuario.FUNCIONARIO);
		usuarioPortalInterno.getUsuarioGrupoUsuariosInicializado().add(montaGrupoUsuario(grupoUsuarioWebRecivil, usuarioPortalInterno));
	}
	
	private void removeUsuarioGrupoUsuarioPorCodigo(UsuarioPortalInterno usuarioPortalInterno, String codigoGrupoUsuario) {
		Iterator<UsuarioGrupoUsuario> iterator = usuarioPortalInterno.getUsuarioGrupoUsuariosInicializado().iterator();
		while (iterator.hasNext()) {
			UsuarioGrupoUsuario usuarioGrupoUsuario = iterator.next();
			if (usuarioGrupoUsuario.getGrupoUsuario().getDescricao().contentEquals(codigoGrupoUsuario)) {
				usuarioPortalInterno.getUsuarioGrupoUsuarios().remove(usuarioGrupoUsuario);
				break;
			}
		}
	}

	public void removeUsuarioServentia(UsuarioPortalInterno usuarioPortalInterno, UsuarioServentia usuarioServentia) {
		if (usuarioServentia.getId() == null) {
			usuarioPortalInterno.getUsuarioServentia().remove(usuarioServentia);
			return;
		}
		int indexUsuarioServentia = usuarioPortalInterno.getUsuarioServentia().indexOf(usuarioServentia);
		usuarioPortalInterno.getUsuarioServentia().get(indexUsuarioServentia).setDataFim(dataDAO.retornaDataBanco());		
	}

	public void adicionarServentia(UsuarioPortalInterno usuarioPortalInterno) {
		if (serventiaJaAdicionada(usuarioPortalInterno)) {
			return;
		}
		UsuarioServentia usuarioServentia = new UsuarioServentia();
		usuarioServentia.setCodigoCorregedoria(usuarioPortalInterno.getCodigoCorregedoriaCadastroUsuario());
		usuarioServentia.setDataInicio(dataDAO.retornaDataBanco());
		usuarioServentia.setUsuario(usuarioPortalInterno);
		usuarioPortalInterno.getUsuarioServentiaInicializada().add(usuarioServentia);
	}
	
	private boolean serventiaJaAdicionada(UsuarioPortalInterno usuarioPortalInterno) {
		return usuarioPortalInterno.getUsuarioServentiaInicializada().stream()
				.anyMatch(usu -> usu.getDataFim() == null && usu.getCodigoCorregedoria().equals(usuarioPortalInterno.getCodigoCorregedoriaCadastroUsuario()));
	}

}
