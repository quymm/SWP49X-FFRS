import CourseAPI from '../../apis/course';
const courseAPI = new CourseAPI();

export const GET_COURSE_BY_TEACHER = 'GET_COURSE_BY_TEACHER';
export const getCourseByTeacher = () => ({
  type: GET_COURSE_BY_TEACHER,
  payload: courseAPI.getCourseByTeacher()
});