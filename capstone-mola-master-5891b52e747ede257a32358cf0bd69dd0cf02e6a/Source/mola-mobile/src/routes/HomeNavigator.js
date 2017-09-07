import {TabNavigator} from 'react-navigation';
import {NavigationComponent} from 'react-native-material-bottom-navigation'
import {HomeScreen, RequestScreen, SendingRequestScreen} from '../screens';
import I18n from '../../constants/locales/i18n';
import Colors from '../../constants/Colors';

const homeScreens = {
  SendingRequest: {
    screen: SendingRequestScreen
  },
  RequestScreen: {
    screen: RequestScreen
  }
};

export default TabNavigator(homeScreens, {
  swipeEnabled: false,
  animationEnabled: false,
  lazy: true,
  // tabBarComponent: NavigationComponent,
  tabBarOptions: {
    // showIcon: true,
    activeTintColor: Colors.lightGreen,
    inactiveTintColor: Colors.grayColor,
    pressColor: Colors.lightGreen,
    indicatorStyle: {
      backgroundColor: Colors.lightGreen,
    },
    style: {
      backgroundColor: Colors.whiteColor
    }
  }

});
