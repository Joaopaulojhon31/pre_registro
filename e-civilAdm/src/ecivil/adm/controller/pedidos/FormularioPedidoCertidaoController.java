package ecivil.adm.controller.pedidos;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.EnderecoUsuarioExternoBO;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.bo.TipoCertidaoBO;
import ecivil.ejb.entidade.EnderecoUsuarioExterno;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.TipoCertidao;
import ecivil.ejb.enumeration.EnumSimNao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.MunicipioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.MigracaoRecompeWS;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class FormularioPedidoCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	@EJB
	private TipoCertidaoBO tipoCertidaoBO;
	
	@EJB
	private EnderecoUsuarioExternoBO enderecoUsuarioExternoBO;
	
	@EJB
	private MigracaoRecompeWS migracaoRecompeWS;
	
	@EJB
	private MunicipioLookUp municipioLookUp;
	
	private PedidoCertidao pedidoCertidao;
	private List<TipoCertidao> listaTipoCertidao;
	private List<EnderecoUsuarioExterno> listaEnderecoUsuario;
	private List<CartorioResponse> listaCartorio;
	
	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		inicializaCertidao();
		if (getPedidoCertidao() != null) {
			inicializaTipoCertidao();
			inicializaListaEnderecoEntrega();
			recuperaListaCartorioPorMunicipio();
		}
	}

	private void inicializaCertidao() throws Exception {
		setPedidoCertidao((PedidoCertidao) JSFUtil.get("pedidoCertidao"));
		if (getPedidoCertidao() == null) {
			redireciona(EnumPaginas.PRINCIPAL.getUrlJsf());
		}
	}

	private void inicializaTipoCertidao() {
		setListaTipoCertidao(tipoCertidaoBO.listaTipoCertidaoNaoApenasNegativa());
	}
	
	private void inicializaListaEnderecoEntrega() {
		if (getPedidoCertidao().isSaidaViaCorreios()) {
			setListaEnderecoUsuario(enderecoUsuarioExternoBO.recuperaListaEnderecoUsuarioExterno(getPedidoCertidao().getUsuarioExterno()));
		}
	}
	
	public String solicitarAnalisePedido() {
		try {
			pedidoCertidaoBO.validaPedidoFormularioCertidao(getPedidoCertidao());
			pedidoCertidaoBO.salvarNovoPedidoCertidao(getPedidoCertidao(), null, EnumSimNao.S);
			super.setarObjetoNoFlash(getPedidoCertidao(), "pedidoCertidao");
			return EnumPaginas.SOLICITACAO_CERTIDAO_REALIZADA.getUrl();
		} catch (ECivilException e) {
			if (e.possuiListaErros()) {
				Mensagem.listaErrosSemBundle(e.getListaErros());
			} else {
				Mensagem.errorSemBundle(e.getMensagemErro());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível concluir a solicitação da certidão nesse momento.");
		}

		return "";
	}

	public List<Municipio> recuperaListaMunicipioEstadoSede() {
		return municipioLookUp.getListaMunicipiosEstadoSede();
	}

	public void recuperaListaCartorioPorMunicipio() {
		try {
			fechaModalStatusDialog();
			setListaCartorio(null);
			if (getPedidoCertidao().getMunicipio() == null || !Util.ehStringValida(getPedidoCertidao().getMunicipio().getCodigoRecompe())) {
				return;
			}
			setListaCartorio(migracaoRecompeWS.recuperaListaCartorioPorCodigoMunicipio(getPedidoCertidao().getMunicipio().getCodigoRecompe()));
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível recuperar os cartórios do município selecionado nesse momento.");
		}
	}
	
	public boolean desabilitaComboCartorio() {
		return getPedidoCertidao() != null && (getPedidoCertidao().getMunicipio() == null || !Util.ehStringValida(getPedidoCertidao().getMunicipio().getCodigoRecompe()) || getListaCartorio() == null || getListaCartorio().isEmpty());
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

	public List<TipoCertidao> getListaTipoCertidao() {
		return listaTipoCertidao;
	}

	public void setListaTipoCertidao(List<TipoCertidao> listaTipoCertidao) {
		this.listaTipoCertidao = listaTipoCertidao;
	}
	
	public List<EnderecoUsuarioExterno> getListaEnderecoUsuario() {
		return listaEnderecoUsuario;
	}

	public void setListaEnderecoUsuario(List<EnderecoUsuarioExterno> listaEnderecoUsuario) {
		this.listaEnderecoUsuario = listaEnderecoUsuario;
	}

	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}
	
}
