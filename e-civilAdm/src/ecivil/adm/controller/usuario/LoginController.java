package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.RecuperacaoSenhaBO;
import ecivil.ejb.bo.UsuarioPortalInternoBO;
import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class LoginController extends BaseController implements Serializable {

	private static final int RANGE_MINUTOS = 3;

	@EJB
	private RecuperacaoSenhaBO recuperacaoSenhaBO;
	
	@EJB
	private UsuarioPortalInternoBO usuarioPortalInternoBO;
	
	@EJB
	private DataDAO dataDAO;
	
	private String cpfRecuperacao;

	private String login;
	private String pass;

	private String cpf;
	private String senha;

	@Override
	public List<EnumPaginas> listBreadCrumb() {
		return new ArrayList<EnumPaginas>();
	}
	
	public String entrar() {
		pass = CriptografiaUtil.retornaCriptografadoMD5(pass);
		
		if (StringUtils.isNotEmpty(login)) {
			login = Util.removeMask(login);
		}

		try {
			JSFUtil.getRequest().login(login, pass);
			UsuarioPortalInterno usuarioPortalInterno = usuarioPortalInternoBO.recuperaUsuarioPorCPF(login);
			if(usuarioPortalInterno.getStatus() == 0) {
				throw new ECivilException();
			}
			return EnumPaginas.SELECIONAR_SERVENTIA.getUrl();
		} catch(ECivilException e) {
			JSFUtil.getRequest().getSession().invalidate();
			Mensagem.errorSemBundle("Usuário informado encontra-se inativo");
		}catch (Exception ex) {
			Mensagem.errorSemBundle("Login e/ou senha inválidos. Verifique novamente");
		}
		return null;
	}
	
	public String entrarViaWebRecivil() throws Exception {
		String login = JSFUtil.getExternalContext().getRequestParameterMap().get("CPF");
		String token = JSFUtil.getExternalContext().getRequestParameterMap().get("token");
		
		if (!Util.ehStringValida(login) || !Util.ehStringValida(token)) {
			throw new ECivilException("Parametros de login não informados corretamente.");
		}
		
		if (!ehTokenWebRecivilValido(login, token)) {
			throw new ECivilException("O token informado é inválido.");
		}
		
		login = Util.removeMask(login);
		UsuarioPortalInterno usuarioPortalInterno = usuarioPortalInternoBO.autenticarUsuarioWebRecivil(login);
		JSFUtil.getRequest().login(login, usuarioPortalInterno.getSenha());
		return EnumPaginas.SELECIONAR_SERVENTIA.getUrlJsf();
	}

	private boolean ehTokenWebRecivilValido(String loginCpf, String token) {
		Date dataAtual = dataDAO.retornaDataBanco();
		String secretMd5HoraMinutoAtual = CriptografiaUtil.retornaCriptografadoMD5(recuperaAcessoSecret(loginCpf, dataAtual));
		if (token.equals(secretMd5HoraMinutoAtual) || ehTokenMinutoFuturoValido(token, dataAtual, loginCpf) || ehTokenMinutoPassadoValido(token, dataAtual, loginCpf)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private boolean ehTokenMinutoFuturoValido(String token, Date dataAtual, String loginCpf) {
		for (int i = 1; i <= RANGE_MINUTOS; i++) {
			String md5 = CriptografiaUtil.retornaCriptografadoMD5(recuperaAcessoSecret(loginCpf, dataMinutoProximo(dataAtual, i)));
			if (token.equals(md5)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	private boolean ehTokenMinutoPassadoValido(String token, Date dataAtual, String loginCpf) {
		for (int i = 1; i <= RANGE_MINUTOS; i++) {
			String md5 = CriptografiaUtil.retornaCriptografadoMD5(recuperaAcessoSecret(loginCpf, dataMinutoAnterior(dataAtual, i)));
			if (token.equals(md5)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private String recuperaAcessoSecret(String login, Date date) {
		String dataAtualStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
		dataAtualStr = dataAtualStr.replace(" ", "");
		return login + dataAtualStr;
	}
	
	private Date dataMinutoProximo(Date dataAtual, int minutoAdd) {
		return DateUtils.addMinutes(dataAtual, minutoAdd);
	}
	
	private Date dataMinutoAnterior(Date dataAtual, int minutoSub) {
		LocalDateTime localDateTime = dataAtual.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.minusMinutes(minutoSub);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public void recuperarSenhaUsuarioPortalInterno() {
		try {
			if (StringUtils.isEmpty(login)) {
				Mensagem.errorSemBundle("Informe o seu login para recuperar sua senha ");
				return;
			}
			recuperacaoSenhaBO.gerarNovaSenhaPortalInterno(Util.removeMask(login));
			Mensagem.infoSemBundle("Senha alterada com sucesso! Acesse seu e-mail para mais informações.");
		} catch (ECivilException exception) {
			Mensagem.listaErros(exception.getListaErros());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível recuperar sua senha nesse momento. Tente novamente mais tarde.");
		}
	}

	public String getCpfRecuperacao() {
		return cpfRecuperacao == null ? "" : cpfRecuperacao;
	}

	public void setCpfRecuperacao(String cpfRecuperacao) {
		this.cpfRecuperacao = cpfRecuperacao;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCpf() {
		return cpf;
	}
	
}
