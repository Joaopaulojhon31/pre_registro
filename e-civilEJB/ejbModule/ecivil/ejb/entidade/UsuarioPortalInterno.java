package ecivil.ejb.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;

@SuppressWarnings("serial")
@Entity
@Table(schema = "ecivil", name = "usuario_portal_interno")
@SequenceGenerator(name = "seq_id_usuario_portal_interno", sequenceName = "ecivil.seq_id_usuario_portal_interno", allocationSize = 1)
public class UsuarioPortalInterno implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_usuario_portal_interno")
	@Column(name = "id_usuario_portal_interno")
	private Long id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cpf")
	private String cpf;

	@Column(name = "email")
	private String email;

	@Column(name = "senha")
	private String senha;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_alteracao_senha")
	private Date dataAlteracaoSenha;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Column(name = "admin")
	private boolean isAdmin;

	@Version
	@Column(name = "versao")
	private Integer versao;
	
	@Version
	@Column(name = "status")
	private Integer status;
	
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<UsuarioServentia> usuarioServentia;

	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<UsuarioGrupoUsuario> usuarioGrupoUsuarios;
	
	@Transient
	private Municipio municipio;
	
	@Transient
	private String codigoCorregedoriaCadastroUsuario;
	
	@Transient
	private String codigoCorregedoriaSelecionado;
	
	@Transient
	private BigDecimal saldoAtual;
	
	@Transient
	private List<GrupoUsuario> gruposUsuario;

	@Transient
	private List<Recurso> listaRecursos;

	@Transient
	private String confirmarSenha;

	@Transient
	private String novaSenha;

	@Transient
	private String confirmarEmail;

	@Transient
	private Uf uf;

	@Transient
	private Boolean resetarSenha;

	public boolean isNew() {
		return getId() == null;
	}
	
	public boolean possuiSaldoAtualConsultado() {
		return saldoAtual != null;
	}
	
	public String cpfFormatado() {
		return Util.addMaskCPF(getCpf());
	}

	public void instanciaUsuarioGrupoUsuario() {
		if (usuarioGrupoUsuarios == null || usuarioGrupoUsuarios.size() == 0) {
			usuarioGrupoUsuarios = new ArrayList<UsuarioGrupoUsuario>();
		}
		UsuarioGrupoUsuario usuarioGrupoUsuario = new UsuarioGrupoUsuario();
		usuarioGrupoUsuario.setGrupoUsuario(new GrupoUsuario());
		usuarioGrupoUsuario.setUsuario(this);
		usuarioGrupoUsuarios.add(usuarioGrupoUsuario);
	}

	public String retornaSenhaCriptografada(String senha) {
		return CriptografiaUtil.retornaCriptografadoMD5(senha);
	}
	
	public String saldoAtualFormatado() {
		if (getSaldoAtual() != null) {
			return Util.formataMoeda(getSaldoAtual());
		}
		return "----";
	}
	
	public List<UsuarioServentia> getUsuarioServentiaAtiva() {
		return getUsuarioServentiaInicializada().stream().filter(serv -> serv.getDataFim() == null).collect(Collectors.toList());
	}
	
	public List<String> recuperaListaCodigoCorregedoriaUsuario(){
		return getUsuarioServentiaAtiva().stream().map(UsuarioServentia::getCodigoCorregedoria).collect(Collectors.toList());
	}

	public List<Long> recuperarListaSegGrupoUsuarios() {
		List<Long> listaIdGrupoUsuario = new ArrayList<Long>();
		if (usuarioGrupoUsuarios != null && usuarioGrupoUsuarios.size() > 0) {
			for (UsuarioGrupoUsuario usuarioGrupoUsuario : getUsuarioGrupoUsuarios()) {
				if (usuarioGrupoUsuario.getGrupoUsuario() != null && usuarioGrupoUsuario.getGrupoUsuario().getId() != null) {
					listaIdGrupoUsuario.add(usuarioGrupoUsuario.getGrupoUsuario().getId());
				}
			}
		}
		return listaIdGrupoUsuario;
	}

	public void inicializaGrupoUsuarios() {
		if (getUsuarioGrupoUsuarios() == null) {
			setUsuarioGrupoUsuarios(new ArrayList<UsuarioGrupoUsuario>());
		}
	}

	public List<UsuarioGrupoUsuario> getListaUsuarioGrupoUsuario() {
		inicializaGruposUsuarios();
		return getUsuarioGrupoUsuarios();
	}

	public void montaListaRecursos() {
		listaRecursos = new ArrayList<Recurso>();
		if (getUsuarioGrupoUsuarios() != null) {
			for (UsuarioGrupoUsuario usuarioGrupoUsuario : getUsuarioGrupoUsuarios()) {
				if (usuarioGrupoUsuario.getGrupoUsuario() != null) {
					GrupoUsuario grupoUsuario = usuarioGrupoUsuario.getGrupoUsuario();
					if (grupoUsuario.getGrupoUsuarioPermissoes() != null) {
						for (GrupoUsuarioPermissao grupoUsuarioPermissao : grupoUsuario.getGrupoUsuarioPermissoes()) {
							if (grupoUsuarioPermissao.getPermissao() != null && grupoUsuarioPermissao.getPermissao().getRecursos() != null) {
								for (Recurso recurso : grupoUsuarioPermissao.getPermissao().getRecursos()) {
									inicializaListaRecursos();
									getListaRecursos().add(recurso);
								}
							}
						}
					}
				}
			}
		}
	}

	public void inicializaListaRecursos() {
		if (getListaRecursos() == null) {
			setListaRecursos(new ArrayList<Recurso>());
		}
	}

	public void inicializaGruposUsuarios() {
		if (getGruposUsuario() == null) {
			setGruposUsuario(new ArrayList<GrupoUsuario>());
		}
	}

	public void montaGruposUsuario() {
		if (getUsuarioGrupoUsuarios() != null) {
			for (UsuarioGrupoUsuario usuarioGrupoUsuario : getUsuarioGrupoUsuarios()) {
				if (usuarioGrupoUsuario.getGrupoUsuario() != null) {
					GrupoUsuario grupoUsuario = usuarioGrupoUsuario.getGrupoUsuario();
					inicializaGruposUsuarios();
					getGruposUsuario().add(grupoUsuario);
				}
			}
		}
	}

	public boolean possuiRecursoPorDescricao(String descricao) {
		if (getListaRecursos() == null) {
			montaListaRecursos();
		}
		for (Recurso recurso : getListaRecursos()) {
			if (descricao != null && descricao.equals(recurso.getDescricao().trim())) {
				return true;
			}
		}
		return false;
	}

	public boolean possuiGrupoPorDescricao(String descricao) {
		if (getGruposUsuario() == null || getGruposUsuario().size() == 0) {
			montaGruposUsuario();
		}

		if (gruposUsuario == null) {
			return Boolean.FALSE;
		}

		for (GrupoUsuario grupoUsuario : getGruposUsuario()) {
			if (descricao != null && descricao.equals(grupoUsuario.getDescricao().trim())) {
				return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}

	public boolean possuiNome() {
		return Util.ehStringValida(getNome());
	}

	public boolean possuiCpf() {
		return Util.ehStringValida(getCpf());
	}

	public boolean possuiSenha() {
		return Util.ehStringValida(getSenha());
	}
	
	public boolean possuiNovaSenha() {
		return Util.ehStringValida(getNovaSenha());
	}

	public boolean possuiConfirmarSenha() {
		return Util.ehStringValida(getConfirmarSenha());
	}

	public boolean possuiEmail() {
		return Util.ehStringValida(getEmail());
	}

	public boolean possuiConfirmarEmail() {
		return Util.ehStringValida(getConfirmarEmail());
	}

	public boolean possuiMunicipio() {
		return getMunicipio() != null;
	}

	public boolean possuiUF() {
		return possuiMunicipio() && getMunicipio().getUf() != null;
	}
	
	public boolean possuiServentiaSelecionado() {
		return Util.ehStringValida(getCodigoCorregedoriaSelecionado());
	}
	
	public boolean possuiApenasUmaServentia() {
		return getUsuarioServentiaAtiva().size() <= 1;
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

	public Date getDataAlteracaoSenha() {
		return dataAlteracaoSenha;
	}

	public void setDataAlteracaoSenha(Date dataAlteracaoSenha) {
		this.dataAlteracaoSenha = dataAlteracaoSenha;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	
	public boolean isPermissaoConsulta() {
		return possuiGrupoPorDescricao(GrupoUsuario.CONSULTA);
	}
	
	public boolean isPermissaoOficial() {
		return possuiGrupoPorDescricao(GrupoUsuario.OFICIAL);
	}
	
	public boolean isPermissaoFuncionario() {
		return possuiGrupoPorDescricao(GrupoUsuario.FUNCIONARIO);
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public String getCodigoCorregedoriaCadastroUsuario() {
		return codigoCorregedoriaCadastroUsuario;
	}

	public void setCodigoCorregedoriaCadastroUsuario(String codigoCorregedoriaCadastroUsuario) {
		this.codigoCorregedoriaCadastroUsuario = codigoCorregedoriaCadastroUsuario;
	}

	public String getCodigoCorregedoriaSelecionado() {
		return codigoCorregedoriaSelecionado;
	}

	public void setCodigoCorregedoriaSelecionado(String codigoCorregedoriaSelecionado) {
		this.codigoCorregedoriaSelecionado = codigoCorregedoriaSelecionado;
	}
	
	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
	public List<UsuarioServentia> getUsuarioServentiaInicializada() {
		if (usuarioServentia == null) {
			usuarioServentia = new ArrayList<>();
		}
		return usuarioServentia;
	}
	
	public List<UsuarioServentia> getUsuarioServentia() {
		return usuarioServentia;
	}

	public void setUsuarioServentia(List<UsuarioServentia> usuarioServentia) {
		this.usuarioServentia = usuarioServentia;
	}
	
	public List<UsuarioGrupoUsuario> getUsuarioGrupoUsuariosInicializado() {
		if (usuarioGrupoUsuarios == null) {
			usuarioGrupoUsuarios = new ArrayList<UsuarioGrupoUsuario>();
		}
		return usuarioGrupoUsuarios;
	}

	public List<UsuarioGrupoUsuario> getUsuarioGrupoUsuarios() {
		return usuarioGrupoUsuarios;
	}

	public void setUsuarioGrupoUsuarios(List<UsuarioGrupoUsuario> usuarioGrupoUsuarios) {
		this.usuarioGrupoUsuarios = usuarioGrupoUsuarios;
	}
	
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<GrupoUsuario> getGruposUsuario() {
		return gruposUsuario;
	}

	public void setGruposUsuario(List<GrupoUsuario> gruposUsuario) {
		this.gruposUsuario = gruposUsuario;
	}

	public List<Recurso> getListaRecursos() {
		return listaRecursos;
	}

	public void setListaRecursos(List<Recurso> listaRecursos) {
		this.listaRecursos = listaRecursos;
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

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public Boolean getResetarSenha() {
		return resetarSenha;
	}

	public void setResetarSenha(Boolean resetarSenha) {
		this.resetarSenha = resetarSenha;
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
		if (obj instanceof UsuarioPortalInterno) {
			return ((UsuarioPortalInterno) obj).getId().equals(this.id);
		}
		return false;
	}

}
