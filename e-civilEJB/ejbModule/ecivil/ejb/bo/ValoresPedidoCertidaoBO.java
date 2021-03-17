package ecivil.ejb.bo;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.ValoresPedidoCertidaoDAO;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.ValoresPedidoCertidao;
import ecivil.ejb.ws.cliente.rest.request.recompe.AtoResponseVo;

@Stateless
public class ValoresPedidoCertidaoBO {
	
	@EJB
	private AliquotaISSQNBO aliquotaISSQNBO;
	
	@EJB
	private ValoresPedidoCertidaoDAO valoresPedidoCertidaoDAO;
	
	public void setaValoresPedidoCertidao(PedidoCertidao pedidoCertidao, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao) {
		if (pedidoCertidao.isPedidoDeCartorioParaCartorio()) {
			setaValoresPedidoCertidaoRequisitante(pedidoCertidao, atoResponseVo, atoResponseVoAverbacao);
		}
		setaValoresPedidoCertidaoRequisitado(pedidoCertidao, atoResponseVo, atoResponseVoAverbacao);
	}

	private void setaValoresPedidoCertidaoRequisitante(PedidoCertidao pedidoCertidao, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao) {
		ValoresPedidoCertidao valoresPedidoCertidao = new ValoresPedidoCertidao();
		valoresPedidoCertidao.setPedidoCertidao(pedidoCertidao);
		valoresPedidoCertidao.setCodCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitante());
		
		setaValoresCertidao(valoresPedidoCertidao, atoResponseVo, pedidoCertidao.getAliquotaISSQNRequisitante());
		valoresPedidoCertidao.setValorCertidao(valoresPedidoCertidaoDAO.calculaValorCertidao(atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(), atoResponseVo.getTfj(), pedidoCertidao.getAliquotaISSQNRequisitante()));
		
		if (pedidoCertidao.isPossuiAverbacao()) {
			setaValoresAverbacao(valoresPedidoCertidao, atoResponseVoAverbacao, pedidoCertidao.getAliquotaISSQNRequisitante());
			valoresPedidoCertidao.setValorAverbacao(valoresPedidoCertidaoDAO.calculaValorAverbacao(atoResponseVoAverbacao.getValorFinal(), atoResponseVoAverbacao.getRecompe(), atoResponseVoAverbacao.getTfj(), pedidoCertidao.getAliquotaISSQNRequisitante()));
		}
		
		valoresPedidoCertidao.setAliquota(pedidoCertidao.getAliquotaISSQNRequisitante());
		valoresPedidoCertidao.setValorTotal(somaValorTotalCertidao(valoresPedidoCertidao));
		pedidoCertidao.getListaValoresPedidoCertidaoInicializada().add(valoresPedidoCertidao);
	}

	private void setaValoresPedidoCertidaoRequisitado(PedidoCertidao pedidoCertidao, AtoResponseVo atoResponseVo, AtoResponseVo atoResponseVoAverbacao) {
		ValoresPedidoCertidao valoresPedidoCertidao = new ValoresPedidoCertidao();
		valoresPedidoCertidao.setPedidoCertidao(pedidoCertidao);
		valoresPedidoCertidao.setCodCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitada());
		
		setaValoresCertidao(valoresPedidoCertidao, atoResponseVo, pedidoCertidao.getAliquotaISSQNRequisitado());
		valoresPedidoCertidao.setValorCertidao(valoresPedidoCertidaoDAO.calculaValorCertidao(atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(), atoResponseVo.getTfj(), pedidoCertidao.getAliquotaISSQNRequisitado()));

		if (pedidoCertidao.isPossuiAverbacao()) {
			setaValoresAverbacao(valoresPedidoCertidao, atoResponseVoAverbacao, pedidoCertidao.getAliquotaISSQNRequisitado());
			valoresPedidoCertidao.setValorAverbacao(valoresPedidoCertidaoDAO.calculaValorAverbacao(atoResponseVoAverbacao.getValorFinal(), atoResponseVoAverbacao.getRecompe(), atoResponseVoAverbacao.getTfj(), pedidoCertidao.getAliquotaISSQNRequisitado()));
		}
		
		valoresPedidoCertidao.setAliquota(pedidoCertidao.getAliquotaISSQNRequisitado());
		valoresPedidoCertidao.setValorTotal(somaValorTotalCertidao(valoresPedidoCertidao));
		pedidoCertidao.getListaValoresPedidoCertidaoInicializada().add(valoresPedidoCertidao);
	}
	
	private void setaValoresCertidao(ValoresPedidoCertidao valoresPedidoCertidao, AtoResponseVo atoResponseVo, BigDecimal aliquotaIssqn) {
		valoresPedidoCertidao.setValorEmolumentosCertidao(atoResponseVo.getEmolumento());
		valoresPedidoCertidao.setValorRecompeCertidao(atoResponseVo.getRecompe());
		valoresPedidoCertidao.setValorTaxaFiscalizacaoCertidao(atoResponseVo.getTfj());
		valoresPedidoCertidao.setValorIssqnCertidao(aliquotaISSQNBO.calculaValorAliquota(aliquotaIssqn, atoResponseVo.getValorFinal(), atoResponseVo.getRecompe(), atoResponseVo.getTfj()));
	}
	
	private void setaValoresAverbacao(ValoresPedidoCertidao valoresPedidoCertidao, AtoResponseVo atoResponseVoAverbacao, BigDecimal aliquotaIssqnAverbacao) {
		valoresPedidoCertidao.setValorEmolumentosAverbacao(atoResponseVoAverbacao.getEmolumento());
		valoresPedidoCertidao.setValorRecompeAverbacao(atoResponseVoAverbacao.getRecompe());
		valoresPedidoCertidao.setValorTaxaFiscalizacaoAverbacao(atoResponseVoAverbacao.getTfj());
		valoresPedidoCertidao.setValorIssqnAverbacao(aliquotaISSQNBO.calculaValorAliquota(aliquotaIssqnAverbacao, atoResponseVoAverbacao.getValorFinal(), atoResponseVoAverbacao.getRecompe(), atoResponseVoAverbacao.getTfj()));
	}
	
	private BigDecimal somaValorTotalCertidao(ValoresPedidoCertidao valoresPedidoCertidao) {
		if (valoresPedidoCertidao.getValorAverbacao() != null) {
			return valoresPedidoCertidao.getValorCertidao().add(valoresPedidoCertidao.getValorAverbacao());
		}
		return valoresPedidoCertidao.getValorCertidao();
	}

}