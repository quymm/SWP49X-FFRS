const defaultState = { freeTime5vs5: [], freeTime7vs7: [] };

const freeTimeReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'GET_ALL_FREE_TIME_5VS5':
      return { ...state, freeTime5vs5: action.payloads };
    case 'GET_ALL_FREE_TIME_7VS7':
      return { ...state, freeTime7vs7: action.payloads };
    default:
      return state;
  }
};
export default freeTimeReducer;
