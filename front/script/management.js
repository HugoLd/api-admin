/*
 * set heads with url params
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(9);
	if (uuid.length < 10) {
		alert("Bad project ID , please check url");
	} else {
		var url = "/api-admin/projects/" + uuid;
		$.get(url).done(
				function(data) {
					document.getElementById("title").appendChild(document.createTextNode("Manage moods for " +data.title));
					
				});
	}
})();

/*
 * global method for adding moods count
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(9);
	var url = "/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {
		if (data.length != 0) {			
			makeStats(data);			
		}
	});
})();
/*
 * global method for adding table
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(9);
	var url = "/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {	
			createTable();
			addHeads();		
			eachDaysMoods(data);

	}).fail(function(xhr, status, error){
		var noMoods = document.createElement("p");
		noMoods.appendChild(document.createTextNode("No moods found"));
		document.getElementById("moods").appendChild(noMoods);
		alert("Error , \n status code ="+status+"\n Error "+xhr+" message : \n"+error);		
	});	
		
})();


/*
 * getting data for each days
 */
function eachDaysMoods(data){	
	var values = [0,0,0,0,0];
	for (var i = 0; i < data.length; i++) {
		var listList=[[],[],[],[],[]];
		createRow(data,i);
		var tdMood = document.createElement("td");
		tdMood.setAttribute("id","tabData"+i);
		document.getElementById("tabRow"+i).appendChild(tdMood);
		for (var y = 0; y < data[i].length; y++) {
			
			listList[data[i][y].mood].push(createImg(data[i],y));
			
		}		
		browseLists(listList,i);
	}
}
/*
 * order moods (DESC)
 */
function browseLists(listList,index){
	for(var i = 4;i>=0;i--){		
		addMoodsToData(listList[i],index);
	}
}
/*
 * add a mood to the <td>
 */
function addMoodsToData(listToAdd,index){
	for(var i = 0 ; i<listToAdd.length ; i++){
		document.getElementById("tabData"+index).appendChild(listToAdd[i]);
	}
}
/*
 * Add a new row to the tab
 */
function createRow(data,index){
	var trRow = document.createElement("tr");
	trRow.setAttribute("id","tabRow"+index);
	var tdDate = document.createElement("td");
	tdDate.appendChild(document.createTextNode(data[index][0].date));
	trRow.appendChild(tdDate)
	document.getElementById("tabMood").appendChild(trRow);
}

/*
 * add heads to the tab
 */
function addHeads(){
	var trHead = document.createElement("tr");
	var thDate = document.createElement("th");
	var thMood = document.createElement("th");
	thDate.appendChild(document.createTextNode("Date"));
	thMood.appendChild(document.createTextNode("Mood"));
	trHead.appendChild(thDate);
	trHead.appendChild(thMood);
	document.getElementById("tabMood").appendChild(trHead);
}

/*
 * create the tab
 */
function createTable(){
	var tab = document.createElement("table");
	tab.setAttribute("id","tabMood");
	document.getElementById("moods").appendChild(tab);
}

/*
 * return the picture depending on params
 */
function createImg(data,index){
	var imgMood = document.createElement("img");
	imgMood.setAttribute("alt", "Mood");
	if (data[index].comment != null) {
		imgMood.setAttribute("src", "img/Csticker" + data[index].mood+".jpg");
		imgMood.setAttribute("title", data[index].comment);
		imgMood.setAttribute("text", "*");
	}
	else{
		imgMood.setAttribute("src", "img/sticker" + data[index].mood+".jpg");
	}
	return imgMood;
	
}

/*
 * global method for adding moods count
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(9);
	var url = "/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {
		if (data.length != 0) {			
			eachDaysCount(data);			
		}
	});
})();
/*
 * get data for each days
 */
function eachDaysCount(data){	
	var values = [0,0,0,0,0]
	for (var i = 0; i < data.length; i++) {
		var listList=[[],[],[],[],[]];
		for (var y = 0; y < data[i].length; y++) {
			values[data[i][y].mood] ++;			
		}
		browseLists(listList,i);
	}
	for(var v = 4;v>=0;v--){
		createDiv(values[v],v);
	}
}
/*
 * clone the mood's div
 */
function createDiv(value ,index){
	var clone = document.getElementById("countDuplicate").cloneNode(true);
	clone.setAttribute("id","count"+index);
	clone.appendChild(document.createTextNode(value))
	document.getElementById("count").appendChild(clone);
}


/*
 * get data for each moods
 */
function makeStats(data){	
	var values = [0,0,0,0,0];
	var listCom = [];
	var avg = 0;
	var total = 0;
	for (var i = 0; i < data.length; i++) {
		for (var y = 0; y < data[i].length; y++) {
			values[data[i][y].mood] ++;	
			if(null != data[i][y].comment && null != data[i][y].date){
				listCom.push({"date" :data[i][y].date , "comment" : data[i][y].comment});
			}
		}
	}
	for(var v = 4;v>=0;v--){
		total += values[v];
		avg += (values[v]*v);
	}
	avg = avg/total;
	createDivBefore("Average mood : "+Math.trunc(avg),Math.trunc(avg));
	useC3(values,Math.round(avg * 100) / 100);
	initCommentTable(listCom);
	
}
/*
 * clone the mood's div
 */
function createDivBefore(value ,index){
	var clone = document.getElementById("countDuplicate").cloneNode(true);
	clone.setAttribute("id","count"+index);
	clone.appendChild(document.createTextNode(value))
	document.getElementById("stats").insertBefore(clone,document.getElementById("stats").firstChild);
}


function useC3(values,avg){
	var chart = c3.generate({
		bindto:'#graphs',	
		 size: {
	            height: 650,
	            width: 650
		},
	    data: {	    	    
	        columns: [
	            ['Deplorable', values[0]],
	            ['Bad', values[1]],
	            ['Average', values[2]],
	            ['Good', values[3]],
	            ['Awesome', values[4]]
	        ],
	       
	        colors: {
	            Deplorable: '#ED1C24',
	            Bad: '#FF7F27',
	            Average: '#FFF200',
	            Good: '#00A2E8',
	 	        Awesome: '#22B14C',
	        },
	        type : 'donut',
	        onclick: function (d, i) { console.log("onclick", d, i); },
	        onmouseover: function (d, i) { console.log("onmouseover", d, i); },
	        onmouseout: function (d, i) { console.log("onmouseout", d, i); }
	    },
	    donut: {
	    	width:180,
	        title: avg
	    }
	});
}

function initCommentTable(data){
	var table =  $('#tabComments').DataTable({
		"data" : data,
		"columns" : [ {
			"data" : "date"
		}, {
			"data" : "comment"
		} ]
	});
}
/*
 * listener for deleting a project in the DB
 */
$('#moodsBtn').click( function () {
	document.getElementById("stats").style.display = "none";
	document.getElementById("graphs").style.display = "none";
	document.getElementById("moods").style.display = "inline-block";
	document.getElementById("count").style.display = "inline-block";
});
/*
 * show stats page
 */
$('#statsBtn').click( function () {
	document.getElementById("moods").style.display = "none";
	document.getElementById("count").style.display = "none";
	document.getElementById("stats").style.display = "inline-block";
	document.getElementById("graphs").style.display = "inline-block";
});
/*
 * go to the project searching page
 */
$('#searchBtn').click( function () {
	window.location.replace("/bamby/searchProject.html");
});


