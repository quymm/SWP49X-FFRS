// import { createStore, applyMiddleware, compose } from 'redux';
// import promiseMiddleware from 'redux-promise-middleware';
// import logger from 'redux-logger';
// import reducers from './reducers';
// //install 2 cai kia vao ghi xong roi, gio sai cho nao, bay h m muon xai middeware hay sao, cho nao can maptostate dm hie ureduxx k, thi maptopros la lay state minh muon
// const middlewares = [
//   promiseMiddleware(),
// ]
// //copy giong cai nay di, store cua redux, copy cai nao, m nhan nham nut dp
// if (__DEV__) { // eslint-disable-line
//   middlewares.push(logger);
// }
// const enhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

// export default createStore(
//   reducers,
//   undefined,
//   enhancers(applyMiddleware(...middlewares)),
// );
// //dm eo bam ctrl dc