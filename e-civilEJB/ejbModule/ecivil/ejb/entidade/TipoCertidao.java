package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "tipo_certidao")
public class TipoCertidao implements Entidade {
	
	public static final String COD_CERTIDAO_NASCIMENTO = "N";
	public static final String COD_CERTIDAO_CASAMENTO = "C";
	public static final String COD_CERTIDAO_OBTO = "O";

	@Id
	@Column(name = "id_tipo_certidao")
	private Long id;

	@Column(name = "codigo")
	private String codigo;

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "fato")
	private String descFato;
	
	@Column(name = "exibe_apenas_negativa")
	private boolean exibeApenasNegativa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescFato() {
		return descFato;
	}

	public void setDescFato(String descFato) {
		this.descFato = descFato;
	}
	
	public boolean isExibeApenasNegativa() {
		return exibeApenasNegativa;
	}

	public void setExibeApenasNegativa(boolean exibeApenasNegativa) {
		this.exibeApenasNegativa = exibeApenasNegativa;
	}

	public boolean isCertidaoNascimento() {
		return this.getCodigo().equals(COD_CERTIDAO_NASCIMENTO);
	}
	
	public boolean isCertidaoCasamento() {
		return this.getCodigo().equals(COD_CERTIDAO_CASAMENTO);
	}
	
	public boolean isCertidaoObto() {
		return this.getCodigo().equals(COD_CERTIDAO_OBTO);
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
		if (obj instanceof TipoCertidao) {
			return ((TipoCertidao) obj).getId().equals(this.id);
		}
		return false;
	}

}
