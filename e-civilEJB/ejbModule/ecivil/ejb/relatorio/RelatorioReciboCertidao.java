package ecivil.ejb.relatorio;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.ValoresPedidoCertidao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.ejb.vo.relatorios.ReciboCertidaoVO;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.governors.MaxPagesGovernor;

@Stateless
public class RelatorioReciboCertidao {
	
	@EJB
	private CartorioLookUp cartorioLookUp;

	public byte[] geraRelatorioReciboCertidao(PedidoCertidao pedidoCertidao) throws ECivilException {
		try {
			InputStream sourceFileName = recuperaArquivoJasper("recibo-certidao.jasper");
			HashMap<String, Object> parameters = new HashMap<>();
			setaParametrosRelatorio(parameters);
			JRBeanCollectionDataSource dataSource = montaObjetoDataSource(pedidoCertidao);
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(sourceFileName);
			jasperReport.setProperty(MaxPagesGovernor.PROPERTY_MAX_PAGES_ENABLED, Boolean.TRUE.toString());
			jasperReport.setProperty(MaxPagesGovernor.PROPERTY_MAX_PAGES, "1");
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ECivilException("Não foi possível gerar o recibo da certidão nesse momento. Tente novamente mais tarde.");
		}
	}

	private void setaParametrosRelatorio(HashMap<String, Object> parameters) throws Exception {
		parameters.put("IMG_LOGO", recuperaPastaImagem("logo.jpg"));		
	}

	private JRBeanCollectionDataSource montaObjetoDataSource(PedidoCertidao pedidoCertidao) throws ECivilException {
		ReciboCertidaoVO reciboCertidaoVO = new ReciboCertidaoVO();
		reciboCertidaoVO.setProtocolo(pedidoCertidao.getProtocolo());
		reciboCertidaoVO.setDataEmissao(new Date());
		reciboCertidaoVO.setNomeRequerente(recuperaNomeRequerente(pedidoCertidao));
		reciboCertidaoVO.setTipoCertidao(StringUtils.capitalize(pedidoCertidao.getTipoCertidao().getDescFato()));
		reciboCertidaoVO.setNomePessoaA(pedidoCertidao.getNomePrimeiraPessoa());
		reciboCertidaoVO.setNomePessoaB(pedidoCertidao.getNomeSegundaPessoa());
		reciboCertidaoVO.setPossuiAverbacao(pedidoCertidao.isPossuiAverbacao());
		reciboCertidaoVO.setExibeEnderecoRetirada(exibeEnderecoEntrega(pedidoCertidao));
		
		if (pedidoCertidao.isPedidoDeCartorioParaCartorio()) {
			setaValoresCertidaoCartorioParaCartorio(reciboCertidaoVO, pedidoCertidao);
		} else {
			setaValoresCertidao(reciboCertidaoVO, pedidoCertidao);
		}
		
		setaEnderecoCartorio(reciboCertidaoVO, pedidoCertidao);
		return new JRBeanCollectionDataSource(Collections.singletonList(reciboCertidaoVO));
	}

