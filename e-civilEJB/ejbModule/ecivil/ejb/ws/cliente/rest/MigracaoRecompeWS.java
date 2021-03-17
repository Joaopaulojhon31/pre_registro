package ecivil.ejb.ws.cliente.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

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
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@Stateless
public class MigracaoRecompeWS {
	
	private static final Logger LOGGER = Logger.getLogger(MigracaoRecompeWS.class.getName());

	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public List<CartorioResponse> recuperaListaCartorioAtivo() throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_MIGRACAO_RECOMPE) + "/cartorio/registroCivil/listaTodosAtivos");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
		
		if (response.getStatus() != 200) {
			lancaExcessaoWS(response.readEntity(String.class));
		}
		
		List<CartorioResponse> listaCartorioResponse = response.readEntity(new GenericType<List<CartorioResponse>>() {});
		
		if (listaCartorioResponse == null || listaCartorioResponse.isEmpty()) {
			return null;
		}
		
		return listaCartorioResponse;
	}
	
	public List<CartorioResponse> recuperaListaCartorioPorCodigoMunicipio(String codigoMunicipio) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_MIGRACAO_RECOMPE) + "/cartorio/registroCivil/listaPorMunicipio/{codigoMunicipio}");
		
		Response response = webTarget.resolveTemplate("codigoMunicipio", codigoMunicipio).request(MediaType.APPLICATION_JSON_TYPE).get();
	
		if (response.getStatus() != 200) {
			lancaExcessaoWS(response.readEntity(String.class));
		}
		
		List<CartorioResponse> listaCartorioResponse = response.readEntity(new GenericType<List<CartorioResponse>>() {});
		
		if (listaCartorioResponse == null || listaCartorioResponse.isEmpty()) {
			return null;
		}
		
		return listaCartorioResponse;
	}

	public CartorioResponse recuperaCartorioPorCodigoCorregedoria(String codCorregedoria) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_MIGRACAO_RECOMPE) + "/cartorio/recuperaPorCodCorregedoria/{codCorregedoria}");
		
		Response response = webTarget.resolveTemplate("codCorregedoria", codCorregedoria).request(MediaType.APPLICATION_JSON_TYPE).get();
		
		if (response.getStatus() != 200) {
			lancaExcessaoWS(response.readEntity(String.class));
		}
		
		CartorioResponse cartorioResponse = response.readEntity(new GenericType<CartorioResponse>() {});
		
		if (cartorioResponse == null) {
			return null;
		}
		
		return cartorioResponse;
	}
	
	public List<CartorioResponse> recuperaListaCartorioPorCodigoCorregedoria(List<String> listaCodCorregedoria) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_MIGRACAO_RECOMPE) + "/cartorio/listaPorCodCorregedoria");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(listaCodCorregedoria));
		
		if (response.getStatus() != 200) {
			lancaExcessaoWS(response.readEntity(String.class));
		}
		
		List<CartorioResponse> listaCartorioResponse = response.readEntity(new GenericType<List<CartorioResponse>>() {});
		
		if (listaCartorioResponse == null || listaCartorioResponse.isEmpty()) {
			return null;
		}
		
		return listaCartorioResponse;
	}
	
	private void lancaExcessaoWS(String mensagemErro) throws ECivilException {
		if (Util.ehStringValida(mensagemErro)) {
			LOGGER.info("##### Erro WS Migracao Recompe - " + mensagemErro);
		}
		throw new ECivilException("Não foi possível recuperar os cartórios do municipio selecionado nesse momento. Tente novamente mais tarde.");
	}
	
}
