import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator';
import FormCreateField from '../containts/Form-Create-Field';
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
    // const { role } = this.props.auth.user
    // console.log(role);
    // if (role !== 'owner') {
    //   // debugger
    //   this.props.accessDenied();
    //   this.props.history.push("/login");
      
    // }
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    console.log(id);
    const data = await fetchGetAllField(1);
    await this.props.getAllField(data);
  }

  async deleteField(evt) {
    const fieldId = evt.target.value;
    await fetchDeleteField(fieldId);
    const data = await fetchGetAllField(1);
    await this.props.getAllField(data);   
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
            <button className="btn btn-info">Cập nhật</button>
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
