import React, { Component } from 'react';
import { Button } from 'react-native-elements';
import {
    Text,
    View,
    StyleSheet,
    Switch
} from 'react-native';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';

class SignupScreen extends Component {
    constructor(props){
        super(props);
    }
    state = {
        trueSwitchIsOn: true,
        falseSwitchIsOn: false,
    };

    render() {
        return (
            <View style={css.container}>
                <Text style={css.header}>{I18n.t('underReview')}</Text>
                <View style={css.input}>

                    <View style={css.response}>
                        <Text>{I18n.t('responseText')}</Text>
                    </View>
                </View>
                <View style={css.switch}>
                    <Text style={{marginRight:20}}>{I18n.t('turnOnNoti')}</Text>
                    <Switch
                        
                        thumbTintColor= {Color.darkGreen}
                        onValueChange={(value) => this.setState({ falseSwitchIsOn: value })}
                        value={this.state.falseSwitchIsOn} />
                </View>
                <Button
                    buttonStyle={css.button}
                    onPress={()=> this.props.navigation.navigate('Main')}
                    title={I18n.t('toLearnerMode')} />
            </View>
        );
    }
}
var css = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: 'white',
    },
    switch: {
        flexDirection: 'row',
        margin: 30,
        alignItems: 'center',
    },
    response: {
        padding: 20, borderColor: 'black', borderWidth: 1, height: 300, width: '100%'
    },
    text: {
        fontSize: 15,
        marginRight: 30,
    },
    header: {
        fontSize: 20,
        fontWeight: 'bold',
        margin: 30,
    },
    input: {
        width: '90%',
        marginBottom: 20,
    },
    button: {
        width: 200,
        backgroundColor: Color.darkGreen,
    }
});
export default SignupScreen;