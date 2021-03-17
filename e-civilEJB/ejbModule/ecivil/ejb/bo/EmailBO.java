package ecivil.ejb.bo;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.enumeration.EnumSimNao;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import web.mail.TemplateCadastroUsuario;
import web.mail.TemplateEmailSenha;
import web.mail.TemplateEmissaoBoleto;
import web.mail.TemplatePedidoCertidao;
import web.util.AnexoEmail;
import web.util.MailUtil;

@Stateless
public class EmailBO {
	
	private static final String MIME_TYPE_PDF = "application/pdf";
	private static final String URL_DOMINIO_APLICACAO_EXTERNA = "https://registrocivilminas.org.br/e-civil";
	
	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	private MailUtil criaNovoMailUtil() {
		MailUtil mailUtil = new MailUtil();
		mailUtil.setServidorSmtp(parametroDAO.buscaValorParametro(Parametro.EMAIL_SERVIDOR_SMTP));
		mailUtil.setPortaServidorSmtp(parametroDAO.buscaValorParametro(Parametro.EMAIL_PORTA_SERVIDOR_SMTP));
		mailUtil.setNomeRemetente(parametroDAO.buscaValorParametro(Parametro.EMAIL_NOME_REMETENTE));
		mailUtil.setDe(parametroDAO.buscaValorParametro(Parametro.EMAIL_USUARIO));
		mailUtil.setSenhaEmail(parametroDAO.buscaValorParametro(Parametro.EMAIL_SENHA));
		return mailUtil;
	}
	
