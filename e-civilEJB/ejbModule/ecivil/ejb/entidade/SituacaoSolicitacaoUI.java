package ecivil.ejb.entidade;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@SuppressWarnings("serial")
@Entity
@Table(schema="ecivil", name="situacao_solicitacao_ui")
public class SituacaoSolicitacaoUI  implements Entidade{

	public static final String EM_PREPARACAO ="1";
	public static final String EM_ANDAMENTO ="2";
	public static final String REJEITADO ="3";
	public static final String REALIZADO = "4";
	public static final String ARQUIVADO = "5";
	public static final String REJEITADO_UI = "6";
	public static final String ANALISE_CRI ="7";
	public static final String INICIAL ="8";
	public static final String FINALIZADO ="9";

	
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

	public static String getInicial() {
		return INICIAL;
	}

	public static String getEmPreparacao() {
		return EM_PREPARACAO;
	}

	public static String getEmAndamento() {
		return EM_ANDAMENTO;
	}

	public static String getRejeitado() {
		return REJEITADO;
	}

	public static String getRealizado() {
		return REALIZADO;
	}

	public static String getArquivado() {
		return ARQUIVADO;
	}

	public static String getRejeitadoUi() {
		return REJEITADO_UI;
	}

	public static String getAnaliseCri() {
		return ANALISE_CRI;
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
