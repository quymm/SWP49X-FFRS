import firebase from 'firebase';

var config = {
  apiKey: 'AIzaSyCLPyWR_SH3x16V6N30YaLdrWpl4J6krb0',
  authDomain: 'ffrs-cfa03.firebaseapp.com',
  databaseURL: 'https://ffrs-cfa03.firebaseio.com',
  projectId: 'ffrs-cfa03',
  storageBucket: 'ffrs-cfa03.appspot.com',
  messagingSenderId: '574175920152',
};
var fire = firebase.initializeApp(config);
export default fire;
