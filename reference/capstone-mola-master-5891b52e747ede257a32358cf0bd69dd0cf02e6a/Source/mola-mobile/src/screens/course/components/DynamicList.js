import React, { Component } from 'react';
import {
    View, Text, AlertAndroid, Alert, ListView, ListViewDataSource, StyleSheet,
    TouchableOpacity, InteractionManager, RefreshControl, Animated, Platform, Dimensions, Modal, TextInput, TouchableHighlight,
    Picker, PickerIOS
} from 'react-native';
import { Icon, Button } from 'react-native-elements';
import Prompt from 'react-native-prompt';

import I18n from '../../../../constants/locales/i18n';
import Colors from '../../../../constants/Colors';
import CourseAPI from '../../../apis/course';


class DynamicList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            chapterId: this.props.navigation.state.params.chapterInfo.id,
            listLesson: this.props.navigation.state.params.chapterInfo.listLesson,
            addNew: true,
            modalTitle: '',
            lessonId: 0,

            loading: true,
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1, row2) => true
            }),
            refreshing: false,
            rowToDelete: null,
            promptVisible: false,
            modalVisible: false,
            text: '',
            description: 'description',
        };
        this.lessonMinutes = [5, 10, 15, 20, 25, 30];
    }

    /**
     * Default state values
     * */

    async _updateListLesson(listLesson) {

        this.props.navigation.state.params.updateListLesson(listLesson);
        this.props.navigation.goBack(null);
        // console.log('course info',this.props.navigation.state.params.courseInfo);
        // this.props.navigation.navigate('CourseInfo', this.props.navigation.state.params.courseInfo);
    }

    componentDidMount() {
        InteractionManager.runAfterInteractions(() => {
            this._loadData()
        });
    }

    _loadData(refresh) {
        refresh && this.setState({
            refreshing: true
        });

        this.dataLoadSuccess({ data: this.state.listLesson });
    }

    dataLoadSuccess(result) {

        this._data = result.data;

        let ds = this.state.dataSource.cloneWithRows(this._data);

        this.setState({
            loading: false,
            refreshing: false,
            rowToDelete: -1,
            dataSource: ds
        });
    }

    setModalVisible(visible) {
        this.setState({ modalVisible: visible });
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.addPanel}>
                    <Modal
                        animationType={"slide"}
                        transparent={true}
                        onRequestClose={() => console.log("dcm")}
                        visible={this.state.modalVisible}
                    >
                        <View style={{
                            flex: 1,
                            flexDirection: 'column',
                            justifyContent: 'center',
                            alignItems: 'center'
                        }}>

                            <View style={styles.modalStyle} >
                            
                                <Text style={styles.header}>{this.state.modalTitle}</Text>
                                <View style={{ width: '100%', marginBottom: 10 }}>
                                    <Text>{`${I18n.t('lessonName')}:`}</Text>
                                    <TextInput
                                        onChangeText={(text) => this.setState({ text })}
                                        value={this.state.text}
                                        style={{ marginLeft: 20, marginRight: 20 }}></TextInput>
                                </View>
                                <View style={{ width: '100%', marginBottom: 10 }}>
                                    <Text>{`${I18n.t('description')}:`}</Text>
                                    <View style={{ height: 80, borderColor: 'black', borderWidth: 1, margin: 20 }}>
                                        <TextInput underlineColorAndroid='white' multiline={true}
                                            value={this.state.description}
                                            onChangeText={(description) => this.setState({ description })}></TextInput>
                                    </View>
                                </View>
                                <View style={{ width: '100%', marginBottom: 10 }}>
                                <Text>{`${I18n.t('lessonDuration')}:`}</Text>
                                <Picker
                                  selectedValue={this.state.duration}
                                  onValueChange={(itemValue, itemIndex) => this.setState({duration: itemValue})}>
                                  {
                                    this.lessonMinutes.map(min => <Picker.Item label={`${min} ${I18n.t('minDuration')}`} value={min} />)
                                  }
                                  
                                </Picker>
                                </View>
                                <View style={{ flexDirection: 'row' }}>
                                    <TouchableOpacity
                                        style={styles.addButton}
                                        onPress={() => {
                                            this.setModalVisible(!this.state.modalVisible)
                                        }}>
                                        <Text style={styles.addButtonText}>{`${I18n.t('cancel')}`}</Text>
                                    </TouchableOpacity>
                                    <TouchableOpacity
                                        style={styles.addButton}
                                        onPress={() => {
                                            const lesson = {
                                                id: this.state.lessonId,
                                                title: this.state.text,
                                                description: this.state.description,
                                                duration: this.state.duration,
                                            }
                                            if (this.state.addNew) {
                                                this._addItem(lesson);
                                            } else {
                                                this._edit(lesson);
                                            }

                                        }}>
                                        <Text style={styles.addButtonText}>{`${I18n.t('confirm')}`}</Text>
                                    </TouchableOpacity>

                                </View>
                            </View>
                        </View>
                    </Modal>
                    <TouchableOpacity
                        style={styles.addButton}
                        onPress={() => {
                            this.setState({
                                text: '',
                                description: '',
                                addNew: true,
                                modalTitle: I18n.t('createLesson')
                            })
                            this.setModalVisible(!this.state.modalVisible)
                        }}>
                        <Text style={styles.addButtonText}>{`+ ${I18n.t('new')}`}</Text>
                    </TouchableOpacity>
                </View>
                <ListView
                    refreshControl={
                        <RefreshControl
                            refreshing={this.state.refreshing}
                            onRefresh={this._loadData.bind(this, true)}
                            tintColor="#00AEC7"
                            title="Loading..."
                            titleColor="#00AEC7"
                            colors={['#FFF', '#FFF', '#FFF']}
                            progressBackgroundColor="#00AEC7"
                        />
                    }
                    enableEmptySections={true}
                    dataSource={this.state.dataSource}
                    renderRow={this._renderRow.bind(this)}
                />
                <View style={{ alignItems: 'center' }}>
                    <Button
                        buttonStyle={styles.button}
                        title={I18n.t('save')}
                        onPress={() => this._updateListLesson({
                            chapterId: this.state.chapterId,
                            listLesson: this.state.listLesson
                        })} />
                </View>
            </View>
        );
    }

    _renderRow(rowData) {
        return (
            <View style={{ paddingLeft: 15, paddingRight: 15 }}>

                <View style={styles.rowStyle}>
                    <View style={styles.lessonContainer}>
                        <Text style={[styles.name]}>{I18n.t('lesson') + ' ' + rowData.number + ': ' + rowData.title}</Text>
                        <Text style={styles.duration} numberOfLines={1}>{`${I18n.t('lessonDuration')}: ${rowData.duration} ${I18n.t('minDuration')}`}</Text>
                        <Text style={styles.description} numberOfLines={1}>{`${I18n.t('description')}: ${rowData.description}`}</Text>
                    </View>
                    <View style={{ flexDirection: 'row' }}>
                        <TouchableOpacity style={styles.deleteWrapper}
                            onPress={() => {
                                this.setState({
                                    text: rowData.title,
                                    description: rowData.description,
                                    lessonId: rowData.id,
                                    addNew: false,
                                    duration: rowData.duration,
                                    modalTitle: I18n.t('editLesson')
                                })
                                this.setModalVisible(!this.state.modalVisible)
                            }} >
                            <Icon name='mode-edit' color='#2e3359' size={35} />
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.deleteWrapper} onPress={() => this._confirmDelete(rowData)}>
                            <Icon name='delete' color='#d5283c' size={35} />
                        </TouchableOpacity>
                    </View>

                </View>

            </View>
        );
    }

    async _addItemPressed() {
        await this.setState({ promptVisible: true });
    }

    async _edit(lesson) {
        const lessonInfo = {
            id: lesson.id,
            title: lesson.title,
            description: lesson.description,
            chapterId: this.state.chapterId,
            duration: lesson.duration,
        };
        const courseAPI = new CourseAPI();
        const data = await courseAPI.updateLesson(lessonInfo);
        if (data.status == 'ok') {
            this._data.forEach(function (item) {
                if (item.id == lesson.id) {
                    item.title = lesson.title;
                    item.description = lesson.description;
                    item.duration = lesson.duration;
                }
            })

            this.setState({
                promptVisible: false,
                rowToDelete: -1,
                dataSource: this.state.dataSource.cloneWithRows(this._data),
                listLesson: this._data,

            });
        }
        this.setModalVisible(!this.state.modalVisible)

    }
    async _addItem(lesson) {
        const lessonInfo = {
            id: 0,
            number: this.state.listLesson.length + 1,
            title: lesson.title,
            description: lesson.description,
            chapterId: this.state.chapterId,
            duration: lesson.duration,
        };

        const courseAPI = new CourseAPI();

        const data = await courseAPI.createLesson(lessonInfo);
        if (data.status == 'ok') {
            this._data.push({
                id: data.data.id,
                number: data.data.number,
                title: data.data.title,
                description: data.data.description,
                duration: data.data.duration,
            });
            this.setState({
                promptVisible: false,
                rowToDelete: -1,
                dataSource: this.state.dataSource.cloneWithRows(this._data),
                listLesson: this._data,
            });
        }

        this.setModalVisible(!this.state.modalVisible)

    }

    componentWillUpdate(nexProps, nexState) {
        if (nexState.rowToDelete !== null) {
            this._data = this._data.filter((item) => {
                if (item.id !== nexState.rowToDelete) {
                    return item;
                }
            });
        }
    }

    _confirmDelete(lesson) {
        Alert.alert(
            `${I18n.t('deleteLesson')}: ` + lesson.title,
            null,
            [
                { text: `${I18n.t('cancel')}` },
                { text: `${I18n.t('confirm')}`, onPress: () => this._deleteLesson(lesson) }
            ],
            {
                cancelable: false
            }
        )
    }
    async _deleteLesson(lesson) {
        const courseAPI = new CourseAPI();

        const data = await courseAPI.deleteLesson(lesson.id);
        if (data.status == 'ok') {
            this._data = this._data.filter((item) => {
                if (item.id != lesson.id) {
                    return item;
                }
            });

            this._data.forEach((item) => {
                if(item.number > lesson.number){
                    item.number = item.number - 1;
                }
            })

            const ds = this.state.dataSource.cloneWithRows(this._data);
            this.setState({ dataSource: ds, listLesson: this._data });
        }


    }
    _onAfterRemovingElement() {
        this.setState({
            rowToDelete: null,
            dataSource: this.state.dataSource.cloneWithRows(this._data)
        });
    }

}
export default DynamicList;
const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff'
    },
    noData: {
        color: '#000',
        fontSize: 18,
        alignSelf: 'center',
        top: 200
    },
    button: {
        width: 200,
        backgroundColor: Colors.darkGreen,
        marginBottom: 20,
        borderRadius: 5,
    },
    header: {
        fontSize: 20,
        fontWeight: 'bold',
        margin: 20
    },
    modalStyle: {
        width: 300, height: 450, backgroundColor: 'white',
        elevation: 15,
        borderRadius: 5,
        alignItems: 'center',
        justifyContent: 'center',
        padding: 20
    },
    addPanel: {
        backgroundColor: '#F9F9F9',
    },
    addButton: {
        backgroundColor: Colors.darkGreen,
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
        backgroundColor: '#f7f6fc',
        // height:500,
        margin: 5,
        paddingVertical: 5,
        paddingHorizontal: 10,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },

    name: {
        color: '#212121',
        fontSize: 20,
    },
    description: {
        color: 'grey',
        fontSize: 15
    },
    duration:{
        color: 'grey',
        fontSize: 15,
        fontStyle: 'italic'
    },
    lessonContainer: {
        maxWidth: 220,
    },

    deleteWrapper: {
        paddingVertical: 10,
        marginLeft: 20,
    }
});