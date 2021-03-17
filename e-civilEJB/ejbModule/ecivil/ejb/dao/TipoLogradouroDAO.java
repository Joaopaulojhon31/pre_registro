package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.TipoLogradouro;

@SuppressWarnings("serial")
@Stateless
public class TipoLogradouroDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<TipoLogradouro> listaTodosTipoLogradouro() {
		String hql = "SELECT tipoLogradouro FROM TipoLogradouro tipoLogradouro ORDER BY tipoLogradouro.nome ASC";
		Query query = getEntityManager().createQuery(hql);
		return query.getResultList();
	}
	
	public TipoLogradouro recuperaTipoLogradouroPorCodigoRfb(String codigoRFB) {
		try {
			Query query = getEntityManager().createQuery("SELECT tipoLogradouro FROM TipoLogradouro tipoLogradouro WHERE tipoLogradouro.codigoRFB = :codigoRFB");
			query.setParameter("codigoRFB", codigoRFB);
			return (TipoLogradouro) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
