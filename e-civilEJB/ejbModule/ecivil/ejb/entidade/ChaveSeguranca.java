package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name = "seq_id_chave_seguranca", sequenceName = "ecivil.seq_id_chave_seguranca", allocationSize = 1)
@Table(schema = "ecivil", name = "chave_seguranca")
public class ChaveSeguranca {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_chave_seguranca")
	@Column(name = "id_chave_seguranca")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;

	@Column(name = "chave_seguranca")
	private String chaveSeguranca;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_download")
	private Date dataDownload;

	@Column(name = "id_usuario")
	private Long seqUsuario;

	@Column(name = "data_fim")
	private Date dataFim;

	@Column(name = "protocolo_via_unica")
	private String protocoloViaUnica;

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

	public String getChaveSeguranca() {
		return chaveSeguranca;
	}

	public void setChaveSeguranca(String chaveSeguranca) {
		this.chaveSeguranca = chaveSeguranca;
	}

	public Date getDataDownload() {
		return dataDownload;
	}

	public void setDataDownload(Date dataDownload) {
		this.dataDownload = dataDownload;
	}

	public Long getSeqUsuario() {
		return seqUsuario;
	}

	public void setSeqUsuario(Long seqUsuario) {
		this.seqUsuario = seqUsuario;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getProtocoloViaUnica() {
		return protocoloViaUnica;
	}

	public void setProtocoloViaUnica(String protocoloViaUnica) {
		this.protocoloViaUnica = protocoloViaUnica;
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
		if (obj instanceof ChaveSeguranca) {
			return ((ChaveSeguranca) obj).getId().equals(this.id);
		}
		return false;
	}

}
