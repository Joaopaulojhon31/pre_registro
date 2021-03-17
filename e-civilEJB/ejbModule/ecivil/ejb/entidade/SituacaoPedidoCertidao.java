package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "situacao_pedido_certidao")
public class SituacaoPedidoCertidao implements Entidade {

	public static final String COD_EM_ANALISE = "1";
	public static final String COD_AGUARDANDO_PAGAMENTO = "2";
	public static final String COD_PRONTO_PARA_ENVIO = "3";
	public static final String COD_FINALIZADO = "4";
	public static final String COD_REJEITADO = "5";
	public static final String COD_ARQUIVADO = "6";
	public static final String COD_COLETA_NO_CARTORIO = "7";
	public static final String COD_PAGAMENTO_APROVADO_AGUARDANDO_LIBERACAO = "8";
	public static final String COD_AGUARDANDO_VALIDACAO_ARQUIVO = "9";
	public static final String COD_ARQUIVO_NAO_ACEITO = "10";
	public static final String COD_SOLICITACAO_ESTORNO = "11";
	public static final String COD_ESTORNO_REJEITADO = "12";
	public static final String COD_ESTORNO_REALIZADO = "13";
	
	@Id
	@Column(name = "id_situacao_pedido_certidao")
	private Long id;

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "progresso")
	private String progresso;
	
	@Column(name = "cor_requisitante")
	private String corRequisitante;
	
	@Column(name = "cor_requisitado")
	private String corRequisitado;
	
	@Column(name = "ordenacao_requisitante")
	private String ordenacaoRequisitante;
	
	@Column(name = "ordenacao_requisitado")
	private String ordenacaoRequisitado;
	
	@Column(name = "classe_css_requisitado")
	private String classeCssRequisitado;
	
	@Column(name = "classe_css_requisitante")
	private String classeCssRequisitante;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getProgresso() {
		return progresso;
	}

	public void setProgresso(String progresso) {
		this.progresso = progresso;
	}
	

	public String getCorRequisitante() {
		return corRequisitante;
	}

	public void setCorRequisitante(String corRequisitante) {
		this.corRequisitante = corRequisitante;
	}

	public String getCorRequisitado() {
		return corRequisitado;
	}

	public void setCorRequisitado(String corRequisitado) {
		this.corRequisitado = corRequisitado;
	}

	public String getOrdenacaoRequisitante() {
		return ordenacaoRequisitante;
	}

	public void setOrdenacaoRequisitante(String ordenacaoRequisitante) {
		this.ordenacaoRequisitante = ordenacaoRequisitante;
	}

	public String getOrdenacaoRequisitado() {
		return ordenacaoRequisitado;
	}

	public void setOrdenacaoRequisitado(String ordenacaoRequisitado) {
		this.ordenacaoRequisitado = ordenacaoRequisitado;
	}
	
	public String getClasseCssRequisitado() {
		return classeCssRequisitado;
	}

	public void setClasseCssRequisitado(String classeCssRequisitado) {
		this.classeCssRequisitado = classeCssRequisitado;
	}

	public String getClasseCssRequisitante() {
		return classeCssRequisitante;
	}

	public void setClasseCssRequisitante(String classeCssRequisitante) {
		this.classeCssRequisitante = classeCssRequisitante;
	}

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof SituacaoPedidoCertidao) {
			return ((SituacaoPedidoCertidao) obj).getId().equals(this.id);
		}
		return false;
	}

}
