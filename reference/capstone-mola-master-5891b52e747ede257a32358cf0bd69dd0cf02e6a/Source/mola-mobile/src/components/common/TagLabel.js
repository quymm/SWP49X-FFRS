import React, {Component} from 'react';
import {Text, View, TextInput, TouchableOpacity, StyleSheet} from 'react-native';
import {Icon} from 'react-native-elements';
import _ from 'lodash';

class TagLabel extends Component {
  render(){
  const { tagColor, tagTextColor } = this.props;
  console.log('props', this.props)
  return (
    <View style={this.props.style}>
      {this.props.value.map((tag, i) => {
        return (
          <TouchableOpacity
            key={i}
            ref={'tag' + i}
            style={[styles.tag, { backgroundColor: tagColor }]}
            onPress={() => this.removeIndex(i)}>
            <Text style={[styles.tagText, { color: tagTextColor }]}>
              {tag}&nbsp;&times; 
            </Text>
          </TouchableOpacity>
        )
      })}
    </View>
  );
  }
  
  removeIndex = (index: number) => {
    const tags = [this.props.value];
    if(index >= 0)
      tags.splice(index, 1);
  }
}

export default TagLabel;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  wrapper: {
    flex: 1,
    flexDirection: 'row',
    marginTop: 3,
    marginBottom: 2,
    alignItems: 'flex-start',
  },
  tagInputContainerScroll: {
    flex: 1,
  },
  tagInputContainer: {
    flex: 1,
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  textInput: {
    height: 36,
    fontSize: 16,
    flex: .6,
    marginBottom: 6,
    padding: 0,

  },
  textInputContainer: {
    height: 36,
  },
  tag: {
    justifyContent: 'center',
    marginTop: 6,
    marginRight: 3,
    padding: 8,
    height: 24,
    borderRadius: 2,
  },
  // tagText: {
  //   padding: 0,
  //   margin: 0,
  // },
});
