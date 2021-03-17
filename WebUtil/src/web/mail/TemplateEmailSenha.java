package web.mail;

public class TemplateEmailSenha extends TemplateEmail {

	public String retornaMensagemFormatada(String nomeUsuario, String novaSenha, String urlAplicacao) {
		StringBuilder mensagemFormatada = new StringBuilder();
		mensagemFormatada.append(retornaCabecario());
		mensagemFormatada.append(retornaMensagemRecuperacaoSenha(nomeUsuario, novaSenha, urlAplicacao));
		mensagemFormatada.append(retornaRodape());
		return mensagemFormatada.toString();
	}

	private String retornaMensagemRecuperacaoSenha(String nomeUsuario, String novaSenha, String urlAplicacao) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) Sr(a) ");
		mensagem.append(nomeUsuario + ",");
		mensagem.append("<br/><br/>");
		mensagem.append("Utilize a senha abaixo para acessar a aplicação:");
		mensagem.append("<br/><br/>");
		mensagem.append("<b>");
		mensagem.append(novaSenha);
		mensagem.append("</b>");
		mensagem.append("<br/><br/>");
		mensagem.append("<a target='_blank' href='" + urlAplicacao + "'>Link</a> ");
		return mensagem.toString();
	}
	
}
