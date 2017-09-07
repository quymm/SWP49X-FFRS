import mola from './mola-api';
import qs from 'qs';

import { AsyncStorage } from 'react-native'

class SessionAPI{
    constructor(){

    }
     async getSessionByUser() {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get('/api/session', {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async getFreeTimeSlot(teacherID, selectDate) {
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.get(`/api/timeslot/${teacherID}?select_date=${selectDate}`, {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async requestSession(timeSlotId, lessonId, startTime) {
        
        const session = {timeSlotId, lessonId, startTime};
        console.log('session', session)
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/session`,qs.stringify(session), {
            headers: {
                'Authorization': token
            }
        }).then(result => {

            return result;
        });

        return data;
    }
    async checkRequestTimeBeforeSetSession(timeSlotId, lessonId, startTime) {
        const session = {timeSlotId, lessonId, startTime};
        const token = await AsyncStorage.getItem('USER_TOKEN');
        const { data } = await mola.post(`/api/session/validate`, qs.stringify(session), {
            headers: {
                'Authorization': token
            }
        }).then(result => {
            return result;
        });

        return data;
    }
    async setFinishedSession(sessionId) {
      
      const token = await AsyncStorage.getItem('USER_TOKEN');
      const { data } = await mola.post(`/api/session/finished`, qs.stringify({sessionId}), {
        headers: {
          'Authorization': token
        }
      }).then(result => {

          return result;
      });

      return data;
    }

}

export default SessionAPI;