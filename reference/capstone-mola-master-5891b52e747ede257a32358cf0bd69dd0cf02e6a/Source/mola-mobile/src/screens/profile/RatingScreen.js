import React, { Component } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet } from 'react-native';
import { List, ListItem, Button, Icon, } from 'react-native-elements';

import I18n from '../../../constants/locales/i18n';
import Colors from '../../../constants/Colors';
import ListRating from './components/ListRating';

class RatingScreen extends Component {
    constructor(props) {
        super(props);
        this.state = {
            listRating: this.props.navigation.state.params,
        }
    }

    static navigationOptions = ({
    ...props
  }) => ({
            headerTintColor: '#ffffff',
            headerTitle: I18n.t('rating'),
            headerStyle: {
                backgroundColor: Colors.darkGreen,
            },
        });

    render() {
        console.log('asdasdasdasd', this.state.listRating);
        return (
            <ScrollView style={css.container}>
                <Text style={css.titles}>Ratings({this.state.listRating.rating.length})</Text>

                <List style={{ marginTop: 1, padding: 10 }}>
                    {this.state.listRating.rating.map((rating, i) => {
                            return <ListRating key={i} rating={rating}  />
                        })
                    }
                </List>
            </ScrollView>
        );
    }
}
var css = StyleSheet.create({
    container: {
        flex: 1,
        marginTop: 10,
        margin: 5,
        backgroundColor: '#e9ebee',
    },
    titles: {
        fontSize: 18,
        marginLeft:10,
        fontWeight:'bold',
        color: Colors.blackColor
    },
    text: {
        fontSize: 20
    },
    button: {
        margin: 30,
        backgroundColor: Colors.darkGreen,
        height: 50,
        width: 200,
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 10
    }
})

export default RatingScreen;