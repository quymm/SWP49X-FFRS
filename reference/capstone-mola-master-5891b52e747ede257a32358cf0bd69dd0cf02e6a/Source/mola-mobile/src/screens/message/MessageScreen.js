import React, {Component} from 'react';
import {View, Text, AsyncStorage, ActivityIndicator, TouchableOpacity} from 'react-native';
import { GiftedChat } from 'react-native-gifted-chat';
import { Icon } from 'react-native-elements';
import { connect } from 'react-redux';

import firebaseApp from '../../components/firebase/FirebaseClient';

import ButtonGroupHeader from './components/ButtonGroupHeader';

import I18n from '../../../constants/locales/i18n';

import Colors from '../../../constants/Colors';


const mapStateToProps = (state) => ({ 
  user: state.auth.user.data.data.user,
});
let self;
@connect(mapStateToProps)
class MessageScreen extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      user_id: '',
      messages: [],
      conversation: null,
    };
    self = this;
    this.onSend = this.onSend.bind(this);
  }
  static navigationOptions = ({...props }) => {
    const { conversation } = props.navigation.state.params;
    return {
      title: `${conversation.userInfo.firstName} ${conversation.userInfo.lastName}`,
      headerTintColor: Colors.whiteColor,
      headerStyle: {
        backgroundColor: Colors.darkGreen,
      },
      headerLeft: <Icon name='arrow-back'
      color={Colors.whiteColor}
                    containerStyle={{marginLeft: 10}}
                    onPress={() => self.onPressBack()}
                    />
                  
      
    }
  };
  onPressBack(){
    const { refresh } = this.props.navigation.state.params;
    if(typeof refresh === 'function'){
      refresh();
    }
    this.props.navigation.goBack();
  }
  async componentWillMount() {
    const user_id = this.props.user.username;
    this.setState({ user_id });
  }

  getRef() {
    return firebaseApp.database().ref();
  }

  async componentDidMount() {
    const { conversation } = this.props.navigation.state.params;
    await this.setState({ conversation });

    const chatId = conversation.key;
    this.conversationKey = this.getRef().child(`conversations/${conversation.key}`);
    this.chatRef = this.getRef().child(`chat/${chatId}`);
    this.chatRefData = this.chatRef.orderByChild('order');

    this.listenForItems(this.chatRefData);
  }
  componentWillUnmount() {
    this.chatRefData.off()
  }

  

  listenForItems(chatRef) {
    chatRef.on('value', (snap) => {
      // get children as an array
      var messages = [];
      snap.forEach((message) => {
        const isSelf = message.val().uid == this.state.user_id
        const name = isSelf
          ? this.state.user_id
          : this.state.conversation.userInfo.username;
        const avatar = isSelf
          ? ''
          : this.state.conversation.userInfo.avatar;

        messages.push({
          _id: message.val().createdAt,
          text: message.val().text,
          createdAt: new Date(message.val().createdAt),
          user: {
            _id: message.val().uid,
            avatar: avatar,
          }
        });
      });

      this.setState({loading: false, messages: messages})

    });
  }

  onSend(messages = []) {
    let { conversation } = this.state || this.state.conversation;
    
    messages.forEach(message => {
      var now = new Date().getTime();
      this.chatRef
        .push({
          _id: now,
          text: message.text,
          createdAt: now,
          uid: this.state.user_id,
          order: -1 * now
        });
      this.conversationKey.update({
        lastMessage: message.text,
        updateAt: now,
        user_one: conversation.user_one || conversation.conversation.user_one,
        user_two: conversation.user_two || conversation.conversation.user_two,
      })
    });
  }

  render() {
    const { loading } = this.state;

    return (
      loading
      ? <ActivityIndicator size='large' color='green' style={{marginTop: 30}}/>
      : <GiftedChat
          messages={this.state.messages}
          onSend={this.onSend}
          user={{
          _id: this.state.user_id
      }}/>
    );
  }
}

export default MessageScreen;