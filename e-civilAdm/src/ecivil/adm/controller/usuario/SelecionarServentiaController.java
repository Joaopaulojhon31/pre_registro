package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.entidade.UsuarioServentia;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class SelecionarServentiaController extends BaseController implements Serializable {

	@EJB
	private CartorioLookUp cartorioLookUp;
	private List<CartorioResponse> listaServentia;
	private boolean exibeMensagemErro;
	
	@PostConstruct
	public void inicializa() throws Exception {
		if (getUsuarioLogadoPortal().possuiApenasUmaServentia()) {
			if (!getUsuarioLogadoPortal().getUsuarioServentiaAtiva().isEmpty()) {
				getUsuarioLogadoPortal().setCodigoCorregedoriaSelecionado(getUsuarioLogadoPortal().getUsuarioServentiaAtiva().get(0).getCodigoCorregedoria());
				recuperaSaldoAtual();
			}
			redireciona(EnumPaginas.PRINCIPAL.getUrlJsf());
		} else {
			inicializaListaServentia();
		}
	}

	private void inicializaListaServentia() {
		try {
			List<String> listaCodCorregedoria = getUsuarioLogadoPortal().getUsuarioServentiaAtiva().stream().map(UsuarioServentia::getCodigoCorregedoria).collect(Collectors.toList());
			setListaServentia(cartorioLookUp.recuperaListaCartorioPorCodigoCorregedoria(listaCodCorregedoria));
		} catch (Exception e) {
			e.printStackTrace();
			setExibeMensagemErro(Boolean.TRUE);
		}
	}

	public void selecionarServentia() {
		try {
			if (!Util.ehStringValida(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado())) {
				Mensagem.errorSemBundle("Favor selecionar a serventia para poder avançar.");
				return;
			}
			recuperaSaldoAtual();
			redireciona(EnumPaginas.PRINCIPAL.getUrlJsf());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível avançar com a serventia selecionada nesse momento. Tente novamente mais tarde.");
		}
	}

	public List<CartorioResponse> getListaServentia() {
		return listaServentia;
	}

	public void setListaServentia(List<CartorioResponse> listaServentia) {
		this.listaServentia = listaServentia;
	}

	public boolean isExibeMensagemErro() {
		return exibeMensagemErro;
	}

	public void setExibeMensagemErro(boolean exibeMensagemErro) {
		this.exibeMensagemErro = exibeMensagemErro;
	}
	
}
