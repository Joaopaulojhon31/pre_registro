package web.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

public class VerificaRecaptcha {
	private static final String url = "https://www.google.com/recaptcha/api/siteverify";
	private static final String secret_key = "6LfBm8kZAAAAAAA3sevMyH1_HvtAFSGRsUMkPg_B"; // production
	//private static final String secret_key = "6LeHEd8ZAAAAADx1ZinxKjzVYk5ld2Z1y8i4UzEQ"; // local
	private final static String USER_AGENT = "Mozilla/5.0";
	
	public static boolean verificar(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		try{
			String postParams = "secret=" + secret_key + "&response=" + gRecaptchaResponse;
			HttpsURLConnection connection =  criarConcexao();
			definirFuxoDados(connection, postParams);
			return recuperarJSONResposta(connection).getBoolean("success");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private static HttpsURLConnection criarConcexao() throws IOException {
		URL obj = new URL(url);
		HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", USER_AGENT);
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		connection.setDoOutput(true);
		return (HttpsURLConnection) connection;
	}
	
	private static DataOutputStream definirFuxoDados(HttpsURLConnection connection, String postParams) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
		dataOutputStream.writeBytes(postParams);
		dataOutputStream.flush();
		dataOutputStream.close();
		return dataOutputStream;
	}
	
	private static JsonObject recuperarJSONResposta(HttpsURLConnection connection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
		JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		return jsonObject;
	}
}
