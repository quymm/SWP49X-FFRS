import React, { Component } from 'react';
import {
  Text,
  View,
  StyleSheet,
  ScrollView,
  Image,
  TextInput,
  TouchableOpacity,
  AsyncStorage,
  DatePickerAndroid
} from 'react-native';
import { Button, Icon } from 'react-native-elements';
import ImagePicker from 'react-native-image-picker';
import { connect } from 'react-redux';
import MultiSelect from 'react-native-multiple-select';

import { countriesFlags, countriesPopular } from '../../../../constants/countries';
import TabLabel from '../../../components/common/TagLabel';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';
import LanguagesAPI from '../../../apis/languages';
import UserAPI from '../../../apis/user';

const userAPI = new UserAPI();
const languagesAPI = new LanguagesAPI();
const mapStateToProps = (state) => ({
  user: state.auth.user.data.data,
});

@connect(mapStateToProps)
class AboutYou extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: {},
      birthday: '',
      avatarSource: null,
      languageSpeak: [],
      languageTeach: [],
    };
    this.selectPhotoTapped = this.selectPhotoTapped.bind(this);
    this.onContinuePress = this.onContinuePress.bind(this);
  }
  async componentWillMount() {
    let user = await AsyncStorage.getItem('USER');
    user = JSON.parse(user);
    const userProfile = await userAPI.getUserProfile({username: user.username});
    const languages = await languagesAPI.getLanguageUser();
    
    this.setState({
      user: Object.assign({}, userProfile),
      languageSpeak: languages.data.languageSpeak,
    //  languageTeach: languages.data.languageTeach,
    })
    
    if (this._multiSelectLanguageSpeak) this._multiSelectLanguageSpeak.state.selectedItems = [...languages.data.languageSpeak];
    // ifw (this._multiSelectLanguageTeach) this._multiSelectLanguageTeach.state.selectedItems = [...languages.data.languageTeach];
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

        this.setState({ avatarSource: source.uri });
      }
    });
  }

  async onPickBirthday() {
    const { action, year, month, day } = await DatePickerAndroid.open({
      maxDate: new Date()
    });
    if (action === DatePickerAndroid.dateSetAction) {

      const birthday = day + '-' + (month + 1) + '-' + year;

      let user = this.state.user;
      user.birthday = birthday;
      this.setState({ user })
    }
  }

  async onContinuePress() {

    let { user, } = this.state
    const { avatarSource, birthday } = this.state;
    const { avatar, email, firstname, lastname } = user;

    user.birthday = birthday;
    user.avatar = avatarSource;
    user.languagesSpeak = this.parseListLanguageChoosen(this._multiSelectLanguageSpeak.state.selectedItems);
    user.languagesTeach = this.parseListLanguageChoosen(this._multiSelectLanguageTeach.state.selectedItems);
    await AsyncStorage.setItem('USER', JSON.stringify(user));
    // this.setState({ avatarSource: null });

    console.log('user with choose languages', user)
    this.props.onContinue();
  }
  parseListLanguageChoosen(countries) {
    return countries.map(country => country.name);
  }

  selectLanguageSpeak(item) {
    // console.log('language speak: ', this._multiSelectLanguageSpeak.state.selectedItems);
    const languageSpeak = this.state.languageSpeak.filter(l => l.name !== item.name);
    languageSpeak.push(item);
    this.setState({languageSpeak: [...languageSpeak]});
  }
  selectLanguageTeach(item) {
    const languageTeach = this.state.languageTeach.filter(l => l.name !== item.name);
    languageTeach.push(item);
    this.setState({languageTeach: [...languageTeach]});

    // console.log('language teach: ', this._multiSelectLanguageTeach.state.selectedItems);
  }
  render() {
    console.log('AboutYou,state', this.state)
    return (
      <ScrollView>
        <View style={styles.page}>
          <Text style={styles.title}>{I18n.t('aboutyou_title')}</Text>
          <View style={{
            width: '90%'
          }}>
            <Text style={styles.text}>{I18n.t('aboutyou_intro')}</Text>
          </View>
          <View>
            <TouchableOpacity onPress={() => this.selectPhotoTapped()}>
              <View
                style={[
                  styles.avatar, {
                    marginBottom: 20
                  }
                ]}>
                {this.state.avatarSource === null
                  ? <Image style={css.avatar} source={{ uri: this.state.user.avatar }} />
                  : <Image style={css.avatar} source={{
                    uri: this.state.avatarSource,
                    scale: 2,
                    type: 'jpeg'
                  }} />
                }
              </View>
            </TouchableOpacity>
          </View>
          <View style={css.input}>
            <Icon
              name='perm-identity'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              onChangeText={(firstName) => {
                let user = this.state.user;
                user.firstName = firstName;
                this.setState({ user })
              }}
              style={css.textInput}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('firstName')}
              value={this.state.user.firstName}
              placeholderTextColor='#98948F'></TextInput>
          </View>
          <View style={css.input}>
            <Icon
              name='perm-identity'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              onChangeText={(lastName) => {
                let user = this.state.user;
                user.lastName = lastName;
                this.setState({ user })
              }}
              style={css.textInput}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('lastName')}
              value={this.state.user.lastName}
              placeholderTextColor='#98948F'></TextInput>
          </View>
          <View style={[css.input, {display: 'none'}]}>
            <Icon
              name='today'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TouchableOpacity
              onPress={() => this.onPickBirthday()}
            >
              <TextInput
                editable={false}
                style={css.textInput}
                underlineColorAndroid='transparent'
                placeholder={I18n.t('birthday')}
                value={this.state.user.birthday}
                onChangeText={(birthday) => this.setState({ birthday })}
                placeholderTextColor='#98948F'></TextInput></TouchableOpacity>
          </View>
          <View style={css.input}>
            <Icon
              name='email'
              color='#98948F'
              style={{
                marginRight: 20
              }} />
            <TextInput
              onChangeText={(email) => {
                let user = this.state.user;
                user.email = email;
                this.setState({ user })
              }}
              style={css.textInput}
              underlineColorAndroid='transparent'
              placeholder={I18n.t('email')}
              value={this.state.user.email}
              placeholderTextColor='#98948F'
              keyboardType='email-address'></TextInput>
          </View>

          {this.state.languageSpeak ?
              <View style={{ width: '100%', padding: 20, display: 'none' }}>
                <Text style={styles.title}>{I18n.t('aboutyou_languagespeak')}</Text>
                
                <MultiSelect
                  ref={c => this._multiSelectLanguageSpeak = c}
                  items={countriesPopular}
                  uniqueKey="name"
                  selectedItemsChange={(itemSelected) => this.selectLanguageSpeak(itemSelected)}
                  selectedItems={this.state.languageSpeak || []}
                  selectText={I18n.t('chooseLanguage')}
                  searchInputPlaceholderText="Search languages..."
                  //altFontFamily="ProximaNova-Light"
                  tagRemoveIconColor="#CCC"
                  tagBorderColor={Color.lightGreen}
                  tagTextColor={Color.lightGreen}
                  selectedItemTextColor="#CCC"
                  selectedItemIconColor={Color.lightGreen}
                  itemTextColor="#000"
                  searchInputStyle={{ color: '#CCC' }}
                  submitButtonColor={Color.darkGreen}
                  submitButtonText="Close"
                />
              </View>
            : <View></View>}

          <View >
            <Text style={styles.title}>{I18n.t('aboutyou_languageTeach')}</Text>
          </View>
          <View style={{ width: '100%', padding: 20 }}>
            <MultiSelect
              ref={c => this._multiSelectLanguageTeach = c}
              items={countriesPopular}
              uniqueKey="name"
              selectedItemsChange={(itemSelected) => this.selectLanguageTeach(itemSelected)}
              selectedItems={this.state.languageTeach || []}
              selectText={I18n.t('chooseLanguage')}
              searchInputPlaceholderText="Search languages..."
              //altFontFamily="ProximaNova-Light"
              tagRemoveIconColor="#CCC"
              tagBorderColor={Color.lightGreen}
              tagTextColor={Color.lightGreen}
              selectedItemTextColor="#CCC"
              selectedItemIconColor={Color.lightGreen}
              itemTextColor="#000"
              searchInputStyle={{ color: '#CCC' }}
              submitButtonColor={Color.darkGreen}
              submitButtonText="Close"
            />
          </View>
          <View style={styles.buttonView}>
            <Button
              disabled={ this.state.languageTeach.length === 0 && this.state.languageSpeak.length === 0 }
              buttonStyle={styles.button}
              textStyle={{
                fontSize: 15
              }}
              onPress={() => this.onContinuePress()}
              title={I18n.t('continue')} />
          </View>
        </View>
      </ScrollView>
    );
  }
}
export default AboutYou;
const styles = StyleSheet.create({
  page: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    // backgroundColor: 'black',
  },
  button: {
    marginTop: 10,
    width: 200,
    backgroundColor: Color.darkGreen,
    borderRadius: 5
  },
  title: {
    textAlign: 'center',
    fontSize: 18,
    fontWeight: 'bold',
    marginTop: 20,
    marginBottom: 20,
  },
  text: {
    fontSize: 15,
    marginTop: 20,
  },
  languageView: {
    alignItems: 'flex-start',
  },
  buttonView: {
    alignItems: 'center',
    margin: 50,
  },
  tagsContainer: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: 'wrap'
  },
  tag: {
    justifyContent: 'center',
    marginTop: 6,
    marginRight: 3,
    padding: 8,
    height: 24,
    borderRadius: 2,
  },

});
var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    paddingTop: 30,
    alignItems: 'center'
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
    backgroundColor: 'rgba(102,198,186,0.75)',
    width: 350,
    margin: 40,
    borderRadius: 5
  },
  textInput: {
    width: 200,
    color: '#98948F',
    fontSize: 15
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 100,
    marginBottom: 30,
    marginTop: 30
  }
});
