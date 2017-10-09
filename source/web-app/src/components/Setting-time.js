import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  fetchGetTimeEnableInWeek,
  fetchUpdateTimeEnableInWeek,
} from '../apis/field-owner-apis';
import { getAllTimeEnableInWeek } from '../redux/field-owner/field-owner-action-creator';

class SettingTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fieldType: '5 vs 5',
      daySelected: 'Mon',
      startDay: null,
      endDay: null,
      price: null,
      isShowUpdate: false,
    };
  }

  async componentDidMount() {
    const data = await fetchGetTimeEnableInWeek(1); //.then(data =>
    this.props.getAllTimeEnableInWeek(data);
    //);
  }

  async handleInputChange(evt) {
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    await this.setState({ [name]: value });
    console.log('state in time: ', this.state);
  }

  handleInputTimeEnableChange(evt) {
    if (evt.length > 0) {
      this.setState({
        startDay: evt[0].startTime,
        endDay: evt[0].endTime,
        price: evt[0].price,
      });
    }
  }

  handelShowChange(evt) {
    console.log(evt);
    evt.preventDefault();
    const { isShowUpdate } = this.state;
    this.setState({ isShowUpdate: !isShowUpdate });
  }

  async handleSubmitTimeInWeek(evt) {
    evt.preventDefault();
    const { startDay, endDay, price, daySelected, isShowUpdate } = this.state;

    if (startDay !== null && endDay !== null && price !== null) {
      await fetchUpdateTimeEnableInWeek(
        1,
        daySelected,
        startDay,
        endDay,
        price,
        2,
      );
      await this.setState({isShowUpdate: !isShowUpdate});
      const data = await fetchGetTimeEnableInWeek(1); //.then(data =>
      this.props.getAllTimeEnableInWeek(data);
      //);
      this.props.history.push('/app/setting-time');
    }
  }

  render() {
    const { timeEnable } = this.props;
    const {
      daySelected,
      fieldType,
      endDay,
      startDay,
      price,
      isShowUpdate,
    } = this.state;
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
                    name="fieldType"
                    value="5 vs 5"
                    onClick={this.handleInputChange.bind(this)}
                  >
                    5 vs 5
                  </button>
                </div>
                <div className="col-lg-6">
                  <button
                    className="btn btn-default btn-block"
                    value="7 vs 7"
                    name="fieldType"
                    onClick={this.handleInputChange.bind(this)}
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
                  value="Mon"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Thứ hai
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Tue"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Thứ ba
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Wed"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Thứ tư
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Thu"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Thứ năm
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Fri"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Thứ sáu
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Sat"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Thứ bảy
                </button>
                <button
                  type="button"
                  className="list-group-item"
                  value="Sun"
                  name="daySelected"
                  onClick={this.handleInputChange.bind(this)}
                >
                  Chủ nhật
                </button>
              </div>
            </div>
            <div className="col-lg-10">
              {isShowUpdate ? (
                <form
                  className="form-horizontal"
                  onSubmit={this.handleSubmitTimeInWeek.bind(this)}
                >
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
                            name="startDay"
                            value={this.state.startDay}
                            onChange={this.handleInputChange.bind(this)}
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
                            name="endDay"
                            value={this.state.endDay}
                            onChange={this.handleInputChange.bind(this)}
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
                            name="price"
                            value={this.state.price}
                            onChange={this.handleInputChange.bind(this)}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="form-group">
                    <div className="col-sm-offset-3 col-sm-9">
                      <button className="btn btn-primary" type="submit">
                        Cập nhật
                      </button>
                    </div>
                  </div>
                </form>
              ) : (
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
                            value={
                              dayAfterFilter.length > 0
                                ? dayAfterFilter[0].startTime
                                : 'Chưa thiết lập'
                            }
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
                            value={
                              dayAfterFilter.length > 0
                                ? dayAfterFilter[0].endTime
                                : 'Chưa thiết lập'
                            }
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
                            value={
                              dayAfterFilter.length > 0
                                ? dayAfterFilter[0].price
                                : 'Chưa thiết lập'
                            }
                            readOnly
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="form-group">
                    <div className="col-sm-offset-3 col-sm-9">
                      <button
                        className="btn btn-primary"
                        name="isShowUpdate"
                        onClick={this.handelShowChange.bind(this)}
                      >
                        Cập nhật
                      </button>
                    </div>
                  </div>
                </form>
              )}
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
