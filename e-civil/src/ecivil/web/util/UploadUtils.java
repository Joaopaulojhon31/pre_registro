package ecivil.web.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.primefaces.event.FileUploadEvent;

public class UploadUtils {

	public static final String EXTENSAO_PDF = "PDF";

	public static final String UTF8 = "UTF-8";

	private UploadUtils() {
		super();
	}

	public static String obterNomeArquivoSemExtensao(FileUploadEvent fileUploadEvent) {
		if (fileUploadEvent != null && fileUploadEvent.getFile() != null && fileUploadEvent.getFile().getFileName() != null) {
			String nomeArquivoComExtensao = "";
			try {
				nomeArquivoComExtensao = new String(fileUploadEvent.getFile().getFileName().getBytes(Charset.defaultCharset()), UTF8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int inicioExtensaoArquivo = nomeArquivoComExtensao.lastIndexOf(".");
			return nomeArquivoComExtensao.substring(0, inicioExtensaoArquivo);
		}
		return null;
	}

	public static String obterExtensaoArquivo(FileUploadEvent fileUploadEvent) {
		String extensao = "";
		if (fileUploadEvent != null && fileUploadEvent.getFile() != null && fileUploadEvent.getFile().getFileName() != null) {
			try {
				String nomeArquivoComExtensao = new String(fileUploadEvent.getFile().getFileName().getBytes(Charset.defaultCharset()), UTF8);
				int inicioExtensaoArquivo = nomeArquivoComExtensao.lastIndexOf(".");
				extensao = nomeArquivoComExtensao.substring(inicioExtensaoArquivo + 1, nomeArquivoComExtensao.length()).toUpperCase();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return extensao;
	}

	public static String recuperaTamanhoArquivo(long size) {
		BigDecimal divisor1024 = new BigDecimal(1024L);
		return String.valueOf((new BigDecimal(size).divide(divisor1024).divide(divisor1024)).setScale(2,BigDecimal.ROUND_HALF_UP) + " MB");
	}
	
}
