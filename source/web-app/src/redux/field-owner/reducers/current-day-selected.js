const defaultState = {currentDaySelected: false};
const currentDaySelected = (state = defaultState, action) => {
  switch (action.type) {
    case 'SET_CURRENT_DAY_SELECTED':
      return { ...state, currentDaySelected: action.payloads};
    default:
      return state;
  }
};
export default currentDaySelected;
