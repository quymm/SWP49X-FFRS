import { combineReducers } from 'redux';
import fieldReducer  from './field-reducer';
import matchReducer from './match-reducer';
import TimeReducer from './time-reducer';
const reducer = combineReducers({
    field: fieldReducer,
    match: matchReducer,
    timeEnable: TimeReducer
});

export default reducer;