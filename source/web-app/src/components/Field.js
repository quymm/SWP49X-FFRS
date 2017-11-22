import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator';
import FormCreateField from '../containts/Form-Create-Field';
import { withRouter } from 'react-router-dom';
import {
  accessDenied,
  doLoginSuccessful,
  doLogout
} from '../redux/guest/guest-action-creators';
import moment from 'moment';
class Field extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isCreateShowed: true,
    };
  }

  async componentDidMount() {
    const { id, roleId } = this.props.auth.user.data;

    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
      if (
        authLocalStorage.roleId.roleName !== 'owner'
      ) {
        await this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const idLocal = authLocalStorage.id;
        await this.props.doLoginSuccessful(authLocalStorage);
        debugger
        const data = await fetchGetAllField(idLocal);
        debugger
        await this.props.getAllField(data.body);
      }
    }
    } else {
      if (roleId.roleName !== 'owner') {
        this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const data = await fetchGetAllField(id);
        await this.props.getAllField(data.body);
      }
    }
  }

  async deleteField(evt) {
    const fieldId = evt.target.value;
    const { id } = this.props.auth.user.data;
    await fetchDeleteField(fieldId);
    const data = await fetchGetAllField(id);
    await this.props.getAllField(data.body);
  }

  render() {
    const { listField } = this.props;
    console.log(listField);
    const renderField =
      listField.length > 0
        ? listField.map((listField, index) => {
            return (
              <tr key={listField.id}>
                <td>{index + 1}</td>
                <td>{listField.name}</td>
                <td>{listField.fieldTypeId.name}</td>
                <td>{listField.expirationDate? moment(listField.expirationDate, 'YYYY-MM-DD').format('DD/MM/YYYY'): null }</td>
                {listField.expirationDate? null:
                <td>
                  <button
                    value={listField.id}
                    onClick={this.deleteField.bind(this)}
                    className="btn btn-danger"
                  >
                    Xoá
                  </button>
                </td>}
              </tr>
            );
          })
        : 'Hiện tại chưa có sân';
    const { isCreateShowed } = this.state;
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-md-4">
                <h2 className="page-header">Quản lý sân</h2>
              </div>
            </div>
            {isCreateShowed ? <FormCreateField /> : null}
            <div className="col-md-8 col-md-offset-2">
              <div className="panel panel-dafault">
                <div className="panel-body">
                  <div className="table-responsive">
                    <table className="table table-striped">
                      <thead>
                        <tr>
                          <th>#</th>
                          <th>Tên sân</th>
                          <th>Loại sân</th>
                          <th>Hiệu lực đến ngày</th>                         
                          <th />
                        </tr>
                      </thead>
                      <tbody>
                        {listField == null ? (
                          <tr>
                            <td>Chưa có sân</td>
                          </tr>
                        ) : (
                          renderField
                        )}
                      </tbody>
                    </table>
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
function mapStateToProps(state) {
  console.log('state in field: ', state);
  return {
    listField: state.listField.listField,
    auth: state.auth,
  };
}

export default withRouter(
  connect(mapStateToProps, { getAllField, accessDenied, doLoginSuccessful, doLogout })(
    Field,
  ),
);
