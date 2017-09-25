import React, { Component } from 'react';
import { createField } from '../redux/field-owner/field-owner-action-creator';
class FormCreateField extends Component {
  constructor(props) {
    super(props);
    this.setState = {
      filedName: '',
      fileStyle: ''
    };
  }
  FormCreateField() {}
  render() {
    return (
      <div className="col-lg-12">
        <form className="form-horizontal">
          <div className="form-group">
            <label for="inputEmail3" className="col-sm-3 control-label">
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
                  />
                </div>
              </div>
            </div>
          </div>

          <div className="form-group">
            <label for="sel1" className="col-sm-3 control-label">
              Field Type
            </label>
            <div className="col-sm-2">
              <select value={this.state.fileStyle} onChange={(evt)=>this.setState.fileStyle} className="form-control" id="sel1">
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
    fieldOwnerId: state.fieldOwnerId,
  };
}

export default connect(mapStateToProps, { createField })(FormCreateField);
