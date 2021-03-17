package ecivil.adm.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.GrupoUsuario;
import ecivil.ejb.entidade.Recurso;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.ContaDigitalWS;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class BaseController implements Serializable {

	@EJB
	private transient UsuarioPortalInternoDAO usuarioPortalInternoDAO;
	
	@EJB
	protected ContaDigitalWS contaDigitalWS;

	public void validaServentiaSelecionada() throws Exception {
		if (getUsuarioLogadoPortal() != null && ehUsuarioOficialOuFuncionario() && !Util.ehStringValida(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado())) {
			redireciona(EnumPaginas.SELECIONAR_SERVENTIA.getUrlJsf());
		}
	}
	
	private boolean ehUsuarioOficialOuFuncionario() {
		return getUsuarioLogadoPortal().possuiGrupoPorDescricao(GrupoUsuario.OFICIAL) || getUsuarioLogadoPortal().possuiGrupoPorDescricao(GrupoUsuario.FUNCIONARIO);
	}

	public UsuarioPortalInterno getUsuarioLogadoPortal() {
		if (JSFUtil.getSession() != null) {
			return (UsuarioPortalInterno) JSFUtil.getSession().getAttribute("usuarioLogado");
		}
		return null;
	}
	
	public void recuperaSaldoAtual() {
		try {
			if (!getUsuarioLogadoPortal().possuiServentiaSelecionado()) {
				return;
			}
			getUsuarioLogadoPortal().setSaldoAtual(contaDigitalWS.consultaSaldo(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado()));
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível consultar o seu saldo disponível nesse momento. Tente novamente mais tarde.");
		}
	}
	
	public String montaNomeArquivo(String nomeInicial, String protocoloIdentificador) {
		if (nomeInicial.endsWith(".pdf")) {
			nomeInicial = nomeInicial.replace(".pdf", "");
		}
		
		if (Util.ehStringValida(protocoloIdentificador)) {
			if (!nomeInicial.endsWith("_")) {
				return nomeInicial.concat("_").concat(protocoloIdentificador).concat(".pdf");
			}
			return nomeInicial.concat(protocoloIdentificador).concat(".pdf");
		}
		return nomeInicial.concat(".pdf");
	}
	
	public void exibirPDF(byte[] bytesRelatorio, String nomeComExtensao) {
		try {
			exibeArquivoNoNavegador(bytesRelatorio, "application/pdf", nomeComExtensao);
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível exibir o documento nesse momento.");
		}
	}
	
	public void downloadXML(byte[] bytesArquivo, String nomeComExtensao) throws Exception {
		downloadArquivo(bytesArquivo, "application/xml", nomeComExtensao);
	}
	
	private void exibeArquivoNoNavegador(byte[] bytesArquivo, String mimeType, String nomeArquivoComExtensao) throws Exception {
		if (bytesArquivo != null) {
			JSFUtil.getResponse().setContentType(mimeType);
			JSFUtil.getResponse().setContentLength(bytesArquivo.length);
			JSFUtil.getResponse().setHeader("Content-disposition", "inline;filename=" + nomeArquivoComExtensao);
			OutputStream outputStream = JSFUtil.getResponse().getOutputStream();
			outputStream.write(bytesArquivo, 0, bytesArquivo.length);
			outputStream.flush();

			if (outputStream != null) {
				outputStream.close();
			}

			bytesArquivo = null;
			FacesContext.getCurrentInstance().responseComplete();
		}
	}
	
	private void downloadArquivo(byte[] bytesArquivo, String mimeType, String nomeComExtensao) throws Exception {
		if (bytesArquivo != null) {
			JSFUtil.getResponse().setContentType(mimeType);
			JSFUtil.getResponse().setContentLength(bytesArquivo.length);
			JSFUtil.getResponse().addHeader("Content-disposition", "attachment; filename=" + nomeComExtensao);
			OutputStream outputStream = JSFUtil.getResponse().getOutputStream();
			outputStream.write(bytesArquivo, 0, bytesArquivo.length);
			outputStream.flush();

			if (outputStream != null) {
				outputStream.close();
			}

			bytesArquivo = null;
			FacesContext.getCurrentInstance().responseComplete();
		}
	}
	
	public boolean possuiPermissaoAdministrador() {
		return getUsuarioEstaLogado() && (isSuperUsuario(getUsuarioLogadoPortal()) || getUsuarioLogadoPortal().possuiGrupoPorDescricao(GrupoUsuario.ADMINISTRADOR));
	}
	
	public boolean possuiPermissaoPreposto() {
		return getUsuarioEstaLogado() && getUsuarioLogadoPortal().possuiGrupoPorDescricao(GrupoUsuario.PREPOSTO);
	}
	
	public boolean possuiPermissaoPreRegistro() {
		return getUsuarioEstaLogado() && getUsuarioLogadoPortal().possuiGrupoPorDescricao(GrupoUsuario.PREREGISTRO);
	}

	public boolean possuiAcessoRecurso(String paginaString) {
		EnumPaginas pagina = EnumPaginas.valueOf(paginaString);

		if (paginaSemRestricaoPermissao(pagina) || isSuperUsuario(getUsuarioLogadoPortal())) {
			return Boolean.TRUE;
		}

		if (getUsuarioLogadoPortal() != null) {
			if (getUsuarioLogadoPortal().getListaRecursos() != null) {
				for (Recurso recurso : getUsuarioLogadoPortal().getListaRecursos()) {
					if (recurso.getDescricaoTratada().equals(pagina.getUrlTratada())) {
						return Boolean.TRUE;
					}
				}
			}
		}
		
		return Boolean.FALSE;
	}

	private boolean paginaSemRestricaoPermissao(EnumPaginas pagina) {
		if (pagina.equals(EnumPaginas.ALTERAR_SENHA_USUARIO_PORTAL_INTERNO) || pagina.equals(EnumPaginas.LOGIN) || pagina.equals(EnumPaginas.PRINCIPAL) || pagina.equals(EnumPaginas.SELECIONAR_SERVENTIA)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private boolean isSuperUsuario(UsuarioPortalInterno usuarioPortalInterno) {
		return usuarioPortalInterno != null && Boolean.TRUE.equals(usuarioPortalInterno.isAdmin());
	}
	
	public List<EnumPaginas> listBreadCrumb() {
		List<EnumPaginas> enums = new ArrayList<EnumPaginas>();
		enums.add(EnumPaginas.PRINCIPAL);
		return enums;
	}

	public Boolean getUsuarioEstaLogado() {
		return getUsuarioLogadoPortal() != null;
	}

	public String recuperarDataAtual() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(DataUtil.agora());
	}

	public byte[] gerarRelatorio(String relatorio, List<? extends Object> beanList, HashMap<String, Object> parametros) throws Exception {
		JRBeanCollectionDataSource dsrel = new JRBeanCollectionDataSource(beanList);
		String pathJasper = JSFUtil.getRealPath() + relatorio;
		JasperPrint impressao = JasperFillManager.fillReport(pathJasper, parametros, dsrel);
		return JasperExportManager.exportReportToPdf(impressao);
	}
	
	public void setarObjetoNoFlash(Object object, String nomeParametro) {
		JSFUtil.put(nomeParametro, object);
	}

	public void redireciona(String pagina) {
		try {
			Flash flash = JSFUtil.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			JSFUtil.redireciona(JSFUtil.getRequest().getContextPath() + pagina);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void fechaModalStatusDialog() {
		RequestContext.getCurrentInstance().execute("PF('statusDialog').hide();");
	}
	
	public void abrirModal(String idModal) {
		RequestContext.getCurrentInstance().execute("$('#" + idModal + "').modal('show');");
	}
	
}
