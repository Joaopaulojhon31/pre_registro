package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.CertidaoNegativa;

@SuppressWarnings("serial")
@Stateless
public class CertidaoNegativaDAO extends BaseDAO {

	public CertidaoNegativa recuperaCertidaoNegativaPorHash(String codigoHash) {
		try {
			String sql = "SELECT negativa FROM CertidaoNegativa negativa WHERE negativa.codigoHash = :codigoHash ";
			Query query = getEntityManager().createQuery(sql);
			query.setParameter("codigoHash", codigoHash.trim());
			CertidaoNegativa certidaoNegativa = (CertidaoNegativa) query.getSingleResult();
			inicializaCertidaoNegativa(certidaoNegativa);
			return certidaoNegativa;
		} catch (NoResultException e) {
			return null;
		}
	}

	private void inicializaCertidaoNegativa(CertidaoNegativa certidaoNegativa) {
		Hibernate.initialize(certidaoNegativa.getMunicipio());
		Hibernate.initialize(certidaoNegativa.getTipoCertidao());
	}

}
