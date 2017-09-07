import React, { Component } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image, ScrollView } from 'react-native';
import { Icon, Button, Avatar } from 'react-native-elements';
import moment from 'moment';
import Colors from '../../../constants/Colors';

import I18n from '../../../constants/locales/i18n';

class RequestDetailScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      session: null
    }
  }

  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('requestDetail'),
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
    });
  async componentWillMount() {
    const { session } = this.props.navigation.state.params;
    if (session) {
      await this.setState({ session });
    }
  }

  renderActionButton() {

    return (
      <View style={css.buttonContainer}>
        <TouchableOpacity style={css.declineButton}>
          <Text style={css.buttonText}>{I18n.t('decline')}</Text>
        </TouchableOpacity>
        <TouchableOpacity style={css.acceptButton}>
          <Text style={css.buttonText}>{I18n.t('accept')}</Text>
        </TouchableOpacity>
      </View>
    )

  }

  render() {
    console.log('state', this.state.session)
    const { session } = this.state;
    // const startTime = moment(session.timeSlot.starTime);
    // const endTime = moment(session.timeSlot.endTime);
    return (
      <ScrollView style={css.container}>
        {this.renderActionButton()}
        <View style={css.requestInfo}>
          <View style={css.avatarContainer}>
            <TouchableOpacity style={{ alignItems: 'center' }} onPress={() => this.props.navigation.navigate('UserProfile', { session: session })}>
              <Image
                style={css.avatar}
                source={{ uri: `${session.learner.avatar}` }} />
              <Text>{session.learner.firstName} {session.learner.lastName}</Text>
            </TouchableOpacity>
          </View>
          <View style={css.requestContent}>
            {
              session.sessionReq
                ? <View>
                  <Text style={css.requestTime}> {moment(session.timeSlot.starTime).format('DD-MM-YYYY')}, {moment(session.timeSlot.starTime).format('HH:mm A')} {I18n.t('time_to')} {moment(session.timeSlot.endTime).format('HH:mm A')}</Text>
                </View>
                : <View></View>
            }
            <Text style={css.text}>{`${I18n.t('courseName')}: ${session.course.title}`}</Text>
            {
              session.sessionReq
                ?
                <Text style={css.text}>{`${I18n.t('lessonName')}: ${session.lesson.title}`}</Text>
                : <View></View>
            }

            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Text style={{ fontSize: 18 }} >{I18n.t('language')}:</Text>
              <Text
                style={
                  {
                    backgroundColor: Colors.darkGreen,
                    marginLeft: 10,
                    paddingHorizontal: 8,
                    borderRadius: 4,
                    color: 'white'
                  }
                }>{`${session.course.language}`.toUpperCase()}</Text>
            </View>
          </View>
        </View>

        <View style={css.messageContainer}>
          <Text style={{ fontWeight: 'bold', marginBottom: 5 }}>{I18n.t('message')}</Text>
          {
            session.sessionReq
            ?
            <Text style={css.messageContent}>{session.session.message}</Text>
            :<View></View>
          }
          
        </View>

        {
          session.sessionReq
            ? <View style={css.schedule}>
              <TouchableOpacity style={css.scheduleButton}>
                <Icon
                  name='calendar'
                  type='foundation'
                  color='white'
                />
                <Text style={{ fontSize: 18, color: 'white' }}> Check Schedule For {`${moment(session.timeSlot.starTime).format('DD-MM-YYYY')}`}</Text>
              </TouchableOpacity>
            </View>
            : <View></View>
        }

      </ScrollView>

    );
  }
}

const css = StyleSheet.create({
  container: {
    backgroundColor: 'white',
    flex: 1,
  },
  buttonContainer: {
    flexDirection: 'row',
    height: 60
  },
  declineButton: {
    backgroundColor: '#E57373',
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  acceptButton: {
    backgroundColor: '#00E676',
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  buttonText: {
    color: 'white',
    fontSize: 16
  },
  requestInfo: {
    borderBottomWidth: 1,
    flexDirection: 'row',
    padding: 20
  },
  avatarContainer: {
    marginRight: 20,
    width: 100
  },
  avatar: {
    width: 70, height: 70, borderRadius: 70
  },
  requestContent: {
    flex: 2,
    justifyContent: 'center'
  },
  requestTime: {
    fontWeight: 'bold',
    fontSize: 19,
    marginBottom: 5
  },
  messageContainer: {
    borderBottomWidth: 1,
    paddingVertical: 10,
    paddingHorizontal: 20
  },
  messageTitle: {
    fontSize: 16,
    fontWeight: 'bold'
  },
  messageContent: {
    fontSize: 18
  },
  schedule: {
    marginTop: 10,
    paddingVertical: 10,
    alignItems: 'center',
  },
  scheduleButton: {
    backgroundColor: Colors.darkGreen,
    flexDirection: 'row',
    paddingHorizontal: 8,
    paddingVertical: 8,
    borderRadius: 4
  },
  text: {
    marginBottom: 5,
    fontSize: 18
  },
})

export default RequestDetailScreen;