import React, { Component } from 'react';
import { View, Text, StyleSheet, Alert, Image, TouchableOpacity } from 'react-native';
import Colors from '../../../../constants/Colors';
import { Icon } from 'react-native-elements';
import I18n from '../../../../constants/locales/i18n';
import RequestAPI from '../../../apis/request';

import moment from 'moment';

class ListSendingRequest extends Component {
    constructor(props) {
        super(props);
    }

    async cancelSessionRequest(sessionId) {
        const requestAPI = new RequestAPI();
        await requestAPI.cancelSessionRequest(sessionId);
    }
    async cancelCourseRegisterRequest(registerCourseID) {
        const requestAPI = new RequestAPI();
        await requestAPI.cancelCourseRegisterRequest(registerCourseID);
    }

    _confirmCancel(name, teacherName, sessionReq, registerCourse, session) {
        Alert.alert(
            'Do you want to cancel request ' + name + ' of ' + teacherName,
            null,
            [
                { text: 'Cancel' }, { text: 'OK', onPress: async () => {
                    if(sessionReq){
                        this.props.setLoading(true);
                        await this.cancelSessionRequest(session.id);
                        this.props.loadRequest();

                    } else{
                        this.props.setLoading(true);
                        await this.cancelCourseRegisterRequest(registerCourse.id);
                        this.props.loadRequest();
                    }
                    
                } }
            ],
            {
                cancelable: false
            }
        )
    }

    render() {
        const { chapter, course, learner, session, teacher, lesson, sessionReq, registerCourse, time, timeSlot, teacherInfo } = this.props.request;

        return (

            <View style={css.lessonContainer}>
                <TouchableOpacity onPress={() => this.props.mainNavigation.navigate('CourseInfoToRegist', {
                    course: course,
                    teacher: teacher,
                    userInfo: teacherInfo
                })}>
                    <View style={{ flexDirection: 'row' }}>
                        <View style={css.imageView}>
                            <Image
                                style={css.image}
                                source={{ uri: teacherInfo.avatar }} />
                            <Text style={{ fontSize: 15, textAlign: 'center' }} numberOfLines={2}>{teacherInfo.firstName} {teacherInfo.lastName}</Text>
                        </View>

                        <View style={css.contentView}>
                            <Text style={css.title} numberOfLines={2}>{course.title}</Text>
                            {
                                sessionReq
                                    ? <View>
                                        <View style={{ flexDirection: 'row' }}>
                                            <Icon
                                                name='calendar'
                                                type='evilicon'
                                                color={Colors.grayColor}
                                            />
                                            <Text> {'' + moment(timeSlot.starTime).format('DD-MM-YYYY')}</Text>
                                        </View>
                                        <View style={{ flexDirection: 'row' }}>
                                            <Icon
                                                name='clock'
                                                type='evilicon'
                                                color={Colors.grayColor}
                                            />
                                            <Text> {moment(timeSlot.starTime).format('HH:mm')} - {moment(timeSlot.endTime).format('HH:mm')}</Text>
                                        </View>
                                    </View>
                                    : <View></View>

                            }

                        </View>
                    </View>
                </TouchableOpacity>
                <View style={{ justifyContent: 'center' }}>
                    <TouchableOpacity style={css.button1} onPress={() => this._confirmCancel(course.title,teacherInfo.firstName, sessionReq, registerCourse, session)}>
                        <Text style={{ color: 'white', fontSize: 18, textAlign: 'center' }}>{I18n.t('cancel')}</Text>
                    </TouchableOpacity>
                </View>

            </View>

        );
    }
}

var css = StyleSheet.create({
    lessonContainer: {
        flexDirection: 'row',
        elevation: 2,
        paddingVertical: 5,
        flex: 1,
        marginBottom: 10,
        backgroundColor: 'white'
    },
    title: {
        fontSize: 18,
        marginBottom: 5,
        textAlign: 'left',
        height: 50,
        width: 120,
    },
    imageView: {
        alignItems: 'center',
        margin: 5,
        width: 100
    },
    contentView: {
        margin: 5,
        padding: 10,
        maxWidth: 180
    },
    duration: {
        fontSize: 20,
        padding: 5,
        color: Colors.redColor,
        textAlign: 'center',
        flex: 1
    },
    image: {
        width: 80,
        height: 80,
        borderRadius: 100
    },
    button1: {
        width: 100,
        marginLeft: 10,
        backgroundColor: '#E57373',
        marginBottom: 10,
        padding: 5,
        borderRadius: 5
    },

})

export default ListSendingRequest;