package ecivil.ejb.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import ecivil.ejb.entidade.HistoricoPedidoCertidao;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.UsuarioPortalExterno;
import ecivil.ejb.vo.FiltroPesquisaPedidoCertidaoVO;
import web.util.Paginacao;

@SuppressWarnings("serial")
@Stateless
public class PedidoCertidaoDAO extends BaseDAO {

	public String recuperaSequencialProtocoloCertidao() {
		Query query = getEntityManager().createNativeQuery("SELECT NEXTVAL('ecivil.seq_protocolo')");
		return query.getSingleResult().toString();
	}

	public int recuperaTotalPedidoCertidaoUsuario(UsuarioPortalExterno usuarioExterno) {
		Query query = getEntityManager().createQuery(queryRecuperaPedidoCertidaoUsuario(Boolean.TRUE));
		query.setParameter("usuarioExterno", usuarioExterno);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<PedidoCertidao> recuperaPedidoCertidaoUsuario(UsuarioPortalExterno usuarioExterno, Paginacao paginacao) {
		Query query = getEntityManager().createQuery(queryRecuperaPedidoCertidaoUsuario(Boolean.FALSE));
		query.setParameter("usuarioExterno", usuarioExterno);
		query.setFirstResult(paginacao.getPrimeiroRegistro());
		query.setMaxResults(paginacao.getNumeroDeRegistrosExibidos());
		List<PedidoCertidao> listaPedidoCertidao = (List<PedidoCertidao>) query.getResultList();
		inicializaGrafoPedidoCertidao(listaPedidoCertidao);
		return listaPedidoCertidao;
	}
	
	private String queryRecuperaPedidoCertidaoUsuario(Boolean ehCount) {
		StringBuffer sb = new StringBuffer();
		if (ehCount) {
			sb.append(" SELECT COUNT(DISTINCT pedidoCertidao) ");
		} else {
			sb.append(" SELECT DISTINCT pedidoCertidao ");
		}
		sb.append(" FROM PedidoCertidao pedidoCertidao ");
		sb.append(" WHERE pedidoCertidao.usuarioExterno = :usuarioExterno ");
		if (!ehCount) {
			sb.append(" ORDER BY pedidoCertidao.dataAlteracao DESC ");
		}
		return sb.toString();
	}
	
	private void inicializaGrafoPedidoCertidao(List<PedidoCertidao> listaPedidoCertidao) {
		if (listaPedidoCertidao == null) {
			return;
		}
		
		for (PedidoCertidao pedidoCertidao : listaPedidoCertidao) {
			Hibernate.initialize(pedidoCertidao.getSituacao());
			Hibernate.initialize(pedidoCertidao.getTipoCertidao());
			Hibernate.initialize(pedidoCertidao.getUsuarioExterno());
		}
	}

	public int pesquisarTotalPedidosCertidao(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisaPedidoCertidao(Boolean.TRUE));
		filtroPesquisaCertidao.setaParametrosPesquisaPedidoCertidao(query);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	public int pesquisarTotalSolicitacoesUI(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisaSolicitacoesUI(Boolean.TRUE));
		filtroPesquisaCertidao.setaParametrosPesquisaPedidoCertidao(query);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<PedidoCertidao> pesquisarPedidosCertidao(FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao, Paginacao paginacao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisaPedidoCertidao(Boolean.FALSE));
		query.setFirstResult(paginacao.getPrimeiroRegistro());
		query.setMaxResults(paginacao.getNumeroDeRegistrosExibidos());
		filtroPesquisaCertidao.setaParametrosPesquisaPedidoCertidao(query);
		List<PedidoCertidao> listaPedidoCertidao = new ArrayList<>();
		listaPedidoCertidao = (List<PedidoCertidao>) query.getResultList();
		inicializaGrafoPedidoCertidao(listaPedidoCertidao);
		return listaPedidoCertidao;
	}

