package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

@SuppressWarnings("serial")
@Stateless
public class NumeroProtocoloDAO extends BaseDAO {

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String getSequenceProtocolo() {
		Query query = getEntityManager().createNativeQuery(" SELECT nextval('ecivil.seq_protocolo') ");
		return query.getSingleResult().toString();
	}

}
