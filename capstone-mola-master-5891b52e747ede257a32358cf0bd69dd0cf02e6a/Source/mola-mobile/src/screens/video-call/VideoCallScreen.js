import React, { Component } from 'react';
import { Alert, View, Text, ListView, Dimensions, TouchableOpacity, TouchableHighlight, TextInput, ScrollView } from 'react-native';
import { Icon } from 'react-native-elements';
import { connect } from 'react-redux';
import timer from 'react-native-timer'

import {NavigationActions} from 'react-navigation';
import InCallManager from 'react-native-incall-manager';
import { RTCPeerConnection, RTCView, } from 'react-native-webrtc';

import io from 'socket.io-client';
import I18n from '../../../constants/locales/i18n';

import _ from 'underscore';
import moment from 'moment';
import { socketIP } from '../../apis/mola-api';
import DictionaryAPI from '../../apis/dictionary-api';
import { languagesAlpha3 } from '../../../constants/countries';
import LanguagesAPI from '../../apis/languages';

import Colors from '../../../constants/Colors';

import { getLocalStream, join, exchange, leave, requestPermission } from '../../services/webrtc.js';

import SessionAPI from '../../apis/session';
const sessionAPI = new SessionAPI();

var { height, width } = Dimensions.get('window');
let socket = null;
const configuration = { "iceServers": [{ "url": "stun:stun.l.google.com:19302" }] };
const pcPeers = {};
let localStream;

const createPeerConnection = (socketId, isOffer) => {
  const pc = new RTCPeerConnection(configuration);
  pcPeers[socketId] = pc;

  // send ice candidate to other peer
  pc.onicecandidate = event => {
    console.log('onicecandidate', event.candidate);
    if (event.candidate) {
      socket.emit('exchange', { 'to': socketId, 'candidate': event.candidate });
    }
  };

  const createOffer = () => {
    pc.createOffer(description => {
      console.log('createOffer', description);
      pc.setLocalDescription(description, () => {
        console.log('setLocalDescription', pc.localDescription);
        socket.emit('exchange', { 'to': socketId, 'sdp': pc.localDescription });
      }, logError);
    }, logError);
  }

  pc.onnegotiationneeded = () => {

    console.log('onnegotiationneeded');
    if (isOffer) {
      createOffer();
    }
  }

  pc.oniceconnectionstatechange = function (event) {
    console.log('oniceconnectionstatechange', event.target.iceConnectionState);
    if (event.target.iceConnectionState === 'completed') {
      setTimeout(() => {
        getStats();
      }, 1000);
    }
    if (event.target.iceConnectionState === 'connected') {
      createDataChannel();
    }
  };
  pc.onsignalingstatechange = function (event) {
    console.log('onsignalingstatechange', event.target.signalingState);
  };

  // event khi co nguoi mo stream chung room
  pc.onaddstream = function (event) {
    console.log('onaddstream', event.stream);
    self.setState({
      status: 'connected',
    });

    const remoteList = self.state.remoteList; //luu stream cua friends trong room
    remoteList[socketId] = event.stream.toURL();
    self.setState({ remoteList: remoteList });
  };
  pc.onremovestream = function (event) {
    console.log('onremovestream', event.stream);
  };

  pc.addStream(localStream);
  function createDataChannel() {
    if (pc.textDataChannel) {
      return;
    }
    const dataChannel = pc.createDataChannel("text");

    dataChannel.onerror = function (error) {
      console.log("dataChannel.onerror", error);
    };

    dataChannel.onmessage = function (event) {
      console.log("dataChannel.onmessage:", event.data);
      self.receiveTextData({ user: socketId, message: event.data });
    };

    dataChannel.onopen = function () {
      console.log('dataChannel.onopen');
      self.setState({ textRoomConnected: true });
    };

    dataChannel.onclose = function () {
      console.log("dataChannel.onclose");
    };

    pc.textDataChannel = dataChannel;
  }
  return pc;
}


const logError = (error) => {
  console.log("logError", error);
}

