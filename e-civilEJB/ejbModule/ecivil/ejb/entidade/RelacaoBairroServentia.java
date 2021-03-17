package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "relacao_bairro_serventia")
public class RelacaoBairroServentia implements Entidade {

	@Id
	@Column(name = "bairro")
	private String bairro;

	@Column(name = "serventia")
	private String serventia;

	@Column(name = "codigo_corregedoria_serventia")
	private String codigoCorregedoriaServentia;

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getCodigoCorregedoriaServentia() {
		return codigoCorregedoriaServentia;
	}

	public void setCodigoCorregedoriaServentia(String codigoCorregedoriaServentia) {
		this.codigoCorregedoriaServentia = codigoCorregedoriaServentia;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
