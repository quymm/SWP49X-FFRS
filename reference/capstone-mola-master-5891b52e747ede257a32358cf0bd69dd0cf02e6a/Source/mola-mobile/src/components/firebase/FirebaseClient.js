import * as firebase from 'firebase';

const firebaseConfig = {
  apiKey: 'AIzaSyCo08owRZoRbH3hn2SFuGvVMmpJZAms9hY',
  authDomain: "mola-5428e.firebaseapp.com",
  databaseURL: "https://mola-5428e.firebaseio.com",
  storageBucket: "mola-5428e.appspot.com",
  messagingSenderId: "108935030008"
}

const firebaseApp = firebase.initializeApp(firebaseConfig)

export default firebaseApp