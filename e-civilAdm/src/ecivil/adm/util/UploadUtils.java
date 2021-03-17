package ecivil.adm.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.primefaces.event.FileUploadEvent;

import ecivil.ejb.exception.UploadException;
import ecivil.ejb.util.PDFUtil;

public class UploadUtils {

	private static final String EXTENSAO_PDF = "PDF";
	private static final BigDecimal TAMANHO_MAXIMO_UPLOAD_MEGA = new BigDecimal("10");
	private static final String UTF8 = "UTF-8";

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
		return String.valueOf(converteTamanhoArquivoParaMega(size)) + " MB";
	}
	
	private static BigDecimal converteTamanhoArquivoParaMega(long size) {
		BigDecimal divisor1024 = new BigDecimal(1024L);
		return (new BigDecimal(size).divide(divisor1024).divide(divisor1024)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public static void validaUploadPdfExtensaoETamanho(FileUploadEvent event) throws UploadException, Exception {
		if (!EXTENSAO_PDF.equalsIgnoreCase(UploadUtils.obterExtensaoArquivo(event))) {
			throw new UploadException("Só é permitido anexar arquivos com extenção PDF");
		}
		
		BigDecimal tamanhoDocumento = converteTamanhoArquivoParaMega(PDFUtil.preparaDocumentoUpload(event.getFile().getContents()).length);
		if (tamanhoDocumento.compareTo(TAMANHO_MAXIMO_UPLOAD_MEGA) > 0) {
			throw new UploadException("Só é permitido anexar arquivos com tamanho máximo de " + TAMANHO_MAXIMO_UPLOAD_MEGA + "mb");
		}
	}

}
