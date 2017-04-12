/*
 * set heads with url params
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	if (uuid.length < 10) {
		alert("Bad project ID , please check url");
	} else {
		var url = "http://localhost:8080/api-admin/projects/" + uuid;
		$.get(url).done(
				function(data) {
					document.getElementById("title").appendChild(document.createTextNode("Manage moods for " +data.title));
					
				});
	}
})();


/*
 * global method for adding table
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	var url = "http://localhost:8080/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {
		if (data.length != 0) {			
			createTable();
			addHeads();		
			eachDaysMoods(data);
			
		} else {
			var noMoods = document.createElement("p");
			noMoods.appendChild(document.createTextNode("No moods found"));
			document.getElementById("moods").appendChild(noMoods);
		}

	});
})();


/*
 * getting data for each days
 */
function eachDaysMoods(data){	
	var values = [0,0,0,0,0]
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
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	var url = "http://localhost:8080/api-admin/projects/" + uuid + "/moods";
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


