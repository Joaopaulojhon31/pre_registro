package ecivil.adm.controller.usuario;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import ecivil.adm.controller.BaseController;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.dao.UsuarioPortalInternoDAO;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@SessionScoped
public class UsuarioController extends BaseController implements Serializable {
	
	@EJB
	private UsuarioPortalInternoDAO usuarioDAO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;

	private UsuarioPortalInterno usuarioLogado;

	@PostConstruct
	public void inicializa() {
		inicializaUsuarioLogado();
	}
	
	public UsuarioPortalInterno getUsuarioLogado() {
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
			setUsuarioLogado(usuarioDAO.recuperarUsuario(JSFUtil.getRequest().getRemoteUser()));
			JSFUtil.getSession().setAttribute("usuarioLogado", getUsuarioLogado());
		}
	}
	
	public void alterarServentia(String codigoCorregedoria) {
		try {
			getUsuarioLogado().setCodigoCorregedoriaSelecionado(codigoCorregedoria);
			recuperaSaldoAtual();
			JSFUtil.reload();
		} catch (Exception e) {
			Mensagem.errorSemBundle("Não foi possível alterar de serventia nesse momento. Tente novamente mais tarde.");
			e.printStackTrace();
		}
	}
	
	public String nomeServentiaSelecionada() {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getUsuarioLogado().getCodigoCorregedoriaSelecionado());
			return cartorio != null ? cartorio.getNomeDistritoECartorio() : "-----";
		} catch (ECivilException e) {
			e.printStackTrace();
			return "-----";
		}
	}
	
	public String nomeServentiaTooltip(String codigoCorregedoria) {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria);
			return cartorio != null ? cartorio.getNomeDistritoECartorio() : "-----";
		} catch (ECivilException e) {
			return "";
		}
	}
	
	public String recuperaNomeDistritoECartorio(String codigoCorregedoria) {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria);
			return cartorio != null ? cartorio.getDistrito() : recuperaCodigoCartorioFormatado(codigoCorregedoria);
		} catch (ECivilException e) {
			e.printStackTrace();
			return recuperaCodigoCartorioFormatado(codigoCorregedoria);
		}
	}
	
	public String recuperaCodigoCartorioFormatado(String codigoCorregedoria) {
		return Util.formataCodigCartorio(codigoCorregedoria);
	}

	public static String logoff() {
		JSFUtil.getSession().invalidate();
		try {
			JSFUtil.redireciona(JSFUtil.getFacesContext().getExternalContext().getRequestContextPath() + "/pages/usuario/login.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean exibeAlterarServentia() {
		return isPossuiUsuarioLogado() && !getUsuarioLogado().possuiApenasUmaServentia();
	}
	
	public boolean isPossuiUsuarioLogado() {
		return getUsuarioLogado() != null;
	}
	
	public boolean exibeVoltarParaWebRecivil() {
		return isPossuiUsuarioLogado() && getUsuarioLogado().isPermissaoOficial() || getUsuarioLogado().isPermissaoFuncionario();
	}
	
	public void setUsuarioLogado(UsuarioPortalInterno usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
	
}
