import React, { Component } from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity } from 'react-native';
import { Icon, Button } from 'react-native-elements';
import Colors from '../../../../constants/Colors';
import I18n from '../../../../constants/locales/i18n';
import ChapterList from '../components/ChapterList';
class CourseInfoScreen extends Component {

  state = {
    courseName: "Basic Spanish",
    teacherName: "Jessie Watts",
    language: "Spanish",
    intro: "Blah blah blah...\nBlah blah blah... You will see hell in this course. Think carefully before registering.",
    courseType: "Structured Course"
  }
  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('courseInfo'),
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
    });
  render() {
    return (
      <ScrollView style={css.container}>

        <View style={css.infoRegion}>
          <View style={css.avatarArea}>
            <TouchableOpacity onPress={() => this.props.navigation.navigate('TeacherProfile')}>
              <Image
                style={css.avatar}
                source={require('../../../../assets/images/profile.jpg')} />
            </TouchableOpacity>
            <View style={{ flexDirection: 'row' }}>
              <Icon
                name='star-half'
                color={Colors.lightYellow} />
              <Text>4.5 </Text>
            </View>
          </View>

          <View style={css.infoArea}>
            <Text style={css.info}>Course Name: {this.state.courseName}</Text>
            <Text style={css.info}>Teacher Name: {this.state.teacherName}</Text>
            <Text style={css.info}>Language: {this.state.language}</Text>
          </View>
        </View>

        <View style={css.regions}>
          <Text style={css.titles}>Introduction Of Course</Text>
          <Text>{this.state.intro}</Text>
        </View>

             <ChapterList style={{ padding: 20 }} navigation={this.props.navigation} showButton={{display:'none'}}/>
        <View style={{ alignItems: 'center' }}>
          <Button
            buttonStyle={css.button}
            textStyle={{ fontSize: 15 }}
            onPress={() => { this.props.onRegister() }}
            title={I18n.t('done')} />
        </View>
      </ScrollView>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.whiteColor
  },
  title: {
    marginTop: 10,
    fontSize: 20,
    fontWeight: 'bold',
    alignSelf: 'center',
  },
  infoRegion: {
    flexDirection: 'row',
    marginTop: 20,
    marginLeft: 10,
    paddingLeft: 10,
    justifyContent: 'center'
  },
  infoArea: {
    flex: 3,
    marginLeft: 10
  },
  info: {
    marginTop: 5
  },
  avatarArea: {
    flex: 1,
    alignItems: 'center'
  },
  avatar: {
    width: 80,
    height: 80,
    borderRadius: 80
  },
  regions: {
    marginTop: 30,
    marginBottom:30,
    marginLeft: 10,
    paddingLeft: 10
  },
  titles: {
    fontSize: 18,
    color: Colors.blackColor
  },
  subContent: {
    marginLeft: 20
  },
  button: {
    marginTop: 30,
    width: 200,
    backgroundColor: Colors.darkGreen,
    borderRadius:5
  },
})

export default CourseInfoScreen;