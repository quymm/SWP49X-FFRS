import React, { Component } from 'react';
import { View, Text, ListView, FlatList, StyleSheet, ActivityIndicator, ScrollView } from 'react-native';
import { Icon, List, ListItem } from 'react-native-elements';
import Colors from '../../../constants/Colors';
import { Button } from 'react-native-elements';
import ActionButton from 'react-native-action-button';
import I18n from '../../../constants/locales/i18n';
import CourseAPI from '../../apis/course';


class CourseScreen extends Component {
  constructor() {
    super()
    this.state = {
      courses: []
    }
    this.loadData = this.loadData.bind(this);
  }

  async componentDidMount() {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.getCourseByTeacher();
    console.log('data', data);
    this.setState({
      courses: data.data
    })
  }

  async loadData(){
    const courseAPI = new CourseAPI();
    const data = await courseAPI.getCourseByTeacher();
    console.log('data', data);
    this.setState({
      courses: data.data
    })
  }



  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('course'),
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
    });


  renderItem({ item, index }) {
    console.log(index);
    return (
      <ListItem
        onPress={() => this.props.navigation.navigate('CourseInfo', { courseInfo: item, loadData: this.loadData })}
        roundAvatar
        key={index}
        title={`Course: ${item.title}`}
        containerStyle={{
          borderBottomWidth: 0,
        }} />
    );
  }

  render() {
    if(!this.state.courses){
      return (
        <ActivityIndicator
          size="small"
        />
      )
    }
    return (
      <View style={css.container}>
        <Button
          buttonStyle={css.button}
          title={I18n.t('newCourse')}
          onPress={() => this.props.navigation.navigate('CreateCourse')} />
        <ScrollView>
          <List
            containerStyle={{
              borderTopWidth: 1,
              borderBottomWidth: 1,
              borderColor: 'gray'
            }}>
            <FlatList
              data={this.state.courses}
              renderItem={this.renderItem.bind(this)}
              keyExtractor={item => {item._id}}
              ListFooterComponent={this.renderFooter} />
          </List>
        </ScrollView>

      </View>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
  },
  button: {
    width: 200,
    backgroundColor: Colors.darkGreen,
    marginTop: 30,
    borderRadius: 5
  }

})
export default CourseScreen;
