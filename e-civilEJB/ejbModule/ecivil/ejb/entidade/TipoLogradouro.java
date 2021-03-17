package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "tipo_logradouro")
public class TipoLogradouro implements Entidade {

	@Id
	@Column(name = "id_tipo_logradouro")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cod_rfb")
	private String codigoRFB;

	public boolean isNew() {
		return getId() == null;
	}

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

	public String getCodigoRFB() {
		return codigoRFB;
	}

	public void setCodigoRFB(String codigoRFB) {
		this.codigoRFB = codigoRFB;
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
		if (obj instanceof TipoLogradouro) {
			return ((TipoLogradouro) obj).getId().equals(this.id);
		}
		return false;
	}

}
