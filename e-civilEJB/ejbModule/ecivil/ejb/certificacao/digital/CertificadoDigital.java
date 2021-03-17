package ecivil.ejb.certificacao.digital;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.DataUtil;

public class CertificadoDigital {

	private PrivateKey privateKey;
	private KeyStore keyStore;
	private X509Certificate certificate;
	private Certificate[] chain;

	public void inicializarParamentrosPKSC12(String pathCertificate, String senha) throws Exception {
		keyStore = loadKeyStorePKCS12(pathCertificate, senha);
		loadPrivateKey(senha);
		loadCertificate();
		loadChainCertificate();
	}

	public KeyStore loadKeyStorePKCS12(String pathCertificate, String password) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		InputStream file = new FileInputStream(new File(pathCertificate));
		keyStore = KeyStore.getInstance("PKCS12", "BC");
		keyStore.load(file, password.toCharArray());
		return keyStore;
	}

	private Certificate loadCertificate() throws Exception {
		certificate = (X509Certificate) keyStore.getCertificate(getAlias(keyStore));
		return certificate;
	}

	private PrivateKey loadPrivateKey(String password) throws Exception {
		privateKey = (PrivateKey) keyStore.getKey(getAlias(keyStore), password.toCharArray());
		return privateKey;
	}

	private Certificate[] loadChainCertificate() throws Exception {
		chain = (Certificate[]) keyStore.getCertificateChain(getAlias(keyStore));
		return chain;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public KeyStore getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	public Certificate[] getChain() {
		return chain;
	}

	public void setChain(Certificate[] chain) {
		this.chain = chain;
	}

	@SuppressWarnings("rawtypes")
	private String getAlias(KeyStore ks) throws KeyStoreException {
		String keyEntry = null;
		Enumeration en = ks.aliases();

		while (en.hasMoreElements()) {
			keyEntry = (String) en.nextElement();
			if (ks.isKeyEntry(keyEntry)) {
				break;
			}
		}

		return keyEntry;
	}

	public String getAutorCertificado() {
		String subject = getSubjectDN();
		return obterValorChave(subject, "CN=");
	}

	public String obterValorChave(String subject, String chave) {
		int posTipoCertificado = subject.indexOf(chave) + chave.length();
		String subjectNew = subject.substring(posTipoCertificado, subject.length());

		if (subjectNew.indexOf(",") > 0) {
			return (subjectNew.substring(0, subjectNew.indexOf(","))).trim();
		} else {
			return subjectNew.trim();
		}
	}

	public String getSubjectDN() {
		Principal principal = certificate.getSubjectDN();
		return principal.getName();
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	public String recuperarCPF(X509Certificate certificado) throws Exception {
		Collection col = X509ExtensionUtil.getSubjectAlternativeNames(certificado);

		for (Object obj : col) {

			if (obj instanceof ArrayList) {

				ArrayList lst = (ArrayList) obj;
				Object value = lst.get(1);

				if (value instanceof DLSequence) {

					/**
					 * DER Sequence ObjectIdentifier Tagged DER Octet String
					 */
					DLSequence seq = (DLSequence) value;

					ASN1Encodable oid = (ASN1Encodable) seq.getObjectAt(0);

					DERTaggedObject tagged = (DERTaggedObject) seq.getObjectAt(1);
					String info = null;

					ASN1Primitive derObj = tagged.getObject();

					if (derObj instanceof DEROctetString) {
						DEROctetString octet = (DEROctetString) derObj;
						info = new String(octet.getOctets());
					} else if (derObj instanceof DERPrintableString) {
						DERPrintableString octet = (DERPrintableString) derObj;
						info = new String(octet.getOctets());
					} else if (derObj instanceof DERUTF8String) {
						DERUTF8String str = (DERUTF8String) derObj;
						info = str.getString();
					}

					if (info != null && !info.isEmpty()) {
						String cpf = info.substring(8, 19);
						return cpf;
					}
				}
			}
		}
		return null;
	}

	public String getNomeCertificado(X509Certificate certificado) throws Exception {
		// Pega o nome da pessoa.
		String subject;
		Matcher mat = null;
		Pattern pat = Pattern.compile("CN|cn");

		subject = certificado.getSubjectX500Principal().toString();
		StringTokenizer subjectcn = new StringTokenizer(subject, ",", false);
		int nsubjectcn = subjectcn.countTokens();
		boolean tsubjectcn = false;
		String subjectname = null;
		for (int count = 0; count < nsubjectcn & tsubjectcn == false; count++) {
			subjectname = subjectcn.nextElement().toString();
			mat = pat.matcher(subjectname);
			tsubjectcn = mat.find();
		}

		if (tsubjectcn) {
			subjectname = subjectname.substring(subjectname.indexOf("=") + 1, subjectname.length());
			if (subjectname.contains(":"))
				subjectname = subjectname.split(":")[0];
		} else {
			subjectname = "<desconhecido>";
		}

		System.out.println(subjectname);
		return subjectname;
	}
	
	public void validarValidadeCertificadoDigital(Date dataAtual) throws Exception {
		if (dataAtual.after(certificate.getNotAfter())) {
			throw new ECivilException("Certidicado digital vencido. Favor verificar. Validade: ", DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(certificate.getNotAfter()));
		}
	}

}
