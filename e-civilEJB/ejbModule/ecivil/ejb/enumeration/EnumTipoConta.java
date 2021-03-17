package ecivil.ejb.enumeration;

public enum EnumTipoConta {

	C("Conta Corrente"), P("Poupan�a");

	private String descricao;

	private EnumTipoConta() {
	}

	private EnumTipoConta(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
