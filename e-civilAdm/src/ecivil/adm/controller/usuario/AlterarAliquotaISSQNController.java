package ecivil.adm.controller.usuario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import ecivil.adm.controller.BaseController;
import ecivil.adm.util.Mensagem;
import ecivil.ejb.bo.AliquotaISSQNBO;
import ecivil.ejb.entidade.AliquotaISSQN;
import ecivil.ejb.entidade.UsuarioPortalInterno;
import ecivil.ejb.exception.ECivilException;
import ecivil.ejb.lookup.CartorioLookUp;
import ecivil.ejb.ws.cliente.rest.response.migracaorecompe.CartorioResponse;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class AlterarAliquotaISSQNController extends BaseController implements Serializable {

	@EJB
	private AliquotaISSQNBO aliquotaISSQNBO;
	private UsuarioPortalInterno usuarioPortalInterno;
	private List<AliquotaISSQN> listaHistoricoAliquotaISSQN;
	private AliquotaISSQN aliquotaISSQNAtual;
	private String novaAliquotaISSQN;
	private String labelServentia;
	@EJB
	private CartorioLookUp cartorioLookUp;
	
	@PostConstruct
	public void inicializar() {
		String codigoCorregedoria = getUsuarioLogadoPortal().getCodigoCorregedoriaSelecionado();
		setLabelServentia(nomeServentiaSelecionada(codigoCorregedoria));
		setListaHistoricoAliquotaISSQN(aliquotaISSQNBO.recuperaListaAliquotaISSQN(codigoCorregedoria));
		setAliquotaISSQNAtual(aliquotaISSQNBO.recuperaAliquotaISSQN(codigoCorregedoria));
	}

	private String nomeServentiaSelecionada(String codigoCorregedoria) {
		try {
			CartorioResponse cartorio = cartorioLookUp.recuperaCartorioPorCodigoCorregedoria(codigoCorregedoria);
			return cartorio != null ? cartorio.getNomeDistritoECartorio() : "-----";
		} catch (ECivilException e) {
			e.printStackTrace();
			return "-----";
		}
	}
	
	public UsuarioPortalInterno getUsuarioPortalInterno() {
		return usuarioPortalInterno;
	}

	public void setUsuarioPortalInterno(UsuarioPortalInterno usuarioPortalInterno) {
		this.usuarioPortalInterno = usuarioPortalInterno;
	}

	public AliquotaISSQN getAliquotaISSQNAtual() {
		return aliquotaISSQNAtual;
	}

	public void setAliquotaISSQNAtual(AliquotaISSQN aliquotaISSQN) {
		this.aliquotaISSQNAtual = aliquotaISSQN;
	}	
	
	public String getNovaAliquotaISSQN() {
		return novaAliquotaISSQN;
	}

	public void setNovaAliquotaISSQN(String novaAliquotaISSQN) {
		this.novaAliquotaISSQN = novaAliquotaISSQN;
	}
	
	public List<AliquotaISSQN> getListaHistoricoAliquotaISSQN() {
		return listaHistoricoAliquotaISSQN;
	}

	public void setListaHistoricoAliquotaISSQN(List<AliquotaISSQN> listaHistoricoAliquotaISSQN) {
		this.listaHistoricoAliquotaISSQN = listaHistoricoAliquotaISSQN;
	}

	public void salvar(){
		try {
			aliquotaISSQNAtual.setDataFim(new Date());
			aliquotaISSQNBO.setarDataFimALiquotaAtualECriaNova(aliquotaISSQNAtual, novaAliquotaISSQN);
			inicializar();
			Mensagem.infoSemBundle("Nova alíquota salva com sucesso.");
		} catch(ECivilException e) {
			Mensagem.listaErrosSemBundle(e.getListaErros());
		} catch(Exception e) {
			e.printStackTrace();
			Mensagem.errorSemBundle("Houve um erro ao tentar salvar a nova alíquota. Tente novamente mais tarde.");
		}
	}

	public String getLabelServentia() {
		return labelServentia;
	}

	public void setLabelServentia(String labelServentia) {
		this.labelServentia = labelServentia;
	}
}