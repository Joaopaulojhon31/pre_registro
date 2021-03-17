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

import ecivil.ejb.util.Util;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "grupo_usuario")
@SequenceGenerator(name = "seq_id_grupo_usuario", sequenceName = "ecivil.seq_id_grupo_usuario", allocationSize = 1)
public class GrupoUsuario implements Serializable {

	public static final String ADMINISTRADOR = "ADMINISTRADOR";
	public static final String CONSULTA = "CONSULTA";
	public static final String FUNCIONARIO = "FUNCIONARIO";
	public static final String OFICIAL = "OFICIAL";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_grupo_usuario")
	@Column(name = "id_grupo_usuario")
	private Long id;

	@Column(name = "descricao")
	private String descricao;

	@OneToMany(mappedBy = "grupoUsuario", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<GrupoUsuarioPermissao> grupoUsuarioPermissoes;

	@Column(name = "excluido")
	private boolean excluido;
	
	@Version
	@Column(name = "versao")
	private Long versao;

	public List<GrupoUsuarioPermissao> getListGrupoUsuarioPermissoes() {
		return new ArrayList<GrupoUsuarioPermissao>(getGrupoUsuarioPermissoes());
	}

	public void setListUsuarioGrupoUsuarios(List<GrupoUsuarioPermissao> ugp) {
		setGrupoUsuarioPermissoes(new HashSet<GrupoUsuarioPermissao>(ugp));
	}

	public List<GrupoUsuarioPermissao> getListaGrupoUsuarioPermissoes() {
		return new ArrayList<GrupoUsuarioPermissao>(getGrupoUsuarioPermissoes());
	}

	public List<Long> recuperarListaSeqPermissoes() {
		List<Long> listaIdPermissoes = new ArrayList<Long>();
		if (grupoUsuarioPermissoes != null) {
			for (GrupoUsuarioPermissao grupoUsuarioPermissao : getGrupoUsuarioPermissoes()) {
				listaIdPermissoes.add(grupoUsuarioPermissao.getPermissao().getId());
			}
		}
		return listaIdPermissoes;
	}

	public GrupoUsuarioPermissao criarGrupoUsuarioPermissao(Permissao permissao) {
		GrupoUsuarioPermissao grupoUsuarioPermissao = new GrupoUsuarioPermissao();
		grupoUsuarioPermissao.setPermissao(permissao);
		grupoUsuarioPermissao.setGrupoUsuario(this);
		return grupoUsuarioPermissao;
	}

	public GrupoUsuarioPermissao recuperarGrupoUsuarioPermissao(Permissao permissao) {
		if (getGrupoUsuarioPermissoes() != null) {
			for (GrupoUsuarioPermissao grupoUsuarioPermissao : getGrupoUsuarioPermissoes()) {
				if (grupoUsuarioPermissao.getPermissao().getId().equals(permissao.getId())) {
					return grupoUsuarioPermissao;
				}
			}
		}
		return null;
	}

	public boolean possuiSeqGrupoUsuario() {
		return getId() != null;
	}

	public boolean possuiDescricao() {
		return Util.ehStringValida(getDescricao());
	}

	public boolean possuiGrupoUsuarioPermissoes() {
		return grupoUsuarioPermissoes != null && grupoUsuarioPermissoes.size() > 0;
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

	public Set<GrupoUsuarioPermissao> getGrupoUsuarioPermissoes() {
		return grupoUsuarioPermissoes;
	}

	public void setGrupoUsuarioPermissoes(Set<GrupoUsuarioPermissao> grupoUsuarioPermissoes) {
		this.grupoUsuarioPermissoes = grupoUsuarioPermissoes;
	}

	public boolean isExcluido() {
		return excluido;
	}

	public void setExcluido(boolean excluido) {
		this.excluido = excluido;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
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
		if (obj instanceof GrupoUsuario) {
			if (((GrupoUsuario) obj).getId() != null) {
				return ((GrupoUsuario) obj).getId().equals(this.id);
			}
			return this == obj;
		}
		return false;
	}
}