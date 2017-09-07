import React, { Component } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert, AsyncStorage } from 'react-native';
import { SideMenu, List, ListItem, Icon, } from 'react-native-elements';
import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';
import LanguagesAPI from '../../apis/languages'
import {connect} from 'react-redux';
import {logout} from '../../screens/login/actions';
const mapStateToProps = (state) => ({ 
  user: state.auth.user.data.data.user,
});

@connect(mapStateToProps, {logout})
class SettingScreen extends Component {

    constructor(props) {
        super(props);
        this._logout = this._logout.bind(this);
        this.removeUserProfile = this.removeUserProfile.bind(this);
        this.state = {
            languageSpeak: [],
            languageLearn: []
        }
    }
    async removeUserProfile() {
        await AsyncStorage.removeItem('USER_TOKEN');
        await AsyncStorage.removeItem('USER');

    }
    async _logout() {
        await this.removeUserProfile();
        await this.props.logout();
        this.props.navigation.navigate('Login');
    }
    _confirmLogout() {
        Alert.alert(
            'Do you want to log out ?',
            null,
            [
                { text: 'Cancel' }, { text: 'OK', onPress: () => this._logout() }
            ],
            {
                cancelable: false
            }
        )
    }
    static navigationOptions = ({
    ...props
  }) => ({
            headerTintColor: '#ffffff',
            headerTitle: I18n.t('setting'),
            headerStyle: {
                backgroundColor: Colors.darkGreen,
            },
        });
    _goToScreen(screenName) {
        this.props.navigation.navigate(screenName);
    }

    // async componentWillMount() {
    //     const languagesAPI = new LanguagesAPI();
    //     const languages = await languagesAPI.getLanguageSpeakLearn();
    //     this.setState({
    //         languages
    //     })
    // }
    render() {
      const {user} = this.props;

        return (
            <View style={css.container}>
                <Text style={css.text}>{I18n.t('account')}</Text>
                <List containerStyle={{ marginBottom: 10 }}>
                    <ListItem
                        onPress={() => this._goToScreen('GeneralSetting')}
                        key={1}
                        title={I18n.t('generalSetting')}
                        leftIcon={{
                            name: 'account-box',
                            color: Colors.darkGreen,
                        }}
                    />
                  {user.isTeacher ? <ListItem
                      onPress={() => {
                          this._goToScreen('Revenue');
                      }}
                      key={2}
                      title={I18n.t('revenue')}
                      leftIcon={{
                          type: 'font-awesome',
                          name: 'money',
                          color: Colors.darkGreen,
                      }}
                  />:<View></View>}
                    <ListItem
                        onPress={() => {
                            this._goToScreen('RegisterLanguage');
                            //this.props.navigation.navigate('RegisterLanguage', this.state.languages);
                        }}
                        key={3}
                        title={I18n.t('languageSetting')}
                        leftIcon={{
                            name: 'language',
                            color: Colors.darkGreen,
                        }}
                    />
                    <ListItem
                        onPress={() => this._goToScreen('PasswordSetting')}
                        key={4}
                        title={I18n.t('passwordSetting')}
                        leftIcon={{
                            name: 'lock-open',
                            color: Colors.darkGreen,
                        }}
                    />
                </List>
                <Text style={css.text}>{I18n.t('logout')}</Text>
                <List containerStyle={{ marginBottom: 20 }}>
                    <ListItem
                        onPress={() => this._confirmLogout()}
                        key={5}
                        title={I18n.t('logoutSmall')}
                        leftIcon={{
                            name: 'power-settings-new',
                            color: Colors.darkGreen,
                        }}
                    />
                </List>
            </View>
        )
    }
}
const css = StyleSheet.create({
    container: {
        flex: 1,
        paddingTop: 10
    },
    text: {
        marginTop: 10,
        marginLeft: 10,
        fontSize: 18,
        fontWeight: 'bold'
    }

})

export default SettingScreen;
