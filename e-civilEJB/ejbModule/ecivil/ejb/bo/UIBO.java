package ecivil.ejb.bo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.UIDAO;
import ecivil.ejb.entidade.UI;
@Stateless
public class UIBO {
	
	@EJB
	private UIDAO uiDAO;
	
	
	public UI recuperaUnidadeInterligadaPrePosto(String cpf) {
		return uiDAO.recuperaUnidadeInterligadaPrePosto(cpf);
	}
	
	public String recuperaUfUnidadeInterligadaPrePosto(String cpf) {
		return uiDAO.recuperaUfUnidadeInterligadaPrePosto(cpf);
	}
	
	public String recuperaMunicipioUnidadeInterligadaPrePosto(String cpf) {
		return uiDAO.recuperaMunicipioUnidadeInterligadaPrePosto(cpf);
	}
	


}
