package ecivil.adm.controller.contadigital;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.ContaAlternativaBO;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.request.contadigital.FiltroExtratoRequest;
import ecivil.ejb.ws.cliente.rest.response.contadigital.ExtratoResponse;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.Paginacao;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class ExtratoController extends BaseController implements Serializable {

	@EJB
	private CartorioLookUp cartorioLookUp;

	@EJB
	private ContaAlternativaBO contaAlternativaBO;

	private ExtratoResponse extrato;
	private FiltroExtratoRequest filtroExtratoRequest;
	private Paginacao paginacao;
	private Date dataInicio;
	private Date dataFim;
	private String protocolo;
	private String codigoCorregedoria;
	private boolean erroConsultaExtrato;
	private boolean consultaExtratoRealizada;
	private List<CartorioResponse> listaCartorio;

	@PostConstruct
	public void inicializa() {
		try {
			super.validaServentiaSelecionada();
			setPaginacao(new Paginacao());
			inicializaFiltro();
			inicializaListaCartorios();
			recuperaExtrato();
		} catch (Exception e) {
			setErroConsultaExtrato(Boolean.TRUE);
			e.printStackTrace();
		}
	}

	private void inicializaFiltro() {
		setFiltroExtratoRequest(new FiltroExtratoRequest());
		getFiltroExtratoRequest().setCodigoCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
	}

	private void inicializaListaCartorios() {
		try {
			if (getUsuarioLogadoPortal().isAdmin()) {
				setListaCartorio(cartorioLookUp.recuperaListaCartorio());
			
				if (Util.ehStringValida(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado())) {
					setCodigoCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recuperaExtrato() {
		setExtrato(null);
		getPaginacao().limpaPaginacao();
		setaDadosFiltroExtrato();
		recuperaExtratoPaginado();
	}

	public void recuperaExtratoPaginado() {
		try {
			if (!Util.ehStringValida(getFiltroExtratoRequest().getCodigoCorregedoria())) {
				return;
			}
			
			setaPaginacaoFiltroExtrato();
			setExtrato(contaDigitalWS.consultarExtrato(getFiltroExtratoRequest()));
			getPaginacao().setTotalRegistros(getExtrato().getTotalRegistros());
			setConsultaExtratoRealizada(Boolean.TRUE);
		} catch (ECivilException e) {
			setErroConsultaExtrato(Boolean.TRUE);
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			setErroConsultaExtrato(Boolean.TRUE);
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar o extrato da conta digital nesse momento. Tente novamente mais tarde.");
		}
	}

	private void setaPaginacaoFiltroExtrato() {
		getFiltroExtratoRequest().setPrimeiroRegistro(getPaginacao().getPrimeiroRegistro());
		getFiltroExtratoRequest().setRegistrosPorPagina(getPaginacao().getRegistrosPorPagina());
	}
	
	private void setaDadosFiltroExtrato() {
		if (getUsuarioLogadoPortal().isAdmin() && Util.ehStringValida(getCodigoCorregedoria())) {
			getFiltroExtratoRequest().setCodigoCorregedoria(getCodigoCorregedoria());
		} else {
			getFiltroExtratoRequest().setCodigoCorregedoria(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
		}
		
		getFiltroExtratoRequest().setDataInicio(DataUtil.converteDateParaString_DDMMYYY_BARRA(getDataInicio()));
		getFiltroExtratoRequest().setDataFim(DataUtil.converteDateParaString_DDMMYYY_BARRA(getDataFim()));
		getFiltroExtratoRequest().setProtocolo(getProtocolo());
	}
	
	public void limparFiltros() {
		inicializaFiltro();
		setDataInicio(null);
		setDataFim(null);
		setProtocolo(null);
		getExtrato().setTotalRegistros(0);
		getExtrato().setDetalheExtratoResponse(null);
		setConsultaExtratoRealizada(Boolean.FALSE);
	}
	
	public boolean exibeDadosExtrato() {
		return getExtrato() != null && getExtrato().getDetalheExtratoResponse() != null && !getExtrato().getDetalheExtratoResponse().isEmpty();
	}
	
	public boolean exibeFiltroCartorio() {
		return getUsuarioLogadoPortal().isAdmin();
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public ExtratoResponse getExtrato() {
		return extrato;
	}

	public void setExtrato(ExtratoResponse extrato) {
		this.extrato = extrato;
	}

	public FiltroExtratoRequest getFiltroExtratoRequest() {
		return filtroExtratoRequest;
	}

	public void setFiltroExtratoRequest(FiltroExtratoRequest filtroExtratoRequest) {
		this.filtroExtratoRequest = filtroExtratoRequest;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	
	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public boolean isErroConsultaExtrato() {
		return erroConsultaExtrato;
	}

	public void setErroConsultaExtrato(boolean erroConsultaExtrato) {
		this.erroConsultaExtrato = erroConsultaExtrato;
	}

	public boolean isConsultaExtratoRealizada() {
		return consultaExtratoRealizada;
	}

	public void setConsultaExtratoRealizada(boolean consultaExtratoRealizada) {
		this.consultaExtratoRealizada = consultaExtratoRealizada;
	}

	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}
	
}
