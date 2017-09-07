import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
} from 'react-native';
import { Button } from 'react-native-elements';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';


class SetAvaiability extends Component {
    state = {}
    render() {
        return (
            <View style={styles.page}>
                <View style={{ alignItems: 'center' }}>
                    <Text style={styles.title}>{I18n.t('setAvaiability')}</Text>
                    <View style={styles.intro}>
                        <Text style={styles.text}>{I18n.t('setAvaiability_into')}</Text>
                    </View>
                    <Button
                        buttonStyle={styles.button}
                        onPress={() => { this.props.onContinue() }}
                        title={I18n.t('continue')} />
                </View>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    page: {
        flex: 1,
        // justifyContent: 'center',
        alignItems: 'center'
    },
    button: {
        marginTop: 10,
        width: 200,
        backgroundColor: Color.darkGreen,
        top:200,
    },
    title: {
        textAlign: 'center',
        fontSize: 18,
        fontWeight: 'bold',
        marginTop: 20,
        marginBottom: 20,
    },
    intro: {
        width: '90%',

    },
    text: {
        fontSize: 15,
        marginTop: 20,
        marginBottom: 20,
    },
})
export default SetAvaiability;