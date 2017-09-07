import React, { Component } from 'react';
import { View, Text, ScrollView } from 'react-native';
import { List, Button, Icon, SideMenu } from 'react-native-elements';



import I18n from '../../../../constants/locales/i18n';
import ButtonGroupHeader from '../components/ButtonGroupHeader';
import HomeNavigator from '../../../routes/HomeNavigator';
import { UpcomingScreen } from '../upcoming';

import Colors from '../../../../constants/Colors';

class HomeRequestScreen extends Component {

  constructor(props) {
    super(props);
  }

  static navigationOptions = ({
    ...props
  }) => ({
      // headerTintColor: 'blue',
      headerTitle: I18n.t('request'),
      headerTintColor: '#ffffff',
      headerLeft: null,
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },


    });

  render() {
    return (
      <HomeNavigator screenProps={this.props.navigation}/>
    );
  }
}

export default HomeRequestScreen;
