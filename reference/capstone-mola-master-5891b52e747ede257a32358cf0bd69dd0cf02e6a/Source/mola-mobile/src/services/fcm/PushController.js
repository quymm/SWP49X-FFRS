import React, { Component } from "react";
import { Platform } from 'react-native';
import FCM, {
  FCMEvent,
  RemoteNotificationResult,
  WillPresentNotificationResult,
  NotificationType
} from "react-native-fcm";

import FirebaseAPI from '../../apis/fcm';

import firebaseClient from  "./FirebaseClient";

const fcmAPI = new FirebaseAPI();

export default class PushController extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    FCM.requestPermissions();

    FCM.getFCMToken().then(token => {
      console.log("TOKEN (getFCMToken)", token);
      this.props.onChangeToken(token);
      fcmAPI.saveTokenToServer(token);
    });

    FCM.getInitialNotification().then(notif => {
      console.log("INITIAL NOTIFICATION", notif)
    });

    this.notificationListener = FCM.on(FCMEvent.Notification, notif => {
      
      console.log("Notification", notif);
      if(notif.local_notification){
        return;
      }
      if(notif.opened_from_tray){
        return;
      }
      if (notif.fcm) {
        FCM.presentLocalNotification({
          vibrate: 500,
          title: notif.fcm.title,
          body: notif.fcm.body,
          click_action: notif.fcm.action,
          priority: "high",
          show_in_foreground: true,
          picture: 'https://firebase.google.com/_static/af7ae4b3fc/images/firebase/lockup.png'
        });
      }

      this.refreshTokenListener = FCM.on(FCMEvent.RefreshToken, token => {
        console.log("TOKEN (refreshUnsubscribe)", token);
        this.props.onChangeToken(token);
        fcmAPI.saveTokenToServer(token);
      });

      // direct channel related methods are ios only
      // directly channel is truned off in iOS by default, this method enables it
      FCM.enableDirectChannel();
      this.channelConnectionListener = FCM.on(FCMEvent.DirectChannelConnectionChanged, (data) => {
        console.log('direct channel connected' + data);
      });
      setTimeout(function() {
        FCM.isDirectChannelEstablished().then(d => console.log('isDirectChannelEstablished', d));
      }, 1000);
    })
  }

  componentWillUnmount() {
    this.notificationListener && this.notificationListener.remove();
    this.refreshTokenListener && this.refreshTokenListener.remove();
  }


  render() {
    return null;
  }
}