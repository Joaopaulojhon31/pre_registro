package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ecivil.ejb.enumeration.EnumTipoConta;

@Entity
@SequenceGenerator(name = "seq_id_conta_alternativa", sequenceName = "ecivil.seq_id_conta_alternativa", allocationSize = 1)
@Table(schema = "ecivil", name = "conta_alternativa")
public class ContaAlternativa implements Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_conta_alternativa")
	@Column(name = "id_conta_alternativa")
	private Long id;

	@Column(name = "codigo_corregedoria")
	private String codigoCorregedoria;

	@Column(name = "nome_titular")
	private String nomeTitular;

	@Column(name = "cpf_cnpj_titular")
	private String cpfCnpjTitular;

	@Column(name = "codigo_banco")
	private String codigoBanco;

	@Column(name = "nome_banco")
	private String nomeBanco;

	@Column(name = "agencia")
	private String agencia;

	@Column(name = "conta")
	private String conta;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_conta")
	private EnumTipoConta tipoConta;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoCorregedoria() {
		return codigoCorregedoria;
	}

	public void setCodigoCorregedoria(String codigoCorregedoria) {
		this.codigoCorregedoria = codigoCorregedoria;
	}

	public String getNomeTitular() {
		return nomeTitular;
	}

	public void setNomeTitular(String nomeTitular) {
		this.nomeTitular = nomeTitular;
	}

	public String getCpfCnpjTitular() {
		return cpfCnpjTitular;
	}

	public void setCpfCnpjTitular(String cpfCnpjTitular) {
		this.cpfCnpjTitular = cpfCnpjTitular;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public EnumTipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(EnumTipoConta tipoConta) {
		this.tipoConta = tipoConta;
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

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof ContaAlternativa) {
			return ((ContaAlternativa) obj).getId().equals(this.id);
		}
		return false;
	}

}
