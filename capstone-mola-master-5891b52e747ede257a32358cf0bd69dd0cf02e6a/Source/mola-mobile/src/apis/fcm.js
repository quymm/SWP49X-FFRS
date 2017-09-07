import mola from './mola-api';
import qs from 'qs';
import { AsyncStorage } from 'react-native'

class FirebaseAPI {
  constructor(){
  }

  async saveTokenToServer(fcmToken) {
    const token = await AsyncStorage.getItem('USER_TOKEN');

    const {data} = await mola.post('/api/firebase/token', qs.stringify({token: fcmToken}), {
      headers: {
        'Authorization': token
      }
    }).then(result => {
      return result;
    });
    return data;
  }

  async sendNotify(username, title, message) {
    const token = await AsyncStorage.getItem('USER_TOKEN');

    const {data} = await mola.post('/api/firebase/send-notify', qs.stringify({username, title, message}), {
      headers: {
        'Authorization': token
      }
    }).then(result => {
      return result;
    });
    return data;
  }
}
export default FirebaseAPI;

