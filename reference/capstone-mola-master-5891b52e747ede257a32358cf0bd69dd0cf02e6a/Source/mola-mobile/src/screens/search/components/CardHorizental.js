import React, { Component } from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity } from 'react-native';
import Colors from '../../../../constants/Colors';
import { Icon } from 'react-native-elements';
import StarRating from 'react-native-star-rating';
import accounting from '../../../services/accounting';
import I18n from '../../../../constants/locales/i18n';
import { currencies } from '../../../../constants/currencies';
import UserSetting from '../../UserSetting';
import ExchangeRates from '../../ExchangeRates';

class CardHorizental extends Component {
   onLearnMore({ course, teacher, userInfo }) {
        console.log('asssssssssssssssssssssssssssssssssssssssssssssss',this.props)
        const { navigate } = this.props.navigation;
        navigate('CourseInfoToRegist', { course, teacher, userInfo });
    }

    getCurrencySymbol(currencyCode) {
        const index = currencies.findIndex(currency => {
            return currency.code === currencyCode;
        })
        return index >= 0 ? currencies[index].symbol : '';
    }

    render() {
        const { teacher, userInfo, course } = this.props.recomend;
        return (
            <TouchableOpacity onPress={() => this.onLearnMore({ course, teacher, userInfo })}>
                <View style={[css.container, this.props.style]}>

                    <Image
                        style={css.image}
                        source={{ uri: userInfo.avatar }} />
                    <View style={css.rowView}>
                        <Text style={css.title} numberOfLines={2}>{course.title}</Text>
                        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                            <StarRating
                                disabled
                                starColor={'#f1c40f'}
                                starSize={18}
                                maxStars={5}
                                rating={course.rating}
                            />
                            <Text>({course.numOfRate})</Text>
                        </View>
                        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                            <Text style={css.language}>{course.language}</Text>
                            <Text style={css.duration}>
                                {/* {(ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * course.price).toFixed(2)}
                                {this.getCurrencySymbol(UserSetting.CURRENCY)} */}
                                {accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * course.price).toFixed(2), "", 2, ".", ",")}
                                    {/* {(ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * course.price).toFixed(2)} */}
                                    {this.getCurrencySymbol(UserSetting.CURRENCY)}
                                    {I18n.t('min')}
                            </Text></View>
                        {/*{/them la co vao truoc chu English/}*/}
                    </View>
                </View>
            </TouchableOpacity>
        );
    }
}

var css = StyleSheet.create({
    container: {
        flex: 1,
        marginBottom: 10,
        height: 200,
        width: 170,
        padding: 10,
        alignItems: 'center'
    },
    title: {
        fontSize: 15,
    },
    rowView: {

    },
    duration: {
        fontSize: 20,
        padding: 5,
        color: Colors.redColor,
    },
    language: {
        borderRadius: 10,
    },
    image: {
        width: 100,
        height: 90

        //borderRadius: 100
    }
})

export default CardHorizental;