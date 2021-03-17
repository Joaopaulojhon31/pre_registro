$('#formPreRegistro').ready(function() {
	verificaQuantidadeGemeos()
	verificaTipoPessoaDeclarantePreRegistro()
	verificaStatusPai()
	verificaOpcaoRegistro()
})

function AbreNavTabDadosMae() {

	$('.nav-tabs a[href="#menu1"]').tab('show')
	$('html,body').scrollTop(0);
}
function AbreNavTabDadosPai() {
	$('.nav-tabs a[href="#menu2"]').tab('show')
	$('html,body').scrollTop(0);
}
function AbreNavTabDadosCrianca() {
	$('.nav-tabs a[href="#home"]').tab('show')
	$('html,body').scrollTop(0);
}
function AbreNavTabDadosDeclarante() {
	$('.nav-tabs a[href="#menu3"]').tab('show')
	$('html,body').scrollTop(0);
}
function AbreNavTabDadosCartorio() {
	$('.nav-tabs a[href="#menu4"]').tab('show')
	$('html,body').scrollTop(0);
}

function AbreNavTabInformacoes() {
	$('.nav-tabs a[href="#menu5"]').tab('show')
	$('html,body').scrollTop(0);
}

function verificaTipoPessoaDeclarantePreRegistro(){	
	var verificaPessoaDeclarante=((document.getElementById("formPreRegistro:comboPessoaDeclarantePreRegistro")||{}).value)||"";
	if(verificaPessoaDeclarante == "Outro"){
		exibeDivInformacoesDeclarante()
	}else
		ocultaDivInformacoesDeclarante()
}

function exibeDivInformacoesDeclarante(){
	if(document.getElementById("dados-declarante")!=null)
		document.getElementById("dados-declarante").style.display = "block";
}

function ocultaDivInformacoesDeclarante(){
	if(document.getElementById("dados-declarante")!=null)
		document.getElementById("dados-declarante").style.display = "none";
}

