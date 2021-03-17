package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.Query;

@SuppressWarnings("serial")
@Stateless
@Named(value = EntidadeConverterDAO.NOME)
public class EntidadeConverterDAO extends BaseDAO {

	public static final String NOME = "entidadeConverterDao";

	@SuppressWarnings("unchecked")
	public <T> T obterEntidade(String seccional, String classe, Long id) {
		Query query = getEntityManager().createQuery(" SELECT entidade FROM " + classe + " entidade WHERE entidade.id = :id ");
		query.setParameter("id", id);
		return (T) query.getSingleResult();
	}

}
