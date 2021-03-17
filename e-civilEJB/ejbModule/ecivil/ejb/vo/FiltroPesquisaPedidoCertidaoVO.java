package ecivil.ejb.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import ecivil.ejb.entidade.SituacaoPedidoCertidao;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.util.Util;

public class FiltroPesquisaPedidoCertidaoVO {

	private String protocolo;
	private String cpfmae;
	private String numeroBoleto;
	private Date dataInicioPedido;
	private Date dataFimPedido;
	private Date dataInicioSolicitacaoUI;
	private Date dataFimSolicitacaoUI;
	private String cpfRequerente;

	private String corregedoriaRequisitada;
	private List<String> listaCorregedoriaRequisitada;
	private String corregedoriaRequisitante;
	private List<String> listaCorregedoriaRequisitante;
	private SituacaoPedidoCertidao situacaoPedidoCertidao;
	private String situacaoSolicitacaoUI;
	private UsuarioPortalInterno usuarioPortalInterno;

	public String getProtocolo() {
		return protocolo;
	}

	public String getSituacaoSolicitacaoUI() {
		return situacaoSolicitacaoUI;
	}

	public void setSituacaoSolicitacaoUI(String situacaoSolicitacaoUI) {
		this.situacaoSolicitacaoUI = situacaoSolicitacaoUI;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}
	
	public String getCpfmae() {
		return cpfmae;
	}

	public void setCpfmae(String cpfmae) {
		this.cpfmae = cpfmae;
	}
	public String getNumeroBoleto() {
		return numeroBoleto;
	}

	public void setNumeroBoleto(String numeroBoleto) {
		this.numeroBoleto = numeroBoleto;
	}
	
	public Date getDataInicioSolicitacaoUI() {
		return dataInicioSolicitacaoUI;
	}

	public void setDataInicioSolicitacaoUI(Date dataInicioSolicitacaoUI) {
		this.dataInicioSolicitacaoUI = dataInicioSolicitacaoUI;
	}

	public Date getDataFimSolicitacaoUI() {
		return dataFimSolicitacaoUI;
	}

	public void setDataFimSolicitacaoUI(Date dataFimSolicitacaoUI) {
		this.dataFimSolicitacaoUI = dataFimSolicitacaoUI;
	}

	public Date getDataInicioPedido() {
		return dataInicioPedido;
	}

	public void setDataInicioPedido(Date dataInicioPedido) {
		this.dataInicioPedido = dataInicioPedido;
	}

	public Date getDataFimPedido() {
		return dataFimPedido;
	}

	public void setDataFimPedido(Date dataFimPedido) {
		this.dataFimPedido = dataFimPedido;
	}
	
	public String getCpfRequerente() {
		return cpfRequerente;
	}

	public void setCpfRequerente(String cpfRequerente) {
		this.cpfRequerente = cpfRequerente;
	}
	
	public String getCorregedoriaRequisitada() {
		return corregedoriaRequisitada;
	}

	public void setCorregedoriaRequisitada(String corregedoriaRequisitada) {
		this.corregedoriaRequisitada = corregedoriaRequisitada;
	}

	public List<String> getListaCorregedoriaRequisitadaInicializada() {
		if (listaCorregedoriaRequisitada == null) {
			listaCorregedoriaRequisitada = new ArrayList<>();
		}
		return listaCorregedoriaRequisitada;
	}
	
	public List<String> getListaCorregedoriaRequisitada() {
		return listaCorregedoriaRequisitada;
	}

	public void setListaCorregedoriaRequisitada(List<String> listaCorregedoriaRequisitada) {
		this.listaCorregedoriaRequisitada = listaCorregedoriaRequisitada;
	}

	public String getCorregedoriaRequisitante() {
		return corregedoriaRequisitante;
	}

	public void setCorregedoriaRequisitante(String corregedoriaRequisitante) {
		this.corregedoriaRequisitante = corregedoriaRequisitante;
	}
	
	public List<String> getListaCorregedoriaRequisitanteInicializada() {
		if (listaCorregedoriaRequisitante == null) {
			listaCorregedoriaRequisitante = new ArrayList<>();
		}
		return listaCorregedoriaRequisitante;
	}
	
	public List<String> getListaCorregedoriaRequisitante() {
		return listaCorregedoriaRequisitante;
	}

	public void setListaCorregedoriaRequisitante(List<String> listaCorregedoriaRequisitante) {
		this.listaCorregedoriaRequisitante = listaCorregedoriaRequisitante;
	}
	
