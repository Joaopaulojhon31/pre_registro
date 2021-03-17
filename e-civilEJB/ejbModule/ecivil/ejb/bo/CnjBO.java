package ecivil.ejb.bo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.CnjDAO;
import ecivil.ejb.entidade.CNJ;
@Stateless
public class CnjBO {
	
	@EJB
	private CnjDAO cnjDAO;
	
	
	public CNJ recuperaTotalSolicitacaoCredito(Integer id) {
		return cnjDAO.recuperaServentiaPorId(id);
	}

}
