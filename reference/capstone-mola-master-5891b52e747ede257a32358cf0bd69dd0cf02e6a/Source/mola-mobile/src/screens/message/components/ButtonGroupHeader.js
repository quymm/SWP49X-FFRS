import React, {Component} from 'react';
import { Alert } from 'react-native';
import {Icon, Button, ButtonGroup} from 'react-native-elements';

class ButtonGroupHeader extends Component {
  constructor() {
    super()
    this.state = {
      selectedIndex: 1
    }
    this.updateIndex = this
      .updateIndex
      .bind(this)
  }
  updateIndex(selectedIndex) {
    this.setState({selectedIndex})
  }
  startVideoCall() {
    Alert.alert('Phone call', 'Dang nghien cuu', [
      {
        text: 'Cancel',
        onPress: () => console.log('Cancel Pressed!')
      }, {
        text: 'OK',
        onPress: () => console.log('OK Pressed!')
      }
    ])
  }

  render() {
    const buttons = [
      {
        element: () =>< Icon name = 'phone' onPress={() =>this.startVideoCall()} />
      }, {
        element: () =>< Icon name = 'videocam' onPress={() =>this.startVideoCall()}/>
      }, {
        element: () =>< Icon name = 'info-outline' onPress={() =>this.startVideoCall()}/>
      }
    ]
    const {selectedIndex} = this.state
    return (<ButtonGroup
      onPress={this.updateIndex}
      selectedIndex={selectedIndex}
      buttons={buttons}
      containerStyle={{
      height: 100
    }}/>);
  }
}

export default ButtonGroupHeader;