import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { doLogout } from '../redux/guest/guest-action-creators';
import { Dropdown, Glyphicon, MenuItem, Badge } from 'react-bootstrap';
import fire from '../services/firebase';
import { Modal } from 'react-bootstrap';
import moment from 'moment';
import 'moment/locale/vi';
import {
  fetchGetFriendlyMatch,
  fetchGetTourMatch,
} from '../apis/field-owner-apis';
import { toast } from 'react-toastify';

class Header extends Component {
  constructor(props) {
    super(props);
    this.state = {
      messages: [],
      key: 1,
      status: false,
      count: 0,
      isShowUpdateField: false,
      match: undefined,
      menuOpened: false,
    };
    this.handelClickDetailMatch = this.handelClickDetailMatch.bind(this);
  }

  handleMenu(evt) {
    evt.preventDefault();
    const { menuOpened } = this.state; 
    this.setState({ menuOpened: !menuOpened });
    // document.getElementsByTagName('html')[0].className =!menuOpened?'nav-open':'';
  }
  componentWillMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      const idLocal = authLocalStorage.id;
      /* Create reference to messages in Firebase Database */
      let messagesRef = fire.database().ref(`fieldOwner/${idLocal}`);
      messagesRef.child('friendlyMatch').on('child_added', snapshot => {
        let message = {
          text: snapshot.val(),
          id: snapshot.key,
          tourMatch: false,
        };
        !message.text.isShowed
          ? (toast.info(message.text.username + ' đã đặt sân của bạn'),
            messagesRef.child(`friendlyMatch/${message.id}/isShowed`).set(1))
          : null;
        !message.text.isRead
          ? this.setState({ count: this.state.count + 1 })
          : null;
        this.setState({
          messages: [message].concat(this.state.messages),
          tourMatch: true,
        });
      });
      messagesRef.child('tourMatch').on('child_added', snapshot => {
        /* Update React state when message is added at Firebase Database */
        let message = {
          text: snapshot.val(),
          id: snapshot.key,
          tourMatch: true,
        };
        !message.text.isShowed
          ? (toast.info('Hệ thống đã chọn sân của bạn'),
            messagesRef.child(`tourMatch/${message.id}/isShowed`).set(1))
          : null;
        !message.text.isRead
          ? this.setState({ count: this.state.count + 1 })
          : null;
        this.setState({
          messages: [message].concat(this.state.messages),
        });
      });
    } else {
      /* Create reference to messages in Firebase Database */
      let messagesRef = fire.database().ref(`fieldOwner/${id}`);
      messagesRef.child('friendlyMatch').on('child_added', snapshot => {
        let message = {
          text: snapshot.val(),
          id: snapshot.key,
          tourMatch: false,
        };
        !message.text.isShowed
          ? (toast.info(message.text.username + ' đã đặt sân của bạn'),
            messagesRef.child(`friendlyMatch/${message.id}/isShowed`).set(1))
          : null;
        !message.text.isRead
          ? this.setState({ count: this.state.count + 1 })
          : null;
        this.setState({
          messages: [message].concat(this.state.messages),
          tourMatch: true,
        });
      });
      messagesRef.child('tourMatch').on('child_added', snapshot => {
        /* Update React state when message is added at Firebase Database */
        let message = {
          text: snapshot.val(),
          id: snapshot.key,
          tourMatch: true,
        };
        !message.text.isShowed
          ? (toast.info('Hệ thống đã chọn sân của bạn'),
            messagesRef.child(`tourMatch/${message.id}/isShowed`).set(1))
          : null;
        !message.text.isRead
          ? this.setState({ count: this.state.count + 1 })
          : null;
        this.setState({
          messages: [message].concat(this.state.messages),
        });
      });
    }
  }
  handleLogout(evt) {
    evt.preventDefault();
    this.setState({ message: undefined });
    this.props.doLogout();
    this.props.history.push('/login');
  }

  async handelClickDetailMatch(match) {
    console.log(match);
    const messageabc = this.state.messages;
    const index = messageabc.findIndex(
      messageabc =>
        messageabc.id === match.id && messageabc.tourMatch === match.tourMatch,
    );
    const { id } = this.props.auth.user.data;
    if (!match.text.isRead) {
      messageabc[index].text.isRead = 1;
      this.setState({ count: this.state.count - 1, messages: messageabc });
      const messagesRef = fire.database().ref(`fieldOwner/${id}`);
      if (match.tourMatch) {
        messagesRef.child(`tourMatch/${match.id}/isRead`).set(1);
      } else {
        messagesRef.child(`friendlyMatch/${match.id}/isRead`).set(1);
      }
    }
    if (match.tourMatch) {
      const data = await fetchGetTourMatch(match.id);
      debugger;
      this.setState({ isShowUpdateField: true, match: data.body });
    } else {
      const data = await fetchGetFriendlyMatch(match.id);
      this.setState({ isShowUpdateField: true, match: data.body });
    }
  }
  handleHideModalField(evt) {
    evt.preventDefault();
    this.setState({ isShowUpdateField: false });
  }

  render() {
    const myStyle = { marginBottom: 0 };
    const styleNotRead = { backgroundColor: 'lavender' };
    const { messages, status } = this.state;
    const { id } = this.props.auth.user.data;
    if (messages.length > 0) {
      const afterSort = messages.sort(
        (a, b) => new Date(a.text.time) - new Date(b.text.time),
      );
      messages.reverse();
    }
    console.log(this.state.match);
    return (
      <nav
        className={`navbar navbar-default ${this.state.menuOpened
          ? 'open'
          : ''}`}
        id="navbar"
        style={myStyle}
      >
        <div className="navbar-header">
          <button
            type="button"
            className="navbar-toggle"
            data-toggle="collapse"
            data-target=".sidebar-collapse"
            onClick={this.handleMenu.bind(this)} // dc roi do a
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
        <ul className="nav navbar-nav navbar-right">
          <li>
            <Dropdown id="dropdown-toggle">
              <Dropdown.Toggle>
                {this.state.count > 0 ? (
                  <span className="notification hidden-sm hidden-xs">
                    {this.state.count}
                  </span>
                ) : null}
                <i className="glyphicon glyphicon-globe" />
              </Dropdown.Toggle>
              <Dropdown.Menu className="dropdown-menu dropdown-messages">
                {messages.length > 0
                  ? messages.map((message, index) => (
                      <li
                        key={index}
                        style={message.text.isRead ? null : styleNotRead}
                      >
                        <a onClick={() => this.handelClickDetailMatch(message)}>
                          <div>
                            <strong>
                              {message.tourMatch ? (
                                <span className=" label label-info">
                                  Hệ thống chọn sân
                                </span>
                              ) : (
                                <span className=" label label-success">
                                  Người dùng đặt
                                </span>
                              )}
                            </strong>
                            <span className="pull-right text-muted">
                              <em>{moment(message.text.time).fromNow()}</em>
                            </span>
                          </div>
                          <div>
                            {!message.tourMatch
                              ? `${message.text.username} đã đặt sân của bạn`
                              : 'Hệ thống đã chọn sân của bạn'}{' '}
                            <i> </i>
                          </div>
                        </a>
                      </li>
                    ))
                  : null}
              </Dropdown.Menu>
            </Dropdown>
          </li>
          <li className="dropdown">
            <Dropdown id="dropdown-custom-1">
              <Dropdown.Toggle>
                <i className="glyphicon glyphicon-user" />
              </Dropdown.Toggle>
              <Dropdown.Menu className="super-colors">
                <MenuItem>
                  {' '}
                  <i className="glyphicon glyphicon-user" /> Cập nhật
                </MenuItem>
                <MenuItem divider />
                <MenuItem onClick={this.handleLogout.bind(this)}>
                  {' '}
                  <i className="glyphicon glyphicon-log-out" /> Đăng xuất
                </MenuItem>
              </Dropdown.Menu>
            </Dropdown>
          </li>
        </ul>
        <Modal
          /* {...this.props} */
          show={this.state.isShowUpdateField}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Chi tiết thông báo</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {this.state.match !== undefined ? (
              <div>
                <h3 className="text-center text-primary">
                  <strong>
                    {moment(
                      '10-10-2017 ' + this.state.match.timeSlotId.startTime,
                    ).format('HH:mm')}
                  </strong>
                </h3>
                <p className="text-center">
                  <strong>
                    {moment(
                      '10-10-2017 ' + this.state.match.timeSlotId.endTime,
                    ).hour() *
                      60 +
                      moment(
                        '10-10-2017 ' + this.state.match.timeSlotId.endTime,
                      ).minute() -
                      (moment(
                        '10-10-2017 ' + this.state.match.timeSlotId.startTime,
                      ).hour() *
                        60 +
                        moment(
                          '10-10-2017 ' + this.state.match.timeSlotId.startTime,
                        ).minute())}{' '}
                    phút
                  </strong>
                </p>
                <p className="text-center">
                  <strong>
                    {moment(this.state.match.timeSlotId.date).format(
                      'DD/MM/YYYY',
                    )}
                  </strong>
                </p>
                <p className="text-center">
                  <strong>
                    {this.state.match.opponentId
                      ? `${this.state.match.userId.profileId.name}, ${this
                          .state.match.opponentId.profileId.name}`
                      : this.state.match.userId.profileId.name}
                  </strong>
                </p>
              </div>
            ) : null}
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.handleHideModalField.bind(this)}
              className="btn btn-danger"
            >
              Đóng
            </button>
          </Modal.Footer>
        </Modal>
      </nav>
    );
  }
}

function mapPropsToState(state) {
  return {
    auth: state.auth,
  };
}

export default withRouter(connect(mapPropsToState, { doLogout })(Header));
