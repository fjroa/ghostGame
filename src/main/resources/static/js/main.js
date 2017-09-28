$(document).ready(function() {

  	$('input#word').mlKeyboard({layout: 'en_US', is_hidden: 'false'});
  	
  	$('input#word').on('input',function(e){
 		alert('Changed!')
	});
  	
	$(".btn-game").click(function(event) {
		fire_ajax_submit($("#word").text() + event.target.id);
	});
	$("#startGame").click(function() {
		fire_ajax_start('en');
	});
	$("#startSpanishGame").click(function() {
		fire_ajax_start('es');
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
			$('#word').html(data.result);
			$('#panel-title').html(data.msg);

			if (data.active == true) {
				$(".btn-game").prop("disabled", false);
				$("#endGame").show();
			} else {
				$("#startGame").show();
				$("#startSpanishGame").show();
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
			$('#panel-title').html(data.msg);

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

function fire_ajax_start(lang) {

	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/api/start",
		data : JSON.stringify(lang),
		dataType : 'json',
		cache : false,
		timeout : 600000,
		success : function(data) {
			$('#word').html("");
			$(".btn-game").prop("disabled", false);
			$('#panel-title').html("Choose a letter...");
			$("#startSpanishGame").hide();
			$("#startGame").hide();
		},
		error : function(e) {

			var json = "<h4>Ajax Response</h4><pre>" + e.responseText
					+ "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);

		}
	});
}