package ecivil.ejb.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class CriptografiaUtil {
	
	public static String retornaCriptografadoMD5(String texto) {
		try {
			MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(texto.getBytes("UTF-8")));
			String s = hash.toString(16);
			while (s.length() < 32) {
				s = "0" + s;
			}
			return s;
		} catch (Exception e) {
			return "";
		}
	}
	
}
