import React, { Component } from 'react';
import Header from './Header';
import Navigation from './Navigation';
import Home from './Home';
class FieldOwnerIndex extends Component {
    render() {
        return(
        <div>
            <Header />
            <Navigation />
            <Home />
        </div>
        );
    }
}
export default FieldOwnerIndex;