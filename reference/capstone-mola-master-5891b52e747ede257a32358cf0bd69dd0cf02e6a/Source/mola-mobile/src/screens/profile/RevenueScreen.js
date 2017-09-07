import React, { Component } from 'react';
import { View, Text, ScrollView, Dimensions, ActivityIndicator } from 'react-native';
import Chart from 'react-native-chart';
import moment from 'moment';
import {Button} from 'react-native-elements';
import Counter from './components/Counter';
import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';
import TeacherAPI from '../../apis/teacher';

import { currencies } from '../../../constants/currencies';
import UserSetting from '../UserSetting';
import ExchangeRates from '../ExchangeRates';
import accounting from '../../services/accounting';

const teacherAPI = new TeacherAPI();
const { height, width } = Dimensions.get('window');
let self;
class RevenueScreen extends Component {
  constructor(props){
    super(props);
    this.state = {
      revenueInMonth: 0,
      revenueInYear: 0,
      loading: true,
    }
    this.currentMonth = moment().get('month') + 1;
    self = this;
  }

  static navigationOptions = ({
    ...props
  }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('revenue'),
    headerStyle: {
        backgroundColor: Colors.darkGreen,
    },
    headerRight: <Button
      color={Colors.whiteColor}
      borderRadius={5}
      icon={{name: 'envira', type: 'font-awesome'}}
      buttonStyle={{backgroundColor: Colors.darkBlueColor}}
      onPress={async() => {
        self._goToPolicyScreen();
      }}/>
  });
  _goToPolicyScreen(){
    this.props.navigation.navigate('Policy');
  }

  getCurrencySymbol(currencyCode) {
    const index = currencies.findIndex(currency => {
      return currency.code === currencyCode;
    })
    return index >= 0 ? currencies[index].symbol : '';
  }

  async componentDidMount() {
    const revenue = await teacherAPI.getRevenue();
    
    if (revenue.status === 'ok') {
      this.setState({ loading: false });
      await this._setChart(revenue);
      await this._setRevenue(revenue);
    }
    console.log('revenue', revenue);

  }
  _setChart(revenue){
    const data = revenue.data.map(month => {
      return [month.month, month.revenue * ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY]];
    });
      debugger
    if (this.currentMonth > 6) {
      const previousMonths = this.currentMonth - 6 + 1;
      this.data = data.filter(month => month[0] >= previousMonths && month[0] <= this.currentMonth);
    } else {
      this.data = data.filter(month => month[0] >= this.currentMonth);
    }
    
  }
  _setRevenue(revenue){
    const rev = revenue.data;
      const revenueInMonthIndex = rev.findIndex(data => data.month === this.currentMonth);
      const revenueInMonth = rev[revenueInMonthIndex].revenue;
      
      let total = 0;
      for(let i = 0; i< rev.length; i++) {
        total = total + rev[i].revenue;
      }
      const revenueInYear = total;
      
      this.setState({revenueInMonth, revenueInYear});
  }

  
  render() {
    const { loading } = this.state;
    return (
      <View style={styles.currentMonth}>
        {
          loading
          ? <ActivityIndicator size='large'/>
          : <View style={styles.container}>
            <View style={{flexDirection: 'row',flex:1/3, alignItems:'center', justifyContent:'space-around'}}>    
            <View style={styles.revenue}>
               <Counter
                start={this.state.revenueInMonth/2}
                end={this.state.revenueInMonth}
                time={3000}
                style={styles.revenueText}/>
              <Text style={styles.revenueLabel}>{I18n.t('MonthRevenue')}</Text>
             

            </View>
            <View style={styles.revenue}>
              <Counter
                start={this.state.revenueInYear/2}
                end={this.state.revenueInYear}
                time={3000}
                style={styles.revenueText}
                onComplete={() => {
                  this.setState({revenueInMonth: 'asfasf'})
                }}/>
              <Text style={styles.revenueLabel}>{I18n.t('revenueInYear')}</Text>
              
            </View>
          </View>
          <View style={{flex:0.2/3}}>
            <Text style={{marginLeft:10, fontSize: 20}}>{UserSetting.CURRENCY + ' ' +this.getCurrencySymbol(UserSetting.CURRENCY)}</Text>
          </View>
          <ScrollView horizontal={true} style={styles.chart}>
            <Chart
              style={{width}}
              data={this.data}
              axisLabelColor={'red'}
              axisLineWidth={2}
              type="bar"
              showDataPoint={true}
              sliceColors={['#e1cd00']}
              gridColor="#ccc"
              showXAxisLabels={true}
              hideVerticalGridLines={true}
              yAxisShortLabel={true}
              color={['#0088ee']}
              cornerRadius={2}
              
            />
          </ScrollView>
          <View style={styles.content}>
          <Text style={{fontSize:20}}>{I18n.t('revenueInMonth')}</Text>
          </View>
			</View>
          
        }
      </View>
    );
  }
}

const styles = {
  container: {
		flex: 1,
		// justifyContent: 'center',
		// alignItems: 'center',
		backgroundColor: 'white',
	},
	chart: {
    flex: 2/3,
		width: 450,
  },
  currentMonth: {
    flex: 1,
    margin: 10,
  },
  revenue:{
    alignItems: 'center',

  },
  revenueLabel: {
    color: Colors.blueColor
  },
  revenueText: {
    color: Colors.darkGreen,
    fontSize: 26
  },
  content:{
    alignItems:'center',
    justifyContent:'center',
    flex:0.5/3,
  }

}
export default RevenueScreen;