package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(schema = "ecivil", name = "parametro")
public class Parametro {

	public static final String ENVIAR_EMAIL = "ENVIAR_EMAIL";
	public static final String EMAIL_SERVIDOR_SMTP = "EMAIL_SERVIDOR_SMTP";
	public static final String EMAIL_PORTA_SERVIDOR_SMTP = "EMAIL_PORTA_SERVIDOR_SMTP";
	public static final String EMAIL_NOME_REMETENTE = "EMAIL_NOME_REMETENTE";
	public static final String EMAIL_USUARIO = "EMAIL_USUARIO";
	public static final String EMAIL_SENHA = "EMAIL_SENHA";
	public static final String TEMPO_ESPERA_NOVA_SENHA = "TEMPO_ESPERA_NOVA_SENHA";
	public static final String CAMINHO_CERTIFICADO = "CAMINHO_CERTIFICADO";
	public static final String SENHA_CERTIFICADO_A1 = "SENHA_CERTIFICADO_A1";
	public static final String SIGLA_ESTADO_SEDE = "SIGLA_ESTADO_SEDE";
	public static final String URL_BASE_MIGRACAO_RECOMPE = "URL_BASE_MIGRACAO_RECOMPE";
	public static final String URL_BASE_INTEGRADOR_CRC = "URL_BASE_INTEGRADOR_CRC";
	public static final String URL_BASE_INTEGRADOR_MATERNIDADE = "URL_BASE_INTEGRADOR_MATERNIDADE";
	public static final String URL_BASE_RECOMPE = "URL_BASE_RECOMPE";
	public static final String URL_BASE_CONTA_DIGITAL = "URL_BASE_CONTA_DIGITAL";
	public static final String URL_BASE_INTEGRADOR_CORREIOS = "URL_BASE_INTEGRADOR_CORREIOS";
	public static final String CODIGO_CARTEIRA_EMISSAO_BOLETO = "CODIGO_CARTEIRA_EMISSAO_BOLETO";
	public static final String QTD_DIAS_VENCIMENTO_BOLETO = "QTD_DIAS_VENCIMENTO_BOLETO";
	public static final String VALOR_TAXA_MANUNTENCAO = "VALOR_TAXA_MANUNTENCAO";
	public static final String VALOR_MINIMO_SAQUE = "VALOR_MINIMO_SAQUE";
	public static final String VALOR_MINIMO_ADD_CREDITO = "VALOR_MINIMO_ADD_CREDITO";
	public static final String RODAR_CRON_ATUALIZA_LOOKUP_CARTORIOS = "RODAR_CRON_ATUALIZA_LOOKUP_CARTORIOS";
	public static final String URL_BASE_PREREGISTRO_XML = "URL_BASE_PREREGISTRO_XML";
	
	@Id
	@Column(name = "id_parametro")
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "valor")
	private String valor;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "habilitado")
	private Boolean habilitado;

	@Version
	@Column(name = "versao")
	private Integer versao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
