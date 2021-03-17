package ecivil.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.enumeration.EnumTipoSaidaCertidao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.Util;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class BuscarCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	private PedidoCertidao pedidoCertidao;
	
	@PostConstruct
	public void inicializa() throws Exception {
		inicializaCertidao();
	}

	private void inicializaCertidao() throws Exception {
		setPedidoCertidao(recuperarPedidoCertidaoNoFlash());
		if (getPedidoCertidao() == null) {
			setPedidoCertidao(new PedidoCertidao());
			getPedidoCertidao().setUsuarioExterno(getUsuarioLogadoPortal());
			setaTipoSaidaCertidao();
		}
	}

	private void setaTipoSaidaCertidao() {
		String codTipoSaida = JSFUtil.getRequest().getParameter("tipoSaidaCertidao");
		EnumTipoSaidaCertidao tipoSaida = EnumTipoSaidaCertidao.recuperaEnumTipoSaidaPorCodigo(codTipoSaida);
		if (tipoSaida == null) {
			redireciona(EnumPaginas.PRINCIPAL.getUrl());
		} else {
			getPedidoCertidao().setTipoSaida(tipoSaida.getCodigo());
		}
	}
	
	public String avancar() {
		try {
			if (!Util.ehStringValida(getPedidoCertidao().getNomePessoaPesquisa())) {
				Mensagem.errorSemBundle("Favor preecnher o nome da pessoa para poder prosseguir.");
				return "";
			}
			
//			if (getPedidoCertidao().getNomePessoaPesquisa().split(" ").length < 2) {
//				Mensagem.errorSemBundle("Favor preecnher pelo menos um sobrenome da pessoa para poder prosseguir.");
//				return "";
//			}
			
			pedidoCertidaoBO.buscarCertidaoIndexada(getPedidoCertidao(), getPedidoCertidao().getNomePessoaPesquisa(), getPedidoCertidao().getNomeAssociado(),
					getPedidoCertidao().getMunicipioPesquisa(), getPedidoCertidao().getCpf(),
					getPedidoCertidao().isNomeIdentico(), 0);
			super.setarPedidoCertidaolNoFlash(getPedidoCertidao());
			return EnumPaginas.RESULTADO_BUSCA_CERTIDAO.getUrl();
		} catch (ECivilException e) {
			if (e.possuiListaErros()) {
				Mensagem.listaErrosSemBundle(e.getListaErros());
			} else {
				Mensagem.errorSemBundle(e.getMensagemErro());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados da certidão nesse momento.");
		}
		
		return "";
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}

}
