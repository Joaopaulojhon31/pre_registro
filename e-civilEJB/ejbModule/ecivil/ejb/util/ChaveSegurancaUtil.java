package ecivil.ejb.util;

import java.util.ArrayList;
import java.util.List;

public class ChaveSegurancaUtil {
	
	private static List<String> NUMEROS = new ArrayList<String>();
	private static List<String> LETRAS = new ArrayList<String>();
	private static List<String> ALL_TYPES = new ArrayList<String>();
	private static final int TAMANHO_SENHA = 5;

	private static void inicializar() {
		inicializaNumeros();
		inicializaLetras();
	}

	private static void inicializaLetras() {
		for (int i = 65; i < 91; i++) {
			LETRAS.add((char) i + "");
		}
		for (int i = 97; i < 123; i++) {
			if (i != 108) {// remover letra l (minúscula)
				LETRAS.add((char) i + "");
			}
		}
		ALL_TYPES.addAll(LETRAS);
	}

	private static void inicializaNumeros() {
		for (int i = 48; i < 58; i++) {
			NUMEROS.add((char) i + "");
		}
		ALL_TYPES.addAll(NUMEROS);
	}

	public static String gerarChaveSeguranca() {
		inicializar();
		String result = "";
		for (int i = 0; i < TAMANHO_SENHA; i++) {
			result += ALL_TYPES.get((int) (Math.random() * ALL_TYPES.size()));
		}
		return result;
	}
}
