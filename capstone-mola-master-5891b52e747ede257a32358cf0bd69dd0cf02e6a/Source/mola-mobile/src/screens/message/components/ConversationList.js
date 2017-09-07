import React, {Component} from 'react';
import {FlatList, TouchableOpacity, Image, View, Text, ActivityIndicator} from 'react-native';
import {List, ListItem} from 'react-native-elements';
import {connect} from 'react-redux';
import {NavigationActions} from 'react-navigation';
import moment from 'moment';
import Colors from '../../../../constants/Colors';
import I18n from '../../../../constants/locales/i18n';

import firebaseApp from '../../../components/firebase/FirebaseClient';
import UserAPI from '../../../apis/user';
const userAPI = new UserAPI();

const mapStateToProps = (state) => ({
  user: state.auth.user.data.data
}); 

@connect(mapStateToProps) 
class ConversationList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      emptyMessage: 0,
      refresh: true,
      loading: true,
      conversations: [],
      conv: [],
      friends: [],
    };
    this.conversations = firebaseApp.database().ref('conversations');
    this.onRefresh = this.onRefresh.bind(this);
  }

  static navigationOptions = ({ ...props }) => {
    return {
      title: I18n.t('message'),
      headerTintColor: Colors.whiteColor,
      headerStyle: {
        backgroundColor: Colors.darkGreen,
        
      },
    }
  };  
  
  async handleReceiveConvesation(snap, myid){
    
    const conv = snap.val();
    const key = snap.key;
    
    let username = null;

    if (conv.user_one === myid && conv.user_two !== myid) {
      username = conv.user_two;
    } else if ((conv.user_one !== myid && conv.user_two === myid)) {
      username = conv.user_one;
    }

    // debugger
    if (username !== null) {
      const friendsInfo = await userAPI.getUserProfile({username});
      const { conversations } = this.state;
      const conversation = Object.assign({}, {key}, {
        userInfo: {...friendsInfo},
        conversation: conv
      });
      
      this.setState({
        conversations: [...this.state.conversations, conversation],
      })
    }  
  }
  onRefresh(){
    this.setState({ refresh: true });
  }
  componentWillMount() {
    this.onRefresh();
  }
  componentDidMount() {
    const { user } = this.props.user;
    this.conversations.orderByKey().on('child_added', async snap => {
      await this.handleReceiveConvesation(snap, user.username);
      await this.setState({loading: false});
    });
    

  }
  

  goToChatScreen(chat) {
    this.props.navigation.navigate('MessageDetail', {conversation: chat, refresh: this.onRefresh});
  }

  renderItem({item, index}) {
    return (
      <ListItem
        onPress={this.goToChatScreen.bind(this, item)}
        roundAvatar
        title={`${item.userInfo.firstName} ${item.userInfo.lastName}`}
        subtitle={item.conversation.lastMessage}
        rightTitle={moment(item.conversation.updateAt).format('hh:mm A')}
        avatar={{
          uri: item.userInfo.avatar
        }}
        containerStyle={{
        borderBottomWidth: 0
      }}/>
    );
  }

  render() {
    const { loading } = this.state;
    return (
      loading
      ? <ActivityIndicator size='large' color='green' style={{marginTop: 30}}/>
      : this.state.conversations.length > 0
      ? <List
          containerStyle={{
          borderTopWidth: 0,
          borderBottomWidth: 0
        }}>
          <FlatList
            data={this.state.conversations}
            renderItem={this.renderItem.bind(this)}
            keyExtractor={item => item.key}
            ListFooterComponent={this.renderFooter}/>
        </List>
      : <Text>No message</Text>
    );
  }
}

export default ConversationList;

const styles = {
    container: {
        flex: 1
    },
    containerIndicator: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignItems: 'center',
        height: 100,
        borderBottomWidth: 1,
        borderBottomColor: '#000'
    },
    avatar: {
        width: 100,
        height: 100,
        borderWidth: 1
    },
    name: {
        fontSize: 18,
        paddingLeft: 15,
        color: '#000'
    }
};