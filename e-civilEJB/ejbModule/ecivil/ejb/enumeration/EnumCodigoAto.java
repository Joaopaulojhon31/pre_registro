package ecivil.ejb.enumeration;

public enum EnumCodigoAto {

	COD_CERTIDAO_DOCUMENTOS_ARQUIVADOS("78048", "Certid�o de documentos arquivados ou de dadoseletronicamente enviados para ou recebidos de outros servi�osregistrais /notariais/�rg�os p�blicos"), 
	COD_AVERBACAO_CERTIDAO_DOCUMENTOS_ARQUIVADOS("79012", "Havendo no termo uma ou mais averba��es ou anota��es,acrescer ao valor da certid�o.");

	private String codigo;
	private String descricao;

	private EnumCodigoAto() {
	}

	private EnumCodigoAto(String codigo, String descricao) {
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