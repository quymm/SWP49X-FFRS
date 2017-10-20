import React from 'react';
import Field from '../components/Field';
import ProfilePlayer from '../components/ProfilePlayer';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import SettingTime from '../components/Setting-time';
import Home from '../components/Home';
import FormCreateField from '../containts/Form-Create-Field';
import { Route, Switch } from 'react-router-dom';
import FreeTime from '../components/FreeTime';
// import PageNotFound from '../components/PageNotFound';

const Roster = ({ match }) => (
  <div>
    <Header />
    <Navigation />
    <Switch>
      <Route  path={`${match.url}/create-field`} component={FormCreateField} />
      <Route exact path={`${match.url}/index`} component={Home} />
      <Route path={`${match.url}/field`} component={Field} />
      <Route path={`${match.url}/player`} component={ProfilePlayer} />
      <Route path={`${match.url}/setting-time`} component={SettingTime} />
      <Route path={`${match.url}/free-time`} component={FreeTime} />
    </Switch>
  </div>
);

export default Roster;
