package ecivil.ejb.enumeration;

public enum EnumSimNao {

	S("Sim"), N("Não");

	private String descricao;

	private EnumSimNao() {
	}

	private EnumSimNao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isSim() {
		return S.equals(this);
	}

}