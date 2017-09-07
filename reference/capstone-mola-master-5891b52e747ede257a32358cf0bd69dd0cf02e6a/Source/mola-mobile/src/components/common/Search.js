import React, {Component} from 'react';
import {SearchBar} from 'react-native-elements';

import I18n from '../../../constants/locales/i18n';
import Colors from '../../../constants/Colors';

class Search extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: ''
    }
    this.goToSearchScreen = this
      .goToSearchScreen
      .bind(this);
  }

  goToSearchScreen(text) {
    const {navigation} = this.props;
    navigation.navigate('SearchResult');     
  }

  render() {
    const {text} = this.state;

    return (<SearchBar
      round
      lightTheme
      //onChangeText={() => this.goToSearchScreen()}
      placeholder='Type Here...'
      //focus={() => this.goToSearchScreen()}
      focus={() => console.log(414)}
      //backgroundColor={Colors.transparent}
      />);
  }
}

export default Search;
