package ecivil.ejb.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.ApplicationException;
import javax.xml.ws.WebFault;

import ecivil.ejb.util.Util;

@WebFault
@ApplicationException(rollback = true)
public class ECivilException extends Exception {

	private static final long serialVersionUID = 1L;

	private String mensagemErro;
	private List<Map<String, Object[]>> listaErros;

	public String getMensagemErro() {
		return mensagemErro;
	}

	public ECivilException() {
		super();
	}
	
	public ECivilException(String chaveErro, Object... args) {
		super();
		listaErros = new ArrayList<Map<String, Object[]>>();
		adicionaErroNaLista(chaveErro, args);
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public ECivilException(String mensagemErro) {
		super();
		this.mensagemErro = mensagemErro;
	}

	public List<Map<String, Object[]>> getListaErros() {
		return listaErros;
	}

	public void setListaErros(List<Map<String, Object[]>> listaErros) {
		this.listaErros = listaErros;
	}

	public void adicionaErroNaLista(String chaveErro) {
		HashMap<String, Object[]> mapa = new HashMap<String, Object[]>();
		mapa.put(chaveErro, null);
		getListaErros().add(mapa);
	}

	public void adicionaErroNaLista(String chaveErro, Object... args) {
		HashMap<String, Object[]> mapa = new HashMap<String, Object[]>();
		mapa.put(chaveErro, (Object[]) args);
		getListaErros().add(mapa);
	}

	public boolean possuiMensagemErro() {
		return Util.ehStringValida(getMensagemErro());
	}
	
	public boolean possuiListaErros() {
		return getListaErros() != null && getListaErros().size() > 0;
	}

	public void instanciaListaErros() {
		if (listaErros == null) {
			listaErros = new ArrayList<Map<String, Object[]>>();
		}
	}
}
