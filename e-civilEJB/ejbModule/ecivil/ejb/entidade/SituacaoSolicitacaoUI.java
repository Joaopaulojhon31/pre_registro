package ecivil.ejb.entidade;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@SuppressWarnings("serial")
@Entity
@Table(schema="ecivil", name="situacao_solicitacao_ui")
public class SituacaoSolicitacaoUI  implements Entidade{

	public static final String COD_EM_PREPARACAO ="1";
	public static final String COD_EM_ANDAMENTO ="2";
	public static final String COD_REJEITADO ="3";
	public static final String COD_REALIZADO = "4";
	public static final String COD_ARQUIVADO = "5";
	public static final String COD_REJEITADO_UI = "6";
	public static final String COD_ANALISE_CRI ="7";
	public static final String COD_INICIAL ="8";
	public static final String COD_FINALIZADO ="9";
	public static final String COD_NAO_ATENDIDO ="10";

	
	@Id
	@Column(name = "id_situacao_solicitacao_ui")
	private Long id;
	
	@Column(name = "nome")
	private String nome;


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

	public static String getCodInicial() {
		return COD_INICIAL;
	}

	public static String getCodEmPreparacao() {
		return COD_EM_PREPARACAO;
	}

	public static String getCodEmAndamento() {
		return COD_EM_ANDAMENTO;
	}

	public static String getCodRejeitado() {
		return COD_REJEITADO;
	}

	public static String getCodRealizado() {
		return COD_REALIZADO;
	}

	public static String getCodArquivado() {
		return COD_ARQUIVADO;
	}

	public static String getCodRejeitadoUi() {
		return COD_REJEITADO_UI;
	}

	public static String getCodAnaliseCri() {
		return COD_ANALISE_CRI;
	}
	
	public static String getCodFinalizado() {
		return COD_FINALIZADO;
	}
	
	public static String getCodNaoAtendido(){
		return COD_NAO_ATENDIDO;
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
		if (obj instanceof SituacaoSolicitacaoUI) {
			return ((SituacaoSolicitacaoUI) obj).getId().equals(this.id);
		}
		return false;
	}
}
