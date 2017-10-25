import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './routers/App';
import registerServiceWorker from './registerServiceWorker';
// import './resource/css/bootstrap.css';
// import './resource/css/sb-admin-2.css';
import './resource/css/myStyle.css';
// import './resource/css/main-style.css';
// import './resource/css/style.css';
// import './resource/css/font-awesome.css';
import './resource/css/animate.min.css';
import './resource/css/bootstrap.min.css';
import './resource/css/light-bootstrap-dashboard.css';
import './resource/css/pe-icon-7-stroke.css';
import './resource/css/demo.css';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
