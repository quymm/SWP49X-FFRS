import React, { Component } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet, ActivityIndicator,RefreshControl } from 'react-native';
import { List, ListItem, Button, Icon, } from 'react-native-elements';
import Colors from '../../../../constants/Colors';
import I18n from '../../../../constants/locales/i18n';
import ListRequest from '../components/ListRequest';
import RequestAPI from '../../../apis/request';

class RequestScreen extends Component {
  constructor(props) {
    super(props);
    this.state = {
      listResquest: [],
      loading: true,
      refreshing:false,
    }
    this.loadRequest = this.loadRequest.bind(this);
    this.setLoading = this.setLoading.bind(this);
  }
  setLoading(load){
    this.setState({loading:load});
  }
  componentDidMount() {
    this.loadRequest();
  }

  async _onRefresh() {
    this.setState({refreshing: true});
    const requestAPI = new RequestAPI();
    const data = await requestAPI.getIncomingRequest();
    this.setState({
      listResquest: data.data.listIncomingRequest,
      loading: false,
      refreshing: false,
    })
  }

  async loadRequest(){
    this.setState({loading:true});
    const requestAPI = new RequestAPI();
    const data = await requestAPI.getIncomingRequest();
    this.setState({
      listResquest: data.data.listIncomingRequest,
      loading: false,
    })
  }
  static navigationOptions = ({ title: I18n.t('request') });
  render() {
    if (this.state.loading) {
      return (
        <ActivityIndicator
          size="large"
          style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }} />
      )
    }
    return (
      <ScrollView style={{ backgroundColor: '#e9ebee' }}
        refreshControl={
          <RefreshControl
            refreshing={this.state.refreshing}
            onRefresh={this._onRefresh.bind(this)}
            tintColor={Colors.darkGreen} title="Loading..." titleColor={Colors.darkGreen} colors={
              ['#FFF', '#FFF', '#FFF']
            }
            progressBackgroundColor={Colors.darkGreen}
          />
        }
      >
        {this.state.listResquest.length > 0
          ? <List style={{ marginTop: 1, padding: 10 }}>
            {this.state.listResquest
              .map((request, i) => (
                <ListRequest key={i}
                  setLoading = {this.setLoading}
                  loadRequest = {this.loadRequest}
                  style={{ backgroundColor: 'white' }}
                  request={request}
                  mainNavigation={this.props.screenProps} />
              )
              )}
          </List>
          : <View style={css.container}>
            <Text style={css.title}>{I18n.t('anyRequest')}</Text>
          </View>

        }

      </ScrollView>
    );
  }
}
var css = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    marginTop: 50,
    margin: 10,
    padding: 10
  },
  title: {
    fontSize: 25,
    // fontWeight: 'bold'
  },
  text: {
    marginTop: 10,
    marginLeft: 10,
    fontSize: 18,
    fontWeight: 'bold'
  }
  ,
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


export default RequestScreen;