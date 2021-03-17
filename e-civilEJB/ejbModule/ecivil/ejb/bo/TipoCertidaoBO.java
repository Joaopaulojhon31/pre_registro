
package ecivil.ejb.bo;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.TipoCertidaoDAO;
import ecivil.ejb.entidade.TipoCertidao;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.response.integradorcrc.DadosCertidaoResponse;

@Stateless
public class TipoCertidaoBO {

	@EJB
	private TipoCertidaoDAO tipoCertidaoDAO;

	public List<TipoCertidao> listaTipoCertidaoNaoApenasNegativa() {
		return tipoCertidaoDAO.listaTipoCertidaoNaoApenasNegativa();
	}
	
	public List<TipoCertidao> listaTodosTipoCertidao() {
		return tipoCertidaoDAO.listaTodosTipoCertidao();
	}
	
	public TipoCertidao recuperaTipoCertidaoPorTipoFatoCRC(DadosCertidaoResponse certidao) {
		if (!Util.ehStringValida(certidao.getTipoFato())) {
			return null;
		}
		
		if (certidao.isCertidaoNascimento()) {
			return tipoCertidaoDAO.recuperaTipoCertidaoPorCodigo(TipoCertidao.COD_CERTIDAO_NASCIMENTO);
		}
		
		if (certidao.isCertidaoCasamento()) {
			return tipoCertidaoDAO.recuperaTipoCertidaoPorCodigo(TipoCertidao.COD_CERTIDAO_CASAMENTO);
		}
		
		if (certidao.isCertidaoObito()) {
			return tipoCertidaoDAO.recuperaTipoCertidaoPorCodigo(TipoCertidao.COD_CERTIDAO_OBTO);
		}

		return null;
	}
}