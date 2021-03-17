package ecivil.ejb.cron;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import ecivil.ejb.bo.AtualizaDadosLookupBO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.enumeration.EnumSimNao;

@Stateless
public class AtualizaDadosLookupCron {

	private final static Logger LOGGER = Logger.getLogger(AtualizaDadosLookupCron.class.getName());

	@EJB
	private ParametroDAO parametroDAO;

	@EJB
	private AtualizaDadosLookupBO atualizaDadosLookupBO;

	@Schedule(hour = "*/1", persistent = false)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public synchronized void recarregaDadosCartorios() {
		try {
			if (parametroDAO.buscaValorParametro(Parametro.RODAR_CRON_ATUALIZA_LOOKUP_CARTORIOS).equalsIgnoreCase(EnumSimNao.N.name())) {
				return;
			}

			LOGGER.info("--------------------- INICIO ROTINA 'recarregaDadosCartorios()' ---------------------");
			atualizaDadosLookupBO.recarregaDadosCartorios();
			LOGGER.info("--------------------- FIM ROTINA 'recarregaDadosCartorios()' ---------------------");
		} catch (Exception e) {
			LOGGER.info("##### Erro na rotina recarregaDadosCartorios() ");
		}
	}

}
