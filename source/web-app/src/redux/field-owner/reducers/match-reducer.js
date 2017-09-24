

const matchReducer = (state = [], action) => {
    switch (action.type) {
        case 'GET_MATCH_BY_DAY':
            return [ action.payloads, ...state ];         
        return state;
    }
}
export default matchReducer;
