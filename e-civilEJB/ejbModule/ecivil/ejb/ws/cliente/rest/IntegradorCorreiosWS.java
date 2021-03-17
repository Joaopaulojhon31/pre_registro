package ecivil.ejb.ws.cliente.rest;

import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.ws.cliente.rest.response.integradorcorreios.ConsultaCepResponse;

@Stateless
public class IntegradorCorreiosWS {
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public ConsultaCepResponse consultarCep(String cep) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_INTEGRADOR_CORREIOS) + "/cep/consultar/{cep}");
		
		Response response = webTarget.resolveTemplate("cep", cep).request(MediaType.APPLICATION_JSON_TYPE).get();
		
		if (response.getStatus() != 200) {
			if (response.getStatus() == 500) {
				throw new ECivilException(response.readEntity(new GenericType<String>() {}));
			}
			throw new ECivilException("Não foi pesquisar pelo CEP nesse momento. Tente novamente mais tarde.");
		}
		
		return response.readEntity(new GenericType<ConsultaCepResponse>() {});
	}

}
