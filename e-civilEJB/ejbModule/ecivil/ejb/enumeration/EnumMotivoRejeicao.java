package ecivil.ejb.enumeration;

public enum EnumMotivoRejeicao {

	REGISTRO_N�O_LOCALIZADO_NESTA_SERVENTIA("1", "REGISTRO N�O LOCALIZADO NESTA SERVENTIA"), 
	INFORMAR_MOTIVO("2", "INFORMAR MOTIVO");

	private String codigo;
	private String descricao;

	private EnumMotivoRejeicao() {
	}

	private EnumMotivoRejeicao(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}
	
}