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

	private String queryListaTodosMunicipioPorSiglaUf() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT mun ");
		sb.append(" FROM Municipio mun ");
		sb.append(" WHERE mun.dataFim IS NULL ");
		sb.append(" AND mun.uf.sigla = :siglaEstado ");
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

}