import {combineReducers} from 'redux';

import FieldReducer from './field-owner/reducers/field-reducer';

export default combineReducers({
    field: FieldReducer,
})