import {TEACHER_SIGNUP_SEND_DATA} from './actions';

const TEACHER_SEND_DATA_INITIAL_STATE = {
  teacher: {
    data: null,
    isFetched: false,
    status: {
      ok: false,
      error: false,
      message: ''
    }
  }
}

export default(state = TEACHER_SEND_DATA_INITIAL_STATE, action) => {
  const response = action.payload;

  switch (action.type) {
    case `${TEACHER_SIGNUP_SEND_DATA}_PENDING`:
      return TEACHER_SEND_DATA_INITIAL_STATE;
    case `${TEACHER_SIGNUP_SEND_DATA}_FULFILLED`:
      return {
        teacher: {
          data: action.payload,
          isFetched: true,
          status: {
            ok: response.status === 'ok',
            error: response.status !== 'ok',
            message: response.message
          }
        }
      }
    case `${TEACHER_SIGNUP_SEND_DATA}_REJECT`:
      return {
        teacher: {
          data: null,
          isFetched: true,
          status: {
            ok: response.status === 'ok',
            error: response.status !== 'ok',
            message: response.message
          }
        }
      }
    default:
      return state;
  }

}