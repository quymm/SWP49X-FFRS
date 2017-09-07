import React, { Component } from 'react';
import { View, Text,Alert, ScrollView, StyleSheet, TimePickerAndroid, TouchableOpacity, Switch, Touchable } from 'react-native';
import { Button, Card, ListItem } from 'react-native-elements';
import Calendar from 'react-native-calendar-select';
import moment from 'moment';
import DropdownAlert from 'react-native-dropdownalert'

import SetTimeSlotConstraint from '../../apis/constraints/set-time-slot';
import Colors from '../../../constants/Colors';
import DatePicker from 'react-native-datepicker';
import I18n from '../../../constants/locales/i18n';
import ScheduleAPI from '../../apis/schedule';
const setTimeSlotConstraint = new SetTimeSlotConstraint();

const schedule = new ScheduleAPI();
const MAIN_CUSTOM_COLOR = '#6441A4'

class SetTimeSlotScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      switchState: false,
      startTimeText: moment().format('HH:mm').toString(),
      endTimeText: moment().add(15, 'minutes').format('HH:mm').toString(),

      repeatStartDate: moment().toDate(),
      repeatEndDate: moment().toDate()
    }
    this.confirmDate = this.confirmDate.bind(this);
    this.openCalendar = this.openCalendar.bind(this);
    this.renderDatePicker = this.renderDatePicker.bind(this);
  }
  static navigationOptions = ({ ...props }) => ({
    header: null,
    // headerTintColor: '#ffffff',
    // headerTitle: I18n.t('setTimeSlot'),
    // headerStyle: {
    //   backgroundColor: Colors.darkGreen,
    // },
  });
  async onPickStartTime() {
    const { action, minute, hour } = await TimePickerAndroid.open();

    if (action === TimePickerAndroid.timeSetAction) {
      this.setState({ startTimeText: hour + ':' + (minute < 10 ? '0' : '') + minute })
    }
  }

  async onPickEndTime() {
    const { action, minute, hour } = await TimePickerAndroid.open();

    if (action === TimePickerAndroid.timeSetAction) {
      this.setState({ endTimeText: hour + ':' + (minute < 10 ? '0' : '') + minute })
    }
  }

  renderDatePicker() {
    const mindate = this.props.navigation.state.params.date;
    const { repeatStartDate, repeatEndDate } = this.state;
    return (
      <DatePicker style={{ width: 200 }}
        date={mindate}
        mode="date"
        placeholder="select date"
        format="DD-MM-YYYY"
        minDate={moment(mindate, 'DD-MM-YYYY').format('DD-MM-YYYY')}
        maxDate="2017-12-31"
        confirmBtnText="Confirm"
        cancelBtnText="Cancel"
        customStyles={{
          dateIcon: {
            position: 'absolute',
            left: 0,
            top: 4,
            marginLeft: 0
          },
          dateInput: {
            marginLeft: 36,borderColor:Colors.darkGreen,
          }
        }}
        onDateChange={(date) => {
          this.setState({ repeatStartDate: date });
        }} />
    );
  }
