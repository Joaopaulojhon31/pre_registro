package ecivil.ejb.bo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.ContaAlternativaDAO;
import ecivil.ejb.entidade.ContaAlternativa;

@Stateless
public class ContaAlternativaBO {

	@EJB
	private ContaAlternativaDAO contaAlternativaDAO;
	
	public ContaAlternativa recuperaContaAlternativaPorCodCorregedoria(String codigoCorregedoria) {
		return contaAlternativaDAO.recuperaContaAlternativa(codigoCorregedoria);
	}

}