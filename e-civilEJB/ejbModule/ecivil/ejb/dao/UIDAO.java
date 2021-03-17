package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.CNJ;
import ecivil.ejb.entidade.Preposto;
import ecivil.ejb.entidade.UI;

@SuppressWarnings("serial")
@Stateless
public class UIDAO extends BaseDAO {



	public UI recuperaUnidadeInterligadaPrePosto(String cpf) {
		try {
			Query query = getEntityManager().createQuery(
					"SELECT ui FROM Preposto preposto INNER JOIN UI ui ON preposto.idUi= ui.idUI WHERE preposto.cpfPreposto = :cpf and preposto.statusPreposto='2'");
			query.setParameter("cpf", cpf);
			return (UI) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String recuperaUfUnidadeInterligadaPrePosto(String cpf) {
		try {
			Query query = getEntityManager().createQuery(
					"SELECT uf.sigla FROM UI ui INNER JOIN Municipio mun ON cast(ui.idMunicipioUI as text) = mun.codigoRecompe INNER JOIN Uf uf ON uf.id = mun.uf INNER JOIN Preposto preposto ON preposto.idUi = ui.idUI where preposto.cpfPreposto = :cpf and preposto.statusPreposto='2'");
			query.setParameter("cpf", cpf);
			return (String) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String recuperaMunicipioUnidadeInterligadaPrePosto(String cpf) {
		try {
			Query query = getEntityManager().createQuery(
					"SELECT mun.nome FROM UI ui INNER JOIN Municipio mun ON cast(ui.idMunicipioUI as text) = mun.codigoRecompe INNER JOIN Preposto preposto ON preposto.idUi = ui.idUI where preposto.cpfPreposto = :cpf and preposto.statusPreposto='2'");
			query.setParameter("cpf", cpf);
			return (String) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
