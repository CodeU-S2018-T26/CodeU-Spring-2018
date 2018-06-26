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
})

/* called when the app is in the foreground */
self.addEventListener('push', function(event) {
    console.log('Received a push message', event);

    var title = 'Yay a message.';
    var body = 'We have receisved a push message.';

    var promise = self.registration.showNotification(title, {body: body,});
    event.waitUntil(promise);
});
messaging = firebase.messaging();

/* called when the app is in the background */
messaging.setBackgroundMessageHandler(function(payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    notificationTitle = 'Background Message Title';
    notificationOptions = {
        body: 'hi',
        icon: '/firebase-logo.png'
    };

    return self.registration.showNotification(notificationTitle,
        notificationOptions);
});
