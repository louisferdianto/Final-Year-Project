	var downloadUrl;
	var db = firebase.firestore();
	var file;

var x = document.getElementById("eventVenue");
var venueid;
var venueNameText;
var getdate;
var xy,xx;
var getvenue;

var option = document.createElement("option");
var today = new Date();
var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();
              document.getElementsByClassName("testid")[0].style.visibility = "hidden";        

if(dd<10) {
    dd = '0'+dd
} 

if(mm<10) {
    mm = '0'+mm
} 

today = dd + '-' + mm + '-' + yyyy;
console.log(today);

function CheckSlot()
{    

  var flag = true;

  getdate = document.getElementById("datepickercheck").value;

    getvenue = document.getElementById("eventVenue").value;
db.collection("Booked")
    .get()
    .then(function(querySnapshot){
      querySnapshot.forEach(function(doc){

        if(getdate == doc.data().dateBooked && getvenue == doc.data().venueName)
        {
          venueid = doc.data().venueID;
          venueNameText = doc.data().venueName;
          
          var a = doc.data().venueName;
          var b = doc.data().dateBooked;

          var all = [a,b];
          console.log(all);

          var x = all.indexOf(getvenue);
          var y = all.indexOf(getdate);
          console.log(x,y);

          if(x == 0 || x != 0)
          {
            if(y == 1)
            {
              console.log("not available");   
              alert("Select other date"); 
              flag = false;  
              document.getElementsByClassName("testid")[0].style.visibility = "hidden";        
            }
          }
        }
      })
      if(flag)
      {
        alert("available"); 
              document.getElementsByClassName("testid")[0].style.visibility = "visible";        

      }
    })
}

db.collection("Venues").get().then(function (querySnapshot) 
{
        if (querySnapshot.empty) {
          console.log("empty");
      } 
      else 
      {                
        console.log(querySnapshot.size);


        querySnapshot.forEach(function (doc) 
          {
            var n = querySnapshot.size;
            var data = doc.data();
            x.options[x.options.length] = new Option(data.venueName);
          })
        };
}).catch (function (error) 
  {
      console.log(error);
  });

	var fileButton = document.getElementById("exampleInputFile1");
	fileButton.addEventListener('change', function(e)
	{
		file = e.target.files[0];
  		var storageRef = firebase.storage().ref('/eventPosters/' + file.name);
		var task = storageRef.put(file);

		task.on('state_changed', function progress(snapshot){

		}, function error(err){

		}, function complete(){
			downloadUrl = task.snapshot.downloadURL;
			console.log(downloadUrl);
		});	
	});

  function storeData()
  {
  	var eventText = document.getElementById("eventTitle").value;
  	var categoryText = document.getElementById("eventCategory").value;
  	var priceText = document.getElementById("eventPrice").value;
  	var participantText = document.getElementById("maxParticipant").value;
  	var dateText = document.getElementById("datepickercheck").value;
  	var startText = document.getElementById("timeStart").value;
  	var endText = document.getElementById("timeEnd").value;
  	var descText = document.getElementById("description").value;
    var locText = document.getElementById("eventVenue").value;

	  var posterImg = downloadUrl;

  	db.collection("Events").doc().set({
  		title: eventText,
  		categories: categoryText,
    	price: priceText,
    	participant: participantText,
    	date: dateText,
    	timeStart: startText,
    	timeEnd: endText,
    	desc: descText,
  		photo: posterImg,
  		location: locText
  	})
  	.then(function(){
  		console.log("Document written");

db.collection("Booked").doc().set({
                venueName: getvenue,
                dateBooked:getdate
              })
            .then(function(docRef) {
              console.log("Document written");
            })
            .catch(function(error) {
                console.error("Error adding document: ", error);
});
      alert("Event Registered");
  	})
  	.catch(function(error){
  		console.error("error writting document : ", error);
  	})
  }