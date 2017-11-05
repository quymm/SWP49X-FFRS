import React, { Component } from 'react';

class Clock extends Component {
    render() {
        const {time} = this.props;
        return (
            <h2 className="text-center text-danger">{time}</h2>
        );
    }
}
export default Clock;