var availablePrefixes = [];

$(document).ready(
		function() {
			$('input#word').keyboard({
				restrictInput : true,
				preventPaste : true,
				layout: 'custom',
				autoAccept : true,
				customLayout: {
					'normal' : [
						'q w e r t y u i o p',
						'a s d f g h j k l ñ' ,
						'z x c v b n m',
					]					
				},
				change : function(e, keyboard, el) {
					fire_ajax_submit(el.value)
				}
			}).autocomplete({
				source: availablePrefixes
			})
			.addAutocomplete({
				position : {
					of : null,        // when null, element will default to kb.$keyboard
					my : 'right top', // 'center top', (position under keyboard)
					at : 'left top',  // 'center bottom',
					collision: 'flip'
				}
			}).addTyping({
				showTyping : true,
				lockTypeIn : false,
				delay : 750
			});
			
			$("#endGame").click(function() {
				fire_ajax_endgame($("input#word").get(0).value);
			});
			$("#newGame").click(function() {
				$("#newGame").hide();
				$("#notification").show();
				$("#notification2").hide();
				$("#notification3").hide();
				$("#notification4").hide();
				$("#notification5").hide();
				$("#notification6").hide();
				$("input#word").get(0).disabled = false;
				$('#result-word').html('');
				$("input#word").get(0).value = "";
			});
});

function fire_ajax_submit(prefix) {
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/api/ghost",
		data : JSON.stringify(prefix),
		dataType : 'json',
		cache : false,
		timeout : 600000,
		success : function(data) {

			var json = "<h4>Ajax Response</h4><pre>"
					+ JSON.stringify(data, null, 4) + "</pre>";
			$('#feedback').html(json);
			$('input#word').getkeyboard().typeIn(data.letter, 500)
			$('input#word').keyboard().autocomplete({source: data.availablePrefixes});
			if (data.msg == "PLAY") {
				$("#notification").hide();
				$("#notification2").show();
				$("#endGame").show();
			} else if (data.msg == "LOSE") {
				$("#notification2").hide();
				$("#notification4").show();
				$("input#word").get(0).disabled = true;
				$("#endGame").hide();
				$("#newGame").show();
			} else if (data.msg == "LOSE2") {
				$("#notification2").hide();
				$("#notification3").show();
				$("input#word").get(0).disabled = true;
				$("#endGame").hide();
				$("#newGame").show();
			} else if (data.msg == "WIN") {
				$("#notification2").hide();
				$("#notification6").show();
				$('#result-word').html(data.result);
				$("input#word").get(0).disabled = true;
				$("#endGame").hide();
				$("#newGame").show();
			}
		},
		error : function(e) {
			var json = "<h4>Ajax Response</h4><pre>" + e.responseText
					+ "</pre>";
			$('#feedback').html(json);
			console.log("ERROR : ", e);
		}
	});
}

function fire_ajax_endgame(prefix) {
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/api/giveup",
		data : JSON.stringify(prefix),
		dataType : 'json',
		cache : false,
		timeout : 600000,
		success : function(data) {
			var json = "<h4>Ajax Response</h4><pre>"
					+ JSON.stringify(data, null, 4) + "</pre>";
			$('#feedback').html(json);
			$("#notification2").hide();
			$("#notification5").show();
			$('#result-word').html(data.result);
			$("input#word").get(0).disabled = true;
			$("#endGame").hide();
			$("#newGame").show();
		},
		error : function(e) {
			var json = "<h4>Ajax Response</h4><pre>" + e.responseText
					+ "</pre>";
			$('#feedback').html(json);
			console.log("ERROR : ", e);
		}
	});

}