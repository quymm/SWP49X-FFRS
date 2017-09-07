import React from 'react';
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  ScrollView,
  Image,
  TouchableOpacity,
  AsyncStorage,
  DatePickerAndroid
} from 'react-native';
import { Button, Icon } from 'react-native-elements';
import ImagePicker from 'react-native-image-picker';
import { connect } from 'react-redux';
import { apiRegister } from './actions';
import I18n from '../../../constants/locales/i18n';
import Color from '../../../constants/Colors';

import RNFetchBlob from 'react-native-fetch-blob';
import { uploads } from '../../apis/uploads';

import UserAPI from '../../apis/user';
// const mapStateToProps = (state) => ({ user: state.auth.user.data });
const userAPI = new UserAPI();

//@connect(mapStateToProps, { apiRegister })
class SignupScreen extends React.Component {

  state = {
    error: '',
    avatarDisplay: require('../../../assets/images/profile.jpg')
  }
  selectPhotoTapped() {
    const options = {
      quality: 0.7,
      maxWidth: 500,
      maxHeight: 500,
      noData: true,
      storageOptions: {
        skipBackup: true,
        cameraRoll: true
      },
      takePhotoButtonTitle: I18n.t('aboutyou_take_photo'),
      cancelButtonTitle: I18n.t('aboutyou_cancel_take_photo'),
      chooseFromLibraryButtonTitle: I18n.t('aboutyou_choose_from_library'),
      permissionDenied: {
        title: I18n.t('aboutyou_permission_denied')
      }

    };

    ImagePicker.showImagePicker(options, (response) => {
      console.log('Response = ', response);
      if (response.didCancel) {
        console.log('User cancelled photo picker');
      } else if (response.error) {
        console.log('ImagePicker Error: ', response.error);
      } else if (response.customButton) {
        console.log('User tapped custom button: ', response.customButton);
      } else {
        let source = {
          uri: response.uri
        };
        this.setState({
          avatarSource: source.uri,
          avatarDisplay: { uri: source.uri }
        });
      }
    });
  }

  async onPickBirthday() {
    const { action, year, month, day } = await DatePickerAndroid.open({
      maxDate: new Date()
    });
    if (action === DatePickerAndroid.dateSetAction) {
      const birthday = day + '-' + (month + 1) + '-' + year;

      this.setState({ birthday })
    }
  }

  async onRegister() {
    const { username, password, email, firstName, lastName } = this.state;
    // await this.props.apiRegister({ username, password, email, firstName, lastName });
    const userSignup = await userAPI.registerUsingRegisterApi({ username, password, email, firstName, lastName });
    
    if (userSignup.status === 'ok') {

      const userData = userSignup.data;
      await AsyncStorage.setItem('USER_TOKEN', userData.token);
      // await AsyncStorage.setItem('USER', JSON.stringify(userData.user));

      let avatar;

      if (this.state.avatarSource) {
        const uploadResult = await this.uploadAvatar({ username });
        avatar = JSON.parse(uploadResult.data).user_avatar[0];
      } else {
        avatar = {
          filePath: 'http://112.78.4.97/fpt/mola/images/avatar.jpg'
        }
      }
      const userAPI = new UserAPI();
      const updateAvatarResult = await userAPI.updateAvatar(avatar);
      if (updateAvatarResult.status === 'ok') {
        // await AsyncStorage.setItem('USER', JSON.stringify(updateAvatarResult.data));
      }

      this.props.navigation.navigate('RegisterLanguage');
    } else {
      this.setState({ error: userSignup.status });
    }
  }

  uploadAvatar(user) {
    const { avatarSource } = this.state;
    const username = user.username;
    const files = [];
    const avatarToUpload = {
      name: `${username}_avatar`,
      filename: avatarSource.length && avatarSource.substr(avatarSource.lastIndexOf('/') + 1),
      type: 'image/jpeg',
      data: RNFetchBlob.wrap(avatarSource)
    }
    files.push(avatarToUpload);

    return uploads(files);
  }

  render() {
    return (
      <ScrollView style={{ backgroundColor: 'white' }}>
        <View style={css.container}>
          <View>
            {/* <TouchableOpacity onPress={() => this.selectPhotoTapped()}>
              <Image
                style={{
                  width: 100,
                  height: 100,
                  borderRadius: 100,
                  marginBottom: 50
                }}
                source={this.state.avatarDisplay} />
            </TouchableOpacity> */}
          </View>
          <Text style={{ color: 'red' }}>{this.state.error}</Text>
          <View style={css.input}>
            <Icon
              name='perm-identity'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              style={{
                width: 200,
                color: '#98948F',
                fontSize: 20
              }}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('firstName')}
              placeholderTextColor='#98948F'
              onChangeText={(firstName) => this.setState({ firstName })}></TextInput>
          </View>
          <View style={css.input}>
            <Icon
              name='perm-identity'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              style={{
                width: 200,
                color: '#98948F',
                fontSize: 20
              }}
              underlineColorAndroid='transparent'
              placeholder={'Last Name'}
              placeholderTextColor='#98948F'
              onChangeText={(lastName) => this.setState({ lastName })}></TextInput>
          </View>
          <View style={css.input}>
            <Icon
              name='email'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              style={{
                width: 200,
                color: '#98948F',
                fontSize: 20
              }}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('email')}
              placeholderTextColor='#98948F'
              keyboardType='email-address'
              onChangeText={(email) => this.setState({ email })}></TextInput>
          </View>
          <View style={css.input}>
            <Icon
              name='perm-identity'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              style={{
                width: 200,
                color: '#98948F',
                fontSize: 20
              }}
              underlineColorAndroid='transparent'
              placeholder={'Username'}
              placeholderTextColor='#98948F'
              onChangeText={(username) => this.setState({ username })}></TextInput>
          </View>
          <View style={css.input}>
            <Icon
              name='lock'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              secureTextEntry={true}
              style={{
                width: 200,
                color: '#98948F',
                fontSize: 20
              }}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('password')}
              placeholderTextColor='#98948F'
              onChangeText={(password) => this.setState({ password })}></TextInput>
          </View>
          <View style={css.input}>
            <Icon
              name='lock'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TouchableOpacity
              onPress={() => this.onPickBirthday()}
            >
              <TextInput
              secureTextEntry={true}
              style={{
                width: 200,
                color: '#98948F',
                fontSize: 20
              }}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('confirmPass')}
              placeholderTextColor='#98948F'></TextInput>
              </TouchableOpacity>
          </View>
          <Button
            onPress={() => this.onRegister()}
            buttonStyle={css.button}
            raised
            color="white"
            title={I18n.t('register')} />

        </View>
      </ScrollView>
    );
  }
}

var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    paddingTop: 30,
    alignItems: 'center'
  },
  textinput: {
    width: 200,
    color: 'black',
    fontSize: 20
  },
  input: {
    borderBottomColor: '#98948F',
    borderBottomWidth: 1,
    width: '90%',
    height: 50,
    margin: 5,
    paddingRight: 20,
    flexDirection: 'row',
    alignItems: 'center'
  },

  button: {
    backgroundColor: Color.darkGreen,
    width: 200,
    margin: 40,
    borderRadius: 5
  }
});

export default SignupScreen;