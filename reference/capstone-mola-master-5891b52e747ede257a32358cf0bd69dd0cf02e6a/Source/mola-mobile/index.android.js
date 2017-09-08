import React, {Component} from 'react';
import {AppRegistry} from 'react-native';
import {Provider} from 'react-redux';
import store from './src/redux/store';
import Root from './src/Root';

export default class molaappdemo3 extends Component {

  constructor(props) {
    super(props);
    this.state = {
      fontLoaded: false
    }
  }

  componentDidMount() {
    this._loadAssetsAsync();
  }

  async _loadAssetsAsync() {
    // await Promise.all(fontAssets); this.setState({fontLoaded: true});
  }

  render() {
    return (
      // <ThemeProvider uiTheme={uiTheme}>
        <Provider store={store}>
          <Root/>
        </Provider>
      //  {/*</ThemeProvider>*/}

    );
  }
}

AppRegistry.registerComponent('molaappdemo3', () => molaappdemo3);
