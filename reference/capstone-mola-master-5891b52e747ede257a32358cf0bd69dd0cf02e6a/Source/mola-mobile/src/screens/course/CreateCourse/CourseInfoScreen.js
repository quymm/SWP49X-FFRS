import React, { Component } from 'react';
import { 
  View, 
  Text, 
  StyleSheet, 
  ScrollView, 
  Image, 
  TouchableOpacity, 
  Modal, 
  TextInput, 
  Slider,
  TouchableHighlight,
  ToastAndroid,
} from 'react-native';
import { Icon, Button } from 'react-native-elements';
import ModalDropdown from 'react-native-modal-dropdown';

import Colors from '../../../../constants/Colors';
import I18n from '../../../../constants/locales/i18n';
import ChapterList from '../components/ChapterList';
import CourseAPI from '../../../apis/course';
import LanguagesAPI from '../../../apis/languages';
import { currencies } from '../../../../constants/currencies';
import UserSetting from '../../UserSetting';
import ExchangeRates from '../../ExchangeRates';
import accounting from '../../../services/accounting';


class CourseInfoScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      courseId: 0,
      courseName: "",
      teacherName: "",
      language: "",
      intro: "",
      courseType: "",
      modalVisible: false,
      price: 0,
      editPrice: 0,
      languageTeach: [],
    }
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
  setModalVisible(visible) {
    this.setState({ modalVisible: visible });
  }

  getCurrencySymbol(currencyCode) {
    const index = currencies.findIndex(currency => {
      return currency.code === currencyCode;
    })
    return index >= 0 ? currencies[index].symbol : '';
  }

  async componentDidMount() {
    console.log('params', this.props.navigation.state.params);
    const { courseInfo } = this.props.navigation.state.params;
    var structuredType;
    if (courseInfo.structured) {
      structuredType = I18n.t('structureCourse');
    } else {
      structuredType =  I18n.t('unstructureCourse');
    }

    const languagesAPI = new LanguagesAPI();
    const languages = await languagesAPI.getLanguageUser();

    const languagesData = languages.data.languageTeach;
    const languageTeach = languagesData.map(lang => lang.name);
    this.setState({ languageTeach });


    this.setState({
      courseId: courseInfo.id,
      courseName: courseInfo.title,
      language: courseInfo.language,
      intro: courseInfo.introduction,
      structure: structuredType,
      price: courseInfo.price,
      editPrice: courseInfo.price,
      languageTeach,
    })
    // BackHandler.addEventListener("hardwareBackPress", ()=>{
    //   console.log('back handler',this);
    //   return true;
    // })
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

  async _editCourse(course) {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.editCourse(course);
  }

  async _deleteCourse(id){
    const courseAPI = new CourseAPI();
    const data = await courseAPI.deleteCourse(id);
    if(data.status && data.status == 'ok'){
      ToastAndroid.show(I18n.t('deleteCourseSuccessfully'), ToastAndroid.SHORT);
      this.props.navigation.state.params.loadData();
      this.props.navigation.goBack();
    }
  }

  render() {
    return (
      <ScrollView style={css.container}>
        <View style={css.regions}>
          <Text style={css.header}>{this.state.courseName}</Text>
          <View style={{ alignItems: 'center' }}>
            <Modal
              animationType={"slide"}
              transparent={true}
              onRequestClose={() => console.log("dcm")}
              visible={this.state.modalVisible}
            >
              <View style={css.modalStyle} >
                <View style={css.headerView}>
                  <Text style={css.headerModel}>{I18n.t('editCourse')}</Text>
                </View>
                <View style={{
                  width: '95%', flexDirection: 'row', elevation: 2,
                  borderRadius: 2, padding: 10, marginTop: 20, marginBottom: 20
                }}>
                  <Icon
                    name='book'
                    color={Colors.darkGreen}
                    size={40}
                  />
                  <TextInput
                    underlineColorAndroid='transparent'
                    onChangeText={(courseName) => this.setState({ courseName })}
                    value={this.state.courseName}
                    style={{ width: 200 }}></TextInput>

                </View>
                <View style={css.search}>
                  <Text style={{ fontSize: 18 }}>{I18n.t('language')} :</Text>
                  <View style={css.searchView}>
                    {/*<Icon
                      name='search'
                      color={Colors.darkGreen}
                      style={{
                        marginRight: 10,
                        marginLeft: 10
                      }} />
                    <TextInput
                      style={{ width: 200 }}
                      underlineColorAndroid='transparent'
                      value={this.state.language}
                      onChangeText={(language) => this.setState({ language })}
                    />*/}
                    <ModalDropdown
                      defaultValue={this.state.language}
                      options={this.state.languageTeach}
                      style={css.modalSelect}
                      textStyle={css.modalText}
                      renderRow={this._renderRowModal.bind(this)}
                      onSelect={(index, value) => this.setState({courseLanguage: value})}
                      />
                  </View>
                </View>
                <View style={{ width: '90%', marginTop: 20, marginBottom: 20 }}>
                  <View style={{ flexDirection: 'row' }}>
                    <Text style={{ fontSize: 18 }}>{I18n.t('setPrice')}:</Text>
                    {/* <Text style={{ marginLeft: 5, fontSize: 25 }}>{'$' + this.state.editPrice + ' /lesson'}</Text> */}
                    <Text style={css.intro}>{I18n.t('yourPrice') + ': '
            + accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * this.state.editPrice).toFixed(2))
            + this.getCurrencySymbol(UserSetting.CURRENCY)
            + I18n.t('min')}</Text>
                  </View>
                  <Slider
                    value={this.state.price}
                    // thumbStyle = {css.tractStyle}
                    // trackStyle = {css.tractStyle}
                    maximumValue={20}
                    step={1}
                    minimumTrackTintColor={Colors.darkGreen}
                    thumbTintColor={Colors.darkGreen}
                    onValueChange={(editPrice) => this.setState({ editPrice })} />

                </View>
                <View style={{ width: '90%', marginBottom: 10 }}>
                  <Text style={{ fontSize: 18, marginBottom: 10 }}>{I18n.t('introduction')} :</Text>
                  <View style={{
                    height: 150, width: '100%',
                    borderColor: 'lightgrey',
                    borderWidth: 1,
                    padding: 10
                  }}>
                    <TextInput underlineColorAndroid='white' multiline={true}
                      value={this.state.intro}
                      onChangeText={(intro) => this.setState({ intro })}></TextInput>
                  </View>
                </View>
                <View style={{ flexDirection: 'row' }}>
                  <TouchableOpacity
                    style={css.addButton}
                    onPress={() => {
                      this.setModalVisible(!this.state.modalVisible)
                    }}>
                    <Text style={css.addButtonText}>Cancel</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={css.addButton}
                    onPress={() => {
                      const course = {
                        id: this.state.courseId,
                        title: this.state.courseName,
                        introduction: this.state.intro,
                        language: this.state.language,
                        price: this.state.editPrice
                      }
                      this._editCourse(course);
                      this.setState({
                        price: this.state.editPrice
                      })
                      this.setModalVisible(!this.state.modalVisible)
                    }}>
                    <Text style={css.addButtonText}>Confirm</Text>
                  </TouchableOpacity>

                </View>
              </View>
            </Modal>
            <TouchableOpacity
              onPress={() => {
                this.setState({
                  courseName: this.state.courseName, language: this.state.language, intro: this.state.intro, price: this.state.price
                })
                this.setModalVisible(!this.state.modalVisible)
              }}
            >
              <Icon
                size={30}
                name='border-color'
                color={Colors.blueTwitter}
              />
            </TouchableOpacity>
            <Text>{I18n.t('editCourse')}</Text>
          </View>
          <Text style={css.titles}>{I18n.t('introduction')}</Text>
          <Text style={css.intro}>{this.state.intro}</Text>
          <Text style={css.language}>{this.state.language}</Text>
          <Text style={css.titles}>{I18n.t('courseType') + ': '}</Text><Text style={css.intro}> {this.state.structure}</Text>
          {/* <Text style={css.intro}>{I18n.t('yourPrice') + ': ' + '$' + this.state.price + ' /lesson'}</Text> */}
          <Text style={css.titles}>{I18n.t('yourPrice') + ': '
            + accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * this.state.editPrice).toFixed(2))
            + this.getCurrencySymbol(UserSetting.CURRENCY)
            + I18n.t('min')}</Text>
        </View>
        <ChapterList style={{ padding: 20 }} navigation={this.props.navigation} editable={true} courseInfo={this.props.navigation.state.params} />
        <View style={{ alignItems: 'center' }}>
          <Button
            buttonStyle={css.button}
            textStyle={{ fontSize: 15 }}
            onPress={() => { this._deleteCourse(this.state.courseId) }}
            title={I18n.t('deleteCourse')} />
        </View>
      </ScrollView>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.whiteColor,

  },
  header: {
    textAlign: 'center',
    fontSize: 28,
    marginBottom: 10,
    color: Colors.blackColor
  },
  search: {
    width: '95%', flexDirection: 'row', alignItems: 'center',
    padding: 10,
  },
  headerModel: {
    fontSize: 20,
    color: 'white',
    fontWeight: 'bold',
    // backgroundColor:'red',
  },
  searchView: {
    alignItems: 'center',
    flexDirection: 'row',
    width: 250,
    borderColor: 'lightgrey',
    // borderWidth: 1,
    marginLeft: 20
  },
  headerView: {
    height: 60,
    backgroundColor: Colors.darkGreen,
    width: '100%',
    paddingVertical: 10,
    padding: 20,
    justifyContent: 'center',
    marginBottom: 20
  },
  modalStyle: {
    backgroundColor: 'white',
    borderRadius: 5,
    alignItems: 'center',
    // justifyContent: 'center',
    height: '100%'

  },
  addButton: {
    backgroundColor: Colors.darkGreen,
    width: 120,
    alignSelf: 'flex-end',
    margin: 10,
    padding: 5,
    borderRadius: 5,
  },
  addButtonText: {
    color: '#fff',
    alignSelf: 'center'
  },
  language: {
    backgroundColor: Colors.darkGreen,
    width: 100,
    textAlign: 'center',
    borderRadius: 10,
    color: 'white',
    padding: 5,
    marginBottom: 10,
    marginTop: 10
  },
  title: {
    marginTop: 10,
    fontSize: 20,
    fontWeight: 'bold',
    alignSelf: 'center',
    color: '#222222'
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
    marginBottom: 30,
    paddingLeft: 20,
    paddingRight: 20
  },
  titles: {
    fontSize: 20,
    fontWeight:'bold'
  },
  subContent: {
    marginLeft: 20
  },
  intro: {
    marginLeft: 5,
    fontSize: 20
  },

  button: {
    marginTop: 30,
    width: 200,
    backgroundColor: Colors.darkGreen,
    borderRadius: 5
  },
   modalSelect: {
    backgroundColor: Colors.transparent,
    // marginBottom: 10,
    borderColor: Colors.darkGreen,
    borderBottomWidth: 1
  },
  modalText: {
    color: Colors.grayColor
  },
  modalDropdown: {
    height: 35  
  },
  modalDropdownText: {
    fontSize: 18
  },
})

export default CourseInfoScreen;