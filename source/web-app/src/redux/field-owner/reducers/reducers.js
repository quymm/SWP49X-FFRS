import { combineReducers } from 'redux';
import fieldReducer  from './field-reducer';
import matchReducer from './match-reducer';

const reducer = combineReducers({
    field: fieldReducer,
    match: matchReducer
});

export default reducer;