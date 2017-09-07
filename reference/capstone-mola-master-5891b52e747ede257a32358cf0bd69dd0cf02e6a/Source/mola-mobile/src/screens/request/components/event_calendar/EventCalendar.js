// @flow
import {
  View,
  Dimensions,
  StyleSheet,
  ScrollView,
  VirtualizedList,
  FlatList,
  Text,
  TouchableOpacity,
  Modal,
  TimePickerAndroid,
  ActivityIndicator,
  TextInput,
  ToastAndroid,
} from 'react-native';
import moment from 'moment';
import PayPal from 'react-native-paypal';
import PayPalConfig from '../../../../services/paypal/configuration';
import { CalculatedEventDimens, StartEndEvent } from './Packer';
import { DayViewStyle } from './style';
import React from 'react';
import Color from '../../../../../constants/Colors';
import DateTimePicker from 'react-native-modal-datetime-picker';
import DatePicker from 'react-native-datepicker';
import SessionAPI from '../../../../apis/session';
import Colors from '../../../../../constants/Colors';
import I18n from '../../../../../constants/locales/i18n';
import { currencies } from '../../../../../constants/currencies';
import UserSetting from '../../../UserSetting';
import ExchangeRates from '../../../ExchangeRates';
import accounting from '../../../../services/accounting';


import styleConstructor, { type DayViewStyleProps } from './style';

import Packer from './Packer';
import DayView from './DayView';

const VIRTUAL_ITEM_COUNT = 1000;
const dayOfWeek = ['MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN'];

type Props = {
  getItem: (data: any, index: number) => StartEndEvent[],
  events: any,
  eventTapped: (event: StartEndEvent) => void,
  width: number,
  styles?: DayViewStyleProps,
  verticleScrollViewProps?: Object,
  virtualizedListProps?: Object,
};

export default class EventCalendar extends React.Component<void, Props, void> {
  styles: DayViewStyle;
  constructor(props: Props) {
    super(props);
    this.styles = styleConstructor(props.styles);
    this.state = {
      startTimeText: '',
      endTimeText: '',
      date: new Date(),
      isDateTimePickerVisible: false,
      curMonth: moment(new Date()).format('MMMM'),
      events: [],
      loading: true,
      teacherId: this.props.teacherId,
      lesson: this.props.lesson,
      modalVisible: false,
      selectedTime: '',
      message: '',
      currTimeSlotId: 0,
    };
    this.onDateChange = this.onDateChange.bind(this);
    this.setModalVisible = this.setModalVisible.bind(this);
    this._eventTapped = this._eventTapped.bind(this);
    this._onScrollEnd = this._onScrollEnd.bind(this);
  }

  setModalVisible(visible) {
    this.setState({ modalVisible: visible });
  }

  onDateChange(date) {
    this.setState({
      selectedStartDate: date,
    });
  }
  async onPickStartTime() {
    const {currTimeSlotId, freeTimeSlotPicked} = this.state;
    const timeSlotHour = moment(freeTimeSlotPicked.starTime).get('hour');
    const timeSlotMin = moment(freeTimeSlotPicked.starTime).get('minute');
    const { action, minute, hour} = await TimePickerAndroid.open({hour: timeSlotHour, minute: timeSlotMin});
    
    console.log('state', this.state);
    const {duration} = this.props.lesson;
    const timePicked = moment({ hour, minute });
    const timeSlotEnd = timePicked.add(duration, 'minute');
    const hourEnd = timeSlotEnd.get('hour');
    const minuteEnd = timeSlotEnd.get('minute');
    if (action === TimePickerAndroid.timeSetAction) {
      this.setState({
        startTimeText: hour + ':' + (minute < 10 ? '0' : '') + minute,
        endTimeText: (hourEnd < 10 ? '0' + hourEnd : hourEnd )+ ':' + (minuteEnd < 10 ? '0' + minuteEnd : minuteEnd) ,
        startHour: hour,
        startMin: minute,
      });
    }
  }

  _parseTimeSlotToEvent(data) {
    let events = [];
    data.forEach((item) => {
      let eventInDate = {
        date: item.date,
        listTimeSlot: []
      };
      if (item.listTimeSlot) {
        item.listTimeSlot.forEach((slot) => {
          const id = slot.id;
          const start = this._parseTimeToNum(slot.starTime);
          const end = this._parseTimeToNum(slot.endTime);
          const { starTime, endTime } = slot;

          eventInDate.listTimeSlot.push({ id, start, end, starTime, endTime });
        })
      }
      events.push(eventInDate);
    })
    return events;
  }

