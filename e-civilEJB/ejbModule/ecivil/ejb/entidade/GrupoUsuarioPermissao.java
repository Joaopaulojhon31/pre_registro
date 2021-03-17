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

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "grupo_usuario_permissao")
@SequenceGenerator(name = "seq_id_grupo_usuario_permissao", sequenceName = "ecivil.seq_id_grupo_usuario_permissao", allocationSize = 1)
public class GrupoUsuarioPermissao implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_grupo_usuario_permissao")
	@Column(name = "id_grupo_usuario_permissao")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_grupo_usuario")
	private GrupoUsuario grupoUsuario;

	@ManyToOne
	@JoinColumn(name = "id_permissao")
	private Permissao permissao;
	
	@Version
	@Column(name = "versao")
	private Long versao;

	public GrupoUsuarioPermissao() {
	}

	public GrupoUsuarioPermissao(GrupoUsuario gu) {
		setGrupoUsuario(gu);
	}

	public Permissao getPermissaoTela() {
		if (getPermissao() == null) {
			return new Permissao();
		}
		return getPermissao();
	}

	public void setPermissaoTela(Permissao permissaoTela) {
		setPermissao(permissaoTela);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public Permissao getPermissao() {
		if (permissao == null) {
			setPermissao(new Permissao());
		}
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
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
		if (obj instanceof GrupoUsuarioPermissao) {
			if (((GrupoUsuarioPermissao) obj).getId() != null) {
				return ((GrupoUsuarioPermissao) obj).getId().equals(this.id);
			}
			return this == obj;
		}
		return false;
	}
	
}