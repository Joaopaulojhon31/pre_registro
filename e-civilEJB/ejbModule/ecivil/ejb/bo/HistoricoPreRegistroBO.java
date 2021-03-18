package ecivil.ejb.bo;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.entidade.HistoricoPreRegistro;
import ecivil.ejb.entidade.PreRegistro;

@Stateless
public class HistoricoPreRegistroBO {
	@EJB
	private DataDAO dataDAO;
	@EJB
	private PreRegistroDAO preRegistroDAO;
	
	public void setaHistoricoPreRegistro(PreRegistro preRegistro, Long idUsuario) {
		preRegistro.getListaHistoricoPreRegistroInicializada().add(montaNovoObjetoHistoricoPreRegistro(preRegistro,idUsuario));
		preRegistroDAO.atualiza(preRegistro);
	}

	private HistoricoPreRegistro montaNovoObjetoHistoricoPreRegistro(PreRegistro preRegistro, Long idUsuario) {
		Date dataAtual = dataDAO.retornaDataBanco();
		HistoricoPreRegistro historicoPreRegistro = new HistoricoPreRegistro();
		historicoPreRegistro.setDataHoraAlteracao(dataAtual);
		historicoPreRegistro.setCnsCartorio(preRegistro.getCnsCartorio());
		historicoPreRegistro.setPreRegistro(preRegistro);
		historicoPreRegistro.setUsuario(idUsuario);
		historicoPreRegistro.setSituacao(preRegistro.getSituacaoSolicitacao());
		return historicoPreRegistro;
	}
	
}
