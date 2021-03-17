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
@SequenceGenerator(name = "seq_id_historico_pedido_certidao", sequenceName = "ecivil.seq_id_historico_pedido_certidao", allocationSize = 1)
@Table(schema = "ecivil", name = "historico_pedido_certidao")
public class HistoricoPedidoCertidao implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_historico_pedido_certidao")
	@Column(name = "id_historico_pedido_certidao")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_portal_interno")
	private UsuarioPortalInterno usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_situacao_pedido_certidao")
	private SituacaoPedidoCertidao situacao;

	@Column(name = "data_inicio")
	private Date dataInicio;

	@Column(name = "data_fim")
	private Date dataFim;
	
	@Column(name = "data_hora_visualizacao_requisitante")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraVisualizacaoRequisitante;
	
	@Column(name = "data_hora_visualizacao_requisitado")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataHoraVisualizacaoRequisitado;
	
	
	public String dataInicioFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataInicio());
	}
	
	public String dataHoraVisualizacaoRequisitanteFormatada() {
		if (getDataHoraVisualizacaoRequisitante() == null) {
			return "-";
		}
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataHoraVisualizacaoRequisitante());
	}
	
	public String dataHoraVisualizacaoRequisitadoFormatada() {
		if (getDataHoraVisualizacaoRequisitado() == null) {
			return "-";
		}
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataHoraVisualizacaoRequisitado());
	}
	
	public boolean isEmAberto() {
		return getDataFim() == null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}
	
	public UsuarioPortalInterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalInterno usuario) {
		this.usuario = usuario;
	}

	public SituacaoPedidoCertidao getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPedidoCertidao situacao) {
		this.situacao = situacao;
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
	
	public Date getDataHoraVisualizacaoRequisitante() {
		return dataHoraVisualizacaoRequisitante;
	}

	public void setDataHoraVisualizacaoRequisitante(Date dataHoraVisualizacaoRequisitante) {
		this.dataHoraVisualizacaoRequisitante = dataHoraVisualizacaoRequisitante;
	}

	public Date getDataHoraVisualizacaoRequisitado() {
		return dataHoraVisualizacaoRequisitado;
	}

	public void setDataHoraVisualizacaoRequisitado(Date dataHoraVisualizacaoRequisitado) {
		this.dataHoraVisualizacaoRequisitado = dataHoraVisualizacaoRequisitado;
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
		if (obj instanceof HistoricoPedidoCertidao) {
			return ((HistoricoPedidoCertidao) obj).getId().equals(this.id);
		}
		return false;
	}

}
