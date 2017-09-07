import React, { Component } from 'react';
import { Icon, Button, ButtonGroup, } from 'react-native-elements';
import { View, Text, StyleSheet } from 'react-native';
import Color from '../../../../constants/Colors';

class ButtonGroupHeader extends Component {
  constructor() {
    super()
    this.state = {
      selectedIndex: 1
    }
    this.updateIndex = this
      .updateIndex
      .bind(this)
  }
  updateIndex(selectedIndex) {
    this.setState({ selectedIndex })
  }
  goToTeacherSignUpScreen() {
    this.props.navigation.navigate('TeacherSignUp');
  }
  goToSearchScreen() {
    this.props.navigation.navigate('SearchScreen');
  }
  goToSettingScreen() {
    this.props.navigation.navigate('SettingScreen');
  }

  render() {
    const buttons = [
      
      {
        element: () => <Icon name='search' color='white' onPress={() => this.goToSearchScreen()} />
      },
      {
        element: () => <Icon name='school' color='white' onPress={() => this.goToTeacherSignUpScreen()} />
      },
      {
        element: () => <Icon name='ios-options-outline' type='ionicon' color='white' onPress={() => this.goToSettingScreen()} />
      },
    ]
    const { selectedIndex } = this.state
    return (<ButtonGroup
      selectedBackgroundColor={Color.darkGreen}
      onPress={this.updateIndex}
      selectedIndex={selectedIndex}
      buttons={buttons}
      containerStyle={css.buttonGroup}
      innerBorderStyle={
        {
          width: 0,
          color: Color.darkGreen,
        }
      }
      underlayColor={Color.lightGreen}
    />);
  }
}
var css = StyleSheet.create({
  buttonGroup: {
    backgroundColor: Color.darkGreen,
    height: '100%',
    width: 150,
    borderColor: Color.darkGreen,
  },

})
export default ButtonGroupHeader;