import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
export class Navigation extends Component {
    render(){
        return(
            <div className="navbar-default sidebar" role="navigation">
            <div className="sidebar-nav navbar-collapse">
                <ul className="nav" id="side-menu">

                    <li>
                        <NavLink to="/index" activeClassName="active" > <i className="glyphicon glyphicon-home"></i> Home </NavLink>
                        
                    </li>
                    <li>
                    <NavLink to="/field" activeClassName="active" > <i className="glyphicon glyphicon-list-alt"></i> Management Field </NavLink>
                    </li>
                </ul>
            </div>
            
        </div>
        );
    }
}
export default Navigation;