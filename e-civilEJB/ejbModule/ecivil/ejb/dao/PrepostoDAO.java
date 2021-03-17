package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.CNJ;
import ecivil.ejb.entidade.Preposto;

@SuppressWarnings("serial")
@Stateless
public class PrepostoDAO extends BaseDAO {

	public Preposto recuperaPrepostoAtivoPorCpf(String cpf) {
		try {
			Query query = getEntityManager().createQuery(
					"SELECT preposto FROM Preposto preposto WHERE preposto.cpfPreposto = :cpf and preposto.statusPreposto='2'");
			query.setParameter("cpf", cpf);
			return (Preposto) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public CNJ recuperaServentiaPreposto(String cpf) {
		try {
			Query query = getEntityManager().createQuery(
					"SELECT cnj FROM Preposto preposto INNER JOIN CNJ cnj ON preposto.cnjId= cnj.cnjId WHERE preposto.cpfPreposto = :cpf and preposto.statusPreposto='2' and cnj.cnjStatus='2'");
			query.setParameter("cpf", cpf);
			return (CNJ) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
