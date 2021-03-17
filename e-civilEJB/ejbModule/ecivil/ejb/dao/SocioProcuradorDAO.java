package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

@SuppressWarnings("serial")
@Stateless
public class SocioProcuradorDAO extends BaseDAO {

	public void apagarSocioProcuradorPorId(Long idSocioProcurador) {
		Query query = getEntityManager().createQuery(" DELETE SocioProcurador socio WHERE socio.id = :idSocioProcurador ");
		query.setParameter("idSocioProcurador", idSocioProcurador);
		query.executeUpdate();
	}

}