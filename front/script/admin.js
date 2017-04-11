
$(document).ready(function() {
	$.get("http://localhost:8080/api-admin/projects").done(function(data) {
		initTableProject();
		var table = $('#projects').DataTable({
			"data" : data,
			"columns" : [ {
				"data" : "id"
			}, {
				"data" : "title"
			} ]
		});
		
		$('#projects tbody').on('click', 'tr', function() {
				document.getElementById("ProjectId").setAttribute("value",($(this).children("td")[0].childNodes[0].nodeValue));			
				initMails($(this).children("td")[0].childNodes[0].nodeValue);
				console.log("info");
				if ( $(this).hasClass('selected') ) {			    	
			        $(this).removeClass('selected');
			    }
			    else {			    	
			        table.$('tr.selected').removeClass('selected');
			        $(this).addClass('selected');
			    }
		});
		
	});
});

function initTableMail(){	
	var thead = document.createElement("thead");
	var tfoot = document.createElement("tfoot");
	var trhead = document.createElement("tr");
	var trfoot = document.createElement("tr");
	var thhead = document.createElement("th");
	var thfoot = document.createElement("th");
	thhead.appendChild(document.createTextNode("mail"));
	thfoot.appendChild(document.createTextNode("mail"));
	trhead.appendChild(thhead);
	trfoot.appendChild(thfoot);
	thead.appendChild(trhead);
	tfoot.appendChild(trfoot);
	document.getElementById("listMails").appendChild(thead);
	document.getElementById("listMails").appendChild(tfoot);
}


function initTableProject(){	
	var thead = document.createElement("thead");
	var tfoot = document.createElement("tfoot");
	var tr = document.createElement("tr");
	var th1 = document.createElement("th");
	var th2 = document.createElement("th");
	th1.appendChild(document.createTextNode("id"));
	th2.appendChild(document.createTextNode("title"));
	tr.appendChild(th1);
	tr.appendChild(th2);
	thead.appendChild(tr);
	tfoot.appendChild(tr);
	document.getElementById("projects").appendChild(thead);
	document.getElementById("projects").appendChild(tfoot);
}


function initMails(id) {
	$.get("http://localhost:8080/api-admin/projects/" + id).done(function(data) {		
		var newData;
		if(null != data.mails){
			newData ="[{\"mail\" : \""+data.mails[0]+"\"}";
			for(var i = 1 ; i<data.mails.length ; i++){
				newData += ",{\"mail\" : \""+data.mails[i]+"\"}";
			}
			newData += "]";
			newData = JSON.parse(newData);
			$('#listMails').empty();
			initTableMail();
			table = $('#listMails').DataTable({
				"bDestroy":"true",
				"data" : newData,
				"columns" : [  {
					"data" : "mail"
					}]			
			});
	    }
		document.getElementById("mails").style.visibility = "visible";		
		$('#listMails tbody').on('click', 'tr', function() {
			document.getElementById("SelectedMail").setAttribute("value",($(this).children("td")[0].childNodes[0].nodeValue));			
			console.log("hello");
			if ( $(this).hasClass('selected') ) {
		        $(this).removeClass('selected');
		    }
		    else {			  
		        table.$('tr.selected').removeClass('selected');
		        $(this).addClass('selected');
		    }
		});
		});
}

$('#btnInputMail').on('click',function() {
	var value = document.getElementById('textInputMail').value;
	var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if(regex.test(value)){
		value = {
				"email" : value
		}
		$.ajax({
			url: "http://localhost:8080/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/emails",
			type: "POST",
			data: JSON.stringify(value),
			contentType: "application/json",
			dataType: 'json'}).done(function(data) {
				$('#listMails').DataTable().row.add({"mail":value.email}).draw(true);
		});	
		
	}
	else{
		alert("Bad e-mail");
	}
});



$('#deleteMail').click( function () {
	if(confirm("Are you sure?")){
		var value = {
				"email" : document.getElementById("SelectedMail").getAttribute("value")
		}		
		console.log(value);
		$.ajax({
			url: "http://localhost:8080/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/deleteUser",
			type: "POST",
			data: JSON.stringify(value),
			contentType: "application/json"}).done(function() {
				console.log("hello");
				$('#listMails').DataTable().row('.selected').remove().draw( true );
			}).fail(function(xhr, status, error){
				alert("Error , \n status code ="+status+"\n Error "+xhr+" message : \n"+error);
			});		
		
	}
} );


$('#btnInputProj').on('click',function() {
	var value = document.getElementById('textInputProj').value;
	var regex = /^[a-zA-Z ]+$/;
	if(regex.test(value)){
		value = {
				"title" : value
		}
		console.log(value);
		$.ajax({
			url: "http://localhost:8080/api-admin/projects",
			type: "POST",
			data: JSON.stringify(value),
			contentType: "application/json",
			dataType: 'json'
		}).done(function(data) {
			$('#projects').DataTable().row.add(data).draw(true);
		});		
	}
	else{
		alert("Bad Name");
	}
});


$('#deleteProj').click( function () {
	if(confirm("Are you sure?")){
		console.log(document.getElementById("ProjectId").getAttribute("value"));		
		$.post("http://localhost:8080/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/delete").done(function(){
			$('#projects').DataTable().row('.selected').remove().draw( true );
		});
		
	}
} );


