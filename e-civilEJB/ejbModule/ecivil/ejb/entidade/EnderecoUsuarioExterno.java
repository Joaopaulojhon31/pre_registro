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
import javax.persistence.Version;

import ecivil.ejb.util.Util;

@Entity
@SequenceGenerator(name = "seq_id_endereco_usuario_externo", sequenceName = "ecivil.seq_id_endereco_usuario_externo", allocationSize = 1)
@Table(schema = "ecivil", name = "endereco_usuario_externo")
public class EnderecoUsuarioExterno implements Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_endereco_usuario_externo")
	@Column(name = "id_endereco_usuario_externo")
	private Long id;

	@Column(name = "nome_logradouro")
	private String nomeLogradouro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_logradouro")
	private TipoLogradouro tipoLogradouro;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "numero_logradouro")
	private String numeroLogradouro;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "cep")
	private String cep;
	
	@Column(name = "referencia")
	private String referencia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio")
	private Municipio municipio;
	
	@Column(name = "identificador_endereco")
	private String identificadorEndereco;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;
	
	@Column(name = "endereco_padrao")
	private boolean enderecoPadrao;
	
	@Column(name = "destinatario")
	private String destinatario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_portal_externo", nullable = false)
	private UsuarioPortalExterno usuarioPortalExterno;
	
	@Version
	@Column(name = "versao")
	private Long versao;
	
	@Transient
	private Uf uf;
	
	@Transient
	private boolean edicaoEndereco;
	
	public String obtemDescricaoEndereco() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getTipoLogradouro().getNome());
		sb.append(" ");
		sb.append(getNomeLogradouro());
		sb.append(", ");
		sb.append(getNumeroLogradouro());
		sb.append(", ");

		if (Util.ehStringValida(getComplemento())) {
			sb.append(getComplemento());
			sb.append(", ");
		}
		
		sb.append(getBairro());
		sb.append(", ");
		sb.append(getMunicipio().getNome());
		sb.append(" - ");
		sb.append(getMunicipio().getUf().getSigla());
		sb.append(" - ");
		sb.append(Util.addMaskCEP(getCep()));
		
		if (Util.ehStringValida(getReferencia())) {
			sb.append(" - ");
			sb.append(getReferencia());
		}
				
		return sb.toString();
	}
	
	public boolean possuiMunicipio() {
		return getMunicipio() != null;
	}

	public boolean possuiTipoLogradouro() {
		return getTipoLogradouro() != null;
	}

	public boolean possuiNomeLogradouro() {
		return Util.ehStringValida(getNomeLogradouro());
	}

	public boolean possuiCep() {
		return Util.ehStringValida(getCep());
	}

	public boolean possuiNumeroLogradouro() {
		return Util.ehStringValida(getNumeroLogradouro());
	}

	public boolean possuiBairro() {
		return Util.ehStringValida(getBairro());
	}

	public boolean possuiUF() {
		return possuiMunicipio() && getMunicipio().getUf() != null;
	}
	
	public boolean possuiDestinatario() {
		return Util.ehStringValida(getDestinatario());
	}
	
	public boolean possuiIdentificador() {
		return Util.ehStringValida(getIdentificadorEndereco());
	}

	public EnderecoUsuarioExterno instanciaNovoObjetoEndereco() {
		EnderecoUsuarioExterno novoObj = new EnderecoUsuarioExterno();
		novoObj.setIdentificadorEndereco(this.identificadorEndereco);
		novoObj.setBairro(this.bairro);
		novoObj.setCep(this.cep);
		novoObj.setComplemento(this.complemento);
		novoObj.setDataFim(this.dataFim);
		novoObj.setDataInicio(this.dataInicio);
		novoObj.setEnderecoPadrao(this.enderecoPadrao);
		novoObj.setId(this.id);
		novoObj.setMunicipio(this.municipio);
		novoObj.setNomeLogradouro(this.nomeLogradouro);
		novoObj.setNumeroLogradouro(this.numeroLogradouro);
		novoObj.setTipoLogradouro(this.tipoLogradouro);
		novoObj.setUf(this.uf);
		novoObj.setUsuarioPortalExterno(this.usuarioPortalExterno);
		novoObj.setDestinatario(this.destinatario);
		return novoObj;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeLogradouro() {
		return nomeLogradouro;
	}

	public void setNomeLogradouro(String nomeLogradouro) {
		this.nomeLogradouro = nomeLogradouro;
	}

	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumeroLogradouro() {
		return numeroLogradouro;
	}

	public void setNumeroLogradouro(String numeroLogradouro) {
		this.numeroLogradouro = numeroLogradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
	public String getIdentificadorEndereco() {
		return identificadorEndereco;
	}

	public void setIdentificadorEndereco(String identificadorEndereco) {
		this.identificadorEndereco = identificadorEndereco;
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

	public boolean isEnderecoPadrao() {
		return enderecoPadrao;
	}

	public void setEnderecoPadrao(boolean enderecoPadrao) {
		this.enderecoPadrao = enderecoPadrao;
	}
	
	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public UsuarioPortalExterno getUsuarioPortalExterno() {
		return usuarioPortalExterno;
	}

	public void setUsuarioPortalExterno(UsuarioPortalExterno usuarioPortalExterno) {
		this.usuarioPortalExterno = usuarioPortalExterno;
	}
	
	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}
	
	public boolean isEdicaoEndereco() {
		return edicaoEndereco;
	}

	public void setEdicaoEndereco(boolean edicaoEndereco) {
		this.edicaoEndereco = edicaoEndereco;
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
		if (obj instanceof EnderecoUsuarioExterno) {
			return ((EnderecoUsuarioExterno) obj).getId().equals(this.id);
		}
		return false;
	}

}
