package ecivil.ejb.lookup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.MigracaoRecompeWS;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@SuppressWarnings("serial")
@Singleton
@Startup
@ApplicationScoped
@Lock(LockType.READ)
public class CartorioLookUp implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(CartorioLookUp.class.getName());

	@EJB
	private MigracaoRecompeWS migracaoRecompeWS;

	private List<CartorioResponse> listaCartorio;
	private Map<String, CartorioResponse> mapCartorio;

	@PostConstruct
	public void inicializa() {
		try {
			inicializarListaCartorios();
			inicializarMapCartorios();
		} catch (Exception e) {
			LOGGER.info("#### Erro ao inicilializar lookup dos Cartorios. Mensagem erro: " + e.getMessage());
		}
	}

	public void recarregarDadosCartorios() throws ECivilException {
		inicializarListaCartorios();
		inicializarMapCartorios();
	}

	private void inicializarListaCartorios() throws ECivilException {
		setListaCartorio(migracaoRecompeWS.recuperaListaCartorioAtivo());
	}

	private void inicializarMapCartorios() {
		if (listaCartorio == null || listaCartorio.isEmpty()) {
			return;
		}
		setMapCartorio(listaCartorio.stream().collect(Collectors.toMap(CartorioResponse::getCodigoCorregedoria, cartorio -> cartorio)));
	}

	public List<CartorioResponse> recuperaListaCartorio() throws ECivilException {
		if (listaCartorio == null || listaCartorio.isEmpty()) {
			recarregarDadosCartorios();
		}
		return listaCartorio;
	}

	public CartorioResponse recuperaCartorioPorCodigoCorregedoria(String codigoCorregedoria) throws ECivilException {
		if (!Util.ehStringValida(codigoCorregedoria)) {
			return null;
		}
		
		if (listaCartorio == null || listaCartorio.isEmpty()) {
			recarregarDadosCartorios();
		}
		
		CartorioResponse cartorio = mapCartorio.get(codigoCorregedoria);
		if (cartorio != null) {
			return cartorio;
		}
		
		return migracaoRecompeWS.recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria);
	}
	
	public List<CartorioResponse> recuperaListaCartorioPorCodigoCorregedoria(List<String> listaCodCorregedoria) throws ECivilException {
		if (listaCodCorregedoria == null || listaCodCorregedoria.isEmpty()) {
			return null;
		}
		
		if (listaCartorio == null || listaCartorio.isEmpty()) {
			recarregarDadosCartorios();
		}
		
		List<CartorioResponse> listaCartorioEncontrado = new ArrayList<>();
		for (String codigoCorregedoria : listaCodCorregedoria) {
			CartorioResponse cartorioResponse = mapCartorio.get(codigoCorregedoria);
			if (cartorioResponse != null) {
				listaCartorioEncontrado.add(cartorioResponse);
			}
		}
		return listaCartorioEncontrado;
	}
	
	public String recuperaNomeMunicipioCartorio(String codigoCorregedoria) throws ECivilException {
		CartorioResponse cartorio = recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria);
		return cartorio != null ? cartorio.getNomeMunicipio() : "";
	}

	private void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}

	public void setMapCartorio(Map<String, CartorioResponse> mapCartorio) {
		this.mapCartorio = mapCartorio;
	}

}
