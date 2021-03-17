package ecivil.ejb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Util {

	private static Pattern CEP_FORMATADO_PATTERN = Pattern.compile("^\\d{5}\\-\\d{3}$");
	private static Pattern CEP_NAO_FORMATADO_PATTERN = Pattern.compile("^\\d{8}$");
	private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
	public static final String SIMBOLO_MOEDA_REAL = "R$";

	public static boolean ehStringValida(String valor) {
		return valor != null && !"".equals(valor);
	}
	
	public static String addMaskCPF(String cpf) {
		return addMask("###.###.###-##", cpf);
	}

	public static String addMaskCEP(String cep) {
		if (!ehStringValida(cep)) {
			return null;
		}
		if (cep.trim().length() == 8) {
			return addMask("##.###-###", cep.trim());
		}
		return cep.trim();
	}

	public static String addMaskCodigoServentia(String codigo) {
		return addMask("#####-#", codigo);
	}

	public static String normalizaTexto(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		return str.replaceAll("[^\\x00-\\x7F]", "");
	}

	public static String addMask(String pMask, String pValue) {
		if (pValue == null || pValue.trim().equals("")) {
			return null;
		}
		for (int i = 0; i < pValue.length(); i++) {
			pMask = pMask.replaceFirst("#", pValue.substring(i, i + 1));
		}
		return pMask.replaceAll("#", "");
	}

	public static String addMaskNumeroProtocolo(String numeroProtocolo) {
		return addMask("####/####", numeroProtocolo);
	}

	public static boolean isNuloPeloServicoRFB(String texto) {
		return isNullOrEmpty(texto.replaceAll("0", "")) || "8888888".equals(texto);
	}

	public static <T> T UnProxy(T entity) {
		if (entity == null) {
			return null;
		}

//		if (entity instanceof HibernateProxy) {
//			Hibernate.initialize(entity);
//			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
//		}

		return entity;
	}

	public static <T> List<T> UnProxyList(List<T> list) {
		List<T> unProxyList = new ArrayList<T>();

		for (T element : list) {
			unProxyList.add(UnProxy(element));
		}

		return unProxyList;
	}

	public static boolean isValidCNPJ(String cnpj) {
		if ((cnpj == null) || (cnpj.length() != 14)) {
			return false;
		}
		Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
		return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
	}

	public static String calculaDigitoVerificador(String baseCnpj) {
		Integer digito1 = calcularDigito(baseCnpj.substring(0, 12), pesoCNPJ);
		Integer digito2 = calcularDigito(baseCnpj.substring(0, 12) + digito1, pesoCNPJ);

		return digito1.toString() + digito2.toString();
	}

	public static boolean isValidCPF(String cpf) {
		String cpfr = cpf;
		if ((cpfr == null) || (cpfr.length() != 11)) {
			return false;
		}
		cpfr = cpf.replace(".", "");
		cpfr = cpfr.replace("-", "");
		Integer digito1 = calcularDigito(cpfr.substring(0, 9), pesoCPF);
		Integer digito2 = calcularDigito(cpfr.substring(0, 9) + digito1, pesoCPF);

		boolean valid = cpfr.equals(cpfr.substring(0, 9) + digito1.toString() + digito2.toString());

		if (valid) {
			if (cpfr.equals("11111111111") || cpfr.equals("22222222222") || cpfr.equals("33333333333") || cpfr.equals("44444444444") || cpfr.equals("55555555555") || cpfr.equals("66666666666")
					|| cpfr.equals("77777777777") || cpfr.equals("88888888888") || cpfr.equals("99999999999") || cpfr.equals("00000000000") || cpfr.equals("01234567890"))
				valid = false;
		}

		return valid;
	}

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			digito = Integer.parseInt(str.substring(indice, indice + 1));
			soma += digito * peso[peso.length - str.length() + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static String removeMask(String str) {
		if (!ehStringValida(str)) {
			return null;
		}
		
		String novoValor = "";
		int cont = 0;
		while (cont < str.length()) {
			if (!((str.charAt(cont) + "").equals(".")) && !((str.charAt(cont) + "").equals("/")) && !((str.charAt(cont) + "").equals("-")) && !((str.charAt(cont) + "").equals("("))
					&& !((str.charAt(cont) + "").equals(")"))) {
				
				novoValor += (str.charAt(cont) + "");
			}
			cont++;
		}
		return novoValor.trim();
	}

	public static String geraNumeroAleatorio(int tam) {
		String random = "";
		for (int x = 0; x < tam; x++) {
			random += String.valueOf((long) (Math.random() * 10));
		}
		return random;
	}

	public static BigDecimal transformaCapitalSocialRFB(String capitalSocialRfb) {
		if (!Util.ehStringValida(capitalSocialRfb) || isNuloPeloServicoRFB(capitalSocialRfb)) {
			return null;
		}

		capitalSocialRfb = capitalSocialRfb.substring(0, 12) + "." + capitalSocialRfb.substring(12, 14);

		return new BigDecimal(capitalSocialRfb);
	}

	public static boolean isCepFormatadoValido(String cep) {
		return CEP_FORMATADO_PATTERN.matcher(cep).matches();
	}

	public static boolean isCepValido(String cep) {
		return cep != null && CEP_NAO_FORMATADO_PATTERN.matcher(cep).matches();
	}

	public static String addMascaraTelefoneCelular(String telefone) {
		if (telefone.length() == 10) {
			return addMask("(##)####-####", telefone);
		}
		return addMask("(##)#####-####", telefone);
	}

	public static boolean possuiMaisdoQue2Digitos(String senha) {
		return countDigits(senha) >= 2;
	}

	public static boolean possuiMaisdoQue4Letras(String senha) {
		return countLetters(senha) >= 4;
	}

	public static int countDigits(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				count++;
			}
		}
		return count;
	}

	public static int countLetters(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (Character.isLetter(s.charAt(i))) {
				count++;
			}
		}
		return count;
	}

	public static String getStringFromBundle(String key, Object... arguments) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources_portalinterno");
		if (arguments != null) {
			return MessageFormat.format(resourceBundle.getString(key), (Object[]) arguments);
		}
		return getStringFromBundle(key);
	}

	public static String getStringFromBundle(String key) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources_portalinterno");
		return resourceBundle.getString(key);
	}

	public static String getStringFromBundle(String key, String resource, Object... arguments) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(resource);
		return MessageFormat.format(resourceBundle.getString(key), (Object[]) arguments);
	}

	@SuppressWarnings("resource")
	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			System.out.println("Arquivo gigante. Tamanho do arquivo: " + length + " bytes");
		}

		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}

	public static boolean isEmailValido(String email) {
		// Expressão Regular para validar E-mail
		Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher m = p.matcher(email);
		if (!m.find()) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNullOrEmpty(Object ob) {
		if (ob != null) {
			if (ob instanceof String) {
				if (((String) ob).isEmpty()) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

	public static String addMaskCnpj(String cnpj) {
		return addMask("##.###.###/####-##", cnpj);
	}

	public static String addMaskNumeroRegistroCartorio(String numeroRegistroCartorio) {
		return addMask("################-##", numeroRegistroCartorio);
	}

	public static boolean isNomeValido(String nome) {
		Pattern pattern = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(nome);
		return matcher.find() && !nome.isEmpty();
	}

	public static String generateRandomString(Integer quantidadeCaracteres) {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().substring(0, quantidadeCaracteres);
	}

	public static String textoNuloSeVazio(String str) {
		if (vazio(str))
			return null;
		return str;
	}

	public static boolean vazio(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static String converterBigDecimal(BigDecimal valor) {
		if (valor instanceof BigDecimal) {
			DecimalFormat formater = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("pt", "BR"));
			formater.applyPattern("###,##0.00");
			return formater.format(valor);
		}
		return null;
	}
	
	public static Integer recuperarDia(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static Integer recuperarMes(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		return month;
	}

	public static Integer recuperarAno(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public static String removeAcentos(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	public static String transformar_Em_Maisculo_Primeira_Letra(String texto) {
		if (texto != null && !"".equals(texto)) {
			String primeiraLetraMaiscula = texto.substring(0, 1).toUpperCase();
			return texto.replaceFirst(texto.substring(0, 1), primeiraLetraMaiscula);
		}
		return texto;
	}
	
	public static String transformar_Em_Maisculo_Primeira_Letra_Cada_Palavra(String texto){  
		texto=texto.toLowerCase();
	    String textoSplit[]=texto.split("\\s");  
	    String capitalizeWord="";  
	    for(String subtexto:textoSplit){  
	        String first=subtexto.substring(0,1);  
	        String afterfirst=subtexto.substring(1);  
	        capitalizeWord+=first.toUpperCase()+afterfirst+" ";  
	    }  
	    return capitalizeWord.trim();  
	}  

	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

	public static String formataMoeda(String valor) {
		NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		if (StringUtils.isNotEmpty(valor)) {
			return f.format(retornaBigDecimal(valor));
		}
		return f.format(new BigDecimal("0"));
	}

	public static String formataMoeda(BigDecimal valor) {
		NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		try {
			return f.format(valor);
		} catch (Exception e) {
			return f.format(new BigDecimal("0"));
		}
	}
	
	public static BigDecimal retornaBigDecimal(String s) {
		if (StringUtils.isNotEmpty(s)) {
			try {
				return new BigDecimal(s.replace(",", "."));
			} catch (NumberFormatException n) {
				return new BigDecimal("0");
			}
		}
		return new BigDecimal("0");
	}

	public static void save_File(String pathFile, byte[] file) throws Exception {
		FileOutputStream fos = new FileOutputStream(pathFile);
		fos.write(file);
		fos.close();
	}

	public static Object retornarFieldValue(Object object, String fieldName) throws Exception {
		if (object == null) {
			return null;
		}
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		Object value = field.get(object);
		return value;
	}

	public static String adicionaMascaraCapitalSocialRfb(String valor) {
		if (!Util.ehStringValida(valor)) {
			return null;
		}

		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return nf.format(retornaBigDecimalCapitalSocialRfb(valor));
	}

	private static BigDecimal retornaBigDecimalCapitalSocialRfb(String number) {
		try {
			number = Util.addMask("############.##", number);
			return number == null ? null : new BigDecimal(number);
		} catch (Exception ex) {
			return null;
		}
	}

	public static String formatarStringParaDinheiro(String valor) {
		if (!Util.ehStringValida(valor)) {
			return null;
		}

		BigDecimal unscaled = new BigDecimal(valor);
		BigDecimal scaled = unscaled.scaleByPowerOfTen(-2);
		return String.format("%,.2f", scaled);
	}
	
	public static String formataCodigCartorio(String codigo) {
		if (!ehStringValida(codigo)) {
			return null;
		}
		return codigo.trim().substring(0, 11) + "-" + codigo.substring(11, 13);
	}
	

	public static boolean isValidTelefoneCelularComDdd(String telefoneCelularComDdd) {
		if (!ehStringValida(telefoneCelularComDdd)) {
			return Boolean.FALSE;
		}
		
		if (telefoneCelularComDdd.length() != 10 && telefoneCelularComDdd.length() != 11) {
			return Boolean.FALSE;
		}
		
		if (!ehStringValida(telefoneCelularComDdd.replace("1", "")) || !ehStringValida(telefoneCelularComDdd.replace("2", "")) || !ehStringValida(telefoneCelularComDdd.replace("3", "")) ||
				!ehStringValida(telefoneCelularComDdd.replace("4", "")) || !ehStringValida(telefoneCelularComDdd.replace("5", "")) || !ehStringValida(telefoneCelularComDdd.replace("6", "")) ||
				!ehStringValida(telefoneCelularComDdd.replace("7", "")) || !ehStringValida(telefoneCelularComDdd.replace("8", "")) || !ehStringValida(telefoneCelularComDdd.replace("9", "")) ||
				!ehStringValida(telefoneCelularComDdd.replace("0", ""))) {
			
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}
	
	public static Integer CalculaIdade(Date dataNascimento) {
		int dia = Util.recuperarDia(dataNascimento);
		int mes = Util.recuperarMes(dataNascimento)+1;
		int ano = Util.recuperarAno(dataNascimento);
		Calendar calendario = GregorianCalendar.getInstance();
		int anoAtual = calendario.get(Calendar.YEAR);
		int idade = anoAtual - ano;
		int diaAtual = calendario.get(Calendar.DAY_OF_MONTH);
		int mesAtual = calendario.get(Calendar.MONTH)+1;
		if (dia > diaAtual && mes >= mesAtual) {
			idade--;
		}
		return idade;
	}
	
}