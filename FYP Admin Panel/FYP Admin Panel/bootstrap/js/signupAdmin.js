var db = firebase.firestore();

function signUpAdmin()
{
    if(!validate())
    {
      return;
    }

    var email = document.getElementById("SocietyEmail").value;
    var password = document.getElementById("SocietyPassword").value;
    var society = document.getElementById("SocietyName").value;
    var type = document.getElementById("SocietyType").value;
    var uid;
    firebase.auth().createUserWithEmailAndPassword(email, password)
      .then(function success(userData){
          uid = userData.uid; // The UID of recently created user on firebase
          console.log(uid);
          /*
          var displayName = userData.displayName;
          var email = userData.email;
          var emailVerified = userData.emailVerified;
          var photoURL = userData.photoURL;
          var isAnonymous = userData.isAnonymous;
          var providerData = userData.providerData;
          */

          db.collection("SocietyData").doc(uid).set({
            societyId: uid,
            societyEmail: email,
            societyPassword: password,
            societyName: society,
            societyType: type
          })
          .then(function(){
            window.location.replace("login.html");
          })
          .catch(function(error){
            console.error("error writting document : ", error);
          })
      }).catch(function failure(error) {
          var errorCode = error.code;
          var errorMessage = error.message;
          console.log(errorCode + " " + errorMessage);
      });
}

function validate()
{
    var allLetters = /^[a-zA-Z]+$/;
    var letter = /[a-zA-Z]/;
    var number = /[0-9]/;



    var email = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

// /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

//"[a-z.]*[@]\apu.edu.my"



    var Adminemail = document.getElementById("SocietyEmail").value;
    var Adminpassword = document.getElementById("SocietyPassword").value;
    var Adminsociety = document.getElementById("SocietyName").value;
    var Societytype = document.getElementById("SocietyType").value;

    var invalid = [];

    if (!email.test(Adminemail))
    {
        invalid.push("Fill Email");
    }
    else
    {
      invalid =[];
          if(Adminemail.indexOf("@apu.edu.my", Adminemail.length - "@apu.edu.my".length) !== -1)
      {
            //VALID
          console.log("VALID");
          invalid =[];
      }
      else
      {
        invalid.push("Email format not match");
        alert("Email format not match");
        return false;
      }
    }



    if (Adminpassword.length < 5) 
    {
        invalid.push("Password must be more than 5");
    }   
    else
    {
        invalid = [];
        if(!letter.test(Adminpassword))
        {
          invalid.push("Password must be contain letters");
        }
        else
        {
          invalid = [];
          if(!number.test(Adminpassword))
          {
            invalid.push("Password must contain numbers");
          }
          else
          {
            invalid = [];
          }
        }
    }

    if (!allLetters.test(Adminsociety)) 
    {
        invalid.push("Fill Society Name");
    }
    else
    {
        invalid = [];
    }
    if (!allLetters.test(Societytype)) 
    {
        invalid.push("Fill Society Type");
    }
    else
    {
        invalid = [];
    }

    if (invalid.length != 0) {
        alert("Please provide response: \n" + invalid.join("\n"));
        return false;
    }
    return true;
}