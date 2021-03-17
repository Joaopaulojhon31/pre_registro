package ecivil.ejb.entidade;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "xml_preregistro", schema = "ecivil")
@SequenceGenerator(name = "seq_id_xml", sequenceName = "seq_id_xml", schema = "ecivil", allocationSize = 1)
public class PreRegistroXML {
	
	@Column(name = "xml")
	private String xml;
	
	@Id
	@Column(name = "id_xml_pre_registro")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_xml")
	private Long idXmlPreRegistro;
	
	@Column(name = "id_pre_registro")
	private Long idPreRegistro;

	@Column(name = "data_inicio")
	private Date dataInicio;
	
	@Column(name = "data_alteracao")
	private Date dataAlteracao;
	
	@Column(name = "data_fim")
	private Date dataFim;
	
	@Column(name = "corregedoria")
	private String corredoria;
	
	@Column(name = "id_unidade_interligada")
	private String idUnidadeInterligada;
	
	public Long getIdPreRegistro() {
		return idPreRegistro;
	}

	public void setIdPreRegistro(Long idPreRegistro) {
		this.idPreRegistro = idPreRegistro;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Long getIdXmlPreRegistro() {
		return idXmlPreRegistro;
	}

	public void setIdXmlPreRegistro(Long idPreRegistro) {
		this.idXmlPreRegistro = idPreRegistro;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getCorredoria() {
		return corredoria;
	}

	public void setCorredoria(String corredoria) {
		this.corredoria = corredoria;
	}

	public String getIdUnidadeInterligada() {
		return idUnidadeInterligada;
	}

	public void setIdUnidadeInterligada(String idUnidadeInterligada) {
		this.idUnidadeInterligada = idUnidadeInterligada;
	}

}
