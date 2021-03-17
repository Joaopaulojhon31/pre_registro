package ecivil.ejb.util;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ecivil.ejb.vo.SolicitacaoSaqueVO;
import ecivil.ejb.ws.cliente.rest.request.contadigital.FiltroSaqueProvisionadoRequest;

public class SolicitacaoSaqueExcelUtil {
	
	public static void gerarPlanilhaExcel(List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO, FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		SXSSFWorkbook wb = new SXSSFWorkbook(workbook);
		SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("Solicitações de Saque");
		
		CellStyle cellStyle = criaCellStyle(wb);
		criaTituloPlanilha(sheet, filtroSaqueProvisionadoRequest, listaSolicitacaoSaqueVO);
		criaCabecalhoColunasPlanilha(cellStyle, sheet);
		setaDadosPlanilha(cellStyle, sheet, listaSolicitacaoSaqueVO);
		renderizaTamanhoColunasPlanilha(sheet);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			wb.write(byteArrayOutputStream);
			DownloadArquivo.downloadFile("Solicitacao_Saque" + DataUtil.recuperaDataAtualNomeArquivo(), byteArrayOutputStream.toByteArray(), "xlsx", FacesContext.getCurrentInstance());
		} finally {
			if (wb != null) {
				wb.close();
			}
			if (byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}
			workbook.close();
		}
	}
	
	private static CellStyle criaCellStyle(SXSSFWorkbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setWrapText(Boolean.TRUE);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		return cellStyle;
	}
	
	private static void criaTituloPlanilha(SXSSFSheet sheet, FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest, List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO) {
		SXSSFRow titulo = sheet.createRow(0);
		SXSSFCell sxssFCellTitulo = titulo.createCell(0);
		sxssFCellTitulo.setCellValue(recuperaValorTitulo(filtroSaqueProvisionadoRequest, listaSolicitacaoSaqueVO));
		
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 6);
		sheet.addMergedRegion(cellRangeAddress);
		
		RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
	}

	private static String recuperaValorTitulo(FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest, List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO) {
		StringBuffer sb = new StringBuffer();
		sb.append("Total de registros: " + listaSolicitacaoSaqueVO.size());
		sb.append(" " + recuperaPeriodoSelecionado(filtroSaqueProvisionadoRequest));
		sb.append(" " + recuperaValorTotal(listaSolicitacaoSaqueVO));
		return sb.toString();
	}

	private static int criaCabecalhoColunasPlanilha(CellStyle cellStyle, SXSSFSheet sheet) {
		SXSSFRow primeiraLinha = sheet.createRow(1);
		
		String[] colunas = new String[] {"#", "CPF/CNPJ Titular", "Nome Titular", "Valor", "Dados Bancários", "Distrito", "Município"};

		for (int indexColunas = 0; indexColunas < colunas.length; indexColunas++) {
			SXSSFCell celula = primeiraLinha.createCell(indexColunas);
			celula.setCellValue(colunas[indexColunas]);
			celula.setCellStyle(cellStyle);
		}
		
		return colunas.length;
	}
	
	private static void setaDadosPlanilha(CellStyle cellStyle, SXSSFSheet sheet, List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO) {
		int indexBoletos = 2;

		for (SolicitacaoSaqueVO solicitacaoSaqueVO : listaSolicitacaoSaqueVO) {
			SXSSFRow linha = sheet.createRow(indexBoletos);
			
			SXSSFCell sxssFCell0 = linha.createCell(0);
			sxssFCell0.setCellValue(String.valueOf((indexBoletos - 1)));
			sxssFCell0.setCellStyle(cellStyle);
			
			SXSSFCell sxssFCell1 = linha.createCell(1);
			sxssFCell1.setCellValue(Util.ehStringValida(solicitacaoSaqueVO.cpfCnpjTitularFormatado()) ? solicitacaoSaqueVO.cpfCnpjTitularFormatado() : "");
			sxssFCell1.setCellStyle(cellStyle);
			
			SXSSFCell sxssFCell2 = linha.createCell(2);
			sxssFCell2.setCellValue(Util.ehStringValida(solicitacaoSaqueVO.getNomeTitularConta()) ? solicitacaoSaqueVO.getNomeTitularConta() : "");
			sxssFCell2.setCellStyle(cellStyle);
			
			SXSSFCell sxssFCell3 = linha.createCell(3);
			sxssFCell3.setCellValue(Util.formataMoeda(solicitacaoSaqueVO.getValorSolicitacao()));
			sxssFCell3.setCellStyle(cellStyle);
			
			SXSSFCell sxssFCell4 = linha.createCell(4);
			sxssFCell4.setCellValue(recuperaDadosBancarios(solicitacaoSaqueVO));
			sxssFCell4.setCellStyle(cellStyle);
			
			SXSSFCell sxssFCell5 = linha.createCell(5);
			sxssFCell5.setCellValue(Util.ehStringValida(solicitacaoSaqueVO.getDistrito()) ? solicitacaoSaqueVO.getDistrito() : "");
			sxssFCell5.setCellStyle(cellStyle);
			
			SXSSFCell sxssFCell6 = linha.createCell(6);
			sxssFCell6.setCellValue(Util.ehStringValida(solicitacaoSaqueVO.getMunicipio()) ? solicitacaoSaqueVO.getMunicipio() : "");
			sxssFCell6.setCellStyle(cellStyle);
			
			indexBoletos++;
		}		
	}

	private static String recuperaDadosBancarios(SolicitacaoSaqueVO solicitacaoSaqueVO) {
		StringBuffer sb = new StringBuffer();
		
		if (Util.ehStringValida(solicitacaoSaqueVO.getNomeBanco())) {
			sb.append(solicitacaoSaqueVO.getNomeBanco());
			sb.append(" - ");
		}
		
		sb.append(solicitacaoSaqueVO.getCodigoBanco());
		sb.append(" - ");
		sb.append("Agência: " + solicitacaoSaqueVO.getAgencia());
		sb.append("; ");
		sb.append("Conta: " + solicitacaoSaqueVO.getConta());
		sb.append("; ");
		sb.append("Tipo Conta: " + solicitacaoSaqueVO.recuperaDescricaoTipoConta());
		return sb.toString();
	}
	
	private static void renderizaTamanhoColunasPlanilha(SXSSFSheet sheet) {
		sheet.setColumnWidth(0, 1000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 11000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
	}
	
	private static String recuperaPeriodoSelecionado(FiltroSaqueProvisionadoRequest filtroSaqueProvisionadoRequest) {
		if (!Util.ehStringValida(filtroSaqueProvisionadoRequest.getDataInicio()) && !Util.ehStringValida(filtroSaqueProvisionadoRequest.getDataFim())) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		
		if (Util.ehStringValida(filtroSaqueProvisionadoRequest.getDataInicio())) {
			sb.append("- Período de: " + filtroSaqueProvisionadoRequest.getDataInicio());
			
			if (Util.ehStringValida(filtroSaqueProvisionadoRequest.getDataFim())) {
				sb.append(" até: " + filtroSaqueProvisionadoRequest.getDataFim());
			}
			
			return sb.toString();
		}
		
		sb.append("Período até: " + filtroSaqueProvisionadoRequest.getDataFim());
		return sb.toString();
	}
	
	private static String recuperaValorTotal(List<SolicitacaoSaqueVO> listaSolicitacaoSaqueVO) {
		return "- Valor total: " + Util.formataMoeda(listaSolicitacaoSaqueVO.stream().map(SolicitacaoSaqueVO::getValorSolicitacao).reduce(BigDecimal::add).get());
	}
	
}
