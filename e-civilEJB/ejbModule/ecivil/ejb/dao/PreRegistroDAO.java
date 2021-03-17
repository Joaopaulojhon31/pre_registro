package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.bo.HistoricoPreRegistroBO;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.UsuarioPortalInterno;

@SuppressWarnings("serial")
@Stateless
public class PreRegistroDAO extends BaseDAO {

	
	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;
	
	
	
	public PreRegistro buscaIdPreRegistro() {
		try {
			Query query = getEntityManager()
					.createQuery("SELECT NEXTVAL('ecivil.seq_id_pre_registro')");
			return (PreRegistro) query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
}
	
	@SuppressWarnings("unchecked")
	public List<PreRegistro> buscaListaPreregistroCpfMaeOrdenadoPorDataAsc(String cpf) {
		if (!cpf.isEmpty()) {
			try {
				Query query = getEntityManager()
						.createQuery("SELECT solicitacao FROM PreRegistro solicitacao WHERE solicitacao.cpfMae = :cpf ORDER BY solicitacao.dataInicioSolicitacao asc");
				query.setParameter("cpf", cpf);
				return (List<PreRegistro>) query.getResultList();
			} catch (NoResultException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return carregaBuscaInicial();
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<PreRegistro> buscaListaPreregistroCpfMaeOrdenadoPorDataDesc(String cpf) {
		if (!cpf.isEmpty()) {
			try {
				Query query = getEntityManager()
						.createQuery("SELECT solicitacao FROM PreRegistro solicitacao WHERE solicitacao.cpfMae = :cpf ORDER BY solicitacao.dataInicioSolicitacao desc");
				query.setParameter("cpf", cpf);
				return (List<PreRegistro>) query.getResultList();
			} catch (NoResultException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return carregaBuscaInicial();
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<PreRegistro> buscaListaPreregistroPorIdUsuarioPortalExternoOuCpfMaeOrdenadoPorData(long id, String cpf) {
			try {
				Query query = getEntityManager()
						.createQuery("SELECT solicitacao FROM PreRegistro solicitacao WHERE  solicitacao.cpfMae = :cpf or solicitacao.idUsuarioPortalExterno = :id ORDER BY solicitacao.dataInicioSolicitacao desc");
				query.setParameter("id", id);
				query.setParameter("cpf", cpf);
				return (List<PreRegistro>) query.getResultList();
			} catch (NoResultException e) {
				e.printStackTrace();
				return null;
			}

	}
	
	public PreRegistro buscaPreregistroEmAbertoPorCpfMae(String cpf) {
			try {
				Query query = getEntityManager()
						.createQuery("SELECT solicitacao FROM PreRegistro solicitacao WHERE solicitacao.cpfMae = :cpf and solicitacao.situacaoSolicitacao='8'");
				query.setParameter("cpf", cpf);
				return (PreRegistro) query.getSingleResult();
			} catch (NoResultException e) {
				e.printStackTrace();
				return null;
			}
	}
	
	public PreRegistro buscaPreregistroEmAbertoPorCpfMaePrePosto(String cpf) {
		try {
			Query query = getEntityManager()
					.createQuery("SELECT solicitacao FROM PreRegistro solicitacao WHERE solicitacao.cpfMae = :cpf and (solicitacao.situacaoSolicitacao='1' or solicitacao.situacaoSolicitacao='8')");
			query.setParameter("cpf", cpf);
			return (PreRegistro) query.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
}

	@SuppressWarnings("unchecked")
	public List<PreRegistro> carregaBuscaInicial() {
		try {
			Query query = getEntityManager().createQuery(
					"SELECT solicitacao FROM PreRegistro solicitacao ORDER BY solicitacao.dataInicioSolicitacao DESC");
			return (List<PreRegistro>) query.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}

	}

	public void setaSituacaoSolicitacao(PreRegistro preRegistro, String codSituacaoPreRegistro, Long idUsuario) {
		preRegistro.setSituacaoSolicitacao(codSituacaoPreRegistro);
		historicoPreRegistroBO.gravaPreRegistroComHistorico(preRegistro, idUsuario);
	}
}
