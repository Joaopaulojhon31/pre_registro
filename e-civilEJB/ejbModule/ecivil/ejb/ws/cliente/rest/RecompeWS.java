package ecivil.ejb.ws.cliente.rest;

import java.util.Base64;
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
import ecivil.ejb.ws.cliente.rest.request.recompe.AtoResponseVo;
import ecivil.ejb.ws.cliente.rest.request.recompe.RegistroBoletoRequestVo;
import ecivil.ejb.ws.cliente.rest.response.recompe.RegistroBoletoResponseVo;

@Stateless
public class RecompeWS {
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public RegistroBoletoResponseVo registrarBoleto(RegistroBoletoRequestVo registroBoletoRequestVo) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_RECOMPE) + "/boleto/bradesco/registro");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(registroBoletoRequestVo));
	
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<RegistroBoletoResponseVo>() {});
	}

	public byte[] imprimirBoleto(String numeroBoleto) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_RECOMPE) + "/boleto/bradesco/imprimir/{numeroBoleto}");
		
		Response response = webTarget.resolveTemplate("numeroBoleto", numeroBoleto).request(MediaType.APPLICATION_JSON_TYPE).get();

		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return Base64.getDecoder().decode(response.readEntity(String.class));
	}

	
	public AtoResponseVo recuperaAtoVigentePorCodigo(String codigoAto) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_RECOMPE) + "/ato/consultarAtoVigente/{codigoAto}");
		
		Response response = webTarget.resolveTemplate("codigoAto", codigoAto).request(MediaType.APPLICATION_JSON_TYPE).get();

		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<AtoResponseVo>() {});
	}
	
}
