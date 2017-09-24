import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './routers/App';
import registerServiceWorker from './registerServiceWorker';
import './resource/css/bootstrap.css';
import './resource/css/sb-admin-2.css';
import './resource/css/myStyle.css';
import fieldOwnerStore from './redux/field-owner/field-owner-store'
import { Provider } from 'react-redux';
import Field from './components/Field';
import {
    BrowserRouter as Router,
    Route, Switch
  } from 'react-router-dom';
// import './resource/css/font-awesome.css';

ReactDOM.render(
<Provider store={fieldOwnerStore}>
      <Router>
        <div>
              <Route path="/field" component={Field} />
        </div>
      </Router>
      </Provider>
      , document.getElementById('root'));
registerServiceWorker();
