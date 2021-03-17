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
import javax.persistence.Transient;

import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@Entity
@SequenceGenerator(name = "seq_id_certidao_negativa", sequenceName = "ecivil.seq_id_certidao_negativa", allocationSize = 1)
@Table(schema = "ecivil", name = "certidao_negativa")
public class CertidaoNegativa {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_certidao_negativa")
	@Column(name = "id_certidao_negativa")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_certidao")
	private TipoCertidao tipoCertidao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio")
	private Municipio municipio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private UsuarioPortalInterno usuarioInterno;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_gravacao")
	private Date dataGravacao;

	@Column(name = "codigo_hash")
	private String codigoHash;

	@Column(name = "codigo_corregedoria")
	private String codCorregedoria;

	@Column(name = "nome_primeira_pessoa")
	private String nomePrimeiraPessoa;

	@Column(name = "nome_segunda_pessoa")
	private String nomeSegundaPessoa;

	@Column(name = "ano")
	private String ano;

	@Transient
	private CartorioResponse cartorio;
	
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

	public TipoCertidao getTipoCertidao() {
		return tipoCertidao;
	}

	public void setTipoCertidao(TipoCertidao tipoCertidao) {
		this.tipoCertidao = tipoCertidao;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public UsuarioPortalInterno getUsuarioInterno() {
		return usuarioInterno;
	}

	public void setUsuarioInterno(UsuarioPortalInterno usuarioInterno) {
		this.usuarioInterno = usuarioInterno;
	}

	public Date getDataGravacao() {
		return dataGravacao;
	}

	public void setDataGravacao(Date dataGravacao) {
		this.dataGravacao = dataGravacao;
	}

	public String getCodigoHash() {
		return codigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	public String getCodCorregedoria() {
		return codCorregedoria;
	}

	public void setCodCorregedoria(String codCorregedoria) {
		this.codCorregedoria = codCorregedoria;
	}

	public String getNomePrimeiraPessoa() {
		return nomePrimeiraPessoa;
	}

	public void setNomePrimeiraPessoa(String nomePrimeiraPessoa) {
		this.nomePrimeiraPessoa = nomePrimeiraPessoa;
	}

	public String getNomeSegundaPessoa() {
		return nomeSegundaPessoa;
	}

	public void setNomeSegundaPessoa(String nomeSegundaPessoa) {
		this.nomeSegundaPessoa = nomeSegundaPessoa;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}
	
	public CartorioResponse getCartorio() {
		return cartorio;
	}

	public void setCartorio(CartorioResponse cartorio) {
		this.cartorio = cartorio;
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
		if (obj instanceof CertidaoNegativa) {
			return ((CertidaoNegativa) obj).getId().equals(this.id);
		}
		return false;
	}

}
