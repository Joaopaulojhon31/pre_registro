package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.SolicitacaoCredito;
import web.util.Paginacao;

@SuppressWarnings("serial")
@Stateless
public class SolicitacaoCreditoDAO extends BaseDAO {

	public int recuperaTotalSolicitacaoCredito(String codigoCorregedoria) {
		Query query = getEntityManager().createQuery(queryRecuperaSolicitacaoCredito(Boolean.TRUE));
		query.setParameter("codigoCorregedoria", codigoCorregedoria);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<SolicitacaoCredito> recuperaSolicitacaoCredito(String codigoCorregedoria, Paginacao paginacao) {
		Query query = getEntityManager().createQuery(queryRecuperaSolicitacaoCredito(Boolean.FALSE));
		query.setParameter("codigoCorregedoria", codigoCorregedoria);
		query.setFirstResult(paginacao.getPrimeiroRegistro());
		query.setMaxResults(paginacao.getNumeroDeRegistrosExibidos());
		List<SolicitacaoCredito> listaSolicitacaoCredito = (List<SolicitacaoCredito>) query.getResultList();
		inicializaGrafoSolicitacaoCredito(listaSolicitacaoCredito);
		return listaSolicitacaoCredito;
	}
	
	private void inicializaGrafoSolicitacaoCredito(List<SolicitacaoCredito> listaSolicitacaoCredito) {
		if (listaSolicitacaoCredito == null) {
			return;
		}
		
		for (SolicitacaoCredito solicitacaoCredito : listaSolicitacaoCredito) {
			Hibernate.initialize(solicitacaoCredito.getListaVinculoBoleto());
		}
	}

	private String queryRecuperaSolicitacaoCredito(boolean ehCount) {
		StringBuffer sb = new StringBuffer();
		if (ehCount) {
			sb.append(" SELECT COUNT(credito) ");
		} else {
			sb.append(" SELECT credito ");
		}

		sb.append(" FROM SolicitacaoCredito credito ");
		sb.append(" WHERE credito.codigoCorregedoria = :codigoCorregedoria ");
		sb.append(" AND ((credito.migracao IS NULL OR credito.migracao = false) OR (credito.migracao = true AND credito.dataFim IS NOT NULL)) ");
		
		if (!ehCount) {
			sb.append(" ORDER BY credito.dataInicio DESC ");
		}
		return sb.toString();
	}
	
}