_confirmDelete() {
    Alert.alert(
      'Do you want to set this time slot',
      null,
      [
        { text: 'Cancel' }, { text: 'OK', onPress: () => this.onSetTime() }
      ],
      {
        cancelable: false
      }
    )
  }

  async onSetTime() {

    

    const { startTimeText, endTimeText, repeatStartDate, repeatEndDate } = this.state;

    const { schedules } = this.props.navigation.state.params;
    let from, to;
    if (this.state.switchState) {
      from = moment(`${moment(repeatStartDate).format('DD-MM-YYYY')} ${startTimeText}`, 'DD-MM-YYYY HH:mm').format('YYYY-MM-DD HH:mm:ss').toString();
      to = moment(`${moment(repeatEndDate).format('DD-MM-YYYY')} ${endTimeText}`, 'DD-MM-YYYY HH:mm').format('YYYY-MM-DD HH:mm:ss').toString();

    } else {
      const startTime = this.props.navigation.state.params.date;
      from = moment(`${moment(startTime, 'DD-MM-YYYY').format('DD-MM-YYYY')} ${startTimeText}`, 'DD-MM-YYYY HH:mm').format('YYYY-MM-DD HH:mm:ss').toString();
      to = moment(`${moment(startTime, 'DD-MM-YYYY').format('DD-MM-YYYY')} ${endTimeText}`, 'DD-MM-YYYY HH:mm').format('YYYY-MM-DD HH:mm:ss').toString();
    }
    console.log('from', from, 'to', to)
    if (moment(from, 'YYYY-MM-DD HH:mm:ss').isBefore(moment(to, 'YYYY-MM-DD HH:mm:ss'))) {
      const response = await schedule.setFreeTimeSlot({ startTime: from, endTime: to });
      this.showAlert(response);
      if(response.status == 'ok'){
        this.props.navigation.state.params.reload();
        this.props.navigation.goBack();
      }
    } else {
      this.dropdown.alertWithType('info', 'Conflict', 'Please select right time');
    }


  }

  onSwitchValueChange(value) {
    //Neu set switch la false thi reset time repeate start va repeate end
    if (!value) {
      this.setState({
        repeatStartDate: moment().toDate(),
        repeatEndDate: moment().toDate(),
      });
    }

    this.setState({ switchState: value });
  }
  render() {
    let customI18n = {
      'w': ['', 'Mon', 'Tues', 'Wed', 'Thur', 'Fri', 'Sat', 'Sun'],
      'weekday': ['', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
      'text': {
        'start': 'Start cmn date',
        'end': 'End cmn date',
        'date': 'Date',
        'save': 'Confirm',
        'clear': 'Reset'
      },
      'date': 'DD / MM'  // date format
    };
    return (

      <View style={css.container}>
        <ScrollView style={{flex:1}}>
          {/*<Card title='SET FREE TIME'>*/}
          <View style={{ alignItems: 'center', marginTop:30 }}>
            {this.renderDatePicker()}
          </View>
          <View style={css.header}>
            <TouchableOpacity style={css.timePickerArea} onPress={() => this.onPickStartTime()}>
              <Text style={css.titles}>From</Text>
              <Text style={css.timeText}>{this.state.startTimeText}</Text>
            </TouchableOpacity>

            <TouchableOpacity style={css.timePickerArea} onPress={() => this.onPickEndTime()}>
              <Text style={css.titles}>To</Text>
              <Text style={css.timeText}>{this.state.endTimeText}</Text>
            </TouchableOpacity>
          </View>
          {/*</Card>*/}

          <Card title='OPTIONS'>
            <ListItem
              title='Repeat'
              subtitle='Set repeated free time in days'
              rightIcon={
                <Switch style={{ marginLeft: 10 }}
                  onValueChange={(value) => this.onSwitchValueChange(value)}
                  value={this.state.switchState} />
              } />

            {
              this.state.switchState
                ?
                <Button
                  title="Open Calendar"
                  raised
                  backgroundColor={Colors.lightGreen}
                  icon={{ name: 'event' }}
                  onPress={() => this.openCalendar()} />
                : <Text></Text>
            }
          </Card>
          <Calendar
            i18n="en"
            ref={(calendar) => { this.calendar = calendar; }}
            customI18n={customI18n}
            color={{ subColor: '#f0f0f0' }}
            format="YYYYMMDD"
            minDate={moment().format('YYYYMMDD')}
            maxDate={moment().endOf('year').format('YYYYMMDD')}
            startDate={this.state.repeatStartDate}
            endDate={this.state.repeatEndDate}
            onConfirm={({ startDate, endDate, startMoment, endMoment }) => {
              this.confirmDate({ startDate, endDate, startMoment, endMoment })
            }}
          />
          <View style={{flexDirection:'row'}}>
          <Button
            disabled={this.state.startTimeText.length === 0 || this.state.endTimeText.length === 0}
            buttonStyle={css.setButton}
            textStyle={{ fontSize: 15 }}
            onPress={() => { this._confirmDelete() }}
            title="Set" />
          <Button
          buttonStyle={css.setButton}
            textStyle={{ fontSize: 15 }}
            onPress={() => { this.props.navigation.goBack() }}
            title="Back" />
            </View>

          <DropdownAlert
            ref={(ref) => this.dropdown = ref}
            containerStyle={{
              backgroundColor: MAIN_CUSTOM_COLOR,
            }}
            imageSrc={'https://facebook.github.io/react/img/logo_og.png'} />
        </ScrollView>
      </View>

    );
  }
  showAlert(timeslot) {

    console.log(timeslot);
    if (!timeslot) {
      return;
    }
    const title = `${timeslot.status}`.toUpperCase();
    let status = 'success';
    let message = '';

    if (timeslot.status === 'ok') {
      status = 'success';
      message = I18n.t('setTimeSlot_success');
    } else if (timeslot.status === 'warn') {
      status = 'warn';
      const { data } = timeslot;
      const violated = data.violated;
      if (violated) {
        const { slotType, timeSlot, violatedName } = violated;
        const slotFrom = moment(`${moment(this.state.repeatStartDate).format('DD-MM-YYYY')} ${this.state.startTimeText}`, 'DD-MM-YYYY HH:mm').format('MMM DD, MM, HH:mm').toString();
        const slotTo = moment(`${moment(this.state.repeatEndDate).format('DD-MM-YYYY')} ${this.state.endTimeText}`, 'DD-MM-YYYY HH:mm').format('MMM DD, MM, HH:mm').toString();
        // if(slotType === 'session' && violatedName === 'violated_time_constraints'){
        // You has a session at timSlot.starTime, please set duration at least 15 mins
        message = `Slot from ${slotFrom} to ${slotTo} conflict with a session at ${moment(timeSlot.starTime).format('MMM DD-MM-YYYY HH:mm').toString()}, please set duration at least 15 mins`;
        // }

        // if(violatedName === 'violated_time_constraints'){

        // }
      }

    } else {
      this.dropdown.onClose()
    }

    this.dropdown.alertWithType(status, title, message);

  }
  componentWillUnmount() {
    this.dropdown.onClose()
  }

  // dismissAlert = () => {
  //   this.dropdown.onClose();
  // }

  confirmDate({ startDate, endDate, startMoment, endMoment }) {
    console.log('startDate, endDate, startMoment, endMoment', startDate, endDate, startMoment, endMoment)
    console.log('repeate startDate, endDate', startDate, endDate)
    this.setState({
      repeatStartDate: startDate,
      repeatEndDate: endDate
    });
  }
  openCalendar() {
    this.calendar && this.calendar.open();
  }
}

