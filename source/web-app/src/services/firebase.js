import firebase from 'firebase';

var config = {
    apiKey: "AIzaSyANgWbUJhcrssu7KONJDKlO1AgU2medFYA",
    authDomain: "capstoneproject-8740a.firebaseapp.com",
    databaseURL: "https://capstoneproject-8740a.firebaseio.com",
    projectId: "capstoneproject-8740a",
    storageBucket: "capstoneproject-8740a.appspot.com",
    messagingSenderId: "463224964825"
  };
var fire = firebase.initializeApp(config);
export default fire;