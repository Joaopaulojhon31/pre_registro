package ecivil.ejb.ws.cliente.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.ws.cliente.rest.request.integradorcrc.BuscarCertidaoRequest;
import ecivil.ejb.ws.cliente.rest.response.integradorcrc.BuscarCertidaoResponse;

@Stateless
public class IntegradorCRCWS {
	
	public static final Integer QTD_REGISTROS_POR_PAGINA = 10;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	private Client getClient() {
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		clientBuilder.connectTimeout(1, TimeUnit.MINUTES);
		clientBuilder.readTimeout(1, TimeUnit.MINUTES);
		return clientBuilder.build();
	}
	
	public BuscarCertidaoResponse buscarCertidaoIndexada(String nomePessoa, String nomeAssociado, Municipio municipioPesquisa, String cpf, boolean nomeIdentico, Integer primeiroRegistro) throws ECivilException {
		WebTarget webTarget = getClient().target(parametroDAO.buscaValorParametro(Parametro.URL_BASE_INTEGRADOR_CRC) + "/certidao/buscaIndexada");
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(montaObjetoRequestBuscaCertidaoIndexada(nomePessoa, nomeAssociado, municipioPesquisa, cpf, nomeIdentico, primeiroRegistro)));
		
		if (response.getStatus() != 200) {
			throw new ECivilException("Não foi possível pesquisar pela certidão nesse momento. Tente novamente mais tarde.");
		}
		
		return response.readEntity(new GenericType<BuscarCertidaoResponse>() {});
	}

	private BuscarCertidaoRequest montaObjetoRequestBuscaCertidaoIndexada(String nomePessoa, String nomeAssociado, Municipio municipioPesquisa, String cpf, boolean nomeIdentico, Integer primeiroRegistro) {
		BuscarCertidaoRequest buscarCertidaoRequest = new BuscarCertidaoRequest();
		buscarCertidaoRequest.setParticulasNomeProcurado(recuperaParticulasTratadas(nomePessoa.trim().split(" ")));
		buscarCertidaoRequest.setNomeIdentico(nomeIdentico);
		buscarCertidaoRequest.setCpf(cpf);
		buscarCertidaoRequest.setNomeAssociado(nomeAssociado);
		if (municipioPesquisa != null) {
			buscarCertidaoRequest.setIdMunicipio(Integer.valueOf(municipioPesquisa.getCodigoRecompe()));
		}
		buscarCertidaoRequest.setPrimeiroRegistro(primeiroRegistro);
		buscarCertidaoRequest.setRegistrosPorPagina(QTD_REGISTROS_POR_PAGINA);
		return buscarCertidaoRequest;
	}
	
	private static String[] recuperaParticulasTratadas(String[] listaParticulas) {
		List<String> listaParticulaTratada = new ArrayList<>();
		for (String particula : Arrays.asList(listaParticulas)) {
			if (StringUtils.isNotEmpty(particula)) {
				listaParticulaTratada.add(particula);
			}
		}
		String[] array = new String[listaParticulaTratada.size()];
		return listaParticulaTratada.toArray(array);
	}

}
