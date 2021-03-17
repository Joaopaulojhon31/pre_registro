package solicitacaoui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.PdfWriter;

import ecivil.adm.controller.BaseController;
import ecivil.adm.controller.PrincipalController;
import ecivil.adm.util.Mensagem;
import ecivil.adm.util.UploadUtils;
import ecivil.ejb.bo.HistoricoPreRegistroBO;
import ecivil.ejb.bo.UIBO;
import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;
import ecivil.ejb.util.Util;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class DeclaracoesPreRegistroPrePostoController extends BaseController implements Serializable {

	@EJB
	private PreRegistroDAO preRegistroDAO;

	@EJB
	private UIBO uiBO;

	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;

	private static PreRegistro preRegistroDeclaracoesPrePosto;
	private byte[] documentacaoAnexaBytes;
	private boolean documentacaoanexada;
	private PrincipalController principalController;

	Font fonteGeral = new Font(Font.FontFamily.HELVETICA, 9);
	Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);

	@PostConstruct
	public void inicializa() throws Exception {
		setPrincipalController(new PrincipalController());
	}

	public PreRegistroDAO getPreRegistroDAO() {
		return preRegistroDAO;
	}

	public void setPreRegistroDAO(PreRegistroDAO preRegistroDAO) {
		this.preRegistroDAO = preRegistroDAO;
	}

	public Font getFonteGeral() {
		return fonteGeral;
	}

	public void setFonteGeral(Font fonteGeral) {
		this.fonteGeral = fonteGeral;
	}

	public Font getFonteTitulo() {
		return fonteTitulo;
	}

	public void setFonteTitulo(Font fonteTitulo) {
		this.fonteTitulo = fonteTitulo;
	}

	public PreRegistro getPreRegistroDeclaracoesPrePosto() {
		return preRegistroDeclaracoesPrePosto;
	}

	public static void setPreRegistroDeclaracoesPrePosto(PreRegistro preRegistroDeclaracoesPrePosto) {
		DeclaracoesPreRegistroPrePostoController.preRegistroDeclaracoesPrePosto = preRegistroDeclaracoesPrePosto;
	}

	public byte[] getDocumentacaoAnexaBytes() {
		return documentacaoAnexaBytes;
	}

	public void setDocumentacaoAnexaBytes(byte[] documentacaoAnexaBytes) {
		this.documentacaoAnexaBytes = documentacaoAnexaBytes;
	}

	public boolean isDocumentacaoanexada() {
		return documentacaoanexada;
	}

	public void setDocumentacaoanexada(boolean documentacaoanexada) {
		this.documentacaoanexada = documentacaoanexada;
	}

	public PrincipalController getPrincipalController() {
		return principalController;
	}

	public void setPrincipalController(PrincipalController principalController) {
		this.principalController = principalController;
	}
	
	public String ConverteQuantidadeCriancasTexto() {
		if(preRegistroDeclaracoesPrePosto.getQuantidadeCriancas().equals("1")) {
			return "N�o";
		}else if(preRegistroDeclaracoesPrePosto.getQuantidadeCriancas().equals("2")) {
			return "G�meos";
		}else if(preRegistroDeclaracoesPrePosto.getQuantidadeCriancas().equals("3")) {
			return "Trig�meos";
		}else if(preRegistroDeclaracoesPrePosto.getQuantidadeCriancas().equals("4")) {
			return "Quadrig�meos";
		}else 
			return "Qu�ntuplos";
	}
	public void gerarPDFDeclaracaoIndicacaoPaternidade() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String arquivoPDF = "Declara��o de Indica��o de Paternidade.pdf";
		String criancaGemeos = ConverteQuantidadeCriancasTexto();
		com.itextpdf.text.List nomeDnvCrianca = new com.itextpdf.text.List(false);
		nomeDnvCrianca.setListSymbol("");
		Document pdf = new Document();
		try {

			PdfWriter.getInstance(pdf, byteArrayOutputStream);
			pdf.open();
			pdf.add(montaDeclaracaoCabecalhoIndicacaoPaternidade());
			pdf.add(montaDeclaracaoDadosMaeIndicacaoPaternidade());
			pdf.add(montaDeclaracaoDadosCriancaIndicacaoPaternidade(preRegistroDeclaracoesPrePosto.getNomeCrianca1()));
			pdf.add(montaDeclaracaoDadosSupostoPaiIndicacaoPaternidade());
			pdf.add(montaDeclaracaoDadosTextoIndicacaoPaternidade());
			pdf.add(montaDeclaracaoAssinaturaIndicacaoPaternidade());
			pdf.add(montaDeclaracaoObservacaoPaternidade());

			if (criancaGemeos.equals("G�meos") || criancaGemeos.equals("Trig�meos")
					|| criancaGemeos.equals("Quadrig�meos") || criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosMaeIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosCriancaIndicacaoPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca2()));
				pdf.add(montaDeclaracaoDadosSupostoPaiIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosTextoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoAssinaturaIndicacaoPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}

			if (criancaGemeos.equals("Trig�meos") || criancaGemeos.equals("Quadrig�meos")
					|| criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosMaeIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosCriancaIndicacaoPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca3()));
				pdf.add(montaDeclaracaoDadosSupostoPaiIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosTextoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoAssinaturaIndicacaoPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}

			if (criancaGemeos.equals("Quadrig�meos") || criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosMaeIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosCriancaIndicacaoPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca4()));
				pdf.add(montaDeclaracaoDadosSupostoPaiIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosTextoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoAssinaturaIndicacaoPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}

			if (criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosMaeIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosCriancaIndicacaoPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca5()));
				pdf.add(montaDeclaracaoDadosSupostoPaiIndicacaoPaternidade());
				pdf.add(montaDeclaracaoDadosTextoIndicacaoPaternidade());
				pdf.add(montaDeclaracaoAssinaturaIndicacaoPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}
		} catch (Exception e) {
			Mensagem.errorSemBundle("N�o foi poss�vel gerar o PDF no momento.");
		} finally {
			pdf.close();
			byte[] pdfArray = byteArrayOutputStream.toByteArray();
			super.exibirPDF(pdfArray, arquivoPDF);

		}
	}

	public com.itextpdf.text.List montaDeclaracaoCabecalhoIndicacaoPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List cabecalho = new com.itextpdf.text.List(false);
		cabecalho.setListSymbol("");
		conteudoLista = new ListItem("DECLARA��O DE INDICA��O DE PATERNIDADE", fonteTitulo);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		conteudoLista = new ListItem("(apenas quando for o caso)", fonteTitulo);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		return cabecalho;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosCriancaIndicacaoPaternidade(String nomeCrianca) {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List dadosMae = new com.itextpdf.text.List(false);
		dadosMae.setListSymbol("");
		conteudoLista = new ListItem("Nome completo do menor: " + nomeCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(30f);
		dadosMae.add(conteudoLista);

		conteudoLista = new ListItem(
				"Matr�cula da certid�o de nascimento: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);
		return dadosMae;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosMaeIndicacaoPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List dadosMae = new com.itextpdf.text.List(false);
		String linhaDadosMae;
		dadosMae.setListSymbol("");
		conteudoLista = new ListItem("Nome completo da m�e: " + preRegistroDeclaracoesPrePosto.getNomeMae(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(22f);
		dadosMae.add(conteudoLista);

		conteudoLista = new ListItem("CPF da m�e: " + preRegistroDeclaracoesPrePosto.getCpfMae(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);

		if (Util.isNullOrEmpty(preRegistroDeclaracoesPrePosto.getNacionalidadeMae()))
			conteudoLista = new ListItem("Nacionalidade: N�o consta", fonteGeral);
		else
			conteudoLista = new ListItem("Nacionalidade: " + preRegistroDeclaracoesPrePosto.getNacionalidadeMae(),
					fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);

		conteudoLista = new ListItem("Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidadeMae() + " - "
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUfMae(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);

		conteudoLista = new ListItem("Estado Civil: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(15f);
		dadosMae.add(conteudoLista);

		conteudoLista = new ListItem("Data de nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoMae()),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);

		conteudoLista = new ListItem("Profiss�o: ", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);

		linhaDadosMae = preRegistroDeclaracoesPrePosto.getResidenciaUfMae() + " - "
				+ preRegistroDeclaracoesPrePosto.getResidenciaMunicipioMae();
		linhaDadosMae += " - " + preRegistroDeclaracoesPrePosto.getResidenciaBairroMae() + " ,"
				+ preRegistroDeclaracoesPrePosto.getResidenciaLogradouroMae();
		linhaDadosMae += ", " + preRegistroDeclaracoesPrePosto.getResidenciaNumeroMae() + ", CEP: "
				+ preRegistroDeclaracoesPrePosto.getResidenciaCEPMae();
		conteudoLista = new ListItem(linhaDadosMae, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);

		linhaDadosMae = "Telefones: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ";
		linhaDadosMae += "E-mail: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _";
		conteudoLista = new ListItem(linhaDadosMae, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosMae.add(conteudoLista);
		return dadosMae;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosSupostoPaiIndicacaoPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List dadosPai = new com.itextpdf.text.List(false);
		dadosPai.setListSymbol("");
		conteudoLista = new ListItem("Dados do SUPOSTO pai:", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(30f);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem("a) de preenchimento obrigat�rio:", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"Nome: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(15f);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"Endere�o: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(" ", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(30f);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"b) de preenchimento t�o completo quanto poss�vel (mas observando-se que a falta dos dados abaixo n�o obstar� o andamento do pedido):",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"Profiss�o:  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(15f);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"Endere�o do local de trabalho:  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"RG: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _                             CPF: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"Telefones: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ E-mail: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		conteudoLista = new ListItem(
				"Outras informa��es: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		dadosPai.add(conteudoLista);

		return dadosPai;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosTextoIndicacaoPaternidade() {
		String linhaInformacoes;
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List textoDeclaracao = new com.itextpdf.text.List(false);
		textoDeclaracao.setListSymbol("");
		conteudoLista = new ListItem(
				"Declaro, sob as penas da lei, que o reconhecimento da paternidade n�o foi pleiteado em ju�zo.",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(32f);
		textoDeclaracao.add(conteudoLista);

		conteudoLista = new ListItem(" ", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		textoDeclaracao.add(conteudoLista);

		conteudoLista = new ListItem(
				"Estou ciente de que, sendo informado o nome e prenome do suposto pai, bem como os dados dispon�veis, o Oficial remeter� tais "
						+ "informa��es ao juiz, acompanhadas de certid�o da crian�a ora registrada, a fim de ser averiguada oficiosamente a proced�ncia da "
						+ "alega��o, nos termos da Lei Federal n� 8.560, de 29 de novembro de 1992. Estou ciente, ainda, de que posso, a qualquer tempo, propor "
						+ "a��o de investiga��o de paternidade, em nome da crian�a, para inclus�o do nome do pai no registro de nascimento, sendo que essa "
						+ "a��o tamb�m poder� ser intentada pelo(a) pr�prio(a) registrando(a), quando tiver capacidade para tanto.",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_JUSTIFIED);
		textoDeclaracao.add(conteudoLista);

		linhaInformacoes = "Local: "
				+ uiBO.recuperaMunicipioUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()) + " - ";
		linhaInformacoes += uiBO.recuperaUfUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()) + ", ";
		linhaInformacoes += "Data: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(getPreRegistroDAO().retornaDataBanco());
		conteudoLista = new ListItem(linhaInformacoes, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(25f);
		textoDeclaracao.add(conteudoLista);

		return textoDeclaracao;
	}

	public com.itextpdf.text.List montaDeclaracaoAssinaturaIndicacaoPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List textoDeclaracao = new com.itextpdf.text.List(false);
		textoDeclaracao.setListSymbol("");
		conteudoLista = new ListItem(
				"__________________________________________________________________________________", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(40f);
		textoDeclaracao.add(conteudoLista);

		conteudoLista = new ListItem("(Assinatura da pessoa que indicou os dados do suposto pai)", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		textoDeclaracao.add(conteudoLista);

		conteudoLista = new ListItem(
				"__________________________________________________________________________________", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(40f);
		textoDeclaracao.add(conteudoLista);

		conteudoLista = new ListItem("(Assinatura do Oficial ou preposto que recebeu a indica��o)", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		textoDeclaracao.add(conteudoLista);

		return textoDeclaracao;
	}

	public void gerarPDFDeclaracaoRecusaPaternidade() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String arquivoPDF = "Declara��o de Recusa de Indica��o de Paternidade.pdf";
		String criancaGemeos = ConverteQuantidadeCriancasTexto();
		com.itextpdf.text.List nomeDnvCrianca = new com.itextpdf.text.List(false);
		nomeDnvCrianca.setListSymbol("");
		Document pdf = new Document();
		try {

			PdfWriter.getInstance(pdf, byteArrayOutputStream);
			pdf.open();
			pdf.add(montaDeclaracaoCabecalhoRecusaPaternidade());
			pdf.add(montaDeclaracaoTextoPrincipalRecusaPaternidade(preRegistroDeclaracoesPrePosto.getNomeCrianca1()));
			pdf.add(montaDeclaracaoAssinaturasRecusaPaternidade());
			pdf.add(montaDeclaracaoObservacaoPaternidade());

			if (criancaGemeos.equals("G�meos") || criancaGemeos.equals("Trig�meos")
					|| criancaGemeos.equals("Quadrig�meos") || criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoRecusaPaternidade());
				pdf.add(montaDeclaracaoTextoPrincipalRecusaPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca2()));
				pdf.add(montaDeclaracaoAssinaturasRecusaPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}

			if (criancaGemeos.equals("Trig�meos") || criancaGemeos.equals("Quadrig�meos")
					|| criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoRecusaPaternidade());
				pdf.add(montaDeclaracaoTextoPrincipalRecusaPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca3()));
				pdf.add(montaDeclaracaoAssinaturasRecusaPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}

			if (criancaGemeos.equals("Quadrig�meos") || criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoRecusaPaternidade());
				pdf.add(montaDeclaracaoTextoPrincipalRecusaPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca4()));
				pdf.add(montaDeclaracaoAssinaturasRecusaPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}

			if (criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoRecusaPaternidade());
				pdf.add(montaDeclaracaoTextoPrincipalRecusaPaternidade(
						preRegistroDeclaracoesPrePosto.getNomeCrianca5()));
				pdf.add(montaDeclaracaoAssinaturasRecusaPaternidade());
				pdf.add(montaDeclaracaoObservacaoPaternidade());
			}
		} catch (Exception e) {
			Mensagem.errorSemBundle("N�o foi poss�vel gerar o PDF no momento.");
		} finally {
			pdf.close();
			byte[] pdfArray = byteArrayOutputStream.toByteArray();
			super.exibirPDF(pdfArray, arquivoPDF);

		}
	}

	public com.itextpdf.text.List montaDeclaracaoCabecalhoRecusaPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List cabecalho = new com.itextpdf.text.List(false);
		cabecalho.setListSymbol("");
		conteudoLista = new ListItem("DECLARA��O DE RECUSA DE INDICA��O DE PATERNIDADE", fonteTitulo);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		conteudoLista = new ListItem("(apenas quando for o caso)", fonteTitulo);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		return cabecalho;
	}

	public com.itextpdf.text.List montaDeclaracaoTextoPrincipalRecusaPaternidade(String nomeCrianca) {
		ListItem conteudoLista = new ListItem();
		String textoPrincipal;
		com.itextpdf.text.List listaTextoPrincipal = new com.itextpdf.text.List(false);
		listaTextoPrincipal.setListSymbol("");
		textoPrincipal = "Eu, " + preRegistroDeclaracoesPrePosto.getNomeMae()
				+ " (nome completo da m�e), portadora do ";
		if (!Util.isNullOrEmpty(preRegistroDeclaracoesPrePosto.getTipoDocumentoMae()))
			textoPrincipal += preRegistroDeclaracoesPrePosto.getTipoDocumentoMae() + " n� "
					+ preRegistroDeclaracoesPrePosto.getDocumentoMae() + " e do ";
		textoPrincipal += "CPF n� " + preRegistroDeclaracoesPrePosto.getCpfMae();
		textoPrincipal += ", profiss�o " + preRegistroDeclaracoesPrePosto.getProfissaoMae() + " , residente em "
				+ preRegistroDeclaracoesPrePosto.getResidenciaUfMae();
		textoPrincipal += " - " + preRegistroDeclaracoesPrePosto.getResidenciaMunicipioMae() + " - "
				+ preRegistroDeclaracoesPrePosto.getResidenciaBairroMae();
		textoPrincipal += ", " + preRegistroDeclaracoesPrePosto.getResidenciaLogradouroMae() + ", "
				+ preRegistroDeclaracoesPrePosto.getResidenciaNumeroMae();
		textoPrincipal += " telefone _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ , e-mail _ _ _ _ _ _ _ _ _ _ _ _ _ _";
		textoPrincipal += "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ , DECLARO, na qualidade de m�e, para os fins e efeitos previstos na Lei n� 8.560, de";
		textoPrincipal += " 29 de novembro de 1992, que N�O � DO MEU INTERESSE INFORMAR O NOME DO PAI de ";
		textoPrincipal += nomeCrianca + " (nome completo do menor), cuja matr�cula do ";
		textoPrincipal += "registro de nascimento � _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ . Estou ciente, ainda, de que posso, a";
		textoPrincipal += " qualquer tempo, propor a��o de investiga��o de paternidade, em nome da crian�a, para inclus�o do nome do pai no registro de";
		textoPrincipal += " nascimento, sendo que essa a��o tamb�m poder� ser intentada pelo(a) pr�prio(a) registrando(a), quando tiver capacidade para tanto.";
		conteudoLista = new ListItem(textoPrincipal, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		listaTextoPrincipal.add(conteudoLista);

		return listaTextoPrincipal;
	}

	public com.itextpdf.text.List montaDeclaracaoObservacaoPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesObservacoes = new com.itextpdf.text.List(false);
		informacoesObservacoes.setListSymbol("");

		conteudoLista = new ListItem("  ", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(35f);
		informacoesObservacoes.add(conteudoLista);

		String linhaInformacoesRodape = "OBS.: O Oficial dever� anexar certid�o de nascimento, original (Provimento n� 16, art. 3�, � 3�, do CNJ) ou por c�pia conferida (art. 3�, � 2�)";
		conteudoLista = new ListItem(linhaInformacoesRodape, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		informacoesObservacoes.add(conteudoLista);
		return informacoesObservacoes;
	}

	public com.itextpdf.text.List montaDeclaracaoAssinaturasRecusaPaternidade() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesAssinaturas = new com.itextpdf.text.List(false);
		informacoesAssinaturas.setListSymbol("");
		String linhaInformacoesRodape = "Local: "
				+ uiBO.recuperaMunicipioUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()) + " - ";
		linhaInformacoesRodape += uiBO.recuperaUfUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()) + ", ";
		linhaInformacoesRodape += "Data: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(getPreRegistroDAO().retornaDataBanco());
		conteudoLista = new ListItem(linhaInformacoesRodape, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(20f);
		informacoesAssinaturas.add(conteudoLista);

		conteudoLista = new ListItem(
				"___________________________________________________________________________________", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(40f);
		informacoesAssinaturas.add(conteudoLista);

		conteudoLista = new ListItem("(Assinatura da pessoa que fez a recusa)", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		informacoesAssinaturas.add(conteudoLista);

		conteudoLista = new ListItem(
				"___________________________________________________________________________________", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(40f);
		informacoesAssinaturas.add(conteudoLista);

		conteudoLista = new ListItem("(Assinatura do Oficial ou preposto que recebeu a recusa)", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		informacoesAssinaturas.add(conteudoLista);

		return informacoesAssinaturas;
	}

	public void gerarPDFDeclaracaoRegistroNascimento() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String criancaGemeos = ConverteQuantidadeCriancasTexto();
		String arquivoPDF = "Declara��o de Registro de Nascimento.pdf";
		com.itextpdf.text.List nomeDnvCrianca = new com.itextpdf.text.List(false);
		nomeDnvCrianca.setListSymbol("");
		Document pdf = new Document();
		try {

			PdfWriter.getInstance(pdf, byteArrayOutputStream);
			pdf.open();
			pdf.add(montaDeclaracaoCabecalhoDeclaracaoRegistroNascimento());
			pdf.add(montaDeclaracaoDadosCrianca1());
			pdf.add(montaDeclaracaoDadosMae());
			pdf.add(montaDeclaracaoDadosPai());
			pdf.add(montaDeclaracaoResidenciaGenitores());
			pdf.add(montaDeclaracaoDeclarante());
			pdf.add(montaDeclaracaoRodape());
			if (criancaGemeos.equals("G�meos") || criancaGemeos.equals("Trig�meos")
					|| criancaGemeos.equals("Quadrig�meos") || criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoDeclaracaoRegistroNascimento());
				pdf.add(montaDeclaracaoDadosCrianca2());
				pdf.add(montaDeclaracaoDadosMae());
				pdf.add(montaDeclaracaoDadosPai());
				pdf.add(montaDeclaracaoResidenciaGenitores());
				pdf.add(montaDeclaracaoDeclarante());
				pdf.add(montaDeclaracaoRodape());
			}
			if (criancaGemeos.equals("Trig�meos") || criancaGemeos.equals("Quadrig�meos")
					|| criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoDeclaracaoRegistroNascimento());
				pdf.add(montaDeclaracaoDadosCrianca3());
				pdf.add(montaDeclaracaoDadosMae());
				pdf.add(montaDeclaracaoDadosPai());
				pdf.add(montaDeclaracaoResidenciaGenitores());
				pdf.add(montaDeclaracaoDeclarante());
				pdf.add(montaDeclaracaoRodape());
			}
			if (criancaGemeos.equals("Quadrig�meos") || criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoDeclaracaoRegistroNascimento());
				pdf.add(montaDeclaracaoDadosCrianca4());
				pdf.add(montaDeclaracaoDadosMae());
				pdf.add(montaDeclaracaoDadosPai());
				pdf.add(montaDeclaracaoResidenciaGenitores());
				pdf.add(montaDeclaracaoDeclarante());
				pdf.add(montaDeclaracaoRodape());
			}
			if (criancaGemeos.equals("Qu�ntuplos")) {
				pdf.newPage();
				pdf.add(montaDeclaracaoCabecalhoDeclaracaoRegistroNascimento());
				pdf.add(montaDeclaracaoDadosCrianca5());
				pdf.add(montaDeclaracaoDadosMae());
				pdf.add(montaDeclaracaoDadosPai());
				pdf.add(montaDeclaracaoResidenciaGenitores());
				pdf.add(montaDeclaracaoDeclarante());
				pdf.add(montaDeclaracaoRodape());
			}
		} catch (Exception e) {
			Mensagem.errorSemBundle("N�o foi poss�vel gerar o PDF no momento.");
		} finally {
			pdf.close();
			byte[] pdfArray = byteArrayOutputStream.toByteArray();
			super.exibirPDF(pdfArray, arquivoPDF);

		}
	}

	public com.itextpdf.text.List montaDeclaracaoCabecalhoDeclaracaoRegistroNascimento() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List cabecalho = new com.itextpdf.text.List(false);
		cabecalho.setListSymbol("");
		conteudoLista = new ListItem(
				"Registrado no Livro A-_________, fls._________, n�____________________ em ____/____/________",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		conteudoLista = new ListItem("(preenchimento ap�s a efetiva��o no registro)", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		conteudoLista = new ListItem("DECLARA��O DE REGISTRO DE NASCIMENTO", fonteTitulo);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		cabecalho.add(conteudoLista);
		conteudoLista = new ListItem("PROVIMENTO N� 13/2010 DA CORREGEDORIA NACIONAL DE JUSTI�A", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		cabecalho.add(conteudoLista);
		return cabecalho;
	}

	public com.itextpdf.text.List montaDeclaracaoNomeDnvCrianca() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List nomeDnvCrianca = new com.itextpdf.text.List(false);
		nomeDnvCrianca.setListSymbol("");
		conteudoLista = new ListItem("NOME DO(A) REGISTRADO(A):" + preRegistroDeclaracoesPrePosto.getNomeCrianca1(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		nomeDnvCrianca.add(conteudoLista);

		conteudoLista = new ListItem("DNV N�: " + preRegistroDeclaracoesPrePosto.getDnvCrianca1(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);

		nomeDnvCrianca.add(conteudoLista);
		return nomeDnvCrianca;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosCrianca1() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesCrianca = new com.itextpdf.text.List(false);
		informacoesCrianca.setListSymbol("");
		conteudoLista = new ListItem("NOME DO(A) REGISTRADO(A):" + preRegistroDeclaracoesPrePosto.getNomeCrianca1(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		informacoesCrianca.add(conteudoLista);
		String linhaInformacoesCrianca = "Solicita��o: " + preRegistroDeclaracoesPrePosto.getId() + " - ";
		linhaInformacoesCrianca += "Data de Nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoCrianca1())
				+ " - ";
		linhaInformacoesCrianca += "Hor�rio de Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getHoraNascimentoCrianca1();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidade() + "/"
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUf() + " - Lugar do Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getNomeUi();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Sexo da crian�a: " + preRegistroDeclaracoesPrePosto.getSexoCrianca1()
				+ " - O fato de ser G�meo: " + ConverteQuantidadeCriancasTexto();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		return informacoesCrianca;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosCrianca2() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesCrianca = new com.itextpdf.text.List(false);
		informacoesCrianca.setListSymbol("");
		conteudoLista = new ListItem("NOME DO(A) REGISTRADO(A):" + preRegistroDeclaracoesPrePosto.getNomeCrianca2(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		informacoesCrianca.add(conteudoLista);
		String linhaInformacoesCrianca = "Solicita��o: " + preRegistroDeclaracoesPrePosto.getId() + " - ";
		linhaInformacoesCrianca += "Data de Nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoCrianca2())
				+ " - ";
		linhaInformacoesCrianca += "Hor�rio de Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getHoraNascimentoCrianca2();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidade() + "/"
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUf() + " - Lugar do Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getNomeUi();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Sexo da crian�a: " + preRegistroDeclaracoesPrePosto.getSexoCrianca2()
				+ " - O fato de ser G�meo: " + ConverteQuantidadeCriancasTexto();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		return informacoesCrianca;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosCrianca3() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesCrianca = new com.itextpdf.text.List(false);
		informacoesCrianca.setListSymbol("");
		conteudoLista = new ListItem("NOME DO(A) REGISTRADO(A):" + preRegistroDeclaracoesPrePosto.getNomeCrianca3(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		informacoesCrianca.add(conteudoLista);
		String linhaInformacoesCrianca = "Solicita��o: " + preRegistroDeclaracoesPrePosto.getId() + " - ";
		linhaInformacoesCrianca += "Data de Nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoCrianca3())
				+ " - ";
		linhaInformacoesCrianca += "Hor�rio de Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getHoraNascimentoCrianca3();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidade() + "/"
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUf() + " - Lugar do Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getNomeUi();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Sexo da crian�a: " + preRegistroDeclaracoesPrePosto.getSexoCrianca3()
				+ " - O fato de ser G�meo: " + ConverteQuantidadeCriancasTexto();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		return informacoesCrianca;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosCrianca4() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesCrianca = new com.itextpdf.text.List(false);
		informacoesCrianca.setListSymbol("");
		conteudoLista = new ListItem("NOME DO(A) REGISTRADO(A):" + preRegistroDeclaracoesPrePosto.getNomeCrianca4(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		informacoesCrianca.add(conteudoLista);
		String linhaInformacoesCrianca = "Solicita��o: " + preRegistroDeclaracoesPrePosto.getId() + " - ";
		linhaInformacoesCrianca += "Data de Nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoCrianca4())
				+ " - ";
		linhaInformacoesCrianca += "Hor�rio de Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getHoraNascimentoCrianca4();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidade() + "/"
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUf() + " - Lugar do Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getNomeUi();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Sexo da crian�a: " + preRegistroDeclaracoesPrePosto.getSexoCrianca4()
				+ " - O fato de ser G�meo: " + ConverteQuantidadeCriancasTexto();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		return informacoesCrianca;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosCrianca5() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesCrianca = new com.itextpdf.text.List(false);
		informacoesCrianca.setListSymbol("");
		conteudoLista = new ListItem("NOME DO(A) REGISTRADO(A):" + preRegistroDeclaracoesPrePosto.getNomeCrianca5(),
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(22f);
		informacoesCrianca.add(conteudoLista);
		String linhaInformacoesCrianca = "Solicita��o: " + preRegistroDeclaracoesPrePosto.getId() + " - ";
		linhaInformacoesCrianca += "Data de Nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoCrianca5())
				+ " - ";
		linhaInformacoesCrianca += "Hor�rio de Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getHoraNascimentoCrianca5();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidade() + "/"
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUf() + " - Lugar do Nascimento: "
				+ preRegistroDeclaracoesPrePosto.getNomeUi();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		linhaInformacoesCrianca = "Sexo da crian�a: " + preRegistroDeclaracoesPrePosto.getSexoCrianca5()
				+ " - O fato de ser G�meo: " + ConverteQuantidadeCriancasTexto();
		conteudoLista = new ListItem(linhaInformacoesCrianca, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesCrianca.add(conteudoLista);

		return informacoesCrianca;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosMae() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesMae = new com.itextpdf.text.List(false);
		informacoesMae.setListSymbol("");
		String linhaInformacoesMae = "";
		conteudoLista = new ListItem("Nome da M�e: " + preRegistroDeclaracoesPrePosto.getNomeMae(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesMae.add(conteudoLista);

		conteudoLista = new ListItem("CPF da M�e: " + preRegistroDeclaracoesPrePosto.getCpfMae(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		conteudoLista = new ListItem("Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidadeMae() + " - "
				+ preRegistroDeclaracoesPrePosto.getNaturalidadeUfMae(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		conteudoLista = new ListItem("Profiss�o:" + preRegistroDeclaracoesPrePosto.getProfissaoMae(), fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		linhaInformacoesMae = "Data de Nascimento: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoMae())
				+ " - ";
		if (preRegistroDeclaracoesPrePosto.getTipoDocumentoMae() == null)
			linhaInformacoesMae += "Documento de Identifica��o: N�o consta";
		else {
			linhaInformacoesMae += "Documento de Identifica��o: " + preRegistroDeclaracoesPrePosto.getTipoDocumentoMae()
					+ " - " + preRegistroDeclaracoesPrePosto.getDocumentoMae();
		}
		conteudoLista = new ListItem(linhaInformacoesMae, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		conteudoLista = new ListItem("Av�s maternos do(a) Registrando(a):", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		if (preRegistroDeclaracoesPrePosto.getFiliacaoPaiMae().isEmpty())
			conteudoLista = new ListItem("Nome do Av�: N�o consta", fonteGeral);
		else
			conteudoLista = new ListItem("Nome do Av�: " + preRegistroDeclaracoesPrePosto.getFiliacaoPaiMae(),
					fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);
		if (preRegistroDeclaracoesPrePosto.getFiliacaoMaeMae().isEmpty())
			conteudoLista = new ListItem("Nome da Av�: N�o consta", fonteGeral);
		else
			conteudoLista = new ListItem("Nome da Av�: " + preRegistroDeclaracoesPrePosto.getFiliacaoMaeMae(),
					fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		conteudoLista = new ListItem(
				"Idade da M�e na ocasi�o do parto: "
						+ Util.CalculaIdade(preRegistroDeclaracoesPrePosto.getDataNascimentoMae()) + " anos",
				fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesMae.add(conteudoLista);

		return informacoesMae;
	}

	public com.itextpdf.text.List montaDeclaracaoDadosPai() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesPai = new com.itextpdf.text.List(false);
		informacoesPai.setListSymbol("");
		String linhainformacoesPai = "";
		if (preRegistroDeclaracoesPrePosto.getStatusPai().equals("2")) {
			conteudoLista = new ListItem("PAI N�O DECLARADO", fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			conteudoLista.setLeading(25f);
			informacoesPai.add(conteudoLista);
		} else {
			if (Util.isNullOrEmpty(preRegistroDeclaracoesPrePosto.getNomePai()))
				conteudoLista = new ListItem("Nome do Pai: N�o consta", fonteGeral);
			else
				conteudoLista = new ListItem("Nome do Pai: " + preRegistroDeclaracoesPrePosto.getNomePai(), fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			conteudoLista.setLeading(25f);
			informacoesPai.add(conteudoLista);
			if (Util.isNullOrEmpty(preRegistroDeclaracoesPrePosto.getCpfPai()))
				conteudoLista = new ListItem("CPF do Pai: N�o consta", fonteGeral);
			else
				conteudoLista = new ListItem("CPF do Pai: " + preRegistroDeclaracoesPrePosto.getCpfPai(), fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);

			conteudoLista = new ListItem("Naturalidade: " + preRegistroDeclaracoesPrePosto.getNaturalidadePai() + " - "
					+ preRegistroDeclaracoesPrePosto.getNaturalidadeUfPai(), fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);

			conteudoLista = new ListItem("Profiss�o:" + preRegistroDeclaracoesPrePosto.getProfissaoPai(), fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);

			linhainformacoesPai = "Data de Nascimento: "
					+ new SimpleDateFormat("dd/MM/yyyy").format(preRegistroDeclaracoesPrePosto.getDataNascimentoMae())
					+ " - ";
			if (preRegistroDeclaracoesPrePosto.getTipoDocumentoPai() == null)
				linhainformacoesPai += "Documento de Identifica��o: N�o consta";
			else {
				linhainformacoesPai += "Documento de Identifica��o: "
						+ preRegistroDeclaracoesPrePosto.getTipoDocumentoPai() + " - "
						+ preRegistroDeclaracoesPrePosto.getDocumentoPai();
			}
			conteudoLista = new ListItem(linhainformacoesPai, fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);

			conteudoLista = new ListItem("Av�s paternos do(a) Registrando(a):", fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);
			if (preRegistroDeclaracoesPrePosto.getFiliacaoPaiPai().isEmpty())
				conteudoLista = new ListItem("Nome do Av�: N�o consta", fonteGeral);
			else
				conteudoLista = new ListItem("Nome do Av�: " + preRegistroDeclaracoesPrePosto.getFiliacaoPaiPai(),
						fonteGeral);

			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);

			if (preRegistroDeclaracoesPrePosto.getFiliacaoPaiPai().isEmpty())
				conteudoLista = new ListItem("Nome da Av�: N�o consta", fonteGeral);
			else
				conteudoLista = new ListItem("Nome da Av�: " + preRegistroDeclaracoesPrePosto.getFiliacaoMaePai(),
						fonteGeral);

			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesPai.add(conteudoLista);
		}

		return informacoesPai;
	}

	public com.itextpdf.text.List montaDeclaracaoResidenciaGenitores() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesResidenciaGenitores = new com.itextpdf.text.List(false);
		informacoesResidenciaGenitores.setListSymbol("");
		String linhaInformacoesResidenciaGenitores = "M�e: " + preRegistroDeclaracoesPrePosto.getResidenciaUfMae()
				+ " - " + preRegistroDeclaracoesPrePosto.getResidenciaMunicipioMae() + " - ";
		linhaInformacoesResidenciaGenitores += preRegistroDeclaracoesPrePosto.getResidenciaBairroMae() + ", "
				+ preRegistroDeclaracoesPrePosto.getResidenciaLogradouroMae() + ", ";
		linhaInformacoesResidenciaGenitores += preRegistroDeclaracoesPrePosto.getResidenciaNumeroMae() + ", CEP: "
				+ preRegistroDeclaracoesPrePosto.getResidenciaCEPMae();
		conteudoLista = new ListItem("Resid�ncia ou Domic�lio dos Genitores:", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		conteudoLista.setLeading(25f);
		informacoesResidenciaGenitores.add(conteudoLista);

		conteudoLista = new ListItem(linhaInformacoesResidenciaGenitores, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_LEFT);
		informacoesResidenciaGenitores.add(conteudoLista);

		if (!preRegistroDeclaracoesPrePosto.getStatusPai().equals("2")) {
			linhaInformacoesResidenciaGenitores = "Pai: " + preRegistroDeclaracoesPrePosto.getResidenciaUfPai() + " - "
					+ preRegistroDeclaracoesPrePosto.getResidenciaMunicipioPai() + " - ";
			linhaInformacoesResidenciaGenitores += preRegistroDeclaracoesPrePosto.getResidenciaBairroPai() + ", "
					+ preRegistroDeclaracoesPrePosto.getResidenciaLogradouroPai() + ", ";
			linhaInformacoesResidenciaGenitores += preRegistroDeclaracoesPrePosto.getResidenciaNumeroPai() + ", CEP: "
					+ preRegistroDeclaracoesPrePosto.getResidenciaCEPpai();

			conteudoLista = new ListItem(linhaInformacoesResidenciaGenitores, fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesResidenciaGenitores.add(conteudoLista);
		}

		return informacoesResidenciaGenitores;
	}

	public com.itextpdf.text.List montaDeclaracaoDeclarante() {
		String declarante = preRegistroDeclaracoesPrePosto.getTipoDeclarante();
		String linhaInformacoesDeclarante = "";
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesResidenciaGenitores = new com.itextpdf.text.List(false);
		informacoesResidenciaGenitores.setListSymbol("");

		if (declarante.equals("M�e") || declarante.equals("Pai") || declarante.equals("Os pais")) {
			conteudoLista = new ListItem("Declarante: " + declarante, fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			conteudoLista.setLeading(25f);
			informacoesResidenciaGenitores.add(conteudoLista);
		} else {

			conteudoLista = new ListItem("Declarante: " + declarante, fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			conteudoLista.setLeading(25f);
			informacoesResidenciaGenitores.add(conteudoLista);

			conteudoLista = new ListItem("Tipo de Declara��o: " + preRegistroDeclaracoesPrePosto.getTipoDeclaracao(),
					fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesResidenciaGenitores.add(conteudoLista);

			conteudoLista = new ListItem("Nome do Declarante: " + preRegistroDeclaracoesPrePosto.getNomeDeclarante(),
					fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesResidenciaGenitores.add(conteudoLista);

			conteudoLista = new ListItem("Profiss�o: " + preRegistroDeclaracoesPrePosto.getProfissaoDeclarante(),
					fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesResidenciaGenitores.add(conteudoLista);

			linhaInformacoesDeclarante = "Data de Nascimento: " + new SimpleDateFormat("dd/MM/yyyy")
					.format(preRegistroDeclaracoesPrePosto.getDataNascimentoDeclarante()) + " - ";
			if (preRegistroDeclaracoesPrePosto.getTipoDocDeclarante() == null)
				linhaInformacoesDeclarante += "Documento de Identifica��o: N�o consta";
			else {
				linhaInformacoesDeclarante += "Documento de Identifica��o: "
						+ preRegistroDeclaracoesPrePosto.getTipoDocDeclarante() + " - "
						+ preRegistroDeclaracoesPrePosto.getDocDeclarante();
			}
			conteudoLista = new ListItem(linhaInformacoesDeclarante, fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesResidenciaGenitores.add(conteudoLista);

			linhaInformacoesDeclarante = "Resid�ncia ou Domic�lio: "
					+ preRegistroDeclaracoesPrePosto.getUfResidenciaDeclarante() + " - ";
			linhaInformacoesDeclarante += preRegistroDeclaracoesPrePosto.getMunicipioResidenciaDeclarante() + " - ";
			linhaInformacoesDeclarante += preRegistroDeclaracoesPrePosto.getBairroResidenciaDeclarante() + ", ";
			linhaInformacoesDeclarante += preRegistroDeclaracoesPrePosto.getLogradouroResidenciaDeclarante() + ", ";
			linhaInformacoesDeclarante += preRegistroDeclaracoesPrePosto.getNumeroResidenciaDeclarante();
			conteudoLista = new ListItem(linhaInformacoesDeclarante, fonteGeral);
			conteudoLista.setAlignment(Element.ALIGN_LEFT);
			informacoesResidenciaGenitores.add(conteudoLista);

		}

		return informacoesResidenciaGenitores;
	}

	public com.itextpdf.text.List montaDeclaracaoRodape() {
		ListItem conteudoLista = new ListItem();
		com.itextpdf.text.List informacoesRodape = new com.itextpdf.text.List(false);
		informacoesRodape.setListSymbol("");
		String linhaInformacoesRodape = uiBO
				.recuperaMunicipioUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()) + " - ";
		linhaInformacoesRodape += uiBO.recuperaUfUnidadeInterligadaPrePosto(getUsuarioLogadoPortal().getCpf()) + ", ";
		linhaInformacoesRodape += new SimpleDateFormat("dd/MM/yyyy").format(getPreRegistroDAO().retornaDataBanco());
		conteudoLista = new ListItem(linhaInformacoesRodape, fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(40f);
		informacoesRodape.add(conteudoLista);

		conteudoLista = new ListItem(
				"___________________________________________________________________________________", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		conteudoLista.setLeading(25f);
		informacoesRodape.add(conteudoLista);
		conteudoLista = new ListItem("(" + getUsuarioLogadoPortal().getNome() + ")", fonteGeral);
		conteudoLista.setAlignment(Element.ALIGN_CENTER);
		informacoesRodape.add(conteudoLista);

		return informacoesRodape;
	}

	public boolean exibeDeclaracaoRecusaPaternidade() {
		if (preRegistroDeclaracoesPrePosto.getStatusPai().equals("2")) {
			return true;
		}
		return false;
	}

	public boolean exibeDeclaracaoIndicacaoPaternidade() {
		if (preRegistroDeclaracoesPrePosto.getStatusPai().equals("3")) {
			return true;
		}
		return false;
	}

	public void converteDocumentacaoArrayBytes(FileUploadEvent event) throws IOException {
		String extesaoArquivo = UploadUtils.obterExtensaoArquivo(event);
		BigDecimal divisor1024 = new BigDecimal(1024L);
		BigDecimal tamanhoMaximoDocumentacao = new BigDecimal(7);
		BigDecimal tamanhoAtualDocumentacao = new BigDecimal(event.getFile().getSize()).divide(divisor1024)
				.divide(divisor1024).setScale(2, BigDecimal.ROUND_HALF_UP);

		if (extesaoArquivo.equals("RAR") || extesaoArquivo.equals("ZIP") || extesaoArquivo.equals("7Z")) {
			if (tamanhoAtualDocumentacao.compareTo(tamanhoMaximoDocumentacao) < 0) {
				documentacaoAnexaBytes = event.getFile().getContents();
				setDocumentacaoanexada(true);
			} else
				Mensagem.errorSemBundle("Tamanho m�ximo do arquivo excedido. O arquivo deve possuir no m�ximo 5 MB");

		} else
			Mensagem.errorSemBundle("Arquivo inv�lido. Favor anexar um arquivo compactado");

	}

	public void removerDocumentacaoAnexada() {
		documentacaoAnexaBytes = null;
		setDocumentacaoanexada(false);
	}

	public String anexaDocumentacaoConvertida() throws IOException {
		String nomeArquivo = preRegistroDeclaracoesPrePosto.getId().toString();
		String caminho = "localhost:8080/teste/" + nomeArquivo
				+ ".rar";
		FileOutputStream anexaArquivo = new FileOutputStream(new File(caminho));
		anexaArquivo.write(documentacaoAnexaBytes);
		anexaArquivo.close();
		gravaPreRegistroPrePostoTelaDeclaracoes();
		return principalController.direcionaInicial();
	}

	public void gravaPreRegistroPrePostoTelaDeclaracoes() {
		preRegistroDeclaracoesPrePosto.setSituacaoSolicitacao(SituacaoSolicitacaoUI.COD_EM_ANDAMENTO);
		historicoPreRegistroBO.gravaPreRegistroComHistorico(preRegistroDeclaracoesPrePosto,
				getUsuarioLogadoPortal().getId());
		Mensagem.infoSemBundle("Solicita��o enviada com sucesso.");
	}

}
