package ecivil.ejb.dao;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.persistence.Query;

@SuppressWarnings("serial")
@Stateless
public class ValoresPedidoCertidaoDAO extends BaseDAO {

	public BigDecimal calculaValorCertidao(BigDecimal valorCertidao, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao, BigDecimal aliquotaIssqn) {
		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_valor_certidao(:valorCertidao, :taxaRecompe, :taxaFiscalizacao, :aliquotaIssqn) ");
		query.setParameter("valorCertidao", valorCertidao);
		query.setParameter("taxaRecompe", taxaRecompe);
		query.setParameter("taxaFiscalizacao", taxaFiscalizacao);
		query.setParameter("aliquotaIssqn", aliquotaIssqn);
		return (BigDecimal) query.getSingleResult();
	}
	
	public BigDecimal calculaValorAverbacao(BigDecimal valorAverbacao, BigDecimal taxaRecompeAverbacao, BigDecimal taxaFiscalizacaoAverbacao, BigDecimal aliquotaIssqn) {
		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_valor_averbacao(:valorAverbacao, :taxaRecompeAverbacao, :taxaFiscalizacaoAverbacao, :aliquotaIssqn) ");
		query.setParameter("valorAverbacao", valorAverbacao);
		query.setParameter("taxaRecompeAverbacao", taxaRecompeAverbacao);
		query.setParameter("taxaFiscalizacaoAverbacao", taxaFiscalizacaoAverbacao);
		query.setParameter("aliquotaIssqn", aliquotaIssqn);
		return (BigDecimal) query.getSingleResult();
	}

}
