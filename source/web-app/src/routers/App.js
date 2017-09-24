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
import Field from '../components/Field';
import Store from '../redux/store';
// import { BASE_URL, LOGIN } from '../apis/base-URL'
class App extends Component {
  render() {

    return (
      <Provider store={fieldOwnerStore}>
      <Router>
        <div>
              <Route path="/field" component={Field} />
        </div>
      </Router>
      </Provider>
    );
  }
}

export default App;
