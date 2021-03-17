package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ecivil.ejb.entidade.RelacaoBairroServentia;

@SuppressWarnings("serial")
@Stateless
public class RelacaoBairroServentiaDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<RelacaoBairroServentia> listaServentiasProximasResidenciaPais(String bairro) {
		Query query = getEntityManager().createQuery(queryListaServentiasProximasResidenciaPais());
		query.setParameter("bairro", bairro);
		return query.getResultList();
	}
	
	private String queryListaServentiasProximasResidenciaPais() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT bairroserventia ");
		sb.append(" FROM RelacaoBairroServentia bairroserventia ");
		sb.append(" WHERE bairroserventia.bairro = :bairro ");
		sb.append(" ORDER BY bairroserventia.serventia ");
		return sb.toString();
	}
}