import React, { Component } from 'react';
import { View, Text, ScrollView, Image } from 'react-native';
import { List, Button, Icon, SideMenu } from 'react-native-elements';

import Profile from './../profile';

import I18n from '../../../constants/locales/i18n';
import ButtonGroupHeader from './components/ButtonGroupHeader';
import HomeNavigator from '../../routes/HomeNavigator';
import { UpcomingScreen } from './upcoming';

import Colors from '../../../constants/Colors';

class HomeScreen extends Component {

  constructor(props) {
    super(props);
  }



  static navigationOptions = ({
    ...props
  }) => ({
      // headerTintColor: 'blue',
      headerTintColor: '#ffffff',
      headerLeft: null, //hide back button
      headerTitle:<Image style={{width:30, height:30, marginLeft:15}}
      source={require('../../../assets/images/mola-logo.png')}/>,
      headerRight: <ButtonGroupHeader navigation={props.navigation} />,
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },


    });

  render() {
    return (
      <UpcomingScreen screenProps={this.props.navigation}/>
    );
  }
}

export default HomeScreen;
