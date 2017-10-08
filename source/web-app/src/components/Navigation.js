import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
export class Navigation extends Component {
  render() {
    return (
      <div className="navbar-default sidebar" role="navigation">
        <div className="sidebar-nav navbar-collapse">
          <ul className="nav" id="side-menu">
            <li>
              <NavLink to="/app/index" activeClassName="active">
                {' '}
                <i className="glyphicon glyphicon-home" /> Trang chủ{' '}
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/field" activeClassName="active">
                {' '}
                <i className="glyphicon glyphicon-list-alt" /> Quản lý sân{' '}
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/setting-time" activeClassName="active">
                {' '}
                <i className="glyphicon glyphicon-time" /> Thiết lập giờ{' '}
              </NavLink>
            </li>
          </ul>
        </div>
      </div>
    );
  }
}
export default Navigation;
