import TeachSignUpAPI from '../../../apis/teacher-signup';

const teacher = new TeachSignUpAPI();

export const TEACHER_SIGNUP_SEND_DATA = 'TEACHER_SIGNUP_SEND_DATA';
export const sendTeacherData = (data) => ({
  type: TEACHER_SIGNUP_SEND_DATA,
  payload: teacher.sendTeacherData(data)
});
