package ecivil.ejb.bo;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.entidade.HistoricoPreRegistro;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.UsuarioPortalInterno;

@Stateless
public class HistoricoPreRegistroBO {
	@EJB
	private DataDAO dataDAO;
	@EJB
	private PreRegistroDAO preRegistroDAO;
	
	public void setaHistoricoPreRegistro(PreRegistro preRegistro, UsuarioPortalInterno usuario) {
		preRegistro.getListaHistoricoPreRegistroInicializada().add(montaNovoObjetoHistoricoPreRegistro(preRegistro, usuario));
		preRegistroDAO.atualiza(preRegistro);
	}

	private HistoricoPreRegistro montaNovoObjetoHistoricoPreRegistro(PreRegistro preRegistro, UsuarioPortalInterno usuario) {
		Date dataAtual = dataDAO.retornaDataBanco();
		HistoricoPreRegistro historicoPreRegistro = new HistoricoPreRegistro();
		historicoPreRegistro.setDataHoraAlteracao(dataAtual);
		historicoPreRegistro.setCnsCartorio(preRegistro.getCnsCartorio());
		historicoPreRegistro.setPreRegistro(preRegistro);
		historicoPreRegistro.setUsuario(usuario.getId());
		historicoPreRegistro.setSituacao(preRegistro.getSituacaoSolicitacao());
		return historicoPreRegistro;
	}
}
