package ecivil.adm.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.enumerator.EnumPaginas;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class PrincipalController extends BaseController implements Serializable {

	@PostConstruct
	public void inicializa() {
		if (!getUsuarioLogadoPortal().possuiSaldoAtualConsultado()) {
			recuperaSaldoAtual();
		}
	}
	
	@Override
	public List<EnumPaginas> listBreadCrumb() {
		List<EnumPaginas> bread = new ArrayList<EnumPaginas>();
		bread.add(EnumPaginas.PRINCIPAL);
		return bread;
	}
	
	public String direcionaConsultarPedidosParaServentia() {
		return EnumPaginas.PESQUISAR_PEDIDOS_CERTIDAO.getUrl();
	}
	
	public String direcionaConsultarPedidosRealizadoPelaServentia() {
		return EnumPaginas.PESQUISAR_PEDIDOS_REALIZADOS.getUrl();
	}
	
	public String direcionaRealizarPedidoCertidao() {
		return EnumPaginas.REALIZAR_PEDIDO_CERTIDAO.getUrl();
	}
	
	public String direcionaSolicitarSaque() {
		return EnumPaginas.SOLICITAR_SAQUE.getUrl();
	}
	
	public String direcionaConfirmarSaque() {
		return EnumPaginas.CONFIRMAR_SAQUE.getUrl();
	}
	
	public String direcionaExtratoContaDigital() {
		return EnumPaginas.EXTRATO_CONTA_DIGITAL.getUrl();
	}
	
	public String direcionaAdicionarSaldo() {
		return EnumPaginas.ADICIONAR_SALDO.getUrl();
	}
	
	public String direcionaValidarHashCertidao() {
		return EnumPaginas.VALIDAR_HASH_CERTIDAO.getUrl();
	}
	
	public String direcionaAlterarServentia() {
		return EnumPaginas.SELECIONAR_SERVENTIA.getUrl();
	}

	public String direcionaInicial() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public String direcionaCadastrarUsuario() {
		return EnumPaginas.PESQUISAR_USUARIO_PORTAL_INTERNO.getUrl();
	}
	
	public String direcionaDeclaracoesPreRegistroPrePosto() {
		return EnumPaginas.DECLARACOES_PRE_REGISTRO_PRE_POSTO.getUrl();
	}


	public String direcionaCadastrarGrupo() {
		return EnumPaginas.PESQUISAR_GRUPO_USUARIO_INTERNO.getUrl();
	}

	public String direcionaCadastrarPermissao() {
		return EnumPaginas.PESQUISAR_PERMISSAO_USUARIO_INTERNO.getUrl();
	}
	
	public String direcionaAlterarISSQN() {
		return EnumPaginas.SELECIONAR_ALIQUOTA_ISSQN.getUrl();
	}
	
	public String direcionaPesquisaPreRegistro() {
		return EnumPaginas.PESQUISA_PRE_REGISTRO.getUrl();
	}
	public String direcionaPreRegistroPrePosto() {
		return EnumPaginas.PRE_REGISTRO_PRE_POSTO.getUrl();
	}

	public String direcionaPaginaInicial() {
		try {
			JSFUtil.redireciona("/e-civilAdm/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String direcionaAlterarSenhaUsuarioPortalInterno() {
		return EnumPaginas.ALTERAR_SENHA_USUARIO_PORTAL_INTERNO.getUrl();
	}

	public String direcionarCadastrarUsuarioPortalInterno() {
		JSFUtil.put("usuarioPortalInterno", getUsuarioLogadoPortal());
		return EnumPaginas.CADASTRAR_USUARIO_PORTAL_INTERNO.getUrl();
	}
	
	public String direcionaSitePrincipal() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public boolean isExibeMenuPedidosCertidao() {
		return possuiAcessoRecurso(EnumPaginas.PESQUISAR_PEDIDOS_CERTIDAO.name());
	}
	
	public boolean isExibeMenuPedidosSolicitados() {
		return possuiAcessoRecurso(EnumPaginas.PESQUISAR_PEDIDOS_REALIZADOS.name());
	}
	
	public boolean isExibeMenuRealizarPedido() {
		return possuiAcessoRecurso(EnumPaginas.REALIZAR_PEDIDO_CERTIDAO.name());
	}
	
	public boolean isExibeMenuSolicitarSaque() {
		return possuiAcessoRecurso(EnumPaginas.SOLICITAR_SAQUE.name());
	}
	
	public boolean isExibeMenuExtratoContaDigital() {
		return possuiAcessoRecurso(EnumPaginas.EXTRATO_CONTA_DIGITAL.name());
	}
	
	public boolean isExibeMenuAdicionarSaldo() {
		return possuiAcessoRecurso(EnumPaginas.ADICIONAR_SALDO.name());
	}
	
	public boolean isExibeMenuConfirmarSolicitacaoSaque() {
		return possuiAcessoRecurso(EnumPaginas.CONFIRMAR_SAQUE.name());
	}
	
	public boolean isExibeMenuValidarHashCertidao() {
		return possuiAcessoRecurso(EnumPaginas.VALIDAR_HASH_CERTIDAO.name());
	}
	
	public boolean isExibeMenuGerenciarUsuario() {
		return possuiAcessoRecurso(EnumPaginas.PESQUISAR_USUARIO_PORTAL_INTERNO.name());
	}
	
	public boolean isExibeMenuGerenciarGrupoUsuario() {
		return possuiAcessoRecurso(EnumPaginas.PESQUISAR_GRUPO_USUARIO_INTERNO.name());
	}
	
	public boolean isExibeMenuGerenciarPermissoes() {
		return possuiAcessoRecurso(EnumPaginas.PESQUISAR_PERMISSAO_USUARIO_INTERNO.name());
	}
	
	public boolean isExibeMenuAlterarAliquotaISSQN() {
		return possuiAcessoRecurso(EnumPaginas.SELECIONAR_ALIQUOTA_ISSQN.name());
	}
	
	public boolean isExibeMenuAlterarServentia() {
		return possuiAcessoRecurso(EnumPaginas.SELECIONAR_SERVENTIA.name()) && !getUsuarioLogadoPortal().possuiApenasUmaServentia();
	}
	
	public boolean isExibeMenuPesquisarPreRegistro() {
		return possuiAcessoRecurso(EnumPaginas.PESQUISA_PRE_REGISTRO.name());
	}
	
}
