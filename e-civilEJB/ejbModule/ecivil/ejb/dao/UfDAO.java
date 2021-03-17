package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ecivil.ejb.entidade.Uf;

@SuppressWarnings("serial")
@Stateless
public class UfDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<Uf> recuperaListaUF() {
		Query query = getEntityManager().createQuery("SELECT uf FROM Uf uf order by uf.nomeCompleto");
		return query.getResultList();
	}

	private StringBuilder preparaQueryBuscaUF() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT uf ");
		sql.append("FROM Uf uf ");
		return sql;
	}

	public Uf recuperarUF(String siglaEstado) {
		try {
			StringBuilder sql = preparaQueryBuscaUF();
			sql.append("WHERE uf.sigla = :sigla");
			Query query = getEntityManager().createQuery(sql.toString());
			query.setParameter("sigla", siglaEstado);
			return (Uf) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}