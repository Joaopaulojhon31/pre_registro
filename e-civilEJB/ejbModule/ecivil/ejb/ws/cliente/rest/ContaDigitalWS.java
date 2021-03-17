package ecivil.ejb.ws.cliente.rest;

import java.math.BigDecimal;
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
import ecivil.ejb.ws.cliente.rest.request.contadigital.AlterarSaqueProvisionadoRequest;
import ecivil.ejb.ws.cliente.rest.request.contadigital.ContaDigitalRequest;
import ecivil.ejb.ws.cliente.rest.request.contadigital.FiltroExtratoRequest;
import ecivil.ejb.ws.cliente.rest.request.contadigital.FiltroSaqueProvisionadoRequest;
import ecivil.ejb.ws.cliente.rest.response.contadigital.ExtratoResponse;
import ecivil.ejb.ws.cliente.rest.response.contadigital.SaqueProvisionadoResponse;

@Stateless
public class ContaDigitalWS {
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public BigDecimal consultaSaldo(String codigoCorregedoria) throws ECivilException {
//		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/conta/consultar/saldo/{codigoCorregedoria}");
//		
//		Response response = webTarget.resolveTemplate("codigoCorregedoria", codigoCorregedoria).request(MediaType.APPLICATION_JSON_TYPE).get();
//	
//		if (response.getStatus() != 200) {
//			throw new ECivilException(response.readEntity(String.class));
//		}
//		
//		return response.readEntity(new GenericType<BigDecimal>() {});
		
		return BigDecimal.ZERO;
	}
	
	public void realizarTransferencia(ContaDigitalRequest contaDigitalRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/conta/realizarTransferencia");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(contaDigitalRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
	}
	
	public void adicionarSaqueProvisionado(ContaDigitalRequest contaDigitalRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/saqueProvisionado/adicionar");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(contaDigitalRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
	}
	
	public SaqueProvisionadoResponse consultarSaqueProvisonadoDetalhado(FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/saqueProvisionado/consultar/detalhado");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(filtroSaqueProvisionadoRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<SaqueProvisionadoResponse>() {});
	}
	
	public SaqueProvisionadoResponse consultarSaqueProvisonadoAgrupado(FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/saqueProvisionado/consultar/agrupado");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(filtroSaqueProvisionadoRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<SaqueProvisionadoResponse>() {});
	}
	
	public SaqueProvisionadoResponse consultarUltimosSaqueProvisionado(String codigoCorregedoria, Integer quantidadeRegistros) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/saqueProvisionado/ultimasSolicitacoes/{codigoCorregedoria}/{quantidadeRegistros}");
		
		Response response = webTarget
				.resolveTemplate("codigoCorregedoria", codigoCorregedoria)
				.resolveTemplate("quantidadeRegistros", quantidadeRegistros)
				.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<SaqueProvisionadoResponse>() {});
	}
	
	public ExtratoResponse consultarExtrato(FiltroExtratoRequest filtroExtratoRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/extrato/consultar");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(filtroExtratoRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
		
		return response.readEntity(new GenericType<ExtratoResponse>() {});
	}
	
	public void alteraSituacaoTransferenciaLancada(AlterarSaqueProvisionadoRequest alterarSaqueProvisionadoRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/saqueProvisionado/transferenciaLancada");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(alterarSaqueProvisionadoRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
	}
	
	public void alteraSituacaoTransferenciaConfirmada(AlterarSaqueProvisionadoRequest alterarSaqueProvisionadoRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/saqueProvisionado/transferenciaConfirmada");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(alterarSaqueProvisionadoRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
	}

	public void realizarEstorno(ContaDigitalRequest contaDigitalRequest) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_CONTA_DIGITAL) + "/conta/realizarEstorno");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(contaDigitalRequest));
		
		if (response.getStatus() != 200) {
			throw new ECivilException(response.readEntity(String.class));
		}
	}
	
}
