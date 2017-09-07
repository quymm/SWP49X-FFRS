import React, { Component } from 'react';
import { View, Text, StyleSheet, TextInput, TouchableOpacity, Image, ScrollView, AsyncStorage, Alert, Picker } from 'react-native';
import { SideMenu, List, ListItem, Icon, } from 'react-native-elements';
import Colors from '../../../constants/Colors';
// import { currencies } from '../../../constants/currencies';
import { countriesArray, appLanguages } from '../../../constants/countries';
import I18n from '../../../constants/locales/i18n';

import ImagePicker from 'react-native-image-picker';
import UserAPI from '../../apis/user';
import { uploads } from '../../apis/uploads';
import RNFetchBlob from 'react-native-fetch-blob';
import UserSetting from '../UserSetting';
import {connect} from 'react-redux';

let self;

const mapStateToProps = (state) => ({
  user: state.auth.user.data.data.user,
});

@connect(mapStateToProps)
class GeneralSettingScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {},
            birthday: '',
            avatarSource: null,
            error: '',
            country: UserSetting.COUNTRY,
            timeZone : UserSetting.TIMEZONE,
            currency: UserSetting.CURRENCY,
            language: UserSetting.APP_LANGUAGE
        };
        this.selectPhotoTapped = this.selectPhotoTapped.bind(this);
        this.loadAppSetting = this.loadAppSetting.bind(this);
        self = this;
    }
    static navigationOptions = ({
    ...props
  }) => ({
            headerTintColor: '#ffffff',
            headerTitle: I18n.t('generalSetting'),
            headerRight: <TouchableOpacity onPress={() => self.onDonePress()}>
                <Icon name='done' color={Colors.whiteColor} size={30} containerStyle={{ marginRight: 30 }} /></TouchableOpacity>,
            headerStyle: {
                backgroundColor: Colors.darkGreen,
            },
        });
    async componentWillMount() {
        const userAPI = new UserAPI();
        const {username} = this.props.user;
        const userProfile = await userAPI.getUserProfile({username});
        const user = Object.assign({}, userProfile);
        await this.setState({ user });
    }

    loadAppSetting(){
        this.setState({
            country: UserSetting.COUNTRY,
            timeZone : UserSetting.TIMEZONE,
            currency: UserSetting.CURRENCY,
            language: UserSetting.APP_LANGUAGE
        })
    }

    selectPhotoTapped() {
        const options = {
            quality: 0.7,
            maxWidth: 500,
            maxHeight: 500,
            noData: true,
            storageOptions: {
                skipBackup: true,
                cameraRoll: true
            },
            takePhotoButtonTitle: I18n.t('aboutyou_take_photo'),
            cancelButtonTitle: I18n.t('aboutyou_cancel_take_photo'),
            chooseFromLibraryButtonTitle: I18n.t('aboutyou_choose_from_library'),
            permissionDenied: {
                title: I18n.t('aboutyou_permission_denied')
            }

        };

        ImagePicker.showImagePicker(options, (response) => {
            console.log('Response = ', response);
            if (response.didCancel) {
                console.log('User cancelled photo picker');
            } else if (response.error) {
                console.log('ImagePicker Error: ', response.error);
            } else if (response.customButton) {
                console.log('User tapped custom button: ', response.customButton);
            } else {
                let source = {
                    uri: response.uri
                };

                this.setState({ avatarSource: source.uri });
            }
        });
    }

    async onDonePress() {
      
        const userAPI = new UserAPI();
        const updateResult = await userAPI.editUser(this.state.user);
        
        if (updateResult.status === 'ok') {
            await AsyncStorage.setItem('USER', JSON.stringify(updateResult.data));
            if (this.state.avatarSource) {
                const uploadResult = await this.uploadAvatar(this.state.user);
                const userProfile = await userAPI.getUserProfile({username: this.state.user.username});
                this.setState({user: userProfile});
                if (userProfile) {
                    await AsyncStorage.setItem('USER', JSON.stringify(userProfile));
                }
            }
            await UserSetting.saveSettings();
            return this.props.navigation.goBack();
        } else {
            // this.setState({ error: updateResult.data.error })
            Alert.alert(
                "Error",
                updateResult.data.error,
                [
                    { text: 'OK' }
                ],
                {
                    cancelable: false
                }
            )
        }
    }
    componentWillUnmount() {
      this.setState({avatarSource:null});
    }
    uploadAvatar(user) {
        const { avatarSource } = this.state;
        const username = user.username;
        const files = [];
        const avatarToUpload = {
            name: `${username}_avatar`,
            filename: avatarSource.length && avatarSource.substr(avatarSource.lastIndexOf('/') + 1),
            type: 'image/jpeg',
            data: RNFetchBlob.wrap(avatarSource)
        }
        files.push(avatarToUpload);

        return uploads(files);
    }

    // getDisplayCurrency(currencyCode) {
    //     const index = currencies.findIndex(currency => {
    //         return currency.code === currencyCode
    //     });
    //     return index >= 0 ? currencies[index].code + ' (' + currencies[index].symbol + ')' : '';
    // }

    getLanguageByCode(languageCode) {
        const index = appLanguages.findIndex(language => {
            return language.code === languageCode
        });
        return index >= 0 ? appLanguages[index].nativeName : '';
    }

    getCountryByCode(countryCode) {
        const index = countriesArray.findIndex(country => {
            return country.code === countryCode
        });
        return index >= 0 ? countriesArray[index].name : '';
    }

    render() {

        return (
            <ScrollView>
                <View style={css.container}>
                    <Image style={{ alignItems: 'center', height: 250 }} blurRadius={5} source={{ uri: this.state.user.avatar }}>
                        <TouchableOpacity style={{ marginTop: 20 }} onPress={() => this.selectPhotoTapped()}>
                            <View
                                style={[
                                    css.avatar
                                ]}>
                                {this.state.avatarSource === null
                                    ? <Image style={css.avatar} source={{ uri: this.state.user.avatar }} />
                                    : <Image style={css.avatar} source={{
                                        uri: this.state.avatarSource,
                                        scale: 2,
                                        type: 'jpeg'
                                    }} />
                                }
                            </View>
                        </TouchableOpacity>
                        <Text style={css.text}>{this.state.user.firstname}</Text>
                    </Image>
                    <Text style={css.text}>{I18n.t('editProfileSetting')}</Text>
                    {/*<Text style={{ color: Colors.redColor, textAlign: 'center' }}>{this.state.error}</Text>*/}
                    <List containerStyle={{ marginBottom: 10 }}>
                        <ListItem
                            key={1}
                            hideChevron={true}
                            leftIcon={
                                <View style={css.input}>
                                    <Icon
                                        name='email'
                                        color={Colors.darkGreen}
                                        style={{
                                            marginRight: 20
                                        }} />
                                    <Text style={{ width: 100 }}>{I18n.t('email')}</Text>
                                    <TextInput
                                        style={css.textInput}
                                        underlineColorAndroid='transparent'
                                        placeholderTextColor='#98948F'
                                        onChangeText={(email) => {
                                            let user = this.state.user;
                                            user.email = email;
                                            this.setState({ user });
                                        }}
                                        value={this.state.user.email}></TextInput>
                                </View>
                            }
                        />
                        <ListItem
                            key={2}
                            hideChevron={true}
                            leftIcon={
                                <View style={css.input}>
                                    <Icon
                                        name='perm-identity'
                                        color={Colors.darkGreen}
                                        style={{
                                            marginRight: 20
                                        }} />
                                    <Text style={{ width: 100 }}>{I18n.t('username')}</Text>
                                    <TextInput
                                        style={css.textInput}
                                        underlineColorAndroid='transparent'
                                        value={this.state.user.username}
                                        editable={false}
                                        placeholderTextColor='#98948F'></TextInput>
                                </View>
                            }
                        />
                        <ListItem
                            key={3}
                            hideChevron={true}
                            leftIcon={
                                <View style={css.input}>
                                    <Icon
                                        name='perm-identity'
                                        color={Colors.darkGreen}
                                        style={{
                                            marginRight: 20
                                        }} />
                                    <Text style={{ width: 100 }}>{I18n.t('firstName')}</Text>
                                    <TextInput
                                        style={css.textInput}
                                        underlineColorAndroid='transparent'
                                        value={this.state.user.firstName}
                                        onChangeText={(firstName) => {
                                            let user = this.state.user;
                                            user.firstName = firstName;
                                            this.setState({ user });
                                        }}
                                        placeholderTextColor='#98948F'></TextInput>
                                </View>
                            }
                        />
                        <ListItem
                            key={4}
                            hideChevron={true}
                            leftIcon={
                                <View style={css.input}>
                                    <Icon
                                        name='perm-identity'
                                        color={Colors.darkGreen}
                                        style={{
                                            marginRight: 20
                                        }} />
                                    <Text style={{ width: 100 }}>{I18n.t('lastName')}</Text>
                                    <TextInput
                                        style={css.textInput}
                                        underlineColorAndroid='transparent'
                                        placeholder={I18n.t('lastName')}
                                        value={this.state.user.lastName}
                                        onChangeText={(lastName) => {
                                            let user = this.state.user;
                                            user.lastName = lastName;
                                            this.setState({ user });
                                        }}
                                        placeholderTextColor='#98948F'></TextInput>
                                </View>
                            }
                        />

                    </List>

                    <List containerStyle={{ marginBottom: 20 }}>
                        <ListItem
                            onPress={() => this.props.navigation.navigate('LocalSetting', {mode:'COUNTRY',loadAppSetting:this.loadAppSetting})}
                            chevronColor={Colors.darkGreen}
                            key={5}
                            title={I18n.t('country')}
                            leftIcon={{
                                name: 'language',
                                color: Colors.darkGreen,
                            }}
                            rightTitle={this.getCountryByCode(this.state.country)}
                            rightTitleContainerStyle={{ flex: 0 }}
                        />
                        <ListItem
                            onPress={() => this.props.navigation.navigate('LocalSetting', {mode:'TIMEZONE',loadAppSetting:this.loadAppSetting})}
                            chevronColor={Colors.darkGreen}
                            key={6}
                            title={I18n.t('timeZone')}
                            leftIcon={{
                                name: 'timelapse',
                                color: Colors.darkGreen,
                            }}
                            titleContainerStyle={{ width: 100 }}
                            rightTitleContainerStyle={{ flex: 1 / 2 }}
                            rightTitle={this.state.timeZone}
                        />
                        <ListItem
                            onPress={() => this.props.navigation.navigate('LocalSetting', {mode:'CURRENCY',loadAppSetting:this.loadAppSetting})}
                            chevronColor={Colors.darkGreen}
                            key={7}
                            title={I18n.t('currency')}
                            leftIcon={{
                                name: 'attach-money',
                                color: Colors.darkGreen,
                            }}
                            titleContainerStyle={{ width: 100 }}
                            rightTitleContainerStyle={{ flex: 1 / 2 }}
                            rightTitle={this.state.currency}
                        />
                        <ListItem
                            onPress={() => this.props.navigation.navigate('LocalSetting', {mode:'LANGUAGE',loadAppSetting:this.loadAppSetting})}
                            chevronColor={Colors.darkGreen}
                            key={8}
                            title={I18n.t('appLanguage')}
                            leftIcon={{
                                name: 'language',
                                color: Colors.darkGreen,
                            }}
                            titleContainerStyle={{ width: 100 }}
                            rightTitleContainerStyle={{ flex: 1 / 2 }}
                            rightTitle={this.getLanguageByCode(this.state.language)}
                        />
                    </List>
                </View>
            </ScrollView>
        )
    }
}
const css = StyleSheet.create({
    container: {
        flex: 1,
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

    avatar: {
        width: 150,
        height: 150,
        borderRadius: 100,
    }

})

export default GeneralSettingScreen;
