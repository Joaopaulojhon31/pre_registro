package ecivil.web.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ecivil.web.enumerator.EnumPaginas;

@SuppressWarnings("serial")
@ViewScoped
@Named
public class SobreController extends BaseController implements Serializable {

	public String voltar() {
		return EnumPaginas.PRINCIPAL.getUrl();
	}
}
