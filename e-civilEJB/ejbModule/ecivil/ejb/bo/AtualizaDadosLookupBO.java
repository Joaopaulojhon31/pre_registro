package ecivil.ejb.bo;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.lookup.CartorioLookUp;

@Stateless
public class AtualizaDadosLookupBO {
	
	private final static Logger LOGGER = Logger.getLogger(AtualizaDadosLookupBO.class.getName());
	
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	public void recarregaDadosCartorios() {
		try {
			cartorioLookUp.recarregarDadosCartorios();
		} catch (Exception e) {
			LOGGER.info("#### Erro ao recarregar o lookup dos dados dos Cartorios.");
			e.printStackTrace();
		}
	}
	
}
