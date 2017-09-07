import React, { Component } from 'react';
import { View, Text, ScrollView, TouchableOpacity, ActivityIndicator, StyleSheet, AsyncStorage,RefreshControl } from 'react-native';
import { List, ListItem, Button, Icon, } from 'react-native-elements';
import CourseAPI from '../../../apis/course';

import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';
import ListCourse from '../components/ListCourseRegisted';
import UserSetting from '../../UserSetting';

let self;
class CourseRegistedScreen extends Component {
  constructor(props) {
    console.log('te', UserSetting.isTeacher);
    super(props);
    this.state = {
      user: {},
      listCourseRegisted: [],
      loading: true,
      refreshing:false,
    }
    self = this;
  }
  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('courseRegisted'),
      headerRight:
      UserSetting.isTeacher ?
        <TouchableOpacity onPress={() => self.props.navigation.navigate('CourseScreen')}>
          <Icon name='account-box' color={Colors.whiteColor} size={30} containerStyle={{ marginRight: 30 }} />
        </TouchableOpacity>
        : <View></View>
      ,
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
    });

  async _onRefresh() {
    this.setState({refreshing: true});
    const courseAPI = new CourseAPI();
    const data = await courseAPI.getCourseRegisted()
    this.setState({
      listCourseRegisted: data.data,
      loading: false,
      refreshing:false,
    });
  }

  async componentWillMount() {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.getCourseRegisted()
    this.setState({
      listCourseRegisted: data.data,
      loading: false,
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
    const { listCourseRegisted } = this.state;
    const { navigation } = this.props;
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

        {this.state.listCourseRegisted.length > 0
          ? <List style={{ marginTop: 1, padding: 10 }}>
            {this
              .state
              .listCourseRegisted
              .map((course, i) => {

                return <ListCourse key={i} course={course} navigation={navigation} />

              })
            }
          </List>
          : <View style={css.container}>
            <Text style={css.title}>{I18n.t('noCourseRegisted')}</Text>

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
    marginTop: 50,
    margin: 10
  },
  title: {
    fontSize: 25,
    // fontWeight: 'bold'
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

export default CourseRegistedScreen;