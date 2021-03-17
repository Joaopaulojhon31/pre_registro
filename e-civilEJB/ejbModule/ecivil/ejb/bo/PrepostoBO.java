package ecivil.ejb.bo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.PrepostoDAO;
import ecivil.ejb.entidade.CNJ;
import ecivil.ejb.entidade.Preposto;
@Stateless
public class PrepostoBO {
	
	@EJB
	private PrepostoDAO prepostoDAO;
	
	
	public Preposto recuperaPrepostoAtivoPorCpf(String cpf) {
		return prepostoDAO.recuperaPrepostoAtivoPorCpf(cpf);
	}
	
	public CNJ recuperaServentiaPreposto(String cpf) {
		return prepostoDAO.recuperaServentiaPreposto(cpf);
	}

}
