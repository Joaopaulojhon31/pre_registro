package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.entidade.GrupoUsuarioPermissao;

@SuppressWarnings("serial")
@Stateless
public class GrupoUsuarioDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<GrupoUsuario> recuperarGruposUsuarios(GrupoUsuario grupoUsuario) throws Exception {
		String consulta = recuperaConsultaPesquisaGrupoUsuario(grupoUsuario);
		Query query = getEntityManager().createQuery(consulta);
		setaParametrosPesquisaGrupoUsuario(grupoUsuario, query);
		List<GrupoUsuario> listaGrupoUsuario = (List<GrupoUsuario>) query.getResultList();
		inicialiarGrupoUsuario(listaGrupoUsuario);
		return listaGrupoUsuario;
	}

	private String recuperaConsultaPesquisaGrupoUsuario(GrupoUsuario grupoUsuario) throws Exception {
		String consulta = " SELECT grupoUsuario FROM GrupoUsuario grupoUsuario WHERE 1 = 1 AND (grupoUsuario.excluido IS NULL OR grupoUsuario.excluido <> :excluido) ";
		if (grupoUsuario.possuiDescricao()) {
			consulta += " AND UPPER(grupoUsuario.descricao) LIKE UPPER(:descricao) ";
		}
		consulta += " ORDER BY grupoUsuario.descricao ASC ";
		return consulta;
	}

	private void setaParametrosPesquisaGrupoUsuario(GrupoUsuario grupoUsuario, Query query) throws Exception {
		if (grupoUsuario.possuiDescricao()) {
			query.setParameter("descricao", "%" + grupoUsuario.getDescricao() + "%");
		}
		query.setParameter("excluido", Boolean.TRUE);
	}

	private void inicialiarGrupoUsuario(List<GrupoUsuario> listaGrupoUsuario) throws Exception {
		if (listaGrupoUsuario != null) {
			for (GrupoUsuario grupoUsuario : listaGrupoUsuario) {
				inicalizarGrupoUsuario(grupoUsuario);
			}
		}
	}

	public void inicalizarGrupoUsuario(GrupoUsuario grupoUsuario) throws Exception {
		incializarGrupoUsuarioPermissao(grupoUsuario);
	}

	public void incializarGrupoUsuarioPermissao(GrupoUsuario grupoUsuario) throws Exception {
		if (grupoUsuario.getGrupoUsuarioPermissoes() != null) {
			for (GrupoUsuarioPermissao grupoUsuarioPermissao : grupoUsuario.getGrupoUsuarioPermissoes()) {
				grupoUsuarioPermissao.getPermissao().getId();
				grupoUsuarioPermissao.getGrupoUsuario().getId();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<GrupoUsuario> recuperarGrupoUsuariosRetirandoLista(GrupoUsuario grupoUsuario, List<Long> listaIdGrupoUsuario) {
		String consulta = recuperaConsultaGrupoUsuariosRetirandoLista(grupoUsuario, listaIdGrupoUsuario);
		Query query = getEntityManager().createQuery(consulta);
		setaParametroRecuperarGrupoUsuariosRetirandoLista(grupoUsuario, listaIdGrupoUsuario, query);
		List<GrupoUsuario> listaGrupoUsuarios = (List<GrupoUsuario>) query.getResultList();
		return listaGrupoUsuarios;
	}

	private String recuperaConsultaGrupoUsuariosRetirandoLista(GrupoUsuario grupoUsuario, List<Long> listaIdGrupoUsuario) {
		String consulta = " SELECT DISTINCT grupoUsuario FROM GrupoUsuario grupoUsuario WHERE 1 = 1 AND (grupoUsuario.excluido IS NULL OR grupoUsuario.excluido <> :excluido) ";
		if (listaIdGrupoUsuario != null && listaIdGrupoUsuario.size() > 0) {
			consulta += " AND grupoUsuario.id NOT IN (:listaGrupoUsuario)";
		}
		if (grupoUsuario != null && grupoUsuario.possuiDescricao()) {
			consulta += " AND UPPER(grupoUsuario.descricao) LIKE UPPER(:descricao) ";
		}
		consulta += " ORDER BY grupoUsuario.descricao ASC";
		return consulta;
	}

	private void setaParametroRecuperarGrupoUsuariosRetirandoLista(GrupoUsuario grupoUsuario, List<Long> listaIdGrupoUsuario, Query query) {
		if (listaIdGrupoUsuario != null && listaIdGrupoUsuario.size() > 0) {
			query.setParameter("listaGrupoUsuario", listaIdGrupoUsuario);
		}

		if (grupoUsuario != null && grupoUsuario.possuiDescricao()) {
			query.setParameter("descricao", "%" + grupoUsuario.getDescricao() + "%");
		}
		query.setParameter("excluido", Boolean.TRUE);
	}

	public GrupoUsuario recuperaGrupoUsuarioPorCodigo(String nomeGrupoUsuario) {
		try {
			Query query = getEntityManager().createQuery("SELECT gp FROM GrupoUsuario gp WHERE gp.descricao = :nomeGrupoUsuario AND gp.excluido = false ");
			query.setParameter("nomeGrupoUsuario", nomeGrupoUsuario);
			return (GrupoUsuario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
