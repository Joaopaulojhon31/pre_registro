package ecivil.web.controller.usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ecivil.ejb.bo.AlterarSenhaPortalExternoBO;
import ecivil.ejb.bo.UsuarioPortalExternoBO;
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
import ecivil.ejb.util.Util;
import ecivil.web.controller.BaseController;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class CadastroUsuarioPortalExternoController extends BaseController implements Serializable {

	@EJB
	private UsuarioPortalExternoBO usuarioPortalExternoBO;
	
	@EJB
	private AlterarSenhaPortalExternoBO alterarSenhaPortalExternoBO;
	
	@EJB
	private UsuarioPortalExternoDAO usuarioPortalDAO;
	
	@EJB
	private MunicipioDAO municipioDAO;

	@EJB
	private UfDAO ufDAO;

	@EJB
	private TipoLogradouroDAO tipoLogradouroDAO;
	
	@Inject
	private UsuarioController usuarioController;
	
	private boolean edicao;
	private boolean pesquisaCepRealizada;
	private boolean recadastramento;
	private UsuarioPortalExterno usuarioPortalExterno;
	
	@PostConstruct
	public void inicializar() {
		inicializaUsuarioPortalExterno();
	}

	private void inicializaUsuarioPortalExterno() {
		if (getUsuarioLogadoPortal() == null) {
			instanciarUsuarioPortalExterno();
		} else {
			setUsuarioPortalExterno(usuarioPortalExternoBO.recuperarUsuarioPorId(getUsuarioLogadoPortal().getId()));
			verificaUsuarioRecadastramento();
			recuperarDadosParaEdicao();
			exibeMensagemAtualizacaoCadastro();
		}
	}

	private void verificaUsuarioRecadastramento() {
		setRecadastramento(getUsuarioPortalExterno().isPendenteAtualizacao());
		if (getUsuarioPortalExterno().getListaEnderecoAtivo().isEmpty()) {
			getUsuarioPortalExterno().setEnderecoUsuarioExterno(new EnderecoUsuarioExterno());
			getUsuarioPortalExterno().getListaEnderecoInicializada().add(getUsuarioPortalExterno().getEnderecoUsuarioExterno());
		}
	}

	private void exibeMensagemAtualizacaoCadastro() {
		if (getUsuarioPortalExterno().isPendenteAtualizacao()) {
			abrirModal("modalAtualizacaoCadastro");
		}
	}

	private void instanciarUsuarioPortalExterno() {
		setUsuarioPortalExterno(new UsuarioPortalExterno());
		getUsuarioPortalExterno().setEnderecoUsuarioExterno(new EnderecoUsuarioExterno());
		getUsuarioPortalExterno().getListaEnderecoInicializada().add(getUsuarioPortalExterno().getEnderecoUsuarioExterno());
		if (JSFUtil.get("emailNovoUsuarioPortalExterno") != null) {
			getUsuarioPortalExterno().setEmail((String) JSFUtil.get("emailNovoUsuarioPortalExterno"));
		}
	}
	
	private void recuperarDadosParaEdicao() {
		setEdicao(Boolean.TRUE);
		getUsuarioPortalExterno().setConfirmarEmail(getUsuarioLogadoPortal().getEmail());
		getUsuarioPortalExterno().setConfirmarSenha(getUsuarioLogadoPortal().getSenha());
		getUsuarioPortalExterno().setNovaSenha(getUsuarioLogadoPortal().getSenha());
		getUsuarioPortalExterno().setSenha(getUsuarioLogadoPortal().getSenha());
	}
	
	public String gravar() {
		try {
			usuarioController.atualizaUsuarioLogado();
			recuperarDadosParaEdicao();
			usuarioPortalExternoBO.validarCamposObrigatorios(getUsuarioPortalExterno());
			usuarioPortalExternoBO.grava(getUsuarioPortalExterno());
			return direcionarGravar();
		} catch (ECivilException e) {
			Mensagem.listaErros(e.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível salvar os dados do usuário nesse momento. Tente novamente mais tarde.");
		}
		return "";
	}

	public String direcionarGravar() {
		if (!isEdicao()) {
			JSFUtil.put("novoUsuarioCadastrado", getUsuarioPortalExterno());
			return EnumPaginas.LOGIN.getUrl();
		}
		
		if (isRecadastramento()) {
			JSFUtil.getSession().setAttribute("usuarioLogado", getUsuarioPortalExterno());
			usuarioController.setUsuarioLogado(getUsuarioPortalExterno());
			return EnumPaginas.PRINCIPAL.getUrl();
		}
		
		Mensagem.infoEntreTelas("Dados atualizados com sucesso.");
		redireciona(EnumPaginas.PRINCIPAL.getUrl());
		return "";
	}
	
	public void salvarEdicaoEndereco() {
		try {
			usuarioController.atualizaUsuarioLogado();
			recuperarDadosParaEdicao();
			usuarioPortalExternoBO.validarEnderecoEdicao(getUsuarioPortalExterno().getEnderecoUsuarioExterno());
			setUsuarioPortalExterno(usuarioPortalExternoBO.atualizarEndereco(getUsuarioPortalExterno()));
			Mensagem.infoSemBundle("Dados do endereço atualizado com sucesso.");
			fechaModalStatusDialog();
			fecharModal("modalEditarEndereco");
		} catch (ECivilException e) {
			Mensagem.listaErros(e.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível salvar os dados do endereço nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public void salvarEdicaoSenha() {
		try {
			usuarioController.atualizaUsuarioLogado();
			alterarSenhaPortalExternoBO.alterarSenha(getUsuarioPortalExterno(), getUsuarioLogadoPortal());
			fechaModalStatusDialog();
			fecharModal("modalAlterarSenha");
			Mensagem.infoEntreTelas("Senha alterada com sucesso.");
			redireciona(EnumPaginas.PRINCIPAL.getUrl());
		} catch (ECivilException e) {
			Mensagem.listaErros(e.getListaErros());
		} catch (Exception e) {
			Mensagem.infoEmModal(null, null);
			Mensagem.error("msg.erro.alterar.senha", e.getMessage());
		}
	}
	
	public void cancelarEdicao() {
		setUsuarioPortalExterno(usuarioPortalExternoBO.recuperarUsuarioPorId(getUsuarioPortalExterno().getId()));
		recuperarDadosParaEdicao();
	}
	
	public void setaAdicaoNovoEndereco() {
		setPesquisaCepRealizada(Boolean.FALSE);
		getUsuarioPortalExterno().setEnderecoUsuarioExterno(new EnderecoUsuarioExterno());
		getUsuarioPortalExterno().getEnderecoUsuarioExterno().setEdicaoEndereco(Boolean.TRUE);
		getUsuarioPortalExterno().getEnderecoUsuarioExterno().setEnderecoPadrao(Boolean.FALSE);
	}
	
	public void setaEnderecoEdicaoOuExclusao(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		enderecoUsuarioExterno.setUf(enderecoUsuarioExterno.getMunicipio().getUf());
		getUsuarioPortalExterno().setEnderecoUsuarioExterno(enderecoUsuarioExterno.instanciaNovoObjetoEndereco());
		getUsuarioPortalExterno().getEnderecoUsuarioExterno().setEdicaoEndereco(Boolean.TRUE);
	}
	
	public void excluirEndereco() {
		setUsuarioPortalExterno(usuarioPortalExternoBO.excluirEndereco(getUsuarioPortalExterno()));
		recuperarDadosParaEdicao();
		Mensagem.infoSemBundle("Endereço excluído com sucesso.");
	}
	
	public void buscaCep(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		try {
			setPesquisaCepRealizada(Boolean.FALSE);
			if (!Util.ehStringValida(enderecoUsuarioExterno.getCep()) || enderecoUsuarioExterno.getCep().trim().length() != 8) {
				return;
			}
			usuarioPortalExternoBO.realizaBuscaCep(enderecoUsuarioExterno);
			setPesquisaCepRealizada(Boolean.TRUE);
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível buscar os dados do endereço a partir do CEP nesse momento.");
		}
	}
	
	public boolean desabilitaCamposEndereco(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		if (!isPesquisaCepRealizada()) {
			return Boolean.TRUE;
		}
		return enderecoUsuarioExterno.possuiMunicipio();
	}
	
	public boolean desabilitaCampoTipoLogradouro(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		if (!isPesquisaCepRealizada()) {
			return Boolean.TRUE;
		}
		return enderecoUsuarioExterno.possuiTipoLogradouro();
	}
	
	public boolean desabilitaCampoNomeLogradouro(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		if (!isPesquisaCepRealizada()) {
			return Boolean.TRUE;
		}
		return enderecoUsuarioExterno.possuiNomeLogradouro();
	}
	
	public boolean desabilitaCampoBairro(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		if (!isPesquisaCepRealizada()) {
			return Boolean.TRUE;
		}
		return enderecoUsuarioExterno.possuiBairro();
	}
	
	public boolean exibeCamposNovoCadastro() {
		return !isEdicao() || getUsuarioPortalExterno().isPendenteAtualizacao();
	}
	
	public boolean exibeListaEnderecoEdicao() {
		return isEdicao() && getUsuarioPortalExterno().getListaEnderecoAtivo().stream().anyMatch(end -> end.getId() != null);
	}
	
	public boolean exibeModalEditarExcluirEndereco() {
		return isEdicao() && getUsuarioPortalExterno().getEnderecoUsuarioExterno() != null && getUsuarioPortalExterno().getEnderecoUsuarioExterno().isEdicaoEndereco();
	}
	
	public String voltar() {
		if (isEdicao()) {
			return EnumPaginas.PRINCIPAL.getUrl();
		}
		return EnumPaginas.LOGIN.getUrl();
	}
	
	public List<Uf> getListaUf() {
		return ufDAO.recuperaListaUF();
	}
	
	public List<TipoLogradouro> getListaTipoLogradouro() {
		return tipoLogradouroDAO.listaTodosTipoLogradouro();
	}	
	
	public List<Municipio> recuperaListaMunicipioPorUf(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		List<Municipio> listaMunicipio = new ArrayList<>();
		
		if (enderecoUsuarioExterno != null && enderecoUsuarioExterno.getUf() != null) {
			listaMunicipio = municipioDAO.listaTodosMunicipioPorSiglaUf(enderecoUsuarioExterno.getUf().getSigla());
		}
		
		fechaModalStatusDialog();
		return listaMunicipio;
	}
	
	public boolean exibeTermosECondicoes() {
		return getUsuarioPortalExterno().isPendenteAtualizacao() || getUsuarioPortalExterno().getId() == null;
	}
	
	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}
	
	public boolean isPesquisaCepRealizada() {
		return pesquisaCepRealizada;
	}
	
	public boolean isRecadastramento() {
		return recadastramento;
	}

	public void setRecadastramento(boolean recadastramento) {
		this.recadastramento = recadastramento;
	}

	public void setPesquisaCepRealizada(boolean pesquisaCepRealizada) {
		this.pesquisaCepRealizada = pesquisaCepRealizada;
	}
	
	public UsuarioPortalExterno getUsuarioPortalExterno() {
		return usuarioPortalExterno;
	}

	public void setUsuarioPortalExterno(UsuarioPortalExterno usuarioPortalExterno) {
		this.usuarioPortalExterno = usuarioPortalExterno;
	}

}
