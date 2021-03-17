package ecivil.ejb.imagem;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

class RodapePagina extends PdfPageEventHelper {
	
	private int pagenumber;
	private int totalPaginas;
	private String chancelaDocumento;

	public RodapePagina(int totalPaginas, String chancelaDocumento) {
		super();
		this.totalPaginas = totalPaginas;
		this.chancelaDocumento = chancelaDocumento;
	}

	/**
	 * Initialize one of the headers.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
	}

	/**
	 * Initialize one of the headers, based on the chapter title; reset the page
	 * number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
	 *      com.itextpdf.text.Paragraph)
	 */
	public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
		pagenumber = 1;
	}

	/**
	 * Increase the page number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		pagenumber++;
	}

	/**
	 * Adds the header and the footer.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public synchronized void onEndPage(PdfWriter writer, Document document) {

		Rectangle r = document.getPageSize();
//		boolean paginaGrande = r.getHeight() > 900;
		PdfContentByte cb = writer.getDirectContent();

		float percentWidth = 100.0f;
//		float percentHeight = 100.0f;
		
		if (r.getWidth() != 593.0f) {
			percentWidth = (r.getWidth() * 100) / 593.0f;
		}
		
//		if (r.getHeight() != 841.0) {
//			percentHeight = (r.getHeight() * 100) / 841.0f;
//		}
		
		float margemDireita = 10 * (percentWidth / 100);
		float margemDireitaMensagem = 1 * (percentWidth / 100);
		float alturaBox = 95 * (percentWidth / 100);
		float alturaBoxPagina = 15 * (percentWidth / 100);
//		float alturaBoxImagem = 1 * (percentWidth / 100);
		float width = r.getWidth();
		float tamanhoLinha = 10 * (percentWidth / 100);
		float fontsize = 10 * (percentWidth / 100);

		String linha = "___________________________________________________________________________________________________________________";
		
//		if (paginaGrande) {
//			linha = linha + linhaGrande;
//		}
		
		String mensagem = "";
		String pagina = "";

		if (true) {
			mensagem = linha + montaMensagem();
			pagina = "pág. " + pagenumber + "/" + this.totalPaginas + " ";
		}
		
		Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, fontsize);
		Phrase phrase = new Phrase(mensagem, font3);
		Phrase phrasePagina = new Phrase(pagina, font3);

		ColumnText ct = new ColumnText(cb);
		ct.setSimpleColumn(phrase, margemDireitaMensagem, alturaBox, width - margemDireita, 1, tamanhoLinha, Element.ALIGN_JUSTIFIED);
		
		try {
			ct.go();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		ColumnText ct1 = new ColumnText(cb);
		ct1.setSimpleColumn(phrasePagina, margemDireita, alturaBoxPagina, width - margemDireita, 1, tamanhoLinha, Element.ALIGN_RIGHT);
		
		try {
			ct1.go();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public String montaMensagem() {
		String mensagem = "";
		mensagem = chancelaDocumento;
		return mensagem;
	}

}