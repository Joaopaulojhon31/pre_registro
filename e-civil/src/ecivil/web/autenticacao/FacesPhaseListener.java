package ecivil.web.autenticacao;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.web.controller.usuario.UsuarioController;
import ecivil.web.enumerator.EnumPaginas;
import web.util.JSFUtil;

@SuppressWarnings("serial")
public class FacesPhaseListener implements PhaseListener {

	@Inject
	private UsuarioController usuarioController;

	public void afterPhase(PhaseEvent event) {
		FacesContext context = event.getFacesContext();
		PhaseId phaseid = event.getPhaseId();
		if (context.getViewRoot() == null) {
			redireciona(context, EnumPaginas.PRINCIPAL.getUrl());
		} else {
			seguranca_acesso(context, phaseid);
		}
	}

	private void seguranca_acesso(FacesContext context, PhaseId phaseid) {
		if ((phaseid == PhaseId.RESTORE_VIEW || phaseid == PhaseId.INVOKE_APPLICATION) && JSFUtil.possuiSessao()) {
			try {
				UsuarioPortalExterno us = usuarioController.getUsuarioLogado();
				if (us != null) {
					String viewId = context.getViewRoot().getViewId();
					if (us.isPendenteAtualizacao() && !viewId.startsWith("/pages/usuario/cadastroUsuarioPortalExterno")) {
						redireciona(context, EnumPaginas.CADASTRO_USUARIO_PORTAL_EXTERNO.getUrlJsf());
					} else {
						redirecionaTelaLogin(context, viewId);
					}
				} else {
					String viewId = context.getViewRoot().getViewId().replace(".xhtml", ".jsf");
					if (!usuarioController.paginaSemRestricao(viewId)) {
						despacha(EnumPaginas.LOGIN);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				redireciona(context, EnumPaginas.PRINCIPAL.getUrl());
			}
		}
	}

	private void redirecionaTelaLogin(FacesContext context, String viewId) {
		if (viewId.equals("/pages/usuario/login.xhtml")) {
			redireciona(context, EnumPaginas.PRINCIPAL.getUrl().replaceAll("xhtml", "jsf"));
		}
	}

	private void redireciona(FacesContext context, String pagina) {
		try {
			context.getExternalContext().redirect(JSFUtil.getRequest().getContextPath() + pagina);
			context.responseComplete();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void despacha(EnumPaginas enderecoPagina) {
		try {
			JSFUtil.forward(enderecoPagina.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}