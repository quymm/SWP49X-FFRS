import React, {Component} from 'react';
import {View, Text, TouchableOpacity} from 'react-native';
import {List, ListItem, Button} from 'react-native-elements'

import { connect } from 'react-redux';

import { searchFilterApply, searchFilterCancel } from './actions';

import _ from 'underscore';

import I18n from '../../../constants/locales/i18n';
import Colors from '../../../constants/Colors';

const mapStateToProps = (state) => ({ 
  searchResults: state.search.courses.data.data,
  filter: state.searchFilter.filterBy,

});
let self;

@connect(mapStateToProps, {searchFilterApply, searchFilterCancel})
class SearchFilterScreen extends Component {
  constructor(props){
    super(props);

    self = this;
  }
  static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: `${I18n.t('filter')} (${props.navigation.state.params.count} courses)`,
    headerRight: <TouchableOpacity onPress={() => {self._onPressCancel()}}>
                  <Text style={{ marginRight: 30, color: Colors.whiteColor }}>&#10005; Cancel</Text>
                </TouchableOpacity>,
    headerStyle: {
      backgroundColor: Colors.darkGreen,
    },
  });

  _goToScreen(screenName) {
    this.props.navigation.navigate(screenName);
  }
  async _onPressApply(){
    const { filter, searchResults } = this.props;
    const { languages, price, rating } = filter;

    if (
      languages.length === 0
    && _.isEmpty(price)
    && rating < 0
    ) {
      this.props.navigation.goBack();
      return;
    }

    let newSearchResult = [];
    
    if (languages.length > 0) {
      languages.map(language => {
        const matchLangugage = searchResults.filter(data => {
          console.log(data.course.language);
          return data.course.language.toLowerCase() === language.toLowerCase();
        });
        newSearchResult = [...newSearchResult, ...matchLangugage];
      })
    }

    if (!_.isEmpty(price)){
      const { minPrice, maxPrice } = price;
      const matchPrice =  searchResults.filter(data => data.course.price >= minPrice && data.course.price <= maxPrice);
      newSearchResult = [...newSearchResult, ...matchPrice];
    }

    if(rating >=0) {
      const matchRating = searchResults.filter(data => data.course.rating >= rating);
      newSearchResult = [...newSearchResult, ...matchRating];
    }

    console.log('newSearchResult',newSearchResult);
    // filter duplicated
    newSearchResult = newSearchResult.filter((data, index, arr) => arr.indexOf(data.course.id) !== index);


    await this.props.searchFilterApply({data: newSearchResult});
    this.props.navigation.goBack();
  }
  async _onPressCancel(){
    await this.props.searchFilterCancel();
    this.props.navigation.goBack();
  }
  render() {
    const { filter } = this.props;
    
    return (
      <View style={styles.container}>
        <Text style={styles.text}>{I18n.t('filterBy')}</Text>
        <List containerStyle={{ marginBottom: 10, borderWidth: 0 }}>
          <ListItem
            key={1}
            title={I18n.t('filterLanguage')}
            onPress={() => this._goToScreen('LanguageFilter')}
            leftIcon={{
                name: 'language',
                color: Colors.darkGreen,
            }}
            rightTitle={
              filter.languages && filter.languages.length > 0
              ? filter.languages.join(', ')
              : 'All'
            }
          />
          <ListItem
            key={2}
            title={I18n.t('filterByPrice')}
            onPress={() => this._goToScreen('PriceFilter')}
            leftIcon={{
                name: 'attach-money',
                color: Colors.darkGreen,
            }}
            rightTitle={
              !_.isEmpty(filter.price)
              ? `$${filter.price.minPrice} ${I18n.t('to')} $${filter.price.maxPrice}`
              : 'All'
            }
          />
          <ListItem
            key={3}
            onPress={() => this._goToScreen('RatingFilter')}
            title={I18n.t('filterByRating')}
            leftIcon={{
                name: 'rate-review',
                color: Colors.darkGreen,
            }}
            rightTitle={
              filter.rating >= 0
              ? `Up to ${filter.rating} stars`
              : 'All'
            }
          />
        </List>
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

export default SearchFilterScreen;

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#FFF'
  },
  buttonTextStyle:{
    fontSize: 20
  },
  text: {
    marginTop: 10,
    marginLeft: 10,
    fontSize: 18,
    fontWeight: 'bold'
  }
}