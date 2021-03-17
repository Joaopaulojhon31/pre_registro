package web.util;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Paginacao implements Serializable {
	
	private static final long serialVersionUID = 1L;

	int paginaAtual = 1;
	int registrosPorPagina = 10;
	int totalRegistros = 0;
	int pagInicio = 1;
	int pagFinal = 5;
	int numLinks = 5;

	String nomeMetodo;
	Object backingBean;

	public Paginacao() {
	}

	public String getNomeMetodo() {
		return nomeMetodo;
	}

	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}

	public Object getBackingBean() {
		return backingBean;
	}

	public void setBackingBean(Object backingBean) {
		this.backingBean = backingBean;
	}

	public String selecionaBean(String beanPesquisa) {
		return beanPesquisa;
	}

	private void invocaMetodoPesquisa(Object backingBean, String nomeMetodo) {
		try {
			Method method = backingBean.getClass().getMethod(nomeMetodo);
			method.invoke(backingBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selecionaProxima() {
		this.selecionaProximaAux();
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void selecionaAnterior() {
		this.selecionaAnteriorAux();
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void selecionaPrimeiraPagina() {
		this.selecionaPrimeiraPaginaAux();
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void selecionaUltimaPagina() {
		this.selecionaUltimaPaginaAux();
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void selecionaProximaAux() {
		pagInicio += numLinks;
		paginaAtual = pagInicio;
		pagFinal += numLinks;
	}

	public void selecionaAnteriorAux() {
		pagInicio -= numLinks;
		paginaAtual = pagInicio;
		pagFinal -= numLinks;
	}

	public void selecionaPrimeiraPaginaAux() {
		paginaAtual = 1;
		setPagInicio(paginaAtual);
		setPagFinal(paginaAtual + numLinks);
	}

	public void selecionaUltimaPaginaAux() {
		try {
			paginaAtual = getUltimaPagina();
			int resto = 0;
			if (getUltimaPagina() % getNumLinks() == 0) {
				resto = getNumLinks() - 1;
			} else {
				resto = (getUltimaPagina() % getNumLinks()) - 1;
			}
			setPagInicio(getUltimaPagina() - resto);
			setPagFinal(getPagInicio() + numLinks);
		} catch (ArithmeticException e) {
			paginaAtual = 1;
		} finally {
		}
	}

	// -- Links para selecionar pagina ---

	public int getUltimaPagina() {
		int ultimaPagina = (int) (totalRegistros / registrosPorPagina);
		if (totalRegistros % registrosPorPagina > 0) {
			ultimaPagina++;
		}
		return ultimaPagina;
	}

	public boolean getShowPreviousPage() {
		return paginaAtual > 1;
	}

	public boolean getShowNextPage() {
		return totalRegistros > (paginaAtual + 1) * registrosPorPagina;
	}

	public boolean getShowFirstPage() {
		return paginaAtual > 1;
	}

	public boolean getShowLastPage() {
		return getUltimaPagina() >= paginaAtual + 2;
	}

	public boolean getShowPaginacao() {
		return this.getTotalRegistros() > this.getRegistrosPorPagina();
	}

	// ----------------

	public int valorLink1() {
		return this.retornaValorLink1();
	}

	public int valorLink2() {
		return this.retornaValorLink2();
	}

	public int valorLink3() {
		return this.retornaValorLink3();
	}

	public int valorLink4() {
		return this.retornaValorLink4();
	}

	public int valorLink5() {
		return this.retornaValorLink5();
	}

	public int retornaValorLink1() {
		return this.pagInicio;
	}

	public int retornaValorLink2() {
		return this.pagInicio + 1;
	}

	public int retornaValorLink3() {
		return this.pagInicio + 2;
	}

	public int retornaValorLink4() {
		return this.pagInicio + 3;
	}

	public int retornaValorLink5() {
		return this.pagInicio + 4;
	}

	public void link1Action() {
		this.setPaginaAtual(this.retornaValorLink1());
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void link2Action() {
		this.setPaginaAtual(this.retornaValorLink2());
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void link3Action() {
		this.setPaginaAtual(this.retornaValorLink3());
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void link4Action() {
		this.setPaginaAtual(this.retornaValorLink4());
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	public void link5Action() {
		this.setPaginaAtual(this.retornaValorLink5());
		invocaMetodoPesquisa(getBackingBean(), getNomeMetodo());
	}

	// Exibir links
	public boolean exibeProxima() {
		return this.getUltimaPagina() > this.getPagFinal();
	}

	public boolean exibeUltima() {
		return (this.getUltimaPagina() - this.getPagFinal()) > this.getNumLinks();
	}

	public boolean exibeAnterior() {
		return (this.getPaginaAtual() > this.getNumLinks());
	}

	public boolean exibePrimeira() {
		return (this.getPagInicio() > (2 * this.numLinks));
	}

	public boolean exibeLink1() {
		return (this.retornaValorLink1() <= this.getUltimaPagina()) && !this.exibeLabel1();
	}

	public boolean exibeLink2() {
		return (this.retornaValorLink2() <= this.getUltimaPagina()) && !this.exibeLabel2();
	}

	public boolean exibeLink3() {
		return (this.retornaValorLink3() <= this.getUltimaPagina()) && !this.exibeLabel3();
	}

	public boolean exibeLink4() {
		return (this.retornaValorLink4() <= this.getUltimaPagina()) && !this.exibeLabel4();
	}

	public boolean exibeLink5() {
		return (this.retornaValorLink5() <= this.getUltimaPagina()) && !this.exibeLabel5();
	}

	public boolean exibeLabel1() {
		return this.getPaginaAtual() == this.retornaValorLink1();
	}

	public boolean exibeLabel2() {
		return this.getPaginaAtual() == this.retornaValorLink2();
	}

	public boolean exibeLabel3() {
		return this.getPaginaAtual() == this.retornaValorLink3();
	}

	public boolean exibeLabel4() {
		return this.getPaginaAtual() == this.retornaValorLink4();
	}

	public boolean exibeLabel5() {
		return this.getPaginaAtual() == this.retornaValorLink5();
	}

	// ----------------

	public int getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public int getRegistrosPorPagina() {
		return registrosPorPagina;
	}

	public void setRegistrosPorPagina(int registrosPorPagina) {
		this.registrosPorPagina = registrosPorPagina;
	}

	public int getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public int getPagInicio() {
		return pagInicio;
	}

	public void setPagInicio(int pagInicio) {
		this.pagInicio = pagInicio;
	}

	public int getPagFinal() {
		return pagFinal;
	}

	public void setPagFinal(int pagFinal) {
		this.pagFinal = pagFinal;
	}

	public int getNumLinks() {
		return numLinks;
	}

	public void setNumLinks(int numLinks) {
		this.numLinks = numLinks;
	}

	public void limpaPaginacao() {
		setPaginaAtual(1);
		setPagInicio(1);
		setTotalRegistros(0);
		setPagFinal(5);
	}

	public void limpaPaginacaoCompleto() {
		limpaPaginacao();
		setTotalRegistros(0);
	}

	public int getPrimeiroRegistroPaginacao() {
		return ((getPaginaAtual() - 1) * registrosPorPagina) + 1;
	}

	public int getUltimoRegistro() {
		return (registrosPorPagina * getPaginaAtual()) < getTotalRegistros() ? (registrosPorPagina * getPaginaAtual()) : (int) getTotalRegistros();
	}

	public int getPrimeiroRegistro() {
		return ((getPaginaAtual() - 1) * registrosPorPagina);
	}

	public int getNumeroDeRegistrosExibidos() {
		return getUltimoRegistro() - getPrimeiroRegistro();
	}
}