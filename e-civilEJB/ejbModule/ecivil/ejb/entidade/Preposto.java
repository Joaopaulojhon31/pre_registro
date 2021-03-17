package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name = "preposto_pre_idpreposto_seq", sequenceName = "ecivil.preposto_pre_idpreposto_seq", allocationSize = 1)
@Table(name = "preposto", schema = "ecivil")
public class Preposto {

	@Id
	@Column(name = "pre_idpreposto")
	private Integer idPreposto;
	
	@Column(name = "ui_idui")
	private Integer idUi;

	@Column(name = "pre_nome")
	private String nomePreposto;
	
	@Column(name = "pre_cpf")
	private String cpfPreposto;
	
	@Column(name = "pre_chavepublica")
	private String chavePublica; 
	
	@Temporal(TemporalType.DATE)
	@Column(name = "pre_statusdtalteracao")
	private Date statusDtAlteracao;
	
	@Column(name = "cnj_id")
	private Integer cnjId; 
	
	@Column(name = "pre_status")
	private Integer statusPreposto;

	public Integer getIdPreposto() {
		return idPreposto;
	}

	public Integer getIdUi() {
		return idUi;
	}

	public String getNomePreposto() {
		return nomePreposto;
	}

	public String getCpfPreposto() {
		return cpfPreposto;
	}

	public String getChavePublica() {
		return chavePublica;
	}

	public Date getStatusDtAlteracao() {
		return statusDtAlteracao;
	}

	public Integer getCnjId() {
		return cnjId;
	}

	public Integer getStatusPreposto() {
		return statusPreposto;
	}

	public void setIdPreposto(Integer idPreposto) {
		this.idPreposto = idPreposto;
	}

	public void setIdUi(Integer idUi) {
		this.idUi = idUi;
	}

	public void setNomePreposto(String nomePreposto) {
		this.nomePreposto = nomePreposto;
	}

	public void setCpfPreposto(String cpfPreposto) {
		this.cpfPreposto = cpfPreposto;
	}

	public void setChavePublica(String chavePublica) {
		this.chavePublica = chavePublica;
	}

	public void setStatusDtAlteracao(Date statusDtAlteracao) {
		this.statusDtAlteracao = statusDtAlteracao;
	}

	public void setCnjId(Integer cnjId) {
		this.cnjId = cnjId;
	}

	public void setStatusPreposto(Integer statusPreposto) {
		this.statusPreposto = statusPreposto;
	}
}

