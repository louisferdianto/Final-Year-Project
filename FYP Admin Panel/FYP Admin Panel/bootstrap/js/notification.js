/*
var FCM = require('fcm-node');
    
    var serverKey = require('/Users/louisferdianto/Desktop/Bootstrap-Admin-Theme-3-master/lib/node_modules/firebase-admin/privateKeyFCM.json'); //put the generated private key path here    
    
    var fcm = new FCM(serverKey);
 
    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
        to: 'TURaXbKxY8ffGzgX9HYIlIsw50l1', 
        
        notification: {
            title: 'Title of your push notification', 
            body: 'Body of your push notification' 
        },
        
        data: {  //you can send only notification or only data(or include both)
            my_key: 'my value',
            my_another_key: 'my another value'
        }
    }

    fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!")
        } else {
            console.log("Successfully sent with response: ", response)
        }
    })
*/

    var admin = require("firebase-admin");

    var serverKey = require('/Users/louisferdianto/Desktop/Bootstrap-Admin-Theme-3-master/lib/node_modules/firebase-admin/privateKeyFCM.json'); //put the generated private key path here    
var registrationToken = "TURaXbKxY8ffGzgX9HYIlIsw50l1";

admin.initializeApp({
  credential: admin.credential.cert(serverKey),
  databaseURL: "https://testt-d2c9f.firebaseio.com/"
});

var message = {
  data: {
    score: '850',
    time: '2:45'
  },
  token: registrationToken
};

// Send a message to the device corresponding to the provided
// registration token.
admin.messaging().send(message)
  .then((response) => {
    // Response is a message ID string.
    console.log('Successfully sent message:', response);
  })
  .catch((error) => {
    console.log('Error sending message:', error);
  });