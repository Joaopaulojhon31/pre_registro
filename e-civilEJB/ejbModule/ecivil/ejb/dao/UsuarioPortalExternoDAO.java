package ecivil.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.EnderecoUsuarioExterno;
import ecivil.ejb.entidade.UsuarioPortalExterno;

@SuppressWarnings("serial")
@Stateless
public class UsuarioPortalExternoDAO extends BaseDAO {

	public UsuarioPortalExterno recuperarUsuarioPorCpf(String cpf) {
		try {
			String consulta = "SELECT usuarioPortal FROM UsuarioPortalExterno usuarioPortal WHERE usuarioPortal.cpf = :cpf";
			Query query = getEntityManager().createQuery(consulta);
			query.setParameter("cpf", cpf);
			UsuarioPortalExterno usuarioPortal = (UsuarioPortalExterno) query.getSingleResult();
			return usuarioPortal;
		} catch (NoResultException e) {
			return null;
		}
	}

	public UsuarioPortalExterno recuperarUsuarioPorId(Long id) {
		try {
			String consulta = "SELECT usuarioPortal FROM UsuarioPortalExterno usuarioPortal WHERE usuarioPortal.id = :id";
			Query query = getEntityManager().createQuery(consulta);
			query.setParameter("id", id);
			UsuarioPortalExterno usuarioPortal = (UsuarioPortalExterno) query.getSingleResult();
			inicializaGrafoUsuario(usuarioPortal);
			return usuarioPortal;
		} catch (NoResultException e) {
			return null;
		}
	}

	private void inicializaGrafoUsuario(UsuarioPortalExterno usuarioPortal) {
		for (EnderecoUsuarioExterno enderecoUsuarioExterno : usuarioPortal.getListaEnderecoInicializada()) {
			Hibernate.initialize(enderecoUsuarioExterno.getUf());
			Hibernate.initialize(enderecoUsuarioExterno.getMunicipio());
			Hibernate.initialize(enderecoUsuarioExterno.getMunicipio().getUf());
			Hibernate.initialize(enderecoUsuarioExterno.getTipoLogradouro());
		}
	}

}
