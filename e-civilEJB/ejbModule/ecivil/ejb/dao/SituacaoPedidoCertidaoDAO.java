package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.SituacaoPedidoCertidao;

@SuppressWarnings("serial")
@Stateless
public class SituacaoPedidoCertidaoDAO extends BaseDAO {

	public SituacaoPedidoCertidao recuperaSituacaoPedidoCertidaoPorCod(String codSituacao) {
		try {
			Query query = getEntityManager().createQuery("SELECT situacao FROM SituacaoPedidoCertidao situacao WHERE situacao.codigo = :codSituacao");
			query.setParameter("codSituacao", codSituacao);
			return (SituacaoPedidoCertidao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<SituacaoPedidoCertidao> recuperaListaSituacaoPedidoCertidao() {
		Query query = getEntityManager().createQuery("SELECT situacao FROM SituacaoPedidoCertidao situacao ORDER BY situacao.nome ASC ");
		return (List<SituacaoPedidoCertidao>) query.getResultList();
	}
}
