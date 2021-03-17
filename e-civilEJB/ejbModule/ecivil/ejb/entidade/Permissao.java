package ecivil.ejb.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "permissao")
@SequenceGenerator(name = "seq_id_permissao", sequenceName = "ecivil.seq_id_permissao", allocationSize = 1)
public class Permissao implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_permissao")
	@Column(name = "id_permissao")
	private Long id;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "excluido")
	private boolean excluido;

	@OneToMany(mappedBy = "permissao", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Recurso.class)
	private Set<Recurso> recursos;

	@Version
	@Column(name = "versao")
	private Integer versao;

	public void instanciarRecursos() {
		if (recursos == null) {
			recursos = new HashSet<Recurso>();
		}
	}

	public void adicionarRecurso() {
		Recurso recurso = new Recurso();
		recurso.setPermissao(this);
		getRecursos().add(recurso);
	}

	public void removerRecurso(Recurso recurso) {
		getRecursos().remove(recurso);
	}

	public boolean possuiIdPermissao() {
		return getId() != null;
	}

	public boolean possuiDescricao() {
		return StringUtils.isNotEmpty(getDescricao());
	}

	public boolean possuiListaRecursos() {
		return getListaRecursos() != null && getListaRecursos().size() > 0;
	}

	public boolean possuiTodosRecursosPreenchidos() {
		if (possuiListaRecursos()) {
			for (Recurso recurso : getListaRecursos()) {
				if (!recurso.possuiDescricao()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public List<Recurso> getListaRecursos() {
		return new ArrayList<Recurso>(getRecursos());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isExcluido() {
		return excluido;
	}

	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}

	public Set<Recurso> getRecursos() {
		return recursos;
	}

	public void setRecursos(Set<Recurso> recursos) {
		this.recursos = recursos;
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
		if (obj instanceof Permissao) {
			if (((Permissao) obj).getId() != null) {
				return ((Permissao) obj).getId().equals(this.id);
			}
			return this == obj;
		}
		return false;
	}

}