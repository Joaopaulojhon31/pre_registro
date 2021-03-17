package ecivil.ejb.vo;

import java.util.Date;

import javax.persistence.Query;

import ecivil.ejb.util.Util;

public class FiltroPesquisaPreRegistroVO {
	
	private String cpfMae1;
	private Date dataInicioSolicitacao;
	private Date dataFimSolicitacao;
	private String situacaoSolicitacao;
	private String codCorregedoriaCartorio;
	private String numeroSolicitacao;

	public String montaConsultaPesquisaSolicitacoesUI(boolean ehCount) {
		StringBuffer consulta = new StringBuffer();
		montaSelectSolicitacaoUI(consulta, ehCount);
		montaClausulaWhereSolicitacaoUI(consulta);
		montaOrdenacaoPreRegistro(consulta, ehCount);
		return consulta.toString();
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
	
	private void montaClausulaWhereSolicitacaoUI(StringBuffer consulta) {
		consulta.append(" WHERE 1 = 1 ");
		
		if (Util.isValidCPF(getCpfMae())) {
			montaClausulaWhereCpfMae(consulta);
		} else {
			montaClausulaWhereDataInicioPedido(consulta);
			montaClausulaWhereDataFimPedido(consulta);
			montaClausulaWhereCorregedoria(consulta);
			montaClausulaWhereSituacaoSolicitacaoPreRegistro(consulta);
		}
	}
	
	private void montaClausulaWhereNumeroSolicitacaoPreRegistro(StringBuffer consulta) {
		consulta.append(" AND preRegistro.numeroSolicitacao = :numeroSolicitacao ");
	}
	
	private void montaClausulaWhereSituacaoSolicitacaoPreRegistro(StringBuffer consulta) {
		if (getSituacaoSolicitacao() != null) {
			consulta.append(" AND preRegistro.situacaoSolicitacao = :situacaoSolicitacao ");
		}
	}

	private void montaClausulaWhereCpfMae(StringBuffer consulta) {
		if (getCpfMae() != null) {
			consulta.append(" AND preRegistro.cpfMae = :cpfMae ");
		}
	}
	
	private void montaClausulaWhereCorregedoria(StringBuffer consulta) {
		if (getCodCorregedoriaCartorio() != null) {
			consulta.append(" AND preRegistro.codCorregedoriaCartorio = :codCorregedoriaCartorio ");
		}
	}
	
	private void montaClausulaWhereDataInicioPedido(StringBuffer consulta) {
		if (getDataInicioSolicitacao() != null) {
			consulta.append(" AND DATE_TRUNC('DAY', preRegistro.dataInicioSolicitacao) >= :dataInicioSolicitacao ");
		}
	}
	
	private void montaClausulaWhereDataFimPedido(StringBuffer consulta) {
		if (getDataFimSolicitacao() != null) {
			consulta.append(" AND DATE_TRUNC('DAY', preRegistro.dataFimSolicitacao) <= :dataFimSolicitacao ");
		}		
	}
	
	private void montaOrdenacaoPreRegistro(StringBuffer consulta, boolean ehCount) {
		if (!ehCount) {
			consulta.append(" ORDER BY preRegistro.dataInicioSolicitacao DESC  ");
		}
	}
	
	public void setaParametrosSolicitacaoUI(Query query) {
		if (Util.isValidCPF(getCpfMae())) {
			setaParametroCpfMae(query);
		} else {
			setaParametroDataInicioSolicitacaoUI(query);
			setaParametroDataFimSolicitacaoUI(query);
			setaParametroSituacaoSolicitacao(query);
			setaParametrosCorregedoria(query);
			setaParametroNumeroSolicitacao(query);
		}
	}
	
	private void setaParametroCpfMae(Query query) {
		if (Util.isValidCPF(getCpfMae())) {
			query.setParameter("cpfMae", getCpfMae());
		}
	}
	
	private void setaParametrosCorregedoria(Query query) {
		if (getCodCorregedoriaCartorio() != null) {
			query.setParameter("codCorregedoriaCartorio", getCodCorregedoriaCartorio());
		}
	}
	
	private void setaParametroDataInicioSolicitacaoUI(Query query) {
		if (getDataInicioSolicitacao() != null) {
			query.setParameter("dataInicioSolicitacao", getDataInicioSolicitacao());
		}
	}
	
	private void setaParametroDataFimSolicitacaoUI(Query query) {
		if (getDataFimSolicitacao() != null) {
			query.setParameter("dataFimSolicitacao", getDataFimSolicitacao());
		}
	}
	
	private void setaParametroSituacaoSolicitacao(Query query) {
		if (getSituacaoSolicitacao() != null) {
			query.setParameter("situacaoSolicitacao", getSituacaoSolicitacao());
		}
	}
	
	private void setaParametroNumeroSolicitacao(Query query) {
		if (getNumeroSolicitacao() != null) {
			query.setParameter("numeroSolicitacao", getNumeroSolicitacao());
		}
	}
	
	public String montaConsultaPesquisarPreRegistro(boolean ehCount) {
		StringBuffer consulta = new StringBuffer();
		montaSelectPreRegistro(consulta, ehCount);
		montaClausulaWhereNumeroSolicitacaoPreRegistro(consulta);
		montaOrdenacaoPreRegistro(consulta, ehCount);
		System.out.println(consulta);
		return consulta.toString();
	}
	
	public String getSituacaoSolicitacao() {
		return situacaoSolicitacao;
	}

	public void setSituacaoSolicitacao(String situacaoSolicitacaoUI) {
		this.situacaoSolicitacao = situacaoSolicitacaoUI;
	}
	
	public String getCpfMae() {
		return cpfMae1;
	}

	public void setCpfMae(String cpfMae) {
		this.cpfMae1 = cpfMae;
	}
	
	public Date getDataInicioSolicitacao() {
		return dataInicioSolicitacao;
	}

	public void setDataInicioSolicitacao(Date dataInicioSolicitacaoUI) {
		this.dataInicioSolicitacao = dataInicioSolicitacaoUI;
	}

	public Date getDataFimSolicitacao() {
		return dataFimSolicitacao;
	}

	public void setDataFimSolicitacao(Date dataFimSolicitacaoUI) {
		this.dataFimSolicitacao = dataFimSolicitacaoUI;
	}
	
	public String getCodCorregedoriaCartorio() {
		return codCorregedoriaCartorio;
	}

	public void setCodCorregedoriaCartorio(String codCorregedoriaCartorio) {
		this.codCorregedoriaCartorio = codCorregedoriaCartorio;
	}

	public String getNumeroSolicitacao() {
		return numeroSolicitacao;
	}

	public void setNumeroSolicitacao(String numeroSolicitacao) {
		this.numeroSolicitacao = numeroSolicitacao;
	}
}