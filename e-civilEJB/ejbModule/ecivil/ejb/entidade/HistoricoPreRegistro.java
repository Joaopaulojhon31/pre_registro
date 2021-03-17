package ecivil.ejb.entidade;

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

import ecivil.ejb.util.DataUtil;

@SuppressWarnings("serial")
@Entity
@SequenceGenerator(name = "seq_id_historico_preregistro", sequenceName = "ecivil.seq_id_historico_preregistro", allocationSize = 1)
@Table(schema = "ecivil", name = "historico_preregistro")
public class HistoricoPreRegistro implements Entidade {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_historico_preregistro")
	@Column(name = "id_historico_preregistro")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_preregistro")
	private PreRegistro preRegistro;
	
	@Column(name = "id_usuario")
	private Long usuario;

	@Column(name = "situacao_preregistro")
	private String situacao;
	
	@Column(name = "cns_cartorio_registrado")
	private String cnsCartorio;
	
	@Column(name = "data_hora_alteracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraAlteracao;
	
	
	public String dataHoraAlteracaoFormatada() {
		if (getDataHoraAlteracao() == null) {
			return "-";
		}
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataHoraVisualizacaoRequisitante());
	}
	
	public Date getDataHoraAlteracao() {
		return this.dataHoraAlteracao;
	}
	
	public void setDataHoraAlteracao(Date dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PreRegistro getPreRegistro() {
		return preRegistro;
	}

	public void setPreRegistro(PreRegistro preRegistro) {
		this.preRegistro = preRegistro;
	}
	
	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public Date getDataHoraVisualizacaoRequisitante() {
		return dataHoraAlteracao;
	}

	public void setDataHoraVisualizacaoRequisitante(Date dataHoraAlteracao) {
		this.dataHoraAlteracao = dataHoraAlteracao;
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
		if (obj instanceof HistoricoPreRegistro) {
			return ((HistoricoPreRegistro) obj).getId().equals(this.id);
		}
		return false;
	}

	public String getCnsCartorio() {
		return cnsCartorio;
	}

	public void setCnsCartorio(String cnsCartorio) {
		this.cnsCartorio = cnsCartorio;
	}
}