	private AnexoEmail montaObjetoAnexoPdfEmail(String filename, byte[] data) {
		AnexoEmail anexoEmail = new AnexoEmail();
		anexoEmail.setFilename(filename);
		anexoEmail.setMimeType(MIME_TYPE_PDF);
		anexoEmail.setData(data);
		return anexoEmail;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void enviaEmailRecuperacaoSenha(String para, String nomeUsuario, String novaSenha) {
		if (parametroDAO.buscaValorParametro(Parametro.ENVIAR_EMAIL).equalsIgnoreCase(EnumSimNao.N.name())) {
			return;
		}
		
		MailUtil mailUtil = criaNovoMailUtil();
		mailUtil.setPara(para);
		mailUtil.setAssunto("e-Civil - Recuperação de Senha");
		mailUtil.setMensagem(new TemplateEmailSenha().retornaMensagemFormatada(nomeUsuario, novaSenha, URL_DOMINIO_APLICACAO_EXTERNA));
		mailUtil.enviaMail();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void enviaEmailReseteSenha(String para, String nomeUsuario, String novaSenha) {
		if (parametroDAO.buscaValorParametro(Parametro.ENVIAR_EMAIL).equalsIgnoreCase(EnumSimNao.N.name())) {
			return;
		}
		
		MailUtil mailUtil = criaNovoMailUtil();
		mailUtil.setPara(para);
		mailUtil.setAssunto("e-Civil - Nova Senha");
		mailUtil.setMensagem(new TemplateEmailSenha().retornaMensagemFormatada(nomeUsuario, novaSenha, URL_DOMINIO_APLICACAO_EXTERNA));
		mailUtil.enviaMail();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void enviaEmailCadastroRealizado(String para, String nomeUsuario) {
		if (parametroDAO.buscaValorParametro(Parametro.ENVIAR_EMAIL).equalsIgnoreCase(EnumSimNao.N.name())) {
			return;
		}
		
		MailUtil mailUtil = criaNovoMailUtil();
		mailUtil.setPara(para);
		mailUtil.setAssunto("e-Civil - Cadastro realizado");
		mailUtil.setMensagem(new TemplateCadastroUsuario().retornaMensagemFormatada(nomeUsuario, URL_DOMINIO_APLICACAO_EXTERNA));
		mailUtil.enviaMail();		
	}

	public void enviaEmailNovoPedidoCertidao(String para, String nomeUsuario, String protocolo) {
		if (parametroDAO.buscaValorParametro(Parametro.ENVIAR_EMAIL).equalsIgnoreCase(EnumSimNao.N.name())) {
			return;
		}
		
		MailUtil mailUtil = criaNovoMailUtil();
		mailUtil.setPara(para);
		mailUtil.setAssunto("e-Civil - Pedido de certidão registrado");
		mailUtil.setMensagem(new TemplatePedidoCertidao().retornaMsgFormatadaNovoPedido(nomeUsuario, protocolo, URL_DOMINIO_APLICACAO_EXTERNA));
		mailUtil.enviaMail();				
	}

	public void enviaEmailAlteracaoSituacaoPedidoCertidao(PedidoCertidao pedidoCertidao) {
		if (parametroDAO.buscaValorParametro(Parametro.ENVIAR_EMAIL).equalsIgnoreCase(EnumSimNao.N.name())) {
			return;
		}
		
		recuperaEmailENomeDestinatario(pedidoCertidao);
		
		MailUtil mailUtil = criaNovoMailUtil();
		mailUtil.setPara(pedidoCertidao.getEmailDestinatario());
		mailUtil.setAssunto("e-Civil - Atualização de situação do pedido de certidão");
		mailUtil.setMensagem(recuperaMsgAlteracaoSituacaoPedidoCertidao(pedidoCertidao));
		if(pedidoCertidao.getAnexo() != null) {
			mailUtil.setListaAnexos(retornaListaAnexosEmailAlteracaoSituacaoPedidoCertidao(pedidoCertidao));
		}
		
		if (mailUtil.getMensagem() != null) {
			mailUtil.enviaMail();
		}
	}
	
	public void enviaEmailEmissaoBoleto(PedidoCertidao pedidoCertidao) {
		if (parametroDAO.buscaValorParametro(Parametro.ENVIAR_EMAIL).equalsIgnoreCase(EnumSimNao.N.name())) {
			return;
		}
		
		recuperaEmailENomeDestinatario(pedidoCertidao);
		
		MailUtil mailUtil = criaNovoMailUtil();
		mailUtil.setPara(pedidoCertidao.getEmailDestinatario());
		mailUtil.setAssunto("e-Civil - Atualização de situação do pedido de certidão");
		mailUtil.setMensagem(new TemplateEmissaoBoleto().retornaMensagemFormatada(pedidoCertidao.getNomeDestinatario(),  URL_DOMINIO_APLICACAO_EXTERNA));
		if(pedidoCertidao.getAnexo() != null) {
			mailUtil.setListaAnexos(retornaListaAnexosEmailAlteracaoSituacaoPedidoCertidao(pedidoCertidao));
		}
		
		if (mailUtil.getMensagem() != null) {
			mailUtil.enviaMail();
		}
	}
	
	public List<AnexoEmail> retornaListaAnexosEmailAlteracaoSituacaoPedidoCertidao(PedidoCertidao pedidoCertidao) {
		List<AnexoEmail> listaAnexosEmail = new ArrayList<>();
		listaAnexosEmail.add(montaObjetoAnexoPdfEmail("Boleto.pdf", pedidoCertidao.getAnexo()));
		return listaAnexosEmail;
	}

	private void recuperaEmailENomeDestinatario(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.getUsuarioExterno() != null) {
			pedidoCertidao.setEmailDestinatario(pedidoCertidao.getUsuarioExterno().getEmail());
			pedidoCertidao.setNomeDestinatario(pedidoCertidao.getUsuarioExterno().getNome());
		} else {
//			CartorioLookUp cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria)
//			pedidoCertidao.setEmailDestinatario(pedidoCertidao.getUsuarioExterno().getEmail());
//			pedidoCertidao.setNomeDestinatario(pedidoCertidao.getUsuarioExterno().getNome());
		}
	}

	private String recuperaMsgAlteracaoSituacaoPedidoCertidao(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.isSituacaoAguardandoPagamento()) {
			return new TemplatePedidoCertidao().retornaMsgFormatadaAguardandoPagamento(pedidoCertidao.getNomeDestinatario(), pedidoCertidao.getProtocolo(), URL_DOMINIO_APLICACAO_EXTERNA);
		}
		
		if (pedidoCertidao.isSituacaoPedidoFinalizado() && Util.ehStringValida(pedidoCertidao.getCodigoRastreio())) {
			return new TemplatePedidoCertidao().retornaMsgFormatadaPedidoFinalizadoCodRastreio(pedidoCertidao.getNomeDestinatario(), pedidoCertidao.getProtocolo(), pedidoCertidao.getCodigoRastreio(), URL_DOMINIO_APLICACAO_EXTERNA);
		}
		
		return null;
	}
	
}