var css = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: Colors.whiteColor
  },
  header: {
    marginTop: 30,
    flexDirection: 'row',
    paddingLeft:30,
    paddingRight:30,

  },
  pageTitle: {
    fontSize: 22,
    color: Colors.blackColor,
    alignSelf: 'center',
    marginTop: 20,
    fontWeight: 'bold'
  },
  rowView: {
    flexDirection: 'row',
    flex: 1,
  },
  timePickerArea: {
    flex: 1,
    alignItems: 'center',
    marginTop: 10,
    borderColor:Colors.whiteColor,
    borderWidth:1,
    backgroundColor:Colors.lightGreen,
  },
  titles: {
    fontSize: 25,
    color: Colors.whiteColor
  },
  timeText: {
    fontSize: 24,
    marginTop: 10,
    color:Colors.whiteColor
  },
  regions: {
    marginRight: 10,
    marginTop: 30,
    marginLeft: 10,
    paddingLeft: 10
  },
  weekDayToggle: {
    marginRight: 10,
    justifyContent: 'center',
    alignItems: 'center',
    height: 50,
    width: 50,
    borderRadius: 50
  },
  toggled: {
    borderWidth: 0,
    backgroundColor: Colors.darkGreen
  },
  notToggled: {
    borderColor: Colors.lightGreen,
    borderWidth: 1,
    backgroundColor: 'transparent'
  },
  setButton: {
    marginTop: 35,
    width: 170,
    backgroundColor: Colors.darkGreen,
    borderRadius: 5
  }
})

export default SetTimeSlotScreen;