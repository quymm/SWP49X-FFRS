import mola from './mola-api';
import qs from 'qs';

import { AsyncStorage } from 'react-native'

class RatingAPI {
    constructor() {

    }

     async getRatingTeacher(teacherId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/rating/teacher/${teacherId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async addRatingTeacher(ratingTeacher) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`api/rating/teacher`,qs.stringify(ratingTeacher), {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
}

export default RatingAPI;