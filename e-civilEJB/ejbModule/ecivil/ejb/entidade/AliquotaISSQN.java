package ecivil.ejb.entidade;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ecivil.ejb.util.DataUtil;

@SuppressWarnings("serial")
@Entity
@SequenceGenerator(name = "seq_id_aliquota_issqn", sequenceName = "ecivil.seq_id_aliquota_issqn", allocationSize = 1)
@Table(schema = "ecivil", name = "aliquota_issqn")
public class AliquotaISSQN implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_aliquota_issqn")
	@Column(name = "id_aliquota_issqn")
	private Long id;

	@Column(name = "codigo_cns")
	private String codigoCNS;

	@Column(name = "codigo_corregedoria")
	private String codigoCorregedoria;

	@Column(name = "aliquota")
	private BigDecimal aliquota;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoCNS() {
		return codigoCNS;
	}

	public void setCodigoCNS(String codigoCNS) {
		this.codigoCNS = codigoCNS;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public BigDecimal getAliquota() {
		return aliquota;
	}

	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
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
		if (obj instanceof AliquotaISSQN) {
			return ((AliquotaISSQN) obj).getId().equals(this.id);
		}
		return false;
	}
	
	public String dataInicioFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_BARRA(getDataInicio());
	}
	
	public String dataFimFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_BARRA(getDataFim());
	}

}
