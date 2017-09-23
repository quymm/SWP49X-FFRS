import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom'
import Index from '../components/Index';
import Login from '../components/Login'

class App extends Component {
  render() {
    return (
      <Router>
      <div>
        <Route path="/login" component={Login}></Route>
        <Route exact path="/" component={Index}> 
        
        </Route>
      </div>
    </Router>
    );
  }
}

export default App;
