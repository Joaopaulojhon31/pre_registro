package ecivil.ejb.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import ecivil.ejb.bo.HistoricoPreRegistroBO;
import ecivil.ejb.entidade.PreRegistro;
import ecivil.ejb.entidade.PreRegistroXML;
import ecivil.ejb.entidade.SituacaoSolicitacaoUI;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.vo.FiltroPesquisaPreRegistroVO;
import web.util.Paginacao;

@SuppressWarnings("serial")
@Stateless
public class PreRegistroDAO extends BaseDAO {


	private static final Date date = new Date();
	@EJB
	private HistoricoPreRegistroBO historicoPreRegistroBO;

	public PreRegistro buscaPreregistroNumeroSolicitacao(Long id) {
		try {
			Query query = getEntityManager().createQuery("SELECT solicitacao FROM PreRegistro solicitacao WHERE solicitacao.id = :id");
			query.setParameter("id", id);
			PreRegistro preRegistro = (PreRegistro) query.getSingleResult();
			return preRegistro;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String retornaDadosXmlPreRegistro(Long idPreRegistro) {
		try {
			Query query = getEntityManager().createQuery("SELECT xml FROM PreRegistroXML xml WHERE xml.idPreRegistro = :idPreRegistro");
			query.setParameter("idPreRegistro", idPreRegistro);
			PreRegistroXML preRegistro = (PreRegistroXML) query.getSingleResult();
			return preRegistro.getXml();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PreRegistro> pesquisarPedidosRealizadoPeloCartorio(String codigoCorregedoria, FiltroPesquisaPreRegistroVO filtroPesquisaCertidao, Paginacao paginacao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisaSolicitacoesUI(Boolean.FALSE));
		filtroPesquisaCertidao.setaParametrosSolicitacaoUI(query);
		query.setFirstResult(paginacao.getPrimeiroRegistro());
		query.setMaxResults(paginacao.getNumeroDeRegistrosExibidos());
		List<PreRegistro> listaProcessos = new ArrayList<>();
		listaProcessos = (List<PreRegistro>) query.getResultList();
		return listaProcessos;
	}
	
	public int pesquisarTotalPreRegistro(FiltroPesquisaPreRegistroVO filtroPesquisaCertidao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisaSolicitacoesUI(Boolean.TRUE));
		filtroPesquisaCertidao.setaParametrosSolicitacaoUI(query);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	public <T> T atualiza(T entidade) {
		try {
			return getEntityManager().merge(entidade);
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	public void montaObjetoXml(PreRegistro preRegistro, String xml) {
		PreRegistroXML preRegistroXml = new PreRegistroXML();
		preRegistroXml.setCorredoria(preRegistro.getCodCorregedoriaCartorio());
		preRegistroXml.setIdPreRegistro(preRegistro.getId());
		preRegistroXml.setDataAlteracao(date);
		preRegistroXml.setIdUnidadeInterligada(preRegistro.getCodigoUi());
		preRegistroXml.setXml(xml);
		preRegistro.setXml(preRegistroXml);
	}
	
	public void salvarXmlPreRegistro(PreRegistroXML preRegistroXml) {
		atualiza(preRegistroXml);
	}
	
	public void setaSituacaoSolicitacao(PreRegistro preRegistro, String codSituacaoPreRegistro, UsuarioPortalInterno usuarioPortalInterno) {
		preRegistro.setSituacaoSolicitacao(codSituacaoPreRegistro);
		if (codSituacaoPreRegistro == SituacaoSolicitacaoUI.FINALIZADO) {
			preRegistro.setDataFimSolicitacao(date);
		}
		historicoPreRegistroBO.setaHistoricoPreRegistro(preRegistro, usuarioPortalInterno);
	}
}
