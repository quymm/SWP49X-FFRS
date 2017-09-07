import React, { Component } from 'react';
import {
  View, Text, ScrollView, StyleSheet,
  Easing, Image, Animated, TouchableOpacity
} from 'react-native';
import { Icon } from 'react-native-elements';
import { connect } from 'react-redux';
import { getUserProfile } from './actions';
import UserAPI from '../../apis/user';
import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';
import {generateMessageConversationKey} from '../../services/utils';
const userAPI = new UserAPI();
const mapStateToProps = (state) => ({ 
  userProfile: state.profile.profile,
  user: state.auth.user.data.data.user,
});
let self;
@connect(mapStateToProps, { getUserProfile })
class UserProfileScreen extends Component {
  constructor(props) {
    super();
    self = this;
  }
  state = {
    firstName: "Eric",
    lastName: "Anderson",
    about: "Blah blah blah...\nBlah blah blah... Really long lineeeeeeeeeeeeeeeeeee eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee hihihi",
    userProfile: {},
  }
  static navigationOptions = ({
    ...props
  }) => ({
      headerTintColor: '#ffffff',
      headerTitle: I18n.t('learnerInfo'),
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
      headerRight: <Icon 
                      size={25}
                      name='commenting'
                      type='font-awesome'
                      color={Colors.whiteColor}
                      onPress={async() => {
                        const { session } = props.navigation.state.params;
                        const { user } = self.props;
                        
                        const userProfile = await userAPI.getUserProfile({username: session.learner.username});
                        const conversation = {};
                        conversation.key = generateMessageConversationKey(session.learner.username, user.username);
                        conversation.userInfo = Object.assign({}, userProfile);
                        
                        if (session.learner.username <= user.username){
                          conversation.user_one = session.learner.username;
                          conversation.user_two = user.username;
                        } else {
                          conversation.user_one = user.username;
                          conversation.user_two = session.learner.username;
                        }
                        self._goToMessageDetailScreen(conversation);
                      }}/>
    });
  _goToMessageDetailScreen(chat){
      this.props.navigation.navigate('MessageDetail', {conversation: chat});
    }
  render() {
    const { session } = this.props.navigation.state.params;

    return (
      <ScrollView style={css.container}>
        <View>
          <Image
            blurRadius={5}
            style={css.image}
            source={{ uri: session.learner.avatar }} />
        </View>
        <View>
          <Image
            style={css.avatar}
            source={{ uri: session.learner.avatar }} />
        </View>
        <View style={css.basicInfoRegion}>
          <View style={css.basicInfo}>
            <Text style={css.userName}>{session.learner.firstName} {session.learner.lastName}</Text>
            <Text>Member since November, 2015</Text>
          </View>
          <View style={css.availability}>
            <View style={css.avaiText}>
              <Text style={{ color: Colors.whiteColor }}>Online</Text>
            </View>
          </View>
        </View>

        <View style={css.regions}>
          <Text style={css.titles}>About {session.learner.firstName}</Text>
          <Text>{session.learner.email}</Text>
        </View>

        <View style={css.regions}>
          <Text style={css.titles}>Languages</Text>
          <View style={css.languageSeperator}>
            <View style={css.languageColumn}>
              <Text>Learning</Text>
              <Text style={css.language}>Spanish</Text>
              <Text style={css.language}>Portugese</Text>
              <Text style={css.language}>Arabian</Text>
            </View>
            <View style={css.languageColumn}>
              <Text>Speaking</Text>
              <Text style={css.language}>English</Text>
              <Text style={css.language}>French</Text>
            </View>

          </View>
        </View>

      </ScrollView>
    );
  }
}

const css = StyleSheet.create({
  container: {
    backgroundColor: Colors.whiteColor,
    flex: 1,
    paddingBottom: 5
  },
  avatar: {
    height: 100,
    width: 100,
    borderRadius: 100,
    position: 'absolute',
    top: -50,
    left: '50%',
    transform: [{ translateX: -50 }]
  },
  image: {
    width: '100%',
    height: 250
  },
  basicInfoRegion: {
    flexDirection: 'row',
    marginTop: 50,
    marginLeft: 10,
    paddingLeft: 10
  },
  basicInfo: {
    flex: 3,
    justifyContent: 'center'
  },
  availability: {
    flex: 1,
    alignItems: 'center'
  },
  userName: {
    fontSize: 24,
    color: Colors.blackColor
  },
  avaiText: {
    backgroundColor: Colors.darkGreen,
    justifyContent: 'center',
    alignItems: 'center',
    height: 50,
    width: 50,
    borderRadius: 50
  },
  regions: {
    marginRight: 10,
    marginTop: 20,
    marginLeft: 10,
    paddingLeft: 10
  },
  titles: {
    fontSize: 20,
    color: Colors.blackColor
  },
  languagesRegion: {
    marginRight: 10,
    marginTop: 30,
    marginLeft: 10,
    paddingLeft: 10,
    alignItems: 'center'
  },
  languageSeperator: {
    flexDirection: 'row'
  },
  languageColumn: {
    flex: 1,
    // alignItems: 'center',
  },
  language: {
    alignSelf: 'flex-start',
    backgroundColor: Colors.darkGreen,
    marginTop: 5,
    paddingHorizontal: 8,
    borderRadius: 4,
    color: Colors.whiteColor
  },
  // interest: {
  //   flex:1,
  //   marginTop: 5,
  //   marginLeft: 5,
  //   paddingHorizontal: 8,
  //   paddingVertical: 3,
  //   borderRadius: 8,
  //   color: Colors.redColor,
  //   borderColor: Colors.redColor,
  //   borderWidth: 1
  // },
})

export default UserProfileScreen;