package ecivil.ejb.certificacao.digital;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CrlClient;
import com.itextpdf.text.pdf.security.CrlClientOnline;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

import ecivil.ejb.util.Util;

public class AssinaturaDigitalPDF {

	public static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();
	
	public byte[] assinarComCertificadoPKSC12(byte[] pdf, String senha, String caminhoCertificadoPKSC12, String reason, Date dataAtual, String siglaUf) throws Exception {
		CertificadoDigital certificadoDigital = new CertificadoDigital();
		certificadoDigital.inicializarParamentrosPKSC12(caminhoCertificadoPKSC12, senha);
		certificadoDigital.validarValidadeCertificadoDigital(dataAtual);
		return assinar(pdf, certificadoDigital.getChain(), certificadoDigital.getPrivateKey(), DigestAlgorithms.SHA256, "BC", CryptoStandard.CMS, reason, "RECIVIL - " + siglaUf);
	}

	public byte[] assinar(byte[] pdf, Certificate[] chain, PrivateKey pk, String digestAlgorithm, String provider, CryptoStandard subfilter, String reason, String location)
			throws GeneralSecurityException, IOException, DocumentException {
		
		// Creating the reader and the stamper

		PdfReader reader = new PdfReader(pdf);
		ByteArrayOutputStream fout = new ByteArrayOutputStream();
		PdfStamper stamper = PdfStamper.createSignature(reader, fout, '\0', null, true);
		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		appearance.setReason(reason);
		appearance.setLocation(location);
		// appearance.setVisibleSignature(new Rectangle(36, 748, 144, 780), 1, "sig");
		// Creating the signature

		List<CrlClient> crlList = new ArrayList<CrlClient>();
		crlList.add(new CrlClientOnline());

		ExternalDigest digest = new BouncyCastleDigest();
		ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, provider);
		MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);
		return fout.toByteArray();
	}

	public static boolean validaAssinaturaSecretarioGeral(X509Certificate certificadoAssinante, String diretorioCertificadoSecretarioGeral) throws Exception {
		List<X509Certificate> listaCertificadosValidos = retornaListaCertificadosDiretorio(diretorioCertificadoSecretarioGeral);
		if (listaCertificadosValidos != null) {
			for (X509Certificate certificadoValido : listaCertificadosValidos) {
				if (certificadoAssinante.getSerialNumber().equals(certificadoValido.getSerialNumber())) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<X509Certificate> retornaListaCertificadosDiretorio(String diretorio) throws Exception {
		List<X509Certificate> listaCertificados = new ArrayList<X509Certificate>();
		File diretorioCadeias = new File(diretorio);
		if (diretorioCadeias.isDirectory()) {
			String[] nomeCertificado = diretorioCadeias.list();
			for (int i = 0; i < nomeCertificado.length; i++) {
				X509Certificate certificadoIcpBrasil = loadCertificate(diretorio + nomeCertificado[i]);
				listaCertificados.add(certificadoIcpBrasil);
			}
		}
		return listaCertificados;
	}

	public static X509Certificate loadCertificate(String pathCertificate) throws Exception {
		InputStream inStream = new FileInputStream(pathCertificate);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);
		inStream.close();
		return cert;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static byte[] inserir_String_No_Pdf(byte[] pdf, String nome, String valor) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfReader reader = new PdfReader(new ByteArrayInputStream(pdf));
		PdfStamper stamper = new PdfStamper(reader, outputStream);
		HashMap info = reader.getInfo();
		info.put(nome, valor);
		stamper.setMoreInfo(info);
		stamper.close();
		return outputStream.toByteArray();
	}

	public static byte[] inserirMarcaDagua(byte[] pdf, String pathImgBackground) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfReader Read_PDF_To_Watermark = new PdfReader(pdf);
		int number_of_pages = Read_PDF_To_Watermark.getNumberOfPages();
		PdfStamper stamp = new PdfStamper(Read_PDF_To_Watermark, outputStream);
		int i = 0;
		Image watermark_image = Image.getInstance(pathImgBackground);
		watermark_image.setAbsolutePosition(0, 0);
		PdfContentByte add_watermark;
		while (i < number_of_pages) {
			i++;
			add_watermark = stamp.getUnderContent(i);
			add_watermark.addImage(watermark_image);
		}
		stamp.close();
		return outputStream.toByteArray();
	}
	
	public static int getNumeroPaginasPdf(byte[] pdf) throws Exception {
		PdfReader reader = new PdfReader(pdf);
		return reader.getNumberOfPages();
	}

	@SuppressWarnings("rawtypes")
	public static boolean possuiAssinaturaDigital(byte[] pdfAssinado) throws Exception {
		if (pdfAssinado != null) {
			PdfReader reader = new PdfReader(pdfAssinado);
			AcroFields af = reader.getAcroFields();
			ArrayList names = af.getSignatureNames();
			return names != null && names.size() > 0;
		}
		return Boolean.FALSE;
	}

	public static byte[] removerAssinaturaPDF(byte[] pdfAssinado) throws DocumentException, IOException, Exception {
		PdfReader reader = new PdfReader(pdfAssinado);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, outputStream);
		stamper.setFormFlattening(Boolean.TRUE);
		stamper.close();
		return outputStream.toByteArray();
	}
	
	public static boolean cpfAssinouPDF(String cpf, byte[] pdfAssinado) throws Exception {
		if (possuiAssinaturaDigital(pdfAssinado)) {
			List<String> listaCpfDocumentoAssinado = recupearCpfsCertificados(pdfAssinado, cpf);
			if (listaCpfDocumentoAssinado != null) {
				for (String cpfDocumentoAssinado : listaCpfDocumentoAssinado) {
					if (cpfDocumentoAssinado.equals(cpf)) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}
	
	private static List<String> recupearCpfsCertificados(byte[] pdfAssinado, String cpfAssinante) throws Exception {
		List<String> listaCpf = new ArrayList<String>();
		List<X509Certificate> listaCertificados = recuperarCertificadosPdfAssinado(pdfAssinado);
		CertificadoDigital certificadoDigital = new CertificadoDigital();
		
		if (listaCertificados != null) {
			for (X509Certificate certificado : listaCertificados) {
				String cpf = certificadoDigital.recuperarCPF(certificado);
				if (Util.ehStringValida(cpf)) {
					listaCpf.add(cpf);
				} else {
					System.out.println("##### certificadoDigital.recuperarCPF() nao encontrou CPF no documento informado. Assinante: " + cpfAssinante);
//					listaCpf.add(certificadoDigital.recuperarCPFCertificadoIcpBrasil(certificado));
				}
			}
		}
		
		return listaCpf;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<X509Certificate> recuperarCertificadosPdfAssinado(byte[] pdfAssinado) throws Exception {
		Security.addProvider(PROVIDER);
		List<X509Certificate> listaCertificados = new ArrayList<X509Certificate>();
		PdfReader reader = new PdfReader(pdfAssinado);
		AcroFields af = reader.getAcroFields();
		ArrayList names = af.getSignatureNames();
		
		for (int k = 0; k < names.size(); ++k) {
			String name = (String) names.get(k);
			PdfPKCS7 pk = af.verifySignature(name);
			listaCertificados.add(pk.getSigningCertificate());
		}
		
		return listaCertificados;
	}

}
