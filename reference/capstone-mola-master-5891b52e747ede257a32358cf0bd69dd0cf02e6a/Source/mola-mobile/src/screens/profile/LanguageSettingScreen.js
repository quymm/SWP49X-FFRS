import React, { Component } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { SideMenu, List, ListItem, Icon, } from 'react-native-elements';
import Colors from '../../../constants/Colors';
import I18n from '../../../constants/locales/i18n';
import MultiSelect from 'react-native-multiple-select';


class LanguageSettingScreen extends Component {

    constructor() {
        super()
    }
    static navigationOptions = ({
    ...props
  }) => ({
            headerTintColor: '#ffffff',
            headerTitle: I18n.t('languageSetting'),
            headerRight: <TouchableOpacity>
                <Icon name='done' color={Colors.whiteColor} size={30} containerStyle={{ marginRight: 30 }} /></TouchableOpacity>,
            headerStyle: {
                backgroundColor: Colors.darkGreen,
            },
        });
    _goToScreen(screenName) {
        this.props.navigation.navigate(screenName);
    }
    render() {


        return (
            <View style={css.container}>
                <Text style={css.text}>{I18n.t('iamlearning')}</Text>
                <List containerStyle={{ marginBottom: 10 }}>
                    <ListItem
                        chevronColor={Colors.darkGreen}
                        key={1}
                        title={'Vietnamese'}
                        rightTitle={'Beginner'}
                    />
                </List>

                <List containerStyle={{ marginBottom: 20 }}>
                    <ListItem
                        chevronColor={Colors.darkGreen}
                        key={2}
                        title={I18n.t('addLanguage')}
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

export default LanguageSettingScreen;
