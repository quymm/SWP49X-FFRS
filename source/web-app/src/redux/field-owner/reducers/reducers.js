import { combineReducers } from 'redux';
import fieldReducer  from './field-reducer';
import matchReducer from './match-reducer';
import TimeReducer from './time-reducer';
import guestReducer from '../../guest/guest-reducers';
const reducer = combineReducers({
    listField: fieldReducer,
    listMatch: matchReducer,
    timeEnable: TimeReducer,
    auth: guestReducer
});

export default reducer;