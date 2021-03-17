package ecivil.ejb.entidade;

import java.math.BigDecimal;

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

@SuppressWarnings("serial")
@Entity
@SequenceGenerator(name = "seq_id_valores_pedido_certidao", sequenceName = "ecivil.seq_id_valores_pedido_certidao", allocationSize = 1)
@Table(schema = "ecivil", name = "valores_pedido_certidao")
public class ValoresPedidoCertidao implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_valores_pedido_certidao")
	@Column(name = "id_valores_pedido_certidao")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;
	
	@Column(name = "codigo_corregedoria")
	private String codCorregedoria;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "valor_certidao")
	private BigDecimal valorCertidao;

	@Column(name = "valor_averbacao")
	private BigDecimal valorAverbacao;

	@Column(name = "valor_emolumentos_certidao")
	private BigDecimal valorEmolumentosCertidao;

	@Column(name = "valor_recompe_certidao")
	private BigDecimal valorRecompeCertidao;

	@Column(name = "valor_taxa_fiscalizacao_certidao")
	private BigDecimal valorTaxaFiscalizacaoCertidao;

	@Column(name = "valor_issqn_certidao")
	private BigDecimal valorIssqnCertidao;

	@Column(name = "valor_emolumentos_averbacao")
	private BigDecimal valorEmolumentosAverbacao;

	@Column(name = "valor_recompe_averbacao")
	private BigDecimal valorRecompeAverbacao;

	@Column(name = "valor_taxa_fiscalizacao_averbacao")
	private BigDecimal valorTaxaFiscalizacaoAverbacao;

	@Column(name = "valor_issqn_averbacao")
	private BigDecimal valorIssqnAverbacao;
	
	@Column(name = "aliquota_issqn")
	private BigDecimal aliquota;
	
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
	
	public String getCodCorregedoria() {
		return codCorregedoria;
	}

	public void setCodCorregedoria(String codCorregedoria) {
		this.codCorregedoria = codCorregedoria;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorCertidao() {
		return valorCertidao;
	}

	public void setValorCertidao(BigDecimal valorCertidao) {
		this.valorCertidao = valorCertidao;
	}

	public BigDecimal getValorAverbacao() {
		return valorAverbacao;
	}

	public void setValorAverbacao(BigDecimal valorAverbacao) {
		this.valorAverbacao = valorAverbacao;
	}

	public BigDecimal getValorEmolumentosCertidao() {
		return valorEmolumentosCertidao;
	}

	public void setValorEmolumentosCertidao(BigDecimal valorEmolumentosCertidao) {
		this.valorEmolumentosCertidao = valorEmolumentosCertidao;
	}

	public BigDecimal getValorRecompeCertidao() {
		return valorRecompeCertidao;
	}

	public void setValorRecompeCertidao(BigDecimal valorRecompeCertidao) {
		this.valorRecompeCertidao = valorRecompeCertidao;
	}

	public BigDecimal getValorTaxaFiscalizacaoCertidao() {
		return valorTaxaFiscalizacaoCertidao;
	}

	public void setValorTaxaFiscalizacaoCertidao(BigDecimal valorTaxaFiscalizacaoCertidao) {
		this.valorTaxaFiscalizacaoCertidao = valorTaxaFiscalizacaoCertidao;
	}

	public BigDecimal getValorIssqnCertidao() {
		return valorIssqnCertidao;
	}

	public void setValorIssqnCertidao(BigDecimal valorIssqnCertidao) {
		this.valorIssqnCertidao = valorIssqnCertidao;
	}

	public BigDecimal getValorEmolumentosAverbacao() {
		return valorEmolumentosAverbacao;
	}

	public void setValorEmolumentosAverbacao(BigDecimal valorEmolumentosAverbacao) {
		this.valorEmolumentosAverbacao = valorEmolumentosAverbacao;
	}

	public BigDecimal getValorRecompeAverbacao() {
		return valorRecompeAverbacao;
	}

	public void setValorRecompeAverbacao(BigDecimal valorRecompeAverbacao) {
		this.valorRecompeAverbacao = valorRecompeAverbacao;
	}

	public BigDecimal getValorTaxaFiscalizacaoAverbacao() {
		return valorTaxaFiscalizacaoAverbacao;
	}

	public void setValorTaxaFiscalizacaoAverbacao(BigDecimal valorTaxaFiscalizacaoAverbacao) {
		this.valorTaxaFiscalizacaoAverbacao = valorTaxaFiscalizacaoAverbacao;
	}

	public BigDecimal getValorIssqnAverbacao() {
		return valorIssqnAverbacao;
	}

	public void setValorIssqnAverbacao(BigDecimal valorIssqnAverbacao) {
		this.valorIssqnAverbacao = valorIssqnAverbacao;
	}
	
	public BigDecimal getAliquota() {
		return aliquota;
	}

	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
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
		if (obj instanceof ValoresPedidoCertidao) {
			return ((ValoresPedidoCertidao) obj).getId().equals(this.id);
		}
		return false;
	}

}
