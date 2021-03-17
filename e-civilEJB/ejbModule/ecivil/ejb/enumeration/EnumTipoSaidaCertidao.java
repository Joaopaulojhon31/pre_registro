package ecivil.ejb.enumeration;

import ecivil.ejb.util.Util;

public enum EnumTipoSaidaCertidao {

	DIGITAL("1", "Digital"), CORREIO("2", "Correio"), PRESENCIAL("3", "Buscar presencialmente");

	private String codigo;
	private String descricao;

	private EnumTipoSaidaCertidao() {
	}

	private EnumTipoSaidaCertidao(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public boolean isDigital() {
		return DIGITAL.equals(this);
	}
	
	public boolean isCorreio() {
		return CORREIO.equals(this);
	}
	
	public boolean isPresencial() {
		return PRESENCIAL.equals(this);
	}

	public static EnumTipoSaidaCertidao recuperaEnumTipoSaidaPorCodigo(String codTipoSaida) {
		if (!Util.ehStringValida(codTipoSaida)) {
			return null;
		}
		
		for (EnumTipoSaidaCertidao tipoSaida : EnumTipoSaidaCertidao.values()) {
			if (tipoSaida.getCodigo().equals(codTipoSaida)) {
				return tipoSaida;
			}
		}
		
		return null;
	}
	
	public static String recuperaDescricaoTipoSaidaPorCodigo(String codTipoSaida) {
		if (!Util.ehStringValida(codTipoSaida)) {
			return null;
		}
		
		EnumTipoSaidaCertidao tipoSaida = recuperaEnumTipoSaidaPorCodigo(codTipoSaida);
		return tipoSaida != null ? tipoSaida.getDescricao() : null;
	}
	
}