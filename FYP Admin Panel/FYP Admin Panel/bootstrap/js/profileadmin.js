var db = firebase.firestore(); 

function listData()
{
  firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.
  var cityRef = db.collection('SocietyData').doc(user.uid);
    var getDoc = cityRef.get()
        .then(doc => 
        {
          if (!doc.exists) {
            console.log('No such document!');
        } 
        else 
        {
          var data = doc.data();
      document.getElementById("adminID").value = data.societyId;
      document.getElementById("adminEmail").value = data.societyEmail;
      document.getElementById("societyName").value = data.societyName;
      document.getElementById("typeSociety").value = data.societyType;
      document.getElementById("adminPass").value = data.societyPassword;
        }
      })
      .catch(err => 
      {
        console.log('Error getting document', err);
      });
  } else {
    // No user is signed in.
  }
});
}

function signOut()
{
  firebase.auth().signOut().then(function() 
  {
  // Sign-out successful.
  }).catch(function(error) {
    // An error happened.
  });
}


function updateProfile()
{
    firebase.auth().onAuthStateChanged(function(user) {

    if(user)
    {

    var name = document.getElementById("UpdateAdminName").value;
    var pass = document.getElementById("UpdateAdminPass").value;

  var cityRef = db.collection('SocietyData').doc(user.uid);

                      return cityRef.update({
                          societyName: name,
                          societyPassword: pass

                      }).then(function() {
                        console.log("Document successfully updated!");  
                        user.updateProfile({
                          displayName: name
                        }).then(function() {
                          // Update successful.
                          var newPassword = pass;

                          user.updatePassword(newPassword).then(function() {
                            // Update successful.
                          }).catch(function(error) {
                            // An error happened.
                          });

                        }).catch(function(error) {
                          // An error happened.
                        });

                      })
                      .catch(function(error) {
                          // The document probably doesn't exist.
                          console.log("Error updating document: ", error);
                      });

    }
  });
}