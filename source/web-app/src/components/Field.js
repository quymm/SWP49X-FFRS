import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator';
import Header from './Header';
import Navigation from './Navigation';
import FormCreateField from '../containts/Form-Create-Field';
class Field extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isCreateShowed: true
    }
  }

  componentDidMount() {
    fetchGetAllField(1).then(data => this.props.getAllField(data));
  }

  deleteField(fieldId) {
    fetchDeleteField(fieldId).then(
      fetchGetAllField().then(data => this.props.getAllField(data)),
    );
  }
  
  shouldComponentUpdate(nextProps, nextState){
    // console.log(nextProps, nextState);
    return true;
  }

  render() {
    const { listField } = this.props;
    // console.log(listField);
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
    const {isCreateShowed} = this.state;
    return (
      <div>
        <div id="page-wrapper">
          <div className="container-fluid">
            <div className="row">
              <div className="col-lg-4">
                <h2 className="page-header">Field</h2>
              </div>
            </div>
            {isCreateShowed? <FormCreateField /> : null }
            
            <div className="col-lg-8 col-lg-offset-2">
              <div className="table-responsive">
                <table className="table table-striped">
                  <thead>
                    <tr>
                      <th>Field Name</th>
                      <th>Field Type</th>
                      <th>Action</th>
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
  return {
    listField: state.field.listField,
  };
}

export default connect(mapStateToProps, { getAllField })(Field);
