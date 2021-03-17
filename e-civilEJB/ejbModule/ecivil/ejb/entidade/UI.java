package ecivil.ejb.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ui", schema = "ecivil")
public class UI {

	@Id
	@Column(name = "ui_idui")
	private Integer idUI;
	
	@Column(name = "ui_hospital")
	private String nomeHospital;

	@Column(name = "ui_endereco")
	private String enderecoUI;
	
	@Column(name = "ui_complemento")
	private String complementoUI;
	
	@Column(name = "ui_bairro")
	private String bairroUI; 
	
	@Column(name = "ui_cep")
	private String cepUI;
	
	@Column(name = "ui_distrito")
	private String distritoUI; 
	
	@Column(name = "ui_telefone")
	private String telefoneUI;
	
	@Column(name = "ui_cnpj")
	private String cnpjUI;
	
	@Column(name = "ui_nomeresponsavel")
	private String nomeResponsavelUI;
	
	@Column(name = "ui_idmunicipio")
	private Integer idMunicipioUI;

	public Integer getIdUI() {
		return idUI;
	}

	public String getNomeHospital() {
		return nomeHospital;
	}

	public String getEnderecoUI() {
		return enderecoUI;
	}

	public String getComplementoUI() {
		return complementoUI;
	}

	public String getBairroUI() {
		return bairroUI;
	}

	public String getCepUI() {
		return cepUI;
	}

	public String getDistritoUI() {
		return distritoUI;
	}

	public String getTelefoneUI() {
		return telefoneUI;
	}

	public String getCnpjUI() {
		return cnpjUI;
	}

	public String getNomeResponsavelUI() {
		return nomeResponsavelUI;
	}

	public Integer getIdMunicipioUI() {
		return idMunicipioUI;
	}

	public void setIdUI(Integer idUI) {
		this.idUI = idUI;
	}

	public void setNomeHospital(String nomeHospital) {
		this.nomeHospital = nomeHospital;
	}

	public void setEnderecoUI(String enderecoUI) {
		this.enderecoUI = enderecoUI;
	}

	public void setComplementoUI(String complementoUI) {
		this.complementoUI = complementoUI;
	}

	public void setBairroUI(String bairroUI) {
		this.bairroUI = bairroUI;
	}

	public void setCepUI(String cepUI) {
		this.cepUI = cepUI;
	}

	public void setDistritoUI(String distritoUI) {
		this.distritoUI = distritoUI;
	}

	public void setTelefoneUI(String telefoneUI) {
		this.telefoneUI = telefoneUI;
	}

	public void setCnpjUI(String cnpjUI) {
		this.cnpjUI = cnpjUI;
	}

	public void setNomeResponsavelUI(String nomeResponsavelUI) {
		this.nomeResponsavelUI = nomeResponsavelUI;
	}

	public void setIdMunicipioUI(Integer idMunicipioUI) {
		this.idMunicipioUI = idMunicipioUI;
	}
}

