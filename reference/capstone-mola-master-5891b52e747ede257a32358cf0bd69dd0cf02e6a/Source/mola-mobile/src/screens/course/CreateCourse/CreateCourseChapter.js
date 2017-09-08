import React, { Component } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Button } from 'react-native-elements';

import ChapterList from './../components/ChapterList';
import I18n from '../../../../constants/locales/i18n';
import Accordion from 'react-native-accordion';
import Color from './../../../../constants/Colors';

class CreateCourseChapter extends Component {
  constructor(props){
    super(props);
  }
   static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('createChapter'),
      headerStyle: {
        backgroundColor: Color.darkGreen,
      },
    });
  state = {}
  render() {
    return (
      <View style={css.container}>
        <View style={css.input}>
          <Text style={css.text}>{I18n.t('courseName')} :</Text>
        </View>
        <ChapterList style={{ padding: 20 }} navigation={this.props.navigation} />
        <View style={{alignItems:'center'}}>
          <Button
            buttonStyle={css.button}
            onPress={()=> this.props.navigation.navigate('SetPrice')}
            title={I18n.t('continue')} />
        </View>

      </View>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
  },
  text: {
    fontSize: 15,
    marginRight: 30,
  },
  input: {
    width: '90%',
    margin: 20,
  },
  button: {
    width: 200,
    backgroundColor: Color.darkGreen,
    marginBottom:20,
    borderRadius: 5
  }

})
export default CreateCourseChapter;