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
		window.location.replace("/bamby/management.html?uuidProj="+($(this).children("td")[0].childNodes[0].nodeValue));			
		
});
}








