import mola from './mola-api';
import qs from 'qs';

import { AsyncStorage } from 'react-native'

class TeacherAPI {
    constructor() {

    }

     async getTeacherExtra(teacherId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/teacher/extra/${teacherId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }

    async getRevenue() {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`/api/revenue`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }
}

export default TeacherAPI;