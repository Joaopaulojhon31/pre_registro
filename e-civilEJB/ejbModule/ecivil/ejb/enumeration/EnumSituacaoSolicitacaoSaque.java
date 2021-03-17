package ecivil.ejb.enumeration;

import ecivil.ejb.util.Util;

public enum EnumSituacaoSolicitacaoSaque {

	EM_ABERTO("1", "Em aberto"), 
	TRANSFERENCIA_LANCADA("2", "Transferência lançada"),
	TRANSFERENCIA_CONFIRMADA("3", "Transferência confirmada");

	private String codigo;
	private String descricao;

	private EnumSituacaoSolicitacaoSaque() {
	}

	private EnumSituacaoSolicitacaoSaque(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public static String descricaoSituacaoPorCodigo(String codigoSituacao) {
		if (!Util.ehStringValida(codigoSituacao)) {
			return null;
		}
		
		if (codigoSituacao.equals(EnumSituacaoSolicitacaoSaque.EM_ABERTO.getCodigo())) {
			return EnumSituacaoSolicitacaoSaque.EM_ABERTO.getDescricao();
		}
		
		if (codigoSituacao.equals(EnumSituacaoSolicitacaoSaque.TRANSFERENCIA_LANCADA.getCodigo())) {
			return EnumSituacaoSolicitacaoSaque.TRANSFERENCIA_LANCADA.getDescricao();
		}
		
		if (codigoSituacao.equals(EnumSituacaoSolicitacaoSaque.TRANSFERENCIA_CONFIRMADA.getCodigo())) {
			return EnumSituacaoSolicitacaoSaque.TRANSFERENCIA_CONFIRMADA.getDescricao();
		}
		
		return null;
	}
	
}