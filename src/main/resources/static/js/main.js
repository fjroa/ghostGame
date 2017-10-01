$(document).ready(
		function() {

			var availableTags = [ "ActionScript", "AppleScript", "Asp",
					"BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion",
					"Erlang", "Fortran", "Groovy", "Haskell", "Java",
					"JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby",
					"Scala", "Scheme" ];

			$('input#word').keyboard({
				openOn : null,
				stayOpen : true,
				restrictInput : true,
				preventPaste : true,
				layout: 'custom',
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
				source : availableTags
			}).addAutocomplete({
				// set position of autocomplete popup
				position : {
					of : null,
					my : 'right top',
					at : 'left top',
					collision : 'flip'
				},
				// custom autocomplete widget settings
				data : '',
				events : ''
			}).addTyping({
				showTyping : true,
				lockTypeIn : false,
				delay : 250
			}).getkeyboard().reveal();

			$(".btn-game").click(function(event) {
				fire_ajax_submit($("#word").text() + event.target.id);
			});

			$("#endGame").click(function() {
				fire_ajax_endgame($("#word").text());
			});
			$(".btn-game").prop("disabled", true);
			$("#endGame").hide();
		});

function fire_ajax_submit(prefix) {

	$(".btn-game").prop("disabled", true);

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

			if (data.active == true) {
				$(".btn-game").prop("disabled", false);
				$("#endGame").show();
			} else {
				$("#endGame").hide();
			}
		},
		error : function(e) {

			var json = "<h4>Ajax Response</h4><pre>" + e.responseText
					+ "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);
			$(".btn-game").prop("disabled", false);

		}
	});
}

function fire_ajax_endgame(prefix) {

	$(".btn-game").prop("disabled", true);

	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/api/challenge",
		data : JSON.stringify(prefix),
		dataType : 'json',
		cache : false,
		timeout : 600000,
		success : function(data) {
			var json = "<h4>Ajax Response</h4><pre>"
					+ JSON.stringify(data, null, 4) + "</pre>";
			$('#feedback').html(json);
			$('#panel-title').html(data.result);

			$("#startGame").show();
			$("#startSpanishGame").show();
			$("#endGame").hide();

		},
		error : function(e) {
			var json = "<h4>Ajax Response</h4><pre>" + e.responseText
					+ "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);
			$(".btn-game").prop("disabled", false);

		}
	});

}