package web.mail;

public class TemplateCadastroUsuario extends TemplateEmail {

	public String retornaMensagemFormatada(String nomeUsuario, String urlAplicacao) {
		StringBuilder mensagemFormatada = new StringBuilder();
		mensagemFormatada.append(retornaCabecario());
		mensagemFormatada.append(retornaMensagemCadastroUsuario(nomeUsuario, urlAplicacao));
		mensagemFormatada.append(retornaRodape());
		return mensagemFormatada.toString();
	}

	private String retornaMensagemCadastroUsuario(String nomeUsuario, String urlAplicacao) {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("Prezado(a) Sr(a) ");
		mensagem.append(nomeUsuario + ",");
		mensagem.append("<br/><br/>");
		mensagem.append("Seu cadastro foi realizado com sucesso no sistema do e-Civil.");
		mensagem.append("<br/><br/>");
		mensagem.append("<a target='_blank' href='" + urlAplicacao + "'>Link</a> ");
		return mensagem.toString();
	}
	
}
