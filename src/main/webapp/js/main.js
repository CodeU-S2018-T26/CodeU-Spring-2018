Notification.requestPermission(function(status) {
    console.log('Notification permission status:', status);
});

navigator.serviceWorker
    .register(server + "/serviceworker.js") //Point to serviceWorker file
    .then(function(registration) {
        // Registration was successful
        console.log('Registration successful with scope: ',registration.scope);
    })
    .catch(function(error) {
        console.error("Failed to register service worker", error);
    });