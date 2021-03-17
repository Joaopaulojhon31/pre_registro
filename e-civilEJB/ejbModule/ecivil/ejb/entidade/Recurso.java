package ecivil.ejb.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import ecivil.ejb.util.Util;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "recurso")
@SequenceGenerator(name = "seq_id_recurso", sequenceName = "ecivil.seq_id_recurso", allocationSize = 1)
public class Recurso implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_recurso")
	@Column(name = "id_recurso")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_permissao")
	private Permissao permissao;

	@Column(name = "descricao")
	private String descricao;

	@Version
	@Column(name = "versao")
	private Integer versao;

	public String getDescricaoTratada() {
		if (!Util.ehStringValida(getDescricao())) {
			return null;
		}
		return getDescricao().replace(".xhtml", "").replace(".jsf", "");
	}

	public boolean possuiDescricao() {
		return Util.ehStringValida(getDescricao());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
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
		if (obj instanceof Recurso) {
			if (((Recurso) obj).getId() != null) {
				return ((Recurso) obj).getId().equals(this.id);
			}
			return this == obj;
		}
		return false;
	}

}