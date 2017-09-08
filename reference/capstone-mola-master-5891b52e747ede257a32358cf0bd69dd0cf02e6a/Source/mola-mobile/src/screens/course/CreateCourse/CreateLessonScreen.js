import React, { Component } from 'react';
import { View, Text, StyleSheet, BackHandler } from 'react-native';
import { Button } from 'react-native-elements';

import DynamicList from './../components/DynamicList';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';

class CreateLessonScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      chapterId: 0,
      title: "",
      unstructureCourseId: 0,
      number:0,
    }
    


  }
  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('lesson'),
      headerStyle: {
        backgroundColor: Color.darkGreen,
      },
    });
  componentDidMount() {
    const { chapterInfo } = this.props.navigation.state.params;
    this.setState({
      chapterId: chapterInfo.id,
      title: chapterInfo.title,
      number: chapterInfo.number
    })
    


  }
  render() {
    return (
      <View style={css.container}>
        <Text style={css.header}>{'Chapter ' + this.state.number +'\n' + this.state.title}</Text>

        <DynamicList style={{ padding: 20 }}navigation={this.props.navigation}  />

        {/*<View style={{ alignItems: 'center' }}>
          <Button
            buttonStyle={css.button}
            title={I18n.t('save')}
            onPress={() => this.props.navigation.goBack()} />
        </View>*/}
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
    marginLeft: 30,
  },
  header: {
    fontSize: 20,
    fontWeight: 'bold',
    marginTop: 30,
    marginBottom: 10,
    textAlign: 'center',
    width: '100%',
  },
  input: {
    width: '90%',
    marginBottom: 20,
  },
  button: {
    width: 200,
    backgroundColor: Color.darkGreen,
    marginBottom: 20,
    borderRadius: 5,
  }

})
export default CreateLessonScreen;