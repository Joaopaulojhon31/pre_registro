package ecivil.web.controller.usuario;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.bo.UsuarioPortalExternoBO;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.web.controller.BaseController;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class UsuarioController extends BaseController implements Serializable {

	@EJB
	private UsuarioPortalExternoBO usuarioPortalExternoBO;

	private UsuarioPortalExterno usuarioLogado;
	
	@PostConstruct
	public void inicializa() {
		inicializaUsuarioLogado();
	}

	public UsuarioPortalExterno getUsuarioLogado() {
		if (usuarioLogado == null) {
			try {
				inicializaUsuarioLogado();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return usuarioLogado;
	}

	public void inicializaUsuarioLogado() {
		if (usuarioLogado == null && StringUtils.isNotEmpty(JSFUtil.getRequest().getRemoteUser())) {
			setUsuarioLogado(usuarioPortalExternoBO.recuperarUsuarioPorCpf(JSFUtil.getRequest().getRemoteUser()));
			JSFUtil.getSession().setAttribute("usuarioLogado", getUsuarioLogado());
		}
	}
	
	public void atualizaUsuarioLogado() {
		setUsuarioLogado(usuarioPortalExternoBO.recuperarUsuarioPorCpf(JSFUtil.getRequest().getRemoteUser()));
		JSFUtil.getSession().setAttribute("usuarioLogado", getUsuarioLogado());
	}
	
	public void setUsuarioLogado(UsuarioPortalExterno usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

}
