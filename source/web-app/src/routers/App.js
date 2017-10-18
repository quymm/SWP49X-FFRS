import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Provider } from 'react-redux';
import fieldOwnerStore from '../redux/field-owner/field-owner-store';
import Login from '../components/Login';
import Register from '../components/Register';
import Roster from './Roster';
import PageNotFound from '../components/PageNotFound';
//import Store from '../redux/store';
// import { BASE_URL, LOGIN } from '../apis/base-URL'

class App extends Component {
  render() {
    
    return (
      <Provider store={fieldOwnerStore}>
        <div>
          <Router>
            <Switch>
              <Route path="/login" component={Login} />
              <Route path="/register" component={Register} />
              <Route path="/app" component={Roster} />
              <Route component={PageNotFound}/>
            </Switch>
          </Router>
        </div>
      </Provider>
    );
  }
}

export default App;
