package ecivil.web.enumerator;

public enum EnumPaginas {
	
	ALTERAR_SENHA_USUARIO_PORTAL_EXTERNO("/pages/usuario/alterarSenhaUsuarioPortalExterno.jsf", "Alterar senha usuário portal externo"),
	CADASTRO_USUARIO_PORTAL_EXTERNO("/pages/usuario/cadastroUsuarioPortalExterno.jsf", "Cadastro usuário portal externo"),
	SUCESSO_CADASTRO("/pages/usuario/sucessoCadastro.jsf", "Sucesso cadastro"), 
	LOGIN("/pages/usuario/login.jsf", "Login"),
	PRINCIPAL("/pages/principal.jsf", "Principal"), 
	SOBRE("/pages/sobre.jsf", "Sobre"), 

	BUSCAR_CERTIDAO("/pages/certidao/buscarCertidao.jsf", "Buscar Certidão"),
	RESULTADO_BUSCA_CERTIDAO("/pages/certidao/resultadoBuscaCertidao.jsf", "Resultado da busca da certidão"),
	FORMULARIO_PEDIDO_CERTIDAO("/pages/certidao/formularioPedidoCertidao.jsf", "Preenchimento dos dados da certidão para pedido via formulário"),
	SOLICITACAO_CERTIDAO_REALIZADA("/pages/certidao/solicitacaoCertidaoRealizada.jsf", "Solicitação de certidão realizada"),
	CONSULTAR_CERTIDAO("/pages/certidao/consultarCertidao.jsf", "Consultar pedido de certidão"),
	VALIDAR_HASH_CERTIDAO("/pages/certidao/validarHashCertidao.jsf", "Validar hash certidão");

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
