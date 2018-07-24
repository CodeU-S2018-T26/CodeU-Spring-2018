messaging = firebase.messaging();
var xhttp = new XMLHttpRequest();

messaging.usePublicVapidKey("BLIwYdJwu8oCxTYQttElp90iDYg5ooy6tzLvx1g2YznQY2JTIQubixoO_stuyADW-wot5Jk8EKYV995C_OPLn3M");
Notification.requestPermission().then(function(status) {
    console.log('Notification permission status<:', status);
    if(status == 'granted'){
// Get Instance ID token.
  messaging.getToken().then(function(currentToken) {
    if (currentToken) {
        console.log("Token retrieved");
        url = "/login?token=" + currentToken+"&t="+Math.random();
        xhttp.open("POST", url, true);
        xhttp.send();

    } else {
        // Show permission request.
        console.log('No Instance ID token available. Request permission to generate one.');
    }
}).catch(function(err) {
    console.log('An error occurred while retrieving token. ', err);
})}});


// Callback fired if Instance ID token is updated.
messaging.onTokenRefresh(function() {
    messaging.getToken().then(function(refreshedToken) {
        console.log('Token refreshed.');
    }).catch(function(err) {
        console.log('Unable to retrieve refreshed token ', err);
        showToken('Unable to retrieve refreshed token ', err);
    });
});


