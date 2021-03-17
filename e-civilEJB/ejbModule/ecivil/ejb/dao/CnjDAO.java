package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.CNJ;

@SuppressWarnings("serial")
@Stateless
public class CnjDAO extends BaseDAO {

	public CNJ recuperaServentiaPorId(Integer id) {
		try {
			Query query = getEntityManager().createQuery("SELECT cnj FROM CNJ cnj WHERE cnj.cnjId = :id and cnj.cnjStatus='2'");
			query.setParameter("id", id);
			return (CNJ) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
