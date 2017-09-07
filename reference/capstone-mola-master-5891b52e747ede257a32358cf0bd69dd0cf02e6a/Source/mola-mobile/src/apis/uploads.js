import mola from './mola-api';
import qs from 'qs';
import RNFetchBlob from 'react-native-fetch-blob';
import {AsyncStorage} from 'react-native';


let token = null;
AsyncStorage.getItem('USER_TOKEN', (err, res) => {
  token = res;
});


export const uploads = (files = []) => {
  return RNFetchBlob.fetch('POST', `${mola.defaults.baseURL}/api/uploads`, {
      'Content-Type': 'multipart/form-data',
      'Authorization': token
    }, files
  );

}
