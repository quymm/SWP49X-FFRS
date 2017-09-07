import React, { Component } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  ScrollView,
  ToastAndroid,
  TouchableHighlight
} from 'react-native';
import ModalDropdown from 'react-native-modal-dropdown';
import { Button, Icon, CheckBox, Slider } from 'react-native-elements';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';
import CourseAPI from '../../../apis/course';
import LanguagesAPI from '../../../apis/languages';
import accounting from '../../../services/accounting';
import { currencies } from '../../../../constants/currencies';
import UserSetting from '../../UserSetting';
import ExchangeRates from '../../ExchangeRates';

class CreateCourseScreen extends Component {
  constructor(props) {
    super(props)
    this.state = {
      title: '',
      value: 0,
      language: '',
      height: 0,
      checked: false,
      structure: false,
      introduction: '',
      price:0,
      languageTeach: []
    }
  }
  async _onCreatePress(course) {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.createCourse(course);
    console.log('data', data);
    this.setState({
      courses: data.data
    });
    ToastAndroid.show(I18n.t('createCourseSuccessfully'), ToastAndroid.SHORT);
    this.props.navigation.navigate('CourseInfo', { courseInfo: data.data })
  }
  handleOnPress(value) {
    this.setState({ value: value })
  }
  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('createCourse'),
      headerStyle: {
        backgroundColor: Color.darkGreen,
      },
    });
  
getCurrencySymbol(currencyCode) {
    const index = currencies.findIndex(currency => {
      return currency.code === currencyCode;
    })
    return index >= 0
      ? currencies[index].symbol
      : '';
  }

  async componentDidMount() {
    
    const languagesAPI = new LanguagesAPI();
    const languages = await languagesAPI.getLanguageUser();

    const languagesData = languages.data.languageTeach;
    const languageTeach = languagesData.map(lang => lang.name);
    this.setState({ languageTeach });

  }

  _renderRowModal(rowData, rowID, highlighted){
    let evenRow = rowID % 2;
    return (
      <TouchableHighlight underlayColor='cornflowerblue'>
        <View style={[css.modalDropdown, {backgroundColor: evenRow ? '#ecf0f1' : 'white'}]}>
          <Text style={[css.modalDropdownText, highlighted && {color: 'mediumaquamarine'}]}>
            {`${rowData}`}
          </Text>
        </View>
      </TouchableHighlight>
    );
  }
  render() {
    return (
      <ScrollView style={{ flex: 1, backgroundColor: 'white' }}>
        <View style={css.container}>

          <View style={css.header}>
            <Icon
              name='book'
              color={Color.darkGreen}
              size={40}
            />
            <TextInput
              underlineColorAndroid='transparent'
              placeholder={I18n.t('inputCourseName')}
              placeholderTextColor='lightgrey'
              onChangeText={(title) => this.setState({ title })}
              style={{ width: 200 }}></TextInput>
          </View>
          <View style={css.dropDown}>
            <Text style={css.text}>{I18n.t('language')}:</Text>
            <View style={css.searchView}>
              
              {/*<TextInput
                style={css.textInput}
                underlineColorAndroid='transparent'
                onChangeText={(language) => this.setState({ language })}
                placeholder={I18n.t('aboutyou_searchLanguage')}
                placeholderTextColor='lightgrey' />*/}
              <ModalDropdown
                defaultValue={I18n.t('courseChooseLanguage')}
                options={this.state.languageTeach}
                style={css.modalSelect}
                textStyle={css.modalText}
                renderRow={this._renderRowModal.bind(this)}
                onSelect={(index, value) => this.setState({courseLanguage: value})}
                />
            </View>
          </View>
          <View style={css.checkView}>
            <CheckBox
              title={I18n.t('unstructureCourse')}
              checked={!this.state.structure}
              containerStyle={css.chbContainer}
              checkedIcon='check'
              uncheckedColor='white'
              checkedColor={Color.darkGreen}
              onPress={() => this.setState({ structure: false })}
              textStyle={css.checkText}
            />

            <CheckBox
              title={I18n.t('structureCourse')}
              checked={this.state.structure}
              containerStyle={css.chbContainer}
              checkedIcon='check'
              checkedColor={Color.darkGreen}
              uncheckedColor='white'
              onPress={() => this.setState({ structure: true })}
              textStyle={css.checkText}
            />
          </View>
          <View style={{ width: '90%', marginTop: 10 }}>
            <View style={{flexDirection:'row'}}>
            <Text style={css.text}>{I18n.t('setPrice')}:</Text>
            <Text style={{ marginLeft:5, fontSize:25}}>{accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * this.state.price).toFixed(2))
              + ' ' + this.getCurrencySymbol(UserSetting.CURRENCY)+I18n.t('min')}</Text>
            </View>
            <Slider
              value={this.state.price}
              // thumbStyle = {css.tractStyle}
              // trackStyle = {css.tractStyle}
              maximumValue={20}
              step={1}
              minimumTrackTintColor={Color.darkGreen}
              thumbTintColor={Color.darkGreen}
              onValueChange={(price) => this.setState({ price })} />

              
              
          </View>

          <View style={css.input}>
            <Text style={css.text}>{I18n.t('introduction')}:</Text>
            <View style={css.introView}>
              <TextInput
                style={{ marginLeft: 10 }}
                placeholder={I18n.t('introCourse')}
                placeholderTextColor='lightgrey'
                underlineColorAndroid='transparent'
                multiline={true}
                onChangeText={(introduction) => this.setState({ introduction })}
              />
            </View>
          </View>

          <Button
            buttonStyle={css.button}
            title={I18n.t('continue')}
            onPress={() => this._onCreatePress({
              title: this.state.title,
              language: this.state.courseLanguage,
              structure: this.state.structure,
              introduction: this.state.introduction,
              price: this.state.price,
            })} />

        </View>
      </ScrollView>

    );
  }
}

