import React from 'react';
import {Icon} from 'react-native-elements';
import {TabNavigator} from 'react-navigation';
import {NavigationComponent} from 'react-native-material-bottom-navigation'
import HomeNavigator from './HomeNavigator';
import {ProfileScreen, ScheduleScreen, CourseScreen, MessageScreen, ConversationList, SelectTimeSlotScreen, CourseRegistedScreen, HomeRequestScreen} from '../screens';
import HomeScreen from '../screens/home/HomeScreen';
import Colors from '../../constants/Colors';

const screens = {
 Home: {
    screen: HomeScreen
  },
  Schedule: {
    screen: ScheduleScreen
  },
  Message: {
    screen: ConversationList
  },
  Course: {
    screen: CourseRegistedScreen
  },
  Request: {
    screen: HomeRequestScreen
  }
};

export default TabNavigator(screens, {
  lazy: true,
  swipeEnabled: false,
  animationEnabled: false,
  tabBarPosition: 'bottom',
  tabBarComponent: NavigationComponent,
  tabBarOptions: {
    style:{
      borderTopColor: '#ebebeb',
      borderTopWidth:1,
    },
    bottomNavigationOptions: {
      rippleColor: Colors.lightGreen,
      tabs: {
        Home: {
          barBackgroundColor: Colors.whiteColor,
          icon: <Icon name="home" color={Colors.grayColor}/>,
          activeLabelColor: Colors.darkGreen,
          activeIcon: <Icon name="home" color={Colors.darkGreen}/>
        },
        Schedule: {
          barBackgroundColor: Colors.whiteColor,
          icon: <Icon type="material-community" name="calendar-clock" color={Colors.grayColor}/>,
          activeLabelColor: Colors.darkGreen,
          activeIcon: <Icon type="material-community" name="calendar-clock" color={Colors.darkGreen}/>
        },
        Message: {
          barBackgroundColor: Colors.whiteColor,
          icon: <Icon name="message" color={Colors.grayColor}/>,
          activeLabelColor: Colors.darkGreen,
          activeIcon: <Icon name="message" color={Colors.darkGreen}/>
        },
        Course: {
          barBackgroundColor: Colors.whiteColor,
          icon: <Icon name="book" color={Colors.grayColor}/>,
          activeLabelColor: Colors.darkGreen,
          activeIcon: <Icon name="book" color={Colors.darkGreen}/>
        },
        Request: {
          barBackgroundColor: Colors.whiteColor,
          icon: <Icon name="swap-vertical-circle" color={Colors.grayColor}/>,
          activeLabelColor: Colors.darkGreen,
          activeIcon: <Icon name="swap-vertical-circle" color={Colors.darkGreen}/>
        }
      }
    },
    showIcon: true,   
  }
});
