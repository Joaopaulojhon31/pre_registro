package ecivil.adm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;

@SuppressWarnings("serial")
public class LoginECivilAdmServlet extends HttpServlet {

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		super.service(req, res);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		String pass = req.getParameter("senha");
		pass = CriptografiaUtil.retornaCriptografadoMD5(pass);
	
		if (StringUtils.isNotEmpty(login)) {
			login = Util.removeMask(login);
		}
		
		try {
			req.login(Util.removeMask(login), pass);
			resp.sendRedirect("pages/principal.jsf");
		} catch (Exception e) {
			setaMensagemErro(req, "Login e/ou senha inválidos. Verifique novamente");
			e.printStackTrace();
			resp.sendRedirect("pages/usuario/login.jsf");
		}
	}

	private void setaMensagemErro(HttpServletRequest req, String mensagem) {
		setaMensagem(req, mensagem, "red");
	}

	private void setaMensagem(HttpServletRequest req, String mensagem, String color) {
		req.getSession().setAttribute("mensagemLogin", mensagem);
		req.getSession().setAttribute("corMensagem", color);
	}

}
