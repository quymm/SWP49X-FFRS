const defaultState = {listField: [ ]};

const fieldReducer = (state = defaultState, action) => {
    debugger
    switch (action.type) {
                 case 'GET_ALL_FIELD':
            return {...state, listField: action.payloads};   
        debugger
        //thieu chu defualt vcl, vya gio chay dc roi a, uhm
        default:
        return defaultState;
        
    }
}
//dang lam cai nay ha uh
export default fieldReducer;
