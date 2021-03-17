package ecivil.ejb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;

import ecivil.ejb.certificacao.digital.AssinaturaDigitalPDF;
import ecivil.ejb.exception.ECivilException;

public class PDFUtil {

	public static byte[] retornaPdfCompactado(byte[] pdf) throws ECivilException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(pdf);
			com.itextpdf.text.pdf.PdfStamper stamper = new com.itextpdf.text.pdf.PdfStamper(reader, baos);
			stamper.setFullCompression();
			stamper.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ECivilException("Não foi possível renderizar o PDF. Favor anexar outro documento.");
		}
	}

	public static byte[] preparaDocumentoUpload(byte[] documento) throws DocumentException, IOException, Exception {
		byte[] imagem = null;
		imagem = AssinaturaDigitalPDF.removerAssinaturaPDF(documento);
		imagem = retornaPdfCompactado(imagem);
		return imagem;
	}
	
}
