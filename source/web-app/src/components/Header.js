import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { doLogout } from '../redux/guest/guest-action-creators';
import { Dropdown, Glyphicon, MenuItem, Badge } from 'react-bootstrap';
class Header extends Component {
  handleLogout(evt) {
    evt.preventDefault();
    this.props.doLogout();
    this.props.history.push('/login');
  }

  render() {
    const myStyle = { marginBottom: 0 };
    return (
      <nav className="navbar navbar-default navbar-fixed-top" id="navbar">
        <div className="navbar-header">
        <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                    <span className="sr-only">Toggle navigation</span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                    <span className="icon-bar"></span>
                </button>
          <a className="navbar-brand" to="/app/index">
            <span className="text-primary"><strong>BÓNG ĐÁ</strong></span>
          </a>
        </div>
        <ul className="nav navbar-top-links navbar-right">
          <li>
            <Dropdown id="dropdown-toggle">
              <Dropdown.Toggle>
              <span className="top-label label label-danger">3</span><i className="fa fa-envelope fa-3x"></i>
              </Dropdown.Toggle>
              <Dropdown.Menu className="super-colors">
                <div className="list-group">
                  <li>
                    <a href="#" className="list-group-item">
                      <i className="fa fa-comment fa-fw" /> New
                      Commentdwadwadawdwadad dwadsw
                      <span className="pull-right text-muted small">
                        <em>4 minutes ago</em>
                      </span>
                    </a>
                  </li>
                  <li>
                    <a href="#" className="list-group-item">
                      <i className="fa fa-comment fa-fw" /> New
                      Commentdwadwadawdwadad dwadsw
                      <span className="pull-right text-muted small">
                        <em>4 minutes ago</em>
                      </span>
                    </a>
                  </li>
                </div>
              </Dropdown.Menu>
            </Dropdown>
          </li>
          <li className="dropdown">
            <Dropdown id="dropdown-custom-1">
              <Dropdown.Toggle>
              <i className="fa fa-user fa-3x"></i>
              </Dropdown.Toggle>
              <Dropdown.Menu className="super-colors">
                <MenuItem>Cập nhật</MenuItem>
                <MenuItem divider />
                <MenuItem onClick={this.handleLogout.bind(this)}>
                  {' '}
                  <i className="glyphicon glyphicon-log-out" /> Đăng xuất
                </MenuItem>
              </Dropdown.Menu>
            </Dropdown>
          </li>
        </ul>
      </nav>
    );
  }
}
export default withRouter(connect(null, { doLogout })(Header));
