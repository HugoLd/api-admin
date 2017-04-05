(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	var url = "http://localhost:8080/api-admin/projects/" + uuid;
	$.get(url).done(function(data) {
		var titl = document.createElement("h2");
		var id = document.createElement("h2");
		titl.appendChild(document.createTextNode(data[0].title));
		id.appendChild(document.createTextNode(data[0].id))
		document.getElementById("heads").appendChild(titl);
		document.getElementById("heads").appendChild(id);
		console.log(data[0]);

	});
})();

(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	var url = "http://localhost:8080/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {
		console.log(data);
		for (var i = 0; i < data.length; i++) {
			var titl = document.createElement("p");
			titl.appendChild(document.createTextNode(data));
			document.getElementById("moods").appendChild(titl);
			
		}

	});
})();