const mapHash = (hash, func) => {
  const array = [];
  for (const key in hash) {
    const obj = hash[key];
    array.push(func(obj, key));
  }
  return array;
}

const getStats = () => {
  const pc = pcPeers[Object.keys(pcPeers)[0]];
  if (pc.getRemoteStreams()[0] && pc.getRemoteStreams()[0].getAudioTracks()[0]) {
    const track = pc.getRemoteStreams()[0].getAudioTracks()[0];
    console.log('track', track);
    pc.getStats(track, function (report) {
      console.log('getStats report', report);
    }, logError);
  }
}
var { height, width } = Dimensions.get('window');
let self;

const mapStateToProps = (state) => ({
  upcoming: state.upcoming,
  user: state.auth.user.data.data,
});


@connect(mapStateToProps)
class VideoCallScreen extends Component {
  constructor(props) {
    super(props);
    this.ds = new ListView.DataSource({ rowHasChanged: (r1, r2) => true });
    this.state = {
      textLog: '',
      info: 'Initializing',
      status: 'init',
      roomID: '',
      isFront: true,
      selfViewSrc: null,
      remoteList: {},
      textRoomConnected: false,
      textRoomData: [],
      textRoomValue: '',
      startTime: new Date(),
      duration: '',
      durationInSeconds: 0,   
      isOffer: false,
      showTextRoom: true,
      showHelper: false,
      dictFrom: '',
      dictDest: '',
      targetName: '',
      targetFirstName: '',
      myName:'',
      startedVideoCall: false,
    };
    self = this;
  }

  static navigationOptions = ({ navigation }) => {
    return { header: null }
  };

  async componentWillMount() {
    // BackHandler.addEventListener('hardwareBackPress', function() {      
    //   return false;
    // });

    const { upcoming } = this.props;
    const session = upcoming.session.sessionEntity;
    if (session) {
      this.setState({ roomID: session.sessionRoom, });
    }
    const isOffer = this.props.navigation.state.params.isOffer;
    const startedVideoCall = this.props.navigation.state.params.startedCall;
    
    this.setState({ isOffer, startedVideoCall });

    console.log("upcoming", upcoming);

    this.setState({ dictFrom: this.getAlpha3LanguageCode(upcoming.session.courseEntity.language) });
    if (upcoming.session.teaching) {
      this.setState({
        dictDest: this.getAlpha3LanguageCode("Vietnamese"),
        targetName: `${upcoming.session.learner.firstName} ${upcoming.session.learner.lastName}` ,
        targetFirstName: `${upcoming.session.learner.firstName}`,
        myName:`${upcoming.session.userEntity.firstName}`,
      });
    } else {
      const userLanguage = await this.getCurrentUserLanguage();
      this.setState({ 
        dictDest: this.getAlpha3LanguageCode(userLanguage),
        targetName: `${upcoming.session.userEntity.firstName} ${upcoming.session.userEntity.lastName}` ,
        targetFirstName: `${upcoming.session.userEntity.firstName}`, 
        myName:`${upcoming.session.learner.firstName}`,
      });
    }

  }
  _setCounting(){
    timer.setInterval(this, 'durationCounting', () => {  
      const diff = moment(new Date()).diff(self.state.startTime);  
      self.setState({ duration: moment(diff).format('mm:ss'), }) 
      const { durationInSeconds } = this.state; 
      const duration = moment.utc(durationInSeconds*1000).format('mm:ss'); 
      self.setState({ 
        duration: duration,  
        durationInSeconds: this.state.durationInSeconds + 1, 
      });
    }, 1000);  
  }

