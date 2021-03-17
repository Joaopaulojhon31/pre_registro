package ecivil.ejb.entidade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ecivil.ejb.util.DataUtil;

@Entity
@SequenceGenerator(name = "seq_id_solicitacao_credito", sequenceName = "ecivil.seq_id_solicitacao_credito", allocationSize = 1)
@Table(schema = "ecivil", name = "solicitacao_credito")
public class SolicitacaoCredito implements Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_solicitacao_credito")
	@Column(name = "id_solicitacao_credito")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private UsuarioPortalInterno usuario;

	@Column(name = "codigo_corregedoria")
	private String codigoCorregedoria;

	@Column(name = "valor")
	private BigDecimal valor;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;
	
	@Column(name = "migracao")
	private Boolean migracao;
	
	@OneToMany(mappedBy = "solicitacaoCredito", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<VinculoBoleto> listaVinculoBoleto;

	public String dataInicioFormatada() {
		return DataUtil.converteDateParaString_DDMMYYY_HHMMSS_BARRA(getDataInicio());
	}
	
	public VinculoBoleto recuperaVinculoBoletoAtivo() {
		if (getListaVinculoBoleto() == null) {
			return null;
		}
		return getListaVinculoBoleto().stream().filter(VinculoBoleto::isAtivo).findFirst().orElse(null);
	}
	
	public String recuperaNumeroBoleto() {
		VinculoBoleto vinculoBoleto = recuperaVinculoBoletoAtivo();
		return vinculoBoleto != null ? vinculoBoleto.getNumeroBoleto() : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsuarioPortalInterno getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPortalInterno usuario) {
		this.usuario = usuario;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
	
	public Boolean getMigracao() {
		return migracao;
	}

	public void setMigracao(Boolean migracao) {
		this.migracao = migracao;
	}

	public List<VinculoBoleto> getListaVinculoBoletoInicializada() {
		if (listaVinculoBoleto == null) {
			listaVinculoBoleto = new ArrayList<>();
		}
		return listaVinculoBoleto;
	}
	
	public List<VinculoBoleto> getListaVinculoBoleto() {
		return listaVinculoBoleto;
	}

	public void setListaVinculoBoleto(List<VinculoBoleto> listaVinculoBoleto) {
		this.listaVinculoBoleto = listaVinculoBoleto;
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
		if (obj instanceof SolicitacaoCredito) {
			return ((SolicitacaoCredito) obj).getId().equals(this.id);
		}
		return false;
	}

}
