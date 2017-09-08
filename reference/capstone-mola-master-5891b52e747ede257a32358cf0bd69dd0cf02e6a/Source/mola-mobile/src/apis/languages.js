import mola from './mola-api';

import {AsyncStorage} from 'react-native';

class LanguagesAPI {
  constructor() {}

  async setLanguageSpeakLearn(languages) {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola
      .post('api/register/language', languages, {
      headers: {
        'Authorization': token
      }
    })
      .then(result => {
        return result;
      });
    return data;
  }

  async getLanguageUser() {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola
      .get('api/languages', {
      headers: {
        'Authorization': token
      }
    })
      .then(result => {
        return result;
      });
    return data;
  }

  async getSingleLanguageSpeak() {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola
      .get('api/native-language', {
      headers: {
        'Authorization': token
      }
    })
      .then(result => {
        return result;
      });
    return data;
  }

  async getLanguageSpeakTeach() {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola
      .get('api/languages/teach', {
      headers: {
        'Authorization': token
      }
    })
      .then(result => {
        return result;
      });
    return data;
  } 
}

export default LanguagesAPI;