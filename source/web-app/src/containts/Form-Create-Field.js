import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  createField,
  getAllField,
} from '../redux/field-owner/field-owner-action-creator';
import { fetchAddField, fetchGetAllField } from '../apis/field-owner-apis';
import { withRouter } from 'react-router-dom';

class FormCreateField extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fieldName: undefined,
      fieldStyle: 1,
      errorMessage: undefined,
    };
  }

  componentDidMount() {
    console.log('form', this.props);
  }

  handelInputChange(evt) {
    this.setState({ fieldName: evt.target.value });
  }

  handleSelectChange(evt) {
    this.setState({ fieldStyle: parseInt(evt.target.value) });
    console.log(this.state.fieldStyle);
  }

  async handleSubmit(evt) {
    evt.preventDefault();
    const { id } = this.props.auth.user.data;
    const { fieldName, fieldStyle } = this.state;
    if (fieldName === null || fieldName === undefined) {
      this.setState({ errorMessage: 'Fieldname can not be blank!' });
    }
    if (fieldName !== null && fieldName !== undefined) {
      // debugger;
      await fetchAddField(fieldName, fieldStyle, 1);
      this.setState({errorMessage: undefined, fieldName: undefined});
      this.fieldNameInput === '';
      // .then(fetchGetAllField(1))
      // .then(data => this.props.getAllField());
      const data = await fetchGetAllField(1);
      await this.props.getAllField(data);
      console.log('form', this.props);
      await this.props.history.push('/app/field');
    }
  }

  render() {
    const { errorMessage } = this.state;
    return (
      <div className="col-lg-12">
        <form
          onSubmit={this.handleSubmit.bind(this)}
          className="form-horizontal"
        >
        {errorMessage === undefined ?  null :  (<div className="alert alert-danger">
            <strong>Danger!</strong> {errorMessage}
          </div>)  }
          
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
                    ref = {el => this.fieldNameInput = el}
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
    fieldList: state.listField.listField,
    auth: state.auth
  };
}

export default withRouter(
  connect(mapStateToProps, { createField, getAllField })(FormCreateField),
);
