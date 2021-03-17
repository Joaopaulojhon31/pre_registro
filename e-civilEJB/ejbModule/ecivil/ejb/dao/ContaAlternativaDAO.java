package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.ContaAlternativa;

@SuppressWarnings("serial")
@Stateless
public class ContaAlternativaDAO extends BaseDAO {

	public ContaAlternativa recuperaContaAlternativa(String codigoCorregedoria) {
		try {
			Query query = getEntityManager().createQuery(" SELECT conta FROM ContaAlternativa conta WHERE conta.codigoCorregedoria = :codigoCorregedoria AND conta.dataFim IS NULL ");
			query.setParameter("codigoCorregedoria", codigoCorregedoria);
			return (ContaAlternativa) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
