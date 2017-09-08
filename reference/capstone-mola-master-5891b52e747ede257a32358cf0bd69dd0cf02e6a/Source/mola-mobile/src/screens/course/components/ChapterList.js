import React, { Component } from 'react';
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
  ActivityIndicator,
} from 'react-native';
import { Icon } from 'react-native-elements';
import Prompt from 'react-native-prompt';

import Accordion from 'react-native-collapsible/Accordion';
import Modal from 'react-native-modal';
import I18n from '../../../../constants/locales/i18n';
import Color from '../../../../constants/Colors';
import CourseAPI from '../../../apis/course';


class ChapterList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      courseID: this.props.navigation.state.params.courseInfo.id,
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
      chapterTitle: '',
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
    this._addItemPressed = this
      ._addItemPressed
      .bind(this);
    this._renderContent = this._renderContent.bind(this);
    this._udpateListLesson = this._udpateListLesson.bind(this);
  }
  async _editChapter(chapter) {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.editChapter(chapter);
    this.setState({
      chapter: data.data
    })
  }
  async _addChapter(chapter) {
    const courseAPI = new CourseAPI();
    const data = await courseAPI.addChapter(chapter);
    this.setState({
      chapter: data.data
    })
  }
  async componentDidMount() {
    
    const courseAPI = new CourseAPI();
    const data = await courseAPI.getChapterByCourse(this.state.courseID);
    console.log(data);
    this.setState({
      dataChapters: data.data
    })

    InteractionManager.runAfterInteractions(() => {
      this._loadData()
    });
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
    this.setState({
      dataChapters: listChapter
  })
  }


  _loadData(refresh) {
    refresh && this.setState({ refreshing: true });

    this.dataLoadSuccess({ data: this.state.dataChapters });
  }

  dataLoadSuccess(result) {

    this._data = result.data;

    const ds = this.state.dataSource.cloneWithRows(this._data);

    this.setState({ loading: false, refreshing: false, rowToDelete: -1, dataSource: ds });
  }

  render() {
    if (!this.state.dataChapters) {
      return (<ActivityIndicator />)
    }

    return (
      <View style={styles.container}>
        <View style={styles.addPanel}>

          <TouchableOpacity
            style={styles.addButton}
            onPress={() => this._addItemPressed()}>
            <Text style={styles.addButtonText}>+ {I18n.t('new')}</Text>
          </TouchableOpacity>

        </View>
        <Prompt
          title={I18n.t('chapterName')}
          placeholder={I18n.t('search_placeholder')}
          defaultValue=""
          visible={this.state.promptVisible}
          onCancel={() => this.setState({ promptVisible: false, message: "You cancelled" })}
          onSubmit={(value) => this._addItem(value)} />
        <ListView
          refreshControl={< RefreshControl refreshing={
            this.state.refreshing
          }
            onRefresh={
              this
                ._loadData
                .bind(this, true)
            }
            tintColor="#00AEC7" title="Loading..." titleColor="#00AEC7" colors={
              ['#FFF', '#FFF', '#FFF']
            }
            progressBackgroundColor="#00AEC7" />}
          enableEmptySections={true}
          dataSource={this.state.dataSource}
          renderRow={this
            ._renderRow
            .bind(this)} />
      </View>
    );
  }
  _edit(chapter) {
    this._data.forEach(function (item) {
      if (item.id == chapter.id) {
        item.title = chapter.title;
      }
    })
    this.setState({
      promptVisible: false,
      rowToDelete: -1,
      dataSource: this.state.dataSource.cloneWithRows(this._data)
    });
    this._editChapter({
      id: chapter.id,
      title: chapter.title,
      number: chapter.number,
      courseId: chapter.courseID
    })
  }
  _renderHeader(section) {
    return (

      <View style={styles.header}>
        <Text style={styles.headerText}>{I18n.t('chapter') + ' ' + section.number + ': ' + section.title}</Text>

      </View>
    );
  }

  _confirmDelete(id, name, number) {
    Alert.alert(
      'Do you want to delete chapter: ' + name,
      null,
      [
        { text: 'Cancel' }, { text: 'OK', onPress: () => this._deleteChapter(id, number) }
      ],
      {
        cancelable: false
      }
    )
  }
  async _deleteChapter(id, number) {
    const courseAPI = new CourseAPI();
    const respond = await courseAPI.deleteChapter(id);
    if (respond.status == 'ok') {
      this._data = this._data.filter((item) => {
        if (item.id != id) {
          return item;
        }
      });
      this._data.forEach((item) => {
        if(item.number > number){
          item.number = item.number - 1;
        }
      })
      const ds = this.state.dataSource.cloneWithRows(this._data);
      this.setState({ dataSource: ds });
    }

  }

  setModalVisible(visible) {
    this.setState({ modalVisible: visible });
  }

  _renderContent(chapter) {

    const ds = new ListView.DataSource({ rowHasChanged: (r1, r2) => r1 !== r2 });
    return (

      <View key={chapter.id}>
        <Modal
          animationType={"slide"}
          transparent={true}
          onRequestClose={() => console.log("dcm")}
          visible={this.state.modalVisible}>
          <View style={{
            flex: 1,
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center'
          }}>

            <View style={styles.modalStyle} >
              <Text style={styles.headerModel}>Edit Chapter</Text>
              <View style={{ width: '100%', marginBottom: 10 }}>
                <Text>Chapter Name :</Text>
                <TextInput
                  onChangeText={(chapterTitle) => this.setState({ chapterTitle })}
                  value={this.state.chapterTitle}
                  style={{ marginLeft: 20, marginRight: 20 }}></TextInput>
              </View>
              <View style={{ flexDirection: 'row' }}>
                <TouchableOpacity
                  style={styles.addButton}
                  onPress={() => {
                    this.setModalVisible(!this.state.modalVisible)
                  }}>
                  <Text style={styles.addButtonText}>Cancel</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={styles.addButton}
                  onPress={() => {
                    const chapter = {
                      id: this.state.chapterId,
                      title: this.state.chapterTitle,
                      number: this.state.number,
                      courseID: this.state.courseID,
                    }
                    this._edit(chapter);
                    this.setModalVisible(!this.state.modalVisible)
                  }}>
                  <Text style={styles.addButtonText}>Confirm</Text>
                </TouchableOpacity>

              </View>
            </View>
          </View>
        </Modal>
        <ListView style={styles.content}
          enableEmptySections={true}
          dataSource={ds.cloneWithRows(chapter.listLesson)}
          renderRow={(rowData) => <Text>{'Lesson ' + rowData.number + ': ' + rowData.title}</Text>}
        >
        </ListView>
        <View style={{ flexDirection: 'row', padding: 10, justifyContent: 'space-between' }}>
          <View>
            <TouchableOpacity onPress={() => this.props.navigation.navigate('CreateLesson', { courseInfo: this.props.courseInfo, chapterInfo: chapter, updateListLesson: this._udpateListLesson.bind(this) })}>
              <Icon name='add-box' color='#3bb64b' size={35} />
            </TouchableOpacity>
            <Text>{I18n.t('editLesson')}</Text>
          </View>
          <View>
            <TouchableOpacity style={{}}
              onPress={() => {
                this.setState({
                  chapterId: chapter.id,
                  chapterTitle: chapter.title,
                  number: chapter.number
                })

                this.setModalVisible(!this.state.modalVisible)
              }}>
              <Icon name='mode-edit' color='#2e3359' size={35} />
            </TouchableOpacity>
            <Text>{I18n.t('editChapter')}</Text>
          </View>
          <View>
            <TouchableOpacity style={{}}
              onPress={() => {
                this._confirmDelete(chapter.id, chapter.title, chapter.number)
              }}>
              <Icon name='delete' color='#d5283c' size={35} />
            </TouchableOpacity>
            <Text>{I18n.t('deleteChapter')}</Text>
          </View>
        </View>
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
          renderContent={this._renderContent} />

      </View>
    );
  }

  async _addItemPressed() {
    await this.setState({ promptVisible: true });
  }

  async _addItem(name) {

    const chapter = {
      title: name,
      number: (this._data.length + 1),
      courseId: this.state.courseID,
      listLesson: []
    }
    const courseAPI = new CourseAPI();
    const respond = await courseAPI.addChapter(chapter);
    if (respond.status == 'ok') {
      this._data
        .push({
          id: respond.data.id,
          title: respond.data.title,
          number: respond.data.number,
          courseID: this.state.courseID,
          listLesson: []
        });
      this.setState({
        promptVisible: false,
        rowToDelete: -1,
        dataChapters: this._data,
        dataSource: this
          .state
          .dataSource
          .cloneWithRows(this._data)
      });
    }



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
export default ChapterList;
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff'
  },
  content: {
    paddingVertical: 5,
    paddingHorizontal: 10,
  },
  headerModel: {
    fontSize: 20,
    fontWeight: 'bold',
    margin: 20
  },
  modalStyle: {
    width: 300, height: 200, backgroundColor: 'white',
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
    borderBottomWidth: 2,
  },
  buttonView: {

  },
  headerText: {
    // maxWidth:250,
    fontSize: 20,
    flex: 1
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
    borderRadius: 5,
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