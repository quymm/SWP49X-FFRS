import React, { Component } from 'react';
import { View, Text } from 'react-native';
import I18n from '../../../constants/locales/i18n';
import Colors from '../../../constants/Colors';

class PolicyScreen extends Component {
  state = {  }
  static navigationOptions = ({
    ...props
  }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('policy'),
    headerStyle: {
        backgroundColor: Colors.darkGreen,
    },
  });
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.header}>{I18n.t('policy')}</Text>
        <View style={styles.main}>
          <Text style={{fontSize:20}}>{I18n.t('policyRule')}</Text>
        </View>
      </View>
    );
  }
}

const styles = {
  container: {
		flex: 1,
    alignItems: 'center',
    padding:10,
    // backgroundColor:'white'
	},
	main:{
    alignItems:'center',
    backgroundColor:'white',
    borderWidth:1,
    borderColor:'black',
    width:'90%',
    marginTop:30,
    padding:20
  },
  header:{
    fontSize:30
  }

}
export default PolicyScreen;