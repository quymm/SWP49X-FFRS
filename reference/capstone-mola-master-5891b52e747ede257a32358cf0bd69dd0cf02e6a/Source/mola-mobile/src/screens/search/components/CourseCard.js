import React, { Component } from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity } from 'react-native';
import Colors from '../../../../constants/Colors';
import { Icon } from 'react-native-elements';
import StarRating from 'react-native-star-rating';

import { flags } from '../../../../constants/countries';

import { currencies } from '../../../../constants/currencies';
import UserSetting from '../../UserSetting';
import ExchangeRates from '../../ExchangeRates';
import accounting from '../../../services/accounting';
import I18n from '../../../../constants/locales/i18n';
class CourseCard extends Component {
    onLearnMore({ course, teacher, userInfo }) {
        console.log(this.props)
        const { navigate } = this.props.navigation;
        navigate('CourseInfoToRegist', { course, teacher, userInfo });
    }

    _countryFlag(name) {
        const index = flags.findIndex(flag => { return flag.name.toLowerCase() === name.toLowerCase() });
        return index >= 0 ? flags[index].flag : '';
    }

    getCurrencySymbol(currencyCode) {
        const index = currencies.findIndex(currency => {
            return currency.code === currencyCode;
        })
        return index >= 0 ? currencies[index].symbol : '';
    }

    render() {
        const { course, teacher, userInfo } = this.props.course;
        const coutryFlag = this._countryFlag(course.language);
        return (
            <View style={[css.container, this.props.style]}>
                <TouchableOpacity style={{ flexDirection: 'row', flex: 1, alignItems:'center' }} onPress={() => this.onLearnMore({ course, teacher, userInfo })}>
                    <View style={{flexDirection:'row', flex:2}}>
                        <Image
                            style={css.image}
                            source={{ uri: userInfo.avatar }} />
                        <View style={[css.rowView, { flex:5/9}]}>
                            <Text style={css.title} numberOfLines={2}>{course.title}</Text>
                            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                                <StarRating
                                    disabled
                                    starColor={'#f1c40f'}
                                    starSize={18}
                                    maxStars={5}
                                    rating={course.rating}
                                />
                                <Text>{`(${course.numOfRate})`}</Text>
                            </View>
                            <Text style={{ fontSize: 15, fontWeight: 'bold' }}>  {userInfo.firstName} {userInfo.lastName}</Text>
                            <Text style={css.language}>{`${coutryFlag} ${course.language}`}</Text>
                            {/*{/them la co vao truoc chu English/}*/}
                        </View>
                    </View>
                    <View style={[css.rowView, { alignItems: 'center', flex:1 }]}>
                        {
                            course.price === 0
                                ? <Text style={css.duration}>Free</Text>
                                : <Text style={css.duration}>
                                  {accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * course.price).toFixed(2))}
                                    {/* {(ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * course.price).toFixed(2)} */}
                                    {this.getCurrencySymbol(UserSetting.CURRENCY)}
                                    {I18n.t('min')}</Text>
                        }


                        {/*<Text style={{ textAlign: 'center', borderColor: 'black', borderWidth: 1, width: 100, fontSize: 20 }}>Book Now</Text>*/}

                    </View>
                </TouchableOpacity>
            </View>

        );
    }
}

var css = StyleSheet.create({
    container: {
        flexDirection: 'row',
        flex: 1,
        marginBottom: 10,
        height: 130,
        padding: 10,
        elevation: 1
    },
    title: {
        fontSize: 15,
    },
    rowView: {
        padding: 5,
    },
    duration: {
        fontSize: 20,
        padding: 5,
        color: Colors.redColor,
        textAlign: 'center',
    },
    language: {
        width: 100,
        borderRadius: 10,
    },
    image: {
        flex: 4/9,
    }
})

export default CourseCard;