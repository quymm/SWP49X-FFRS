import React, { Component } from 'react';
import { Link } from 'react-router-dom';
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
                <Link className="navbar-brand" to="/app/index">FOOTBALL</Link>
            </div>
            </nav>
        );
    }
}
export default Header;