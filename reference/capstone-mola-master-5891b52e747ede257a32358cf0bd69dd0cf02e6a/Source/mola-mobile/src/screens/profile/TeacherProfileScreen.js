import React, { Component } from 'react';
import { View, Text, TextInput, ScrollView, WebView, Modal, ActivityIndicator, List, FlatList, StyleSheet, Image, TouchableOpacity } from 'react-native';
import { Icon } from 'react-native-elements';
import StarRating from 'react-native-star-rating';
import moment from 'moment';
import {connect} from 'react-redux';

import RatingAPI from '../../apis/rating';
import TeacherAPI from '../../apis/teacher';
import UserAPI from '../../apis/user';
import { IP_CLIP } from '../../apis/mola-api';
import { generateMessageConversationKey } from '../../services/utils';
import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';

const mapStateToProps = (state) => ({ 
  user: state.auth.user.data.data.user,
});
let self;
const userAPI = new UserAPI();
@connect(mapStateToProps)
class TeacherProfileScreen extends Component {
  constructor(props) {
    super(props);
    self = this;
  }
  state = {
    loading: true,
    totalRating: 0,
    rate:0,
    negativeRating: "6",
    neutralRating: "8",
    positiveRating: "22",
    starCount: 0,
    modalVisible: false,
    rating: [],
    extra: [],
    uri: "",
    learned: false,
    comment:'',
  }
  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('teacherInfo'),
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
      headerRight: <Icon 
                      size={25}
                      name='commenting'
                      type='font-awesome'
                      color={Colors.whiteColor}
                      containerStyle={{margin: 15}}
                      onPress={async() => {
                        const { teacher } = props.navigation.state.params;
                        const { user } = self.props;
                        
                        const teacherProfile = await userAPI.getUserProfile({username: teacher.username});
                        const conversation = {};
                        conversation.key = generateMessageConversationKey(teacher.username, user.username);
                        conversation.userInfo = Object.assign({}, teacherProfile);
                        
                        if (teacher.username <= user.username){
                          conversation.user_one = teacher.username;
                          conversation.user_two = user.username;
                        } else {
                          conversation.user_one = user.username;
                          conversation.user_two = teacher.username;
                        }
                        self._goToMessageDetailScreen(conversation);
                      }}/>
    });
    _goToMessageDetailScreen(chat){
      this.props.navigation.navigate('MessageDetail', {conversation: chat});
    }
  onStarRatingPress(rating) {
    this.setState({
      starCount: rating
    });
  }
  setModalVisible(visible) {
    this.setState({ modalVisible: visible });
  }

  async _addRating(teacherId){
    const  ratingTeacher = {
      teacherId: teacherId,
      comment: this.state.comment,
      rate: this.state.starCount
    }
    this.setState({loading:true})
    const ratingAPI = new RatingAPI();
    const data = await ratingAPI.addRatingTeacher(ratingTeacher);
    this.setModalVisible(false);
    await this.loadData();
  }

  async loadData(){    
    const { teacher } = this.props.navigation.state.params;
    const ratingAPI = new RatingAPI();
    const teacherAPI = new TeacherAPI();
    const extra = await teacherAPI.getTeacherExtra(teacher.id);
    const data = await ratingAPI.getRatingTeacher(teacher.id);
    this.setState({
      rating: data.data,
      extra: extra.data,
      loading: false,
      uri: extra.data.languageTeach[0].introClip,
      learned: extra.data.learned,
      totalRating: extra.data.numOfRate,   
      rate: extra.data.rating  
    })
  }

  async componentDidMount() {
    const { teacher } = this.props.navigation.state.params;
    const ratingAPI = new RatingAPI();
    const teacherAPI = new TeacherAPI();
    const extra = await teacherAPI.getTeacherExtra(teacher.id);
    const data = await ratingAPI.getRatingTeacher(teacher.id);
    this.setState({
      rating: data.data,
      extra: extra.data,
      loading: false,
      uri: extra.data.languageTeach[0].introClip,
      learned: extra.data.learned,
      totalRating: extra.data.numOfRate,  
      rate: extra.data.rating  
    })
  }
  render() {
    if (this.state.loading) {
      return (
        <ActivityIndicator
          size="large"
          style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }} />
      )
    }
    const { teacher, userInfo } = this.props.navigation.state.params;
    const uri = IP_CLIP + teacher.id;
    return (
      <ScrollView style={css.container}>
        <View style={[css.image, {maxHeight: 300, backgroundColor: Colors.blueTwitter}]}>
          <WebView
          
            source={{ uri: uri }}

          />
        </View>

        <View style={css.profileRegion}>
          <View>
            <Image
              style={css.avatar}
              source={{ uri: userInfo.avatar }} />
          </View>
          <View style={{ alignItems: 'center', marginLeft: 40 }}>
            <View style={{ marginBottom: 10 }}>
              <Text style={css.basicProfile}>{userInfo.firstName} {userInfo.lastName} - 25
            </Text>
            </View>
            <View style={{ marginBottom: 10, flexDirection:'row' }}>
              <StarRating
                disabled
                starColor={Colors.starColor}
                emptyStarColor={'gray'}
                starSize={18}
                maxStars={5}
                rating={this.state.rate}
              />
              <Text> ({this.state.totalRating})</Text>
            </View>
            <View style={{ flexDirection: 'row' }}>
              <View style={css.ratingArea}>
                <Text style={css.rating}>{this.state.extra.numCourse}</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <Icon name='book' color={'grey'} size={15} />
                  <Text style={{ fontSize: 15, marginLeft: 5 }}>{I18n.t('course')}</Text>
                </View>
              </View>
              <View style={[css.ratingArea, { marginLeft: 40 }]}>
                <Text style={css.rating}>{this.state.extra.numStudent}</Text>
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <Icon name='perm-identity' color={'grey'} size={17} />
                  <Text style={{ fontSize: 15, marginLeft: 5 }}>{I18n.t('student')}</Text>
                </View>
              </View>

            </View>
          </View>
        </View>
        <View style={css.regions}>
          <Text style={css.titles}>{I18n.t('teachIn')}</Text>
          <FlatList
            horizontal={true}
            data={this.state.extra.languageTeach}
            renderItem={({ item }) =>
              <Text style={css.language} key={item.language}>{item.language}</Text>
            }
          />
        </View>

        <View style={css.regions}>
          <Text style={css.titles}>{I18n.t('speak')}</Text>
          <FlatList
            horizontal={true}
            data={this.state.extra.languageSpeak}
            renderItem={({ item }) => <Text style={css.language} key={item}>{item}</Text>}
          />
        </View>

        <View style={css.regions}>
          <Text style={css.titles}>{I18n.t('about')}</Text>
          <Text>{teacher.introduction}</Text>
        </View>

        <View style={css.regions}>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <Text style={css.titles}>{I18n.t('experience')}   </Text>
            <Text style={{}}>{teacher.expYears} {I18n.t('year')}</Text>
          </View>
          <View style={css.rowView}>
            <Text>{teacher.expDescription}</Text>
          </View>
          <View>
            <Modal
              animationType={"slide"}
              transparent={true}
              onRequestClose={() => console.log("dcm")}
              visible={this.state.modalVisible}>
              <View style={{
                flex: 1,
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center'
              }}>

                <View style={css.modalStyle} >
                  <Text style={css.headerModel}>{I18n.t('ratingTeacher')}</Text>
                  <View style={{ width: '100%', marginBottom: 10, alignItems: 'center' }}>
                    <Image
                      style={css.avatar}
                      source={{ uri: userInfo.avatar }} />
                    <Text style={css.basicProfile}>{userInfo.firstName} {userInfo.lastName}</Text>
                    <StarRating
                      starColor={Colors.starColor}
                      emptyStarColor={'gray'}
                      starSize={25}
                      maxStars={5}
                      rating={this.state.starCount}
                      selectedStar={(rating) => this.onStarRatingPress(rating)}
                    />
                  </View>
                  <View style={{ width: '100%' }}>
                    <Text>{I18n.t('comment')}</Text>
                    <TextInput
                      onChangeText={(comment) => this.setState({ comment })}
                      value={this.state.comment}
                      style={{ marginLeft: 20, marginRight: 20 }}></TextInput>
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
                        this._addRating(teacher.id);
                        this.setModalVisible(!this.state.modalVisible)
                      }}>
                      <Text style={css.addButtonText}>Confirm</Text>
                    </TouchableOpacity>

                  </View>
                </View>
              </View>
            </Modal>
            {
              this.state.learned

                ? <TouchableOpacity onPress={() => {
                  this.setModalVisible(!this.state.modalVisible)
                }}>
                  <Text style={[css.language, {backgroundColor:'#00bfff'}]}>{I18n.t('rateNow')}</Text>
                </TouchableOpacity>

                : <View></View>
            }


          </View>

        </View>
        {
          this.state.rating.length > 0
            ? <View style={css.ratingRegions}>
              <Text style={css.titles}>{I18n.t('rating')}({this.state.rating.length})</Text>
              <TouchableOpacity onPress={() => this.props.navigation.navigate('Rating', { rating: this.state.rating })}>
                <View style={css.ratingView}>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 10 }}>
                    <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                      <Image
                        style={css.ratingAvatar}
                        source={{ uri: this.state.rating[0].user.avatar }} />
                      <View>
                        <Text style={{ marginBottom: 5 }}>{this.state.rating[0].user.firstName} {this.state.rating[0].user.lastName}</Text>
                        {/* <Text>Quốc tịch</Text> */}
                      </View>
                    </View>
                    <View style={{ marginRight: 10 }}>
                      <Text style={{ marginBottom: 5 }}> {moment(this.state.rating[0].ratingTeacher.time).format('DD-MM-YYYY')}</Text>
                      <StarRating
                        disabled
                        starColor={Colors.starColor}
                        emptyStarColor={'gray'}
                        starSize={18}
                        maxStars={5}
                        rating={this.state.rating[0].ratingTeacher.rating}
                      />
                    </View>
                  </View>
                  <View>
                    <Text>{this.state.rating[0].ratingTeacher.comment}</Text>
                  </View>
                </View>
              </TouchableOpacity>
            </View>
            : <View></View>

        }

      </ScrollView>
    );
  }
}

