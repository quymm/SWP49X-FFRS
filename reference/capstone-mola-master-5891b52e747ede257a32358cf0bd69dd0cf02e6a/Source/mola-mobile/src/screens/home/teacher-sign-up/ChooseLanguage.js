import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
} from 'react-native';
import { Button } from 'react-native-elements';


class ChooseLanguage extends Component {
    state = {}
    render() {
        return (
            <View style={styles.page}>
                <View style={{ alignItems: 'center' }}>
                    <Button
                        buttonStyle={styles.button}
                        onPress={() => { this.props.onContinue() }}
                        title='Continue' />
                </View>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    page: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        
    },
    button: {
        marginTop: 10,
        width: 200,
        backgroundColor: '#4aae4f',
    },
})
export default ChooseLanguage;