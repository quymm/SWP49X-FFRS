const defaultState = {listField: [ ]};

const fieldReducer = (state = defaultState, action) => {
    switch (action.type) {
        case 'GET_ALL_FIELD':
            return {...state, listField: action.payloads};   
        debugger
        return defaultState;
        
    }
}
//dang lam cai nay ha uh
export default fieldReducer;
