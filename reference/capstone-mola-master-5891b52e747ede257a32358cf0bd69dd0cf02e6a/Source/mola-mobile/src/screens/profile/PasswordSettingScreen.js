import React, { Component } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, TextInput, Alert } from 'react-native';
import { SideMenu, List, ListItem, Icon, } from 'react-native-elements';
import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';
import UserAPI from '../../apis/user';
let self;
class PasswordSettingScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            newPassword: '',
            confirm: '',
        }
        this._updatePassword = this._updatePassword.bind(this);
        self = this;
    }
    static navigationOptions = ({
    ...props
  }) => ({
            headerTintColor: '#ffffff',
            headerTitle: I18n.t('passwordSetting'),
            headerRight: <TouchableOpacity
                onPress={() => {
                    self._checkPassword({
                        password: self.state.password,
                        newPassword: self.state.newPassword,
                        confirm: self.state.confirm,
                    })
                }
                }
            >
                <Icon name='done' color={Colors.whiteColor} size={30} containerStyle={{ marginRight: 30 }} /></TouchableOpacity>,
            headerStyle: {
                backgroundColor: Colors.darkGreen,
            },
        });
    _goToScreen(screenName) {
        this.props.navigation.navigate(screenName);
    }
    async _updatePassword({ password, newPassword }) {
        const userAPI = new UserAPI();
        const data = await userAPI.updatePassword({ password, newPassword });
        if (data.status === 'ok') {
           return this.props.navigation.goBack();
        } else {
            Alert.alert(
                'Try again',
                null,
                [
                    { text: 'OK' }
                ],
                {
                    cancelable: false
                }
            )
        }
    }
    _checkPassword(passwordInfo) {
        if (passwordInfo.newPassword.length < 6) {

            return Alert.alert(
                'Password must be more than 5 characters !',
                null,
                [
                    { text: 'Cancel' }
                ],
                {
                    cancelable: false
                }
            )
        } else
            if (passwordInfo.newPassword !== passwordInfo.confirm) {
                return Alert.alert(
                    'Confirm password not match !. Retype',
                    null,
                    [
                        { text: 'Cancel' }
                    ],
                    {
                        cancelable: false
                    }
                )
            } else {
                this._updatePassword({
                    password: passwordInfo.password,
                    newPassword: passwordInfo.newPassword
                });
            }
    }
    render() {


        return (
            <View style={css.container}>
                <Text style={css.text}>{I18n.t('password')}</Text>
                <List containerStyle={{ marginBottom: 10 }}>
                    <ListItem
                        key={1}
                        hideChevron={true}
                        leftIcon={
                            <View style={css.input}>
                                <Icon
                                    name='lock'
                                    color={Colors.darkGreen}
                                    style={{
                                        marginRight: 20
                                    }} />
                                <TextInput
                                    secureTextEntry={true}
                                    style={css.textInput}
                                    underlineColorAndroid='transparent'
                                    placeholder={I18n.t('password')}
                                    placeholderTextColor='#98948F'
                                    onChangeText={(password) => this.setState({ password })}
                                ></TextInput>
                            </View>
                        }
                    />
                    <ListItem
                        key={2}
                        hideChevron={true}
                        leftIcon={
                            <View style={css.input}>
                                <Icon
                                    name='security'
                                    color={Colors.darkGreen}
                                    style={{
                                        marginRight: 20
                                    }} />
                                <TextInput
                                    secureTextEntry={true}
                                    style={css.textInput}
                                    underlineColorAndroid='transparent'
                                    placeholder={I18n.t('newPassword')}
                                    placeholderTextColor='#98948F'
                                    onChangeText={(newPassword) => this.setState({ newPassword })}
                                ></TextInput>
                            </View>
                        }
                    />
                    <ListItem
                        key={3}
                        hideChevron={true}
                        leftIcon={
                            <View style={css.input}>
                                <Icon
                                    name='security'
                                    color={Colors.darkGreen}
                                    style={{
                                        marginRight: 20
                                    }} />
                                <TextInput
                                    secureTextEntry={true}
                                    style={css.textInput}
                                    underlineColorAndroid='transparent'
                                    placeholder={I18n.t('confirmPass')}
                                    placeholderTextColor='#98948F'
                                    onChangeText={(confirm) => this.setState({ confirm })}></TextInput>
                            </View>
                        }
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
    },
    input: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    textInput: {
        width: 200,
        color: '#98948F',
        fontSize: 15
    },

})

export default PasswordSettingScreen;
