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
      this.setState({ errorMessage: 'Tên sân không thể bỏ trống!' });
    }
    if (fieldName !== null && fieldName !== undefined) {
      // debugger;
      await fetchAddField(fieldName, fieldStyle, 1);
      await this.setState({errorMessage: undefined, fieldName: undefined});
      const data = await fetchGetAllField(1);
      await this.props.getAllField(data.body);
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
            <strong>Lỗi!</strong> {errorMessage}
          </div>)  }
          
          <div className="form-group">
            <label htmlFor="inputEmail3" className="col-sm-3 control-label">
              Tên sân
            </label>
            <div className="col-sm-9">
              <div className="row">
                <div className="col-sm-6">
                  <input
                    type="text"
                    className="form-control"
                    id="inputPassword3"
                    value={this.state.fieldName}
                    onChange={this.handelInputChange.bind(this)}
                  />
                </div>
              </div>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="sel1" className="col-sm-3 control-label">
              Loại sân
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
                Tạo sân
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
