$(document).ready(function() {

  	$('input#word').keyboard({
		layout: 'custom',
		customLayout: {
			'normal' : [
				// "n(a):title_or_tooltip"; n = new key, (a) = actual key, ":label" = title_or_tooltip (use an underscore "_" in place of a space " ")
				'\u03b1(a):lower_case_alpha_(type_a) \u03b2(b):lower_case_beta_(type_b) \u03b3(c):lower_case_gamma_(type_c) \u03b4(d):lower_case_delta_(type_d) \u03b5(e):lower_case_epsilon_(type_e) \u03b6(f):lower_case_zeta_(type_f) \u03b7(g):lower_case_eta_(type_g)', // lower case Greek
				'{shift} {accept} {cancel}'
			],
			'shift' : [
				'\u0391(A) \u0392(B) \u0393(C) \u0394(D) \u0395(E) \u0396(F) \u0397(G)', // upper case Greek
				'{shift} {accept} {cancel}'
			]
		},
		usePreview: false // no preveiw
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