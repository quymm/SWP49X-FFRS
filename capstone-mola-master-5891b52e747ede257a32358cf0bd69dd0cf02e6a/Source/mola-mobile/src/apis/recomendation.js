import mola from './mola-api';
import qs from 'qs';
import { AsyncStorage } from 'react-native';

class RecomendationAPI {
  recomendCourse = async (username) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.get(`/api/recommend`, {
            headers: { 'Authorization': token }
        }).then(result => {
            return result;
        });
    return data;
  }

  similarCourse = async (courseId) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.get(`/api/recommend/${courseId}`, {
            headers: { 'Authorization': token }
        }).then(result => {
            return result;
        });
    return data;
  }
}

export default RecomendationAPI;