var css = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.whiteColor
  },
  ratingView: {
    borderRadius: 5,
    elevation: 1,
    // backgroundColor:'lightgray',
    padding: 15,
    marginTop: 10,
    marginBottom: 10
  },
  headerModel: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20
  },
  modalStyle: {
    width: 300, height: 350, backgroundColor: 'white',
    elevation: 15,
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20
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
  image: {
    width: '100%',
    height: 230,
    backgroundColor: 'white'
  },
  profileRegion: {
    alignItems: 'center',
    margin: 20,
    marginBottom: 5,
    flexDirection: 'row'
  },
  ratingAvatar: {
    width: 50,
    height: 50,
    borderRadius: 60,
    marginRight: 20
  },
  avatar: {
    width: 80,
    height: 80,
    borderRadius: 60,
    // marginRight: 40
  },
  rowView: {
    flexDirection: 'row',
    flex: 1,
  },
  basicProfile: {
    fontSize: 20,
    color: Colors.blackColor,
  },
  regions: {
    marginTop: 10,
    marginLeft: 10,
    marginRight: 10,
    paddingLeft: 10,
    paddingRight: 10
  },
  ratingRegions: {
    marginTop: 20,
    marginLeft: 10,
    marginRight: 10,
    paddingLeft: 10,
    paddingRight: 10
  },
  titles: {
    fontSize: 18,
    color: Colors.blackColor
  },
  language: {
    alignSelf: 'flex-start',
    backgroundColor: Colors.darkGreen,
    marginTop: 5,
    marginRight: 10,
    borderRadius: 4,
    padding: 10,
    color: Colors.whiteColor
  },
  ratingArea: {
    alignItems: 'center',
  },
  rating: {
    color: Colors.darkGreen,
    fontSize: 20,
    fontWeight: 'bold'
  }
})

export default TeacherProfileScreen;