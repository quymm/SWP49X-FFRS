import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetTimeEnableInWeek } from '../apis/field-owner-apis';
import { getAllTimeEnableInWeek } from '../redux/field-owner/field-owner-action-creator';

class SettingTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fieldType: '5 vs 5',
      daySelected: 'Monday',
      startDay: null,
      endDay: null, 
      price: null,
      isShowUpdate: false,
    };
  }
  // defaultProps = { timeEnable: {} };
  async componentDidMount() {
    const data = await fetchGetTimeEnableInWeek(4); //.then(data =>
    this.props.getAllTimeEnableInWeek(data);
    //);
  }
  handelChangeFieldType(evt) {
    this.setState({ fieldType: evt.target.value });
  }
  async handelDaySelected(evt) {
    await this.setState({ daySelected: evt.target.value });
    console.log(this.state.daySelected);
  }

  handleInputTimeEnableChange(evt){
    if (evt.length > 0) {
      this.setState({startDay: evt[0].startTime, endDay: evt[0].endTime, price: evt[0].price })
    } 
  }

  render() {
    const { timeEnable } = this.props;
    const { daySelected, fieldType, endDay, startDay, price } = this.state;
    console.log(timeEnable.timeEnable);
    // debugger
    const dayAfterFilter =
      timeEnable.timeEnable &&
      timeEnable.timeEnable.filter(
        data =>
          data.dateInWeek === daySelected &&
          data.fieldTypeId.name === fieldType,
      );
    console.log(dayAfterFilter);
    if (!dayAfterFilter) {
      return <h1>loading...</h1>;
    }
    // this.handleInputTimeEnableChange(dayAfterFilter);
  //  console.log(new Date(dayAfterFilter[0].startTime));
    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-4">
              <h2 className="page-header">Thiết lập giờ</h2>
            </div>
          </div>
          <div className="col-lg-12">
            <div className="col-lg-4 col-lg-offset-4">
              <div className="row">
                <div className="col-lg-6">
                  <button
                    className="btn btn-default btn-block"
                    value="5 vs 5"
                    onClick={this.handelChangeFieldType.bind(this)}
                  >
                    5 vs 5
                  </button>
                </div>
                <div className="col-lg-6">
                  <button
                    className="btn btn-default btn-block"
                    value="7 vs 7"
                    onClick={this.handelChangeFieldType.bind(this)}
                  >
                    7 vs 7
                  </button>
                </div>
              </div>
            </div>
          </div>
          <h4>Thứ trong tuần</h4>
          <div className="row">
            <div className="col-lg-2">
              <div className="list-group">
                <button
                  type="button"
                  className="list-group-item"
                  value="Monday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Thứ hai
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Tuesday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Thứ ba
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Wednesday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Thứ tư
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Thusday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Thứ năm
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Friday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Thứ sáu
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Saturday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Thứ bảy
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Sunday"
                  onClick={this.handelDaySelected.bind(this)}
                >
                  Chủ nhật
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
                    Mở cửa
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          placeholder="Start time"
                          value={dayAfterFilter.length > 0? dayAfterFilter[0].startTime : 'Chưa thiết lập'  }
                          readOnly
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
                    Đóng cửa
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          placeholder="End time"
                          value={dayAfterFilter.length > 0?  dayAfterFilter[0].endTime : 'Chưa thiết lập'}
                          readOnly
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
                    Giá 
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          placeholder="End time"
                          value={dayAfterFilter.length > 0? dayAfterFilter[0].price : 'Chưa thiết lập'}
                          readOnly                         
                        />
                      </div>
                    </div>
                  </div>
                </div>
                <div className="form-group">
                  <div className="col-sm-offset-3 col-sm-9">
                    <button className="btn btn-primary">
                      Cập nhật
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
  return { timeEnable: state.timeEnable };
}

export default connect(mapStateToProps, { getAllTimeEnableInWeek })(
  SettingTime,
);
