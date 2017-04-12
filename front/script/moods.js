/*
 * set a default mood from url
 */
(function() {
	var defaultMood = window.location.search.substring(1).split("&")[3].substring(5);	
	$("input[name=emotion][value=" + defaultMood + "]").prop('checked', true);
})();

/*
 * Add project's name to the title
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(9);
	var url = "http://localhost:8080/api-admin/projects/" + uuid;
	$.get(url).done(function(data) {
		document.getElementById("title").appendChild(document.createTextNode(data.title));

	});
})();

/*
 * handle submit event to add mood in the DB
 */
$('#moodSelector').submit(function (event) {
	event.preventDefault();

	var args = window.location.search.substring(1).split("&");
	var projectUUID = args[0].substring(9);
	var url = "http://localhost:8080/api-admin/projects/"+projectUUID+"/moods";
	var moodUUID = args[1].substring(5);
	var moodDate = args[2].substring(5);
	var data = {
		"uuid": moodUUID,
		"mood": $("input[name=emotion]:checked").val(),
		"comment": $("textarea[name=comment]").val(),
		"date":moodDate
	};
	
	$.ajax({
		url: url,
		type: "POST",
		data: JSON.stringify(data),
		contentType: "application/json",
		dataType: 'json'
	}).done(function(){
		alert("Thanks for voting =) \n Please come back soon !")
	}).fail(function(xhr, status, error){
		alert("Error , \n status code ="+status+"\n Error message : \n"+error);
	});

});