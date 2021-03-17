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

@Entity
@SequenceGenerator(name = "seq_id_anexo_certidao", sequenceName = "ecivil.seq_id_anexo_certidao", allocationSize = 1)
@Table(schema = "ecivil", name = "anexo_certidao")
public class AnexoCertidao {
	
	public static final String TIPO_EXTENSAO_PDF = "PDF";
	public static final String TIPO_EXTENSAO_XML = "XML";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_anexo_certidao")
	@Column(name = "id_anexo_certidao")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;

	@Column(name = "certidao")
	private byte[] certidao;
	
	@Column(name = "tipo_extensao")
	private String tipoExtensao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_gravacao")
	private Date dataGravacao;

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

	public byte[] getCertidao() {
		return certidao;
	}

	public void setCertidao(byte[] certidao) {
		this.certidao = certidao;
	}
	
	public String getTipoExtensao() {
		return tipoExtensao;
	}

	public void setTipoExtensao(String tipoExtensao) {
		this.tipoExtensao = tipoExtensao;
	}

	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

}
