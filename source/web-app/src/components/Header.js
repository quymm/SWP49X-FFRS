import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { doLogout } from '../redux/guest/guest-action-creators';
import { Dropdown, Glyphicon, MenuItem, Badge } from 'react-bootstrap';
import fire from '../services/firebase';
import { Modal } from 'react-bootstrap';
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
      isShowUpdateField: false,
    };
    this.handelClickDetailMatch = this.handelClickDetailMatch.bind(this);
  }
  componentWillMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      const idLocal = authLocalStorage.id;
      /* Create reference to messages in Firebase Database */
      let messagesRef = fire
        .database()
        .ref(`fieldOwner/${idLocal}/friendlyMatch`)
        .limitToLast(100);
      console.log(messagesRef);
      messagesRef.on('child_added', snapshot => {
        let message = { text: snapshot.val(), id: snapshot.key };
        !message.text.isRead
          ? (notyf.confirm('Có người mới đặt sân'),
            this.setState({ count: this.state.count + 1 }))
          : null;
        console.log(
          'firebase inside: ',
          `fieldOwner/${idLocal}/friendlyMatch/${message.id}/isRead`,
        );
        fire
          .database()
          .ref(`fieldOwner/${idLocal}/friendlyMatch/${message.id}/isRead`)
          .set(1);
        this.setState({
          messages: [message].concat(this.state.messages),
          status: true,
        });
      });
    } else {
      /* Create reference to messages in Firebase Database */
      let messagesRef = fire
        .database()
        .ref(`fieldOwner/${id}/friendlyMatch`)
        .limitToLast(100);
      messagesRef.on('child_added', snapshot => {
        let message = { text: snapshot.val(), id: snapshot.key };
        !message.text.isRead
          ? (notyf.confirm('Có người mới đặt sân'),
            this.setState({ count: this.state.count + 1 }))
          : null;
        console.log('firebase inside: ', message);
        fire
          .database()
          .ref(`fieldOwner/${id}/friendlyMatch/${message.id}/isRead`)
          .set(1);
        this.setState({
          messages: [message].concat(this.state.messages),
          status: true,
        });
      });
    }
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
    // console.log(match);
    // const newPostKey = fire
    //   .database()
    //   .ref('17/friendlyMatch')
    //   .push({ friendlyMatchId: 10, status: true, time: '10-10-2017 10:00:00' });
    // console.log(newPostKey);
    this.setState({ isShowUpdateField: true });
  }
  handleHideModalField(evt) {
    evt.preventDefault();
    this.setState({ isShowUpdateField: false });
  }

  render() {
    const myStyle = { marginBottom: 0 };
    const { messages, status } = this.state;
    const { id } = this.props.auth.user.data;
    // console.log(id);
    // if (!status) {
    //   return <div className="loader" />;
    // }
    console.log('firebase: ', messages);
    return (
      <nav className="navbar navbar-default" id="navbar">
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
        <ul className="nav navbar-nav navbar-right">
          <li>
            <Dropdown
              id="dropdown-toggle"
              onClick={this.handelClickNotify.bind(this)}
            >
              <Dropdown.Toggle>
                {this.state.count ? (
                  <span className="notification hidden-sm hidden-xs">
                    {this.state.count}
                  </span>
                ) : null}
                <i className="fa fa-globe" />
              </Dropdown.Toggle>
              <Dropdown.Menu className="dropdown-menu dropdown-messages">
                {messages.length > 0
                  ? messages.map(messages => (
                      <li key={messages.id}>
                        <a
                          onClick={() => this.handelClickDetailMatch(messages)}
                        >
                          <div>
                            <strong>
                              <span className=" label label-info">
                                Người dùng tự đặt
                              </span>
                            </strong>
                            <span className="pull-right text-muted">
                              <em>{messages.text.time}</em>
                            </span>
                          </div>
                          <div>Bạn vừa có người đặt sân, xem chi tiết</div>
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
        <Modal
          /* {...this.props} */
          show={this.state.isShowUpdateField}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Thiết lập sân</Modal.Title>
          </Modal.Header>
          <Modal.Body>dwadd</Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.handleHideModalField.bind(this)}
              className="btn btn-danger"
            >
              Huỷ
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