	public SituacaoPedidoCertidao getSituacaoPedidoCertidao() {
		return situacaoPedidoCertidao;
	}

	public void setSituacaoPedidoCertidao(SituacaoPedidoCertidao situacaoPedidoCertidao) {
		this.situacaoPedidoCertidao = situacaoPedidoCertidao;
	}
	
	public UsuarioPortalInterno getUsuarioPortalInterno() {
		return usuarioPortalInterno;
	}

	public void setUsuarioPortalInterno(UsuarioPortalInterno usuarioPortalInterno) {
		this.usuarioPortalInterno = usuarioPortalInterno;
	}

	public String montaConsultaPesquisaPedidoCertidao(boolean ehCount) {
		StringBuffer consulta = new StringBuffer();
		montaSelect(consulta, ehCount);
		montaJoin(consulta);
		montaClausulaWhere(consulta);
		montaOrdenacao(consulta, ehCount);
		return consulta.toString();
	}
	
	public String montaConsultaPesquisaSolicitacoesUI(boolean ehCount) {
		StringBuffer consulta = new StringBuffer();
		montaSelectSolicitacaoUI(consulta, ehCount);
		montaClausulaWhereSolicitacaoUI(consulta);
		montaOrdenacaoPreRegistro(consulta, ehCount);
		return consulta.toString();
	}

	private void montaSelect(StringBuffer consulta, boolean ehCount) {
		if (ehCount) {
			consulta.append(" SELECT COUNT(DISTINCT pedidoCertidao) ");
		} else {
			consulta.append(" SELECT DISTINCT pedidoCertidao ");
		}
		consulta.append(" FROM PedidoCertidao pedidoCertidao ");
	}
	
	private void montaSelectPreRegistro(StringBuffer consulta, boolean ehCount) {
		if (ehCount) {
			consulta.append(" SELECT COUNT(DISTINCT preRegistro) ");
		} else {
			consulta.append(" SELECT DISTINCT preRegistro ");
		}
		consulta.append(" FROM PreRegistro preRegistro ");
	}
	
	private void montaSelectSolicitacaoUI(StringBuffer consulta, boolean ehCount) {
		if (ehCount) {
			consulta.append(" SELECT COUNT(DISTINCT preRegistro) ");
		} else {
			consulta.append(" SELECT DISTINCT preRegistro ");
		}
		consulta.append(" FROM PreRegistro preRegistro");
	}
	
	private void montaJoin(StringBuffer consulta) {
		if (Util.ehStringValida(getNumeroBoleto())) {
			consulta.append(" JOIN pedidoCertidao.listaVinculoBoleto vinculoBoleto ");
		}
	}
	
	private void montaClausulaWhere(StringBuffer consulta) {
		consulta.append(" WHERE 1 = 1 ");
		
		if (Util.ehStringValida(getProtocolo())) {
			montaClausulaWhereProtocolo(consulta);
		} else {
			montaClausulaWhereDataInicioPedido(consulta);
			montaClausulaWhereDataFimPedido(consulta);
			montaClausulaWhereCpfRequerente(consulta);
			montaClausulaWhereSituacao(consulta);
			montaClausulaWhereNumeroBoleto(consulta);
		}
		
		montaClausulaWhereCorregedoriaRequisitada(consulta);
	}
	private void montaClausulaWhereSolicitacaoUI(StringBuffer consulta) {
		consulta.append(" WHERE 1 = 1 ");
		
		if (Util.isValidCPF(getCpfmae())) {
			montaClausulaWhereCpfmae(consulta);
		} else {
			montaClausulaWhereDataInicioPedido(consulta);
			montaClausulaWhereDataFimPedido(consulta);
			montaClausulaWhereSituacao(consulta);
		}
		
		montaClausulaWhereCorregedoriaRequisitada(consulta);
	}

	private void montaClausulaWhereProtocolo(StringBuffer consulta) {
		consulta.append(" AND pedidoCertidao.protocolo = :protocolo ");
	}
	
	private void montaClausulaWhereProtocoloPreRegistro(StringBuffer consulta) {
		consulta.append(" AND preRegistro.numeroSolicitacao = :numeroSolicitacao ");
	}
	
