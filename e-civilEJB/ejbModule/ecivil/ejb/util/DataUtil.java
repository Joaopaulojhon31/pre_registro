package ecivil.ejb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataUtil {

	private static final String REGEX_YYYYMMDD_TRACO = "\\d{4}-\\d{2}-\\d{2}";
	private static final String REGEX_DDMMYYYY_TRACO = "\\d{2}-\\d{2}-\\d{4}";
	private static final String REGEX_YYYYMMDD_BARRA = "\\d{4}/\\d{2}/\\d{2}";
	private static final String REGEX_DDMMYYYY_BARRA = "\\d{2}/\\d{2}/\\d{4}";
	private static final String REGEX_YYYYMMDD_HHMMSS_TRACO = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
	private static final String REGEX_DDMMYYYY_HHMMSS_TRACO = "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}";
	private static final String REGEX_YYYYMMDD_HHMMSS_BARRA = "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}";
	private static final String REGEX_DDMMYYYY_HHMMSS_BARRA = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}";

	private final static SimpleDateFormat DF_YYYYMMDD_TRACO = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat DF_DDMMYYYY_TRACO = new SimpleDateFormat("dd-MM-yyyy");
	private final static SimpleDateFormat DF_YYYYMMDD_BARRA = new SimpleDateFormat("yyyy/MM/dd");
	private final static SimpleDateFormat DF_DDMMYYYY_BARRA = new SimpleDateFormat("dd/MM/yyyy");
	private final static SimpleDateFormat DF_YYYYMMDD_RFB = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat DF_YYYYMMDD_HHMMSS_TRACO = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat DF_DDMMYYYY_HHMMSS_TRACO = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private final static SimpleDateFormat DF_YYYYMMDD_HHMMSS_BARRA = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private final static SimpleDateFormat DF_DDMMYYYY_HHMMSS_BARRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private final static SimpleDateFormat DF_DDMMYYYY_HHMM_BARRA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public static Date agora() {
		return new Date();
	}
	
	public static String getdataFormatadaStrDDMMYYYY(Date data) {
		return data != null ? DF_DDMMYYYY_BARRA.format(data) : null;
	}

	public static Date converteDataStringParaDate(String dataStr) {
		try {
			if (!Util.ehStringValida(dataStr)) {
				return null;
			}

			if (dataStr.length() == 10) {
				return formataDataStringParaDate(dataStr);
			}

			return formataDataHoraStringParaDate(dataStr);
		} catch (Exception e) {
			return null;
		}
	}

	private static Date formataDataStringParaDate(String dataStr) throws ParseException {
		if (dataStr.matches(REGEX_YYYYMMDD_TRACO)) {
			return (Date) DF_YYYYMMDD_TRACO.parse(dataStr);
		}

		if (dataStr.matches(REGEX_DDMMYYYY_TRACO)) {
			return (Date) DF_DDMMYYYY_TRACO.parse(dataStr);
		}

		if (dataStr.matches(REGEX_YYYYMMDD_BARRA)) {
			return (Date) DF_YYYYMMDD_BARRA.parse(dataStr);
		}

		if (dataStr.matches(REGEX_DDMMYYYY_BARRA)) {
			return (Date) DF_DDMMYYYY_BARRA.parse(dataStr);
		}

		return null;
	}

	private static Date formataDataHoraStringParaDate(String dataStr) throws ParseException {
		if (dataStr.matches(REGEX_YYYYMMDD_HHMMSS_TRACO)) {
			return (Date) DF_YYYYMMDD_HHMMSS_TRACO.parse(dataStr);
		}

		if (dataStr.matches(REGEX_DDMMYYYY_HHMMSS_TRACO)) {
			return (Date) DF_DDMMYYYY_HHMMSS_TRACO.parse(dataStr);
		}

		if (dataStr.matches(REGEX_YYYYMMDD_HHMMSS_BARRA)) {
			return (Date) DF_YYYYMMDD_HHMMSS_BARRA.parse(dataStr);
		}

		if (dataStr.matches(REGEX_DDMMYYYY_HHMMSS_BARRA)) {
			return (Date) DF_DDMMYYYY_HHMMSS_BARRA.parse(dataStr);
		}

		return null;
	}
	
	public static String converteDateParaString_YYYYMMDD_TRACO(Date data) {
		return converteDateParaString(data, DF_YYYYMMDD_TRACO);
	}

	public static String converteDateParaString_DDMMYYY_TRACO(Date data) {
		return converteDateParaString(data, DF_DDMMYYYY_TRACO);
	}

	public static String converteDateParaString_YYYYMMDD_BARRA(Date data) {
		return converteDateParaString(data, DF_YYYYMMDD_BARRA);
	}

	public static String converteDateParaString_DDMMYYY_BARRA(Date data) {
		return converteDateParaString(data, DF_DDMMYYYY_BARRA);
	}

	public static String converteDateParaString_YYYYMMDD_HHMMSS_TRACO(Date data) {
		return converteDateParaString(data, DF_YYYYMMDD_HHMMSS_TRACO);
	}

	public static String converteDateParaString_DDMMYYY_HHMMSS_TRACO(Date data) {
		return converteDateParaString(data, DF_DDMMYYYY_HHMMSS_TRACO);
	}

	public static String converteDateParaString_YYYYMMDD_HHMMSS_BARRA(Date data) {
		return converteDateParaString(data, DF_YYYYMMDD_HHMMSS_BARRA);
	}

	public static String converteDateParaString_DDMMYYY_HHMMSS_BARRA(Date data) {
		return converteDateParaString(data, DF_DDMMYYYY_HHMMSS_BARRA);
	}
	
	public static String converteDateParaString_DDMMYYY_HHMM_BARRA(Date data) {
		return converteDateParaString(data, DF_DDMMYYYY_HHMM_BARRA);
	}

	private static String converteDateParaString(Date data, SimpleDateFormat sdf) {
		try {
			if (data == null) {
				return null;
			}
			return sdf.format(data);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDataPorExtenso() {
		LocalDate now = LocalDate.now();
		StringBuffer sb = new StringBuffer();
		sb.append(now.getDayOfMonth());
		sb.append(" de ");
		sb.append(now.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt")));
		sb.append(" de ");
		sb.append(now.getYear());
		return sb.toString();
	}

	public static Date converteDataStringRFBParaDate(String dataStr) {
		try {
			if (!Util.ehStringValida(dataStr)) {
				return null;
			}
			return DF_YYYYMMDD_RFB.parse(dataStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String converteDateParaDataStringRFB(Date data) {
		if (data == null) {
			return null;
		}
		return DF_YYYYMMDD_RFB.format(data);
	}
	
	public static Calendar converteStringParaCalendar(String dataStr) {
		try {
			if (!Util.ehStringValida(dataStr)) {
				return null;
			}
			Calendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
			cal.setTime(converteDataStringParaDate(dataStr));
			return cal;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Calendar converteDateParaCalendar(Date data) {
		try {
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(data);
			return gregorianCalendar;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getAnoAtual() {
		return String.valueOf(LocalDate.now().getYear());
	}
	
	public static Date adicionaDiasData(Date data, int numeroDias) {
		GregorianCalendar dataGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
		dataGregorian.setTime(data);
		dataGregorian.add(Calendar.DAY_OF_MONTH, numeroDias);
		return dataGregorian.getTime();
	}

	public static Date adicionaMinutosData(Date data, int numeroMinutos) {
		GregorianCalendar dataGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
		dataGregorian.setTime(data);
		dataGregorian.add(Calendar.MINUTE, numeroMinutos);
		return dataGregorian.getTime();
	}

	public static Date subtraiMinutosData(Date data, int numeroMinutos) {
		GregorianCalendar dataGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
		dataGregorian.setTime(data);
		dataGregorian.add(Calendar.MINUTE, -numeroMinutos);
		return dataGregorian.getTime();
	}
	
	public static String formataDataRegistroBoletoBradesco(Date data) {
		return new SimpleDateFormat("dd.MM.yyyy").format(data);
	}

	public static String recuperaDataAtualNomeArquivo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
		return formatter.format(now);
	}
	
}