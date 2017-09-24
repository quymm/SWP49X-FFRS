import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './routers/App';
import registerServiceWorker from './registerServiceWorker';
import './resource/css/bootstrap.css';
import './resource/css/sb-admin-2.css';
import './resource/css/myStyle.css';
// import './resource/css/font-awesome.css';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
