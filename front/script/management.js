/**
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


/**
 * global method for adding table
 */
(function() {
	var uuid = window.location.search.substring(1).split("&")[0].substring(5);
	var url = "http://localhost:8080/api-admin/projects/" + uuid + "/moods";
	$.get(url).done(function(data) {
		if (data.length != 0) {			
			createTable();
			addHeads();		
			eachDays(data);
			
		} else {
			var noMoods = document.createElement("p");
			noMoods.appendChild(document.createTextNode("No moods found"));
			document.getElementById("moods").appendChild(noMoods);
		}

	});
})();

/**
 * Process getting data for each days
 * @param data
 * @returns
 */
function eachDays(data){	
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
		for(var v = 0;v<5;v++){
			values[v] += listList[v].length;
		}
		
		browseLists(listList,i);
	}
	for(var v = 0;v<5;v++){
		console.log(values[v]);
	}
}
/**
 * order moods (DESC)
 * @param listList
 * @param index
 * @returns
 */
function browseLists(listList,index){
	for(var i = 4;i>=0;i--){		
		addMoodsToData(listList[i],index);
	}
}
/**
 * add a mood to the <td>
 * @param listToAdd
 * @param index
 * @returns
 */
function addMoodsToData(listToAdd,index){
	for(var i = 0 ; i<listToAdd.length ; i++){
		document.getElementById("tabData"+index).appendChild(listToAdd[i]);
	}
}
/**
 * Add a new row to the tab
 * @param data
 * @param index
 * @returns
 */
function createRow(data,index){
	var trRow = document.createElement("tr");
	trRow.setAttribute("id","tabRow"+index);
	var tdDate = document.createElement("td");
	tdDate.appendChild(document.createTextNode(data[index][0].date));
	trRow.appendChild(tdDate)
	document.getElementById("tabMood").appendChild(trRow);
}

/**
 * add heads to the tab
 * @returns
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

/**
 * create the tab
 * @returns
 */
function createTable(){
	var tab = document.createElement("table");
	tab.setAttribute("id","tabMood");
	document.getElementById("moods").appendChild(tab);
}

/**
 * return the picture depending on params
 * @param data
 * @param index
 * @returns
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