package ecivil.ejb.dao;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

@SuppressWarnings("serial")
@Stateless
public class DataDAO extends BaseDAO {

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Date retornaDataBanco() {
		String sql = "SELECT NOW();";
		Query query = getEntityManager().createNativeQuery(sql);
		return (Date) query.getSingleResult();
	}

}
