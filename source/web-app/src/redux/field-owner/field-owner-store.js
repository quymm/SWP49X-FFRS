import { createStore } from 'redux';
import fieldReducer from './reducers/field-reducer';

const fieldOwnerStore = createStore(fieldReducer);

export default fieldOwnerStore;