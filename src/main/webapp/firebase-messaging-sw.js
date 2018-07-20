/*
Give the service worker access to Firebase Messaging.
*/
importScripts('https://www.gstatic.com/firebasejs/3.5.0/firebase-app.js')
importScripts('https://www.gstatic.com/firebasejs/3.5.0/firebase-messaging.js')

/*
Initialize the Firebase app in the service worker by passing in the messagingSenderId.
*/
firebase.initializeApp({
    'messagingSenderId': "projecteam26"
});

/* called when the app is in the foreground */
self.addEventListener('push', function(event) {
    console.log('Received a push message', event);

    var payload = JSON.parse(event.data.text());
    var title = payload.data.title;
    var body = payload.data.body;

    var promise = self.registration.showNotification(title, {body: body,});
    event.waitUntil(promise);
});

messaging = firebase.messaging();

/* called when the app is in the background */
messaging.setBackgroundMessageHandler(function(payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);

    return self.registration.showNotification(payload.data.title,
        payload.data.body);
});
