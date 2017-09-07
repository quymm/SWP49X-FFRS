import React, { Component } from 'react';
import { View, Text, ScrollView, TouchableOpacity } from 'react-native';
import { CheckBox, Button, Icon } from 'react-native-elements';
import {connect} from 'react-redux';

import { filterLanguagesApply, filterLanguagesCancel } from './actions';

import { countriesPopular } from '../../../../constants/countries';
import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';

const mapStateToProps = (state) => ({ 
  searchResults: state.search.courses.data.data,
  filter: state.searchFilter.filterBy,
});

let self;

@connect(mapStateToProps, {filterLanguagesApply, filterLanguagesCancel})
class LanguageFilterScreen extends Component {
  constructor(props){
    super(props);

    this.state = {
      listCheckBox: [],
    };
    self = this;

  }

  static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('filterLanguage'),
    headerRight: (<TouchableOpacity onPress={() => {self._onPressCancel()}}>
                  <Text style={{ marginRight: 30, color: Colors.whiteColor }}>&#10005; Cancel</Text> 
                </TouchableOpacity>),
    headerStyle: {
      backgroundColor: Colors.darkGreen,
    },
  });
  componentWillMount() {
    const listCheckBox = [];
    countriesPopular && countriesPopular.map(language => {
      listCheckBox.push({language, checked: false});
    });

    this.setState({listCheckBox});
  }

  componentDidMount() {
    const filterLanguage = this.props.filter.languages;
    if (!filterLanguage) {
      return;
    }
    const { listCheckBox }= this.state;
    filterLanguage.map(language => {
      const index = listCheckBox.findIndex(checkbox => checkbox.language === language);
      if (index >= 0) {
        listCheckBox[index].checked = true;
      }
    })

    this.setState({listCheckBox});
  }
  _check(language){
    const { listCheckBox } = this.state;
    const index = listCheckBox.findIndex(checkbox => checkbox.language === language);
    if (index >= 0) {
      listCheckBox[index].checked = !listCheckBox[index].checked;
    }
    this.setState({listCheckBox});
  }
  _displayButtonApply(){
    const { listCheckBox } = this.state;
    const index = listCheckBox.findIndex(checkbox => checkbox.checked === true);
    return index >= 0;
  }

  async _onPressApply(){
    const { listCheckBox } = this.state;
    const listLanguagesChecked = listCheckBox.filter(checkbox => checkbox.checked === true)
                                            .map(checkbox => checkbox.language);
                                            console.log('props', this.props)
    const {filter} = this.props;
    const newFilter = Object.assign({}, filter);
    newFilter.languages = [...listLanguagesChecked];
    await this.props.filterLanguagesApply(newFilter);
    this.props.navigation.goBack();
  }
  async _onPressCancel(){
    const {filter} = this.props;
    
    const newFilter = Object.assign({}, filter);
    newFilter.languages = [];
    
    await this.props.filterLanguagesCancel(newFilter);
    this.props.navigation.goBack();
  }
  render() {
    const { listCheckBox } = this.state;
    
    return (
      <ScrollView style={styles.containter}>
        <View style={styles.containter}>
          {
            listCheckBox.map(checkbox => {
              return <CheckBox
                key={checkbox.language.name}
                title={checkbox.language.name}
                checkedIcon='check-square'
                uncheckedIcon='square-o'
                onPress={() => this._check(checkbox.language)}
                checked={checkbox.checked}
              />
            })
          }
        </View>
        {
          this._displayButtonApply() 
          ? <Button
            title='Apply'
            onPress={() => this._onPressApply()}
            raised
            size={25}
            backgroundColor={ Colors.lightOrangeColor }
            textStyle={ styles.buttonTextStyle }
            icon={{name: 'check', size: 25}}
            />
          : null
        }
      </ScrollView>
    );
  }
}

export default LanguageFilterScreen;
const styles = {
  containter: {
    flex: 1,
    backgroundColor: 'white'
  },
  buttonTextStyle:{
    fontSize: 20
  },
  buttonApply: {
    width: '100%'
  }
}