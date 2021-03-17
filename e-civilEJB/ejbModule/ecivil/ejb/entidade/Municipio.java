package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "municipio")
public class Municipio implements Entidade {
	
	@Id
	@Column(name = "id_municipio")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "id_uf")
	private Uf uf;

	@Temporal(TemporalType.DATE)
	@Column(name = "dt_fim")
	private Date dataFim;

	@Column(name = "cod_rfb")
	private String codRFB;
	
	@Column(name = "codigo_recompe")
	private String codigoRecompe;

	@Version
	@Column(name = "versao")
	private Integer versao;
	
	public boolean isNew() {
		return getId() == null;
	}

	public boolean isAtivo() {
		return getDataFim() == null;
	}
	
	public boolean possuiNome() {
		return StringUtils.isNotEmpty(getNome());
	}

	public boolean possuiUF() {
		return getUf() != null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getCodRFB() {
		return codRFB;
	}

	public void setCodRFB(String codRFB) {
		this.codRFB = codRFB;
	}

	public String getCodigoRecompe() {
		return codigoRecompe;
	}

	public void setCodigoRecompe(String codigoRecompe) {
		this.codigoRecompe = codigoRecompe;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
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
		if (obj instanceof Municipio) {
			return ((Municipio) obj).getId().equals(this.id);
		}
		return false;
	}
}