import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { doLogout } from '../redux/guest/guest-action-creators';
import { Dropdown, Glyphicon, MenuItem, Badge } from 'react-bootstrap';
import fire from '../services/firebase';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.min.css';
var Notyf = require('notyf');
var notyf = new Notyf({
  delay: 20000,
});

class Header extends Component {
  constructor(props) {
    super(props);
    this.state = {
      messages: [],
      status: false,
      count: 0,
    };
    this.handelClickDetailMatch = this.handelClickDetailMatch.bind(this);
  }
  componentWillMount() {
    /* Create reference to messages in Firebase Database */
    let messagesRef = fire
      .database()
      .ref('18/friendlyMatch')
      .orderByKey()
      .limitToLast(100);
    messagesRef.on('value', snapshot => {
      /* Update React state when message is added at Firebase Database */
      let message = { text: snapshot.val(), id: snapshot.key };
      console.log('firebase: ', message);
      this.setState({ messages: this.state.messages.push(message), status: true });
    });
  }
  handleLogout(evt) {
    evt.preventDefault();
    this.props.doLogout();
    this.props.history.push('/login');
  }

  handelClickNotify(evt) {
    evt.preventDefault();
    this.setState({ count: 0 });
  }

  handelClickDetailMatch(match) {
    console.log(match);
    const newPostKey = fire
      .database()
      .ref('18/friendlyMatch')
      .push({ friendlyMatchId: 10, status: true, time: '10-10-2017 10:00:00' })
    console.log(newPostKey);
  }

  render() {
    const myStyle = { marginBottom: 0 };
    const { messages, status } = this.state;
    if (!status) {
      return <div className="loader" />;
    }
    console.log( messages);
    const notify = messages.text.friendlyMatch.map((messages, index) => {
      const { count } = this.state;
      messages.status
        ? (notyf.confirm('Có người mới đăt sân'),
          this.setState({ count: count + 1 }))
        : null;
      fire
        .database()
        .ref(`18/friendlyMatch/${index}/status`)
        .set(false);
    });
    
    return (
      <nav className="navbar navbar-default navbar-fixed-top" id="navbar">
        <div className="navbar-header">
          <button
            type="button"
            className="navbar-toggle"
            data-toggle="collapse"
            data-target=".sidebar-collapse"
          >
            <span className="sr-only">Toggle navigation</span>
            <span className="icon-bar" />
            <span className="icon-bar" />
            <span className="icon-bar" />
          </button>
          <a className="navbar-brand" to="/app/index">
            <span className="text-primary">
              <img src={require('../resource/images/ffrs.png')} />
            </span>
          </a>
        </div>
        <ul className="nav navbar-top-links navbar-right">
          <li>
            <Dropdown
              id="dropdown-toggle"
              className="btn-primary-outline"
              onClick={this.handelClickNotify.bind(this)}
            >
              <Dropdown.Toggle>
                <span className="top-label label label-danger">
                  {this.state.count ? this.state.count : null}
                </span>
                <i className="fa fa-envelope fa-3x" />
              </Dropdown.Toggle>
              <Dropdown.Menu className="dropdown-menu dropdown-messages">
                {messages.text.friendlyMatch.map((messages, index) => (
                  <li key={index}>
                    <a onClick={() => this.handelClickDetailMatch(messages)}>
                      <div>
                        <strong>
                          <span className=" label label-info">
                            Người dùng tự đặt
                          </span>
                        </strong>
                        <span className="pull-right text-muted">
                          <em>{messages.time}</em>
                        </span>
                      </div>
                      <div>Bạn vừa có người đặt sân, xem chi tiết</div>
                    </a>
                  </li>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </li>
          <li className="dropdown">
            <Dropdown id="dropdown-custom-1">
              <Dropdown.Toggle>
                <i className="fa fa-user fa-3x" />
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
