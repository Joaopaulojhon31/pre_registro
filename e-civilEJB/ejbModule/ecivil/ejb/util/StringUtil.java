package ecivil.ejb.util;

import java.util.Arrays;

public class StringUtil {

	public static String completaComZeroAEsquerda(String texto, int tamanho) {
		return lPad(texto, tamanho, '0');
	}

	private static String lPad(String str, int length, char completingChar) {
		str = (str == null ? "" : str);
		int strLength = str.length();
		if (strLength < length) {
			char[] chars = new char[length];
			Arrays.fill(chars, 0, length - strLength, completingChar);
			if (strLength > 0) {
				str.getChars(0, strLength, chars, length - strLength);
			}
			return new String(chars);
		}
		return str;
	}

}
