package ecivil.web.enumerator;

public enum EnumPaginas {
	
	ALTERAR_SENHA_USUARIO_PORTAL_EXTERNO("/pages/usuario/alterarSenhaUsuarioPortalExterno.jsf", "Alterar senha usu�rio portal externo"),
	CADASTRO_USUARIO_PORTAL_EXTERNO("/pages/usuario/cadastroUsuarioPortalExterno.jsf", "Cadastro usu�rio portal externo"),
	SUCESSO_CADASTRO("/pages/usuario/sucessoCadastro.jsf", "Sucesso cadastro"), 
	LOGIN("/pages/usuario/login.jsf", "Login"),
	PRINCIPAL("/pages/principal.jsf", "Principal"), 
	SOBRE("/pages/sobre.jsf", "Sobre"), 

	BUSCAR_CERTIDAO("/pages/certidao/buscarCertidao.jsf", "Buscar Certid�o"),
	RESULTADO_BUSCA_CERTIDAO("/pages/certidao/resultadoBuscaCertidao.jsf", "Resultado da busca da certid�o"),
	FORMULARIO_PEDIDO_CERTIDAO("/pages/certidao/formularioPedidoCertidao.jsf", "Preenchimento dos dados da certid�o para pedido via formul�rio"),
	SOLICITACAO_CERTIDAO_REALIZADA("/pages/certidao/solicitacaoCertidaoRealizada.jsf", "Solicita��o de certid�o realizada"),
	CONSULTAR_CERTIDAO("/pages/certidao/consultarCertidao.jsf", "Consultar pedido de certid�o"),
	VALIDAR_HASH_CERTIDAO("/pages/certidao/validarHashCertidao.jsf", "Validar hash certid�o");

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

	public String getTitle() {
		return title;
	}
}