function verificaQuantidadeGemeos() {	
	var verificaSelectGemeos=((document.getElementById("formPreRegistro:comboQuantidadeGemeos")||{}).value)||"";
			if (verificaSelectGemeos == "Não") {
		exibeDivNaoGemeo()
	} else if (verificaSelectGemeos == "Gêmeos") {
		exibeDivGemeos()
	} else if (verificaSelectGemeos == "Trigêmeos") {
		exibeDivTrigemeos()
	} else if (verificaSelectGemeos == "Quadrigêmeos") {
		exibeDivQuadrigemeos()
	} else if (verificaSelectGemeos == "Quíntuplos") {
		exibeDivQuintuplos()
	}
}
function exibeDivNaoGemeo() {
	if(document.getElementById("segundo-gemelar")!=null)
	document.getElementById("segundo-gemelar").style.display = "none";
	if(document.getElementById("terceiro-gemelar")!=null)
	document.getElementById("terceiro-gemelar").style.display = "none";
	if(document.getElementById("quarto-gemelar")!=null)
	document.getElementById("quarto-gemelar").style.display = "none";
	if(document.getElementById("quinto-gemelar")!=null)
	document.getElementById("quinto-gemelar").style.display = "none";
	
}
function exibeDivGemeos() {
	if(document.getElementById("segundo-gemelar")!=null)
	document.getElementById("segundo-gemelar").style.display = "block";
	if(document.getElementById("terceiro-gemelar")!=null)
	document.getElementById("terceiro-gemelar").style.display = "none";
	if(document.getElementById("quarto-gemelar")!=null)
	document.getElementById("quarto-gemelar").style.display = "none";
	if(document.getElementById("quinto-gemelar")!=null)
	document.getElementById("quinto-gemelar").style.display = "none";
}
function exibeDivTrigemeos() {
	if(document.getElementById("segundo-gemelar")!=null)
		document.getElementById("segundo-gemelar").style.display = "block";
		if(document.getElementById("terceiro-gemelar")!=null)
		document.getElementById("terceiro-gemelar").style.display = "block";
		if(document.getElementById("quarto-gemelar")!=null)
		document.getElementById("quarto-gemelar").style.display = "none";
		if(document.getElementById("quinto-gemelar")!=null)
		document.getElementById("quinto-gemelar").style.display = "none";
}
function exibeDivQuadrigemeos() {
	if(document.getElementById("segundo-gemelar")!=null)
		document.getElementById("segundo-gemelar").style.display = "block";
		if(document.getElementById("terceiro-gemelar")!=null)
		document.getElementById("terceiro-gemelar").style.display = "block";
		if(document.getElementById("quarto-gemelar")!=null)
		document.getElementById("quarto-gemelar").style.display = "block";
		if(document.getElementById("quinto-gemelar")!=null)
		document.getElementById("quinto-gemelar").style.display = "none";
}
function exibeDivQuintuplos() {
	
	if(document.getElementById("segundo-gemelar")!=null)
		document.getElementById("segundo-gemelar").style.display = "block";
		if(document.getElementById("terceiro-gemelar")!=null)
		document.getElementById("terceiro-gemelar").style.display = "block";
		if(document.getElementById("quarto-gemelar")!=null)
		document.getElementById("quarto-gemelar").style.display = "block";
		if(document.getElementById("quinto-gemelar")!=null)
		document.getElementById("quinto-gemelar").style.display = "block";
}
function verificaOpcaoRegistro() {
	var opcaoRegistro=((document.getElementById("formPreRegistro:comboOpcaoRegistro")||{}).value)||"";	
	if (opcaoRegistro == "Local de Nascimento -Unidade Interligada") {	
		if(document.getElementById("serventia-pre-registro")!=null)
		document.getElementById("serventia-pre-registro").style.display = "none";
	} else
		if(document.getElementById("serventia-pre-registro")!=null)
		document.getElementById("serventia-pre-registro").style.display = "block";
}
function verificaStatusPai() {
	var StatusPai=((document.getElementById("formPreRegistro:comboSituacaoPai")||{}).value)||"";
	if (StatusPai == "Declarado") {
		setaConfigStatusPaiDeclarado()
	} else if (StatusPai == "Não declarado") {
		setaConfigStatusPaiNaoDeclarado()

	} else if (StatusPai == "Suposto") {
		setaConfigStatusPaiSuposto()
	}
}
function setaConfigStatusPaiSuposto() {
	document.getElementById("informacoes-pai").style.display = "block";
	document.getElementById("botaoAproveitarDadosCepMae").style.display = "none";
	document.getElementById("formPreRegistro:nome-pai-label").style.color = "black";
	document.getElementById("formPreRegistro:cpf-pai-label").style.color = "black";
	document.getElementById("formPreRegistro:contato-pai-label").style.color = "black";
	document.getElementById("formPreRegistro:cep-pai-label").style.color = "black";
	document.getElementById("formPreRegistro:nacionalidade-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:residencia-uf-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:residencia-municipio-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:residencia-distrito-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:residencia-bairro-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:residencia-logradouro-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:residencia-numero-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:profissao-pai-n").style.color = "black";
	document.getElementById("formPreRegistro:data-nascimento-pai-n").style.color = "black";
}

function setaConfigStatusPaiDeclarado(){
	document.getElementById("informacoes-pai").style.display = "block";
	document.getElementById("botaoAproveitarDadosCepMae").style.display = "block";
	document.getElementById("formPreRegistro:nacionalidade-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:residencia-uf-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:residencia-municipio-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:residencia-distrito-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:residencia-bairro-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:residencia-logradouro-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:residencia-numero-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:profissao-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:data-nascimento-pai-n").style.color ="red";
    document.getElementById("formPreRegistro:nome-pai-label").style.color = "red";
	document.getElementById("formPreRegistro:cpf-pai-label").style.color = "red";
	document.getElementById("formPreRegistro:contato-pai-label").style.color = "red";
	document.getElementById("formPreRegistro:cep-pai-label").style.color = "red";
}

function setaConfigStatusPaiNaoDeclarado(){
	document.getElementById("informacoes-pai").style.display = "none";
}
