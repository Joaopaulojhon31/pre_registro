package ecivil.ejb.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "usuario_serventia")
@SequenceGenerator(name = "seq_id_usuario_serventia", sequenceName = "ecivil.seq_id_usuario_serventia", allocationSize = 1)
public class UsuarioServentia implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_usuario_serventia")
	@Column(name = "id_usuario_serventia")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_portal_interno")
	private UsuarioPortalInterno usuario;

	@Column(name = "codigo_corregedoria")
	private String codigoCorregedoria;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;
	
	public boolean isServentiaAtiva() {
		return getDataFim() == null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioPortalInterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalInterno usuario) {
		this.usuario = usuario;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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
		if (obj instanceof UsuarioServentia) {
			if (((UsuarioServentia) obj).getId() != null) {
				return ((UsuarioServentia) obj).getId().equals(this.id);
			}
			return this == obj;
		}
		return false;
	}

}