  componentDidMount() {
    this.socket = this.props.navigation.state.params.socket;
    socket = this.socket;
    const room = this.state.roomID;
    
    self._onStartCall(this.socket);
    
    //dem gio
    if (self.state.startedVideoCall) {
      this.socket.emit(`STARTED_${room}`, true);
    }
    //bat may
    this.socket.on('exchange', (data) => {
      exchange(data, pcPeers, createPeerConnection, socket);
      self._setCounting();
      
      InCallManager.stopRingback();
      InCallManager.stopRingtone();

    });

    this.socket.on(`CAMERA_OFF_${room}`, data => {
      //TODO show avata de len view camera remote
      console.log('data', data);
    });

    //disconnect
    this.socket.on('leave', (socketId) => {
      leave(socketId, pcPeers);
      InCallManager.stopRingback();
      InCallManager.stopRingtone();
      InCallManager.stop({ busytone: '_DTMF_' });
      self.renderWhenOtherLeave();

    });

    this.socket.on(`START_COUNTING_${room}`, time => {
      self.setState({startTime: time});
    });

    requestPermission();
    //request camera truoc va show len screen
    getLocalStream(true, async stream => {
      localStream = stream;
      self.setState({
        selfViewSrc: stream.toURL(),
        status: 'connecting'
      });
    });

    
  }

  componentWillUnmount() {
    this._leave(this.socket.id, pcPeers);
    timer.clearInterval(this);

  }

  async _onStartCall(socket) {
    const room = this.state.roomID;
    const { isOffer, startedVideoCall } = this.state;

    await this.setState({ status: 'connecting', info: 'Connecting' });
    InCallManager.start({ media: 'video' });
    if (isOffer) {
      socket.emit(`OFFER_${room}`, room);
    }
    
    join(room, socket, createPeerConnection);
  }

  _switchVideoType() {
    const isFront = !this.state.isFront;
    // debugger
    this.setState({ isFront });
    getLocalStream(isFront, stream => {
      if (localStream) {
        for (const id in pcPeers) {
          const peer = pcPeers[id];
          peer && peer.removeStream(localStream);
        }
        localStream.release();
      }
      localStream = stream;
      self.setState({ selfViewSrc: localStream.toURL() });
      for (const id in pcPeers) {
        const peer = pcPeers[id];
        peer && peer.addStream(localStream);
      }
    });
  }

  receiveTextData(data) {
    const {upcoming} = this.props;
    const {userEntity, learner, teaching} = upcoming.session;
    let fullname = !teaching ? `${userEntity.firstName} ${userEntity.lastName}` : `${learner.firstName} ${learner.lastName}`;
    data.user = fullname;
    console.log('chat name: ', fullname);
    const textRoomData = this.state.textRoomData.slice();
    textRoomData.push(data);
    this.setState({ textRoomData, textRoomValue: '' });
  }

  async getCurrentUserLanguage() {
    const languagesAPI = new LanguagesAPI();
    const language = await languagesAPI.getSingleLanguageSpeak()
      .then((result) => {
        return result.data;
      });

    return language;
  }

