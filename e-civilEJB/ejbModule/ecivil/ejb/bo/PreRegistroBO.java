package ecivil.ejb.bo;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.PreRegistroDAO;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.PreRegistroXML;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.vo.FiltroPesquisaPreRegistroVO;
import web.util.Paginacao;

@Stateless
public class PreRegistroBO {
	@EJB
	private PreRegistroDAO preRegistroDAO;
	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;
	
	public PreRegistro recuperaPreRegistroPelaSolicitacao(Long id) {
		return preRegistroDAO.buscaPreregistroNumeroSolicitacao(id);
	}
	
	public List<PreRegistro> pesquisarPedidosRealizadoPeloCartorio(UsuarioPortalInterno usuarioLogadoPortal, FiltroPesquisaPreRegistroVO filtroPesquisaCertidao, Paginacao paginacao) {
		return preRegistroDAO.pesquisarPedidosRealizadoPeloCartorio(usuarioLogadoPortal.getCodigoCorregedoriaSelecionado(), filtroPesquisaCertidao, paginacao);
	}
	
	public int pesquisarTotalPreRegistro(FiltroPesquisaPreRegistroVO filtroPesquisaCertidao) {
		return preRegistroDAO.pesquisarTotalPreRegistro(filtroPesquisaCertidao);
	}
	
	public void anexarXmlPreRegistro(PreRegistro preRegistro, String xml) {
		preRegistroDAO.montaObjetoXml(preRegistro,xml);
	}
	
	public void salvarXml (PreRegistroXML preRegistro) {
		preRegistroDAO.salvarXmlPreRegistro(preRegistro);
	}
	
	public String retornaXmlPreRegistro(Long idPreRegistro) {
		return preRegistroDAO.retornaDadosXmlPreRegistro(idPreRegistro);
	}
	
	public void setaHistoricoPreRegistro(PreRegistro preRegistro, UsuarioPortalInterno usuario) {
		historicoPreRegistroBO.setaHistoricoPreRegistro(preRegistro, usuario);
	}
	public void setaSituacaoPreRegistro(PreRegistro preRegistro, String codSituacaoPreRegistro, UsuarioPortalInterno usuarioPortalInterno) {
		preRegistroDAO.setaSituacaoSolicitacao(preRegistro, codSituacaoPreRegistro, usuarioPortalInterno);
	}
}
