package web.mail;

import web.util.MailUtil;

public class TesteEmail {

	public static void main(String[] args) {
		MailUtil mailUtil = new MailUtil();
		mailUtil.setServidorSmtp("smtp.gmail.com");
		mailUtil.setPortaServidorSmtp("465");
		mailUtil.setNomeRemetente("e-Civil");
		mailUtil.setDe("ecivil@recivil.com.br");
		mailUtil.setSenhaEmail("Rl9912tlj*");
		mailUtil.setPara("diogo.barbosa@r2da.com.br");
		mailUtil.setAssunto("e-Civil - Teste");
		
		
		/**
		 * Recueracao de senha 
		 */
//		TemplateEmailSenha templateEmailSenha = new TemplateEmailSenha();
//		mailUtil.setMensagem(templateEmailSenha.retornaMensagemRecuperacaoSenhaFormatada("João Silva Pereira", "1ASF2F", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
		/**
		 * Cadastro de usuario
		 */
//		TemplateCadastroUsuario templateCadastroUsuario = new TemplateCadastroUsuario();
//		mailUtil.setMensagem(templateCadastroUsuario.retornaMensagemFormatada("João Silva Pereira", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
		/**
		 * Novo pedido de certidão
		 */
		TemplatePedidoCertidao templatePedidoCertidao = new TemplatePedidoCertidao();
		mailUtil.setMensagem(templatePedidoCertidao.retornaMsgFormatadaNovoPedido("João da Silva Barroso", "CER200000065", "https://registrocivilminas.org.br/e-civil"));
		mailUtil.enviaMail();
		
		
		
		
		
		
		
		
		
//		TemplateEmailGuiaPamento templateEmailGuiaPamento = new TemplateEmailGuiaPamento();
		
//		Guia Emitida
//		mailUtil.setMensagem(templateEmailGuiaPamento.retornaMensagemGuiaEmitidaFormatada("2065995533", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
//		Guia Paga
//		mailUtil.setMensagem(templateEmailGuiaPamento.retornaMensagemGuiaPagaFormatada("2065995533", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
		/**
		 * Emails referente ao peticionamento/exame ordem
		 */
//		TemplateEmailSolicitacaoDigital templateEmailSolicitacaoDigital = new TemplateEmailSolicitacaoDigital();
		
//		Processo Protocolado
//		mailUtil.setMensagem(templateEmailSolicitacaoDigital.retornaMensagemSolicitacaoProtocoladaFormatada("DFP1800099897", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
//		Processo Pendente
//		mailUtil.setMensagem(templateEmailSolicitacaoDigital.retornaMensagemSolicitacaoPendenteFormatada("DFP1800099897", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
//		Processo Concluido
//		mailUtil.setMensagem(templateEmailSolicitacaoDigital.retornaMensagemSolicitacaoConcluidoFormatada("DFP1800099897", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		
//		Processo Aguardando Assinatura
//		mailUtil.setMensagem(templateEmailSolicitacaoDigital.retornaMensagemSolicitacaoAguardandoAssinaturaFormatada("DFP1800099897", "https://registrocivilminas.org.br/e-civil"));
//		mailUtil.enviaMail();
		/*********************************************************************************************/
		
	}
}
