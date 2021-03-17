
/**
 * Exibe/Esconde as mensagens de erro/sucesso do sistema.
 * */
function exibeEscondeMensagemSistema(){
    // Initially, hide them all
    hideAllMessages();            
    // Exibe mensagem
    exibeMensagem();

    // When message is clicked, hide it
    //Esconde mensagem quando clicado
    escondeMensagem();
}            



// Define os tipos de mensagens
var myMessages = ['INFO','WARNING','ERROR','SUCCESS'];

/**
 * Esconde todas as mensagens.
 * */
function hideAllMessages(){
	var messagesHeights = new Array(); // this array will store height for each
	for (i=0; myMessages.length > i; i++){
		var outerHeight = $('.' + myMessages[i]).outerHeight();
		
		if(outerHeight != null){
			$('.' + myMessages[i]).css('margin-top', -(outerHeight + 10)); //move element outside viewport
		} 				 
	}
}

/**
 * Exibe as mensagens de acordo com o tipo
 * */
function showMessage(type){	
	hideAllMessages();
	if(($('.'+type).html() != null)){
		
		if(($('.'+type).html() != "")){						
				$('.'+type).animate({marginTop:"0"}, 500);
		}	  
	}		  
}

/**
 * Exibe todas as mensagens.
 * */
function exibeMensagem(){
  for(var i=0; myMessages.length > i;i++){
         showMessage(myMessages[i]);
  }
}

/**
 * Esconde a mensagem no evento onclick.
 * */
function escondeMensagem(){
	$('.message').click(function(){
        //$(this).animate({marginTop: -($(this).outerHeight() + 10) }, 500); 
        $("#mensagens-sistema").removeClass('INFO WARNING ERROR SUCCESS');
        $("#mensagens-sistema").html("");
    });
	/*setTimeout(function(){hideAllMessages()},7000);*/
	
}