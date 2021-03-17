package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.Municipio;

@SuppressWarnings("serial")
@Stateless
public class MunicipioDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<Municipio> listaTodosMunicipioPorSiglaUf(String siglaEstado) {
		Query query = getEntityManager().createQuery(queryListaTodosMunicipioPorSiglaUf());
		query.setParameter("siglaEstado", siglaEstado);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Municipio> listaTodosMunicipioPorIdUf(String idEstado) {
		Query query = getEntityManager().createQuery(queryListaTodosMunicipioPorIdUf());
		query.setParameter("idEstado",Long.parseLong(idEstado));
		return query.getResultList();
	}

	private String queryListaTodosMunicipioPorSiglaUf() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.uf.sigla = :siglaEstado ");
		sb.append(" ORDER BY mun.nome ");
		return sb.toString();
	}
	
	private String queryListaTodosMunicipioPorIdUf() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.uf.id = :idEstado ");
		sb.append(" ORDER BY mun.nome ");
		return sb.toString();
	}

	public Municipio recuperaMunicipioPorCodRfb(String codMunicipioRfb) {
		try {
			Query query = getEntityManager().createQuery(queryRecuperaMunicipioPorCodRfb());
			query.setParameter("codMunicipioRfb", codMunicipioRfb);
			return (Municipio) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	private String queryRecuperaMunicipioPorCodRfb() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.codRFB = :codMunicipioRfb ");
		return sb.toString();
	}

	public Municipio recuperaMunicipioPorCodigoRecompe(String codigoMunicipioRecompe) {
		try {
			Query query = getEntityManager().createQuery(queryRecuperaMunicipioPorCodigoRecompe());
			query.setParameter("codigoMunicipioRecompe", codigoMunicipioRecompe);
			return (Municipio) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	private String queryRecuperaMunicipioPorCodigoRecompe() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.codigoRecompe = :codigoMunicipioRecompe ");
		return sb.toString();
	}
	
	public Municipio recuperaMunicipioPorNome(String nomeMunicipio) {
		try {
			Query query = getEntityManager().createQuery(queryRecuperaMunicipioPorNome());
			query.setParameter("nomeMunicipio", nomeMunicipio);
			return (Municipio) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	private String queryRecuperaMunicipioPorNome() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.nome = :nomeMunicipio ");
		return sb.toString();
	}
	
	public Municipio recuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull(String codrfb) {
		try {
			Query query = getEntityManager().createQuery(queryRecuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull());
			query.setParameter("codrfb", codrfb);
			return (Municipio) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	private String queryRecuperaMunicipioPorCodRFBCodigoRecompeDiferenteNull() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.codRFB = :codrfb ");
		sb.append(" AND mun.codigoRecompe IS NOT NULL ");
		return sb.toString();
	}

}