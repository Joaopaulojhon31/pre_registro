package ecivil.ejb.bo;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.entidade.HistoricoPedidoCertidao;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.UsuarioPortalInterno;

@Stateless
public class HistoricoPedidoCertidaoBO {

	@EJB
	private DataDAO dataDAO;

	public void setaHistoricoPedidoCertidao(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuario) {
		HistoricoPedidoCertidao historicoPedidoCertidaoEmAberto = pedidoCertidao.recuperaHistoricoPedidoCertidaoEmAberto();
		
		if (historicoPedidoCertidaoEmAberto != null && historicoPedidoCertidaoEmAberto.getSituacao().equals(pedidoCertidao.getSituacao())) {
			return;
		}
		
		Date dataAtual = dataDAO.retornaDataBanco();
		encerraHistoricoPedidoCertidaoEmAberto(historicoPedidoCertidaoEmAberto, dataAtual);
		pedidoCertidao.getListaHistoricoPedidoCertidaoInicializada().add(montaNovoObjetoHistoricoPedidoCertidao(pedidoCertidao, usuario, dataAtual));
	}

	private void encerraHistoricoPedidoCertidaoEmAberto(HistoricoPedidoCertidao historicoPedidoCertidaoEmAberto, Date dataAtual) {
		if (historicoPedidoCertidaoEmAberto != null) {
			historicoPedidoCertidaoEmAberto.setDataFim(dataAtual);
		}
	}

	private HistoricoPedidoCertidao montaNovoObjetoHistoricoPedidoCertidao(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuario, Date dataAtual) {
		HistoricoPedidoCertidao historicoPedidoCertidao = new HistoricoPedidoCertidao();
		historicoPedidoCertidao.setDataInicio(dataAtual);
		historicoPedidoCertidao.setPedidoCertidao(pedidoCertidao);
		historicoPedidoCertidao.setUsuario(usuario);
		historicoPedidoCertidao.setSituacao(pedidoCertidao.getSituacao());
		return historicoPedidoCertidao;
	}

}