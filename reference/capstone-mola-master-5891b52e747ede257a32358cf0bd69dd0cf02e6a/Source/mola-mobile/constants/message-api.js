import axios from 'axios';

import {message_groups, messages_ids} from '../data/message-list';
import firebaseApp from '../src/components/firebase/FirebaseClient';

axios.defaults.baseURL = 'url-backend-api';
export class MessageAPI {
  constructor() {}

  async getRecentConversation() {

    const chatRef = firebaseApp.database().ref('chat');
    console.log('chatref', chatRef.orderByKey().on('child_added', snap => console.log(snap.key))
    );
    return message_groups.filter(group => messages_ids.indexOf(group._id) >= 0);
  }

  async getListConversationId() {
    return messages_ids;
  }

}
