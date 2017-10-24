import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator';
import FormCreateField from '../containts/Form-Create-Field';
import { withRouter } from 'react-router-dom';
import {
  accessDenied,
  doLoginSuccessful,
} from '../redux/guest/guest-action-creators';
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
      if (authLocalStorage === null || authLocalStorage.roleId.roleName !== 'owner') {
        await this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const idLocal = authLocalStorage.id;
        await this.props.doLoginSuccessful(authLocalStorage);
        const data = await fetchGetAllField(idLocal);
        await this.props.getAllField(data.body);
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
    const {id} = this.props.auth.user.data;
    await fetchDeleteField(fieldId);
    const data = await fetchGetAllField(id);
    await this.props.getAllField(data.body);
  }

  render() {
    const { listField } = this.props;
    console.log(listField);
    const renderField =
      listField.length > 0
        ? listField.map(listField => {
            return (
              <tr key={listField.id}>
                <td>{listField.name}</td>
                <td>{listField.fieldTypeId.name}</td>
                <td>               
                  <button
                    value={listField.id}
                    onClick={this.deleteField.bind(this)}
                    className="btn btn-danger"
                  >
                    Xoá
                  </button>
                </td>
              </tr>
            );
          })
        : 'Hiện tại chưa có sân';
    const { isCreateShowed } = this.state;
    return (
      <div>
        <div id="page-wrapper">
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
                      <th>Tên sân</th>
                      <th>Loại sân</th>
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    {listField == null ? '<td>Chưa có sân</td>' : renderField}
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
  connect(mapStateToProps, { getAllField, accessDenied, doLoginSuccessful })(
    Field,
  ),
);
