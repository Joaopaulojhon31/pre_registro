package ecivil.ejb.bo;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.MunicipioDAO;
import ecivil.ejb.dao.TipoLogradouroDAO;
import ecivil.ejb.dao.UfDAO;
import ecivil.ejb.dao.UsuarioPortalExternoDAO;
import ecivil.ejb.entidade.EnderecoUsuarioExterno;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.TipoLogradouro;
import ecivil.ejb.entidade.Uf;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.IntegradorCorreiosWS;
import ecivil.ejb.ws.cliente.rest.response.integradorcorreios.ConsultaCepResponse;
import ecivil.ejb.ws.cliente.rest.response.integradorcorreios.MunicipioResponse;
import ecivil.ejb.ws.cliente.rest.response.integradorcorreios.TipoLogradouroResponse;

@Stateless
public class UsuarioPortalExternoBO {

	@EJB
	private EnderecoUsuarioExternoBO enderecoUsuarioExternoBO;
	
	@EJB
	private UsuarioPortalExternoDAO usuarioPortalExternoDAO;

	@EJB
	private DataDAO dataDAO;
	
	@EJB
	private TipoLogradouroDAO tipoLogradouroDAO;
	
	@EJB
	private MunicipioDAO municipioDAO;
	
	@EJB
	private UfDAO ufDAO;
	
	@EJB
	private EmailBO emailBO;
	
	@EJB
	private IntegradorCorreiosWS integradorCorreiosWS;
	
	public void validarCamposObrigatorios(UsuarioPortalExterno usuario) throws Exception {
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();

		validarObrigatoriedadeNome(usuario, eCivilException);
		validarObrigatoriedadeCPF(usuario, eCivilException);
		validarCpfValido(usuario, eCivilException);
		validarObrigatoriedadeEmail(usuario, eCivilException);
		validarEmailValido(usuario, eCivilException);
		validarObrigatoriedadeConfirmarEmail(usuario, eCivilException);
		validarConfirmarEmailValido(usuario, eCivilException);
		validarIgualdadeEmail(usuario, eCivilException);
		validarObrigatoriedadeTelefoneCelular(usuario, eCivilException);
		validarListaEndereco(usuario.getListaEndereco(), Boolean.TRUE, eCivilException);
		validarObrigatoriedadeSenha(usuario, eCivilException);
		validarObrigatoriedadeConfirmarSenha(usuario, eCivilException);
		validarIgualdadeSenhas(usuario, eCivilException);
		validarUsuarioJaCadastrado(usuario, eCivilException);
		validarTermoAceite(usuario, eCivilException);
		
		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}

