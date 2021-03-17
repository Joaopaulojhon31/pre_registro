package ecivil.ejb.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import ecivil.ejb.entidade.AliquotaISSQN;

@SuppressWarnings("serial")
@Stateless
public class AliquotaISSQNDAO extends BaseDAO {

	public AliquotaISSQN recuperarAliquotaISSQN(String codigoCorregedoria) {
		try {
			Query query = getEntityManager().createQuery(queryRecuperarAliquotaISSQN());
			query.setParameter("codigoCorregedoria", codigoCorregedoria);
			return (AliquotaISSQN) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AliquotaISSQN> recuperarListaAliquotaISSQN(String codigoCorregedoria) {
		try {
			Query query = getEntityManager().createQuery(queryRecuperarListaAliquotaISSQN());
			query.setParameter("codigoCorregedoria", codigoCorregedoria);
			return (List<AliquotaISSQN>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	private String queryRecuperarAliquotaISSQN() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT aliquota ");
		sb.append(" FROM AliquotaISSQN aliquota");
		sb.append(" WHERE aliquota.dataFim IS NULL ");
		sb.append(" AND aliquota.codigoCorregedoria = :codigoCorregedoria ");
		return sb.toString();
	}
	
	private String queryRecuperarListaAliquotaISSQN() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT aliquota ");
		sb.append(" FROM AliquotaISSQN aliquota");
		sb.append(" WHERE aliquota.dataFim IS NOT NULL ");
		sb.append(" AND aliquota.codigoCorregedoria = :codigoCorregedoria ");
		sb.append(" ORDER BY dataFim DESC ");
		return sb.toString();
	}
	
	public BigDecimal calculaValorAliquota(BigDecimal aliquotaIssqn, BigDecimal valor, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao) {
		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_aliquota_issqn(:aliquotaIssqn, :valor, :taxaRecompe, :taxaFiscalizacao) ");
		query.setParameter("aliquotaIssqn", aliquotaIssqn);
		query.setParameter("valor", valor);
		query.setParameter("taxaRecompe", taxaRecompe);
		query.setParameter("taxaFiscalizacao", taxaFiscalizacao);
		return (BigDecimal) query.getSingleResult();
	}
}
