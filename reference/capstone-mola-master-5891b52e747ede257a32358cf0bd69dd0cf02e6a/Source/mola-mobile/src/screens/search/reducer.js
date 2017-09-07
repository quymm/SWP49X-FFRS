import {
  SEARCH_COURSE_NAME,
  SEARCH_FILTER_APPLIED,
  SEARCH_FILTER_CANCEL
} from './actions';

const INITIAL_STATE = {
  courses: {
    data: [],
    isFetched: false,
    error: {
      on: false,
      message: null
    }
  }
}

export default(state = INITIAL_STATE, action) => {
  switch (action.type) {
    case `${SEARCH_COURSE_NAME}_PENDING`:
      return INITIAL_STATE;
    case `${SEARCH_COURSE_NAME}_FULFILLED`:
      return {
        courses: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${SEARCH_COURSE_NAME}_REJECT`:
      return {
        courses: {
          data: [],
          isFetched: true,
          error: {
            on: true,
            message: 'ERROR: search courses by name is rejected'
          }
        }
      }
    case `${SEARCH_FILTER_APPLIED}`:
    const data = action.payload;
      return {
        courses: {
          data: action.payload,
          isFetched: true,
          error: {
            on: false,
            message: null
          }
        }
      }
    case `${SEARCH_FILTER_CANCEL}`:
      return {
        courses: {
          data: [],
          isFetched: false,
          error: {
            on: false,
            message: null
          }
        }
      }
    default:
      return state;
  }

}