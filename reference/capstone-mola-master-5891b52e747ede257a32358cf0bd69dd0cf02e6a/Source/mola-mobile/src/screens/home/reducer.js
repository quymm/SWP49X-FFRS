import {
  SET_NEXT_UPCOMING_SESSION,
  SET_NO_UPCOMING_SESSION
} from './actions.js';

const INITIAL_STATE = {
  upcoming: {
    session: null,
    hasNextSession: false,
  }
}

export default(state = INITIAL_STATE, action) => {
  switch(action.type){
    case `${SET_NEXT_UPCOMING_SESSION}`:
      return Object.assign({}, {
        ...action.payload
      });
    case `${SET_NO_UPCOMING_SESSION}`:
      return Object.assign({}, {
        session: null,
        hasNextSession: false,
      });
    
    default:
      return state;
  }
}
