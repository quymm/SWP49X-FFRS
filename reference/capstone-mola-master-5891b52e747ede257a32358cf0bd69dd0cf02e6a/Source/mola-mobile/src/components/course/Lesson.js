import React, { Component } from 'react';
import { View, Text, StyleSheet, Image, TextInput, Alert, TouchableOpacity } from 'react-native';
import Colors from '../../../constants/Colors';
import { Icon } from 'react-native-elements';
import SessionAPI from '../../apis/session';
import I18n from '../../../constants/locales/i18n';
import Modal from 'react-native-modal';
import moment from 'moment';

import io from 'socket.io-client';
import { socketIP } from '../../apis/mola-api';

import { connect } from 'react-redux';
import { setNextUpcomingSession, setNoUpcomingSession } from '../../screens/home/actions';

let self;
@connect(null, { setNextUpcomingSession, setNoUpcomingSession })
class Lesson extends Component {
  constructor(props) {
    super(props);
    const {onTime, timeSlotEntity } = this.props.session;
    // const timeUp = moment().isBetween(moment(timeSlotEntity.starTime), moment(timeSlotEntity.endTime));
    const timeUp = onTime;
    this.state = {
      timeUp: timeUp,
      modalVisible: false,
      modalSession: '',
    };
    this.socket = null;
    self = this;
  }

  componentDidMount() {
    const { session, user } = this.props;
    
    const { starTime, endTime } = session.timeSlotEntity;
    // const timeUp = moment().isBetween(moment(starTime), moment(endTime));
    const timeUp = session.onTime;
    if (timeUp) {
      console.log('timeup',session.sessionEntity.id);
      const room = session.sessionEntity.sessionRoom;
      this.socket = io.connect(socketIP, {
        query: `sessionRoom=${room}`, transports: ['websocket']
      });
      this.socket.on('connect', data => {
        //thong bao cuoc goi
        this.socket.on(`ANSWER_${room}`, data => {
          this.props.mainNavigation.navigate('CallWaiting', {
            socket: this.socket,
            isOffer: false,
            user, session
          });
          // self.props.mainNavigation.navigate('VideoCall', {socket: self.socket});

        });
      });
    }
  }

  setModalVisible(visible) {
    this.setState({ modalVisible: visible });
  }

  _confirmCancel(name) {
    Alert.alert(
      'Do you want to cancel session ' + name,
      null,
      [
        { text: 'Cancel' }, { text: 'OK' }
      ],
      {
        cancelable: false
      }
    )
  }
  onLearnMore() {
    const { userEntity, courseEntity, timeSlotEntity, teacherEntity } = this.props.session;
    this.props.mainNavigation.navigate('CourseInfoToRegist', {
      course: courseEntity, teacher: teacherEntity, userInfo: userEntity
    });
  }

  goToVideoCallScreen() {
    this.props.mainNavigation.navigate('VideoCall', {
      socket: this.socket,
      isOffer: true,
    });
  }

