import React, {Component} from 'react';
import {
  Text,
  View,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
  ScrollView,
  AsyncStorage,
  ActivityIndicator
} from 'react-native';
import {Button, Icon} from 'react-native-elements';
import ImagePicker from 'react-native-image-picker';
import {connect} from 'react-redux';
import RNFetchBlob from 'react-native-fetch-blob'
import {SegmentedControls} from 'react-native-radio-buttons'
import DropdownAlert from 'react-native-dropdownalert'
import * as Progress from 'react-native-progress';

import TeacherSignUpAPI from '../../../apis/teacher-signup';
import {sendTeacherData} from './actions';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';
import UserAPI from '../../../apis/user';
import {updateUserProfile} from '../../../screens/login/actions';
const teacherSignUpAPI = new TeacherSignUpAPI();
const userAPI = new UserAPI();
const mapStateToProps = (state) => ({teacher: state.teacherSignUp.teacher});
var {height, width} = Dimensions.get('window');
@connect(mapStateToProps, {updateUserProfile})
class VideoIntro extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: {},
      selectedOption: null,
      videoSource: [],
      percentUploading: 0,
      loading: false,
      click: false
    };

    this.selectVideoTapped = this
      .selectVideoTapped
      .bind(this);
  }
  async componentWillMount() {
    let user = await AsyncStorage.getItem('USER');
    user = JSON.parse(user);

    this.setState({user});
  }

  async selectVideoTapped(language) {
    const options = {
      title: I18n.t('videoPicker'),
      takePhotoButtonTitle: I18n.t('takeVideo'),
      mediaType: 'video',
      videoQuality: 'medium',
      immediate: false
    };

    ImagePicker.showImagePicker(options, async(response) => {

      if (response.didCancel) {
        console.log('User cancelled video picker');
      } else if (response.error) {
        console.log('ImagePicker Error: ', response.error);
      } else if (response.customButton) {
        console.log('User tapped custom button: ', response.customButton);
      } else {
        const video = {
          language: language,
          uri: response.uri
        };
        const pos = this.state.videoSource
          .findIndex(source => source.language === video.language);
        if (pos >= 0) {
          const videoSource = this.removeIndex(this.state.videoSource, pos);
          this.setState({
            videoSource: [
              ...videoSource,video
            ]
          });
        } else {
          await this.setState({
            videoSource: [
              ...this.state.videoSource, video
            ]
          });
        }

      }
    });
  }
  removeIndex(array, index) {
    return [
      ...array.slice(0, index),
      ...array.slice(index + 1)
    ];
  }
  async onPressComplete() {
    this.setState({loading: true, click: true})
    let {user} = this.state;
    const {videoSource} = this.state;
    user.videoIntro = videoSource;
    const teacher = await teacherSignUpAPI.sendTeacherData(user);
    const composed = await teacherSignUpAPI.composedData(user);

    await teacherSignUpAPI
      .uploads(composed)
      .uploadProgress((written, total) => {
        this.setState({
          percentUploading: (written / total) * 100
        })
      })
      .then((res) => {
        this.setState({videoSource: [], percentUploading: 100});
        this.props.onContinue();
      })
      .catch(err => console.log(err));

    const userProfile = await userAPI.getUserProfile({username: user.username});
    user.avatar = userProfile.avatar;
    await this.props.updateUserProfile(user);
    await AsyncStorage.setItem('USER', JSON.stringify(user));
  }

  render() {
    const {percentUploading} = this.state;
    const {languagesTeach} = this.state.user;
    const languageChoose = this.state.selectedOption;
    // if (percentUploading === 100) {   this.props.onContinue(); }

    return (
      <View style={styles.page}>
        <View style={{
          marginLeft: 40
        }}>
          <Text style={styles.text}>{I18n.t('videoOutline')}</Text>
          <View
            style={{
            marginBottom: 20
          }}>
            <Text style={styles.text}>- {I18n.t('whoYouAre')}
              ?</Text>
            <Text style={styles.text}>- {I18n.t('youInteres')}
              ?</Text>
            <Text style={styles.text}>- {I18n.t('canYouTeach')}
              ?</Text>
          </View>
        </View>
        {languagesTeach
          ? <SegmentedControls
              optionContainerStyle={{width:60}}
              options={languagesTeach}
              onSelection={selectedOption => this.setState({selectedOption})}
              selectedOption={languageChoose}/>
          : <View></View>
        }
        <Text style={styles.header}>{I18n.t('recordVideo')}</Text>
        <Button
          raised
          icon={{
          name: 'videocam'
        }}
          iconRight
          disabled={!languageChoose}
          buttonStyle={styles.buttonRecord}
          onPress={() => {
          this.selectVideoTapped(languageChoose)
        }}
          title={languageChoose
          ? I18n.t('teacherSignup_recordVideo')
          : I18n.t('teacherSignup_recordVideo_disabled')}/>
        <Button
          raised
          icon={{
            name: 'send'
          }}
          iconRight
          disabled={this.state.videoSource != null && this.state.videoSource.length != (languagesTeach && languagesTeach.length)}
          buttonStyle={styles.buttonComplete}
          onPress={() => {
            this.onPressComplete()
          }}
          title={I18n.t('complete')}/>
          {this.state.click
          ? <View
              style={{
                width: width,
                alignItems: 'center'
              }}>
              <Progress.Bar
                size={100}
                width={width}
                style={{
                  margin: 10
                }}
                color={Color.darkGreen}
                showsText={true}
                formatText={() => `${ (this.state.percentUploading).toFixed(0)}%`}
                progress={this.state.percentUploading / 100}
                indeterminate={this.state.percentUploading === 100}
                direction="counter-clockwise"/>
            </View>
          : <View></View>
        }
        <DropdownAlert
          ref={(ref) => this.dropdown = ref}
          onClose={(data) => this.onClose()}/>
      </View>

    );
  }
  onClose() {
    return {
      type: 'info',
      title: 'hello',
      message: 'world',
      action: () => {
        console.log('action pressed')
      }
    }
  }
}
const styles = StyleSheet.create({
  page: {
    flex: 1,
    alignItems:'center'

  },
  header: {
    fontSize: 20,
    fontWeight: 'bold', marginTop:30
  },
  text: {
    fontSize: 15
  },
  button: {
    top: 50,
    width: 200,
    marginTop: 10,
    borderRadius: 5,
    backgroundColor: Color.lightGreen
  },
  buttonRecord: {
    marginTop: 30,
    width: 200,
    backgroundColor: Color.redColor,
    borderRadius: 5
  },
  buttonComplete: {
    marginTop: 30,
    width: 200,
    borderRadius: 5,
    backgroundColor: Color.darkGreen
  },
  buttonView: {
    alignItems: 'center'
  }
})
export default VideoIntro;