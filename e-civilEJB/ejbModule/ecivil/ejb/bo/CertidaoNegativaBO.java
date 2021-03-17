package ecivil.ejb.bo;

import java.time.LocalDateTime;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ecivil.ejb.dao.CertidaoNegativaDAO;
import ecivil.ejb.dao.DataDAO;
import ecivil.ejb.dao.MunicipioDAO;
import ecivil.ejb.entidade.CertidaoNegativa;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.CriptografiaUtil;
import ecivil.ejb.util.Util;

@Stateless
public class CertidaoNegativaBO {
	
	@EJB
	private CartorioLookUp cartorioLookUp;

	@EJB
	private CertidaoNegativaDAO certidaoNegativaDAO;
	
	@EJB
	private MunicipioDAO municipioDAO;
	
	@EJB
	private DataDAO dataDAO;

	public CertidaoNegativa salvarNovaSolicitacaoCertidaoNegativa(CertidaoNegativa certidaoNegativa) throws ECivilException {
		if (certidaoNegativa.getCartorio() != null && Util.ehStringValida(certidaoNegativa.getCartorio().getCodigoMunicipio())) {
			certidaoNegativa.setMunicipio(municipioDAO.recuperaMunicipioPorCodigoRecompe(certidaoNegativa.getCartorio().getCodigoMunicipio()));
		}
		
		certidaoNegativa.setDataGravacao(dataDAO.retornaDataBanco());
		certidaoNegativaDAO.persiste(certidaoNegativa);
		return certidaoNegativa;
	}
	
	public CertidaoNegativa gerarCertidaoNegativaCartorio(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) throws ECivilException {
		CertidaoNegativa certidaoNegativa = new CertidaoNegativa();
		certidaoNegativa.setCodCorregedoria(usuarioPortalInterno.getCodigoCorregedoriaSelecionado());
		certidaoNegativa.setCodigoHash(geraCodigoHash(pedidoCertidao.getCodCorregedoriaRequisitada()));
		certidaoNegativa.setNomePrimeiraPessoa(pedidoCertidao.getNomePessoaPesquisa().trim());
		certidaoNegativa.setTipoCertidao(pedidoCertidao.getTipoCertidao());
		certidaoNegativa.setUsuarioInterno(usuarioPortalInterno);
		certidaoNegativa.setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(usuarioPortalInterno.getCodigoCorregedoriaSelecionado()));
		return certidaoNegativa;
	}

	public CertidaoNegativa montaObjetoCertidaoNegativa(PedidoCertidao pedidoCertidao, UsuarioPortalInterno usuarioPortalInterno) {
		CertidaoNegativa certidaoNegativa = new CertidaoNegativa();
		certidaoNegativa.setAno(pedidoCertidao.getAno());
		certidaoNegativa.setCodCorregedoria(pedidoCertidao.getCodCorregedoriaRequisitada());
		certidaoNegativa.setCodigoHash(geraCodigoHash(pedidoCertidao.getCodCorregedoriaRequisitada()));
		certidaoNegativa.setDataGravacao(dataDAO.retornaDataBanco());
		certidaoNegativa.setMunicipio(pedidoCertidao.getMunicipio());
		certidaoNegativa.setNomePrimeiraPessoa(pedidoCertidao.getNomePrimeiraPessoa());
		certidaoNegativa.setNomeSegundaPessoa(pedidoCertidao.getNomeSegundaPessoa());
		certidaoNegativa.setTipoCertidao(pedidoCertidao.getTipoCertidao());
		certidaoNegativa.setUsuarioInterno(usuarioPortalInterno);

		if (pedidoCertidao.getId() != null) {
			certidaoNegativa.setPedidoCertidao(pedidoCertidao);
		}
		
		return certidaoNegativa;
	}

	private String geraCodigoHash(String codCorregedoriaRequisitada) {
		String hash = recuperaCodigoRandom() + codCorregedoriaRequisitada + LocalDateTime.now().toString();
		return CriptografiaUtil.retornaCriptografadoMD5(hash);
	}

	private static String recuperaCodigoRandom() {
		Random random = new Random();
		return String.valueOf(random.nextInt(9999));
	}

	public CertidaoNegativa recuperaCertidaoNegativaPorHash(String codigoHash) throws ECivilException {
		return certidaoNegativaDAO.recuperaCertidaoNegativaPorHash(codigoHash);
	}

}