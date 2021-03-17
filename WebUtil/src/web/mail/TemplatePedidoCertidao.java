package web.mail;

public class TemplatePedidoCertidao extends TemplateEmail {

	public String retornaMsgFormatadaNovoPedido(String nomeUsuario, String protocolo, String urlAplicacao) {
		StringBuilder mensagemFormatada = new StringBuilder();
		mensagemFormatada.append(retornaCabecario());
		mensagemFormatada.append(retornaMensagemCadastroUsuario(nomeUsuario, protocolo, urlAplicacao));
		mensagemFormatada.append(retornaRodape());
		return mensagemFormatada.toString();
	}

	private String retornaMensagemCadastroUsuario(String nomeUsuario, String protocolo, String urlAplicacao) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) Sr(a) ");
		mensagem.append(nomeUsuario + ",");
		mensagem.append("<br/><br/>");
		mensagem.append("Seu pedido de certidão de protocolo ");
		mensagem.append("<b>");
		mensagem.append(protocolo);
		mensagem.append("</b>");
		mensagem.append(" foi registrado com sucesso no sistema do e-Civil.");
		mensagem.append("<br/><br/>");
		mensagem.append("Acesse o link abaixo e utilize o protocolo acima para acompanhar o andamento da sua solicitação.");
		mensagem.append("<br/><br/>");
		mensagem.append("<a target='_blank' href='" + urlAplicacao + "'>Link</a> ");
		return mensagem.toString();
	}
	
	public String retornaMsgFormatadaAguardandoPagamento(String nomeUsuario, String protocolo, String urlAplicacao) {
		StringBuilder mensagemFormatada = new StringBuilder();
		mensagemFormatada.append(retornaCabecario());
		mensagemFormatada.append(retornaMsgAguardandoPagamento(nomeUsuario, protocolo, urlAplicacao));
		mensagemFormatada.append(retornaRodape());
		return mensagemFormatada.toString();
	}

	private String retornaMsgAguardandoPagamento(String nomeUsuario, String protocolo, String urlAplicacao) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) Sr(a) ");
		mensagem.append(nomeUsuario + ",");
		mensagem.append("<br/><br/>");
		mensagem.append("Seu pedido de certidão de protocolo ");
		mensagem.append("<b>");
		mensagem.append(protocolo);
		mensagem.append("</b>");
		mensagem.append(" foi atualizado. Nesse momento você já pode emitir o boleto para pagamento da certidão.");
		mensagem.append("<br/><br/>");
		mensagem.append("Acesse o link abaixo e utilize o protocolo acima para acompanhar o andamento da sua solicitação.");
		mensagem.append("<br/><br/>");
		mensagem.append("<a target='_blank' href='" + urlAplicacao + "'>Link</a> ");
		return mensagem.toString();
	}
	
	public String retornaMsgFormatadaPedidoFinalizadoCodRastreio(String nomeUsuario, String protocolo, String codigoRastreio, String urlAplicacao) {
		StringBuilder mensagemFormatada = new StringBuilder();
		mensagemFormatada.append(retornaCabecario());
		mensagemFormatada.append(retornaMsgPedidoFinalizadoCodRastreio(nomeUsuario, protocolo, codigoRastreio, urlAplicacao));
		mensagemFormatada.append(retornaRodape());
		return mensagemFormatada.toString();
	}

	private String retornaMsgPedidoFinalizadoCodRastreio(String nomeUsuario, String protocolo, String codigoRastreio, String urlAplicacao) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) Sr(a) ");
		mensagem.append(nomeUsuario + ",");
		mensagem.append("<br/><br/>");
		mensagem.append("Seu pedido de certidão de protocolo ");
		mensagem.append("<b>");
		mensagem.append(protocolo);
		mensagem.append("</b>");
		mensagem.append(" foi finalizado. Utilize o código de rastreio a seguir para acompanhar a entrega da sua certidão: " + codigoRastreio);
		mensagem.append("<br/><br/>");
		mensagem.append("Acesse o link abaixo e utilize o protocolo acima para acompanhar o andamento da sua solicitação.");
		mensagem.append("<br/><br/>");
		mensagem.append("<a target='_blank' href='" + urlAplicacao + "'>Link</a> ");
		return mensagem.toString();
	}

	
}
