const defaultState = { listField: [] };

const fieldReducer = (state = defaultState, action) => {
  
  switch (action.type) {
    case 'GET_ALL_FIELD':
      return { ...state, listField: action.payloads };
    default:
      return state;
  }
};
export default fieldReducer;
