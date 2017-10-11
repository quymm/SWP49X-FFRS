import React, { Component } from 'react';

class Clock extends Component {
    render() {
        const {time} = this.props;
        return (
            <h3>{time}</h3>
        );
    }
}
export default Clock;