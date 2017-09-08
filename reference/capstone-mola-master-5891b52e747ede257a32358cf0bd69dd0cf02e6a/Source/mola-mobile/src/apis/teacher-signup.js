import mola, {filesUploadURL} from './mola-api';
import {AsyncStorage} from 'react-native'
import qs from 'qs';
import RNFetchBlob from 'react-native-fetch-blob';
import {uploads} from './uploads';

class TeacherSignUpAPI {
  constructor() {
    this.response = {};
  }

  composedData = async(user = {}) => {
    const avatar = user.avatar || '';
    const video_intro = user.videoIntro || [];
    const username = user.username || '';

    const files = [];
    if (avatar !== '') {
      files.push({
        name: `${username}_avatar`,
        filename: avatar.length && avatar.substr(avatar.lastIndexOf('/') + 1),
        type: 'image/jpeg',
        data: RNFetchBlob.wrap(avatar)
      });
    }

    video_intro.map(video => {
      files.push({
        name: `${username}_${video.language}_video`,
        filename: video
          .uri
          .substr(video.uri.lastIndexOf('/') + 1),
        type: 'video/mp4',
        data: RNFetchBlob.wrap(video.uri)
      });
    });
    return files;
  }
  uploads = files => {
    return uploads(files);
  }

  sendTeacherData = async(user) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');

    const {data} = await mola.post('/api/teachers/signup', qs.stringify(user), {
      headers: {
        'Authorization': token
      }
    });
    return data;
  }

  getInfoTeacher = async (username) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');

    const {data} = await mola.get('/api/teachers/' + username, {
      headers: {
        'Authorization': token
      }
    });
    return data;
  }
}
export default TeacherSignUpAPI;