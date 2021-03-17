package ecivil.ejb.lookup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;

import ecivil.ejb.dao.MunicipioDAO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.Parametro;

@SuppressWarnings("serial")
@Singleton
@Startup
@ApplicationScoped
@Lock(LockType.READ)
public class MunicipioLookUp implements Serializable {

	@EJB
	private MunicipioDAO municipioDAO;
	
	@EJB
	private ParametroDAO parametroDAO;

	private List<Municipio> listaMunicipiosEstadoSede;

	@PostConstruct
	public void inicializa() {
		setListaMunicipiosEstadoSede(new ArrayList<>());
		inicializarMunicipios();
	}

	private void inicializarMunicipios() {
		setListaMunicipiosEstadoSede(municipioDAO.listaTodosMunicipioPorSiglaUf(parametroDAO.buscaValorParametro(Parametro.SIGLA_ESTADO_SEDE)));
	}
	
	public List<Municipio> getListaMunicipiosEstadoSede() {
		return listaMunicipiosEstadoSede;
	}

	public void setListaMunicipiosEstadoSede(List<Municipio> listaMunicipiosEstadoSede) {
		this.listaMunicipiosEstadoSede = listaMunicipiosEstadoSede;
	}

}
