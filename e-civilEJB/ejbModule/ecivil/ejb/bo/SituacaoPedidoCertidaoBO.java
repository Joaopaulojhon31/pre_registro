package ecivil.ejb.bo;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.SituacaoPedidoCertidaoDAO;
import ecivil.ejb.entidade.SituacaoPedidoCertidao;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;

@Stateless
public class SituacaoPedidoCertidaoBO {

	@EJB
	private SituacaoPedidoCertidaoDAO situacaoPedidoCertidaoDAO;
	
	public SituacaoPedidoCertidao recuperaSituacaoPedidoCertidaoPorCod(String codSituacaoPedidoCertidao) {
		return situacaoPedidoCertidaoDAO.recuperaSituacaoPedidoCertidaoPorCod(codSituacaoPedidoCertidao);
	}

	public List<SituacaoPedidoCertidao> recuperaListaSituacaoPedidoCertidao() {
		return situacaoPedidoCertidaoDAO.recuperaListaSituacaoPedidoCertidao();
	}
}
