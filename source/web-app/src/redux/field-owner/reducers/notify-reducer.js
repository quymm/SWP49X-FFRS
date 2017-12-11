const notifyReducer = (state = [], action) => {
    // console.log('state: ',state);
    switch (action.type) {
      case 'GET_ALL_NOTIFY':
        return { ...state, notify: action.payloads };
      default:
        return state;
    }
  };
  export default notifyReducer;
  