import React, { Component } from 'react';
import { connect } from 'react-redux';
import { doLoginSuccessful } from '../redux/guest/guest-action-creators';
class ManageResToOwner extends Component {
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      const idLocal = authLocalStorage.id;
      await this.props.doLoginSuccessful(authLocalStorage);
    } else {
    }
  }
  render() {
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-8">
                <h2 className="page-header">Yêu cầu thành chủ sân</h2>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body" />
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

export default connect(mapPropsToState, {doLoginSuccessful}) (ManageResToOwner);