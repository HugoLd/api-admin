/*
 * when document is ready init table project
 */
$(document).ready(function() {
	$.get("/api-admin/projects").done(function(data) {
		initDataTableProject(data);
	}).fail(function(xhr, status, error){
		alert("Error , \n status code ="+status+"\n Error "+xhr+" message : \n"+error);
	});	;
});


/*
 * initialize a datatable object for project list
 */
function initDataTableProject(data){
	console.log(data);
	var table =  $('#projectList').DataTable({
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
	$('#projectList tbody').on('click', 'tr', function() {
		window.location.replace("/bamby/stats.html?uuidProj="+($(this).children("td")[0].childNodes[0].nodeValue));			
		
});
}








