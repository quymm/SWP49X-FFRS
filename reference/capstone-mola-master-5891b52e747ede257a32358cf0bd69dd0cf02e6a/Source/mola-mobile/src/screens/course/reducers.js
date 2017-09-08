import { GET_COURSE_BY_TEACHER } from './actions';

const INITIAL_STATE = {
  teacherCourse: {
    data: [],
    isFetched: false,
    status: {
      ok: false,
      error: false,
      message: ''
    }
  }
}

export default (state = INITIAL_STATE, action) => {
  switch (action.type) {
    case `${GET_COURSE_BY_TEACHER}_PENDING`:
      return INITIAL_STATE;
    case `${GET_COURSE_BY_TEACHER}_FULFILLED`:
      return {
        teacherCourse: {
          data: action.payload,
          isFetched: true,
          status: {
            ok: response.status === 'ok',
            error: response.status !== 'ok',
            message: ''
          }
        }
      }
    case `${GET_COURSE_BY_TEACHER}_REJECT`:
      return {
        teacherCourse: {
          data: [],
          isFetched: true,
          status: {
            ok: false,
            error: true,
            message: ''
          }
        }
      }
    default:
      return state;
  }

}