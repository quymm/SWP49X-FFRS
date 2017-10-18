import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { doLogout } from '../redux/guest/guest-action-creators';
class Header extends Component {

    handleLogout(evt){
        evt.preventDefault();
        this.props.doLogout();
        this.props.history.push('/login');
    }

    render(){
        return(
            <nav className="navbar navbar-default navbar-static-top">
            <div className="navbar-header">
                <a className="navbar-brand" to="/app/index">FOOTBALL</a>
            </div>
            <div className="nav navbar-top-links navbar-right">
            <li><button onClick={this.handleLogout.bind(this)} className="btn btn-default"><i className="glyphicon glyphicon-log-out" /> Logout</button></li>
            </div>
            </nav>
        );
    }
}
export default withRouter(connect(null, {doLogout})(Header));