import mola from './mola-api';
import qs from 'qs';
import { AsyncStorage } from 'react-native'
class UserAPI {
  constructor() {}
  updateUserProfile(profile){
    
  }
  async reloginWithToken({username, token}){
    const data = {
      username,
    };
    let user;
    await mola
      .post('/api/relogin', qs.stringify(data), {
            headers: {
                'Authorization': token
            }
        })
      .then(json => {
        
        user = json.data;
      })
      .catch(err => {
        throw err
      });
    return user;
  }
  async loginUsernamePassword({username, password}) {
    const data = {
      username,
      password
    }
    let user;
    const res = await mola
      .post('/api/login', qs.stringify(data))
      .then(json => {
        user = json.data;
      })
      .catch(err => {
        debugger
        throw err
      });
      debugger
    return user;
  }
  async updatePassword({password, newPassword}){
    const data = {
      password,
      newPassword
    }
    let isSuccess;
    const token = await AsyncStorage.getItem('USER_TOKEN');
    await mola.post('/api/user/password', qs.stringify(data),{
            headers: {
                'Authorization': token
            }
        }).then(json => {
      isSuccess = json.data;
    }).catch(err => {
      throw err
    });
    return isSuccess;
  }
  async loginWithFacebook({accessToken}) {
    let user;
    await mola
      .post('/api/login/fb', qs.stringify(accessToken))
      .then(json => {
        user = json.data;
      })
      .catch(err => { throw err});
      return user;
  }
  async registerUsingRegisterApi({
    username,
    password,
    email,
    firstName,
    lastName
  }) {
    const data = {
      username,
      password,
      email,
      firstName,
      lastName
    }

    let user;
    await mola
      .post('/api.v1/register', qs.stringify(data))
      .then(json => {
        user = json.data;
      })
      .catch(err => {
        console.log('err here');
        user = err.response.data
      });
    return user;
  }

  async getUserProfile({username}) {
    let userProfile;
    await mola
      .get('/api.v1/profile/' + username)
      .then(json => {
        userProfile = json.data;
      });
    return userProfile;
    // return data;
  }

  async findUsers(usernames = []) {
    let users = [];
    await mola
      .get('/api/find-users?u='+ usernames.join(','))
      .then(json => {
        users = json.data;
        debugger
      });
    return users;
  }

  async updateAvatar(avatar) {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.post('/api/avatar', avatar, {
      headers: {
        'Authorization': token
      }
    }).then(result => {
      return result;
    });
    return data;
  }

  async editUser(user) {
    const token = await AsyncStorage.getItem('USER_TOKEN');
    const {data} = await mola.post('/api/user/edit', qs.stringify(user), {
      headers: {
        'Authorization': token
      }
    }).then(result => {
      return result;
    });
    return data;
  }

  

}
export default UserAPI;