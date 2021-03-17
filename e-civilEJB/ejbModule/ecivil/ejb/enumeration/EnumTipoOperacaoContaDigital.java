package ecivil.ejb.enumeration;

public enum EnumTipoOperacaoContaDigital {

	COD_ENTRADA_DEPOSITO_BOLETO("E01", "Depósito por boleto"), 
	COD_ENTRADA_TRANSFERENCIA_RECEBIDA("E02", "Transferência recebida por outro cartório"), 
	COD_SAIDA_SAQUE("S01", "Saque"), 
	COD_SAIDA_TRANSFERENCIA_REALIZADA("S02", "Transferência realizada para outro cartório"); 

	private String codigo;
	private String descricao;

	private EnumTipoOperacaoContaDigital() {
	}

	private EnumTipoOperacaoContaDigital(String codigo, String descricao) {
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