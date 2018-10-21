

    var emailLogin = document.getElementById("loginEmail");
    var passwordLogin = document.getElementById("loginPassword");

    var db = firebase.firestore();
    var user = firebase.auth().currentUser;
    //var firebase = require('firebase');
    //var firebaseui = require('firebaseui');

      var uiConfig = {
        signInFlow: 'popup',
        signInSuccessUrl: 'profile.html',
        signInOptions: [
          // Leave the lines as is for the providers you want to offer your users.
          firebase.auth.EmailAuthProvider.PROVIDER_ID
        ],
        // Terms of service url.
        tosUrl: '<your-tos-url>'
      };
    var ui = new firebaseui.auth.AuthUI(firebase.auth());
    ui.start('#firebaseui-auth-container', uiConfig);



function loginAdmin()
{
    if(!validate())
    {
      return;
    }

    var email = emailLogin.value;
    var password = passwordLogin.value;

    
    firebase.auth().signInWithEmailAndPassword(email, password)
      	.catch(function(error) 
      	{
			// Handle Errors here.
			var errorCode = error.code;
			var errorMessage = error.message;
			alert("Login Failed");
		});
}

function validate()
{
    var allLetters = /^[a-zA-Z]+$/;
    var letter = /[a-zA-Z]/;
    var number = /[0-9]/;
    var email = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

    var Adminemail = document.getElementById("loginEmail").value;
    var Adminpassword = document.getElementById("loginPassword").value;

    var invalid = [];

    if (!email.test(Adminemail))
    {
        invalid.push("Fill Email");
    }
    else
    {
        invalid = [];
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
    if (invalid.length != 0) {
        alert("Please provide response: \n" + invalid.join("\n"));
        return false;
    }
    return true;    
}