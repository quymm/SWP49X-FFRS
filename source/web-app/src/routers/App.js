import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route, Switch
} from 'react-router-dom'
import { Provider } from 'react-redux';
import fieldOwnerStore from '../redux/field-owner/field-owner-store'
import FieldOwnerIndex from '../components/FieldOwnerIndex';
import Login from '../components/Login'
import Register from '../components/Register';
import Field from '../components/Field'
// import { BASE_URL, LOGIN } from '../apis/base-URL'
class App extends Component {
  render() {

    return (
      <Router>
        <div>
          <Switch>
            <Route path="/login" component={Login} />
            <Route path="/register" component={Register} />
            <Provider store={fieldOwnerStore}>
              <div>
              <Route exact path="/fieldowner" component={FieldOwnerIndex} />
              <Route path="/field" component={Field} />
              </div>
            </Provider>
          </Switch>
        </div>
      </Router>
    );
  }
}

export default App;
