package ecivil.ejb.enumeration;

public enum EnumSituacaoSolicitacaoEstorno {

	A("Aceita"), E("Em aberto"), R("Rejeitada");

	private String descricao;

	private EnumSituacaoSolicitacaoEstorno() {
	}

	private EnumSituacaoSolicitacaoEstorno(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isAceita() {
		return this.equals(EnumSituacaoSolicitacaoEstorno.A);
	}
	
	public boolean isEmAberto() {
		return this.equals(EnumSituacaoSolicitacaoEstorno.E);
	}
	
	public boolean isRejeitada() {
		return this.equals(EnumSituacaoSolicitacaoEstorno.R);
	}

}