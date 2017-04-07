(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	if (uuid.length < 10) {
		console.log("null");
		alert("Bad project ID , please check url");
	} else {
		console.log(uuid);
		var url = "http://localhost:8080/api-admin/projects/" + uuid;
		$.get(url).done(function(data) {
			var titl = document.createElement("h2");
			var id = document.createElement("h2");
			titl.appendChild(document.createTextNode(" Project Title : "+ data.title));
			id.appendChild(document.createTextNode(" Project ID : " + data.id))
			document.getElementById("heads").appendChild(titl);
			document.getElementById("heads").appendChild(id);
		});
	}
})();

(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	var url = "http://localhost:8080/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {
		console.log(data);
		if (data.length != 0) {
			for (var i = 0; i < data.length; i++) {
				var date = document.createElement("h3");
				date.appendChild(document.createTextNode(data[i][0].date));
				document.getElementById("moods").appendChild(date);
				var tab = document.createElement("table");
				var trHead = document.createElement("tr");
				var thCom = document.createElement("th");
				var thMood = document.createElement("th");
				thMood.appendChild(document.createTextNode("Moods"));
				thCom.appendChild(document.createTextNode("Comments"));
				trHead.appendChild(thMood);
				trHead.appendChild(thCom);
				tab.appendChild(trHead);
				for(var y = 0 ; y < data[i].length;y++){
					var tr = document.createElement("tr");
					var mood = document.createElement("td");
					var com = document.createElement("td");
					mood.appendChild(document.createTextNode(data[i][y].mood));
					com.appendChild(document.createTextNode(data[i][y].comment));
					tr.appendChild(mood);
					tr.appendChild(com);
					tab.appendChild(tr);
				}
				document.getElementById("moods").appendChild(tab);

			}
		} else {
			var noMoods = document.createElement("p");
			noMoods.appendChild(document.createTextNode("No moods found"));
			document.getElementById("moods").appendChild(noMoods);
		}

	});
})();