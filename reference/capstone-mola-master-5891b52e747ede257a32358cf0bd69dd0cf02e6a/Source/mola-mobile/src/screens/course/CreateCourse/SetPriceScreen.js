import React, { Component } from 'react';
import { View, Text, StyleSheet,Alert } from 'react-native';
import { Button, Slider } from 'react-native-elements';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';


class SetPriceScreen extends Component {

  state = {
    value: 0,
  }
   static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('yourPrice'),
      headerStyle: {
        backgroundColor: Color.darkGreen,
      },
    });
  _success(){
     Alert.alert(
            I18n.t('createSuccess'),
            null,
            [
             {text: 'OK'}
            ],
            {
              cancelable: false
            }
          )
  }
  render() {
    return (
      <View style={css.container}>
        <Text style={css.header}>{I18n.t('createProgress')}</Text>
        <View style={css.input}>
          <Text style={css.text}>{I18n.t('setPrice')}:</Text>
          <View style={{}}>
            <Slider
              value={this.state.value}
              // thumbStyle = {css.tractStyle}
              // trackStyle = {css.tractStyle}
              maximumValue='20'
              step='0.5'
              minimumTrackTintColor = {Color.darkGreen}
              thumbTintColor = {Color.darkGreen}
              onValueChange={(value) => this.setState({ value })} />
          </View>
           
        </View>
        <Button
          buttonStyle={css.button}
          textStyle={css.btnText}
          title={I18n.t('price') + this.state.value + I18n.t('min')}
          onPress={() =>{ 
            this._success()
            this.props.navigation.navigate('Course')}}/>
      </View>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
	backgroundColor: 'white',
  alignItems:'center',
  },
  text: {
    fontSize: 18,
    marginRight: 30,
    marginBottom: 30,
  },
  header: {
    fontSize: 20,
    fontWeight: 'bold',
    margin: 30,
  },
  input: {
    width: '90%',
    marginBottom: 20,
  },
  button: {
    width: 200,
    marginTop: 50,
    backgroundColor: Color.darkGreen,
    borderRadius:5
  },
  btnText: {
    fontSize: 18,
  },
  sliderView: {
    flex: 1,
    alignItems: 'stretch',
    justifyContent: 'center'
  },
  tractStyle: {
    backgroundColor: Color.darkGreen,
  },

})
export default SetPriceScreen;