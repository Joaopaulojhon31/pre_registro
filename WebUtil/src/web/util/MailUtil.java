package web.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class MailUtil implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(MailUtil.class.getName());
	
	private String de;
	private String para;
	private String nomeRemetente;
	private String assunto;
	private String mensagem;
	private String senhaEmail;
	private String servidorSmtp;
	private String portaServidorSmtp;
	private List<AnexoEmail> listaAnexos;

	public void enviaMail() {
		try {
			Thread t = new Thread(this);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			if (getPara() == null || getPara().trim().equals("")) {
				return;
			}
			
			Properties props = configuraProperties();
			Session session = Session.getDefaultInstance(props);
			InternetAddress destinatario = new InternetAddress(getPara());
			InternetAddress remetente = new InternetAddress(getDe(), getNomeRemetente());

			System.setProperty("mail.mime.charset", "ISO-8859-1");
			Message msg = new MimeMessage(session);
			msg.setHeader("X-Priority", "1");
			msg.setHeader("X-MSMail-Priority", "High");
			msg.setHeader("Priority", "Urgent");
			msg.setHeader("Importance", "High");
			msg.setSentDate(new Date());
			msg.setFrom(remetente);
			msg.setRecipient(Message.RecipientType.TO, destinatario);
			msg.setSubject(getAssunto());

			MimeMultipart multipart = new MimeMultipart();

			MimeBodyPart textBody = new MimeBodyPart();
			textBody.setContent((getMensagem()), "text/html; charset=ISO-8859-1");
			multipart.addBodyPart(textBody);

			if (getListaAnexos() != null) {
				for (AnexoEmail anexo : getListaAnexos()) {
					BodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new ByteArrayDataSource(anexo.getData(), anexo.getMimeType());
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					attachmentBodyPart.setFileName(anexo.getFilename());
					multipart.addBodyPart(attachmentBodyPart);
				}
			}

			msg.setContent(multipart);
			
			// Intervalo de 1 seg para enviar cada e-mail. Caso contrario o servidor comecar a bloquear o envio.			
			Thread.sleep(1000);
			envia(session, msg);
			
		} catch (Exception ex) {
			LOGGER.info("##### Não foi possivel enviar email para: " + getPara());
			ex.printStackTrace();
		}
	}

	private Properties configuraProperties() {
		Properties props = System.getProperties();

		if (getServidorSmtp().equals("smtp.gmail.com")) {
			props.put("mail.smtp.host", getServidorSmtp());
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", getPortaServidorSmtp());
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.socketFactory.port", getPortaServidorSmtp());
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		} else {
			props.put("mail.smtp.user", getDe());
			props.put("mail.smtp.password", getSenhaEmail());
			props.put("mail.smtp.host", getServidorSmtp());
			props.put("mail.smtp.port", getPortaServidorSmtp());
			props.put("mail.smtp.auth", "true");
		}

		return props;
	}

	private void envia(Session session, Message msg) throws MessagingException, NoSuchProviderException {
		try {
			Transport transport = session.getTransport("smtp");
			transport.connect(getServidorSmtp(), getDe(), getSenhaEmail());
			msg.saveChanges();
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
	}

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	public String getNomeRemetente() {
		return nomeRemetente;
	}

	public void setNomeRemetente(String nomeRemetente) {
		this.nomeRemetente = nomeRemetente;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getSenhaEmail() {
		return senhaEmail;
	}

	public void setSenhaEmail(String senhaEmail) {
		this.senhaEmail = senhaEmail;
	}

	public String getServidorSmtp() {
		return servidorSmtp;
	}

	public void setServidorSmtp(String servidorSmtp) {
		this.servidorSmtp = servidorSmtp;
	}

	public String getPortaServidorSmtp() {
		return portaServidorSmtp;
	}

	public void setPortaServidorSmtp(String portaServidorSmtp) {
		this.portaServidorSmtp = portaServidorSmtp;
	}

	public List<AnexoEmail> getListaAnexos() {
		return listaAnexos;
	}

	public void setListaAnexos(List<AnexoEmail> listaAnexos) {
		this.listaAnexos = listaAnexos;
	}

}
