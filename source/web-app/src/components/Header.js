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
      <nav className="navbar navbar-default navbar-static-top" style={myStyle}>
        <div className="navbar-header">
          <a className="navbar-brand" to="/app/index">
            FOOTBALL
          </a>
        </div>
        <div className="nav navbar-top-links navbar-right">
          <li>
            <Dropdown id="dropdown-custom-1">
              <Dropdown.Toggle>
                <Glyphicon glyph="bullhorn" />' '
                <Badge bsClass="badges">
                  <span>1</span>
                </Badge>
              </Dropdown.Toggle>
              <Dropdown.Menu className="super-colors">
                {/* <MenuItem eventKey="1">Action</MenuItem>
                <MenuItem eventKey="2">Another action</MenuItem>
                <MenuItem eventKey="3" active>
                  Active Item
                </MenuItem>
                <MenuItem divider />
                <MenuItem eventKey="4">Separated link</MenuItem> */}
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
          <li>
            <Dropdown id="dropdown-custom-1">
              <Dropdown.Toggle>
                <Glyphicon glyph="user" />
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
        </div>
      </nav>
    );
  }
}
export default withRouter(connect(null, { doLogout })(Header));
