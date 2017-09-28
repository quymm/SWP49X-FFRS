import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  createField,
  getAllField,
} from '../redux/field-owner/field-owner-action-creator';
import { fetchAddField, fetchGetAllField } from '../apis/field-owner-apis';

class FormCreateField extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fieldName: '',
      fieldStyle: 1
    };
  }

  handelInputChange(evt) {
    this.setState({ fieldName: evt.target.value });
    console.log(this.state.fieldName);
  }

  handleSelectChange(evt) {
    this.setState({ fieldStyle: parseInt(evt.target.value) });
    console.log(this.state.fieldStyle);
  }

  handleSubmit(evt) {
    // evt.preventDefault();
    const { fieldName, fieldStyle } = this.state;
    // const { fieldOwnerId } = this.props;
    fetchAddField(fieldName, fieldStyle, 1).then(
      fetchGetAllField(1).then(data => this.props.getAllField(data)),
    );
  }
  FormCreateField() {}
  render() {
    return (
      <div className="col-lg-12">
        <form
          onSubmit={this.handleSubmit.bind(this)}
          className="form-horizontal"
        >
          <div className="form-group">
            <label htmlFor="inputEmail3" className="col-sm-3 control-label">
              Field Name
            </label>
            <div className="col-sm-9">
              <div className="row">
                <div className="col-sm-6">
                  <input
                    type="text"
                    className="form-control"
                    id="inputPassword3"
                    placeholder="Field name"
                    value={this.state.fieldName}
                    onChange={this.handelInputChange.bind(this)}
                  />
                </div>
              </div>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="sel1" className="col-sm-3 control-label">
              Field Type
            </label>
            <div className="col-sm-2">
              <select
                value={this.state.fieldStyle}
                onChange={this.handleSelectChange.bind(this)}
                className="form-control"
                id="sel1"
              >
                <option value="1">5 vs 5</option>
                <option value="2">7 vs 7</option>
              </select>
            </div>
          </div>
          <div className="form-group">
            <div className="col-sm-offset-3 col-sm-9">
              <button type="submit" className="btn btn-primary">
                Create
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  }
}
function mapStateToProps(state) {
  return {
    // fieldOwnerId: state.listField.fieldOwnerId.id
  };
}

export default connect(mapStateToProps, { createField, getAllField })(
  FormCreateField,
);
