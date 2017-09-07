import React, {Component} from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Image,
  AsyncStorage,
  ActivityIndicator,
  RefreshControl
} from 'react-native';

import {Icon, Button} from 'react-native-elements';
import Timeline from 'react-native-timeline-listview';
import DatePicker from 'react-native-datepicker'
import moment from 'moment';
import { connect } from 'react-redux';

import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';
import ScheduleAPI from '../../apis/schedule';
const schedule = null;

const mapStateToProps = (state) => ({ 
  user: state.auth.user.data.data.user,
});

@connect(mapStateToProps)
class ScheduleScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      date: moment().format('DD-MM-YYYY').toString(),
      schedules: [],
      isRefreshing: false,
      isWaiting: false,
    }
    this.onRefresh = this.onRefresh.bind(this);
    this.renderFooter = this.renderFooter.bind(this);
    this.renderDetail = this.renderDetail.bind(this);
    this.goToRequestDetail = this.goToRequestDetail.bind(this);
  }

  static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('schedule'),
    headerStyle: {
      backgroundColor: Colors.darkGreen
    }
  });
  async _parsingSchedule(response){ 
      let upComing = false;
      const sessionData = await response.data.sessions.map(session => {
        const momentStartTime = moment(session.timeSlot.starTime).format('HH:mm');
        const momentEndTime = moment(session.timeSlot.endTime).format('HH:mm');
        
        const time = `${momentStartTime}\n${momentEndTime}`;
        
        upComing = moment() < moment(session.timeSlot.starTime);
        session.time = time;
        session.startTimeLong = session.timeSlot.starTime
        session.endTimeLong = session.timeSlot.endTime
        session.next = upComing;
        session.type = 'session'

        if(upComing){
          upComing = !upComing;
        }
        return session;
      });
      let timeSlotsData = [];
      if(response.data.timeSlots){
        timeSlotsData = await response.data.timeSlots.map(slot => {
          const momentStartTime = moment(slot.starTime).format('HH:mm');
          const momentEndTime = moment(slot.endTime).format('HH:mm');
          const time = `${momentStartTime}\n${momentEndTime}`;
          slot.time = time;
          slot.startTimeLong = slot.starTime;
          slot.endTimeLong = slot.endTime;
          slot.type = 'timeslot';
          return slot;
        });

      }
      const schedules = [...sessionData, ...timeSlotsData].sort((a, b) => a.startTimeLong - b.startTimeLong);

      this.setState({schedules});
    
  }
  async _fetchSchedule(){
    this.setState({isWaiting: true});
    const token = await AsyncStorage.getItem('USER_TOKEN');
    schedule = new ScheduleAPI(token);

    let {date} = this.state;
    
    const from = moment(date, 'DD-MM-YYYY').format('YYYY-MM-DD 00:00:00').toString();
    const to = moment(date, 'DD-MM-YYYY').add(1, 'day').format('YYYY-MM-DD 00:00:00').toString();
    
    let response = null;
    if (this.props.user.isTeacher) {
      response = await schedule.getTeacherSchedule(from, to);
    } else {
      response = await schedule.getLearnerSchedule(from, to);
    }
    if (response != null && response.status === 'ok') {
      await this._parsingSchedule(response);
    }
    
    this.setState({isWaiting: false, isRefreshing: false});
    
  }
  async componentDidMount() {
    this._fetchSchedule();
  }
  

  renderDetail(schedule, sectionID, rowID) {
      
      const { teacher, learner } = schedule;
      if(schedule.type === 'session'){
        return (
          learner !== null && teacher === null
          ? <TouchableOpacity
              onPress={() => {
              this.goToRequestDetail(schedule)
            }}>
              <View style={schedule.session.status !== 'pending' ? css.sessionActive :css.sessonPending}>
                <Text style={[css.title]}>{schedule.lesson.title}</Text>
                <View style={css.descriptionContainer}>
                  <Image source={{ uri: learner && learner.avatar || teacher && teacher.avatar }} style={css.image}/>
                  <View>
                    <Text style={[css.textDescription]}>{schedule.lesson.description}</Text>
                    <Text style={[css.textDescription, {color: Colors.redColor, fontWeight: 'bold'}]}>{schedule.next ? '' : ''}</Text>
                  </View>
                </View>
              </View>
            </TouchableOpacity>
          : <View style={schedule.session.status !== 'pending' ? css.sessionActive :css.sessonPending}>
              <Text style={[css.title]}>{schedule.lesson.title}</Text>
              <View style={css.descriptionContainer}>
                <Image source={{ uri: learner && learner.avatar || teacher && teacher.avatar }} style={css.image}/>
                <View>
                  <Text style={[css.textDescription]}>{schedule.lesson.description}</Text>
                  <Text style={[css.textDescription, {color: Colors.redColor, fontWeight: 'bold'}]}>{schedule.next ? '' : ''}</Text>
                </View>
              </View>
            </View>
        )
      } else if (schedule.type === 'timeslot'){
        return (
          /*<TouchableOpacity
            onPress={() => {
            this.goToRequestDetail(schedule)
          }}>*/
            <View style={schedule.status !== 'booked' ? css.freeTimeSlot :css.sessionActive}>
              <Text style={[css.title]}>Free slot</Text>
              
            </View>
          // </TouchableOpacity>
        )
      }

  }
  

  componentDidUpdate(prevProps, prevState) {
    if(prevState.date !== this.state.date){
      this._fetchSchedule();
    }
  }

  goToRequestDetail(session) { 
    this.props.navigation.navigate('RequestDetail', {session}); 
  } 

  renderDatePicker(){
    return <DatePicker style={{ width: 200 }}
              date={this.state.date}
              mode="date"
              placeholder="select date"
              format="DD-MM-YYYY"
              minDate={moment().format('DD-MM-YYYY')}
//              maxDate="2017-12-31"
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
                  marginLeft: 36
                }
              }}
              onDateChange={(date) => {
              this.setState({date: date})
            }}/>
  }
  renderSetTimeslotIcon(){
    const {schedules, date} = this.state;
    return (
      <TouchableOpacity onPress={() => this.props.navigation.navigate('SetTimeSlot', {schedules, date, reload: this.onRefresh.bind(this)})}>
          <Icon
            name='reorder'
            color={Colors.darkGreen}
            size={40}
            style={{
              marginLeft: 20
            }}/>
        </TouchableOpacity>
    );
  }

  async onRefresh(){
    this.setState({isRefreshing: true});
    console.log('refreshing, pull to refresh');
    
    await this._fetchSchedule();
    await this.setState({
      isRefreshing: false
    });
    
  }
  renderFooter() {
    if (this.state.schedules.length === 0) {
        return <ActivityIndicator />;
    }
  }
  render() {
    const {schedules, date, isWaiting} = this.state;
    if(isWaiting){
      return (
        <View style={css.container}>
          <View
            style={{
            margin: 10,
            flexDirection: 'row'
            }}>
            {this.renderDatePicker()}
            { this.props.user.isTeacher ? this.renderSetTimeslotIcon()  : null }
          </View>
            {
              this.renderFooter() 
            }
        </View>
      )
    }else if(!isWaiting && schedules.length === 0){
      const {schedules, date} = this.state;
    
      return (
        <View style={css.container}>
          <View
            style={{
            margin: 10,
            flexDirection: 'row',
            justifyContent: 'center'
          }}>
            { this.renderDatePicker() }
            { this.props.user.isTeacher ? this.renderSetTimeslotIcon()  : null }
          </View>
          <ScrollView

            refreshControl={
          <RefreshControl
            refreshing={this.state.isRefreshing}
                onRefresh={this.onRefresh.bind(this)}
            tintColor={Colors.darkGreen} title="Loading..." titleColor={Colors.darkGreen} colors={
              ['#FFF', '#FFF', '#FFF']
            }
            progressBackgroundColor={Colors.darkGreen}
          />
        }
          >
          <View style={{ justifyContent: 'center', alignItems: 'center'}}>

          <Text style={{fontSize: 16}}>{I18n.t('schedule_empty')}</Text>
            <Icon size={250}
                  type="material-community"
                  name="calendar-clock"
                  color='#f0f0f0'
                  onPress={() => this.props.navigation.navigate('SetTimeSlot', {schedules, date})}/>
          </View>
          </ScrollView>
        </View>
      )
    }

    return (
      <View style={css.container}>
        <View style={{
          alignItems: 'center'
        }}>
          {/*<Text style={css.header}>{I18n.t('schedules')}</Text>*/}
          <View
            style={{
            margin: 10,
            flexDirection: 'row'
          }}>
            {this.renderDatePicker()}
            { this.props.user.isTeacher ? this.renderSetTimeslotIcon() : null }
          </View>
        </View>
        <ScrollView style={{
          padding: 20
        }}>
          <Timeline
            lineColor={Colors.darkGreen}
            circleColor={Colors.darkGreen}
            separator={false}
            descriptionStyle={{
              textAlign: 'right',
            // color: Colors.blackColor
            }}
            timeStyle={{textAlign: 'center',color:Colors.darkGreen, fontSize:18, borderRadius:8}}
            options={{
            style:{paddingTop:5},
            refreshControl: (
              <RefreshControl
                refreshing={this.state.isRefreshing}
                onRefresh={this.onRefresh.bind(this)}
              />
            ),
            renderFooter: this.renderFooter,
          }}
            innerCircle={'dot'}
            data={this.state.schedules}
            renderDetail={this.renderDetail}/>
        </ScrollView>

      </View>
    );
  }
}

var css = StyleSheet.create({
  container: {
    flex: 1,
    // alignItems: 'center',
    backgroundColor: 'white'
  },
  freeTimeSlot:{
    flex: 1,
    backgroundColor: '#94ED88',
    padding: 10
  },
  sessonPending: {
    flex: 1,
    backgroundColor: '#F5c4c0',
    padding: 10
  },
  sessionActive: {
    flex: 1,
    backgroundColor: Colors.whiteColor,
    padding: 10
  },
  requestBlock: {
    backgroundColor: '#EB7153',
    width: 150,
    borderRadius: 10,
    padding: 10,
    alignItems: 'center'
  },
  text: {
    fontSize: 15,
    marginRight: 30
  },
  header: {
    fontSize: 20,
    fontWeight: 'bold',
    margin: 20,
    alignItems: 'center'
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold'
  },
  descriptionContainer: {
    flexDirection: 'row',
    paddingRight: 50
  },
  image: {
    width: 50,
    height: 50,
    borderRadius: 25
  },
  textDescription: {
    marginLeft: 10,
    color: 'gray'
  },
  input: {
    width: '90%',
    marginBottom: 20
  },
  button: {
    width: 200,
    backgroundColor: '#4aae4f'
  }

})
export default ScheduleScreen;
