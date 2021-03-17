package ecivil.ejb.bo;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.EnderecoUsuarioExternoDAO;
import ecivil.ejb.entidade.EnderecoUsuarioExterno;
import ecivil.ejb.entidade.UsuarioPortalExterno;

@Stateless
public class EnderecoUsuarioExternoBO {

	@EJB
	private EnderecoUsuarioExternoDAO enderecoUsuarioExternoDAO;
	
	@EJB
	private DataDAO dataDAO;

	public List<EnderecoUsuarioExterno> recuperaListaEnderecoUsuarioExterno(UsuarioPortalExterno usuarioPortalExterno) {
		return enderecoUsuarioExternoDAO.recuperaListaEnderecoUsuarioExterno(usuarioPortalExterno);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void excluirEndereco(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		enderecoUsuarioExternoDAO.setaDataFimEnderecoPorId(enderecoUsuarioExterno.getId());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void atualizarDadosEndereco(UsuarioPortalExterno usuarioPortalExterno) {
		if (usuarioPortalExterno.getEnderecoUsuarioExterno().getId() == null) {
			usuarioPortalExterno.getEnderecoUsuarioExterno().setUsuarioPortalExterno(usuarioPortalExterno);
			usuarioPortalExterno.getEnderecoUsuarioExterno().setDataInicio(dataDAO.retornaDataBanco());
			enderecoUsuarioExternoDAO.persiste(usuarioPortalExterno.getEnderecoUsuarioExterno());
		} else {
			enderecoUsuarioExternoDAO.atualiza(usuarioPortalExterno);
		}
	}

}