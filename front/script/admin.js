$(document).ready(function() {
	$.get("http://localhost:8080/api-admin/projects").done(function(data) {
		var table = $('#projects').DataTable({
			"data" : data,
			"columns" : [ {
				"data" : "id"
			}, {
				"data" : "title"
			} ]
		});
		
		$('#projects tbody').on('click', 'tr', function() {
			if(document.getElementById("mails").getAttribute("hidden") == "false"){
				console.log("fff"+document.getElementById("mails").getAttribute("hidden"));
				updateMails($(this).children("td")[0].childNodes[0].nodeValue , table);
			}
			else{
				console.log("###"+document.getElementById("mails").getAttribute("hidden"));
				initMails();	
				document.getElementById("mails").setAttribute("hidden","false");
			}
		});
	});

});

function initMails(id) {
	$.get("http://localhost:8080/api-admin/projects/" + id).done(function(data) {
	
				var table = $('#listMails').DataTable({
					"data" : data,
					"columns" : [ {
						"data" : "id"
					}, {
						"data" : "title"
					}, {
						"data" : "mails"
					} ]
				});
				document.getElementById("mails").style.visibility = "visible";

			});
}


function updateMails(id,table) {
	console.log(table);
	var newData;
	$.get("http://localhost:8080/api-admin/projects/" + id).done(function(data) {
		console.log(table);
				newData ="[{\"mail\" : \""+data.mails[0]+"\"}";
				for(var i = 1 ; i<data.mails.length ; i++){
					newData += ",{\"mail\" : \""+data.mails[i]+"\"}";
				}
				newData += "]";
				console.log(newData);
		
			});

	table.fnClearTable();
	console.log(data);
	table.fnAddData(newData);				

}