	public PedidoCertidao recuperaPedidoCertidaoPorProtocolo(String protocolo) {
		try {
			Query query = getEntityManager().createQuery("SELECT pedidoCertidao FROM PedidoCertidao pedidoCertidao WHERE pedidoCertidao.protocolo = :protocolo ");
			query.setParameter("protocolo", protocolo.toUpperCase().trim());
			PedidoCertidao pedidoCertidao = (PedidoCertidao) query.getSingleResult();
			inicializaGrafoCompletoPedidoCertidao(pedidoCertidao);
			return pedidoCertidao;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public PedidoCertidao recuperaPedidoCertidaoPorProtocoloEUsuarioExterno(String protocolo, UsuarioPortalExterno usuarioExterno) {
		try {
			Query query = getEntityManager().createQuery("SELECT pedidoCertidao FROM PedidoCertidao pedidoCertidao WHERE pedidoCertidao.protocolo = :protocolo AND pedidoCertidao.usuarioExterno = :usuarioExterno ");
			query.setParameter("protocolo", protocolo.toUpperCase().trim());
			query.setParameter("usuarioExterno", usuarioExterno);
			PedidoCertidao pedidoCertidao = (PedidoCertidao) query.getSingleResult();
			inicializaGrafoPedidoCertidao(Arrays.asList(pedidoCertidao));
			return pedidoCertidao;
		} catch (NoResultException e) {
			return null;
		}
	}

	private void inicializaGrafoCompletoPedidoCertidao(PedidoCertidao pedidoCertidao) {
		Hibernate.initialize(pedidoCertidao.getSituacao());
		Hibernate.initialize(pedidoCertidao.getTipoCertidao());
		Hibernate.initialize(pedidoCertidao.getUsuarioExterno());		
		Hibernate.initialize(pedidoCertidao.getEnderecoUsuarioExterno());
		Hibernate.initialize(pedidoCertidao.getCertidaoNegativa());
		Hibernate.initialize(pedidoCertidao.getListaVinculoBoleto());
		Hibernate.initialize(pedidoCertidao.getListaHistoricoPedidoCertidao());
		Hibernate.initialize(pedidoCertidao.getListaValoresPedidoCertidao());
		
		inicializaEnderecoUsuarioExterno(pedidoCertidao);
		inicializaHistoricoPedidoCertidao(pedidoCertidao);
	}

	private void inicializaEnderecoUsuarioExterno(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.getEnderecoUsuarioExterno() != null) {
			Hibernate.initialize(pedidoCertidao.getEnderecoUsuarioExterno().getTipoLogradouro());		
			Hibernate.initialize(pedidoCertidao.getEnderecoUsuarioExterno().getMunicipio());		
			Hibernate.initialize(pedidoCertidao.getEnderecoUsuarioExterno().getUf());		
		}		
	}

	private void inicializaHistoricoPedidoCertidao(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.getListaHistoricoPedidoCertidao() == null) {
			return;
		}
		
		for (HistoricoPedidoCertidao historicoPedidoCertidao : pedidoCertidao.getListaHistoricoPedidoCertidao()) {
			Hibernate.initialize(historicoPedidoCertidao.getSituacao());		
		}
	}

	public int pesquisarTotalPedidosRealizadoPeloCartorio(String codigoCorregedoria, FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisarPedidosRealizadoPeloCartorio(Boolean.TRUE));
		filtroPesquisaCertidao.setaParametrosPesquisaPedidoRealizadoPeloCartorio(query);
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<PedidoCertidao> pesquisarPedidosRealizadoPeloCartorio(String codigoCorregedoria, FiltroPesquisaPedidoCertidaoVO filtroPesquisaCertidao, Paginacao paginacao) {
		Query query = getEntityManager().createQuery(filtroPesquisaCertidao.montaConsultaPesquisarPedidosRealizadoPeloCartorio(Boolean.FALSE));
		query.setFirstResult(paginacao.getPrimeiroRegistro());
		query.setMaxResults(paginacao.getNumeroDeRegistrosExibidos());
		filtroPesquisaCertidao.setaParametrosPesquisaPedidoRealizadoPeloCartorio(query);
		List<PedidoCertidao> listaProcessos = new ArrayList<>();
		listaProcessos = (List<PedidoCertidao>) query.getResultList();
		inicializaGrafoPedidoCertidao(listaProcessos);
		return listaProcessos;
	}

	public BigDecimal calculaValorCertidaoPessoaSemAverbacao(BigDecimal valorCertidao, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao, BigDecimal taxaManuntencao, BigDecimal aliquotaIssqn) {
		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_valor_certidao_pessoa_sem_averbacao(:valorCertidao, :taxaRecompe, :taxaFiscalizacao, :taxaManuntencao, :aliquotaIssqn) ");
		query.setParameter("valorCertidao", valorCertidao);
		query.setParameter("taxaRecompe", taxaRecompe);
		query.setParameter("taxaFiscalizacao", taxaFiscalizacao);
		query.setParameter("taxaManuntencao", taxaManuntencao);
		query.setParameter("aliquotaIssqn", aliquotaIssqn);
		return (BigDecimal) query.getSingleResult();
	}
	
