import { createStore } from 'redux';
import reducer from './reducers/reducers';
import { autoRehydrate } from 'redux-persist';
import { persistStore } from 'redux-persist';

// const fieldOwnerStore = createStore(reducer, undefined, autoRehydrate());
// persistStore(fieldOwnerStore);
// persistStore(fieldOwnerStore, {blacklist: ['someTransientReducer']}, () => {
//   console.log('rehydration complete')
// })
const fieldOwnerStore = createStore(reducer);
export default fieldOwnerStore;
