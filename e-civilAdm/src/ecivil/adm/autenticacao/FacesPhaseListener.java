package ecivil.adm.autenticacao;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import ecivil.adm.controller.usuario.LoginController;
import ecivil.adm.controller.usuario.UsuarioController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.Util;
import web.util.JSFUtil;

@SuppressWarnings("serial")
public class FacesPhaseListener implements PhaseListener {

	private final static Logger LOGGER = Logger.getLogger(FacesPhaseListener.class.getName());
	
	@Inject
	private UsuarioController usuarioController;
	
	@Inject
	private LoginController loginController;

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
				UsuarioPortalInterno us = usuarioController.getUsuarioLogado();
				if (us != null) {
					String viewId = context.getViewRoot().getViewId();
					verificaAcessoAoRecurso(context, us, viewId);
					redirecionaTelaLogin(context, viewId);
				} else {
					if (ehLoginViaWebRecivil()) {
						realizaLoginViaWebRecivil(context);
					} else {
						despacha(EnumPaginas.LOGIN);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				redireciona(context, EnumPaginas.PRINCIPAL.getUrl());
			}
		}
	}

	private boolean ehLoginViaWebRecivil() {
		String loginViaWeb = JSFUtil.getExternalContext().getRequestParameterMap().get("webRecivil");
		return Util.ehStringValida(loginViaWeb) && loginViaWeb.equals("OK") ? Boolean.TRUE : Boolean.FALSE;
	}

	private void realizaLoginViaWebRecivil(FacesContext context) {
		try {
			redireciona(context, loginController.entrarViaWebRecivil());
		} catch (ECivilException e) {
			LOGGER.info("#### Erro ao fazer login via WebRecivil: " + e.getMensagemErro());
			despacha(EnumPaginas.LOGIN);
		} catch (Exception e) {
			e.printStackTrace();
			despacha(EnumPaginas.LOGIN);
		}
	}

	private void redirecionaTelaLogin(FacesContext context, String viewId) {
		if (viewId.equals("/pages/usuario/login.xhtml")) {
			redireciona(context, EnumPaginas.PRINCIPAL.getUrl().replaceAll("xhtml", "jsf"));
		}
	}
	
	private void verificaAcessoAoRecurso(FacesContext context, UsuarioPortalInterno us, String viewId) throws Exception {
		if (!temAcessoRecurso(us, viewId)) {
			despacha(EnumPaginas.PRINCIPAL);
			Mensagem.error("msg.validacao.acesso");
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

	private boolean temAcessoRecurso(UsuarioPortalInterno us, String viewId) throws Exception {
		if (us.isAdmin() || usuarioController.possuiAcessoRecurso(retornaEnumPorUrl(viewId).toString())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	private EnumPaginas retornaEnumPorUrl(String urlPagina) {
		EnumPaginas enumPagina = null;
		for (EnumPaginas ep : EnumPaginas.values()) {
			if (ep.getUrl().equals(urlPagina)) {
				enumPagina = ep;
			}
		}
		return enumPagina;
	}

}