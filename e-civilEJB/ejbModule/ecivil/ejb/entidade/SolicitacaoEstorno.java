package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import ecivil.ejb.enumeration.EnumSituacaoSolicitacaoEstorno;

@Entity
@SequenceGenerator(name = "seq_id_solicitacao_estorno", sequenceName = "ecivil.seq_id_solicitacao_estorno", allocationSize = 1)
@Table(schema = "ecivil", name = "solicitacao_estorno")
public class SolicitacaoEstorno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_solicitacao_estorno")
	@Column(name = "id_solicitacao_estorno")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_solicitacao")
	private Date dataSolicitacao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "codigo_situacao")
	private EnumSituacaoSolicitacaoEstorno situacao;
	
	@Column(name = "descricao_solicitacao")
	private String descricaoSolicitacao;
	
	@Column(name = "descricao_rejeicao")
	private String descricaoRejeicao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_resposta")
	private Date dataResposta;

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

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public EnumSituacaoSolicitacaoEstorno getSituacao() {
		return situacao;
	}

	public void setSituacao(EnumSituacaoSolicitacaoEstorno situacao) {
		this.situacao = situacao;
	}

	public String getDescricaoSolicitacao() {
		return descricaoSolicitacao;
	}

	public void setDescricaoSolicitacao(String descricaoSolicitacao) {
		this.descricaoSolicitacao = descricaoSolicitacao;
	}

	public String getDescricaoRejeicao() {
		return descricaoRejeicao;
	}

	public void setDescricaoRejeicao(String descricaoRejeicao) {
		this.descricaoRejeicao = descricaoRejeicao;
	}

	public Date getDataResposta() {
		return dataResposta;
	}

	public void setDataResposta(Date dataResposta) {
		this.dataResposta = dataResposta;
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
		if (obj instanceof SolicitacaoEstorno) {
			return ((SolicitacaoEstorno) obj).getId().equals(this.id);
		}
		return false;
	}
	
}