	private void validarObrigatoriedadeCPF(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiCpf()) {
			eCivilException.adicionaErroNaLista("msg.campo.cpf.usuario.obrigatorio");
		}
	}

	private void validarCpfValido(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiCpf()) {
			if (!Util.isValidCPF(usuario.getCpf())) {
				eCivilException.adicionaErroNaLista("msg.campo.cpf.invalido");
			}
		}
	}

	private void validarObrigatoriedadeNome(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiNome()) {
			eCivilException.adicionaErroNaLista("msg.campo.nome.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeEmail(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiEmail()) {
			eCivilException.adicionaErroNaLista("msg.campo.email.usuario.obrigatorio");
		}
	}

	private void validarEmailValido(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiEmail()) {
			if (!Util.isEmailValido(usuario.getEmail())) {
				eCivilException.adicionaErroNaLista("msg.campo.email.invalido");
			}
		}
	}

	private void validarObrigatoriedadeConfirmarEmail(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiConfirmarEmail()) {
			eCivilException.adicionaErroNaLista("msg.campo.confirmar.email.usuario.obrigatorio");
		}
	}

	private void validarConfirmarEmailValido(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiConfirmarEmail()) {
			if (!Util.isEmailValido(usuario.getConfirmarEmail())) {
				eCivilException.adicionaErroNaLista("msg.campo.confirmar.email.invalido");
			}
		}
	}
	
	private void validarIgualdadeEmail(UsuarioPortalExterno usuario, ECivilException eCivilException) {
		if (usuario.possuiEmail() && usuario.possuiConfirmarEmail()) {
			if (!usuario.getEmail().equals(usuario.getConfirmarEmail())) {
				eCivilException.adicionaErroNaLista("msg.campo.email.diferente.confirmar.email");
			}
		}
	}
	
	private void validarObrigatoriedadeTelefoneCelular(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiTelefone() && !usuario.possuiCelular()) {
			eCivilException.adicionaErroNaLista("msg.campo.telefone.celular.usuario.obrigatorio");
		}
		
		if (usuario.possuiCelular() && !Util.isValidTelefoneCelularComDdd(usuario.getCelular())) {
			eCivilException.adicionaErroNaLista("msg.campo.celular.invalido");
		}
		
		if (usuario.possuiTelefone() && !Util.isValidTelefoneCelularComDdd(usuario.getTelefone())) {
			eCivilException.adicionaErroNaLista("msg.campo.telefone.invalido");
		}
	}
	
	public void validarObrigatoriedadeSenha(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.nova.senha.obrigatorio");
		}
	}

	public void validarObrigatoriedadeConfirmarSenha(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (!usuario.possuiConfirmarSenha()) {
			eCivilException.adicionaErroNaLista("msg.campo.confirmar.senha.usuario.obrigatorio");
		}
	}

	private void validarIgualdadeSenhas(UsuarioPortalExterno usuario, ECivilException eCivilException) {
		if (usuario.possuiSenha() && usuario.possuiConfirmarSenha()) {
			if (!usuario.getSenha().equals(usuario.getConfirmarSenha())) {
				eCivilException.adicionaErroNaLista("msg.campo.senha.diferente.confirmar.senha");
			}
		}
	}

	public void validarUsuarioJaCadastrado(UsuarioPortalExterno usuario, ECivilException eCivilException) throws Exception {
		if (usuario.possuiCpf() && !usuario.isEdicao()) {
			UsuarioPortalExterno usuarioCadastrado = usuarioPortalExternoDAO.recuperarUsuarioPorCpf(usuario.getCpf());
			if (usuarioCadastrado != null) {
				eCivilException.adicionaErroNaLista("msg.usuario.cadastrado", Util.addMask("###.###.###-##", usuario.getCpf()));
			}
		}
	}
	
	private void validarTermoAceite(UsuarioPortalExterno usuario, ECivilException eCivilException) {
		if (!usuario.isTermoAceite()) {
			eCivilException.adicionaErroNaLista("msg.campo.termo.aceite.obrigatorio");
		}
	}
	
	private void validarListaEndereco(List<EnderecoUsuarioExterno> listaEndereco, boolean ehNovoUsuario, ECivilException eCivilException) throws Exception {
		if (listaEndereco == null || listaEndereco.isEmpty()) {
			eCivilException.adicionaErroNaLista("msg.endereco.usuario.obrigatorio");
			return;
		}
		
		for (EnderecoUsuarioExterno endereco : listaEndereco) {
			validarEndereco(endereco, ehNovoUsuario, eCivilException);
		}
	}

	public void validarEnderecoEdicao(EnderecoUsuarioExterno endereco) throws Exception {
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();
		validarEndereco(endereco, Boolean.FALSE, eCivilException);
		validaIdentificadorEndereco(endereco, eCivilException);
		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}
	
	private void validaIdentificadorEndereco(EnderecoUsuarioExterno endereco, ECivilException eCivilException) {
		if (!endereco.possuiIdentificador()) {
			eCivilException.adicionaErroNaLista("msg.campo.identificador.endereco.usuario.obrigatorio");
		}
	}

	private void validarEndereco(EnderecoUsuarioExterno endereco, boolean ehNovoUsuario, ECivilException eCivilException) throws Exception {
		validarObrigatoriedadeDestinatario(endereco, ehNovoUsuario, eCivilException);
		validarObrigatoriedadeCEP(endereco, eCivilException);
		validarCEPValido(endereco, eCivilException);
		validarObrigatoriedadeMunicipio(endereco, eCivilException);
		validarObrigatoriedadeUF(endereco, eCivilException);
		validarObrigatoriedadeTipoLogradouro(endereco, eCivilException);
		validarObrigatoriedadeLogradouro(endereco, eCivilException);
		validarObrigatoriedadeNumeroLogradouro(endereco, eCivilException);
		validarObrigatoriedadeBairro(endereco, eCivilException);
	}

	private void validarObrigatoriedadeDestinatario(EnderecoUsuarioExterno endereco, boolean ehNovoUsuario, ECivilException eCivilException) {
		if (!ehNovoUsuario && !endereco.possuiDestinatario()) {
			eCivilException.adicionaErroNaLista("msg.campo.destinatario.usuario.obrigatorio");
		}		
	}

	private void validarObrigatoriedadeCEP(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiCep()) {
			eCivilException.adicionaErroNaLista("msg.campo.cep.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeTipoLogradouro(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiTipoLogradouro()) {
			eCivilException.adicionaErroNaLista("msg.campo.tipo.logradouro.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeLogradouro(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiNomeLogradouro()) {
			eCivilException.adicionaErroNaLista("msg.campo.logradouro.usuario.obrigatorio");
		}
	}
	
	private void validarObrigatoriedadeNumeroLogradouro(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiNumeroLogradouro()) {
			eCivilException.adicionaErroNaLista("msg.campo.numero.logaradouro.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeBairro(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiBairro()) {
			eCivilException.adicionaErroNaLista("msg.campo.bairro.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeUF(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiUF()) {
			eCivilException.adicionaErroNaLista("msg.campo.uf.usuario.obrigatorio");
		}
	}

	private void validarObrigatoriedadeMunicipio(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (!endereco.possuiMunicipio()) {
			eCivilException.adicionaErroNaLista("msg.campo.municipio.usuario.obrigatorio");
		}
	}
	
	private void validarCEPValido(EnderecoUsuarioExterno endereco, ECivilException eCivilException) throws Exception {
		if (endereco.possuiCep()) {
			if (!Util.isCepValido(endereco.getCep())) {
				eCivilException.adicionaErroNaLista("msg.campo.cep.invalido");
			}
		}
	}

	public void grava(UsuarioPortalExterno usuarioPortalExterno) {
		if (usuarioPortalExterno.isEdicao()) {
			verificaDadosUsuarioRecadastramento(usuarioPortalExterno);
			usuarioPortalExternoDAO.atualiza(usuarioPortalExterno);
		} else {
			gravaNovoUsuario(usuarioPortalExterno);
		}
	}

	private void verificaDadosUsuarioRecadastramento(UsuarioPortalExterno usuarioPortalExterno) {
		if (usuarioPortalExterno.isPendenteAtualizacao()) {
			usuarioPortalExterno.setSenha(CriptografiaUtil.retornaCriptografadoMD5(usuarioPortalExterno.getSenha()));
			usuarioPortalExterno.setPendenteAtualizacao(Boolean.FALSE);
			if (ehPrimeiroEndereco(usuarioPortalExterno)) {
				setaDadosNovoUsuarioEndereco(usuarioPortalExterno, dataDAO.retornaDataBanco());
			}
		}		
	}

	private boolean ehPrimeiroEndereco(UsuarioPortalExterno usuarioPortalExterno) {
		return usuarioPortalExterno.getListaEnderecoAtivo().size() == 1 && usuarioPortalExterno.getListaEnderecoAtivo().get(0).getId() == null;
	}

	private void gravaNovoUsuario(UsuarioPortalExterno usuarioPortalExterno) {
		Date dataAtual = dataDAO.retornaDataBanco();
		setaDadosNovoUsuario(usuarioPortalExterno, dataAtual);
		setaDadosNovoUsuarioEndereco(usuarioPortalExterno, dataAtual);
		usuarioPortalExternoDAO.persiste(usuarioPortalExterno);
		emailBO.enviaEmailCadastroRealizado(usuarioPortalExterno.getEmail(), usuarioPortalExterno.getNome());
	}

	private void setaDadosNovoUsuario(UsuarioPortalExterno usuarioPortalExterno, Date dataAtual) {
		usuarioPortalExterno.setSenha(CriptografiaUtil.retornaCriptografadoMD5(usuarioPortalExterno.getSenha()));
		usuarioPortalExterno.setDataInicio(dataAtual);		
	}

	private void setaDadosNovoUsuarioEndereco(UsuarioPortalExterno usuarioPortalExterno, Date dataAtual) {
		usuarioPortalExterno.getEnderecoUsuarioExterno().setIdentificadorEndereco("Padrão");
		usuarioPortalExterno.getEnderecoUsuarioExterno().setDataInicio(dataAtual);
		usuarioPortalExterno.getEnderecoUsuarioExterno().setUsuarioPortalExterno(usuarioPortalExterno);
		usuarioPortalExterno.getEnderecoUsuarioExterno().setEnderecoPadrao(Boolean.TRUE);
		usuarioPortalExterno.getEnderecoUsuarioExterno().setDestinatario(usuarioPortalExterno.getNome());
	}

	public UsuarioPortalExterno recuperarUsuarioPorId(Long id) {
		return usuarioPortalExternoDAO.recuperarUsuarioPorId(id);
	}
	
	public UsuarioPortalExterno recuperarUsuarioPorCpf(String cpf) {
		return usuarioPortalExternoDAO.recuperarUsuarioPorCpf(cpf);
	}

	public UsuarioPortalExterno excluirEndereco(UsuarioPortalExterno usuarioPortalExterno) {
		enderecoUsuarioExternoBO.excluirEndereco(usuarioPortalExterno.getEnderecoUsuarioExterno());
		return recuperarUsuarioPorId(usuarioPortalExterno.getId());
	}
	
	public UsuarioPortalExterno atualizarEndereco(UsuarioPortalExterno usuarioPortalExterno) {
		if (usuarioPortalExterno.getEnderecoUsuarioExterno().getId() != null) {
			atualizaDadosEnderecoUsuarioExterno(usuarioPortalExterno);
		}
		enderecoUsuarioExternoBO.atualizarDadosEndereco(usuarioPortalExterno);
		UsuarioPortalExterno usuarioPortalExternoAtualizado = recuperarUsuarioPorId(usuarioPortalExterno.getId());
		usuarioPortalExternoAtualizado.setConfirmarEmail(usuarioPortalExternoAtualizado.getEmail());
		usuarioPortalExternoAtualizado.setConfirmarSenha(usuarioPortalExternoAtualizado.getSenha());
		usuarioPortalExternoAtualizado.setNovaSenha(usuarioPortalExternoAtualizado.getSenha());
		//usuarioPortalExternoAtualizado.setConfirmarSenha(usuarioPortalExternoAtualizado.getSenha());
		return usuarioPortalExternoAtualizado;
	}

	private void atualizaDadosEnderecoUsuarioExterno(UsuarioPortalExterno usuarioPortalExterno) {
		EnderecoUsuarioExterno enderecoEditado = usuarioPortalExterno.getEnderecoUsuarioExterno();
		EnderecoUsuarioExterno enderecoUsuario = usuarioPortalExterno.getListaEndereco().stream().filter(end -> end.getId().equals(enderecoEditado.getId())).findFirst().orElse(null);
		enderecoUsuario.setIdentificadorEndereco(enderecoEditado.getIdentificadorEndereco());
		enderecoUsuario.setBairro(enderecoEditado.getBairro());
		enderecoUsuario.setCep(enderecoEditado.getCep());
		enderecoUsuario.setComplemento(enderecoEditado.getComplemento());
		enderecoUsuario.setDataFim(enderecoEditado.getDataFim());
		enderecoUsuario.setDataInicio(enderecoEditado.getDataInicio());
		enderecoUsuario.setEnderecoPadrao(enderecoEditado.isEnderecoPadrao());
		enderecoUsuario.setMunicipio(enderecoEditado.getMunicipio());
		enderecoUsuario.setNomeLogradouro(enderecoEditado.getNomeLogradouro());
		enderecoUsuario.setNumeroLogradouro(enderecoEditado.getNumeroLogradouro());
		enderecoUsuario.setTipoLogradouro(enderecoEditado.getTipoLogradouro());
		enderecoUsuario.setUf(enderecoEditado.getUf());
		enderecoUsuario.setUsuarioPortalExterno(enderecoEditado.getUsuarioPortalExterno());
		enderecoUsuario.setDestinatario(enderecoEditado.getDestinatario());
	}

	public void realizaBuscaCep(EnderecoUsuarioExterno enderecoUsuarioExterno) throws ECivilException {
		ConsultaCepResponse cepResponse = integradorCorreiosWS.consultarCep(enderecoUsuarioExterno.getCep());
		
		if (cepResponse == null) {
			enderecoUsuarioExterno.setBairro(null);
			enderecoUsuarioExterno.setTipoLogradouro(null);
			enderecoUsuarioExterno.setNomeLogradouro(null);
			enderecoUsuarioExterno.setMunicipio(null);
			enderecoUsuarioExterno.setUf(null);
			return;
		}
		
		enderecoUsuarioExterno.setTipoLogradouro(recuperaTipoLogradouro(cepResponse.getTipoLogradouro()));
		enderecoUsuarioExterno.setNomeLogradouro(cepResponse.getLogradouro());
		enderecoUsuarioExterno.setMunicipio(recuperaMunicipioPorCodRfb(cepResponse.getMunicipio()));
		enderecoUsuarioExterno.setUf(recuperaUfPorSigla(cepResponse.getMunicipio()));
		
		if (Util.ehStringValida(cepResponse.getBairro())) {
			enderecoUsuarioExterno.setBairro(cepResponse.getBairro());
		}
	}

	private TipoLogradouro recuperaTipoLogradouro(TipoLogradouroResponse tipoLogradouro) {
		if (tipoLogradouro == null || !Util.ehStringValida(tipoLogradouro.getCodigoRfb())) {
			return null;
		}
		return tipoLogradouroDAO.recuperaTipoLogradouroPorCodigoRfb(tipoLogradouro.getCodigoRfb());
	}

	private Municipio recuperaMunicipioPorCodRfb(MunicipioResponse municipio) {
		if (municipio == null || !Util.ehStringValida(municipio.getCodigoRfb())) {
			return null;
		}
		return municipioDAO.recuperaMunicipioPorCodRfb(municipio.getCodigoRfb());
	}
	
	private Uf recuperaUfPorSigla(MunicipioResponse municipio) {
		if (municipio == null || municipio.getUf() == null || !Util.ehStringValida(municipio.getUf().getSigla())) {
			return null;
		}
		return ufDAO.recuperarUF(municipio.getUf().getSigla());
	}
	
}
