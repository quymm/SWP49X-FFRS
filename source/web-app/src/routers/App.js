import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route, Switch
} from 'react-router-dom'
import Index from '../components/Index';
import Login from '../components/Login'
import Register from '../components/Register';
import { BASE_URL, LOGIN } from '../apis/base-URL'
class App extends Component {
  render() {
    return (
      <Router>
      <div>
        <Switch>
        <Route path="/login" component={Login} />
        <Route path="/register" component={Register} />
        <Route exact path="/index" component={Index} />> 
        </Switch>
      </div>
    </Router>
    );
  }
}

export default App;
