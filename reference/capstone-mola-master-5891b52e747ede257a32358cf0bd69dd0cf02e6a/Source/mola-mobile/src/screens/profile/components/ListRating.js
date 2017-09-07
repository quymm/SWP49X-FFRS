import React, { Component } from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity } from 'react-native';
import Colors from '../../../../constants/Colors';
import { Icon } from 'react-native-elements';
import StarRating from 'react-native-star-rating';
import moment from 'moment';

class ListRating extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        const ratingInfo  = this.props.rating;
        console.log('rating', this.props.rating)
        return (         
                    <View style={css.ratingView}>
                        <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 10 }}>
                            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                                <Image
                                    style={css.ratingAvatar}
                                    source={{ uri: ratingInfo.user.avatar }} />
                                <View>
                                    <Text style={{ marginBottom: 5 }}>{ratingInfo.user.firstName} {ratingInfo.user.lastName}</Text>
                                    <Text>Quốc tịch</Text>
                                </View>
                            </View>
                            <View style={{ marginRight: 10 }}>
                                <Text style={{ marginBottom: 5 }}> {moment(ratingInfo.ratingTeacher.time).format('DD-MM-YYYY')}</Text>
                                <StarRating
                                    disabled
                                    starColor={Colors.starColor}
                                    emptyStarColor={'gray'}
                                    starSize={18}
                                    maxStars={5}
                                    rating={ratingInfo.ratingTeacher.rating}
                                />
                            </View>
                        </View>
                        <View>
                            <Text>{ratingInfo.ratingTeacher.comment}</Text>
                        </View>
                    </View>
        );
    }
}

var css = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: Colors.whiteColor
    },
    ratingView: {
        borderRadius: 5,
        padding: 15,
        marginTop: 5,
        marginBottom: 5,
        backgroundColor:'white'
    },
    image: {
        width: '100%',
        height: 200
    },
    profileRegion: {
        alignItems: 'center',
        margin: 20,
        flexDirection: 'row'
    },
    ratingAvatar: {
        width: 50,
        height: 50,
        borderRadius: 60,
        marginRight: 20
    },
    avatar: {
        width: 80,
        height: 80,
        borderRadius: 60,
        marginRight: 40
    },
    rowView: {
        flexDirection: 'row',
        flex: 1,
    },
    basicProfile: {
        fontWeight: "bold",
        fontSize: 20,
        color: Colors.blackColor,
        marginLeft: 20
    },
    regions: {
        marginTop: 10,
        marginLeft: 10,
        marginRight: 10,
        paddingLeft: 10,
        paddingRight: 10
    },
    ratingRegions: {
      
    },
    titles: {
        fontSize: 18,
        color: Colors.blackColor
    },
    language: {
        alignSelf: 'flex-start',
        backgroundColor: Colors.darkGreen,
        marginTop: 5,
        paddingHorizontal: 8,
        borderRadius: 4,
        color: Colors.whiteColor
    },
    ratingArea: {
        alignItems: 'center',
        marginLeft: 20
    },
    rating: {
        color: Colors.darkGreen,
        fontSize: 20,
        fontWeight: 'bold'
    }
})

export default ListRating;