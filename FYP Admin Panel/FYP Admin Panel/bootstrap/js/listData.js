	  	var db = firebase.firestore();

	var table = document.getElementById("table"),rIndex;

function listData()
{
	db.collection("Events").get().then(function (querySnapshot) {
	    if (querySnapshot.empty) {
	        console.log("empty");
	    } 
	    else 
	    {
	        var rowCount = 1;
	        querySnapshot.forEach(function (doc) 
	        {
	            var data = doc.data();
	            var row = table.insertRow(rowCount);
	            row.setAttribute("id","rowID");
	            var cell1 = row.insertCell(0);
	            var cell2 = row.insertCell(1);
	            var cell3 = row.insertCell(2);
	            var cell4 = row.insertCell(3);
	            var cell5 = row.insertCell(4);
	            var cell6 = row.insertCell(5);
	            var cell7 = row.insertCell(6);
	            var cell8 = row.insertCell(7);
	            var cell9 = row.insertCell(8);
	            var cell10 = row.insertCell(9);
	            var cell11 = row.insertCell(10);
	            var cell12 = row.insertCell(11);

	            cell1.innerHTML = doc.id;
	            cell2.innerHTML = data.title;
	            cell3.innerHTML = data.categories;
	            cell4.innerHTML = data.location;
	            cell5.innerHTML = data.price;
	            cell6.innerHTML = data.participant;
	            cell7.innerHTML = data.date;
	            cell8.innerHTML = data.timeStart;
	            cell9.innerHTML = data.timeEnd;
	            cell10.innerHTML = data.desc;
	            cell11.innerHTML = "<button onclick='editRow()'>Edit</button>"
	            cell12.innerHTML = "<button onclick='deleteData()'>Delete</button>"
	            rowCount++;

	            for(var i = 1; i < table.rows.length;i++)
				{
					table.rows[i].onclick = function()
					{
						rIndex = this.rowIndex;
						console.log(rIndex);
						document.getElementById("editTitle").value = this.cells[1].innerHTML;
						document.getElementById("editCategory").value = this.cells[2].innerHTML;
						document.getElementById("editPrice").value = this.cells[4].innerHTML;
						document.getElementById("editParticipant").value = this.cells[5].innerHTML;
						document.getElementById("editDate").value = this.cells[6].innerHTML;
						document.getElementById("editTimeStart").value = this.cells[7].innerHTML;
						document.getElementById("editTimeEnd").value = this.cells[8].innerHTML;
						document.getElementById("editDescription").value = this.cells[9].innerHTML;

					}
				}
	        });
	    }
	}).catch (function (error) 
	{
	    console.log(error);
	});
}

function editRow()
{
	var x = table.rows[rIndex].cells[0].innerHTML;
	console.log(x);
	table.rows[rIndex].cells[1].innerHTML = document.getElementById("editTitle").value;
	table.rows[rIndex].cells[2].innerHTML = document.getElementById("editCategory").value;
	table.rows[rIndex].cells[4].innerHTML = document.getElementById("editPrice").value;
	table.rows[rIndex].cells[5].innerHTML = document.getElementById("editParticipant").value;
	table.rows[rIndex].cells[6].innerHTML = document.getElementById("editDate").value;
	table.rows[rIndex].cells[7].innerHTML = document.getElementById("editTimeStart").value;
	table.rows[rIndex].cells[8].innerHTML = document.getElementById("editTimeEnd").value;
	table.rows[rIndex].cells[9].innerHTML = document.getElementById("editDescription").value;

	var edittitle = document.getElementById("editTitle").value;
	var editcategory = document.getElementById("editCategory").value;
	var editprice = document.getElementById("editPrice").value;
	var editdate = document.getElementById("editDate").value;
	var edittimestart = document.getElementById("editTimeStart").value;
	var edittimeend = document.getElementById("editTimeEnd").value;
	var editparticipant = document.getElementById("editParticipant").value;
	var editdescription = document.getElementById("editDescription").value;

var washingtonRef = db.collection("Events").doc(x);

// Set the "capital" field of the city 'DC'
return washingtonRef.update({
  		title: edittitle,
  		categories: editcategory,
    	price: editprice,
    	participant: editparticipant,
    	date: editdate,
    	timeStart: edittimestart,
    	timeEnd: edittimeend,
    	desc: editdescription
})
.then(function() {
    console.log("Document successfully updated!");
})
.catch(function(error) {
    // The document probably doesn't exist.
    console.error("Error updating document: ", error);
});
}


function deleteData()
{
		var x = table.rows[rIndex].cells[0].innerHTML;
	db.collection("Events").doc(x).delete().then(function() {
    console.log("Document successfully deleted!");
}).catch(function(error) {
    console.error("Error removing document: ", error);
});

}