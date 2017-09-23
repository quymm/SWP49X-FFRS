import React, { Component } from 'react';

class Header extends Component {
    render(){
        return(
            <nav className="navbar navbar-default navbar-static-top">
            <div className="navbar-header">
                <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span className="sr-only">Toggle navigation</span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                </button>
                <a className="navbar-brand" href="index.html">FOOTBALL</a>
            </div>
            </nav>
        );
    }
}
export default Header;