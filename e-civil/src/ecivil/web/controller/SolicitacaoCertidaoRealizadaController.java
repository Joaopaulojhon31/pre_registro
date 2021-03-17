package ecivil.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.web.enumerator.EnumPaginas;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class SolicitacaoCertidaoRealizadaController extends BaseController implements Serializable {

	private PedidoCertidao pedidoCertidao;

	@PostConstruct
	public void inicializa() throws Exception {
		inicializaCertidao();
	}

	private void inicializaCertidao() throws Exception {
		setPedidoCertidao(recuperarPedidoCertidaoNoFlash());
		if (getPedidoCertidao() == null) {
			redireciona(EnumPaginas.PRINCIPAL.getUrl());
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
