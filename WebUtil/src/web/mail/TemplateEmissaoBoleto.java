package web.mail;

public class TemplateEmissaoBoleto extends TemplateEmail{
	public String retornaMensagemFormatada(String nomeUsuario, String urlAplicacao) {
		StringBuilder mensagemFormatada = new StringBuilder();
		mensagemFormatada.append(retornaCabecario());
		mensagemFormatada.append(retornaMensagemEmissaoBoleto(nomeUsuario, urlAplicacao));
		mensagemFormatada.append(retornaRodape());
		return mensagemFormatada.toString();
	}

	private String retornaMensagemEmissaoBoleto(String nomeUsuario, String urlAplicacao) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) Sr(a) ");
		mensagem.append(nomeUsuario + ",");
		mensagem.append("<br/><br/>");
		mensagem.append("Seu boleto foi gerado com sucesso no sistema do e-Civil e está anexo à essa mensagem.");
		mensagem.append("<br/><br/>");
		mensagem.append("<a target='_blank' href='" + urlAplicacao + "'>Link</a> ");
		return mensagem.toString();
	}
}
