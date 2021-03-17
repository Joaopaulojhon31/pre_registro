package ecivil.ejb.bo;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.ParametroDAO;
import ecivil.ejb.dao.SolicitacaoCreditoDAO;
import ecivil.ejb.entidade.Parametro;
import ecivil.ejb.entidade.SolicitacaoCredito;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.entidade.VinculoBoleto;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.util.DataUtil;
import ecivil.ejb.util.Util;
import ecivil.ejb.ws.cliente.rest.RecompeWS;
import ecivil.ejb.ws.cliente.rest.request.recompe.RegistroBoletoRequestVo;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;
import ecivil.ejb.ws.cliente.rest.response.recompe.RegistroBoletoResponseVo;
import web.util.Paginacao;

@Stateless
public class SolicitacaoCreditoBO {

	@EJB
	private RecompeWS recompeWS;
	
	@EJB
	private SolicitacaoCreditoDAO solicitacaoCreditoDAO;
	
	@EJB
	private ParametroDAO parametroDAO;
	
	@EJB
	private DataDAO dataDAO;
	
	public int recuperaTotalSolicitacaoCredito(String codigoCorregedoria) {
		return solicitacaoCreditoDAO.recuperaTotalSolicitacaoCredito(codigoCorregedoria);
	}

	public List<SolicitacaoCredito> recuperaSolicitacaoCredito(String codigoCorregedoria, Paginacao paginacao) {
		return solicitacaoCreditoDAO.recuperaSolicitacaoCredito(codigoCorregedoria, paginacao);
	}

	public void adicionarSolicitacaoCredito(UsuarioPortalInterno usuario, BigDecimal valorCredito, CartorioResponse cartorio) throws ECivilException {
		SolicitacaoCredito solicitacaoCredito = montaNovoObjetoSolicitacaoCredito(usuario, valorCredito);
		emitirNovoBoleto(solicitacaoCredito, cartorio);
		solicitacaoCreditoDAO.persiste(solicitacaoCredito);
	}
	
	private SolicitacaoCredito montaNovoObjetoSolicitacaoCredito(UsuarioPortalInterno usuario, BigDecimal valorCredito) {
		SolicitacaoCredito solicitacaoCredito = new SolicitacaoCredito();
		solicitacaoCredito.setCodigoCorregedoria(usuario.getCodigoCorregedoriaSelecionado());
		solicitacaoCredito.setDataInicio(dataDAO.retornaDataBanco());
		solicitacaoCredito.setUsuario(usuario);
		solicitacaoCredito.setValor(valorCredito);
		return solicitacaoCredito;
	}
	
	private byte[] emitirNovoBoleto(SolicitacaoCredito solicitacaoCredito, CartorioResponse cartorio) throws ECivilException {
		RegistroBoletoResponseVo respostaRegistroBoleto = registrarBoleto(cartorio, solicitacaoCredito.getValor());
		solicitacaoCredito.getListaVinculoBoletoInicializada().add(montaObjetoVinculoBoletoSolicitacaoCredito(solicitacaoCredito, respostaRegistroBoleto));
		return Base64.getDecoder().decode(respostaRegistroBoleto.getBoletoPdf());
	}
	
	private VinculoBoleto montaObjetoVinculoBoletoSolicitacaoCredito(SolicitacaoCredito solicitacaoCredito, RegistroBoletoResponseVo respostaRegistroBoleto) {
		VinculoBoleto vinculoBoleto = new VinculoBoleto();
		vinculoBoleto.setDataInicio(dataDAO.retornaDataBanco());
		vinculoBoleto.setDataVencimento(DataUtil.converteDataStringParaDate(respostaRegistroBoleto.getDataVencimento()));
		vinculoBoleto.setNumeroBoleto(respostaRegistroBoleto.getNumeroTitulo());
		vinculoBoleto.setSolicitacaoCredito(solicitacaoCredito);
		return vinculoBoleto;
	}
	
	public byte[] imprimirBoleto(SolicitacaoCredito solicitacaoCredito, CartorioResponse cartorio) throws ECivilException {
		VinculoBoleto vinculoBoleto = solicitacaoCredito.recuperaVinculoBoletoAtivo();
		
//		FIXME - Rever essa regra
//		if (vinculoBoleto.ehBoletoVencido()) {
//			vinculoBoleto.setDataFim(dataDAO.retornaDataBanco());
//			byte[] boleto = emitirNovoBoleto(solicitacaoCredito, cartorio);
//			solicitacaoCreditoDAO.atualiza(solicitacaoCredito);
//			return boleto;
//		}
		
		return recompeWS.imprimirBoleto(vinculoBoleto.getNumeroBoleto());
	}

	private RegistroBoletoResponseVo registrarBoleto(CartorioResponse cartorio, BigDecimal valorCredito) throws ECivilException {
		try {
			return recompeWS.registrarBoleto(montaObjetoRegistroBoletoRequestVo(cartorio, valorCredito));
		} catch (ECivilException e) {
			throw new ECivilException("Não foi possível registrar o boleto nesse momento. Tente novamente mais tarde. " + e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ECivilException("Não foi possível registrar o boleto nesse momento. Tente novamente mais tarde.");
		}
	}

	private RegistroBoletoRequestVo montaObjetoRegistroBoletoRequestVo(CartorioResponse cartorio, BigDecimal valorCredito) {
		Date dataAtual = dataDAO.retornaDataBanco();
		RegistroBoletoRequestVo registroBoletoRequestVo = new RegistroBoletoRequestVo();
		registroBoletoRequestVo.setCodigoCarteira(parametroDAO.buscaValorParametro(Parametro.CODIGO_CARTEIRA_EMISSAO_BOLETO));
		registroBoletoRequestVo.setDataEmissao(DataUtil.formataDataRegistroBoletoBradesco(dataAtual));
		registroBoletoRequestVo.setDataVencimento(recuperaDataVencimentoBoleto(dataAtual));
		registroBoletoRequestVo.setValor(String.valueOf(valorCredito));
		registroBoletoRequestVo.setNomePagador(cartorio.getNomeTitularConta());
		registroBoletoRequestVo.setCpfCnpjPagador(cartorio.getCpfCnpjTitularConta());
		registroBoletoRequestVo.setCepPagador(Util.removeMask(cartorio.getCep()));
		registroBoletoRequestVo.setLogradouroPagador(cartorio.getLogradouro());
		registroBoletoRequestVo.setNumeroLogradouroPagador(cartorio.getNumeroLogradouro());
		registroBoletoRequestVo.setBairroPagador(cartorio.getBairro());
		registroBoletoRequestVo.setMunicipioPagador(cartorio.getNomeMunicipio());
		registroBoletoRequestVo.setUfPagador(cartorio.getSiglaUf());
		registroBoletoRequestVo.setEmailPgador(cartorio.getEmail());
		return registroBoletoRequestVo;
	}
	
	private String recuperaDataVencimentoBoleto(Date dataEmissao) {
		return DataUtil.formataDataRegistroBoletoBradesco(DataUtil.adicionaDiasData(dataEmissao, Integer.valueOf(parametroDAO.buscaValorParametro(Parametro.QTD_DIAS_VENCIMENTO_BOLETO))));
	}
	
}
