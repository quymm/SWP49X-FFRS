
import React, { Component } from 'react';
import { View, Text, Animated, ScrollView, TouchableOpacity, StyleSheet, ActivityIndicator, Switch, Image } from 'react-native';
import { List, ListItem, Button, Icon, } from 'react-native-elements';

console.ignoredYellowBox = ['Warning: Can only update'];
class FadeInView extends Component {
    state = {
        fadeAnim: new Animated.Value(1),  // Initial value for opacity: 0
        moDi: new Animated.Value(0.5),
    }

    componentDidMount() {
        this.timeOut = setTimeout(()=>{
            Animated.timing(                  // Animate over time

                this.state.moDi,            // The animated value to drive
                {
                    toValue: 0,                   // Animate to opacity: 1 (opaque)
                    duration: 1500,              // Make it take a while
                }
            ).start();                        // Starts the animation
            Animated.timing(                  // Animate over time
                this.state.fadeAnim,            // The animated value to drive
                {
                    toValue: 4,                   // Animate to opacity: 1 (opaque)
                    duration: 1500,              // Make it take a while
                }).start();
        
        this.timeInterval = setInterval(() => {
            this.setState({
                fadeAnim: new Animated.Value(1),  // Initial value for opacity: 0
                moDi: new Animated.Value(0.5),
            });
            Animated.timing(                  // Animate over time

                this.state.moDi,            // The animated value to drive
                {
                    toValue: 0,                   // Animate to opacity: 1 (opaque)
                    duration: 1500,              // Make it take a while
                }
            ).start();                        // Starts the animation
            Animated.timing(                  // Animate over time
                this.state.fadeAnim,            // The animated value to drive
                {
                    toValue: 4,                   // Animate to opacity: 1 (opaque)
                    duration: 1500,              // Make it take a while
                }).start();
        }, 1500)
        }, this.props.timeOut)
        
    }
    componentWillUnmount() {
      clearTimeout(this.timeOut);
      clearInterval(this.timeInterval);
    }
    render() {
        let { fadeAnim, moDi } = this.state;

        return (
            <Animated.View                 // Special animatable View
                style={{
                    ...this.props.style,
                    opacity: moDi,
                    transform: [{ scale: fadeAnim }],         // Bind opacity to animated value
                }}
            >
                {this.props.children}
            </Animated.View>
        );
    }

}

export default FadeInView;