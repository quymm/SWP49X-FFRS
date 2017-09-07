import mola from './mola-api';
import qs from 'qs';

import { AsyncStorage } from 'react-native'

class RequestAPI {
    constructor() {

    }

    async getIncomingRequest() {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get('api/request/incoming', {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async getSendingRequest() {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get('api/request/sending', {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }


    //API -------------------------------------------------------------------------
    async cancelCourseRegisterRequest(registerCourseId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/course/cancel/${registerCourseId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async rejectCourseRegisterRequest(registerCourseId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/course/reject/${registerCourseId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async acceptCourseRegisterRequest(registerCourseId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/course/accept/${registerCourseId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }



    async cancelSessionRequest(sessionId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/session/cancel/${sessionId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async rejectSessionRequest(sessionId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/session/reject/${sessionId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async acceptSessionRequest(sessionId) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`api/session/accept/${sessionId}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
}

export default RequestAPI;