var css = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'white'
  },
  chbContainer: {
    marginTop: 10,
    marginBottom: 0,
    paddingBottom: 0,
    paddingLeft: 0,
    marginRight: 0,
    marginLeft: 0,
    backgroundColor: 'white',
    borderRadius: 0,
    borderWidth: 1,
    borderTopWidth: 0,
    borderLeftWidth: 0,
    borderRightWidth: 0
  },
  checkView: {
    width: '50%', marginTop: 5,
  },
  checkText: {
    fontWeight: 'normal',
    fontSize: 15,
  },
  searchView: {
    alignItems: 'center',
    flexDirection: 'row',
    width: 250,
    borderColor: 'lightgrey',
    // borderWidth: 1
  },
  text: {
    fontSize: 15,
    marginRight: 10,
    marginBottom: 10
  },
  textInput: {
    width: 200
  },
  header: {
    padding: 10,
    backgroundColor: 'white',
    width: '95%',
    margin: 20,
    flexDirection: 'row',
    elevation: 2,
    borderRadius: 2
  },
  input: {
    padding: 10,
    backgroundColor: 'white',
    width: '95%',
    margin: 20,
    marginTop:5

  },
  dropDown: {
    width: '95%', flexDirection: 'row', alignItems: 'center',
    padding: 10,

  },
  introView: {
    height: 150, width: '100%',
    borderColor: 'lightgrey',
    borderWidth: 1

  },

  button: {
    width: 200,
    backgroundColor: Color.darkGreen,
    borderRadius: 5
  },
  modalSelect: {
    backgroundColor: Color.transparent,
    marginBottom: 10,
    borderColor: Color.darkGreen,
    borderBottomWidth: 1
  },
  modalText: {
    color: Color.grayColor
  },
  modalDropdown: {
    height: 35  
  },
  modalDropdownText: {
    fontSize: 18
  },
})

export default CreateCourseScreen;