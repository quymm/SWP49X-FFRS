import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  fetchGetTimeEnableInWeek,
  fetchUpdateTimeEnableInWeek,
} from '../apis/field-owner-apis';
import { getAllTimeEnableInWeek } from '../redux/field-owner/field-owner-action-creator';
import TimePicker from 'rc-time-picker';
import 'rc-time-picker/assets/index.css';
import {
  doLoginSuccessful,
  accessDenied,
} from '../redux/guest/guest-action-creators';
class SettingTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fieldType: '5 vs 5',
      fieldTypeId: 1,
      daySelected: 'Mon',
      startDay: null,
      endDay: null,
      price: undefined,
      isShowUpdate: false,
      buttonGroupDayInWeek: [
        {
          id: 1,
          value: 'Mon',
          text: 'Thứ hai',
        },
        {
          id: 2,
          value: 'Tue',
          text: 'Thứ ba',
        },
        {
          id: 3,
          value: 'Wed',
          text: 'Thứ tư',
        },
        {
          id: 4,
          value: 'Thu',
          text: 'Thứ năm',
        },
        {
          id: 5,
          value: 'Fri',
          text: 'Thứ sáu',
        },
        {
          id: 6,
          value: 'Sat',
          text: 'Thứ bảy',
        },
        {
          id: 7,
          value: 'Sun',
          text: 'Chủ nhật',
        },
      ],
      buttonGroupFieldType: [
        {
          id: 1,
          value: '5 vs 5',
          text: 'Loại sân 5 người',
        },
        {
          id: 2,
          value: '7 vs 7',
          text: 'Loại sân 7 người',
        },
      ],
    };
  }

  async componentDidMount() {
    const { id, roleId } = this.props.auth.user.data;
    debugger;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (
        authLocalStorage === null ||
        authLocalStorage.roleId.roleName !== 'owner'
      ) {
        await this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const idLocal = authLocalStorage.id;
        await this.props.doLoginSuccessful(authLocalStorage);
        const data = await fetchGetTimeEnableInWeek(idLocal);
        this.props.getAllTimeEnableInWeek(data.body);
      }
    } else {
      if (roleId.roleName !== 'owner') {
        this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const data = await fetchGetTimeEnableInWeek(id);
        this.props.getAllTimeEnableInWeek(data.body);
      }
    }
  }
  configTimeDiable() {
    let disableTime = [];
    for (let i = 1; i < 30; i++) {
      disableTime.push(i);
    }
    for (let i = 31; i < 60; i++) {
      disableTime.push(i);
    }
    return disableTime;
  }
  async handleInputChange(evt) {
    const target = evt.target;
    const value = target.value;
    if (value === '5 vs 5') {
      this.setState({ fieldTypeId: 1 });
    } else if (value === '7 vs 7') {
      this.setState({ fieldTypeId: 2 });
    }
    const name = target.name;
    await this.setState({ [name]: value });
    console.log(this.state);
  }
  async handelTimeStartDayInputChange(evt) {
    await this.setState({ startDay: evt.format('HH:mm') });
    console.log(this.state);
  }
  async handelTimeEndDayInputChange(evt) {
    await this.setState({ endDay: evt.format('HH:mm') });
    console.log(this.state);
  }

  handelShowChange(evt) {
    evt.preventDefault();
    const { isShowUpdate } = this.state;
    this.setState({ isShowUpdate: !isShowUpdate });
  }

  async handleSubmitTimeInWeek(evt) {
    evt.preventDefault();
    const { id } = this.props.auth.user.data;
    const {
      startDay,
      endDay,
      price,
      daySelected,
      isShowUpdate,
      fieldTypeId,
    } = this.state;

    if (startDay !== null && endDay !== null && price !== null) {
      await fetchUpdateTimeEnableInWeek(
        1,
        daySelected,
        startDay,
        endDay,
        price,
        fieldTypeId,
      );
      await this.setState({ isShowUpdate: !isShowUpdate });
      const data = await fetchGetTimeEnableInWeek(1);
      this.props.getAllTimeEnableInWeek(data.body);
      this.props.history.push('/app/setting-time');
    }
    await this.setState({ isShowUpdate: !isShowUpdate });
  }

  render() {
    const { timeEnable } = this.props.timeEnable;
    const {
      daySelected,
      fieldType,
      endDay,
      startDay,
      price,
      isShowUpdate,
    } = this.state;
    const dayAfterFilter =
      timeEnable &&
      timeEnable.filter(
        data =>
          data.dateInWeek === daySelected &&
          data.fieldTypeId.name === fieldType,
      );

    if (!dayAfterFilter) {
      return <h1>loading...</h1>;
    }
    const { buttonGroupDayInWeek, buttonGroupFieldType } = this.state;
    // console.log(dayAfterFilter);
    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-4">
              <h2 className="page-header">Thiết lập giờ</h2>
            </div>
          </div>
          <div className="col-lg-12">
            <div className="row">
              <div className="col-lg-6 col-lg-offset-3">
                <div className="row">
                  {buttonGroupFieldType.map(fieldType => (
                    <div className="col-lg-6" key={fieldType.id}>
                      <button
                        className={`${fieldType.value == this.state.fieldType
                          ? 'btn btn-primary btn-lg btn-block'
                          : 'btn btn-default btn-lg btn-block'}`}
                        name="fieldType"
                        value={fieldType.value}
                        onClick={this.handleInputChange.bind(this)}
                      >
                        {fieldType.text}
                      </button>
                    </div>
                  ))}
                </div>
              </div>
              <div className="col=sm-4">
                <button
                  className="btn btn-info"
                  name="isShowUpdate"
                  onClick={this.handelShowChange.bind(this)}
                >
                  Thêm mới khung giờ
                </button>
              </div>
            </div>
          </div>
          <h4>Thứ trong tuần</h4>
          <div className="row">
            <div className="col-lg-2">
              <div className="list-group">
                {buttonGroupDayInWeek.map(day => (
                  <div key={day.id}>
                    <button
                      type="button"
                      className={`list-group-item ${day.value ==
                      this.state.daySelected
                        ? 'active'
                        : ''}`}
                      value={day.value}
                      name="daySelected"
                      onClick={this.handleInputChange.bind(this)}
                    >
                      {day.text}
                    </button>
                  </div>
                ))}
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
                      Từ
                    </label>
                    <div className="col-sm-9">
                      <div className="row">
                        <div className="col-sm-6">
                          <TimePicker
                            showSecond={false}
                            name="startDay"
                            onChange={this.handelTimeStartDayInputChange.bind(
                              this,
                            )}
                            disabledMinutes={this.configTimeDiable.bind(this)}
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
                      Đến
                    </label>
                    <div className="col-sm-9">
                      <div className="row">
                        <div className="col-sm-6">
                          <TimePicker
                            showSecond={false}
                            name="endDay"
                            onChange={this.handelTimeEndDayInputChange.bind(
                              this,
                            )}
                            disabledMinutes={this.configTimeDiable.bind(this)}
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
                            placeholder="Giá"
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
                      <button
                        onClick={this.handelShowChange.bind(this)}
                        className="btn btn-danger"
                        type="submit"
                      >
                        Huỷ
                      </button>
                    </div>
                  </div>
                </form>
              ) : (
                <form className="form-horizontal">
                  {dayAfterFilter.map(affterConvertArr => (
                    <div key={affterConvertArr.id}>
                      <div className="form-group">
                        <label
                          htmlFor="inputEmail3"
                          className="col-sm-3 control-label"
                        >
                          Từ
                        </label>
                        <div className="col-sm-9">
                          <div className="row">
                            <div className="col-sm-6">
                              <input
                                type="text"
                                className="form-control"
                                id="inputPassword3"
                                placeholder="Start time"
                                value={affterConvertArr.startTime}
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
                          Đến
                        </label>
                        <div className="col-sm-9">
                          <div className="row">
                            <div className="col-sm-6">
                              <input
                                type="text"
                                className="form-control"
                                id="inputPassword3"
                                placeholder="End time"
                                value={affterConvertArr.endTime}
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
                                value={affterConvertArr.price}
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
                          <button className="btn btn-danger">Xoá</button>
                        </div>
                      </div>
                    </div>
                  ))}
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
  return {
    timeEnable: state.timeEnable,
    auth: state.auth,
  };
}

export default connect(mapStateToProps, {
  getAllTimeEnableInWeek,
  doLoginSuccessful,
  accessDenied,
})(SettingTime);
