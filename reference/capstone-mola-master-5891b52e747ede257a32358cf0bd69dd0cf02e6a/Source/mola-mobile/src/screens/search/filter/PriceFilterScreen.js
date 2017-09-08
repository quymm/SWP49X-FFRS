import React, { Component } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import MultiSlider from '@ptomasroos/react-native-multi-slider';
import { Icon, Button } from 'react-native-elements';
import {connect} from 'react-redux';

import _ from 'underscore';

import { filterPriceApply, filterPriceCancel } from './actions';

import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';

let self;
const MIN_PRICE = 0,
      MAX_PRICE = 20;

const mapStateToProps = (state) => ({ 
  filter: state.searchFilter.filterBy,
});


@connect(mapStateToProps, {filterPriceApply, filterPriceCancel})
class PriceFilterScreen extends Component {
  constructor(props){
    super(props);
    this.state = {
      minPrice: MIN_PRICE,
      maxPrice: MAX_PRICE
    };

    self = this;
  }

  static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('filterByPrice'),
    headerRight: (<TouchableOpacity onPress={() => {self._onPressCancel()}}>
                  <Text style={{ marginRight: 30, color: Colors.whiteColor }}>&#10005; {I18n.t('cancel')}</Text>
                </TouchableOpacity>),
    headerStyle: {
      backgroundColor: Colors.darkGreen,
    },
  });

  componentDidMount() {
    if (_.isEmpty(this.props.filter.price)) {
      return;
    }
    const { minPrice, maxPrice } = this.props.filter.price;

    this.setState({ minPrice, maxPrice });
  }
  sliderOneValuesChange = (values) => {
    const [minPrice, maxPrice]  = values;
    this.setState({
      minPrice,
      maxPrice 
    });
  }
  async _onPressApply(){
    const {filter} = this.props;
    
    const newFilter = Object.assign({}, filter);
    
    const { minPrice, maxPrice } = this.state;
    newFilter.price = { minPrice, maxPrice};

    await this.props.filterPriceApply(newFilter);
    this.props.navigation.goBack();
  }
  async _onPressCancel(){
    const {filter} = this.props;
    
    const newFilter = Object.assign({}, filter);
    
    newFilter.price = {};

    await this.props.filterPriceCancel(newFilter);
    this.props.navigation.goBack();
  }

  render() {
    console.log('state', this.state);
    return (
      <View style={ styles.slider }>
        <Text>Min: {this.state.minPrice}</Text>
        <Text>Max: {this.state.maxPrice}</Text>
        <MultiSlider
            ref={s => this._slider = s}
            values={[this.state.minPrice, this.state.maxPrice]}
            min={0}
            max={20}
            step={0.5}
            sliderLength={280}
            onValuesChange={this.sliderOneValuesChange}
            selectedStyle={{
              backgroundColor: 'gold',
            }}
            unselectedStyle={{
              backgroundColor: 'silver',
            }}
            trackStyle={{
              height:8,
            }}
            touchDimensions={{
              height: 40,
              width: 40,
              borderRadius: 20,
              slipDisplacement: 40,
            }}
            customMarker={
              () => <Icon name='monetization-on' size={25} color={Colors.orangeColor} />
            }
            />

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

export default PriceFilterScreen;
const styles = {
  containter: {
    flex: 1,
    backgroundColor: 'white'
  },
  slider: {
    alignItems: 'center',
    marginTop: '50%'
  },
  buttonTextStyle:{
    fontSize: 20
  },
  buttonApply: {
    width: '100%'
  }
}