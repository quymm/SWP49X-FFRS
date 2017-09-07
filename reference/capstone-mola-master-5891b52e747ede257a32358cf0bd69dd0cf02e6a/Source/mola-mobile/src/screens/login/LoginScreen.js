import React, { Component } from 'react';
import {
  View,
  Text,
  ScrollView,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Image,
  AsyncStorage,
  ActivityIndicator,
  StatusBar,
  Dimensions,
  BackHandler,
  ToastAndroid
} from 'react-native';
import Spinner from 'react-native-loading-spinner-overlay';
import {
  LoginButton,
  LoginManager,
  AccessToken,
} from 'react-native-fbsdk';
import {NavigationActions} from 'react-navigation';
import { connect } from 'react-redux';

import { Icon, Button, SocialIcon } from 'react-native-elements';
import FontAwesomeIcon from 'react-native-vector-icons/FontAwesome';

import { loginUsernamePassword, loginWithFacebook, reloginUsernameToken } from './actions';
import I18n from '../../../constants/locales/i18n';
import Color from '../../../constants/Colors';
import UserSetting from '../UserSetting';
import ExchangeRates from '../ExchangeRates';
import FCM, {
  FCMEvent,
  RemoteNotificationResult,
  WillPresentNotificationResult,
  NotificationType
} from "react-native-fcm";
import FirebaseAPI from '../../apis/fcm';
import qs from 'qs';

import firebaseClient from "../../services/fcm/FirebaseClient";

const fcmAPI = new FirebaseAPI();
const mapStateToProps = (state) => ({ user: state.auth.user.data });
const onHomeScreen = true;
var { height, width } = Dimensions.get('window');

console.ignoredYellowBox = ['Warning: Can only update'];

@connect(mapStateToProps, { loginUsernamePassword, loginWithFacebook, reloginUsernameToken })
class LoginScreen extends Component {
  state = {
    username: '',
    password: '',
    isLogging: false,
    isLoggedInSuccess: false,
    accessToken: '',
    loading: false,
  }
  static navigationOptions = ({ header: null });

  async saveUserInStorage() {
    try {
      if (this.state.isLoggedInSuccess) {
        const { username } = this.state;
        const userData = this.props.user.data;
        await AsyncStorage.setItem('USER_TOKEN', userData.token);
        await AsyncStorage.setItem('USER', JSON.stringify(userData.user));
      } else {
        console.error('Username empty');
      }
    } catch (err) {

      console.log(err);
    }
  }

  async saveFirebaseToken() {
    FCM.requestPermissions();

    FCM.getFCMToken().then(token => {
      console.log("TOKEN (getFCMToken)", token);
      fcmAPI.saveTokenToServer(token);
    });

    FCM.getInitialNotification().then(notif => {
      console.log("INITIAL NOTIFICATION", notif)
    });

    this.notificationListener = FCM.on(FCMEvent.Notification, notif => {
      console.log("Notification", notif);
      if (notif.local_notification) {
        return;
      }
      if (notif.opened_from_tray) {
        return;
      }
      if (notif.fcm) {
        const body = notif.fcm.body.split('@');
        FCM.presentLocalNotification({
          vibrate: 500,
          title: notif.fcm.title,
          body: notif.fcm.body,
          click_action: notif.fcm.action,
          priority: "high",
          show_in_foreground: true,
        });
      }

      this.refreshTokenListener = FCM.on(FCMEvent.RefreshToken, token => {
        console.log("TOKEN (refreshUnsubscribe)", token);
        fcmAPI.saveTokenToServer(token);
      });

      // direct channel related methods are ios only
      // directly channel is truned off in iOS by default, this method enables it
      FCM.enableDirectChannel();
      this.channelConnectionListener = FCM.on(FCMEvent.DirectChannelConnectionChanged, (data) => {
        console.log('direct channel connected' + data);
      });
      setTimeout(function () {
        FCM.isDirectChannelEstablished().then(d => console.log('isDirectChannelEstablished', d));
      }, 1000);
    })

  }

