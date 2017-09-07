import React, { Component } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet, ActivityIndicator, Switch, AsyncStorage, TextInput,RefreshControl } from 'react-native';
import { List, ListItem, Button, Icon, } from 'react-native-elements';
import InCallManager from 'react-native-incall-manager';
import moment from 'moment';

import SessionAPI from '../../../apis/session';
import { NavigationActions } from 'react-navigation'
import Lesson from './../../../components/course/Lesson';
import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';
import styles from './styles/UpcomingScreen';


import io from 'socket.io-client';
import { connect } from 'react-redux';
import { setNextUpcomingSession, setNoUpcomingSession } from '../../home/actions';

import FCM from "react-native-fcm";

import PushController from "../../../services/fcm/PushController";
import firebaseClient from  "../../../services/fcm/FirebaseClient";
import Ghostwriter from 'react-native-ghostwriter';

import UserSetting from '../../UserSetting';

@connect(null, { setNextUpcomingSession, setNoUpcomingSession })
class UpcomingScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      listComingLesson: [],
      loading: true,
      trueSwitchIsOn: true,
      falseSwitchIsOn: false,
      nextSession: null,
      token: '',
      user: {}
    };
    this.socket = null;
    this.ghostWriterOptions = {
      sequences: [
        { string: I18n.t('ghost1'), duration: 2500 },
        { string: I18n.t('ghost2'), duration: 2000 },
        { string: I18n.t('ghost3'), duration: 2000 },
        { string: I18n.t('ghost4'), duration: 2000 },
      ]
    };
  }
  static navigationOptions = ({
    title: I18n.t('comingup')
  });

  async _onRefresh() {
    this.setState({refreshing: true});
    const sessionAPI = new SessionAPI();
    const data = await sessionAPI.getSessionByUser();
    await this.setState({
      listComingLesson: data.data,
      loading: false,
      refreshing:false,
    });
    let nextSession = null;
    debugger
    data.data.map((session, i) => {
      const { starTime, endTime } = session.timeSlotEntity;
      // const timeUp = moment().isBetween(moment(starTime), moment(endTime));
      const timeUp = session.onTime;
      if(timeUp){
        nextSession = session;
      }
    });
    if (this.state.nextSession === null && nextSession !== null) {
      await this.props.setNextUpcomingSession({ session: nextSession, hasNextSession: true });
    } else {
      await this.props.setNoUpcomingSession();
    }

    await this.setState({ nextSession });
  }

  async componentDidMount() {
    //test notify
    FCM.getInitialNotification().then(notif => {
      this.setState({
        initNotif: notif
      })
    });
    //end notify



    const sessionAPI = new SessionAPI();
    const data = await sessionAPI.getSessionByUser();
    await this.setState({
      listComingLesson: data.data,
      loading: false,
      refreshing:false,
    });
    let nextSession = null;
    data.data.map((session, i) => {
      const { starTime, endTime } = session.timeSlotEntity;
      // const timeUp = moment().isBetween(moment(starTime), moment(endTime));
      const timeUp = session.onTime;
      if(timeUp){
        nextSession = session;
      }
    });
    if (this.state.nextSession === null && nextSession !== null) {
      await this.props.setNextUpcomingSession({ session: nextSession, hasNextSession: true });
    } else {
      await this.props.setNoUpcomingSession();
    }

    await this.setState({ nextSession });

    let user = await AsyncStorage.getItem('USER');
    user = JSON.parse(user);
    this.setState({user});
    UserSetting.isTeacher = user.isTeacher;
  }

  showLocalNotification() {
    FCM.presentLocalNotification({
      vibrate: 500,
      title: 'Hello',
      body: 'Test Notification',
      priority: "high",
      show_in_foreground: true,
      picture: 'https://firebase.google.com/_static/af7ae4b3fc/images/firebase/lockup.png'
    });
  }

  scheduleLocalNotification() {
    FCM.scheduleLocalNotification({
      id: 'testnotif',
      fire_date: new Date().getTime()+8000,
      vibrate: 500,
      title: 'Hello',
      body: 'Test Scheduled Notification',
      priority: "high",
      // repeat_interval: 'minute',
      // count: 3,
      show_in_foreground: true,
      // picture: 'https://firebase.google.com/_static/af7ae4b3fc/images/firebase/lockup.png'
    });
  }

  render() {
    if (this.state.loading) {
      return (
        <ActivityIndicator
          size="large"
          style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }} />
      )
    }
    const { listComingLesson, nextSession } = this.state;
    const { token, username, title, message} = this.state;
    return (
      <ScrollView style={{ backgroundColor: '#e9ebee' }}
        refreshControl={
          <RefreshControl
            refreshing={this.state.refreshing}
            onRefresh={this._onRefresh.bind(this)}
            tintColor={Colors.darkGreen} title="Loading..." titleColor={Colors.darkGreen} colors={
              ['#FFF', '#FFF', '#FFF']
            }
            progressBackgroundColor={Colors.darkGreen}
          />
        }
      >
        {/*test notify*/}
        {/*
        <Text>Test Notify</Text>
        <Text>Type username</Text>
        <TextInput onChangeText={username => this.setState({username})} />
        <Text>Type title</Text>
        <TextInput onChangeText={title => this.setState({title})} />
        <Text>Type message</Text>
        <TextInput onChangeText={message => this.setState({message})} />
        
        <PushController
          onChangeToken={token => this.setState({token: token || ""})}
        />
        
        <Button title={`Send ${this.state.username}`} onPress={() => firebaseClient.sendToSomeone(username, title, message)} />
        
        <Button title='Send Notification' onPress={() => firebaseClient.sendNotification(token)} />

        <Button title='Send Data' onPress={() => firebaseClient.sendData(token)} />

        <Button title='Send Notification With Data' onPress={() => firebaseClient.sendNotificationWithData(token)} />

        <Button title='Send Local Notification' onPress={() => this.showLocalNotification()} />

        <Button title='Schedule Notification in 5s' onPress={() => this.scheduleLocalNotification()} />
        */}
          
        {/*end notify*/}
        {/* <View style={css.switch}>
          <Text style={css.textTop}>{I18n.t('available')}</Text>
          <Switch
            thumbTintColor={Colors.darkGreen}
            onValueChange={(value) => this.setState({ falseSwitchIsOn: value })}
            value={this.state.falseSwitchIsOn} />
        </View> */}

        {listComingLesson.length > 0
          ? <List style={{ marginTop: 1, padding: 10 }}>
            {listComingLesson.map((session, i) => {              
              return (
                <Lesson key={i}
                  style={{ backgroundColor: 'white' }}
                  socketBoolean={nextSession&&nextSession.sessionEntity.id === session.sessionEntity.id}
                  session={session}
                  mainNavigation={this.props.screenProps}
                  />
              )}
            )}
          </List>
          : <View style={css.container}>
            <Ghostwriter
            stringStyles={css.title}
            options={this.ghostWriterOptions} 
            clearEverySequence={true}
            />

            {/* <Text style={css.title}>{I18n.t('lookingForSome')}</Text> */}
            <Text style={css.text}>{I18n.t('textUpcoming')}</Text>
            <TouchableOpacity style={css.button} onPress={()=> this.props.screenProps.navigate('SearchScreen')}>
              <Text style={{ color: 'white', fontSize: 20 }}>{I18n.t('findTeacher')}</Text>
            </TouchableOpacity>
          </View>

        }

      </ScrollView>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    marginTop: 80,
    margin: 30
  },
  switch:{
    backgroundColor:'white',
    flexDirection:'row',
    alignItems:'center',
    justifyContent:'space-between',
    marginTop:10,
    margin:10,
    marginBottom:10,
    padding:15,
    elevation:2
  },
  textTop:{
    fontSize:20,
    marginLeft:20
  },
  title: {
    fontSize: 25,
    fontWeight: 'bold'
  },
  text: {
    fontSize: 20
  },
  button: {
    margin: 30,
    backgroundColor: Colors.darkGreen,
    height: 50,
    width: 200,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10
  }
})

export default UpcomingScreen;