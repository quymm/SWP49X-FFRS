import React, { Component } from 'react';

export class Navigation extends Component {
    render(){
        return(
            <div className="navbar-default sidebar" role="navigation">
            <div className="sidebar-nav navbar-collapse">
                <ul className="nav" id="side-menu">

                    <li>
                        <a href="index.html"><i className="glyphicon glyphicon-home"></i> Home</a>
                    </li>
                    <li>
                        <a href="tables.html"><i className="glyphicon glyphicon-list-alt"></i> Management Field</a>
                    </li>
                </ul>
            </div>
            
        </div>
        );
    }
}
export default Navigation;