package ecivil.adm.controller.pedidos;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.adm.controller.BaseController;
import ecivil.adm.enumerator.EnumPaginas;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.PedidoCertidaoBO;
import ecivil.ejb.entidade.Municipio;
import ecivil.ejb.entidade.PedidoCertidao;
import ecivil.ejb.enumeration.EnumTipoSaidaCertidao;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.MunicipioLookUp;
import ecivil.ejb.util.Util;
import web.util.JSFUtil;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class RealizarPedidoCertidaoController extends BaseController implements Serializable {

	@EJB
	private PedidoCertidaoBO pedidoCertidaoBO;
	
	private PedidoCertidao pedidoCertidao;
	
	@EJB
	private MunicipioLookUp municipioLookUp;
	
	@PostConstruct
	public void inicializa() throws Exception {
		super.validaServentiaSelecionada();
		inicializaPedidoCertidao();
	}
	
	private void inicializaPedidoCertidao() {
		setPedidoCertidao((PedidoCertidao) JSFUtil.get("pedidoCertidao"));
		if (getPedidoCertidao() == null) {
			setPedidoCertidao(new PedidoCertidao());
			getPedidoCertidao().setTipoSaida(EnumTipoSaidaCertidao.DIGITAL.getCodigo());
			getPedidoCertidao().setUsuarioInternoRequisitante(getUsuarioLogadoPortal());
			getPedidoCertidao().setCodCorregedoriaRequisitante(getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado());
		}
	}
	
	public List<Municipio> recuperaListaMunicipioEstadoSede() {
		return municipioLookUp.getListaMunicipiosEstadoSede();
	}
	
	public void limparFiltros() {
		getPedidoCertidao().setNomePessoaPesquisa(null);
		getPedidoCertidao().setNomeAssociado(null);
		getPedidoCertidao().setCpf(null);
		getPedidoCertidao().setMunicipioPesquisa(null);
		getPedidoCertidao().setNomeIdentico(Boolean.FALSE);
	}

	public String avancar() {
		try {
			if (!isAlgumFiltroPreenchido()) {
				Mensagem.errorSemBundle("Favor preeencher um dos campos para realizar a consulta.");
				return "";
			}
			
			if (Util.ehStringValida(getPedidoCertidao().getCpf()) && !Util.isValidCPF(getPedidoCertidao().getCpf())) {
				Mensagem.errorSemBundle("O CPF informado é inválido.");
				return "";
			}
			
			pedidoCertidaoBO.buscarCertidaoIndexada(getPedidoCertidao(), getPedidoCertidao().getNomePessoaPesquisa(), getPedidoCertidao().getNomeAssociado(),
					getPedidoCertidao().getMunicipioPesquisa(), getPedidoCertidao().getCpf(),
					getPedidoCertidao().isNomeIdentico(), 0);
			super.setarObjetoNoFlash(getPedidoCertidao(), "pedidoCertidao");
			return EnumPaginas.RESULTADO_BUSCA_CERTIDAO.getUrl();
		} catch (ECivilException e) {
			if (e.possuiListaErros()) {
				Mensagem.listaErrosSemBundle(e.getListaErros());
			} else {
				Mensagem.errorSemBundle(e.getMensagemErro());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Não foi possível consultar os dados da certidão nesse momento.");
		}

		return "";
	}

	private boolean isAlgumFiltroPreenchido() {
		return Util.ehStringValida(getPedidoCertidao().getNomePessoaPesquisa()) || Util.ehStringValida(getPedidoCertidao().getNomeAssociado()) || Util.ehStringValida(getPedidoCertidao().getCpf())
				|| getPedidoCertidao().getMunicipioPesquisa() != null;
	}

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
	
	public PedidoCertidao getPedidoCertidao() {
		return pedidoCertidao;
	}

	public void setPedidoCertidao(PedidoCertidao pedidoCertidao) {
		this.pedidoCertidao = pedidoCertidao;
	}

}
