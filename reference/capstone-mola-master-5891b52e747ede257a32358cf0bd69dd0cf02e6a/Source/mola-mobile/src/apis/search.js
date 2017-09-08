import mola from './mola-api';
import qs from 'qs';
import { AsyncStorage } from 'react-native';
class SearchAPI {
  searchCourse = async (courseName) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.get(`/api/search?q=${courseName}`, {
            headers: { 'Authorization': token }
        }).then(result => {
            return result;
        });
    return data;
  }
}

export default SearchAPI;