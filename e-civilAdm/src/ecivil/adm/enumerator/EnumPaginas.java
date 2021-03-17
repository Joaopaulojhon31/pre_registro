package ecivil.adm.enumerator;

public enum EnumPaginas {

	LOGIN("/pages/usuario/login.xhtml", "Login"), 
	PRINCIPAL("/pages/principal.xhtml", "Principal"),
	
	CADASTRAR_USUARIO_PORTAL_INTERNO("/pages/usuario/cadastrarUsuarioPortalInterno.xhtml", "Cadastrar usu�rio portal interno"),
	PESQUISAR_PERMISSAO_USUARIO_INTERNO("/pages/usuario/pesquisarPermissaoUsuarioInterno.xhtml", "Pesquisar permiss�o usu�rio interno"),
	CADASTRAR_PERMISSAO_USUARIO_INTERNO("/pages/usuario/cadastrarPermissaoUsuarioInterno.xhtml", "Cadastrar permiss�o usu�rio interno"),
	PESQUISAR_GRUPO_USUARIO_INTERNO("/pages/usuario/pesquisarGrupoUsuarioInterno.xhtml", "Pequisar grupo usu�rio interno"),
	CADASTRAR_GRUPO_USUARIO_INTERNO("/pages/usuario/cadastrarGrupoUsuarioInterno.xhtml", "Cadastrar grupo usu�rio interno"),
	ALTERAR_SENHA_USUARIO_PORTAL_INTERNO("/pages/usuario/alterarSenhaUsuarioPortalInterno.xhtml", "Alterar senha usu�rio portal interno"),
	PESQUISAR_USUARIO_PORTAL_INTERNO("/pages/usuario/pesquisarUsuarioPortalInterno.xhtml", "Pesquisar usu�rio portal interno"),
	SELECIONAR_SERVENTIA("/pages/usuario/selecionarServentia.xhtml", "Sele��o de Serventia"),
	SELECIONAR_ALIQUOTA_ISSQN("/pages/usuario/selecionarAliquotaISSQN.xhtml", "Altera��o de Al�quota ISSQN"),
	
	PESQUISAR_PEDIDOS_CERTIDAO("/pages/pedidos/pesquisaPedidoCertidao.xhtml", "Pesquisa Pedidos de Certid�o"),
	PESQUISAR_PEDIDOS_REALIZADOS("/pages/pedidos/pesquisaPedidoRealizado.xhtml", "Pesquisa Pedidos de Certid�o Realizados pela Serventia"),
	DADOS_PEDIDO_CERTIDAO("/pages/pedidos/dadosPedidoCertidao.xhtml", "Dados do Pedidos de Certid�o"),
	REALIZAR_PEDIDO_CERTIDAO("/pages/pedidos/realizarPedidoCertidao.xhtml", "Realizar Pedido de Certid�o"),
	RESULTADO_BUSCA_CERTIDAO("/pages/pedidos/resultadoBuscaCertidao.xhtml", "Resultado da busca da certid�o"),
	FORMULARIO_PEDIDO_CERTIDAO("/pages/pedidos/formularioPedidoCertidao.xhtml", "Preenchimento dos dados da certid�o para pedido via formul�rio"),
	SOLICITACAO_CERTIDAO_REALIZADA("/pages/pedidos/solicitacaoCertidaoRealizada.xhtml", "Solicita��o de certid�o realizada"),
	VALIDAR_HASH_CERTIDAO("/pages/pedidos/validarHashCertidao.xhtml", "Validar Hash Certid�o"),
	
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