  async _textRoomPress() {
    if (!this.state.textRoomValue) {
      return
    }
    const textRoomData = this.state.textRoomData.slice();

    const textRoomValue = this.state.textRoomValue;

    switch (textRoomValue.substr(0, 5)) {

      case '/dict': // dung API tu dien
        const phrase = textRoomValue.substring(5).trim().toLowerCase();

        const queryString = this.buildDictQueryString(phrase);

        let results = await DictionaryAPI.get(queryString).then(results => {
          return results.data.tuc;
        });

        results = this.modifyDictResult(results);

        textRoomData.push({ user: 'Dict', message: this.displayDictResult(phrase, results) });
        break;

      case '/from':
        const value = textRoomValue.substring(5).trim().toLowerCase();
        var message;

        if (value) {
          const language = this.getLanguageName(value);
          if (language) {
            this.setState({ dictFrom: value });
            message = 'Successfully set \'from\' language to ' + language;
          } else {
            message = 'Invalid language';
          }
        } else {
          message = '\'from\' language: ' + this.getLanguageName(this.state.dictFrom) + ' - '
            + '\'dest\' language: ' + this.getLanguageName(this.state.dictDest);
        }
        textRoomData.push({ user: 'Dict', message: message });
        break;

      case '/dest':
        const valueDest = textRoomValue.substring(5).trim().toLowerCase();
        var messageDest;

        if (valueDest) {
          const language = this.getLanguageName(valueDest);
          if (language) {
            this.setState({ dictDest: valueDest });
            messageDest = 'Successfully set \'dest\' language to ' + language;
          } else {
            messageDest = 'Invalid language';
          }
        } else {
          messageDest = '\'from\' language: ' + this.getLanguageName(this.state.dictFrom) + ' - '
            + '\'dest\' language: ' + this.getLanguageName(this.state.dictDest);
        }
        textRoomData.push({ user: 'Dict', message: messageDest });
        break;

      case '/swap':
        const dictFrom = this.state.dictFrom;
        const dictDest = this.state.dictDest;
        this.setState({ dictFrom: dictDest, dictDest: dictFrom });

        const messageSwap = 'Successfully swap languages';
        textRoomData.push({ user: 'Dict', message: messageSwap });
        // messageSwap = '\'from\' language: ' + this.getLanguageName(this.state.dictFrom) + ' - '
        //   + '\'dest\' language: ' + this.getLanguageName(this.state.dictDest);
        // textRoomData.push({ user: 'Dict', message: messageSwap });
        //TODO display something
        break;

      default: // chat
        textRoomData.push({ user: this.state.myName, message: this.state.textRoomValue });
        for (const key in pcPeers) {
          const pc = pcPeers[key];
          pc && pc.textDataChannel && pc.textDataChannel.send(this.state.textRoomValue);
        }
    } // end switch case

    this.setState({ textRoomData, textRoomValue: '' });
  }
  _renderTextRoom() {
    if (this.state.showTextRoom) {
      return (
        <View style={{ position: 'absolute', bottom: 58, maxHeight: 200, backgroundColor: 'transparent', zIndex: 1 }}>
          <Text style={{ textAlign: 'center', fontSize: 20, color:'blue' }}>{this.state.duration}</Text> 
          <ScrollView
            onContentSizeChange={(contentWidth, contentHeight) => {
              const _scrollToBottomY = contentHeight;
              this._ScrollView.scrollToEnd({ animated: true });
            }}
            ref={s => this._ScrollView = s}
            style={{ marginBottom: 20 }}
          >

            <ListView
              dataSource={this.ds.cloneWithRows(this.state.textRoomData)}
              enableEmptySections={true}
              renderRow={rowData =>
                <View style={{ flex: 1 }}>
                  <Text style={{ marginLeft: 10, alignSelf: 'flex-start', marginTop: 5, padding: 5, color: 'white', borderRadius: 20, backgroundColor: 'rgba(0,0,0,0.5)' }}>{`${rowData.user}: ${rowData.message}`}
                  </Text>
                </View>}
            />
          </ScrollView>

          <View style={{ flexDirection: 'row', alignItems: 'center', padding: 5, backgroundColor: 'white' }}>

            <TextInput
              returnKeyType='done'
              underlineColorAndroid='transparent'
              style={{ width: '90%', height: 50, color: 'grey', borderColor: 'grey', borderWidth: 1, borderRadius: 20 }}
              onChangeText={value => this.setState({ textRoomValue: value })}
              value={this.state.textRoomValue}
            />
            <TouchableOpacity
              style={{ width: '10%' }}
              onPress={() => this._textRoomPress()}>
              <Icon
                name='send'
                color={Colors.darkGreen}
                size={35}
              />
            </TouchableOpacity>
          </View>
        </View>
      );
    } else {
      return <View></View>;
    }
  }

