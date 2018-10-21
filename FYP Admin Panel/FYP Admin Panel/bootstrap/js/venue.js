var db = firebase.firestore();
var venueBookedText;

var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();

if(dd<10) {
    dd = '0'+dd
} 

if(mm<10) {
    mm = '0'+mm
} 

today = dd + '-' + mm + '-' + yyyy;
console.log(today);

function storeVenueData()
{
  var idvenue;
  	var venueNameText = document.getElementById("venueName").value;
  	var venueCapacityText = document.getElementById("venueCapacity").value;

    console.log(venueNameText);
    console.log(venueCapacityText);
    
            db.collection("Venues").add({
                venueName: venueNameText,
                venueCapacity: venueCapacityText,
                dateCreated:today
            })
            .then(function(docRef) {
              console.log("Document written with ID: ", docRef.id);
              idvenue = docRef.id;
              db.collection("Booked").doc().set({
                venueName: venueNameText,
                dateBooked:today
              })
            .then(function(docRef) {
              console.log("Document written");
            })
            .catch(function(error) {
                console.error("Error adding document: ", error);
});
            })
            .catch(function(error) {
                console.error("Error adding document: ", error);
});
            

}


var table = document.getElementById("tableVenue"),rIndex;

function listVenueData()
{
  db.collection("Venues").get().then(function(querySnapshot) {
      if (querySnapshot.empty) {
          console.log("empty");
      } 
      else 
      {
          var rowCount = 1;
          querySnapshot.forEach(function(doc)
          {
              var data = doc.data();
              var row = table.insertRow(rowCount);
              row.setAttribute("id","rowID");
              var cell1 = row.insertCell(0);
              var cell2 = row.insertCell(1);
              var cell3 = row.insertCell(2);
              var cell4 = row.insertCell(3);
              var cell5 = row.insertCell(4);

              cell1.innerHTML = doc.id;
              cell2.innerHTML = data.venueName;
              cell3.innerHTML = data.venueCapacity;
              cell4.innerHTML = data.dateCreated;
              cell5.innerHTML = "<button onclick='deleteVenueData()'>Delete</button>"
              rowCount++;

            for(var i = 1; i < table.rows.length;i++)
            {
              table.rows[i].onclick = function()
              {
                rIndex = this.rowIndex;
                console.log(rIndex);
                document.getElementById("editVenueName").value = this.cells[1].innerHTML;
                document.getElementById("editVenueCapacity").value = this.cells[2].innerHTML;
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
  table.rows[rIndex].cells[1].innerHTML = document.getElementById("editVenueName").value;
  table.rows[rIndex].cells[2].innerHTML = document.getElementById("editVenueCapacity").value;

  var editVenueName = document.getElementById("editVenueName").value;
  var editVenueCapacity = document.getElementById("editVenueCapacity").value;

var washingtonRef = db.collection("Venues").doc(x);

// Set the "capital" field of the city 'DC'
return washingtonRef.update({
                venueName: editVenueName,
                venueCapacity: editVenueCapacity,
                dateCreated: table.rows[rIndex].cells[3].innerHTML
})
.then(function() {
    console.log("Document successfully updated!");
    var bookRef = db.collection("Booked").doc(x);

// Set the "capital" field of the city 'DC'
return bookRef.update({
                venueName: editVenueName,
                dateBooked: table.rows[rIndex].cells[3].innerHTML
})
.then(function() {
    console.log("Document successfully updated!");
    
})
.catch(function(error) {
    // The document probably doesn't exist.
    console.error("Error updating document: ", error);
});
})
.catch(function(error) {
    // The document probably doesn't exist.
    console.error("Error updating document: ", error);
});
}

  function deleteVenueData()
  {
      var x = table.rows[rIndex].cells[0].innerHTML;
    db.collection("Events").doc(x).delete().then(function() {
      console.log("Document successfully deleted!");
    }).catch(function(error) {
      console.error("Error removing document: ", error);
    });
  }