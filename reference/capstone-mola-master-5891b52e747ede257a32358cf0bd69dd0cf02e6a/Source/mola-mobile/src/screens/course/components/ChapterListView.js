import React, {Component} from 'react';
import {
  View,
  Text,
  AlertAndroid,
  Alert,
  ListView,
  ListViewDataSource,
  StyleSheet,
  TouchableOpacity,
  InteractionManager,
  RefreshControl,
  Animated,
  Platform,
  Dimensions,
  TouchableHighlight,
  TextInput,
  ActivityIndicator
} from 'react-native';
import {Icon} from 'react-native-elements';
import Prompt from 'react-native-prompt';

import Accordion from 'react-native-collapsible/Accordion';
import Modal from 'react-native-modal';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';
import CourseAPI from '../../../apis/course';
import { currencies } from '../../../../constants/currencies';
import UserSetting from '../../UserSetting';
import ExchangeRates from '../../ExchangeRates';
import accounting from '../../../services/accounting';

class ChapterListView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      courseID: this.props.courseId,
      dataChapters: [],
      loading: true,
      dataSource: new ListView.DataSource({
        rowHasChanged: (row1, row2) => true
      }),
      refreshing: false,
      rowToDelete: null,
      promptVisible: false,
      modalVisible: false,
      text: '',
      chapterId: 0,
      chapterTitle: ''
    }

    this._loadData = this
      ._loadData
      .bind(this);
    this.dataLoadSuccess = this
      .dataLoadSuccess
      .bind(this);
    this._renderHeader = this
      ._renderHeader
      .bind(this);
    this._renderRow = this
      ._renderRow
      .bind(this);
    this._renderContent = this
      ._renderContent
      .bind(this);
    this._udpateListLesson = this
      ._udpateListLesson
      .bind(this);
  }

  async componentDidMount() {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.getChapterByCourse(this.props.courseId);

    this.setState({dataChapters: data.data})

    InteractionManager.runAfterInteractions(() => {
      this._loadData()
    });
  }

  getCurrencySymbol(currencyCode) {
    const index = currencies.findIndex(currency => {
      return currency.code === currencyCode;
    })
    return index >= 0
      ? currencies[index].symbol
      : '';
  }

  _udpateListLesson(listUpdateLesson) {
    console.log('update list', listUpdateLesson);
    const chapterId = listUpdateLesson.chapterId;
    const listUpdatedLesson = listUpdateLesson.listLesson;
    const listChapter = this.state.dataChapters;
    listChapter.forEach((item) => {
      if (item.id === chapterId) {
        item.listLesson = listUpdatedLesson;
      }
    })
    this.setState({dataChapters: listChapter})
  }

  _loadData(refresh) {
    refresh && this.setState({refreshing: true});

    this.dataLoadSuccess({data: this.state.dataChapters});
  }

  dataLoadSuccess(result) {

    this._data = result.data;

    const ds = this
      .state
      .dataSource
      .cloneWithRows(this._data);

    this.setState({loading: false, refreshing: false, rowToDelete: -1, dataSource: ds});
  }

  render() {
    if (!this.state.dataChapters) {
      return (<ActivityIndicator/>)
    }
    return (
      <View style={styles.container}>
        <ListView
          refreshControl={< RefreshControl refreshing = {
          this.state.refreshing
        }
        onRefresh = {
          this
            ._loadData
            .bind(this, true)
        }
        tintColor = "#00AEC7" title = "Loading..." titleColor = "#00AEC7" colors = {
          ['#FFF', '#FFF', '#FFF']
        }
        progressBackgroundColor = "#00AEC7" />}
          enableEmptySections={true}
          dataSource={this.state.dataSource}
          renderRow={this
          ._renderRow
          .bind(this)}/>
      </View>
    );
  }
  _renderHeader(section) {
    return (

      <View style={styles.header}>
        <Text style={styles.headerText}>{I18n.t('chapter') + ' ' + section.number + ': ' + section.title}</Text>

      </View>
    );
  }

  _renderContent(chapter) {
    console.log('chapter', chapter);
    const {coursePrice, isRegisted} = this.props;
    const ds = new ListView.DataSource({
      rowHasChanged: (r1, r2) => r1 !== r2
    });
    return (

      <View key={chapter.id}>
        <ListView
          style={styles.content}
          enableEmptySections={true}
          dataSource={ds.cloneWithRows(chapter.listLesson)}
          renderRow={(rowData) => <View>
          <View
            style={{
            flexDirection: 'row',
            padding: 10,
            justifyContent: 'space-between'
          }}>
            <View style={{
              flexDirection: 'row'
            }}>
              {rowData.finished
                ? <Icon name='check' type='evilicon' color={Color.darkGreen} size={30}/>
                : <View></View>}
              <Text
                style={{
                marginLeft: 10,
                fontSize: 18
              }}>{'Lesson ' + rowData.number + ': ' + rowData.title}</Text>
            </View>
            {isRegisted
            ? <TouchableOpacity
                onPress={() => this.props.navigation.navigate('SelectTimeSlot', {
                lesson: rowData,
                teacherId: this.props.teacher.id,
                price: coursePrice
              })}>
                <Icon name='calendar' type='evilicon' color={Color.grayColor} size={30}/>
              </TouchableOpacity>
            : <View></View>
            }
          </View>
          <View
            style={{
            padding: 10,
            flexDirection: 'row',
            alignItems: 'center'
          }}>
            <Icon name='clock' type='evilicon' color={Color.grayColor} size={25}/>
            <Text style={styles.duration}>{`${rowData.duration} ${I18n.t('minDuration')}`}</Text>
            <Icon name='attach-money' color={Color.grayColor} size={23}/>
            <Text style={styles.duration}>
              {accounting.formatMoney((ExchangeRates.EXCHANGERATES[UserSetting.CURRENCY] * coursePrice * rowData.duration).toFixed(2))
              + ' ' + this.getCurrencySymbol(UserSetting.CURRENCY)}
            </Text>
          </View>
        </View>}></ListView>

      </View>
    );
  }

  _renderRow(rowData, sectionID, rowID) {
    const SECTIONS = [rowData];
    return (
      <View style={styles.listContainer}>
        <Accordion
          sections={SECTIONS}
          renderHeader={this
          ._renderHeader
          .bind(this)}
          renderContent={this._renderContent}/>

      </View>
    );
  }

  componentWillUpdate(nexProps, nexState) {
    if (nexState.rowToDelete !== null) {
      this._data = this
        ._data
        .filter((item) => {
          if (item.id != nexState.rowToDelete) {
            return item;
          }
        });
    }
  }

  _onAfterRemovingElement() {
    this.setState({
      rowToDelete: null,
      dataSource: this
        .state
        .dataSource
        .cloneWithRows(this._data)
    });
  }

}
export default ChapterListView;
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff'
  },
  duration: {
    // marginLeft: 10,
    fontStyle: 'italic'
  },
  content: {
    paddingVertical: 5,
    paddingHorizontal: 10
  },
  headerModel: {
    fontSize: 20,
    fontWeight: 'bold',
    margin: 20
  },
  modalStyle: {
    width: 300,
    height: 200,
    backgroundColor: 'white',
    elevation: 15,
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20
  },
  listContainer: {
    width: '90%',
    alignSelf: 'center',
    backgroundColor: '#eaedf4'
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    backgroundColor: '#f7f6fc',
    padding: 20,
    borderBottomColor: 'white',
    borderBottomWidth: 2
  },
  buttonView: {},
  headerText: {
    // maxWidth:250,
    fontSize: 20,
    flex: 1,
    fontWeight: 'bold'
  },
  noData: {
    color: '#000',
    fontSize: 18,
    alignSelf: 'center',
    top: 200
  },

  addPanel: {
    backgroundColor: '#F9F9F9'
  },
  addButton: {
    backgroundColor: Color.darkGreen,
    width: 120,
    alignSelf: 'flex-end',
    margin: 10,
    padding: 5,
    borderRadius: 5
  },
  addButtonText: {
    color: '#fff',
    alignSelf: 'center'
  },

  rowStyle: {
    backgroundColor: '#FFF',
    paddingVertical: 5,
    paddingHorizontal: 10,
    borderBottomColor: '#ccc',
    borderBottomWidth: 1,
    flexDirection: 'row',
    justifyContent: 'space-between'
  },

  rowIcon: {
    width: 30,
    alignSelf: 'flex-start',
    marginHorizontal: 10,
    fontSize: 24
  },

  name: {
    color: '#212121',
    fontSize: 14
  },
  phone: {
    color: '#212121',
    fontSize: 10
  },
  contact: {
    width: window.width - 100,
    alignSelf: 'flex-start'
  },

  dateText: {
    fontSize: 10,
    color: '#ccc',
    marginHorizontal: 10
  },
  deleteWrapper: {
    paddingVertical: 10,
    marginLeft: 20
  }
});