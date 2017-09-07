import React, { Component } from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity } from 'react-native';
import Colors from '../../../../constants/Colors';
import { Icon } from 'react-native-elements';

class ListCourse extends Component {
  constructor(props) {
    super(props)
  }

  _goToCourseInfo(){
    const { navigate } = this.props.navigation;
    const {course, user, teacher} = this.props.course;
    navigate('CourseInfoToRegist', {course, teacher, userInfo: user});
  }
  render() {
  const {course, user} = this.props.course;
    return (
      <TouchableOpacity
      onPress={() => this._goToCourseInfo()}>
        <View style={css.lessonContainer}>
          <View style={css.imageView}>
            <Image
              style={css.image}
              source={{uri: user.avatar}} />
            <Text style={{ fontSize: 15, textAlign: 'center' }} numberOfLines={2}>{user.firstName} {user.lastName}</Text>
          </View>
          <View style={css.contentView}>
            <Text style={css.title} >Course: {course.title}</Text>       

          </View>

        </View>
      </TouchableOpacity>
    );
  }
}

var css = StyleSheet.create({
  lessonContainer: {
    flexDirection: 'row',
    borderRadius: 2,
    elevation: 2,
    paddingVertical: 5,
    flex: 1,
    marginBottom: 10,
    backgroundColor:'white'
  },
  title: {
    fontSize: 25,
    marginBottom: 5,
    textAlign: 'left',
  },
  imageView: {
    alignItems: 'center',
    margin: 5,
    width: 100
  },
  contentView: {
    margin:5,
    padding:10,
    width:260
  },
  duration: {
    fontSize: 20,
    padding: 5,
    color: Colors.redColor,
    textAlign: 'center',
    flex: 1
  },
  image: {
    width: 80,
    height: 80,
    borderRadius: 100
  }
})

export default ListCourse;