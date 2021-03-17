package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ecivil.ejb.entidade.Parametro;

@SuppressWarnings("serial")
@Stateless
public class ParametroDAO extends BaseDAO {

	public String buscaValorParametro(String nomeParametro) {
		Parametro parametro = buscaParametro(nomeParametro);
		return parametro != null ? parametro.getValor() : null;
	}
	
	private Parametro buscaParametro(String nomeParametro) {
		try {
			Query query = getEntityManager().createQuery(queryBuscaParametro());
			query.setParameter("nome", nomeParametro);
			query.setParameter("habilitado", Boolean.TRUE);
			return (Parametro) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	private String queryBuscaParametro() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p ");
		sql.append(" FROM Parametro p ");
		sql.append(" WHERE p.nome = :nome ");
		sql.append(" AND p.habilitado = :habilitado ");
		return sql.toString();
	}

}
