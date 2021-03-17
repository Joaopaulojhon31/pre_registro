package ecivil.adm.controller.pedidos;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.ejb.entidade.PedidoCertidao;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class SolicitacaoCertidaoRealizadaController extends BaseController implements Serializable {

	private PedidoCertidao pedidoCertidao;

	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		inicializaCertidao();
	}

	private void inicializaCertidao() throws Exception {
		setPedidoCertidao((PedidoCertidao) JSFUtil.get("pedidoCertidao"));
		if (getPedidoCertidao() == null) {
			redireciona(EnumPaginas.PRINCIPAL.getUrlJsf());
		}
	}
	
	public String voltar() throws Exception {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}

}
