import {GET_USER_PROFILE} from './actions';

const INITIAL_STATE = {
  profile: {
    data: null,
    isFetched: false,
    error: {
      on: false,
      message: null
    }
  }
}

export default(state = INITIAL_STATE, action) => {
  switch (action.type) {
    case `${GET_USER_PROFILE}_PENDING`:
      return INITIAL_STATE;
    case `${GET_USER_PROFILE}_FULFILLED`:
      return {
        profile: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${GET_USER_PROFILE}_REJECT`:
      return {
        profile: {
          data: null,
          isFetched: true,
          error: {
            on: true,
            message: 'ERROR: search courses by languages is rejected'
          }
        }
      }
    default:
      return state;
  }

}