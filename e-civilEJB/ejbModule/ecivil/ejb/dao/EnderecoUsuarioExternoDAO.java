package ecivil.ejb.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.EnderecoUsuarioExterno;
import ecivil.ejb.entidade.UsuarioPortalExterno;

@SuppressWarnings("serial")
@Stateless
public class EnderecoUsuarioExternoDAO extends BaseDAO {

	@SuppressWarnings("unchecked")
	public List<EnderecoUsuarioExterno> recuperaListaEnderecoUsuarioExterno(UsuarioPortalExterno usuarioPortalExterno) {
		Query query = getEntityManager().createQuery(" SELECT endereco FROM EnderecoUsuarioExterno endereco WHERE endereco.usuarioPortalExterno = :usuarioPortalExterno AND endereco.dataFim IS NULL ORDER BY endereco.dataInicio ");
		query.setParameter("usuarioPortalExterno", usuarioPortalExterno);
		List<EnderecoUsuarioExterno> listaEnderecoExterno = (List<EnderecoUsuarioExterno>) query.getResultList();
		inicializaEnderecoUsuarioExterno(listaEnderecoExterno);
		return listaEnderecoExterno;
	}

	private void inicializaEnderecoUsuarioExterno(List<EnderecoUsuarioExterno> listaEnderecoExterno) {
		if (listaEnderecoExterno == null || listaEnderecoExterno.isEmpty()) {
			return;
		}
		
		for (EnderecoUsuarioExterno endereco : listaEnderecoExterno) {
			Hibernate.initialize(endereco.getTipoLogradouro());
			Hibernate.initialize(endereco.getMunicipio());
			if (endereco.getMunicipio() != null) {
				Hibernate.initialize(endereco.getMunicipio().getUf());
			}
		}
	}

	public void setaDataFimEnderecoPorId(Long idEndereco) {
		Query query = getEntityManager().createQuery(" UPDATE EnderecoUsuarioExterno endereco SET endereco.dataFim = CURRENT_TIMESTAMP WHERE endereco.id = :idEndereco ");
		query.setParameter("idEndereco", idEndereco);
		query.executeUpdate();
	}

}
