import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  doLoginSuccessful,
  accessDenied,
  doLogout,
} from '../redux/guest/guest-action-creators';
class ManagePrice extends Component {
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));

      if (authLocalStorage === null) {
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
        if (authLocalStorage.roleId.roleName !== 'staff') {
          this.props.accessDenied();
          this.props.history.push('/login');
        } else {
          await this.props.doLoginSuccessful(authLocalStorage);
          
        }
      }
    } else {
    }
  }
  render() {
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Quản lý giá</h2>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body">
                    <div className="col-sm-12">
                      <div className="table-responsive">
                        <div className="panel panel-heading">
                          <h4>Giờ thấp điểm</h4>
                        </div>
                        <table className="table table-striped">
                          <thead>
                            <tr>
                              <th>#</th>
                              <th>Loại giá</th>
                              <th>Giá</th>
                              <th>Loại sân</th>
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
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body">
                    <div className="col-sm-12">
                      <div className="table-responsive">
                        <div className="panel panel-heading">
                          <h4>Giờ cao điểm</h4>
                        </div>
                        <table className="table table-striped">
                          <thead>
                            <tr>
                              <th>#</th>
                              <th>Loại giá</th>
                              <th>Giá</th>
                              <th>Loại sân</th>
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
            </div>
          </div>
        </div>
      </div>
    );
  }
}
function mapPropsToState(state) {
  return { auth: state.auth };
}
export default connect(mapPropsToState, {
  doLoginSuccessful,
  accessDenied,
  doLogout,
})(ManagePrice);
