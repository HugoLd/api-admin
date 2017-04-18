/*
 * when document is ready init table project
 */
$(document).ready(function() {
	$.get("/api-admin/projects").done(function(data) {
		initDataTableProject(data);
	});
});


/*
 * initialize a datatable object for project list
 */
function initDataTableProject(data){
	var table =  $('#projects').DataTable({
		"data" : data,
		"initComplete" : function(){
			setOnClickListenerProject(this);
		} ,
		"columns" : [ {
			"data" : "id"
		}, {
			"data" : "title"
		} ]
	});
	
}


/*
 * Set the row onClick listener for project table
 */
function setOnClickListenerProject(table){
	$('#projects tbody').on('click', 'tr', function() {
		document.getElementById("ProjectId").setAttribute("value",($(this).children("td")[0].childNodes[0].nodeValue));			
		initMails($(this).children("td")[0].childNodes[0].nodeValue);
		if ( $(this).hasClass('selected') ) {			    	
	        $(this).removeClass('selected');
	    }
	    else {			    	
	        table.$('tr.selected').removeClass('selected');
	        $(this).addClass('selected');
	    }
});
}

/*
 * init table mail 
 */
function initMails(id) {
	$.get("/api-admin/projects/" + id).done(function(data) {		
		var newData = addDataMail(data);
		initDataTableMail(newData);
		});
}


/*
 * init datatable object for mails list
 */
function initDataTableMail(newData){
	$('#listMails').empty();
	initTableMail();
	var table = $('#listMails').DataTable({
		"bDestroy":"true",
		"initComplete" : function(){
			setOnClickListenerMail(this);
		} ,
		"data" : newData,
		"columns" : [  {
			"data" : "mail"
			}]			
	});
document.getElementById("mails").style.visibility = "visible";	
}


/*
 * set a onClick listener to mail row
 */
function setOnClickListenerMail(table){
	$('#listMails tbody').on('click', 'tr', function() {
		document.getElementById("SelectedMail").setAttribute("value",($(this).children("td")[0].childNodes[0].nodeValue));			
		if ( $(this).hasClass('selected') ) {
	        $(this).removeClass('selected');
	    }
	    else {			  
	        table.$('tr.selected').removeClass('selected');
	        $(this).addClass('selected');
	    }
	});
}


/*
 * convert from server returned data to json for datatable
 */
function addDataMail(data){
	var newData;		
	if(null != data.mails){
		newData = [{
				"mail" : data.mails[0]			
		}];
		for(var i = 1 ; i<data.mails.length ; i++){
			newData.push({"mail" : data.mails[i]});
		}
	}else{
		newData = {
				"mail" : null
		}
	}
	return newData;
}

/*
 * listener for adding a new mail in the DB
 */
$('#btnInputMail').on('click',function() {
	var value = document.getElementById('textInputMail').value;
	var regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	if(regex.test(value)){
		value = {
				"email" : value
		}
		$.ajax({
			url: "/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/emails",
			type: "POST",
			data: JSON.stringify(value),
			contentType: "application/json",
			dataType: 'json'}).done(function(data) {
				$('#listMails').DataTable().row.add({"mail":value.email}).draw(true);
				document.getElementById('textInputMail').value = "";
		});			
	}
	else{
		alert("Bad e-mail");
	}
});


/*
 * listener for deleting a mail in DB
 */
$('#deleteMail').click( function () {
	if(confirm("Are you sure?")){
		var value = {
				"email" : document.getElementById("SelectedMail").getAttribute("value")
		}		
		
		$.ajax({
			url: "/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/user",
			type: "DELETE",
			data: JSON.stringify(value),
			contentType: "application/json"}).done(function() {
				$('#listMails').DataTable().row('.selected').remove().draw( true );
			}).fail(function(xhr, status, error){
				alert("Error , \n status code ="+status+"\n Error "+xhr+" message : \n"+error);
			});		
	}
} );

/*
 * listener for adding a new project in the DB
 */
$('#btnInputProj').on('click',function() {
	var value = document.getElementById('textInputProj').value;
	var regex = /^[a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*$/;
	if(regex.test(value)){
		value = {
				"title" : value
		}
		$.ajax({
			url: "/api-admin/projects",
			type: "POST",
			data: JSON.stringify(value),
			contentType: "application/json",
			dataType: 'json'
		}).done(function(data) {
			$('#projects').DataTable().row.add(data).draw(true);
			document.getElementById('textInputMail').value = "";
		}).fail(function(xhr, status, error){
			alert("Error , \n status code ="+status+"\n Error message : \n"+error);
		});		
	}
	else{
		alert("Bad Name");
	}
});


/*
 * listener for deleting a project in the DB
 */
$('#deleteProj').click( function () {
	if(confirm("Are you sure?")){		
		console.log("i'msure");
		
		$.ajax({
			url: "/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value"),
			type: "DELETE",
			contentType: "application/json"
		}).done(function(){
			$('#projects').DataTable().row('.selected').remove().draw( true );
		}).fail(function(xhr, status, error){
			alert("Error , \n status code ="+status+"\n Error message : \n"+error);
		});	
		
	}
} );

/*
 * listener for deleting a project in the DB
 */
$('#purgeProj').click( function () {
	if(confirm("Do you really want to purge this project?")){		
		$.post("/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/purge").done(function(){
			alert("Purge success");
		}).fail(function(xhr, status, error){
			alert("Error , \n status code ="+status+"\n Error message : \n"+error);
		});
		
	}
} );

/*
 * listener for getting data on a project
 */
$('#seeStats').click( function () {			
	window.location.replace("/bamby/stats.html?uuidProj="+document.getElementById("ProjectId").getAttribute("value"));
} );
/*
 * (re)initialise mail table components to be filled by datatables
 */
function initTableMail(){	
	$('#listMails').html($('#templateMail').html());
}
/*
 * send the mail to project's members
 */
$('#mailBtn').click( function () {
	$.post("/api-admin/projects/"+document.getElementById("ProjectId").getAttribute("value")+"/sendMail").done(function(){
		alert("Sent");
	});
});


