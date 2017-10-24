import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import { connect } from 'react-redux';
export class Navigation extends Component {
  render() {
    // const { fieldOwner } = this.props;
    // console.log(this.props);
    // if (fieldOwner.user.data.profileId === undefined) {
    //   return <div className="loader">loading</div>
    // }
    return (
      <div className="navbar-default navbar-static-side" role="navigation">
        <div className="sidebar-collapse">
          <ul className="nav" id="side-menu">
            {/* <li>
              <div className="user-section">
                <div className="user-info">
                  <div>
                    {fieldOwner.user.data.profileId.name? fieldOwner.user.data.profileId.name : null}
                  </div>
                  <div className="user-text-online" />
                </div>
              </div>
            </li> */}
            <li>
              <NavLink to="/app/index" activeClassName="selected">
                
                <i className="glyphicon glyphicon-calendar" /> Trận trong ngày
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/free-time" activeClassName="selected">
                
                <i className="glyphicon glyphicon-time" /> Thời gian trống
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/field" activeClassName="selected">
                <i className="glyphicon glyphicon-list-alt" /> Quản lý sân
              </NavLink>
            </li>
            <li>
              <NavLink to="/app/setting-time" activeClassName="selected">
               
                <i className="glyphicon glyphicon-time" /> Thiết lập giờ
              </NavLink>
            </li>
          </ul>
        </div>
      </div>
    );
  }
}

function mapPropsToState(state){
  return {
    fieldOwner: state.auth
  }
}

export default Navigation;
