import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Provider } from 'react-redux';
import fieldOwnerStore from '../redux/field-owner/field-owner-store';
import Home from '../components/Home';
import Login from '../components/Login';
import Register from '../components/Register';
import Field from '../components/Field';
import ProfilePlayer from '../components/ProfilePlayer';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
//import Store from '../redux/store';
// import { BASE_URL, LOGIN } from '../apis/base-URL'
class App extends Component {
  render() {
    return (
      <Provider store={fieldOwnerStore}>
        <Router>
          <div>
            <Header />
            <Navigation />
            <Route path="/index" component={Home} />
            <Route path="/field" component={Field} />
            <Route path="/player" component={ProfilePlayer} />
          </div>
        </Router>
      </Provider>
    );
  }
}

export default App;
