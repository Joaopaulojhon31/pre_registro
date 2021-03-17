package ecivil.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.ejb.bo.CertidaoNegativaBO;
import ecivil.ejb.entidade.CertidaoNegativa;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.util.Util;
import ecivil.web.enumerator.EnumPaginas;
import ecivil.web.util.Mensagem;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class ValidarHashCertidaoController extends BaseController implements Serializable {

	@EJB
	private CertidaoNegativaBO certidaoNegativaBO;
	
	@EJB
	private CartorioLookUp cartorioLookUp;

	private String codigoHash;
	private CertidaoNegativa certidaoNegativa;

	@PostConstruct
	public void inicializa() {
	}
	
	public void validaCodigoHash() {
		try {
			setCertidaoNegativa(null);
		
			if (!Util.ehStringValida(getCodigoHash())) {
				Mensagem.errorSemBundle("Favor informar o código hash para poder prosseguir com a validação da certidão negativa.");
				return;
			}
			
			if (getCodigoHash().length() < 20) {
				Mensagem.errorSemBundle("O código hash informado é inválido.");
				return;
			}
			
			setCertidaoNegativa(certidaoNegativaBO.recuperaCertidaoNegativaPorHash(getCodigoHash()));
			validaResultadoBuscaCertidaoNegativa();
		} catch (ECivilException e) {
			Mensagem.errorSemBundle(e.getMensagemErro());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível validar o código hash informado no momento. Tente novamente mais tarde.");
		}
	}
	
	private void validaResultadoBuscaCertidaoNegativa() throws ECivilException {
		if (getCertidaoNegativa() != null) {
			buscaDadosCartorio();
			Mensagem.infoSemBundle("Código hash validado com sucesso.");
		} else {
			Mensagem.errorSemBundle("Não foi encontrada certidão negativa com o código hash informado.");
		}
	}

	private void buscaDadosCartorio() throws ECivilException {
		try {
			getCertidaoNegativa().setCartorio(cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(getCertidaoNegativa().getCodCorregedoria()));
		} catch (Exception e) {
			throw new ECivilException("Não foi possível buscar os dados do cartório nesse momento. Tente novamente mais tarde.");
		}
	}

	public void limpar() {
		setCodigoHash(null);
		setCertidaoNegativa(null);
	}
	
	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}

	public String getCodigoHash() {
		return codigoHash;
	}

	public void setCodigoHash(String codigoHash) {
		this.codigoHash = codigoHash;
	}

	public CertidaoNegativa getCertidaoNegativa() {
		return certidaoNegativa;
	}

	public void setCertidaoNegativa(CertidaoNegativa certidaoNegativa) {
		this.certidaoNegativa = certidaoNegativa;
	}
	
}