	private void montaClausulaWhereCpfmae(StringBuffer consulta) {
		consulta.append(" AND solicitacaoUINascimento.cpfMae = :cpfmae ");
	}
	
	
	private void montaClausulaWhereDataInicioPedido(StringBuffer consulta) {
		if (getDataInicioPedido() != null) {
			consulta.append(" AND DATE_TRUNC('DAY', pedidoCertidao.dataPedido) >= :dataInicioPedido ");
		}
	}
	
	private void montaClausulaWhereDataFimPedido(StringBuffer consulta) {
		if (getDataFimPedido() != null) {
			consulta.append(" AND DATE_TRUNC('DAY', pedidoCertidao.dataPedido) <= :dataFimPedido ");
		}		
	}

	private void montaClausulaWhereCpfRequerente(StringBuffer consulta) {
		if (Util.ehStringValida(getCpfRequerente())) {
			consulta.append(" AND pedidoCertidao.usuarioExterno.cpf = :cpfRequerente ");
		}
	}

	private void montaClausulaWhereSituacao(StringBuffer consulta) {
		if (getSituacaoPedidoCertidao() != null) {
			consulta.append(" AND pedidoCertidao.situacao = :situacao ");
		}
	}
	
	private void montaClausulaWhereNumeroBoleto(StringBuffer consulta) {
		if (Util.ehStringValida(getNumeroBoleto())) {
			consulta.append(" AND vinculoBoleto.numeroBoleto = :numeroBoleto ");
		}		
	}
	
	private void montaClausulaWhereCorregedoriaRequisitada(StringBuffer consulta) {
		if (!getListaCorregedoriaRequisitadaInicializada().isEmpty()) {
			consulta.append(" AND pedidoCertidao.codCorregedoriaRequisitada IN (:listaCodCorregedoriaRequisitada) ");
		}
		
		if (Util.ehStringValida(getCorregedoriaRequisitada())) {
			consulta.append(" AND pedidoCertidao.codCorregedoriaRequisitada = :codCorregedoriaRequisitada ");
		}
	}
	
	private void montaOrdenacao(StringBuffer consulta, boolean ehCount) {
		if (!ehCount) {
			consulta.append(" ORDER BY pedidoCertidao.dataAlteracao DESC  ");
		}
	}
	
	private void montaOrdenacaoPreRegistro(StringBuffer consulta, boolean ehCount) {
		if (!ehCount) {
			consulta.append(" ORDER BY preRegistro.dataInicioSolicitacao DESC  ");
		}
	}
	
	public void setaParametrosPesquisaPedidoCertidao(Query query) {
		if (Util.ehStringValida(getProtocolo())) {
			setaParametroProtocolo(query);
		} else {
			setaParametroDataInicioPedido(query);
			setaParametroDataFimPedido(query);
			setaParametroCpfRequerente(query);
			setaParametroSituacao(query);
			setaParametroNumeroBoleto(query);
		}
		
		setaParametroCorregedoriaRequisitada(query);
	}
	
	public void setaParametrosSolicitacaoUI(Query query) {
		if (Util.isValidCPF(getCpfmae())) {
			setaParametroCpfMae(query);
		} else {
			setaParametroDataInicioSolicitacaoUI(query);
			setaParametroDataFimSolicitacaoUI(query);
			setaParametroSituacaoSolicitacaoUI(query);
		}
		
		setaParametroCorregedoriaRequisitada(query);
	}

	private void setaParametroProtocolo(Query query) {
		query.setParameter("protocolo", getProtocolo());
	}
	
	private void setaParametroCpfMae(Query query) {
		query.setParameter("cpfmae", getCpfmae());
	}
	
	private void setaParametroDataInicioPedido(Query query) {
		if (getDataInicioPedido() != null) {
			query.setParameter("dataInicioPedido", getDataInicioPedido());
		}
	}
	
	private void setaParametroDataInicioSolicitacaoUI(Query query) {
		if (getDataInicioSolicitacaoUI()!= null) {
			query.setParameter("dataInicioSolicitacaoUI", getDataInicioSolicitacaoUI());
		}
	}
	
	private void setaParametroDataFimSolicitacaoUI(Query query) {
		if (getDataFimSolicitacaoUI() != null) {
			query.setParameter("dataFimSolicitacaoUI", getDataFimSolicitacaoUI());
		}
	}


	private void setaParametroDataFimPedido(Query query) {
		if (getDataFimPedido() != null) {
			query.setParameter("dataFimPedido", getDataFimPedido());
		}
	}

