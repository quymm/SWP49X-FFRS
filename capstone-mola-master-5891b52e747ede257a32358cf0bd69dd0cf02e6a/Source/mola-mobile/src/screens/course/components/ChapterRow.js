import React, { Component } from 'react';
import { } from 'react-native';
import Accordion from 'react-native-collapsible/Accordion';
import DynamicListRow from './DynamicListRow';


class ChapterRow extends Component {
    _renderHeader(section) {
        return (
            <DynamicListRow
                remove={section.id === this.state.rowToDelete}
                onRemoving={this._onAfterRemovingElement.bind(this)}
            >
                <View style={styles.rowStyle}>

                    <View style={styles.contact}>
                        <Text style={[styles.name]}>{section.name}</Text>
                        <Text style={styles.phone}>{section.description}</Text>
                    </View>
                    <View style={{ flexDirection: 'row' }}>
                        <TouchableOpacity style={styles.deleteWrapper} onPress={() => this._deleteItem(section.id)}>
                            <Icon name='add-box' size={30} />
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.deleteWrapper} onPress={() => this._deleteItem(section.id)}>
                            <Icon name='delete' size={30} />
                        </TouchableOpacity>
                    </View>
                </View>
            </DynamicListRow>
        );
    }

    _renderContent(section) {
        return (
            <View style={styles.content}>
                <Text>{section.content}</Text>
            </View>
        );
    }
    state = {}
    render() {
        return (
            <Accordion

            />
        );
    }
}

export default ChapterRow;