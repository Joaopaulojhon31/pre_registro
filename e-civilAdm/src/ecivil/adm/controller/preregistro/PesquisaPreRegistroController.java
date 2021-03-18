package ecivil.adm.controller.preregistro;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.pdfbox.io.IOUtils;
import org.primefaces.event.FileUploadEvent;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.HistoricoPreRegistroBO;
import ecivil.ejb.bo.PreRegistroBO;
import ecivil.ejb.bo.SituacaoPreRegistroBO;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;
import ecivil.ejb.entidade.UsuarioServentia;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.exception.UploadException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.vo.FiltroPesquisaPreRegistroVO;
import ecivil.ejb.ws.cliente.rest.PreRegistroWS;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.Paginacao;
@SuppressWarnings("serial")
@ViewScoped
@Named
public class PesquisaPreRegistroController extends BaseController implements Serializable  {

	private SituacaoSolicitacaoUI situacaoSolicitacaoUI;
	private PreRegistro preRegistro;
	private List<PreRegistro> listaPreRegistro;
	private List<SituacaoSolicitacaoUI> listaSituacaoSolicitacaoUI;
	private FiltroPesquisaPreRegistroVO filtroPesquisaSolicitacaoUI;
	private Paginacao paginacao;
	private String cpfMae;
	private boolean pesquisaRealizada;
	private boolean permissaoPrepostoUsuario;
	private boolean desabilitaBotaoDownload;
	private List<CartorioResponse> listaCartorio;

