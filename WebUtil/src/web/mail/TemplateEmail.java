package web.mail;

public class TemplateEmail {

	private static final String MENSAGEM_AUTOMATICA = "Esta \u00E9 uma mensagem autom\u00E1tica do sistema, favor n\u00E3o responder.";

	protected String retornaCabecario() {
		StringBuilder cabecario = new StringBuilder();
		cabecario.append("<table border='0' align='center' cellpadding='0' cellspacing='0' style='width:546px' >");
		cabecario.append("<tr style='font-family: Arial; font-size:10px; text-align:center;'>");
		cabecario.append("<td style=' padding-bottom:5px'>%s</td></tr>");
		cabecario.append("<tr><td><img src='https://recivil.com.br/email/topo.png'");
		cabecario.append("></td> </tr><tr><td style='padding: 50px 50px 90px 50px; font-family: Segoe UI, sans-serif; font-size: 15px; border: 1px solid #eee; border-top: 0px; border-bottom: 0px;' >");
		return String.format(cabecario.toString(), MENSAGEM_AUTOMATICA);
	}

	protected String retornaRodape() {
		StringBuilder rodape = new StringBuilder();
		rodape.append("</td></tr><tr><td><img src='https://recivil.com.br/email/rodape.png'");
		rodape.append("></td></tr><tr style='font-family: Arial; font-size:10px; ");
		rodape.append("text-align:center;'><td style=' padding-bottom:5px'>");
		rodape.append("%s</td></tr>");
		rodape.append("</table>");
		return String.format(rodape.toString(), MENSAGEM_AUTOMATICA);
	}
	
}