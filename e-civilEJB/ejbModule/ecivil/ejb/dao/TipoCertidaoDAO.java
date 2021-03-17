package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.TipoCertidao;

@SuppressWarnings("serial")
@Stateless
public class TipoCertidaoDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<TipoCertidao> listaTipoCertidaoNaoApenasNegativa() {
		Query query = getEntityManager().createQuery("SELECT tp FROM TipoCertidao tp WHERE exibeApenasNegativa = false ORDER BY tp.codigo");
		return (List<TipoCertidao>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoCertidao> listaTodosTipoCertidao() {
		Query query = getEntityManager().createQuery("SELECT tp FROM TipoCertidao tp ORDER BY tp.nome");
		return (List<TipoCertidao>) query.getResultList();
	}

	public TipoCertidao recuperaTipoCertidaoPorCodigo(String codigo) {
		try {
			Query query = getEntityManager().createQuery("SELECT tp FROM TipoCertidao tp WHERE tp.codigo = :codigo ");
			query.setParameter("codigo", codigo);
			return (TipoCertidao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