  _parseTimeToNum(time) {
    const hour = parseInt(moment(time).format('HH'));
    const minute = parseInt(moment(time).format('mm'));
    return (hour + minute / 60) / 24 * 1024;
  }

  _eventTapped(event) {
    // this.setModalVisible(!this.state.modalVisible);
    // alert(event.id)
    const freeTimeSlotPicked = {
      starTime: event.starTime,
      endTime: event.endTime,
    }
    
    this.setState({
      currTimeSlotId: event.id,
      modalVisible: true,
      freeTimeSlotPicked
    })
  }

  _getEventsForIndex = (data: any, index: number) => {
    return this.state.events[Math.abs(index % this.state.events.length)];
  };

  async loadTimeSlot(teacherID, selectDate) {
    const sessionAPI = new SessionAPI();
    const respond = await sessionAPI.getFreeTimeSlot(teacherID, selectDate);
    const events = this._parseTimeSlotToEvent(respond.data);
    this.setState({ events })
    this.setState({ loading: false });
  }
  makePayment() {
    const { lesson, price } = this.props;
    const priceToPay = (ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * lesson.duration * price).toFixed(2);
    const currency = UserSetting.CURRENCY;
    PayPal.paymentRequest({
      clientId: PayPalConfig.clientId,
      environment: PayPal.SANDBOX,
      price: (lesson.duration * price).toFixed(2),
      currency: PayPalConfig.currency,
      description: 'Payment for session with lesson ' + lesson.title,
    }).then(async (confirm, payment) => {
      const starTimeMili = moment(this.state.date).startOf('day').add(this.state.startHour, 'hour').add(this.state.startMin, 'minute');
      const sessionAPI = new SessionAPI();
      const requestRespond = await sessionAPI.requestSession(this.state.currTimeSlotId, this.props.lesson.id, starTimeMili.valueOf());
      if (requestRespond.status === "ok") {
        ToastAndroid.show(I18n.t('sendSessionRequestSuccessfully'), ToastAndroid.SHORT);
        this.setModalVisible(!this.state.modalVisible);
        this.props.navigation.goBack();
      } else {
        if (requestRespond.message) {
          alert(requestRespond.message);

        } else {
          const startTime = moment(requestRespond.data.startTime).format('DD-MM-YYYY HH:mm');
          const endTime = moment(requestRespond.data.endTime).format('DD-MM-YYYY HH:mm');
          const message = `${I18n.t('conflictMessage')} ${requestRespond.data.lessonTitle} ${I18n.t('with')} ${requestRespond.data.teacherName} ${I18n.t('from')} ${starTime} ${I18n.t('to')} ${endTime}`;
          alert(message);
        }

      }

      
    })
      .catch((error_code) => {
        // if (error_code === PayPal.USER_CANCELLED) {
        // ToastAndroid.show(I18n.t('cancelSessionRequestSuccessfully'), ToastAndroid.SHORT);
        // }
        console.log(error_code);
        return false;
      });
  }
  async sendSessionRequest() {
    const starTimeMili = moment(this.state.date).startOf('day').add(this.state.startHour, 'hour').add(this.state.startMin, 'minute');
    const sessionAPI = new SessionAPI();
    const checkRequest = 
    await sessionAPI.checkRequestTimeBeforeSetSession(this.state.currTimeSlotId, this.props.lesson.id, starTimeMili.valueOf());
    //todo xem lai checkRequest response
    if (checkRequest.status === 'ok') {
      this.makePayment();
    } else {
      if (checkRequest.message) {
        alert(checkRequest.message);

      } else {
        const starTime = moment(checkRequest.data.starTime).format('DD-MM-YYYY HH:mm');
        const endTime = moment(checkRequest.data.endTime).format('DD-MM-YYYY HH:mm');
        const message = `${I18n.t('conflictMessage')} ${checkRequest.data.lessonTitle} ${I18n.t('with')} ${checkRequest.data.teacherName} ${I18n.t('from')} ${starTime} ${I18n.t('to')} ${endTime}`;
        alert(message);
      }


    }
  }

  componentDidMount() {
    this.loadTimeSlot(this.state.teacherId, moment(this.state.date).format('DD-MM-YYYY'));
    this.setState({ loading: false });
  }

  _showDateTimePicker = () => this.setState({ isDateTimePickerVisible: true });

  _hideDateTimePicker = () => this.setState({ isDateTimePickerVisible: false });

