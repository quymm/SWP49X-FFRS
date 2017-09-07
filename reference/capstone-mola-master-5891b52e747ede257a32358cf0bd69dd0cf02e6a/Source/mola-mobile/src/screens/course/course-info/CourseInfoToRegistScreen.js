import React, { Component } from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity, Alert, Modal, Dimensions } from 'react-native';
import { Icon, Button } from 'react-native-elements';
import StarRating from 'react-native-star-rating';

import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';
import ChapterListView from '../components/ChapterListView';
import CourseAPI from '../../../apis/course';
import { connect } from 'react-redux';
import { flags } from '../../../../constants/countries';
import { currencies } from '../../../../constants/currencies';
import UserSetting from '../../UserSetting';
import ExchangeRates from '../../ExchangeRates';
import accounting from '../../../services/accounting';
import RecomendationAPI from '../../../apis/recomendation';
import CardHorizental from '../../search/components/CardHorizental';

const courseAPI = new CourseAPI();
const recomendation = new RecomendationAPI();
var { height, width } = Dimensions.get('window');
const mapStateToProps = (state) => ({
  user: state.auth.user.data.data.user,
});

@connect(mapStateToProps)
class CourseInfoToRegistScreen extends Component {

  constructor(props) {
    super(props);
    this.state = {
      courseType: "",
      ratingCourse: 0,
      ratingTeacher: 0,
      enrollStatus: '',
      // enrollStatus: 1,
      modalVisible: false,
      displayPrice: null,
      recomendCourse: []
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




  async componentDidMount() {
    const { course, userInfo, teacher } = this.props.navigation.state.params;
    const canRegistCourse = teacher.username !== this.props.user.username;

    var structuredType;
    if (course.structured) {
      structuredType = I18n.t('structureCourse');
    } else {
      structuredType = I18n.t('unstructureCourse');
    }
    this.setState({
      courseType: structuredType,
      canRegistCourse
    });
    const recomendCourse = await recomendation.similarCourse(course.id);

    if (recomendCourse.status === 'ok') {
      await this.setState({ recomendCourse: recomendCourse.data });
    }
  }
  async componentWillMount() {
    const { course } = this.props.navigation.state.params;
    const courseId = course.id;
    const username = this.props.user.username;
    const data = await courseAPI.checkUserEnrollInCourse({ username, courseId });

    if (data.status === 'ok') {
      // if (!data.data === 'UNENROLLED') {
      //   this.setState({ enrollStatus: 1 });
      // } else {
      //   this.setState({ enrollStatus: -1 });
      // }
      this.setState({ enrollStatus: data.data });
    }

  }

  _countryFlag(name) {
    const index = flags.findIndex(flag => { return flag.name.toLowerCase() === name.toLowerCase() });
    return index >= 0 ? flags[index].flag : '';
  }

  getCurrencySymbol(currencyCode) {
    const index = currencies.findIndex(currency => {
      return currency.code === currencyCode;
    })
    return index >= 0 ? currencies[index].symbol : '';
  }

  onPressRegistCourse(visible) {
    const { course } = this.props.navigation.state.params;

    Alert.alert(
      `${course.title}`,
      I18n.t('confirmEnrollCourse'),
      [
        { text: I18n.t('cancel'), onPress: () => false, style: 'cancel' },
        {
          text: I18n.t('yes'), onPress: async () => {
            await this.setState({ enrollStatus: 'REGISTERING' });
            await this._enrollToCourse();
          }
        },
      ],
      { cancelable: false }
    )
    const { registerCourseStep } = this.state;
  }

  async _enrollToCourse() {
    const { course, userInfo, teacher } = this.props.navigation.state.params;
    const courseId = course.id;
    const username = this.props.user.username;
    const data = await courseAPI.enrollCourse({ username, courseId });
    if (data.status === 'ok') {
      await this.setState({ enrollStatus: data.data });
    }
  }

  setModalVisible(visible) {
    this.setState({ modalVisible: visible });
  }

  _renderUnEnroll() {
    console.log('render unenroll');
    const { enrollStatus } = this.state;
    return (
      <TouchableOpacity
        onPress={() => {
          this.onPressRegistCourse()
        }}>
        <View style={[
          //enrollStatus === -1 || enrollStatus === 0
          //? css.registerButton
          //: {}
          css.registerButton
        ]}>
          <Text style={{
            color: Colors.whiteColor,
            fontWeight: 'bold'
          }}>
            {
              enrollStatus === 'UNENROLLED'
                ? I18n.t('registerCourse')
                //: enrollStatus === 1
                //? I18n.t('registeredCourse')
                : I18n.t('registeringCourse')
            }
          </Text>
          <Icon
            size={35}
            name='add'
            color={'#C1EAF2'}
          />
        </View>
      </TouchableOpacity>
    );
  }
  _renderEnrolled() {
    console.log('render enrolled');
    const { enrollStatus } = this.state;
    return (
      <View>
        <View style={{ flexDirection: 'row', backgroundColor: '#00AAA0', height: 30, width: 120, alignItems: 'center', justifyContent: 'center' }}>
          <Text style={{
            color: Colors.lightGreen,
            fontWeight: 'bold',
          }}>
            {
              /* enrollStatus === -1
                ? I18n.t('registerCourse')
                : enrollStatus === 1
                  ? I18n.t('registeredCourse')
                  : I18n.t('registeringCourse') */
              enrollStatus === 'ACCEPTED' ? I18n.t('registeredCourse') : I18n.t('pendingCourse')
            }
          </Text>
          <Icon
            size={35}
            name='check'
            color={Colors.lightGreen}
          />
        </View>
        <Modal
          animationType={"slide"}
          transparent={true}
          onRequestClose={() => console.log("modal close")}
          visible={this.state.modalVisible}
        >
          <View style={{
            flex: 1,
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center'
          }}>

            <View style={css.modalStyle} >
              <Text style={css.headerModal}>Select lesson</Text>



              <View style={{ flexDirection: 'row' }}>
                <TouchableOpacity
                  style={css.addButton}
                  onPress={() => {
                    this.setState({ modalVisible: false })
                  }}>
                  <Text style={css.addButtonText}>Cancel</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={css.addButton}
                  onPress={() => {
                    this.setState({ modalVisible: false })

                  }}>
                  <Text style={css.addButtonText}>Confirm</Text>
                </TouchableOpacity>

              </View>

            </View>

          </View>
        </Modal>
      </View>
    );
  }
  render() {
    const { course, userInfo, teacher } = this.props.navigation.state.params;
    const { enrollStatus, recomendCourse } = this.state;
    return (
      <ScrollView style={css.container}>
        <View>
          <Image style={{ height: 250, padding: 10, backgroundColor: 'rgba(0, 0, 0, 0.5)' }}
            resizeMode='cover'
            source={{ uri: course.cover }}
          >
            <View style={{ height: 250, width: width, zIndex: 5, position: 'absolute', backgroundColor: 'rgba(0,0,0,0.5)' }}>

            </View>
            <View style={{ flexDirection: 'row', flex: 1, alignItems: 'center', zIndex: 10 }}>
              <TouchableOpacity style={{ flex: 1 }} onPress={() => this.props.navigation.navigate('TeacherProfile', { teacher: teacher, userInfo: userInfo })}>
                <Image style={css.avatar} source={{ uri: userInfo.avatar }} />
              </TouchableOpacity>
              <View style={css.contentView}>
                <Text style={css.header} numberOfLines={2}>{course.title}</Text>

                {/* <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <StarRating
                    disabled
                    starColor={Colors.starColor}
                    emptyStarColor={Colors.whiteColor}
                    starSize={18}
                    maxStars={5}
                    rating={course.rating}
                  />
                  <Text style={{ color: 'white' }}>{`(${course.numOfRate})`}</Text>
                </View> */}
              </View>
            </View>

            <View style={{ flexDirection: 'row', marginLeft: 20, zIndex: 10 }}>
              <View style={{ flex: 1 }}>
                <Text style={css.teacherName}>{`${userInfo.firstName} ${userInfo.lastName}`}</Text>
                <Text style={css.language}>{`${this._countryFlag(course.language)} ${course.language}`}</Text>
              </View>
              {this.state.enrollStatus === '' || !this.state.canRegistCourse ? <View></View> :
                <View style={{ flex: 1, alignItems: 'center' }}>
                  {enrollStatus === 'UNENROLLED' || enrollStatus === 'REGISTERING'
                    ? this._renderUnEnroll()
                    : this._renderEnrolled()
                  }
                </View>
              }
            </View>
          </Image>
        </View>
        <View style={css.headerView}>
          <Text style={css.titles}>{I18n.t('introduction')}</Text>
          <Text style={css.intro}>{course.introduction}</Text>
          <Text style={css.titles}>{I18n.t('courseType') + ': ' }</Text><Text style={css.intro}> {this.state.courseType}</Text>
          <Text style={css.titles}>{I18n.t('yourPrice') + ': '
            + accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * course.price).toFixed(2))
            + this.getCurrencySymbol(UserSetting.CURRENCY)
            + I18n.t('min')}</Text>
        </View>
        <ChapterListView style={{ padding: 20, marginBottom:0 }} navigation={this.props.navigation} courseId={course.id} teacher={teacher} coursePrice={course.price} isRegisted={enrollStatus === 'ACCEPTED'} />

        <Text style={[css.titles,{marginLeft:20}]}>{I18n.t('relatedCourse')}</Text>
         <ScrollView horizontal={true} style={{}}>
          {
            recomendCourse &&
            recomendCourse.map((recomend, i) => {
              return <CardHorizental key={i} style={{ height:400 }} recomend={recomend} navigation={this.props.navigation} />
            })
          }
        </ScrollView> 
      </ScrollView>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.whiteColor,
  },
  contentView: {
    alignItems: 'center',
    flex: 2
  },
  header: {
    textAlign: 'center',
    fontWeight: '700',
    fontSize: 18,
    marginBottom: 10,
    color: Colors.whiteColor,
    marginTop: 10
  },
  search: {
    width: '95%', flexDirection: 'row', alignItems: 'center',
    padding: 10,
  },

  language: {
    backgroundColor: 'rgba(10, 0, 0, 0.1)',
    width: 100,
    textAlign: 'center',
    //borderRadius: 10,
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
  headerView: {
    padding: 20
  },
  avatarArea: {
    flex: 1,
    alignItems: 'center'
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 100,
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
    fontSize: 18
  },

  button: {
    marginTop: 30,
    width: 200,
    backgroundColor: Colors.darkGreen,
    borderRadius: 5
  },
  teacherName: {

    fontSize: 17,
    fontWeight: 'bold',
    color: 'white',
    // textShadowOffset: { width: 1, height: 1 },
    // textShadowColor: Colors.darkGreen
  },
  registerButton: {
    flexDirection: 'row',
    backgroundColor: '#00AAA0',
    width: 150,
    height: 40,
    borderRadius: 5,
    marginLeft: 30,
    alignItems: 'center',
    justifyContent: 'center',
  },
  registeredButton: {
    flexDirection: 'row',
    backgroundColor: Colors.lightCloudColor,
    width: 150,
    height: 63,
    borderRadius: 10,
    marginLeft: 30,
    alignItems: 'center',
    justifyContent: 'center',
  },
  modalStyle: {
    width: 350, height: 500, backgroundColor: 'white',
    elevation: 15,
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20
  },
  headerModal: {
    fontSize: 20,
    fontWeight: 'bold',
    margin: 20
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
})
export default CourseInfoToRegistScreen;