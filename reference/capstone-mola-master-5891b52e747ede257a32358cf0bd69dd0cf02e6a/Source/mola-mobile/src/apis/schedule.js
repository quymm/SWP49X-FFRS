import BaseAPI from './base-api';
import mola from './mola-api';
import qs from 'qs';
import { AsyncStorage } from 'react-native'

class ScheduleAPI extends BaseAPI {
  constructor(token){
    super(token);
  }

  getTeacherSchedule = async (from, to) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.get(`/api/teachers/schedules?from=${from}&to=${to}`, {
            headers: { 'Authorization': token }
        }).then(result => {
            return result;
        });
    return data;
  }
  getLearnerSchedule = async (from, to) => {
    const token = await AsyncStorage.getItem('USER_TOKEN');
      const {data} = await mola.get(`/api/learner/schedules?from=${from}&to=${to}`, {
              headers: { 'Authorization': token }
          }).then(result => {
              return result;
          });
    return data;
  }
  setFreeTimeSlot = async ({startTime, endTime}) =>{
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.post(`/api/teachers/timeslot`, qs.stringify({startTime, endTime}) ,{
      headers: { 'Authorization': token }
    }).then(result => {
      return result;
    }).catch(err =>{
      return err
    })
    return data;
  }
}
export default ScheduleAPI;