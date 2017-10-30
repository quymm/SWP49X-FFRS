import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import { connect } from 'react-redux';
import BackGround from '../resource/images/navbar.jpg';
export class Navigation extends Component {

  handleSelectd(evt){
    evt.preventDefault();
    this.setState({})
  }
  componentWillMount() {
    const { username } = this.props.auth.user.data
    if (username === undefined) {
      return <div className="loader"></div>
    }
  }
  render() {
    const backGround = {backgroundImage: "url(" + BackGround + ")"}
    const { username } = this.props.auth.user.data
    if (username === undefined) {
      return <div className="loader"></div>
    }
    return (
      <div
        className="sidebar"
        data-color="green"
        data-image={require('../resource/images/sidebar-5.jpg')
        }
        style={backGround}
      >
        <div className="sidebar-wrapper">
          <div className="logo">
            <a href="#" className="simple-text">
              {username}
            </a>
          </div>

          <ul className="nav">
            <li>
              <NavLink to="/app/index" activeClassName="activeLink">
                <i className="glyphicon glyphicon-calendar" /> Trận trong ngày
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/free-time" activeClassName="activeLink">
                <i className="glyphicon glyphicon-time" /> Thời gian trống
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/field" activeClassName="activeLink">
                <i className="glyphicon glyphicon-list-alt" /> Quản lý sân
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/setting-time" activeClassName="activeLink">
                <i className="pe-7s-alarm" /> Thiết lập giờ
              </NavLink>
            </li>
          </ul>
        </div>
      </div>
    );
  }
}

function mapPropsToState(state) {
  return {
    auth: state.auth,
  };
}

export default connect(mapPropsToState)(Navigation);
