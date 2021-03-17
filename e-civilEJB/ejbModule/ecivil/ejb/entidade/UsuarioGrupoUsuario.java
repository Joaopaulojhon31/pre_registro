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
@Table(schema = "ecivil", name = "usuario_grupo_usuario")
@SequenceGenerator(name = "seq_id_usuario_grupo_usuario", sequenceName = "ecivil.seq_id_usuario_grupo_usuario", allocationSize = 1)
public class UsuarioGrupoUsuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_usuario_grupo_usuario")
	@Column(name = "id_usuario_grupo_usuario")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_grupo_usuario")
	private GrupoUsuario grupoUsuario;

	@ManyToOne
	@JoinColumn(name = "id_usuario_portal_interno")
	private UsuarioPortalInterno usuario;

	@Version
	private Integer versao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public UsuarioPortalInterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalInterno usuario) {
		this.usuario = usuario;
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
		if (obj instanceof UsuarioGrupoUsuario) {
			if (((UsuarioGrupoUsuario) obj).getId() != null) {
				return ((UsuarioGrupoUsuario) obj).getId().equals(this.id);
			}
			return this == obj;
		}
		return false;
	}

}