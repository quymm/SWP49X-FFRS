import React, { Component } from 'react';
import { View, Text, Animated, ScrollView, TouchableOpacity, StyleSheet, ActivityIndicator, Switch, Image } from 'react-native';
import { List, ListItem, Button, Icon, } from 'react-native-elements';
import InCallManager from 'react-native-incall-manager';
import moment from 'moment'
import I18n from '../../../constants/locales/i18n';
import Colors from '../../../constants/Colors';
import FadeInView from './FadeInView';

console.ignoredYellowBox = ['Setting a timer']

class CallWaitingScreen extends Component {
  constructor(props) {
      super(props);

  }

  static navigationOptions = ({ navigation }) => {
    return { header: null }
  };

  _goToVideoCallSreen(){
    const { socket, isOffer, user } = this.props.navigation.state.params;
    const startedCall = true;
    this.props.navigation.navigate('VideoCall', {socket, isOffer, user, startedCall});
  }
  componentDidMount() {
    InCallManager.start({media: 'audio', ringback: '_BUNDLE_'}); // or _DEFAULT_ or _DTMF_

  }
  render() {
    const { session } = this.props.navigation.state.params;
    const {userEntity, courseEntity, timeSlotEntity, learner, teaching } = session;
    debugger
    return (
      <Image style={css.bgImg} blurRadius={2} source={{ uri: !teaching ? userEntity.avatar : learner.avatar}}>
        <View style={css.container}>
          <View style={{ alignItems: 'center', position: 'absolute', top: 100 }}>
            <FadeInView style={{ width: 100, height: 100, borderRadius: 100, backgroundColor: 'white', position:'absolute' }} timeOut={0}>
            </FadeInView>
            <FadeInView style={{ width: 100, height: 100, borderRadius: 100, backgroundColor: 'white', position:'absolute' }} timeOut={750}>
            </FadeInView>
            <Image
              style={css.avatar}
              source={{uri: teaching ? learner.avatar : userEntity.avatar}} />

            <View style={{ alignItems: 'center' }}>
              <Text style={css.name}>{`${ teaching ? learner.firstName : userEntity.firstName} ${teaching ? learner.lastName : userEntity.lastName}`}</Text>
              <Text style={css.info}>{courseEntity.title}</Text>
              <Text style={css.time}>{`${moment(timeSlotEntity.starTime).format('hh:mm A')} - ${moment(timeSlotEntity.endTime).format('hh:mm A')}`}</Text>
            </View>
          </View>
          <View style={{ flexDirection: 'row', justifyContent: 'space-around', alignItems: 'center', width: '100%', position: 'absolute', bottom: 100 }}>
            <TouchableOpacity
              style={{ backgroundColor: '#E57373', borderRadius: 100, padding: 10 }}>
              <Icon
                name='call-end'
                color="white"
                size={50}
              />
            </TouchableOpacity>
            <TouchableOpacity
              style={{ backgroundColor: Colors.darkGreen, borderRadius: 100, padding: 10 }}
              onPress={() => this._goToVideoCallSreen()}>
              <Icon
                name='videocam'
                color="white"
                size={50}
              />
            </TouchableOpacity>
          </View>


        </View>

      </Image>
    );
  }
}
var css = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        // justifyContent:'center',
        paddingTop: 30,
    },
    bgImg: {
        flex: 1,
        resizeMode: 'cover',
        height: null,
        width: null
    },
    avatar: {
        width: 100,
        height: 100,
        borderRadius: 100,
        marginBottom: 20,
        transform: [{ scale: 1 }]
    },
    name: {
        color: 'white',
        fontSize: 28,
        fontWeight: 'bold'
    },
    info: {
        color: 'white',
        fontSize: 25
    },
    time: {
        color: 'white',
        fontSize: 22
    }

})

export default CallWaitingScreen;