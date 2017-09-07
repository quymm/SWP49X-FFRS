import React, { Component } from 'react';
import Color from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';

import { countriesPopular } from '../../../constants/countries';

import { Button, Icon } from 'react-native-elements';

import MultiSelect from 'react-native-multiple-select';
import {
    StyleSheet,
    Text,
    View,
    TextInput,
    ScrollView,
    Image,
    TouchableOpacity,
    AsyncStorage
} from 'react-native';

import LanguagesAPI from '../../apis/languages'

class RegisterLanguageScreen extends Component {

    constructor(props) {
        super(props);
        this.state = {
            languageSpeak: null,
            languageLearn: null
        }
    }

    selectLanguageSpeak(item) {
        console.log('language speak: ', this._multiSelectLanguageSpeak.state.selectedItems);
    }

    selectLanguageLearn(item) {
        console.log('language learn: ', this._multiSelectLanguageLearn.state.selectedItems);
    }

    async onContinuePress() {
        const languages = {
            languageSpeak: this._multiSelectLanguageSpeak.state.selectedItems,
            languageLearn: this._multiSelectLanguageLearn.state.selectedItems
        }
        const languagesAPI = new LanguagesAPI();
        await languagesAPI.setLanguageSpeakLearn(languages);
        this.props.navigation.navigate('Login');
    }

    async componentWillMount() {
        const languagesAPI = new LanguagesAPI();
        const languages = await languagesAPI.getLanguageUser();
        this.setState({
            languageSpeak: languages.data.languageSpeak,
            languageLearn: languages.data.languageLearn
        })

        this._multiSelectLanguageSpeak.state.selectedItems = languages.data.languageSpeak;
        this._multiSelectLanguageLearn.state.selectedItems = languages.data.languageLearn;
    }

    render() {
        return (
            <ScrollView style={styles.page}>
                {this.state.languageSpeak ?
                    <View>
                        <Text style={styles.title}>{I18n.t('aboutyou_languagespeak')}</Text>
                        <MultiSelect
                            ref={c => this._multiSelectLanguageSpeak = c}
                            items={countriesPopular}
                            uniqueKey="name"
                            selectedItemsChange={(itemSelected) => this.selectLanguageSpeak(itemSelected)}
                            selectedItems={this.state.languageSpeak}
                            selectText="Choose language(s)"
                            searchInputPlaceholderText="Search languages..."
                            //altFontFamily="ProximaNova-Light"
                            tagRemoveIconColor="#CCC"
                            tagBorderColor={Color.lightGreen}
                            tagTextColor={Color.lightGreen}
                            selectedItemTextColor="#CCC"
                            selectedItemIconColor={Color.lightGreen}
                            itemTextColor="#000"
                            searchInputStyle={{ color: '#CCC' }}
                            submitButtonColor={Color.darkGreen}
                            submitButtonText="Close"
                        />
                    </View>
                    : <View></View>}

                {this.state.languageLearn ?
                    <View>
                        <Text style={styles.title}>{I18n.t('languageLearn')}</Text>
                        <MultiSelect
                            ref={c => this._multiSelectLanguageLearn = c}
                            items={countriesPopular}
                            uniqueKey="name"
                            selectedItemsChange={(itemSelected) => this.selectLanguageLearn(itemSelected)}
                            selectedItems={this.state.languageLearn}
                            selectText="Choose language(s)"
                            searchInputPlaceholderText="Search languages..."
                            //altFontFamily="ProximaNova-Light"
                            tagRemoveIconColor="#CCC"
                            tagBorderColor={Color.lightGreen}
                            tagTextColor={Color.lightGreen}
                            selectedItemTextColor="#CCC"
                            selectedItemIconColor={Color.lightGreen}
                            itemTextColor="#000"
                            searchInputStyle={{ color: '#CCC' }}
                            submitButtonColor={Color.darkGreen}
                            submitButtonText="Close"
                        />
                    </View>
                    : <View></View>}

                <View style={styles.buttonView}>
                    <Button
                        buttonStyle={styles.button}
                        textStyle={{
                            fontSize: 15
                        }}
                        onPress={() => this.onContinuePress()}
                        title={I18n.t('done')} />
                </View>
            </ScrollView>
        )
    }
}
const styles = StyleSheet.create({
    page: {
        flex: 1,
        backgroundColor: '#e9ebee'
    },
    button: {
        marginTop: 10,
        width: 200,
        backgroundColor: Color.darkGreen,
        borderRadius: 5
    },
    title: {
        textAlign: 'center',
        fontSize: 18,
        fontWeight: 'bold',
        marginTop: 20,
        marginBottom: 20,
    },
    text: {
        fontSize: 15,
        marginTop: 20,
    },
    languageView: {
        alignItems: 'flex-start',
    },
    buttonView: {
        alignItems: 'center',
        position: 'relative',
    },
    tagsContainer: {
        flex: 1,
        flexDirection: 'row',
        flexWrap: 'wrap'
    },
    tag: {
        justifyContent: 'center',
        marginTop: 6,
        marginRight: 3,
        padding: 8,
        height: 24,
        borderRadius: 2,
    },

});
export default RegisterLanguageScreen;