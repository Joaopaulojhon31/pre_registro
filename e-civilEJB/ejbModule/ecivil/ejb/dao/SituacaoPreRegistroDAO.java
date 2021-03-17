package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import ecivil.ejb.entidade.SituacaoSolicitacaoUI;

@SuppressWarnings("serial")
@Stateless
public class SituacaoPreRegistroDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<SituacaoSolicitacaoUI> recuperaListaSituacaoSolicitacaoUI(Boolean permissaoPrepostoUsuario) {
		try {
			Query query;
			if (permissaoPrepostoUsuario) {
				query = getEntityManager().createQuery("SELECT situacao FROM SituacaoSolicitacaoUI situacao Where situacao.id <> 8 ORDER BY situacao.nome ASC ");
			}
			else {
				query = getEntityManager().createQuery("SELECT situacao FROM SituacaoSolicitacaoUI situacao ORDER BY situacao.nome ASC ");
			}
			
		return (List<SituacaoSolicitacaoUI>) query.getResultList();
		}catch (Exception e) {
			e.getStackTrace();
		}
		return null;
		
		}
}
