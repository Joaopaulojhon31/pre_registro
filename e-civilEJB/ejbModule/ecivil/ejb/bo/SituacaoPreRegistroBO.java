package ecivil.ejb.bo;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.SituacaoPreRegistroDAO;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;

@Stateless
public class SituacaoPreRegistroBO {

	@EJB
	private SituacaoPreRegistroDAO situacaoPreRegistroDAO;
	
	public List<SituacaoSolicitacaoUI> recuperaListaSituacaoSolicitacaoUI(Boolean permissaoPrepostoUsuario) {
		return situacaoPreRegistroDAO.recuperaListaSituacaoSolicitacaoUI(permissaoPrepostoUsuario);
	}
}
