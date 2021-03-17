package ecivil.ejb.bo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import ecivil.ejb.dao.AliquotaISSQNDAO;
import ecivil.ejb.entidade.AliquotaISSQN;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.Util;

@Stateless
public class AliquotaISSQNBO {
	
	@EJB
	private AliquotaISSQNDAO aliquotaISSQNDAO;
	
	public AliquotaISSQN recuperaAliquotaISSQN(String codigoCorregedoria) {
		if (!Util.ehStringValida(codigoCorregedoria)) {
			return null;
		}
		return aliquotaISSQNDAO.recuperarAliquotaISSQN(codigoCorregedoria);
	}
	
	public List<AliquotaISSQN> recuperaListaAliquotaISSQN(String codigoCorregedoria){
		if (!Util.ehStringValida(codigoCorregedoria)) {
			return null;
		}
		return aliquotaISSQNDAO.recuperarListaAliquotaISSQN(codigoCorregedoria);
	}

	public BigDecimal recuperaPorcentagemAliquotaISSQN(String codigoCorregedoria) {
		AliquotaISSQN aliquotaISSQN = recuperaAliquotaISSQN(codigoCorregedoria);
		return aliquotaISSQN != null ? aliquotaISSQN.getAliquota() : new BigDecimal(0);
	}
	
	public BigDecimal calculaValorAliquota(BigDecimal aliquotaIssqn, BigDecimal valor, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao) {
		return aliquotaISSQNDAO.calculaValorAliquota(aliquotaIssqn, valor, taxaRecompe, taxaFiscalizacao);
	}
	
	public void setarDataFimALiquotaAtualECriaNova(AliquotaISSQN aliquotaISSQNAtual, String novaAliquota) throws ECivilException {
		validaNovaAliquota(novaAliquota);
		inativaAliquotaAtual(aliquotaISSQNAtual);
		criaNovaAliquota(aliquotaISSQNAtual, novaAliquota);
	}

	private void validaNovaAliquota(String novaAliquota) throws ECivilException{
		ECivilException eCivilException = new ECivilException();
		eCivilException.instanciaListaErros();
		
		validaPreenchimentoAliquota(novaAliquota, eCivilException);
		validaValorAliquota(novaAliquota, eCivilException);
		
		if (eCivilException.possuiListaErros()) {
			throw eCivilException;
		}
	}
	
	private void validaValorAliquota(String novaAliquota, ECivilException eCivilException) throws ECivilException{
		if(!Objects.isNull(novaAliquota)) {
			float aliquotaDecimal = new BigDecimal(novaAliquota).floatValue();
			if(aliquotaDecimal < 0 && aliquotaDecimal > 5.0) {
				eCivilException.adicionaErroNaLista("O valor da alíquota deve estar entre 0 e 5.");
			}
		}
	}

	private void validaPreenchimentoAliquota(String novaAliquota, ECivilException eCivilException) throws ECivilException{
		if(Objects.isNull(novaAliquota)) {
			eCivilException.adicionaErroNaLista("Deve-se informar um valor para a alíquota.");
		}
	}

	private void criaNovaAliquota(AliquotaISSQN aliquotaISSQNAtual, String novaAliquota) {
		AliquotaISSQN aliquotaISSQN = new AliquotaISSQN();
		aliquotaISSQN.setAliquota(new BigDecimal(novaAliquota));
		aliquotaISSQN.setDataInicio(aliquotaISSQNDAO.retornaDataBanco());
		aliquotaISSQN.setCodigoCorregedoria(aliquotaISSQNAtual.getCodigoCorregedoria());
		aliquotaISSQN.setCodigoCNS(aliquotaISSQNAtual.getCodigoCNS());
		aliquotaISSQN.setDataFim(null);
		aliquotaISSQNDAO.persiste(aliquotaISSQN);
	}

	public void inativaAliquotaAtual(AliquotaISSQN aliquotaISSQNAtual) {
		aliquotaISSQNAtual.setDataFim(aliquotaISSQNDAO.retornaDataBanco());
		aliquotaISSQNDAO.atualiza(aliquotaISSQNAtual);
	}
	
}
