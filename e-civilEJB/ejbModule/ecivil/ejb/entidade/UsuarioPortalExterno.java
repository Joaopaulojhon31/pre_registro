package ecivil.ejb.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.util.CriptografiaUtil;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "usuario_portal_externo")
@SequenceGenerator(name = "seq_id_usuario_portal_externo", sequenceName = "ecivil.seq_id_usuario_portal_externo", allocationSize = 1)
public class UsuarioPortalExterno implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_usuario_portal_externo")
	@Column(name = "id_usuario_portal_externo")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cpf")
	private String cpf;

	@Column(name = "email")
	private String email;

	@Column(name = "senha")
	private String senha;

	@Column(name = "telefone")
	private String telefone;

	@Column(name = "celular")
	private String celular;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_alteracao_senha")
	private Date dataAlteracaoSenha;

	@Column(name = "pendente_atualizacao")
	private boolean pendenteAtualizacao;
	
	@Column(name = "termo_aceite")
	private boolean termoAceite;
	
	@OrderBy(value = "dataInicio")
	@OneToMany(mappedBy = "usuarioPortalExterno", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<EnderecoUsuarioExterno> listaEndereco;
	
	@Version
	@Column(name = "versao")
	private Integer versao;
	
	@Transient
	private String confirmarSenha;
	
	@Transient
	private String novaSenha;

	@Transient
	private String confirmarEmail;
	
	@Transient
	private EnderecoUsuarioExterno enderecoUsuarioExterno;

	public boolean isEdicao() {
		return getId() != null;
	}
	
	public boolean possuiNome() {
		return StringUtils.isNotEmpty(getNome());
	}

	public boolean possuiCpf() {
		return StringUtils.isNotEmpty(getCpf());
	}

	public boolean possuiSenha() {
		return StringUtils.isNotEmpty(getSenha());
	}

	public boolean possuiConfirmarSenha() {
		return StringUtils.isNotEmpty(getConfirmarSenha());
	}

	public boolean possuiEmail() {
		return StringUtils.isNotEmpty(getEmail());
	}

	public boolean possuiConfirmarEmail() {
		return StringUtils.isNotEmpty(getConfirmarEmail());
	}
	
	public boolean possuiCelular() {
		return StringUtils.isNotEmpty(getCelular());
	}

	public boolean possuiTelefone() {
		return StringUtils.isNotEmpty(getTelefone());
	}

	public boolean possuiNovaSenha() {
		return StringUtils.isNotEmpty(getNovaSenha());
	}
	
	public boolean possuiEndereco() {
		return getListaEndereco() != null && !getListaEndereco().isEmpty();
	}
	
	public String retornaSenhaCriptografada(String senha) {
		return CriptografiaUtil.retornaCriptografadoMD5(senha);
	}
	
	public EnderecoUsuarioExterno getEnderecoPadraoUsuarioExterno() {
		return getListaEnderecoAtivo().stream().filter(end -> end.isEnderecoPadrao()).findFirst().orElse(getListaEnderecoAtivo().get(0));
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Date getDataAlteracaoSenha() {
		return dataAlteracaoSenha;
	}

	public void setDataAlteracaoSenha(Date dataAlteracaoSenha) {
		this.dataAlteracaoSenha = dataAlteracaoSenha;
	}
	
	public boolean isPendenteAtualizacao() {
		return pendenteAtualizacao;
	}

	public void setPendenteAtualizacao(boolean pendenteAtualizacao) {
		this.pendenteAtualizacao = pendenteAtualizacao;
	}
	
	public boolean isTermoAceite() {
		return termoAceite;
	}

	public void setTermoAceite(boolean termoAceite) {
		this.termoAceite = termoAceite;
	}

	public List<EnderecoUsuarioExterno> getListaEnderecoInicializada() {
		if (listaEndereco == null) {
			listaEndereco = new ArrayList<>();
		}
		return listaEndereco;
	}
	
	public List<EnderecoUsuarioExterno> getListaEnderecoAtivo() {
		return getListaEnderecoInicializada().stream().filter(end -> end.getDataFim() == null).collect(Collectors.toList());
	}
	
	public List<EnderecoUsuarioExterno> getListaEndereco() {
		return listaEndereco;
	}

	public void setListaEndereco(List<EnderecoUsuarioExterno> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}
	
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	
	public String getConfirmarEmail() {
		return confirmarEmail;
	}

	public void setConfirmarEmail(String confirmarEmail) {
		this.confirmarEmail = confirmarEmail;
	}
	
	public EnderecoUsuarioExterno getEnderecoUsuarioExterno() {
		return enderecoUsuarioExterno;
	}

	public void setEnderecoUsuarioExterno(EnderecoUsuarioExterno enderecoUsuarioExterno) {
		this.enderecoUsuarioExterno = enderecoUsuarioExterno;
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
		if (obj instanceof UsuarioPortalExterno) {
			return ((UsuarioPortalExterno) obj).getId().equals(this.id);
		}
		return false;
	}
	
}
