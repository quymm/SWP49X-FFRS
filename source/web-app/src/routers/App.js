import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Provider } from 'react-redux';
import fieldOwnerStore from '../redux/field-owner/field-owner-store';
import Login from '../components/Login';
import Register from '../components/Register';
import Roster from './Roster';
// import { persistStore } from 'redux-persist';

//import Store from '../redux/store';
// import { BASE_URL, LOGIN } from '../apis/base-URL'

class App extends Component {
  render() {
    // persistStore(fieldOwnerStore);
    // persistStore(fieldOwnerStore, {blacklist: ['someTransientReducer']}, () => {
    //   console.log('rehydration complete')
    // })
    return (
      <Provider store={fieldOwnerStore}>
        <div>
          <Router>
            <Switch>
              <Route path="/login" component={Login} />
              <Route path="/register" component={Register} />
              <Route path="/update" component={Register} />
              {/* <Route path="/register" component={} />> */}
              <Route path="/app" component={Roster} />
            </Switch>
          </Router>
        </div>
      </Provider>
    );
  }
}

export default App;
