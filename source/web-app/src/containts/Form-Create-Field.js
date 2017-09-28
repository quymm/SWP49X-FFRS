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
      fieldStyle: 1,
    };
  }
//tam thoi t nghi dc cach nay thoi, tao 1 state trong component nay, luu listfield, luc bam create thi goi api, return ok thi add them vao list, la no tu dong update
  handelInputChange(evt) {
    this.setState({ fieldName: evt.target.value });
    console.log(this.state.fieldName);
  }

  handleSelectChange(evt) {
    this.setState({ fieldStyle: parseInt(evt.target.value) });
    console.log(this.state.fieldStyle);
  }

  async handleSubmit(evt) {
    
    evt.preventDefault();
    const { fieldName, fieldStyle } = this.state;
    
    await fetchAddField(fieldName, fieldStyle, 1);
      const data = await fetchGetAllField(1);
        debugger
        
        this.props.updateListField(data);
        this.props.getAllField(data)
      //}),//ham nay dau
    //);
  }
  
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
                  
                  <input //cho nay de span, chinh style cho no giong button roi gan su kien onlick vao, la sao?
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
    fieldList : state.listField,
    // fieldOwnerId: state.listField.fieldOwnerId.id gan cai data vao day di, data nào ket qua tu response cho goi actio
  };
}

export default connect(mapStateToProps, { createField, getAllField })(
  FormCreateField,
);