  goToHomeScreen() {
    this.setState({ isLoggedInSuccess: true, loading: false });
    // this.props.navigation.navigate('Main')
    const resetAction = NavigationActions.reset({
      index: 0,
      actions: [
        NavigationActions.navigate({ routeName: 'Main'})
      ]
    })
    this.props.navigation.dispatch(resetAction)
  }
  async componentWillMount() {

    let user = await AsyncStorage.getItem('USER');
    const token = await AsyncStorage.getItem('USER_TOKEN');

    if (user && token) {
      this.setState({ loading: true })
      user = JSON.parse(user);
      const username = user.username;
      await this.props.reloginUsernameToken({ username, token }).catch(err => {
        ToastAndroid.show('ERROR vui long thu lai', ToastAndroid.SHORT);
      });

      if (this.props.user.status === 'ok') {
        await this.setState({ isLoggedInSuccess: true });
        this.saveUserInStorage();
        await this.saveFirebaseToken();
        this.goToHomeScreen();
      } else {

        this.setState({ loading: false });
        ToastAndroid.show(I18n.t('tokenExpire'), ToastAndroid.SHORT);
      }
      // await this.setState({ loading: false })
    }
    // this.setState({ loading: false });

    await UserSetting.loadAppSetting();
    await ExchangeRates.loadApi();
  }
  async onLoginPress() {
    await this.setState({ loading: true });
    const { username, password } = this.state;

  const userlogin = await this.props.loginUsernamePassword({ username, password }).catch(err => {
      ToastAndroid.show('ERROR vui long thu lai', ToastAndroid.SHORT);
    });

    if (this.props.user && this.props.user.status === 'ok') {
      await this.setState({ isLoggedInSuccess: true });
      await this.saveFirebaseToken();
      this.saveUserInStorage();
      this.goToHomeScreen();
    } else {
      await this.setState({ isLoggedInSuccess: false, loading: false });
      await this.props.navigation.navigate('Login');
    }

  }
  async onLoginFacebookPress(accessToken) {
    await this.setState({ loading: true });
    await this.props.loginWithFacebook({ accessToken: accessToken });

    // TODO kiem tra login sai, show message
    if (this.props.user.status === 'ok') {
      await this.setState({ isLoggedInSuccess: true });
      await this.saveFirebaseToken();
      this.saveUserInStorage();
      this.goToHomeScreen();
    } else {
      await this.setState({ isLoggedInSuccess: false, loading: false });
    }

  }

  render() {
    return (
      <Image style={css.bgImg} source={require('../../../assets/images/bg.png')}>
        <StatusBar backgroundColor={Color.statusBar}
          //translucent
          //hidden
          showHideTransition='fade'
          barStyle="default" />
        <View style={css.container}>
          {
            this.state.loading

              ? <View style={{ width: width, height: height, position: 'absolute', justifyContent: 'center', backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1 }}>
                <ActivityIndicator size='large' />
              </View>

              : <View></View>
          }
          <Image
            style={{
              width: 165,
              height: 165,
              marginBottom: 50,
              marginTop: 50
            }}
            source={require('../../../assets/images/mola-logo.png')} />


          <View style={css.input}>
            <Icon
              name='perm-identity'
              color='white'
              style={{
                marginRight: 20
              }} />
            <TextInput
              style={{
                width: 200,
                color: 'white',
                fontSize: 15
              }}
              onChangeText={(username) => this.setState({ username })}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('username')}
              placeholderTextColor='white'></TextInput>
          </View>

          <View style={css.input}>
            <Icon
              name='lock'
              color='white'
              style={{
                marginRight: 20
              }} />
            <TextInput
              underlineColorAndroid='transparent'
              secureTextEntry={true}
              style={{
                width: 200,
                color: 'white',
                fontSize: 15
              }}
              onChangeText={(password) => this.setState({ password })}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('password')}
              placeholderTextColor='white'></TextInput>
          </View>

          <View
            style={{
              flexDirection: 'row',
              marginTop: 20
            }}>
            <Button
              buttonStyle={css.button}
              title={I18n.t('signUp')}
              textStyle={{
                fontSize: 20
              }}
              onPress={() => this.props.navigation.navigate('Signup')} />
            <Button
              buttonStyle={css.button}
              title={I18n.t('signIn')}
              textStyle={{
                fontSize: 20
              }}
              onPress={() => {
                this.onLoginPress()
              }} />
          </View>
          <SocialIcon
            style={css.button1}
            title={I18n.t('signInGoogle')}
            button
            type='google-plus-official'
            onPress={() => {
              console.log('saf')  
            }} />
          <SocialIcon
            disable={true}
            style={css.button2}
            title={I18n.t('signInFacebook')}
            button

            onPress={async () => {
              const response = await LoginManager.logInWithReadPermissions(['public_profile']);
              if (response.isCancelled) {
                alert('Login was cancelled');
              } else {
                const { accessToken } = await AccessToken.getCurrentAccessToken();
                console.log('token', accessToken);
                this.onLoginFacebookPress({ accessToken });
              }
            }}
            type='facebook' />
        </View >
      </Image>

    );
  }

}
var css = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    paddingTop: 30
  },
  bgImg: {
    flex: 1,
    resizeMode: 'cover',
    height: null,
    width: null
  },
  textinput: {
    width: 200,
    color: 'white',
    fontSize: 15
  },
  text: {
    color: 'white'
  },
  input: {
    borderBottomColor: 'white',
    borderBottomWidth: 1,
    width: 340,
    height: 50,
    flexDirection: 'row',
    alignItems: 'center'
  },

  button1: {
    backgroundColor: '#4BB9AE',
    elevation: 0,
    width: 340,
    margin: 10,
    borderRadius: 10
  },
  button2: {
    backgroundColor: '#4BB9AE',
    elevation: 0,
    width: 340,
    margin: 10,
    borderRadius: 10
  },
  button: {
    backgroundColor: '#4BB9AE',
    width: 150,
    marginBottom: 20,
    borderRadius: 10,
  }

})

export default LoginScreen;