package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "uf")
public class Uf implements Entidade {

	@Id
	@Column(name = "id_uf")
	private Long id;

	@Column(name = "nome_completo")
	private String nomeCompleto;

	@Column(name = "sigla_uf")
	private String sigla;

	public boolean ehOutroEstado(String siglaUFSede) {
		return !sigla.equals(siglaUFSede);
	}

	public boolean ehEstadoDiferenteDeEstadoSede(String siglaUFSede) {
		return sigla != null && ehOutroEstado(siglaUFSede);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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
		if (obj instanceof Uf) {
			return ((Uf) obj).getId().equals(this.id);
		}
		return false;
	}

}