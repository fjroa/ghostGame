$(document).ready(function() {

	$(".btn-game").click(function(event) {
		fire_ajax_submit($("#word").text() + event.target.id);
	});
	$("#startGame").click(function() {
		$('#word').html("");
		$(".btn-game").prop("disabled", false);
		$('#panel-title').html("Choose a letter...");
		$(this).hide();
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