  render() {
    const { userEntity, courseEntity, timeSlotEntity, learner, sessionEntity } = this.props.session;
    const startTime = moment(timeSlotEntity.starTime).format('HH:mm');
    const endTime = moment(timeSlotEntity.endTime).format('HH:mm');
    let avatar, firstName, lastName;
    if (this.props.session.teaching) {
      avatar = learner.avatar;
      firstName = learner.firstName;
      lastName = learner.lastName;
    } else {
      avatar = userEntity.avatar;
      firstName = userEntity.firstName;
      lastName = userEntity.lastName;
    }
    return (
      <View>
        <View style={[css.lessonContainer, this.props.style]}>
          {
            this.state.timeUp
              ? <View style={{ flexDirection: 'row', width: '100%', backgroundColor: '#E8F5E9' }}>
                <TouchableOpacity style={{ flexDirection: 'row', flex: 3 }}
                  onPress={() => {
                    this.props.session.teaching
                      ? this.props.mainNavigation.navigate('UserProfile', { session: this.props.session })
                      : this.onLearnMore()

                  }}>
                  <View style={{ alignItems: 'center', margin: 5, flex: 1 }}>
                    <Image
                      style={css.image}
                      source={{ uri: avatar }} />
                    <Text style={{ fontSize: 15, textAlign: 'center' }} numberOfLines={2}>{firstName} {lastName}</Text>
                  </View>
                  <View style={{ margin: 5, padding: 10, flex: 2 }}>
                    <Text style={{ fontWeight: 'bold', fontSize: 18 }}>{this.props.session.teaching ? 'Teaching with' : 'Learning with'}</Text>
                    <Text style={css.title} numberOfLines={2}>{courseEntity.title}</Text>
                    <Text>{}</Text>
                  </View>
                </TouchableOpacity>
                <TouchableOpacity style={{ padding: 10, flex: 1, justifyContent: 'center', alignItems: 'center' }}
                  onPress={this.goToVideoCallScreen.bind(this)}>

                  <Icon
                    name='phone'
                    color={Colors.darkGreen}
                    size={35}

                  />
                </TouchableOpacity>
              </View>

              :
              <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
                <TouchableOpacity style={{ flex: 5 / 9, paddingLeft: 10 }} onPress={() => {
                  this.props.session.teaching
                    ? this.props.mainNavigation.navigate('UserProfile', { session: this.props.session })
                    : this.onLearnMore()
                }}>
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <View style={css.imageView}>
                      <Image
                        style={css.image}
                        source={{ uri: avatar }} />
                      <Text style={{ fontSize: 15, textAlign: 'center' }} numberOfLines={2}>{firstName}</Text>
                    </View>
                    <View style={css.contentView}>
                      <Text style={{ fontWeight: 'bold', fontSize: 18 }}>{this.props.session.teaching ? 'Teaching with' : 'Learning with'}</Text>
                      <Text style={css.title} numberOfLines={2}>{courseEntity.title}</Text>
                      <View style={{ flexDirection: 'row' }}>
                        <Icon
                          name='calendar'
                          type='evilicon'
                          color={Colors.grayColor}
                        />
                        <Text> {'' + moment(timeSlotEntity.starTime).format('DD-MM-YYYY')}</Text>
                      </View>
                      <View style={{ flexDirection: 'row' }}>
                        <Icon
                          name='clock'
                          type='evilicon'
                          color={Colors.grayColor}
                        />
                        <Text> {startTime} - {endTime}</Text>
                      </View>
                    </View>
                  </View>
                </TouchableOpacity>
                <View style={{ flex: 4 / 9, justifyContent: 'center', alignItems: 'center' }}>
                  {
                    this.props.session.teaching
                      ? <View></View>
                      : <TouchableOpacity style={css.button1}>
                        <Text style={{ color: 'white', fontSize: 18, textAlign: 'center' }}>{I18n.t('reschedule')}</Text>
                      </TouchableOpacity>
                  }

                  <Modal
                    animationType={"slide"}
                    transparent={true}
                    onRequestClose={() => console.log("dcm")}
                    visible={this.state.modalVisible}>
                    <View style={{
                      flex: 1,
                      flexDirection: 'column',
                      justifyContent: 'center',
                      alignItems: 'center'
                    }}>

                      <View style={css.modalStyle} >
                        <Text style={css.headerModel}>{I18n.t('cancelSession')}</Text>
                        <Text style={{ backgroundColor: '#E57373', color: 'white', padding: 10 }}>
                          Warning! if you want to reschedule the lesson, click on the reschedule link instead
                        </Text>
                        <View style={{ width: '95%', margin: 10 }}>
                          <Text>You are about to cancel this session</Text>
                          <Text>- This session will be cancelled</Text>
                          <Text>- {firstName} will be notified </Text>
                        </View>
                        <View style={{ width: '100%', marginBottom: 10 }}>
                          <Text style={{ marginLeft: 10, marginTop: 10 }}>Reason :</Text>
                          <TextInput

                            style={{ marginLeft: 20, marginRight: 20 }}></TextInput>
                        </View>
                        <Text style={{ margin: 10 }}>
                          Are you sure you want to cancel session with {courseEntity.title} from {startTime} to {endTime} ?
                        </Text>
                        <View style={{ flexDirection: 'row' }}>
                          <TouchableOpacity
                            style={css.addButton}
                            onPress={() => {
                              this.setModalVisible(!this.state.modalVisible)
                            }}>
                            <Text style={css.addButtonText}>Close</Text>
                          </TouchableOpacity>
                          <TouchableOpacity
                            style={css.addButton}
                            onPress={() => {
                              this.setModalVisible(!this.state.modalVisible)
                            }}>
                            <Text style={css.addButtonText}>{I18n.t('cancelSession')}</Text>
                          </TouchableOpacity>

                        </View>
                      </View>
                    </View>
                  </Modal>
                  <TouchableOpacity style={css.button2} onPress={() => {

                    this.setModalVisible(!this.state.modalVisible)
                  }}>
                    <Text style={{ color: 'white', fontSize: 18, textAlign: 'center' }}>{I18n.t('cancel')}</Text>
                  </TouchableOpacity>
                </View>
              </View>
          }
        </View>

        {

          this.state.timeUp
            ? <View style={{ borderBottomWidth: 1, borderBottomColor: 'grey', height: 20, marginBottom: 30, width: 200, alignSelf: 'center' }}></View>
            : <View></View>
        }
      </View>
    );
  }
}

var css = StyleSheet.create({
  lessonContainer: {
    flexDirection: 'row',
    borderRadius: 2,
    elevation: 2,
    // paddingVertical: 5,
    flex: 1,
    marginBottom: 10
  },
  headerModel: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 10
  },
  addButton: {
    backgroundColor: Colors.darkGreen,
    width: 120,
    alignSelf: 'flex-end',
    margin: 10,
    padding: 5,
    borderRadius: 5,
  },
  addButtonText: {
    color: '#fff',
    alignSelf: 'center'
  },
  modalStyle: {
    width: 350, height: 400, backgroundColor: 'white',
    elevation: 15,
    borderRadius: 5,
    alignItems: 'center',
    paddingTop: 10
  },
  title: {
    fontSize: 18,
    marginBottom: 5,
    textAlign: 'left',
    height: 50
  },
  imageView: {
    alignItems: 'center',
    margin: 5,
    // width: 100,
    flex: 1
  },
  contentView: {
    // alignItems: 'center',
    // justifyContent:'center',
    // backgroundColor: 'red',
    margin: 5,
    padding: 10,
    // width: 140
    flex: 2
  },
  duration: {
    fontSize: 20,
    padding: 5,
    color: Colors.redColor,
    textAlign: 'center',
    flex: 1
  },
  image: {
    width: 60,
    height: 60,
    borderRadius: 100
  },
  button1: {
    width: 105,
    marginLeft: 10,
    backgroundColor: '#26A69A',
    marginBottom: 15,
    padding: 5,
    borderRadius: 5
  },
  button2: {
    marginLeft: 10,
    backgroundColor: '#E57373',
    marginBottom: 10,
    padding: 5,
    borderRadius: 5,
    width: 105,
  }
})

export default Lesson;