const defaultState = { listMatch: [] };
const matchReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'GET_MATCH_BY_DAY':
      return { ...state, listMatch: action.payloads };
    default:
      return state;
  }
};
export default matchReducer;
