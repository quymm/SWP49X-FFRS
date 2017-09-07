import { AsyncStorage } from 'react-native';
import geoDataApi from '../apis/geodata-api';
import { currenciesByCountryCode } from '../../constants/currencies';
import accounting from '../services/accounting'
import DeviceInfo from 'react-native-device-info';
import I18n from 'react-native-i18n';

const countryKey = 'APP_SETTING_COUNTRY';
const timeZoneKey = 'APP_SETTING_TIMEZONE';
const currencyKey = 'APP_SETTING_CURRENCY';
const appLanguageKey = 'APP_SETTING_APP_LANGUAGE';
var geoData;

class UserSetting {
    static COUNTRY;
    static TIMEZONE;
    static CURRENCY;
    static APP_LANGUAGE;
    static isTeacher;

    static async saveSettings() {
        await AsyncStorage.setItem(countryKey, this.COUNTRY);
        await AsyncStorage.setItem(timeZoneKey, this.TIMEZONE);
        await AsyncStorage.setItem(currencyKey, this.CURRENCY);
        await AsyncStorage.setItem(appLanguageKey, this.APP_LANGUAGE);
        if(this.CURRENCY == 'VND'){
          accounting.settings.currency.decimal = ",";
          accounting.settings.currency.thousand = ".";
        } else {
          accounting.settings.currency.decimal = ".";
          accounting.settings.currency.thousand = ",";
        }
    }

    static async clearSettings() {
        await AsyncStorage.removeItem(countryKey);
        await AsyncStorage.removeItem(timeZoneKey);
        await AsyncStorage.removeItem(currencyKey);
        await AsyncStorage.removeItem(appLanguageKey);
    }

    static async loadAppSetting() {
        this.COUNTRY = await AsyncStorage.getItem(countryKey);
        this.TIMEZONE = await AsyncStorage.getItem(timeZoneKey);
        this.CURRENCY = await AsyncStorage.getItem(currencyKey);
        this.APP_LANGUAGE = await AsyncStorage.getItem(appLanguageKey);
        I18n.locale = this.APP_LANGUAGE;
        if (!(this.COUNTRY && this.TIMEZONE && this.CURRENCY && this.APP_LANGUAGE)) {
            geoData = await geoDataApi.get();
            
            if (!this.COUNTRY) {
                this.initCountryAndTimeZone();
            }

            if (!this.CURRENCY) {
                this.initCurrency();
            }

            if (!this.APP_LANGUAGE) {
                this.initAppLanguage();
            }

            this.saveSettings();
        }
          accounting.settings.currency.symbol = "";
        if(this.CURRENCY == 'VND'){
          accounting.settings.currency.decimal = ",";
          accounting.settings.currency.thousand = ".";
        } else {
          accounting.settings.currency.decimal = ".";
          accounting.settings.currency.thousand = ",";
        }
    }

    static initCountryAndTimeZone() {
        this.COUNTRY = geoData.data.country_code;
        this.TIMEZONE = geoData.data.time_zone;
    }

    static initCurrency() {
        const countryCode = geoData.data.country_code;
        this.CURRENCY = this.getCurrency(countryCode);
    }

    static getCurrency(countryCode) {
        const index = currenciesByCountryCode.findIndex(currency => {
            return currency.countryCode === countryCode
        });
        return index >= 0 ?
            currenciesByCountryCode[index].currency : '';
    }

    static initAppLanguage() {
        // const countryCode = DeviceInfo.getDeviceCountry();
        // this.APP_LANGUAGE = this.getLanguageCode(countryCode);
        this.APP_LANGUAGE = "vn";
    }

    static getLanguageCode(countryCode) {
        const lookup = require('country-data').lookup;
        const countries = lookup.countries({ alpha2: countryCode })[0];
        const languageAlpha3 = countries.languages[0];
        const language = lookup.languages({ alpha3: languageAlpha3 })[0];
        return language.alpha2;
    }
}

export default UserSetting;