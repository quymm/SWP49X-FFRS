import React, { Component } from 'react';
import { connect } from 'react-redux';
import { doLoginSuccessful, accessDenied, doLogout } from '../redux/guest/guest-action-creators';
import { Modal, Pagination } from 'react-bootstrap';
import moment from 'moment';
class AdminPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      handleHideModalAccept: false,
    };
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        debugger;
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
        if (authLocalStorage.roleId.roleName !== 'admin') {
          this.props.accessDenied();
          this.props.history.push('/login');
        } else {
          const idLocal = authLocalStorage.id;
          await this.props.doLoginSuccessful(authLocalStorage);
        }
      }
    }
    else {

    }
  }
  handleShowModalAccept(evt) {
    evt.preventDefault();
    this.setState({ isShowModalAccept: true });
  }
  handleHideModalAccept(evt) {
    evt.preventDefault();
    this.setState({ isShowModalAccept: false });
  }

  render() {
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Trang chủ</h2>
              </div>
            </div>
            <div className="col-sm-12">
              <div className="panel panel-default">
                <div className="table-responsive">
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Tên tài khoản yêu cầu khoá</th>
                        <th>Nhân viên yêu cầu</th>
                        <th>Ngày yêu cầu</th>
                        <th />
                      </tr>
                    </thead>
                    <tbody />
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.isShowModalAccept}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Bạn có chắc muốn khoá tài khoản này</Modal.Title>
          </Modal.Header>
          >
          <Modal.Footer>
            <button
              onClick={this.handleHideModalAccept.bind(this)}
              className="btn btn-primary"
            >
              Xác nhận
            </button>
            <button
              onClick={this.handleHideModalAccept.bind(this)}
              className="btn btn-danger"
            >
              Huỷ
            </button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}
function mapPropsToState(state) {
  return { auth: state.auth };
}
export default connect(mapPropsToState, { doLoginSuccessful, accessDenied, doLogout })(AdminPage);
