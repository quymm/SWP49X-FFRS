import React, { Component } from 'react';
import { Text, TouchableHighlight, TouchableOpacity, View, TextInput, ScrollView } from 'react-native';
import { List, ListItem, Icon } from 'react-native-elements';
import { connect } from 'react-redux';
import Placeholder from 'rn-placeholder';
import _ from 'underscore';
import { searchCourse } from './actions';

import { getListComingLesson } from '../../../constants/api';
import CourseCard from './components/CourseCard';
import CardHorizental from './components/CardHorizental';
import RecomendationAPI from '../../apis/recomendation';
import SearchAPI from '../../apis/search';


import I18n from '../../../constants/locales/i18n';
import Colors from '../../../constants/Colors';
import { countriesPopular, countries } from '../../../constants/countries';

const recomendation = new RecomendationAPI();
const search = new SearchAPI();

const mapStateToProps = (state) => ({ 
  courses: state.search.courses,
  user: state.auth.user.data.data,
  searchResults: state.search.courses.data.data
});

@connect(mapStateToProps, { searchCourse })
class SearchScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      recomendCourse: [],
      courseSearchResult: [],
      isLoading: false,
      searchValue: '',
      lastSearchValue: '',
    };

  }

  static navigationOptions = ({ navigation }) => {
    return { header: null }
  };
  goBack() {
    this.props.navigation.goBack();
  }
  async search() {
    const { searchValue } = this.state;
    await this.setState({isLoading: true});
    
    await this.props.searchCourse(searchValue);

    console.log('this.props', this.props)
    const searchResults = this.props.searchResults;
    this.setState({
      courseSearchResult: [...searchResults],
      lastSearchValue: searchValue,
      isLoading: false,
    });
    
  }

  async componentWillMount() {
    const list = await getListComingLesson();
    this.setState({ listComingLesson: list });
  }
  async componentDidMount() {
    const {user} = this.props;
    const {username} = user.user;
    const recomendCourse = await recomendation.recomendCourse(username);
    
    if (recomendCourse.status === 'ok') {
      await this.setState({recomendCourse: recomendCourse.data});
    }
  }

  _goToFilterScreen(){
    if(this.state.lastSearchValue !== this.state.searchValue){
      this.search();
    }
    // this.search();
    this.props.navigation.navigate('SearchFilter', {count: this.props.searchResults.length});
  }
  

  render() {
    const { recomendCourse, courseSearchResult } = this.state;
    const { searchResults } = this.props;
    return (
      <View>
        <ListItem
            key={1}
            hideChevron={true}
            containerStyle={{
              marginTop: 0,
              backgroundColor: 'white'
            }}
            leftIcon={
              <View style={styles.input}>
                <Icon
                  onPress={() => this.goBack()}
                  name='arrow-back'
                  size={28}
                  color={Colors.darkGreen}
                  style={{
                      marginRight: 20
                  }} />
                <TextInput
                  style={styles.textInput}
                  underlineColorAndroid='transparent'
                  autoFocus={true}
                  returnKeyType='search'
                  placeholder={I18n.t('search')}
                  placeholderTextColor='#98948F'
                  onChangeText={(searchValue) => this.setState({ searchValue })}
                  onSubmitEditing={this.search.bind(this)}
                ></TextInput>
              </View>
            }
            rightIcon={{name: 'search'}}
            onPressRightIcon={() => this.search.bind(this)}
          />
        <ScrollView>
          
          <View />
          <View style={{
            flex: 1
          }}>
            <ListItem
              title={I18n.t('searchRecomnend')}
              leftIcon={<Icon name='whatshot' color='#f39c12' />}
              hideChevron
              containerStyle={{
                borderBottomWidth: 0,
                backgroundColor: 'white'
              }}
              titleStyle={styles.titleStyle} />
            <ScrollView horizontal={true}>
              {
                recomendCourse &&
                recomendCourse.map((recomend, i) => {
                  return <CardHorizental key={i} style={{backgroundColor: Colors.whiteColor}} recomend={recomend}  navigation={this.props.navigation} />
                })
              }
            </ScrollView>

            {searchResults && searchResults.length > 0
            ? <View style={{ backgroundColor: Colors.whiteColor, flexDirection: 'row',  padding: 10 }}>
              <Icon
                name='filter-list'
                color={Colors.blueColor}
                iconStyle={{marginLeft: 10, marginRight: 10}}
                onPress={() => this._goToFilterScreen()}/>
              <Text style={{ color: Colors.grayColor, fontSize: 18, fontWeight: 'bold' }}>Filter by</Text>

             
            </View>
            : <View>
              <Placeholder.ImageContent
                size={120}
                firstLineWidth="70%"
                animate="fade"
                color={'#ecf0f1'}
                lineNumber={5}
                lineSpacing={9}
                lastLineWidth="30%"
                onReady={!this.state.isLoading} />
              
            </View>
          
            }
            <List style={{}}>
              {
                searchResults && searchResults.map((course, i) => {
                  return <CourseCard key={i}
                                    style={{backgroundColor: Colors.whiteColor}}
                                    course={course}
                                    navigation={this.props.navigation} />
                })
              }
            </List>
          </View>

        </ScrollView>
      </View>
    );
  }
}

export default SearchScreen;

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#FFF'
  },
  titleStyle: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#f39c12'
  },
  labelContainer: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: 'wrap'
  },
  label: {
    height: 40,
    fontSize: 16,
    backgroundColor: '#2ecc71',
    borderRadius: 5,
    color: '#ffffff',
    margin: 5,
    padding: 10
  },
  row: {
    padding: 10,
    height: 44
  },
  input: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: Colors.whiteColor
  },
  textInput: {
    width: 200,
    color: '#98948F',
    fontSize: 20
  },

};