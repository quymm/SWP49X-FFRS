// const defaultState = {listTimeEnableInWeek: []};

const TimeReducer = (state = [], action) =>{
    switch (action.type) {
        case 'GET_ALL_TIME_ENABLE_IN_WEEK':          
            return{...state, timeEnable: action.payloads };
        default:
            return state;
    }
}
export default TimeReducer;
