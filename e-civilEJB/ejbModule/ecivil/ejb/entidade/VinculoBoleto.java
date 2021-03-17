package ecivil.ejb.entidade;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

@Entity
@SequenceGenerator(name = "seq_id_vinculo_boleto", sequenceName = "ecivil.seq_id_vinculo_boleto", allocationSize = 1)
@Table(schema = "ecivil", name = "vinculo_boleto")
public class VinculoBoleto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_id_vinculo_boleto")
	@Column(name = "id_vinculo_boleto")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pedido_certidao")
	private PedidoCertidao pedidoCertidao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_solicitacao_credito")
	private SolicitacaoCredito solicitacaoCredito;
	
	@Column(name = "numero_boleto")
	private String numeroBoleto;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencimento")
	private Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagamento")
	private Date dataPagamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private Date dataFim;

	public boolean isAtivo() {
		return getDataFim() == null;
	}
	
	public boolean ehBoletoVencido() {
		LocalDate dataVencimento = Instant.ofEpochMilli(getDataVencimento().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dataAtual = LocalDate.now();
		
		if (dataAtual.isAfter(dataVencimento)) {
			if (dataVencimento.getDayOfWeek().equals(DayOfWeek.SATURDAY) && dataVencimento.plusDays(2).equals(dataAtual)) {
				return Boolean.FALSE;
			}
			if (dataVencimento.getDayOfWeek().equals(DayOfWeek.SUNDAY) && dataVencimento.plusDays(1).equals(dataAtual)) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
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

	public SolicitacaoCredito getSolicitacaoCredito() {
		return solicitacaoCredito;
	}

	public void setSolicitacaoCredito(SolicitacaoCredito solicitacaoCredito) {
		this.solicitacaoCredito = solicitacaoCredito;
	}

	public String getNumeroBoleto() {
		return numeroBoleto;
	}

	public void setNumeroBoleto(String numeroBoleto) {
		this.numeroBoleto = numeroBoleto;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
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
		if (obj instanceof VinculoBoleto) {
			return ((VinculoBoleto) obj).getId().equals(this.id);
		}
		return false;
	}

}
