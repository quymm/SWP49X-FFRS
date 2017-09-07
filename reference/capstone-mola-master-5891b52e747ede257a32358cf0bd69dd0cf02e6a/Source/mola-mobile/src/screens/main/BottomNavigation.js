import React, { Component } from 'react';
import BottomNavigation, { Tab } from 'react-native-material-bottom-navigation';
import Icon from 'react-native-vector-icons/MaterialIcons';
import Colors from '../../../constants/Colors';

class MainBottomNavigation extends Component {
  render() {
    return (
      <BottomNavigation
        labelColor="white"
        rippleColor="white"
        style={{ height: 56, elevation: 8, position: 'absolute', left: 0, bottom: 0, right: 0 }}
        onTabChange={(newTabIndex) => alert(`New Tab at position ${newTabIndex}`)}>
        <Tab
          barBackgroundColor="#37474F"
          label="Home"
          icon={<Icon size={24} color="white" name="home" />}
        />
        <Tab
          barBackgroundColor="#00796B"
          label="Schedule"
          icon={<Icon type="material-community" name="calendar-clock" color={Colors.whiteColor}/>}
        />
        <Tab
          barBackgroundColor="#5D4037"
          label="Message"
          icon={<Icon size={24} color={Colors.whiteColor} name="message" />}
        />
        <Tab
          barBackgroundColor="#3E2723"
          label="Course"
          icon={<Icon size={24} color="white" name="book" />}
        />
      </BottomNavigation>
    )
  }
}
export default MainBottomNavigation;