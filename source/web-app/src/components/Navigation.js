import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import { connect } from 'react-redux';
import BackGround from '../resource/images/kologo-1.png';
export class Navigation extends Component {
  handleSelectd(evt) {
    evt.preventDefault();
    this.setState({});
  }
  componentWillMount() {
    const { username } = this.props.auth.user.data;
    if (username === undefined) {
      return <div className="loader" />;
    }
  }
  render() {
    const backGround = { backgroundImage: 'url(' + BackGround + ')' };
    const { username, roleId } = this.props.auth.user.data;
    if (username === undefined) {
      return <div className="loader" />;
    }
    return (
      <div
        className="sidebar"
        data-color={roleId.roleName === 'owner'? 'green' : 'yellow'}
        data-image={require('../resource/images/sidebar-5.jpg')}
        style={backGround}
      >
        <div className="sidebar-wrapper">
          <div className="logo color-owner">
            <a href="#" className="simple-text">
              <i className="glyphicon glyphicon-user" /> {username}
            </a>
          </div>
          {roleId.roleName === 'owner' ? (
            <ul className="nav">
              <li>
                <NavLink to="/app/index" activeClassName="activeLink">
                  <i className="glyphicon glyphicon-calendar" />{' '}
                  <strong>Trận trong ngày</strong>
                </NavLink>
              </li>
              <li>
                <NavLink to="/app/free-time" activeClassName="activeLink">
                  <i className="glyphicon glyphicon-time" />{' '}
                  <strong>Đặt sân</strong>
                </NavLink>
              </li>
              <li>
                <NavLink to="/app/field" activeClassName="activeLink">
                  <i className="glyphicon glyphicon-list-alt" />{' '}
                  <strong>Quản lý sân</strong>
                </NavLink>
              </li>
              <li>
                <NavLink to="/app/setting-time" activeClassName="activeLink">
                  <i className="pe-7s-alarm" /> <strong>Thiết lập giờ</strong>
                </NavLink>
              </li>
              <li>
                <NavLink to="/app/income" activeClassName="active">
                  <i className="pe-7s-news-paper" /> <strong>Thu nhập</strong>
                </NavLink>
              </li>
              <li>
                <NavLink to="/app/promotion" activeClassName="active">
                  <i className="glyphicon glyphicon-gift" />{' '}
                  <strong>Khuyến mãi</strong>
                </NavLink>
              </li>
            </ul>
          ) : roleId.roleName === 'staff' ? (
            <ul className="nav">
              <li>
                <NavLink to="/app/staff-manage-user" activeClassName="active">
                  <i className="pe-7s-users" />{' '}
                  <strong>Quản lý người dùng</strong>
                </NavLink>
              </li>
              <li>
                <NavLink
                  to="/app/staff-manage-overcome"
                  activeClassName="active"
                >
                  <i className="pe-7s-note2" />{' '}
                  <strong>Quản lý thu nhập</strong>
                </NavLink>
              </li>
              <li>
                <NavLink to="/app/staff-manage-price" activeClassName="active">
                  <i className="pe-7s-diskette" /> <strong>Quản lý giá</strong>
                </NavLink>
              </li>
              <li>
                <NavLink
                  to="/app/staff-manage-voucher"
                  activeClassName="active"
                >
                  <i className="pe-7s-albums" />{' '}
                  <strong>Quản lý voucher</strong>
                </NavLink>
              </li>
            </ul>
          ) : (
            <ul className="nav">
              <li>
                <NavLink
                  to="/app/admin-manage-account"
                  activeClassName="active"
                >
                  <i className="pe-7s-home" /> <strong>Trang chủ</strong>
                </NavLink>
              </li>
            </ul>
          )}
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
