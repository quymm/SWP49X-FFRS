import {USER_LOGIN_USERNAME_PASSWORD, REGISTER_USING_REGISTERAPI, USER_LOGIN_FACEBOOK, USER_RELOGIN_USERNAME_TOKEN, UPDATE_USER_PROFILE, LOGOUT_USER} from './actions';

const INITIAL_STATE = {
  user: {
    data: null,
    isFetched: false,
    status: {
      ok: false,
      error: false,
      message: ''
    }
  }
}

export default(state = INITIAL_STATE, action) => {
  switch (action.type) {
    case `${USER_LOGIN_USERNAME_PASSWORD}_PENDING`:
      return INITIAL_STATE;
    case `${USER_LOGIN_USERNAME_PASSWORD}_FULFILLED`:
      return {
        user: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${USER_LOGIN_USERNAME_PASSWORD}_REJECT`:
      return {
        user: {
          data: null,
          isFetched: true,
          error: {
            on: true,
            message: action.payload
          }
        }
      }
    case `${USER_LOGIN_FACEBOOK}_PENDING`:
      return INITIAL_STATE;
    case `${USER_LOGIN_FACEBOOK}_FULFILLED`:
      return {
        user: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${USER_LOGIN_FACEBOOK}_REJECT`:
      return {
        user: {
          data: null,
          isFetched: true,
          error: {
            on: true,
            message: action.payload
          }
        }
      }
    case `${REGISTER_USING_REGISTERAPI}_PENDING`:
      return INITIAL_STATE;
    case `${REGISTER_USING_REGISTERAPI}_FULFILLED`:
      return {
        user: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${REGISTER_USING_REGISTERAPI}_REJECT`:
      return {
        user: {
          data: null,
          isFetched: true,
          error: {
            on: true,
            message: action.payload
          }
        }
      }
    case `${USER_RELOGIN_USERNAME_TOKEN}_PENDING`:
      return INITIAL_STATE;
    case `${USER_RELOGIN_USERNAME_TOKEN}_FULFILLED`:
      return {
        user: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${USER_RELOGIN_USERNAME_TOKEN}_REJECT`:
      return {
        user: {
          data: null,
          isFetched: true,
          error: {
            on: true,
            message: action.payload
          }
        }
      }
    case `UPDATE_USER_PROFILE`:
      return {
        user: {
          data: 
          {data: {user: action.payload}},
          isFetched: true,
          error: {
            on: false,
            message: 'updated'
          }
        }
      }
      case `LOGOUT_USER`:
      return {
        user: {
          data: 
          {data: {user: {}}},
          isFetched: false,
          error: {
            on: false,
            message: 'logout success'
          }
        }
      }
    default:
      return state;
  }

}