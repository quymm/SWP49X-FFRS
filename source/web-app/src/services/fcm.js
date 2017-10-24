// importScripts('https://www.gstatic.com/firebasejs/3.5.0/firebase-app.js')
// importScripts('https://www.gstatic.com/firebasejs/3.5.0/firebase-messaging.js')
import * as firebase from 'firebase';
/*
Initialize the Firebase app in the service worker by passing in the messagingSenderId.
*/
firebase.initializeApp({
  'messagingSenderId': '463224964825'
})
//https://www.npmjs.com/package/firebase cai di

export function showNotify() {
  // Initialize Firebase copy cai nay =)) install nha tu tu, hoi trua t lam, co 1 cai thu vien 
  var config = {
    apiKey: 'AIzaSyANgWbUJhcrssu7KONJDKlO1AgU2medFYA',
    authDomain: 'capstoneproject-8740a.firebaseapp.com',
    databaseURL: 'https://capstoneproject-8740a.firebaseio.com',
    projectId: 'capstoneproject-8740a',
    storageBucket: 'capstoneproject-8740a.appspot.com',
    messagingSenderId: '463224964825',
  };
  if (!firebase.apps.length) {
    firebase.initializeApp(config);
}
  //firebase.initializeApp();

  var message = firebase.messaging();

  message
    .requestPermission()
    .then(function() {
      console.log('permission granted!');
      navigator.serviceWorker
        .register('../../firebase-messaging-sw.js')
        .then(function(registration) {
          //set firebase use created serviceworker above
          firebase.messaging().useServiceWorker(registration);

          var notification = new Notification('What next?', {
            icon:
              'https://d2gg9evh47fn9z.cloudfront.net/thumb_COLOURBOX6155992.jpg',
            body: 'body',
          });

          notification.onclick = function(event) {
            event.preventDefault();
            console.log('clicked');
          };
        });
      ///////////////////////////////////////////////// gio import function nay, click nut nao do goi xem

      return message.getToken();
    })
    .then(function(token) {
      if (token) {
        console.log('Your token: ' + token);
        //sendTokenToServer(token);
      } else {
        console.log('cannot get token. do not have permission');
      }
    })
    .catch(function(err) {
      console.log(err);
    });
}
