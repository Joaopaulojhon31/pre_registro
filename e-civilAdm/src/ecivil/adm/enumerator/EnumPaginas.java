package ecivil.adm.enumerator;

public enum EnumPaginas {

	LOGIN("/pages/usuario/login.xhtml", "Login"), 
	PRINCIPAL("/pages/principal.xhtml", "Principal"),
	
	CADASTRAR_USUARIO_PORTAL_INTERNO("/pages/usuario/cadastrarUsuarioPortalInterno.xhtml", "Cadastrar usuário portal interno"),
	PESQUISAR_PERMISSAO_USUARIO_INTERNO("/pages/usuario/pesquisarPermissaoUsuarioInterno.xhtml", "Pesquisar permissão usuário interno"),
	CADASTRAR_PERMISSAO_USUARIO_INTERNO("/pages/usuario/cadastrarPermissaoUsuarioInterno.xhtml", "Cadastrar permissão usuário interno"),
	PESQUISAR_GRUPO_USUARIO_INTERNO("/pages/usuario/pesquisarGrupoUsuarioInterno.xhtml", "Pequisar grupo usuário interno"),
	CADASTRAR_GRUPO_USUARIO_INTERNO("/pages/usuario/cadastrarGrupoUsuarioInterno.xhtml", "Cadastrar grupo usuário interno"),
	ALTERAR_SENHA_USUARIO_PORTAL_INTERNO("/pages/usuario/alterarSenhaUsuarioPortalInterno.xhtml", "Alterar senha usuário portal interno"),
	PESQUISAR_USUARIO_PORTAL_INTERNO("/pages/usuario/pesquisarUsuarioPortalInterno.xhtml", "Pesquisar usuário portal interno"),
	SELECIONAR_SERVENTIA("/pages/usuario/selecionarServentia.xhtml", "Seleção de Serventia"),
	SELECIONAR_ALIQUOTA_ISSQN("/pages/usuario/selecionarAliquotaISSQN.xhtml", "Alteração de Alíquota ISSQN"),
	
	PESQUISAR_PEDIDOS_CERTIDAO("/pages/pedidos/pesquisaPedidoCertidao.xhtml", "Pesquisa Pedidos de Certidão"),
	PESQUISAR_PEDIDOS_REALIZADOS("/pages/pedidos/pesquisaPedidoRealizado.xhtml", "Pesquisa Pedidos de Certidão Realizados pela Serventia"),
	DADOS_PEDIDO_CERTIDAO("/pages/pedidos/dadosPedidoCertidao.xhtml", "Dados do Pedidos de Certidão"),
	REALIZAR_PEDIDO_CERTIDAO("/pages/pedidos/realizarPedidoCertidao.xhtml", "Realizar Pedido de Certidão"),
	RESULTADO_BUSCA_CERTIDAO("/pages/pedidos/resultadoBuscaCertidao.xhtml", "Resultado da busca da certidão"),
	FORMULARIO_PEDIDO_CERTIDAO("/pages/pedidos/formularioPedidoCertidao.xhtml", "Preenchimento dos dados da certidão para pedido via formulário"),
	SOLICITACAO_CERTIDAO_REALIZADA("/pages/pedidos/solicitacaoCertidaoRealizada.xhtml", "Solicitação de certidão realizada"),
	VALIDAR_HASH_CERTIDAO("/pages/pedidos/validarHashCertidao.xhtml", "Validar Hash Certidão"),
	
	PESQUISA_PRE_REGISTRO("/pages/preRegistro/pesquisaPreRegistro.xhtml", "Pre-Registro"),
	
	SOLICITAR_SAQUE("/pages/contadigital/solicitarSaque.xhtml", "Solicitar saque"),
	CONFIRMAR_SAQUE("/pages/contadigital/confirmarSaque.xhtml", "Confirmar saque"),
	EXTRATO_CONTA_DIGITAL("/pages/contadigital/extrato.xhtml", "Extrato conta digital"),
	ADICIONAR_SALDO("/pages/contadigital/adicionarSaldo.xhtml", "Adicionar saldo");

	private String url;
	private String title;

	private EnumPaginas(String url, String title) {
		this.url = url;
		this.title = title;
	}

	public String getUrl() {
		return url;
	}
	
	public String getUrlJsf() {
		return url.replace("xhtml", "jsf");
	}
	
	public String getUrlTratada() {
		return url.replace(".xhtml", "");
	}

	public String getTitle() {
		return title;
	}

}