	@EJB
    private PreRegistroWS preRegistroWS;
	@EJB
	private SituacaoPreRegistroBO situacaoPreRegistroBO;
	@EJB
	private PreRegistroBO preRegistroBO;
	@EJB
	private CartorioLookUp cartorioLookUp;
	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;

	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		setPaginacao(new Paginacao());
		if(getPreRegistro() ==null) {
			setPreRegistro(new PreRegistro());
		}
		listaPreRegistro = new ArrayList<PreRegistro>();
		setDesabilitaBotaoDownload(false);
		inicializaSituacaoSolicitacaoUI();
		inicializaListaCartorio();
		pesquisarSolicitacoesUI();
	}
	
	public void setaPreRegistro(PreRegistro preRegistro, String idModal) {
		try {
			setPreRegistro(preRegistroBO.recuperaPreRegistroPelaSolicitacao(preRegistro.getId()));
			setDesabilitaBotaoDownload(false);
			abrirModal(idModal);
			//cpfMae = preRegistro.getCpfMae();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados do pedido de certidão nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public boolean isPesquisaRealizada() {
		return pesquisaRealizada;
	}
	
	public void carregaParamentros() {
		try {
			
		}catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível carregar as solicitações de pré registro nesse momento.");
		}
	}
	
	private void inicializaListaCartorio() {
		try {
			if (getUsuarioLogadoPortal().isAdmin() || getUsuarioLogadoPortal().isPermissaoConsulta()) {
				setListaCartorio(cartorioLookUp.recuperaListaCartorio());
				return;
			}
			List<String> listaCodCorregedoria = getUsuarioLogadoPortal().getUsuarioServentiaAtiva().stream().map(UsuarioServentia::getCodigoCorregedoria).collect(Collectors.toList());
			setListaCartorio(cartorioLookUp.recuperaListaCartorioPorCodigoCorregedoria(listaCodCorregedoria));
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível recuperar a(s) serventias do usuário nesse momento. Tente novamente mais tarde."); 
			e.printStackTrace();
		}
	}
	
	public boolean desabilitaComboSelecionarServentia() {
		return !getUsuarioLogadoPortal().isAdmin() && !getUsuarioLogadoPortal().isPermissaoConsulta() && getUsuarioLogadoPortal().possuiApenasUmaServentia();
	}
	
	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public void pesquisarSolicitacoesUI() {
		try {
			setListaSituacaoSolicitacaoUI(new ArrayList<>());
			paginacao.limpaPaginacao();
			inicializaSituacaoSolicitacaoUI();
			preencherDadosFiltroPesquisa();
			paginacao.setTotalRegistros(preRegistroBO.pesquisarTotalPreRegistro(getFiltroPesquisaSolicitacaoUI()));
			pesquisarPreRegistroPaginado();
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Por favor, preencha algum campo para efetuar a pesquisa.");
		}
	}
	
	private FiltroPesquisaPreRegistroVO preencherDadosFiltroPesquisa() throws ParseException {
		FiltroPesquisaPreRegistroVO filtro = new FiltroPesquisaPreRegistroVO();
		filtro.setCpfMae(preRegistro.getCpfMae());
	    filtro.setDataInicioSolicitacao(preRegistro.getDataInicioSolicitacao());
		filtro.setDataFimSolicitacao(preRegistro.getDataFimSolicitacao());
		filtro.setSituacaoSolicitacao(preRegistro.getSituacaoSolicitacao());
		filtro.setCodCorregedoriaCartorio(preRegistro.getCodCorregedoriaCartorio());
		setFiltroPesquisaSolicitacaoUI(filtro);
		listaPreRegistro.clear();
		return filtro;
	}
	
	public void pesquisarPreRegistroPaginado() {
		if (paginacao.getTotalRegistros() > 0) {
			setListaPreRegistro(preRegistroBO.pesquisarPedidosRealizadoPeloCartorio(getUsuarioLogadoPortal(), getFiltroPesquisaSolicitacaoUI(), getPaginacao()));
		} else {
			setListaPreRegistro(new ArrayList<>());
		}
		pesquisaRealizada=true;
	}
	
	public void downloadRmlPreRegistro() {
		try {
			if (getPreRegistro() != null) {
			super.downloadXML(preRegistroWS.gerarXmlPreRegistro(preRegistro.getCnsCartorio() + ";" + preRegistro.getCpfMae()).getBytes(),"SOL" + preRegistro.getId() + ".RML");
				//super.downloadXML(preRegistroWS.gerarXMLPreRegistroViaCRC("07214427656", "036236").getBytes(),"SOL157496.xml");
			preRegistroBO.setaSituacaoPreRegistro(preRegistro, SituacaoSolicitacaoUI.REALIZADO, getUsuarioLogadoPortal().getId());
			} 
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível fazer download da certidão nesse momento.");
		}
	}
	
	public void downloadPreRegistro() {
		//File arquivoDocumentacoes= new File("DIretorio"+preRegistro.getId()+".rar");
		try {
			if (getPreRegistro() != null) {
				super.downloadXML(preRegistroBO.retornaXmlPreRegistro(preRegistro.getId()).getBytes(),"CER"+preRegistro.getId()+".xml");
				//arquivoDocumentacoes.delete();
				preRegistroBO.setaSituacaoPreRegistro(preRegistro, SituacaoSolicitacaoUI.FINALIZADO, getUsuarioLogadoPortal().getId());
			} 
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível fazer download da certidão nesse momento.");
		}
	}
	
	public void limpar() {
		listaPreRegistro.clear();
		filtroPesquisaSolicitacaoUI = new FiltroPesquisaPreRegistroVO();
		preRegistro = new PreRegistro();
				paginacao.limpaPaginacao();
		
	}
	
	public int retornaQuantidadeCriancas() {
		if(preRegistro.getQuantidadeCriancas() != null) {
		return Integer.parseInt(preRegistro.getQuantidadeCriancas());
		}
		return 0;
	}
	
	public String retornaNomeSolicitacao() {
		if (preRegistro != null && preRegistro.getSituacaoSolicitacao() != null) {
			for (int i = 0; i < listaSituacaoSolicitacaoUI.size(); i++) {
				if (listaSituacaoSolicitacaoUI.get(i).getId() == Long.parseLong(preRegistro.getSituacaoSolicitacao())) {
					return listaSituacaoSolicitacaoUI.get(i).getNome();
				}
			}
		}
		return "";
	}
	
	public void salvarXml () {
		try {
			if (this.preRegistro.getXml() == null) {
				Mensagem.errorSemBundle("Favor anexar o XML da certidão antes de finalizar o pedido.");
				return;
			}
			preRegistroBO.salvarXml(preRegistro.getXml());
			preRegistroBO.setaSituacaoPreRegistro(preRegistro, SituacaoSolicitacaoUI.REALIZADO, getUsuarioLogadoPortal().getId());
			Mensagem.infoSemBundle("XML da certidão anexado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível anexar o XML da certidão nesse momento.");
		}
	}
	
	public void anexarXml (FileUploadEvent event) {
		try {
			this.preRegistro.setXml(null);
			if (!event.getFile().getFileName().toUpperCase().endsWith(".XML")) {
				throw new UploadException("Só é permitido anexar arquivos com extenção .XML");
			}
			String content = new String(event.getFile().getContents());
			preRegistroBO.anexarXmlPreRegistro(preRegistro, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removerXmlAnexado() {
		preRegistro.setXml(null);
	}
	
	public boolean possuiPermissaoPreposto() {
		return super.possuiPermissaoPreposto();
	}
	
	public void downloadDocumentacao() throws IOException, Exception {
		try {
			super.downloadXML(recuperaDocumentacao(), "Documentacao.zip");
			Mensagem.infoSemBundle("Download da documentação efetuado com sucesso.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] recuperaDocumentacao() throws IOException {
		URL url = new URL("file:///C:/Users/eduardo/Desktop/Projeto%20WebRecivil/e-civil-Matheus-Alterando/e-civilAdm/Download/recivil.zip");
        InputStream is = url.openStream();
        byte[] bytes = IOUtils.toByteArray(is);
        return bytes;
	}
	
	public boolean exibeBotoesInteracao() {
		if(preRegistro.getSituacaoSolicitacao() != null) {
			if (possuiPermissaoPreRegistro()) {
				return (preRegistro.getSituacaoSolicitacao().equals(SituacaoSolicitacaoUI.EM_ANDAMENTO) || preRegistro.getSituacaoSolicitacao().equals(SituacaoSolicitacaoUI.REJEITADO_UI));
			}
			else if (possuiPermissaoPreposto()) {
				return (preRegistro.getSituacaoSolicitacao().equals(SituacaoSolicitacaoUI.EM_PREPARACAO) || preRegistro.getSituacaoSolicitacao().equals(SituacaoSolicitacaoUI.REALIZADO));
			}
		}
		return false;
	}
	
	
	public PreRegistro getPreRegistro() {
		return preRegistro;
	}

	public void setPreRegistro(PreRegistro preRegistro) {
		this.preRegistro = preRegistro;
	}

	public List<PreRegistro> getListaPreRegistro() {
		return listaPreRegistro;
	}

	public void setListaPreRegistro(List<PreRegistro> listPre) {
		this.listaPreRegistro = listPre;
	}

	public SituacaoSolicitacaoUI getSituacaoSolicitacaoUI() {
		return situacaoSolicitacaoUI;
	}
	public void setSituacaoSolicitacaoUI(SituacaoSolicitacaoUI situacaoSolicitacaoUI) {
		this.situacaoSolicitacaoUI = situacaoSolicitacaoUI;
	}
	
	private void inicializaSituacaoSolicitacaoUI() {
		permissaoPrepostoUsuario = possuiPermissaoPreposto();
		setListaSituacaoSolicitacaoUI(situacaoPreRegistroBO.recuperaListaSituacaoSolicitacaoUI(permissaoPrepostoUsuario));
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public FiltroPesquisaPreRegistroVO getFiltroPesquisaSolicitacaoUI() {
		return filtroPesquisaSolicitacaoUI;
	}

	public void setFiltroPesquisaSolicitacaoUI(FiltroPesquisaPreRegistroVO filtroPesquisaSolicitacaoUI) {
		this.filtroPesquisaSolicitacaoUI = filtroPesquisaSolicitacaoUI;
	}
	
	public List<SituacaoSolicitacaoUI> getListaSituacaoSolicitacaoUI() {
		return listaSituacaoSolicitacaoUI;
	}

	public void setListaSituacaoSolicitacaoUI(List<SituacaoSolicitacaoUI> listaSituacaoSolicitacaoUI) {
		this.listaSituacaoSolicitacaoUI = listaSituacaoSolicitacaoUI;
	}
	public boolean isDesabilitaBotaoDownload() {
		return desabilitaBotaoDownload;
	}

	public void setDesabilitaBotaoDownload(boolean desabilitaBotaoDownload) {
		this.desabilitaBotaoDownload = desabilitaBotaoDownload;
	}
	public List<CartorioResponse> getListaCartorio() {
		return listaCartorio;
	}

	public void setListaCartorio(List<CartorioResponse> listaCartorio) {
		this.listaCartorio = listaCartorio;
	}
}

