import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator';
import FormCreateField from '../containts/Form-Create-Field';
import { Redirect } from 'react-router-dom';
import { withRouter } from 'react-router-dom';
import { accessDenied } from '../redux/guest/guest-action-creators';
class Field extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isCreateShowed: true,
    };
  }

  componentWillMount() {
    const { role } = this.props.auth.user
    console.log(role);
    if (role !== 'owner') {
      // debugger
      this.props.accessDenied();
      this.props.history.push("/login");
      
    }
  }
  componentDidMount() {
    const { id } = this.props.auth.user.data;
    console.log(id);
    debugger
    fetchGetAllField(id).then(data => this.props.getAllField(data));
  }

  deleteField(fieldId) {
    fetchDeleteField(fieldId).then(
      fetchGetAllField().then(data => this.props.getAllField(data)),
    );
  }

  render() {
    const { listField } = this.props;
    console.log(listField);
    const renderField = listField.map(listField => {
      return (
        <tr key={listField.id}>
          <td>{listField.name}</td>
          <td>{listField.fieldTypeId.name}</td>
          <td>
            <button className="btn btn-info">Update</button>
            <button
              value={listField.id}
              onClick={() => this.deleteField(listField.id)}
              className="btn btn-danger"
            >
              Delete
            </button>
          </td>
        </tr>
      );
    });
    const { isCreateShowed } = this.state;
    return (
      <div>
        <div id="page-wrapper">
          <div className="container-fluid">
            <div className="row">
              <div className="col-lg-4">
                <h2 className="page-header">Quản lý sân</h2>
              </div>
            </div>
            {isCreateShowed ? <FormCreateField /> : null}

            <div className="col-lg-8 col-lg-offset-2">
              <div className="table-responsive">
                <table className="table table-striped">
                  <thead>
                    <tr>
                      <th>Tên sân</th>
                      <th>Loại sân</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {listField == null ? 'There is no field' : renderField}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
function mapStateToProps(state) {
  console.log("state in field: ", state);
  return {
    listField: state.listField.listField,
    auth: state.auth,
  };
}

export default withRouter(connect(mapStateToProps, { getAllField, accessDenied })(Field));
