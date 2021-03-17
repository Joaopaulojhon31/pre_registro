package ecivil.ejb.ws.cliente.rest;

import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.ws.cliente.rest.request.preregistro.BuscaPreRegistroRequest;

@Stateless
public class PreRegistroWS {
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public String gerarXmlPreRegistro(String cpfMae) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_PREREGISTRO_XML) + "/buscar");
		
		Response response = webTarget.request().method("POST",Entity.text(cpfMae));
		
		if (response.getStatus() != 200) {
			if (response.getStatus() == 500) {
				throw new ECivilException(response.readEntity(new GenericType<String>() {}));
			}
			throw new ECivilException("Não foi possível baixar a certidão no momento. Tente novamente mais tarde.");
		}
		
		return response.readEntity(new GenericType<String>() {});
	}
	
	public String gerarXMLPreRegistroViaCRC(String cpfMae, String cnsCartorio) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_PREREGISTRO_XML) + "/gerarXml");
		
		@SuppressWarnings("static-access")
		Response response = webTarget.request().method("POST",Entity.text(cpfMae));
		
		if (response.getStatus() != 200) {
			if (response.getStatus() == 500) {
				throw new ECivilException(response.readEntity(new GenericType<String>() {}));
			}
			throw new ECivilException("Não foi possível baixar a certidão no momento. Tente novamente mais tarde.");
		}
		
		return response.readEntity(new GenericType<String>() {});
	}
	
	private BuscaPreRegistroRequest montaObjetoRequest(String cpfMae, String cnsCartorio) {
		BuscaPreRegistroRequest buscaPreRegistroRequest = new BuscaPreRegistroRequest();
		buscaPreRegistroRequest.setCpfMae(cpfMae);
		buscaPreRegistroRequest.setCnsCartório(cnsCartorio);
		return buscaPreRegistroRequest;
	}

}
