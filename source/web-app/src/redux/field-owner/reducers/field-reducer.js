const defaultState = { listField: [] };

const fieldReducer = (state = defaultState, action) => {
  debugger;
  switch (action.type) {
    case 'GET_ALL_FIELD':
      return { ...state, listField: action.payloads };
      debugger;
    default:
      return state;
  }
};
export default fieldReducer;
