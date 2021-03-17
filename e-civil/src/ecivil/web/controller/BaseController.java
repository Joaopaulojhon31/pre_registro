package ecivil.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import ecivil.ejb.dao.UsuarioPortalExternoDAO;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.Util;
import ecivil.web.controller.usuario.UsuarioController;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class BaseController implements Serializable {

	@Inject
	UsuarioController usuarioController;
	
	@EJB
	private UsuarioPortalExternoDAO usuarioPortalExternoDAO;
	
	private boolean habilitarRedirecionar;

	public boolean isHabilitarRedirecionar() {
		return habilitarRedirecionar;
	}

	public void setHabilitarRedirecionar(boolean habilitarRedirecionar) {
		this.habilitarRedirecionar = habilitarRedirecionar;
	}

	public UsuarioPortalExterno getUsuarioLogadoPortal() {
		if (JSFUtil.getSession() != null) {
			return (UsuarioPortalExterno) JSFUtil.getSession().getAttribute("usuarioLogado");
		}
		return null;
	}

	
	public void fechaModalStatusDialog() {
		RequestContext.getCurrentInstance().execute("PF('statusDialog').hide();");
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

	public void exibirPDF(byte[] bytesRelatorio, String nomeComExtensao) throws Exception {
		try {
			exibeArquivoNoNavegador(bytesRelatorio, "application/pdf", nomeComExtensao);
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível exibir o documento nesse momento.");
		}
	}

	public void downloadPDF(byte[] bytesArquivo, String nomeComExtensao) throws Exception {
		downloadArquivo(bytesArquivo, "application/pdf", nomeComExtensao);
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
	
	public void redireciona(String pagina) {
		try {
			Flash flash = JSFUtil.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			JSFUtil.redireciona(JSFUtil.getRequest().getContextPath() + pagina);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setarPedidoCertidaolNoFlash(PedidoCertidao pedidoCertidao) throws Exception {
		JSFUtil.put("pedidoCertidao", pedidoCertidao);
	}

	public PedidoCertidao recuperarPedidoCertidaoNoFlash() throws Exception {
		return (PedidoCertidao) JSFUtil.get("pedidoCertidao");
	}

	public List<EnumPaginas> listBreadCrumb() {
		List<EnumPaginas> enums = new ArrayList<EnumPaginas>();
		enums.add(EnumPaginas.PRINCIPAL);
		return enums;
	}

	public boolean paginaSemRestricao(String view) {
		return EnumPaginas.LOGIN.getUrl().equals(view) || EnumPaginas.CADASTRO_USUARIO_PORTAL_EXTERNO.getUrl().equals(view);
	}
	
	public Boolean getUsuarioEstaLogado() {
		return getUsuarioLogadoPortal() != null;
	}

	public String logoff() {
		JSFUtil.getSession().invalidate();
		try {
			JSFUtil.redireciona(JSFUtil.getFacesContext().getExternalContext().getRequestContextPath() + "/pages/principal.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] gerarRelatorio(String relatorio, List<? extends Object> beanList, HashMap<String, Object> parametros) throws Exception {
		JRBeanCollectionDataSource dsrel = new JRBeanCollectionDataSource(beanList);
		String pathJasper = JSFUtil.getRealPath() + relatorio;
		JasperPrint impressao = JasperFillManager.fillReport(pathJasper, parametros, dsrel);
		return JasperExportManager.exportReportToPdf(impressao);
	}

	public String getDataFormatada(Date data) {
		return DataUtil.getdataFormatadaStrDDMMYYYY(data);
	}

	public void abrirModal(String idModal) {
		RequestContext.getCurrentInstance().execute("$('#" + idModal + "').modal('show');");
	}
	
	public void fecharModal(String idModal) {
		RequestContext.getCurrentInstance().execute("$('#" + idModal + "').modal('hide');");
	}
	
}
