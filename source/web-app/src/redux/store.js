import { createStore, applyMiddleware, compose } from 'redux';
import promiseMiddleware from 'redux-promise-middleware';
import logger from 'redux-logger';
import reducers from './reducers';
//install 2 cai kia vao ghi xong roi, gio sai cho nao, bay h m muon xai middeware hay sao, cho nao can maptostate dm hie ureduxx k, thi maptopros la lay state minh muon
//tai cai project m nat qua, doc eo hieu cau truc sao, nen t moi lam lai
const middlewares = [
  promiseMiddleware(),
]
//2 store saocho vo provider dc =)), thi chia ra 2 provider, tai phan quyen ma, m phan o frontend a :)) uhmvcl =)) 
//nay gio m xoa het roi a, uhm =)), co xai dathoui thoi eo lam nua =)) vc, cai do dung anotation, con t ko xai thi import lam gi, sao k, lam theo cach giong t no kieu do
//copy giong cai nay di, store cua redux, copy cai nao, m nhan nham nut dp
if (__DEV__) { // eslint-disable-line
  middlewares.push(logger);
}
const enhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

export default createStore(
  reducers,
  undefined,
  enhancers(applyMiddleware(...middlewares)),
);