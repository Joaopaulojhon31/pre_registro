package ecivil.ejb.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

@SuppressWarnings("serial")
public class BaseDAO implements Serializable {

	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public <T> T find(Class<T> classe, Object id) {
		return getEntityManager().find(classe, id);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> classe) {
		return getEntityManager().createQuery("SELECT E FROM " + classe.getSimpleName() + " E").getResultList();
	}

	public <T> void refresh(T entidade) {
		getEntityManager().refresh(entidade);
	}

	public <T> void persiste(T entidade) {
		getEntityManager().persist(entidade);
	}

	public <T> T atualiza(T entidade) {
		try {
			return getEntityManager().merge(entidade);
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	public <T> void remove(T entidade) {
		if (!getEntityManager().contains(entidade)) {
			entidade = getEntityManager().merge(entidade);
		}

		getEntityManager().remove(entidade);
	}
	
	public Date retornaDataBanco() {
		String sql = "SELECT NOW();";
		Query query = getEntityManager().createNativeQuery(sql);
		return (Date) query.getSingleResult();
	}
}
