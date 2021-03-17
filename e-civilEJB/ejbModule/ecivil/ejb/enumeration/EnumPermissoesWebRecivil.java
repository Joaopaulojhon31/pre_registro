package ecivil.ejb.enumeration;

import ecivil.ejb.util.Util;

public enum EnumPermissoesWebRecivil {

	PERMISSAO_OFICIAL("1", "Oficial"), 
	PERMISSAO_FUNCIONARIO("3", "Funcionário");

	private String codigo;
	private String descricao;

	private EnumPermissoesWebRecivil() {
	}

	private EnumPermissoesWebRecivil(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public EnumPermissoesWebRecivil recuperaPermissaoPorCodigo(String codigo) {
		if (!Util.ehStringValida(codigo)) {
			return null;
		}
		
		if (codigo.equals(EnumPermissoesWebRecivil.PERMISSAO_OFICIAL.getCodigo())) {
			return EnumPermissoesWebRecivil.PERMISSAO_OFICIAL;
		}
		
		return EnumPermissoesWebRecivil.PERMISSAO_FUNCIONARIO;
	}
	
}