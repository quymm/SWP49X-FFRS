import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
    TextInput,
    ScrollView,
    AsyncStorage

} from 'react-native';
import {connect} from 'react-redux';
import { CheckBox, Button } from 'react-native-elements';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';
import TeacherSignUpAPI from '../../../apis/teacher-signup';
const teacherSignUpAPI = new TeacherSignUpAPI();


const mapStateToProps = (state) => ({
  user: state.auth.user.data.data.user,
});

@connect(mapStateToProps)
class Experience extends Component {
    state = {
        height: 0,
        checked: false,
        expYears: 1,
        experienceAbout: '',
        user: {}
    }
  async componentWillMount() {
    let user = await AsyncStorage.getItem('USER');
    user = JSON.parse(user);
    await this.setState({user});

  }
  async componentDidMount() {
    const {user} = this.props;
    if(user.isTeacher){
      const teacherInfo = await teacherSignUpAPI.getInfoTeacher(user.username);
      if (teacherInfo.status === 'ok') {
        this.setState({
          experienceAbout: teacherInfo.data.expDescription,
          expYears: teacherInfo.data.expYears,
        });
      }

    }
  }
  async onContinuePress () {
    let {user} = this.state;
    const {expYears, experienceAbout,} = this.state
    
    user.experienceYear = expYears;
    user.experienceAbout = experienceAbout;
    await AsyncStorage.setItem('USER', JSON.stringify(user));

    this.props.onContinue();
  }

    render() {
      console.log('expe USER', this.state.user)
        return (

            <ScrollView>
                <View style={styles.page}>
                    <Text style={styles.title}>{I18n.t('experience')}</Text>
                    <View>
                        <Text style={{ fontSize: 15 }}>{I18n.t('howlongTeaching')}?</Text>
                        <CheckBox
                            title={I18n.t('oneOrLess')}
                            checked={this.state.expYears == 1}
                            containerStyle={styles.chbContainer}
                            checkedIcon='check'
                            uncheckedColor='white'
                            checkedColor= {Color.darkGreen}
                            onPress={() => this.setState({ expYears: 1 })}
                            textStyle={styles.checkText}
                        />

                        <CheckBox
                            title={I18n.t('oneToFive')}
                            checked={this.state.expYears == 2}
                            containerStyle={styles.chbContainer}
                            checkedIcon='check'
                            checkedColor={Color.darkGreen}
                            uncheckedColor='white'
                            onPress={() => this.setState({ expYears: 2 })}
                            textStyle={styles.checkText}
                        />
                        <CheckBox
                            title={I18n.t('moreFive')}
                            checked={this.state.expYears == 3}
                            containerStyle={styles.chbContainer}
                            checkedIcon='check'
                            uncheckedColor='white'
                            checkedColor={Color.darkGreen}

                            onPress={() => this.setState({ expYears: 3 })}
                            textStyle={styles.checkText}
                        />
                        <View style={styles.txtAreaView}>
                            <TextInput
                                // style={styles.txtArea}
                                selectionColor='white'
                                placeholder={I18n.t('tellExperiance')}
                                underlineColorAndroid='white'
                                onContentSizeChange={(event) => {
                                    this.setState({ height: event.nativeEvent.contentSize.height });
                                }}
                                multiline={true}
                                onChangeText={experienceAbout => {
                                    this.setState({ experienceAbout });
                                }}
                                style={[styles.default, { height: Math.max(35, this.state.height), fontSize: 15 }]}
                                value={this.state.experienceAbout}
                            />
                        </View>
                        <View style={{ alignItems: 'center' }}>
                            <Button
                                buttonStyle={styles.button}
                                textStyle={{ fontSize: 15 }}
                                onPress={() => this.onContinuePress() }
                                title={I18n.t('continue')} />
                        </View>
                    </View>
                </View>
            </ScrollView>
        );
    }
}
const styles = StyleSheet.create({
    page: {
        flex: 1,
        padding: 20,
        // justifyContent: 'center',
        // alignItems: 'center'
    },
    title: {
        textAlign: 'center',
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 30,
    },
    chbContainer: {
        marginTop: 10,
        marginBottom: 0,
        paddingBottom: 0,
        paddingLeft: 0,
        marginRight: 0,
        marginLeft: 0,
        backgroundColor: 'white',
        borderRadius: 0,
        borderLeftWidth: 0,
        borderRightWidth: 0,
        borderBottomWidth: 0,
    },
    checkText: {
        fontWeight: 'normal',
        fontSize: 15,
    },
    txtArea: {


    },
    txtAreaView: {
        marginTop: 20,
        height: 150,
        borderWidth: 1,
        borderColor: '#98948F',
    },
    button: {
        marginTop: 10,
        width: 200,
        backgroundColor: Color.darkGreen,
        borderRadius:5
    },

})
export default Experience;