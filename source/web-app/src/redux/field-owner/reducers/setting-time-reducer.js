const defaultState = {fieldTypeTimeEnable: []}
const settingTimeReducer = (state =[], action) {
    switch (action.type) {
        case 'GET_5VS5_TIME_ENABLE':
            
            { fieldTypeTimeEnable: state.timeEnable.filter(listTime => {
                
            }) };
    
        default:
            return state;
    }
}