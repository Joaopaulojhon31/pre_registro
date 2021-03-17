package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.GrupoUsuarioPermissao;
import ecivil.ejb.entidade.Recurso;
import ecivil.ejb.entidade.UsuarioGrupoUsuario;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import web.util.Paginacao;

@SuppressWarnings("serial")
@Stateless
public class UsuarioPortalInternoDAO extends BaseDAO {

	public Integer quantidadePesquisarUaurios(UsuarioPortalInterno usuarioPortalInterno) throws Exception {
		String consulta = " SELECT COUNT(DISTINCT usuarioPortalInterno.id) ";
		consulta += recuperaConsultaPesquisaUsuarioGeral(usuarioPortalInterno);
		Query query = getEntityManager().createQuery(consulta);
		setaParametrosPesquisaUsuario(usuarioPortalInterno, query);
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<UsuarioPortalInterno> pesquisarUsuario(UsuarioPortalInterno usuario, Paginacao paginacao) throws Exception {
		String consulta = recuperaConsultaPesquisaUsuario(usuario);
		Query query = getEntityManager().createQuery(consulta);
		setaParametrosPesquisaUsuario(usuario, query);
		query.setFirstResult(paginacao.getPrimeiroRegistro());
		query.setMaxResults(paginacao.getNumeroDeRegistrosExibidos());
		List<UsuarioPortalInterno> listaUsuario = (List<UsuarioPortalInterno>) query.getResultList();
		inicializaListaUsuario(listaUsuario);
		return listaUsuario;
	}

	private void inicializaListaUsuario(List<UsuarioPortalInterno> listaUsuario) {
		inicializaListaUsuarioGrupoUsuario(listaUsuario);
		inicializarListaUsuarioServentia(listaUsuario);
	}

	public UsuarioPortalInterno recuperarUsuario(String cpf) {
		String consulta = " SELECT usuarioPortalInterno FROM UsuarioPortalInterno usuarioPortalInterno WHERE usuarioPortalInterno.cpf =:cpf";
		Query query = getEntityManager().createQuery(consulta);
		query.setParameter("cpf", cpf);
		try {
			UsuarioPortalInterno usuarioPortalInterno = (UsuarioPortalInterno) query.getSingleResult();
			inicializaUsuarioGrupoUsuario(usuarioPortalInterno);
			inicializaGrupoUsuarioPermissaoERecurso(usuarioPortalInterno);
			inicializaUsuarioServentia(usuarioPortalInterno);
			usuarioPortalInterno.montaListaRecursos();
			return usuarioPortalInterno;
		} catch (NoResultException e) {
			return null;
		}
	}

	private String recuperaConsultaPesquisaUsuario(UsuarioPortalInterno usuarioPortalInterno) throws Exception {
		String consulta = " SELECT usuarioPortalInterno ";
		consulta += recuperaConsultaPesquisaUsuarioGeral(usuarioPortalInterno);
		consulta += " ORDER BY usuarioPortalInterno.nome ASC";
		return consulta;
	}

	private String recuperaConsultaPesquisaUsuarioGeral(UsuarioPortalInterno usuarioPortalInterno) throws Exception {
		String consulta = " FROM UsuarioPortalInterno usuarioPortalInterno WHERE 1 = 1 ";
		if (usuarioPortalInterno.possuiCpf()) {
			consulta += " AND usuarioPortalInterno.cpf =:cpf ";
		}

		if (usuarioPortalInterno.possuiNome()) {
			consulta += " AND UPPER(usuarioPortalInterno.nome) LIKE UPPER(:nome) ";
		}

		if (usuarioPortalInterno.possuiEmail()) {
			consulta += " AND usuarioPortalInterno.email =:email ";
		}

		return consulta;
	}

	private void setaParametrosPesquisaUsuario(UsuarioPortalInterno usuario, Query query) throws Exception {
		if (usuario.possuiCpf()) {
			query.setParameter("cpf", usuario.getCpf());
		}
		if (usuario.possuiNome()) {
			query.setParameter("nome", "%" + usuario.getNome().toUpperCase() + "%");
		}
		if (usuario.possuiEmail()) {
			query.setParameter("email", usuario.getEmail());
		}
	}

	public void inicializaListaUsuarioGrupoUsuario(List<UsuarioPortalInterno> listaUsuario) {
		if (listaUsuario != null) {
			for (UsuarioPortalInterno usuario : listaUsuario) {
				inicializaUsuarioGrupoUsuario(usuario);
			}
		}
	}

	private void inicializarListaUsuarioServentia(List<UsuarioPortalInterno> listaUsuario) {
		if (listaUsuario != null) {
			for (UsuarioPortalInterno usuario : listaUsuario) {
				inicializaUsuarioServentia(usuario);
			}
		}		
	}
	
	private void inicializaGrupoUsuarioPermissaoERecurso(UsuarioPortalInterno usuarioPortalInterno) {
		for (UsuarioGrupoUsuario usuarioGrupoUsuario : usuarioPortalInterno.getListaUsuarioGrupoUsuario()) {
			for (GrupoUsuarioPermissao grupoUsuarioPermissao : usuarioGrupoUsuario.getGrupoUsuario().getGrupoUsuarioPermissoes()) {
				for (Recurso recurso : grupoUsuarioPermissao.getPermissao().getListaRecursos()) {
					recurso.getId();
				}
			}
		}
	}

	private void inicializaUsuarioGrupoUsuario(UsuarioPortalInterno usuario) {
		if (usuario.getListaUsuarioGrupoUsuario() != null) {
			for (UsuarioGrupoUsuario usuarioGrupoUsuario : usuario.getListaUsuarioGrupoUsuario()) {
				usuarioGrupoUsuario.getGrupoUsuario().getId();
			}
		}
	}
	
	private void inicializaUsuarioServentia(UsuarioPortalInterno usuarioPortalInterno) {
		Hibernate.initialize(usuarioPortalInterno.getUsuarioServentia());
	}

	public UsuarioPortalInterno recuperarUsuario(Long id) {
		String consulta = " SELECT usuarioPortalInterno FROM UsuarioPortalInterno usuarioPortalInterno WHERE usuarioPortalInterno.id =:id";
		Query query = getEntityManager().createQuery(consulta);
		query.setParameter("id", id);
		try {
			UsuarioPortalInterno usuarioPortalInterno = (UsuarioPortalInterno) query.getSingleResult();
			return usuarioPortalInterno;
		} catch (NoResultException e) {
			return null;
		}
	}
}
