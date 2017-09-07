import React from 'react';
import { StyleSheet, Text, View, Dimensions } from 'react-native';

import EventCalendar from './components/event_calendar/EventCalendar';
import moment from 'moment';
import SessionAPI from '../../apis/session';
import I18n from '../../../constants/locales/i18n';
import Color from '../../../constants/Colors';

let { width } = Dimensions.get('window');

class SelectTimeSlotScreen extends React.Component {

  constructor(props) {
    super(props);

  }

  state = {
    events: [],
  };

static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('selectTimeSlot'),
    headerStyle: {
      backgroundColor: Color.darkGreen
    }
  });

 

  componentDidMount() {
  }

  render() {
    return (
      <EventCalendar
        teacherId={this.props.navigation.state.params.teacherId}
        lesson = {this.props.navigation.state.params.lesson}
        price = {this.props.navigation.state.params.price}
        eventTapped={this._eventTapped}
        events={this.state.events}
        navigation={this.props.navigation}
        getItem={this._getEventsForIndex}
        width={width}
      />
    );
  }
}

export default SelectTimeSlotScreen;
