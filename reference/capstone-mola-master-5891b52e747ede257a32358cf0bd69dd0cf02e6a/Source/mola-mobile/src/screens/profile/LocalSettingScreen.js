import React, { Component } from 'react';
import { ScrollView, View } from 'react-native';
import { List, ListItem } from 'react-native-elements';

import { timezones } from '../../../constants/timezones';
import { currencies } from '../../../constants/currencies';
import { countriesArray, appLanguages } from '../../../constants/countries';
import UserSetting from '../UserSetting';
import I18n from 'react-native-i18n';
import RNRestart from 'react-native-restart';

class LocalSettingScreen extends Component {
    constructor(props) {
        super(props);
    }

    renderCountry() {
        return countriesArray.map((item, index) => (
            <ListItem
                onPress={() => this.onSetCountry(item.code)}
                key={index}
                title={item.name}
                hideChevron={true}
            />
        ))
    }

    onSetCountry(country) {
        UserSetting.COUNTRY = country;
        UserSetting.saveSettings()
        this.props.navigation.state.params.loadAppSetting();
        this.props.navigation.goBack();
    }

    renderTimeZone() {
        return timezones.map((item, index) => (
            <ListItem
                onPress={() => this.onSetTimeZone(item.name)}
                key={index}
                title={item.name + ' ' + item.offset}
                hideChevron={true}
            />
        ))
    }

    onSetTimeZone(timeZone) {
        UserSetting.TIMEZONE = timeZone;
        UserSetting.saveSettings()
        this.props.navigation.state.params.loadAppSetting();
        this.props.navigation.goBack();
    }

    renderCurrency() {
        return currencies.map((item, index) => (
            <ListItem
                onPress={() => this.onSetCurrency(item.code)}
                key={index}
                title={item.code}
                hideChevron={true}
            />
        ))
    }

    onSetCurrency(currency) {
        UserSetting.CURRENCY = currency;
        UserSetting.saveSettings()
        this.props.navigation.state.params.loadAppSetting();
        this.props.navigation.goBack();
    }

    renderLanguage() {
        return appLanguages.map((item, index) => (
            <ListItem
                onPress={() => this.onSetLanguage(item.code)}
                key={index}
                title={item.nativeName}
                hideChevron={true}
            />
        ))
    }

    onSetLanguage(language) {
        UserSetting.APP_LANGUAGE = language;
        UserSetting.saveSettings()
        I18n.locale = language;
        this.props.navigation.state.params.loadAppSetting();
        // this.props.navigation.goBack();
        RNRestart.Restart();
    }

    render() {
        const mode = this.props.navigation.state.params.mode;
        return (
            <ScrollView>
                <List>
                    {
                        mode === 'COUNTRY' ? this.renderCountry() :
                            mode === 'TIMEZONE' ? this.renderTimeZone() :
                                mode === 'CURRENCY' ? this.renderCurrency() :
                                    this.renderLanguage()
                    }
                </List>
            </ScrollView>
        )
    }
}

export default LocalSettingScreen;