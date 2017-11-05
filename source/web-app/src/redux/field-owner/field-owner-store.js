import { createStore } from 'redux';
import reducer from './reducers/reducers';
import { autoRehydrate } from 'redux-persist';

const fieldOwnerStore = createStore(reducer, undefined, autoRehydrate());

export default fieldOwnerStore;
