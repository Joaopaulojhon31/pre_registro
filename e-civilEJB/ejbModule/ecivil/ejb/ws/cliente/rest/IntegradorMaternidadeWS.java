package ecivil.ejb.ws.cliente.rest;

import java.util.List;
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
import ecivil.ejb.ws.cliente.rest.response.integradormaternidade.AutenticacaoResponse;

@Stateless
public class IntegradorMaternidadeWS {
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public List<AutenticacaoResponse> autenticarUsuarioWebRecivil(String cpf) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_INTEGRADOR_MATERNIDADE) + "/usuario/autenticar/{cpf}");
		
		Response response = webTarget.resolveTemplate("cpf", cpf.trim()).request(MediaType.APPLICATION_JSON_TYPE).get();
		
		if (response.getStatus() != 200) {
			throw new ECivilException("Não foi possível autenticar o usuário pelo login do WebRecivil nesse momento no Integrador Maternidade. Tente novamente mais tarde.");
		}
		
		return response.readEntity(new GenericType<List<AutenticacaoResponse>>() {});
	}

}
