import React, { Component } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SearchBar } from 'react-native-elements';
import { FormLabel, FormInput, FormValidationMessage } from 'react-native-elements';
import Label from '../../../../constants/Label';
import ViewPager from 'react-native-viewpager';
import StepIndicator from 'react-native-step-indicator';
import { AboutYou, Experience, ChooseLanguage, SetAvaiability, VideoIntro } from '../teacher-sign-up';
import I18n from '../../../../constants/locales/i18n';
const PAGES = ['Page 1', 'Page 2', 'Page 3', 'Page 4']
import Color from '../../../../constants/Colors';



const firstIndicatorStyles = {
  stepIndicatorSize: 30,
  currentStepIndicatorSize: 40,
  separatorStrokeWidth: 3,
  currentStepStrokeWidth: 5,
  stepStrokeCurrentColor: Color.darkGreen,
  separatorFinishedColor: Color.darkGreen,
  separatorUnFinishedColor: Color.lightGreen,
  stepIndicatorFinishedColor: Color.darkGreen,
  stepIndicatorUnFinishedColor:  Color.lightGreen,
  stepIndicatorCurrentColor: '#ffffff',
  stepIndicatorLabelFontSize: 15,
  currentStepIndicatorLabelFontSize: 15,
  stepIndicatorLabelCurrentColor: Color.darkGreen,
  stepIndicatorLabelFinishedColor: '#ffffff',
  stepIndicatorLabelUnFinishedColor: 'rgba(255,255,255,0.5)',
  labelColor: '#666666',
  labelSize: 12,
  currentStepLabelColor: Color.darkGreen,
}

class TeacherSignUpScreen extends Component {
  constructor() {
    super();
    var dataSource = new ViewPager.DataSource({
      pageHasChanged: (p1, p2) => p1 !== p2,
    });
    this.state = {
      dataSource: dataSource.cloneWithPages(PAGES),
      currentPage: 0
    }
  }
  static navigationOptions = ({ ...props }) => ({
    headerTintColor: '#ffffff',
    headerTitle: I18n.t('signUpTeacher'),
    headerStyle: {
      backgroundColor: Color.darkGreen
    }
  });

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.stepIndicator}>
          <StepIndicator customStyles={firstIndicatorStyles} stepCount={3} currentPosition={this.state.currentPage} labels={[I18n.t('aboutyou_title'), I18n.t('experience'), /*I18n.t('setAvaiability'),*/ I18n.t('videoIntro')]} />
        </View>
        <ViewPager
          locked={true}
          dataSource={this.state.dataSource}
          renderPage={this.renderViewPagerPage}
          renderPageIndicator={false}
          onChangePage={(page) => { this.setState({ currentPage: page }) }}
        />


      </View>
    );
  }

  renderViewPagerPage = (data) => {
    switch (this.state.currentPage) {
      case 0:
        return <AboutYou onContinue={() => {
          this.setState({ currentPage: 1 })
        }} />;
      case 1:
        return <Experience onContinue={() => {
          this.setState({ currentPage: 2 })
        }} />;
      //case 2:
      //  return <SetAvaiability onContinue={() => {
      //    this.setState({ currentPage: 3 })
      //  }} />;
      case 2:
        return <VideoIntro onContinue={() => {
          this.props.navigation.navigate('SignupSuccess')
        }} />;
    }
    // return (<View style={styles.page}>
    //   <Text>{data}</Text>
    // </View>)
    // console.log({data});
    // return <AboutYou/>;
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ffffff',
  },
  stepIndicator: {
    marginVertical: 20,
  },
  page: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  }
});

export default TeacherSignUpScreen;