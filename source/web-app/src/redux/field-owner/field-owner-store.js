import { createStore } from 'redux';
import reducer from './reducers/reducers';
const fieldOwnerStore = createStore(reducer);
export default fieldOwnerStore;