	public BigDecimal calculaValorCertidaoPessoaComAverbacao(BigDecimal valorCertidao, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao, BigDecimal valorAverbacao, BigDecimal taxaRecompeAverbacao,
			BigDecimal taxaFiscalizacaoAverbacao, BigDecimal taxaManuntencao, BigDecimal aliquotaIssqn) {
		
		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_valor_certidao_pessoa_com_averbacao(:valorCertidao, :taxaRecompe, :taxaFiscalizacao, :valorAverbacao, :taxaRecompeAverbacao, :taxaFiscalizacaoAverbacao, :taxaManuntencao, :aliquotaIssqn) ");
		query.setParameter("valorCertidao", valorCertidao);
		query.setParameter("taxaRecompe", taxaRecompe);
		query.setParameter("taxaFiscalizacao", taxaFiscalizacao);
		query.setParameter("valorAverbacao", valorAverbacao);
		query.setParameter("taxaRecompeAverbacao", taxaRecompeAverbacao);
		query.setParameter("taxaFiscalizacaoAverbacao", taxaFiscalizacaoAverbacao);
		query.setParameter("taxaManuntencao", taxaManuntencao);
		query.setParameter("aliquotaIssqn", aliquotaIssqn);
		return (BigDecimal) query.getSingleResult();
	}
	
	public BigDecimal calculaValorCertidaoCartorioSemAverbacao(BigDecimal valorCertidao, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao, BigDecimal taxaManuntencao, BigDecimal aliquotaIssqnCartorio1, BigDecimal aliquotaIssqnCartorio2) {
		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_valor_certidao_cartorio_sem_averbacao(:valorCertidao, :taxaRecompe, :taxaFiscalizacao, :taxaManuntencao, :aliquotaIssqnCartorio1, :aliquotaIssqnCartorio2) ");
		query.setParameter("valorCertidao", valorCertidao);
		query.setParameter("taxaRecompe", taxaRecompe);
		query.setParameter("taxaFiscalizacao", taxaFiscalizacao);
		query.setParameter("taxaManuntencao", taxaManuntencao);
		query.setParameter("aliquotaIssqnCartorio1", aliquotaIssqnCartorio1);
		query.setParameter("aliquotaIssqnCartorio2", aliquotaIssqnCartorio2);
		return (BigDecimal) query.getSingleResult();
	}

	public BigDecimal calculaValorCertidaoCartorioComAverbacao(BigDecimal valorCertidao, BigDecimal taxaRecompe, BigDecimal taxaFiscalizacao, BigDecimal valorAverbacao, BigDecimal taxaRecompeAverbacao,
			BigDecimal taxaFiscalizacaoAverbacao, BigDecimal taxaManuntencao, BigDecimal aliquotaIssqnCartorio1, BigDecimal aliquotaIssqnCartorio2) {

		Query query = getEntityManager().createNativeQuery(" SELECT ecivil.calcular_valor_certidao_cartorio_com_averbacao(:valorCertidao, :taxaRecompe, :taxaFiscalizacao, :valorAverbacao, :taxaRecompeAverbacao, :taxaFiscalizacaoAverbacao, :taxaManuntencao, :aliquotaIssqnCartorio1, :aliquotaIssqnCartorio2) ");
		query.setParameter("valorCertidao", valorCertidao);
		query.setParameter("taxaRecompe", taxaRecompe);
		query.setParameter("taxaFiscalizacao", taxaFiscalizacao);
		query.setParameter("valorAverbacao", valorAverbacao);
		query.setParameter("taxaRecompeAverbacao", taxaRecompeAverbacao);
		query.setParameter("taxaFiscalizacaoAverbacao", taxaFiscalizacaoAverbacao);
		query.setParameter("taxaManuntencao", taxaManuntencao);
		query.setParameter("aliquotaIssqnCartorio1", aliquotaIssqnCartorio1);
		query.setParameter("aliquotaIssqnCartorio2", aliquotaIssqnCartorio2);
		return (BigDecimal) query.getSingleResult();
	}

}