	private void setaParametroCpfRequerente(Query query) {
		if (Util.ehStringValida(getCpfRequerente())) {
			query.setParameter("cpfRequerente", getCpfRequerente());
		}
	}

	private void setaParametroSituacao(Query query) {
		if (getSituacaoPedidoCertidao() != null) {
			query.setParameter("situacao", getSituacaoPedidoCertidao());
		}
	}
	
	private void setaParametroSituacaoSolicitacaoUI(Query query) {
		if (getSituacaoSolicitacaoUI() != null) {
			query.setParameter("situacao", getSituacaoSolicitacaoUI());
		}
	}
	
	private void setaParametroNumeroBoleto(Query query) {
		if (Util.ehStringValida(getNumeroBoleto())) {
			query.setParameter("numeroBoleto", getNumeroBoleto());
		}		
	}
	
	private void setaParametroCorregedoriaRequisitada(Query query) {
		if (!getListaCorregedoriaRequisitadaInicializada().isEmpty()) {
			query.setParameter("listaCodCorregedoriaRequisitada", getListaCorregedoriaRequisitadaInicializada());
		}
		
		if (Util.ehStringValida(getCorregedoriaRequisitada())) {
			query.setParameter("codCorregedoriaRequisitada", getCorregedoriaRequisitada());
		}
	}

	public String montaConsultaPesquisarPedidosRealizadoPeloCartorio(boolean ehCount) {
		StringBuffer consulta = new StringBuffer();
		montaSelect(consulta, ehCount);
		montaClausulaWherePedidosRealizadoPeloCartorio(consulta);
		montaOrdenacao(consulta, ehCount);
		return consulta.toString();
	}
	
	public String montaConsultaPesquisarPreRegistro(boolean ehCount) {
		StringBuffer consulta = new StringBuffer();
		montaSelectPreRegistro(consulta, ehCount);
		montaClausulaWhereProtocoloPreRegistro(consulta);
		montaOrdenacaoPreRegistro(consulta, ehCount);
		System.out.println(consulta);
		return consulta.toString();
	}
	
	private void montaClausulaWherePedidosRealizadoPeloCartorio(StringBuffer consulta) {
		consulta.append(" WHERE 1 = 1 ");
		
		if (Util.ehStringValida(getProtocolo())) {
			montaClausulaWhereProtocolo(consulta);
		} else {
			montaClausulaWhereDataInicioPedido(consulta);
			montaClausulaWhereDataFimPedido(consulta);
			montaClausulaWhereCorregedoriaRequisitada(consulta);
			montaClausulaWhereSituacao(consulta);
		}
		
		montaClausulaWherePedidosFeitosPorCartorio(consulta);
		montaClausulaWhereCorregedoriaRequisitante(consulta);
	}

	private void montaClausulaWherePedidosFeitosPorCartorio(StringBuffer consulta) {
		consulta.append(" AND pedidoCertidao.codCorregedoriaRequisitante IS NOT NULL ");				
	}
	
	private void montaClausulaWhereCorregedoriaRequisitante(StringBuffer consulta) {
		if (!getListaCorregedoriaRequisitanteInicializada().isEmpty()) {
			consulta.append(" AND pedidoCertidao.codCorregedoriaRequisitante IN (:listaCodCorregedoriaRequisitante) ");
		}
		
		if (Util.ehStringValida(getCorregedoriaRequisitante())) {
			consulta.append(" AND pedidoCertidao.codCorregedoriaRequisitante = :codCorregedoriaRequisitante ");
		}		
	}

	public void setaParametrosPesquisaPedidoRealizadoPeloCartorio(Query query) {
		if (Util.ehStringValida(getProtocolo())) {
			setaParametroProtocolo(query);
		} else {
			setaParametroDataInicioPedido(query);
			setaParametroDataFimPedido(query);
			setaParametroCorregedoriaRequisitada(query);
			setaParametroSituacao(query);
		}
		
		setaParametroCorregedoriaRequisitante(query);
	}
	
	private void setaParametroCorregedoriaRequisitante(Query query) {
		if (!getListaCorregedoriaRequisitanteInicializada().isEmpty()) {
			query.setParameter("listaCodCorregedoriaRequisitante", getListaCorregedoriaRequisitanteInicializada());
		}
		
		if (Util.ehStringValida(getCorregedoriaRequisitante())) {
			query.setParameter("codCorregedoriaRequisitante", getCorregedoriaRequisitante());
		}		
	}
}
