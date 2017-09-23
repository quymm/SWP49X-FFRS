import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom'
import Home from '../components/Home'
import Navigation from '../components/Navigation';
import Index from '../components/Index'
import Header from '../components/Header'
class App extends Component {
  render() {
    return (
      <Router>
      <div>
        <Route exact path="/" component={Index}> 
        
        </Route>
      </div>
    </Router>
    );
  }
}

export default App;
