function formatar_mascara(e, src, mask) {
	var _TXT = 0;
	if (window.event) {
		_TXT = e.keyCode;
	} else if (e.which) {
		_TXT = e.which;
	}
	if (_TXT > 47 && _TXT < 58) {
		var i = src.value.length;
		var saida = mask.substring(0, 1);
		var texto = mask.substring(i)
		if (texto.substring(0, 1) != saida) {
			src.value += texto.substring(0, 1);
		}
		return true;
	} else if (_TXT == 0) {
		return true;
	} else {
		if (_TXT != 8) {
			return false;
		} else {
			return true;
		}
	}
}

function moeda(z){                  
	v = z.value;                
	v=v.replace(/\D/g,"")  //permite digitar apenas  nmeros        
	v=v.replace(/[0-9]{16}/,"invalido")      //limita pra maximo 999.999.999.999,99
	v=v.replace(/(\d{1})(\d{14})$/,"$1.$2")   //coloca ponto antes dos ultimos 14 digitos
	v=v.replace(/(\d{1})(\d{11})$/,"$1.$2")   //coloca ponto antes dos ultimos 11 digitos
	v=v.replace(/(\d{1})(\d{8})$/,"$1.$2")   //coloca ponto antes dos ultimos 8 digitos
	v=v.replace(/(\d{1})(\d{5})$/,"$1.$2")   //coloca ponto antes dos ultimos 5 digitos
	v=v.replace(/(\d{1})(\d{1,2})$/,"$1,$2") //coloca virgula antes dos ultimos 2 digitos
	z.value = v;        
}

function gen_mail_to_link(lhs, rhs, subject) {
	document.write("<a href=\"mailto");
	document.write(":" + lhs + "@");
	document.write(rhs + "?subject=" + subject + "\">" + lhs + "@" + rhs + "<\/a>");
}

function mensagensSistema() {
	var visivel = $('#mensagens-sistema').is('.ERROR, .INFO, .WARN');
	if (visivel) {
		$('#mensagens-sistema').height('auto');
		$('#mensagens-sistema li.ERROR')
				.prepend(
						"<div class=\"icone-msg\">"
								+ "<i class=\"fa fa-times fa-lg\"></i>"
								+ "</div>"
								+ "<button type=\"button\" class=\"close\" onclick=\"$(this).parent().fadeOut();\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>"
								+ "<strong>Atenção: </strong>");
		$('#mensagens-sistema li.WARN')
				.prepend(
						"<div class=\"icone-msg\">"
								+ "<i class=\"fa fa-exclamation fa-lg\"></i>"
								+ "</div>"
								+ "<button type=\"button\" class=\"close\" onclick=\"$(this).parent().fadeOut();\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>"
								+ "<strong>Atenção: </strong>");
		$('#mensagens-sistema li.INFO')
				.prepend(
						"<div class=\"icone-msg\">"
								+ "<i class=\"fa fa-info fa-lg\"></i>"
								+ "</div>"
								+ "<button type=\"button\" class=\"close\" onclick=\"$(this).parent().fadeOut();\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span></button>"
								+ "<strong>Informação: </strong>");
		setTimeout(function() {
			$('#mensagens-sistema').addClass('active')
		}, 1000);
	}
	setTimeout(function() {
		$('#mensagens-sistema').removeClass('active')
	}, 13000);
	setTimeout(function() {
		$('#mensagens-sistema').height(0)
	}, 15000);
}

function animaDropdowns() {
	$(".dropdown").hover(function() {
		$('.dropdown-menu', this).stop(true, true).slideDown("fast");
		$(this).toggleClass('open');
	}, function() {
		$('.dropdown-menu', this).stop(true, true).slideUp("fast");
		$(this).toggleClass('open');
	});
}

function comboboxFilter() {
	$('.combofilter').combobox();
}

function iniciaFuncoes() {
	animaDropdowns();
	mensagensSistema();
	comboboxFilter();
}

function exibeCaixaEntrada() {
	$(".caixa-entrada").toggleClass("active");
}

function somenteNumero(e) {
	var tecla = (window.event) ? event.keyCode : e.which;
	if ((tecla > 47 && tecla < 58) || tecla == 0 || tecla == 13)
		return true;
	else {
		if (tecla != 8)
			return false;
		else
			return true;
	}
}

function validarEmail(email) {
	var filter = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
	if (email.length > 0 && !(filter.test(email))) {
		return false;
	}
	return true;
}

function obterParametro(name) {
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null)
		return "";
	else
		return results[1];
}

function respostaAutenticacao() {
	if (obterParametro('josso_error_type') != '')
		alert('Usu�rio/Senha inv�lidos!');
}

function goAncora(ancora) {

	location.href = '' + ancora + '';
}

// adiciona mascara ao telefone
function formataMascaraTelefone(tel) {
	if (mascaraInteiro(tel) == false) {
		event.returnValue = false;
	}
	return formataCampo(tel, '(00)0000-0000', event);
}

// valida numero inteiro com mascara
function mascaraInteiro() {
	if (event.keyCode < 48 || event.keyCode > 57) {
		event.returnValue = false;
		return false;
	}
	return true;
}

// formata de forma generica os campos
function formataCampo(campo, Mascara, evento) {
	var boleanoMascara;

	var Digitato = evento.keyCode;
	exp = /\-|\.|\/|\(|\)| /g
	campoSoNumeros = campo.value.toString().replace(exp, "");

	var posicaoCampo = 0;
	var NovoValorCampo = "";
	var TamanhoMascara = campoSoNumeros.length;
	;

	if (Digitato != 8) { // backspace
		for (i = 0; i <= TamanhoMascara; i++) {
			boleanoMascara = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".") || (Mascara.charAt(i) == "/"))
			boleanoMascara = boleanoMascara || ((Mascara.charAt(i) == "(") || (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " "))
			if (boleanoMascara) {
				NovoValorCampo += Mascara.charAt(i);
				TamanhoMascara++;
			} else {
				NovoValorCampo += campoSoNumeros.charAt(posicaoCampo);
				posicaoCampo++;
			}
		}
		campo.value = NovoValorCampo;
		return true;
	} else {
		return true;
	}
}