  _renderHelper() {
    if (this.state.showTextRoom && this.state.showHelper) {
      return (
        <View style={{ width: '90%', padding: 5, position: 'absolute', bottom: 112, zIndex: 20 }}>
          <View style={{ borderColor: 'grey', borderWidth: 1, borderRadius: 10, paddingHorizontal: 5, backgroundColor: 'white' }}>
            <TouchableOpacity style={{ marginBottom: 10, marginTop: 10, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}
              onPress={() => {
                this.setState({
                  showHelper: false,
                  textRoomValue: '/dict '
                })
              }}>
              <Text style={{ fontSize: 20 }}>/dict</Text>
              <Text style={{ fontStyle: 'italic' }}>Translate</Text>
            </TouchableOpacity>
            <TouchableOpacity style={{ marginBottom: 10, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}
              onPress={() => {
                this.setState({
                  showHelper: false,
                  textRoomValue: '/from '
                })
              }}>
              <Text style={{ fontSize: 20 }}>/from</Text>
              <Text style={{ fontStyle: 'italic' }}>From which language</Text>
            </TouchableOpacity>
            <TouchableOpacity style={{ marginBottom: 10, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}
              onPress={() => {
                this.setState({
                  showHelper: false,
                  textRoomValue: '/dest '
                })
              }}>
              <Text style={{ fontSize: 20 }}>/dest</Text>
              <Text style={{ fontStyle: 'italic' }}>Destination language to translate</Text>
            </TouchableOpacity>
            <TouchableOpacity style={{ marginBottom: 10, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}
              onPress={() => {
                this.setState({
                  showHelper: false,
                  textRoomValue: '/swap'
                })
              }}>
              <Text style={{ fontSize: 20 }}>/swap</Text>
              <Text style={{ fontStyle: 'italic' }}>Swap languages</Text>
            </TouchableOpacity>

          </View>
        </View>
      )
    } else {
      return <View></View>;
    }
  }

  _leave = (socketId, pcPeers) => {
    leave(socketId, pcPeers);

    InCallManager.stopRingback();
    InCallManager.stopRingtone();

    clearInterval(this.countDuration);

    localStream.release();
    this.socket.disconnect();
    this.setState({ status: 'Leave' });
  }

  getAlpha3LanguageCode(languageName) {
    const index = languagesAlpha3.findIndex(language => {
      return language.name === languageName;
    });
    return index >= 0 ? languagesAlpha3[index].alpha3 : '';
  }

  getLanguageName(alpha3) {
    const index = languagesAlpha3.findIndex(language => {
      return language.alpha3 === alpha3;
    });
    return index >= 0 ? languagesAlpha3[index].name : '';
  }

  buildDictQueryString(phrase) {
    return '/gapi/translate?format=json'
      + '&from=' + this.state.dictFrom
      + '&dest=' + this.state.dictDest
      + '&phrase=' + phrase;
  }

  modifyDictResult(results) {
    let returnResult = results.filter((item) => {
      if (item.phrase) {
        return item;
      }
    });

    if (returnResult.length > 5) {
      returnResult = returnResult.slice(0, 5);
    }
    return returnResult;
  }

  displayDictResult(phrase, results) {
    let displayResult = phrase + ' - ';
    for (var index in results) {
      displayResult += results[index].phrase.text;
      if (!(index == results.length - 1)) {
        displayResult += ', ';
      }
    }
    return displayResult;
  }

  async renderWhenOtherLeave(){
    const {user, upcoming} = this.props;
    let fullname = upcoming.session.learner && upcoming.session.learner.firstName || upcoming.session.userEntity && upcoming.session.userEntity.firstName;
    
    await Alert.alert(
      `${fullname} call has ended`,
      `Duration call: ${this.state.duration}`,
      [
        { text: 'Back to Home', onPress: async () => await this.resetScreenToMainScreen() }
      ],
      {
        cancelable: false
      }
    )
  }
  resetScreenToMainScreen(){
    const resetAction = NavigationActions.reset({
      index: 0,
      actions: [
        NavigationActions.navigate({ routeName: 'Main'})
      ]
    })
    this.props.navigation.dispatch(resetAction)
    
  }
  async onPressCallEnd(){
    const session = this.props.upcoming.session.sessionEntity;
    await sessionAPI.setFinishedSession(session.id);

    this._leave(this.socket.id, pcPeers)
    await this.resetScreenToMainScreen();

  }
  async confirmEndSessionCall(){
      await Alert.alert(
      I18n.t('confirmEndCall'),
      '',
      [
        { text: I18n.t('yes'), onPress: async () => await this.onPressCallEnd() },
        { text: I18n.t('no'), onPress: () => false }
      ],
      {
        cancelable: false
      }
    )
  }
  async _onPressOffCamera() {
    const {user} = this.props;
    const room = this.state.roomID;

    this.socket.emit(`ACTION:CAMERA_OFF_${room}`, true);
    //TODO show avatar de len view camera remote
  }
  render() {
    return (
      <View style={styles.container}>

        <View style={{ flex: 1, backgroundColor: 'white', padding: 0 }}>
          <View style={{ position: 'absolute', top: 15, width: '100%' }}>
            <Text style={{ textAlign: 'center', fontSize: 20 }}>{this.state.targetName}</Text>
            {/* <Text style={{ textAlign: 'center', fontSize: 20 }}>{this.state.duration}</Text> */}
          </View>

          {
            _.isEmpty(this.state.remoteList)
              ? <RTCView streamURL={this.state.selfViewSrc} style={styles.selfView} />
              : mapHash(this.state.remoteList, function (remote, index) {
                return <RTCView key={index} streamURL={remote} style={styles.selfView} />
              })
          }

          {
            this._renderHelper()
          }

          {
            this._renderTextRoom()

          }


          <View style={{ flex: 1, flexDirection: 'row', position: 'absolute', paddingVertical: 10, bottom: 0, justifyContent: 'space-around', backgroundColor: 'white' }}>

            <TouchableOpacity
              style={styles.icon}
              onPress={() => {
                this.confirmEndSessionCall()
              }}>
              <View style={{ borderColor: '#E57373', borderRadius: 100, borderWidth: 1, backgroundColor: 'white' }}>
                <Icon
                  name='call-end'
                  color="#E57373"
                  size={35}
                />
              </View>
            </TouchableOpacity>
            <TouchableOpacity style={styles.icon}
              onPress={() => {
                this._onPressOffCamera()
              }}>
              <View style={styles.iconView}>
                <Icon
                  name='videocam-off'
                  color={Colors.darkGreen}
                  size={35}
                />
              </View>
            </TouchableOpacity>

            <TouchableOpacity style={styles.icon} onPress={() => {
              this.setState({
                showTextRoom: !this.state.showTextRoom
              })
            }}>
              <View style={styles.iconView}>
                <Icon
                  name='message'
                  color={Colors.darkGreen}
                  size={35}
                />
              </View>
            </TouchableOpacity>

            <TouchableOpacity style={styles.icon} onPress={() => {
              this.setState({
                showHelper: !this.state.showHelper
              })
            }}>
              <View style={styles.iconView}>
                <Icon
                  name='translate'
                  color={Colors.darkGreen}
                  size={35}
                />
              </View>
            </TouchableOpacity>



          </View>
        </View>

        <View style={{ height: 200, width: 100, position: 'absolute', top: 60, left: 10 }}>
          {
            _.isEmpty(this.state.remoteList)
              ? <View></View>
              : <RTCView streamURL={this.state.selfViewSrc} style={styles.remoteView} />
          }

        </View>

      </View>
    );
  }
}


export default VideoCallScreen;
const styles = {
  selfView: {
    // backgroundColor:'red'
    // flex: 1,
    // padding: 0,
    width: width,
    height: height,
  },
  remoteView: {
    flex: 1 / 2,
    // width: 470,
    // height: 350,
  },
  container: {
    flex: 1,
    // justifyContent: 'center',
    // backgroundColor: 'red',
  },
  icon: {
    flex: 1,
    paddingLeft: 10,
    paddingRight: 10,
  },
  iconView: {
    borderColor: Colors.darkGreen,
    borderWidth: 1,
    borderRadius: 100,
    backgroundColor: 'white'
  }
}