  _handleDatePicked = async (date) => {
    // console.log('A date has been picked: ', moment(date).format('DD-MM-YYYY'));

    this.setState({ loading: true });
    let startDate = 1;
    if (moment(this.state.date).format('MM-YYYY') != moment(date).format('MM-YYYY')) {
      const teacherID = this.state.teacherId;
      const selectDate = moment(date).format('DD-MM-YYYY');
      this.setState({
        curMonth: moment(date).format('MMMM')
      })
      await this.loadTimeSlot(teacherID, selectDate);


      if (moment().format('MM-YYYY') == moment(date).format('MM-YYYY')) {
        startDate = new Date().getDate();
      }
      this._virtualizedList.scrollToIndex({ index: date.getDate() - startDate });
      this._hideDateTimePicker();
      this.setState({ date });
    } else {
      if (moment().format('MM-YYYY') == moment(date).format('MM-YYYY')) {
        startDate = new Date().getDate();
      }
      this._virtualizedList.scrollToIndex({ index: date.getDate() - startDate });
      this._hideDateTimePicker();
      this.setState({ date });
    }

  };

  _onScrollEnd(e) {
    let contentOffset = e.nativeEvent.contentOffset;
    let viewSize = e.nativeEvent.layoutMeasurement;

    // // Divide the horizontal offset by the width of the view to see which page is visible
    let pageNum = Math.floor(contentOffset.x / viewSize.width);
    // // Get date string format: DD-MM-YYYY
    const date = this.state.events[pageNum].date;
    this.setState({
      date: moment(date, 'DD-MM-YYYY').toDate()
    });
    // console.log('scrolled to page ', this);
  }

  _getItemLayout = (data: any, index: number) => {
    const { width } = this.props;
    return { length: width, offset: width * index, index };
  };

  _getItem = (data: Array<any>, index: number) => {
    const events = this._getEventsForIndex(
      this.state.events,
      // TODO: pass current date this index corresponds to
      // instead of the index itself
      index
    );
    return events;
  };

  getCurrencySymbol(currencyCode) {
    const index = currencies.findIndex(currency => {
      return currency.code === currencyCode;
    })
    return index >= 0 ? currencies[index].symbol : '';
  }
  _renderItem = ({ index, item }) => {
    const { width, lesson } = this.props;
    const { selectedStartDate, startTimeText, endTimeText } = this.state;
    const startDate = selectedStartDate ? selectedStartDate.toString() : '';

    return (
      <View style={{ backgroundColor: 'white', alignItems: 'center' }}>

        <View style={styles.dateTitle}>
          <Text style={styles.text}>{moment(item.date, 'DD-MM-YYYY').format('DD').toString()}</Text>
          <Text>{dayOfWeek[moment(item.date, 'DD-MM-YYYY').format('E') - 1]}</Text>
        </View>
        <Modal
          transparent={true}
          visible={this.state.modalVisible}
          onRequestClose={() => { alert("Modal has been closed.") }}
        >
          <View style={{
            flex: 1,
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center'
          }}>
            <View style={styles.modalStyle} >
              <View style={{ alignItems: 'center' }}>
                <View>
                  {
                    startTimeText
                      ?<View style={styles.sessionInfomation}>
                        <Text style={styles.sessionInfomationText} numberOfLines={2}>{`${I18n.t('lessonName')}: ${lesson.title}`}</Text>
                        <Text style={styles.sessionInfomationText}>{`${I18n.t('lessonDuration')}: ${lesson.duration} ${I18n.t('minDuration')}`}</Text>
                        <Text style={styles.sessionInfomationText}>{`${I18n.t('time')}: ${I18n.t('from')} ${startTimeText} ${I18n.t('to')} ${endTimeText}`}</Text>
                        <Text style={styles.sessionInfomationText}>{I18n.t('yourPrice') + ': '
                          + accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * this.props.price).toFixed(2))
                          + this.getCurrencySymbol(UserSetting.CURRENCY)
                          + I18n.t('min')}</Text>
                        {/* <Text style={styles.header}>{`${I18n.t('bookTimeSlotConfirm')} ${I18n.t('from')} ${startTimeText} ${I18n.t('to')} ${endTimeText}`}</Text> */}
                      </View> 
                      : <Text style={styles.header}>{I18n.t('selectStarttime')} </Text>
                  }
                </View>


                <TouchableOpacity style={styles.timePickerArea} onPress={() => this.onPickStartTime()}>
                  <Text style={styles.titles}>{I18n.t('setTimeSlot')}</Text>
                </TouchableOpacity>

