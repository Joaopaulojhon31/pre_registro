package ecivil.ejb.imagem;

import java.awt.geom.AffineTransform;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class ImagemViaUnica {

	@SuppressWarnings("deprecation")
	public synchronized byte[] resize(byte[] imagem, String chancelaDocumento) throws Exception {
		PdfReader reader = new PdfReader(imagem);
		Document doc = new Document();// PageSize.A4, 0, 0, 0, 0);

		RodapePagina event = new RodapePagina(reader.getNumberOfPages(), chancelaDocumento);

		ByteArrayOutputStream imag = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(doc, imag);
		writer.setPageEvent(event);
		doc.open();
		PdfContentByte cb = writer.getDirectContent();
		
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			PdfImportedPage page = writer.getImportedPage(reader, i);
			doc.setPageSize(new Rectangle(page.getWidth(), page.getHeight()));
			doc.newPage();

			AffineTransform af = new AffineTransform();
			af.scale(1, 0.90);
			cb.transform(af);

			float percentWidth = (page.getWidth() * 100) / 593.0f;
			float alturaBox = 95 * (percentWidth / 100);
			cb.addTemplate(page, 0, alturaBox);
		}
		doc.close();
		return imag.toByteArray();
	}

	public static void write(byte[] fileBytes, String yourFile) throws Exception {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(yourFile));
		bos.write(fileBytes);
		bos.flush();
		bos.close();
	}
}
