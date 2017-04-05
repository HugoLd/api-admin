(function() {
	var defaultMood = window.location.search.substring(1).split("&")[3].substring(5);
	$("input[name=emotion]").setChecked(defaultMood);	
})();



$('#moodSelector').submit(function (event) {
	event.preventDefault();

	var args = window.location.search.substring(1).split("&");
	var baseLink = "http://localhost:8080/api-admin/projects";

	var projectUUID = args[0].substring(9);
	var moodUUID = args[1].substring(5);
	var moodDate = args[2].substring(5);
	
	var data = {
		"uuid": projectUUID,
		"uuidProj": args[1],
		"mood": $("input[name=emotion]:checked").val(),
		"comment": $("input[name=comment]").val(),
		"date":args[2]
	};

	$.ajax({
		url: baseLink + "/" + projectUUID + "/userMood",
		type: "POST",
		data: data,
		contentType: "application/json",
	}).done(function(){
		//todo trqiter le retour du done
	}).fail(function(){
		//todo trqiter le retour du fail
	});

});