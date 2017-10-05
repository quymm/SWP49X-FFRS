import React, { Component } from 'react';
import { connect } from 'react-redux';
class SettingTime extends Component {
  constructor(props){
    super(props);
    this.state = {
      is5vs5Show: true,
      
    }
  }
  handelChangeFieldType(type){
    this.setState = { is5vs5Show: !type } 
  }
  handelChangeDay(day){
    
  }
  render() {
    const { timeEnable } = this.props;

    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-4">
              <h2 className="page-header">Setting Time</h2>
            </div>
          </div>
          <div className="col-lg-12">
            <div className="col-lg-4 col-lg-offset-4">
              <div className="row">
                <div className="col-lg-6">
                  <button className="btn btn-default btn-block" value={this.state.is5vs5Show}>5 vs 5</button>
                </div>
                <div className="col-lg-6">
                  <button className="btn btn-primary btn-block">7 vs 7</button>
                </div>
              </div>
            </div>
          </div>
          <h4>Day in week</h4>
          <div className="row">
            <div className="col-lg-2">
              <div className="list-group">
                <button type="button" className="list-group-item" value='Moday'>
                  Monday
                </button>
                <button type="button" className="list-group-item" value='Tuesday'>
                  Tuesday
                </button>
                <button type="button" className="list-group-item" value='Wednesday'>
                  Wednesday
                </button>
                <button type="button" className="list-group-item" value='Thusday'>
                  Thusday
                </button>
                <button type="button" className="list-group-item" value='Friday'>
                  Friday
                </button>
                <button type="button" className="list-group-item" value='Saturday'>
                  Saturday
                </button>
                <button type="button" className="list-group-item" value='Sunday'>
                  Sunday
                </button>
              </div>
            </div>
            <div className="col-lg-10">
              <form className="form-horizontal">
                <div className="form-group">
                  <label
                    htmlFor="inputEmail3"
                    className="col-sm-3 control-label"
                  >
                    Start time
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          placeholder="Start time"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <div className="form-group">
                  <label
                    htmlFor="inputEmail3"
                    className="col-sm-3 control-label"
                  >
                    End time
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          placeholder="End time"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <div className="form-group">
                  <div className="col-sm-offset-3 col-sm-9">
                    <button type="submit" className="btn btn-primary">
                      Update
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
function mapStateToProps(state) {
  return { timeEnable: state.timeEnable }
}

export default connect(mapStateToProps)(SettingTime);