                {/* <View style={styles.input}>
                  <Text style={styles.header}>{I18n.t('message')} </Text>
                  <TextInput
                    style={{ marginLeft: 10, width: 250, borderBottomWidth: 1, borderBottomColor: '#ebebeb' }}
                    placeholder={I18n.t('message')}
                    placeholderTextColor='lightgrey'
                    underlineColorAndroid='transparent'
                    multiline={true}
                    onChangeText={(message) => this.setState({ message })}
                  />
                </View> */}
                <View style={{ flexDirection: 'row' }}>
                  <TouchableOpacity
                    style={styles.cancelButton}
                    onPress={() => {
                      this.setModalVisible(!this.state.modalVisible)
                      this.setState({
                        startTimeText: '',
                        endTimeText: '',
                      });
                    }}>
                    <Text style={styles.addButtonText}>{I18n.t('cancel')}</Text>
                  </TouchableOpacity>
                  {
                    startTimeText.length!==0
                    ?<TouchableOpacity
                      style={styles.addButton}
                      onPress={() => {
                        this.sendSessionRequest();
                      }}>
                      <Text style={styles.addButtonText}>{I18n.t('confirm')}</Text>
                    </TouchableOpacity>
                    :null
                  }
                </View>
              </View>
            </View>
          </View>
        </Modal>
        <DayView
          eventTapped={this._eventTapped}
          events={item.listTimeSlot}
          width={width}
          styles={this.styles}
        />
      </View>
    );
  };

  render() {
    const {
          width,
      verticleScrollViewProps,
      virtualizedListProps,
      events,
    } = this.props;
    return (
      <ScrollView {...verticleScrollViewProps}>
        <View style={{ alignItems: 'center', backgroundColor: 'white', padding: 10 }}>
          <TouchableOpacity onPress={this._showDateTimePicker}>
            <Text style={styles.text}>{this.state.curMonth}</Text>
          </TouchableOpacity>
          <DateTimePicker
            isVisible={this.state.isDateTimePickerVisible}
            onConfirm={this._handleDatePicked}
            onCancel={this._hideDateTimePicker}
            date={this.state.date}
            minimumDate={new Date()}
          />
        </View>

        <VirtualizedList
          ref={c => this._virtualizedList = c}
          windowSize={2}
          initialNumToRender={2}
          initialScrollIndex={this.state.events.length / 2}
          data={this.state.events}
          getItemCount={() => this.state.events.length}
          getItem={this._getItem}
          keyExtractor={(item, index) => String(index)}
          getItemLayout={this._getItemLayout}
          horizontal
          pagingEnabled
          renderItem={this._renderItem}
          onMomentumScrollEnd={this._onScrollEnd}
          style={{ width: width }}
          {...virtualizedListProps}
        />



      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  input: {
    padding: 10,
    backgroundColor: 'white',
    width: '95%',
    margin: 20,
    marginTop: 5
  },
  timePickerArea: {
    alignItems: 'center',
    justifyContent: 'center',
    height: 80,
    width: 170,
    backgroundColor: Colors.lightGreen,
    marginBottom: 20,
    elevation: 2
  },
  titles: {
    fontSize: 25,
    color: Colors.whiteColor
  },
  timeText: {
    fontSize: 24,
    fontWeight: 'bold',
    color: Colors.whiteColor
  },
  sessionInfomation: {
    padding: 10,
  },
  sessionInfomationText: {
    marginBottom: 5,
    fontSize: 18,
  },
  header: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20
  },
  cancelButton: {
    backgroundColor: Colors.grayColor,
    width: 120,
    alignSelf: 'flex-end',
    margin: 10,
    padding: 5,
    borderRadius: 5,
  },
  addButton: {
    backgroundColor: Colors.blueTwitter,
    width: 120,
    alignSelf: 'flex-end',
    margin: 10,
    padding: 5,
    borderRadius: 5,
  },
  addButtonText: {
    fontSize: 18,
    color: '#fff',
    alignSelf: 'center'
  },
  modalStyle: {
    width: 320, height: 350, backgroundColor: 'white',
    elevation: 15,
    borderRadius: 5,
    alignItems: 'center',
    padding: 20
  },
  dateTitle: {
    borderWidth: 1,
    borderTopColor: Color.darkGreen,
    borderBottomColor: Color.darkGreen,
    backgroundColor: 'white',
    alignItems: 'center',
    width: '100%',
    marginTop: 5,
    padding: 5
  },
  text: {
    fontSize: 18,
  }
});
