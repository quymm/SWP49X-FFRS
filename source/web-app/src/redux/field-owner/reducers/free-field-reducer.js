const defaultState = { freeField: []};

const freeFieldReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'GET_ALL_FREE_FIELD':
      return { ...state, freeField: action.payloads };
    default:
      return state;
  }
};
export default freeFieldReducer;