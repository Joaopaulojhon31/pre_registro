package ecivil.ejb.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.Permissao;
import ecivil.ejb.entidade.Recurso;

@SuppressWarnings("serial")
@Stateless
public class PermissaoDAO extends BaseDAO implements Serializable {

	@SuppressWarnings("unchecked")
	public List<Permissao> recuperarPermissoes(Permissao permissao) throws Exception {
		Query query = getEntityManager().createQuery(queryRecuperarConsultaPesquisaPermissao(permissao));
		setaParametrosPesquisaPermissao(permissao, query);
		List<Permissao> listaPermissao = (List<Permissao>) query.getResultList();
		inicializarListaRecursos(listaPermissao);
		return listaPermissao;
	}
	
	private String queryRecuperarConsultaPesquisaPermissao(Permissao permissao) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT Permissao ");
		sb.append(" FROM Permissao permissao ");
		sb.append(" WHERE (permissao.excluido IS NULL OR permissao.excluido <> :excluido) ");
		if (permissao.possuiDescricao()) {
			sb.append(" AND UPPER(permissao.descricao) LIKE UPPER(:descricao) ");
		}
		sb.append(" ORDER BY permissao.descricao ASC ");
		return sb.toString();
	}
	
	private void setaParametrosPesquisaPermissao(Permissao permissao, Query query) throws Exception {
		if (permissao.possuiDescricao()) {
			query.setParameter("descricao", "%" + permissao.getDescricao() + "%");
		}
		query.setParameter("excluido", Boolean.TRUE);
	}

	private void inicializarListaRecursos(List<Permissao> listaPermissoes) {
		if (listaPermissoes == null) {
			return;
		}
		
		for (Permissao permissao : listaPermissoes) {
			inicializarListaRecursos(permissao);
		}
	}

	private void inicializarListaRecursos(Permissao permissao) {
		if (permissao.getListaRecursos() == null) {
			return;
		}

		for (Recurso recurso : permissao.getListaRecursos()) {
			Hibernate.initialize(recurso);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Permissao> recuperarPermissoesRetirandoLista(List<Long> listaPermissoes) throws Exception {
		Query query = getEntityManager().createQuery(queryRecuperarPermissoesRetirandoLista(listaPermissoes));
		setaParametrosRecuperarPermissoesRetirandoLista(query, listaPermissoes);

		query.setParameter("excluido", Boolean.TRUE);
		List<Permissao> listaPermissao = (List<Permissao>) query.getResultList();
		return listaPermissao;
	}

	private String queryRecuperarPermissoesRetirandoLista(List<Long> listaPermissoes) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT Permissao ");
		sb.append(" FROM Permissao permissao ");
		sb.append(" WHERE (permissao.excluido IS NULL OR permissao.excluido <> :excluido) ");
		if (listaPermissoes != null && listaPermissoes.size() > 0) {
			sb.append(" AND permissao.id NOT IN (:listaPermissoes) ");
		}
		sb.append(" ORDER BY permissao.descricao ASC ");
		return sb.toString();
	}

	private void setaParametrosRecuperarPermissoesRetirandoLista(Query query, List<Long> listaPermissoes) {
		if (listaPermissoes != null && listaPermissoes.size() > 0) {
			query.setParameter("listaPermissoes", listaPermissoes);
		}		
	}

}
