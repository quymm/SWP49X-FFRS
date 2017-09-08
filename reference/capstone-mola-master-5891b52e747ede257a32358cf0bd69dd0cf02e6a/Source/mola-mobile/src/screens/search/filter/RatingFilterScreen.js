import React, { Component } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { ListItem, CheckBox, Button } from 'react-native-elements';
import StarRating from 'react-native-star-rating';

import { connect } from 'react-redux';

import { filterRatingApply, filterRatingCancel } from './actions';

import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';


let self;
const mapStateToProps = (state) => ({ 
  searchResults: state.search.courses.data.data,
  filter: state.searchFilter.filterBy,
});

@connect(mapStateToProps, {filterRatingApply, filterRatingCancel})
class RatingFilterScreen extends Component {
  constructor(props){
    super(props);
    this.state = {
      rating: {},
      value: 0,
    };

    self = this;
  }

  static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('filterByRating'),
    headerRight: <TouchableOpacity onPress={() => {self._onPressCancel()}}>
                  <Text style={{ marginRight: 30, color: Colors.whiteColor }}>&#10005; {I18n.t('cancel')}</Text>
                </TouchableOpacity>,
    headerStyle: {
      backgroundColor: Colors.darkGreen,
    },
  });

  async _onPressApply(){
    const {filter} = this.props;
    
    const newFilter = Object.assign({}, filter);
    
    const { rating } = this.state;
    let star;
    for (let key of Object.keys(rating)) {
      if(rating[key].checked){
        star = key;
        break;
      }
    }
    newFilter.rating =  star;

    await this.props.filterRatingApply(newFilter);
    this.props.navigation.goBack();
  }
  async _onPressCancel(){
    const {filter} = this.props;
    
    const newFilter = Object.assign({}, filter);
    
    newFilter.rating = -1;

    await this.props.filterRatingCancel(newFilter);
    this.props.navigation.goBack();
  }

  componentDidMount() {
    const rating = {
        0: {
          count: 0,
          checked: false
        },
        1: {
          count: 0,
          checked: false
        },
        2: {
          count: 0,
          checked: false
        },
        3: {
          count: 0,
          checked: false
        },
        4: {
          count: 0,
          checked: false
        },
        5: {
          count: 0,
          checked: false
        },
      };
    const { searchResults } = this.props;
    searchResults.map(data => {
      
      const ratingPoint = data.course.rating;
      rating[ratingPoint].count = rating[ratingPoint].count + 1;

    });

    this.setState({ rating });
  }
  _check(star){
    const { rating } = this.state;
    for (let key of Object.keys(rating)) {
      rating[key].checked = false;
    }
    rating[star].checked = true;
    console.log(rating)
    this.setState({ rating });
  }
  _renderListItemCheckBox(){
    const checkBoxGroup = [];
    const { rating } = this.state;
    for (let star of Object.keys(rating)) {
      checkBoxGroup.push(
        <ListItem
          key={star}
          onPress={() => this._check(star)}
          rightIcon={<CheckBox
                      checkedIcon='check'
                      uncheckedColor='white'
                      
                      checkedColor={Colors.darkGreen}
                      checked={rating[star].checked}
                      containerStyle={styles.checkBoxStyle}
                      />}
          title={<View style={{maxWidth: '40%'}}><StarRating
                          disabled
                          starColor={'#f1c40f'}
                          starSize={18}
                          maxStars={5}
                          rating={Number(star)}
                        /></View>}
          rightTitle={<Text>{rating[star].count} </Text>}
          
        />

      );
    }
    return checkBoxGroup;
  }
  
  render() {
    const checkBoxGroup = this._renderListItemCheckBox();
    return (
      <View style={styles.containter}>
        {checkBoxGroup.map(checkbox => 
          checkbox
        )}
        <Button
          title='Apply'
          onPress={() => this._onPressApply()}
          raised
          size={25}
          backgroundColor={ Colors.lightOrangeColor }
          textStyle={ styles.buttonTextStyle }
          icon={{name: 'check', size: 25}}/>
      </View>
    );
  }
}

export default RatingFilterScreen;
const styles = {
  containter: {
    flex: 1,
    backgroundColor: 'white'
  },
  checkBoxStyle: {
    // marginTop: 10,
    marginBottom: 0,
    paddingBottom: 0,
    paddingLeft: 0,
    marginRight: 0,
    marginLeft: 0,
    backgroundColor: 'white',
    borderRadius: 0,
    borderWidth:0,
    borderTopWidth: 0,
    borderLeftWidth: 0,
    borderRightWidth: 0
  },
  buttonTextStyle:{
    fontSize: 20
  },
  buttonApply: {
    width: '100%'
  }
}