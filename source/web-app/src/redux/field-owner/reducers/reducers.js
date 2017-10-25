import { combineReducers } from 'redux';
import fieldReducer  from './field-reducer';
import matchReducer from './match-reducer';
import TimeReducer from './time-reducer';
import guestReducer from '../../guest/guest-reducers';
import freeTimeReducer from './free-time-reducer';
import freeFieldReducer from './free-field-reducer';
import notifyReducer from './notify-reducer';
const reducer = combineReducers({
    listField: fieldReducer,
    listMatch: matchReducer,
    timeEnable: TimeReducer,
    auth: guestReducer,
    freeTime: freeTimeReducer,
    freeField: freeFieldReducer,
    notify: notifyReducer,
});

export default reducer;