	private boolean exibeEnderecoEntrega(PedidoCertidao pedidoCertidao) {
		if (pedidoCertidao.isSaidaBuscaPresencial()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private String recuperaNomeRequerente(PedidoCertidao pedidoCertidao) throws ECivilException {
		if (Util.ehStringValida(pedidoCertidao.getCodCorregedoriaRequisitante())) {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitante());
			return cartorio.getNomeCartorio() + " - " + cartorio.getNomeTitularConta();
		}
		return pedidoCertidao.getUsuarioExterno().getNome();
	}

	private void setaValoresCertidao(ReciboCertidaoVO reciboCertidaoVO, PedidoCertidao pedidoCertidao) {
		ValoresPedidoCertidao valoresCertidao = pedidoCertidao.recuperaValoresCertidaoRequisitado();
		reciboCertidaoVO.setValorCertidao(Util.formataMoeda(valoresCertidao.getValorCertidao()));
		reciboCertidaoVO.setValorAverbacao(Util.formataMoeda(valoresCertidao.getValorAverbacao()));
		reciboCertidaoVO.setValorEmolumentos(Util.formataMoeda(recuperaSomaValores(valoresCertidao.getValorEmolumentosCertidao(), valoresCertidao.getValorEmolumentosAverbacao())));
		reciboCertidaoVO.setValorRecompe(Util.formataMoeda(recuperaSomaValores(valoresCertidao.getValorRecompeCertidao(), valoresCertidao.getValorRecompeAverbacao())));
		reciboCertidaoVO.setValorTaxaFiscalizacao(Util.formataMoeda(recuperaSomaValores(valoresCertidao.getValorTaxaFiscalizacaoCertidao(), valoresCertidao.getValorTaxaFiscalizacaoAverbacao())));
		reciboCertidaoVO.setValorISSQN(Util.formataMoeda(recuperaSomaValores(valoresCertidao.getValorIssqnCertidao(), valoresCertidao.getValorIssqnAverbacao())));
		reciboCertidaoVO.setValorTotalCertidao(Util.formataMoeda(valoresCertidao.getValorTotal()));
	}
	
	private void setaValoresCertidaoCartorioParaCartorio(ReciboCertidaoVO reciboCertidaoVO, PedidoCertidao pedidoCertidao) {
		ValoresPedidoCertidao valoresCertidaoRequisitado = pedidoCertidao.recuperaValoresCertidaoRequisitado();
		ValoresPedidoCertidao valoresCertidaoRequisitante = pedidoCertidao.recuperaValoresCertidaoRequisitante();
		reciboCertidaoVO.setPedidoCartorioParaCartorio(Boolean.TRUE);
		reciboCertidaoVO.setValorCertidao(Util.formataMoeda(valoresCertidaoRequisitado.getValorCertidao()));
		reciboCertidaoVO.setValorAverbacao(Util.formataMoeda(valoresCertidaoRequisitado.getValorAverbacao()));
		reciboCertidaoVO.setValorAverbacaoRequisitante(Util.formataMoeda(valoresCertidaoRequisitante.getValorAverbacao()));
		reciboCertidaoVO.setValorTransmissao(Util.formataMoeda(valoresCertidaoRequisitante.getValorCertidao()));
		somaValorCertidaoDeCartorioParaCartorio(reciboCertidaoVO, valoresCertidaoRequisitado, valoresCertidaoRequisitante);
	}
	
	private void somaValorCertidaoDeCartorioParaCartorio(ReciboCertidaoVO reciboCertidaoVO, ValoresPedidoCertidao valoresCertidaoRequisitado, ValoresPedidoCertidao valoresCertidaoRequisitante) {
		BigDecimal totalEmolumentos = recuperaSomaValores(valoresCertidaoRequisitado.getValorEmolumentosCertidao(), valoresCertidaoRequisitado.getValorEmolumentosAverbacao());
		totalEmolumentos = totalEmolumentos.add(recuperaSomaValores(valoresCertidaoRequisitante.getValorEmolumentosCertidao(), valoresCertidaoRequisitante.getValorEmolumentosAverbacao()));
		reciboCertidaoVO.setValorEmolumentos(Util.formataMoeda(totalEmolumentos));
		
		BigDecimal totalRecompe = recuperaSomaValores(valoresCertidaoRequisitado.getValorRecompeCertidao(), valoresCertidaoRequisitado.getValorRecompeAverbacao());
		totalRecompe = totalRecompe.add(recuperaSomaValores(valoresCertidaoRequisitante.getValorRecompeCertidao(), valoresCertidaoRequisitante.getValorRecompeAverbacao()));
		reciboCertidaoVO.setValorRecompe(Util.formataMoeda(totalRecompe));
		
		BigDecimal totalTaxaFisc = recuperaSomaValores(valoresCertidaoRequisitado.getValorTaxaFiscalizacaoCertidao(), valoresCertidaoRequisitado.getValorTaxaFiscalizacaoAverbacao());
		totalTaxaFisc = totalTaxaFisc.add(recuperaSomaValores(valoresCertidaoRequisitante.getValorTaxaFiscalizacaoCertidao(), valoresCertidaoRequisitante.getValorTaxaFiscalizacaoAverbacao()));
		reciboCertidaoVO.setValorTaxaFiscalizacao(Util.formataMoeda(totalTaxaFisc));
		
		BigDecimal totalIssqn = recuperaSomaValores(valoresCertidaoRequisitado.getValorIssqnCertidao(), valoresCertidaoRequisitado.getValorIssqnAverbacao());
		totalIssqn = totalIssqn.add(recuperaSomaValores(valoresCertidaoRequisitante.getValorIssqnCertidao(), valoresCertidaoRequisitante.getValorIssqnAverbacao()));
		reciboCertidaoVO.setValorISSQN(Util.formataMoeda(totalIssqn));
		
		reciboCertidaoVO.setValorTotalCertidao(Util.formataMoeda(valoresCertidaoRequisitado.getValorTotal().add(valoresCertidaoRequisitante.getValorTotal())));		
	}

	private void setaEnderecoCartorio(ReciboCertidaoVO reciboCertidaoVO, PedidoCertidao pedidoCertidao) throws ECivilException {
		CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitada());
		reciboCertidaoVO.setEnderecoCartorioRequisitado(cartorio.obtemDescricaoEndereco());
	}

	private BigDecimal recuperaSomaValores(BigDecimal valor1, BigDecimal valor2) {
		if (valor2 != null) {
			return valor1.add(valor2);
		}
		return valor1;
	}

	private InputStream recuperaArquivoJasper(String nomeJasper) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream("META-INF/relatorios/jasper/".concat(nomeJasper));
	}
	
	private String recuperaPastaImagem(String file) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		URL path = classLoader.getResource("META-INF/relatorios/imagem/".concat(file));
		return path != null ? path.toString() : null;
	}

}
