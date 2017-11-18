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
import { Modal, OverlayTrigger, Popover } from 'react-bootstrap';
import moment from 'moment';
class SettingTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      message: undefined,
      fieldType: '5 vs 5',
      fieldTypeId: 1,
      daySelected: 'Mon',
      startDay: moment('10-10-2017 06:00:00', 'YYYY-MM-DD HH:mm'),
      endDay: moment('10-10-2017 22:00:00', 'YYYY-MM-DD HH:mm'),
      price: undefined,
      peakPrice: undefined,
      idelPrice: undefined,
      isShowAddTime: false,
      maximumPricePeak: 500,
      minimumPricePeak: 200,
      maximumPriceIdel: 300,
      minimumPriceIdel: 100,
      isOptimze: true,
      optimizeTime: 1.5,
      optimizeNumOfMatch: 2,
      endTimeWithOptize: moment('10-10-2017 21:30', 'DD-MM-YYYY HH:mm'),
      buttonGroupDayInWeek: [
        {
          id: 0,
          value: 'Mon',
          text: 'Thứ hai',
          checked: false,
          disable: false,
        },
        {
          id: 1,
          value: 'Tue',
          text: 'Thứ ba',
          checked: false,
          disable: false,
        },
        {
          id: 2,
          value: 'Wed',
          text: 'Thứ tư',
          checked: false,
          disable: false,
        },
        {
          id: 3,
          value: 'Thu',
          text: 'Thứ năm',
          checked: false,
          disable: false,
        },
        {
          id: 4,
          value: 'Fri',
          text: 'Thứ sáu',
          checked: false,
          disable: false,
        },
        {
          id: 5,
          value: 'Sat',
          text: 'Thứ bảy',
          checked: false,
          disable: false,
        },
        {
          id: 6,
          value: 'Sun',
          text: 'Chủ nhật',
          checked: false,
          disable: false,
        },
      ],
      buttonGroupFieldType: [
        {
          id: 1,
          value: '5 vs 5',
          text: 'Sân 5 người',
        },
        {
          id: 2,
          value: '7 vs 7',
          text: 'Sân 7 người',
        },
      ],
    };
    this.handelCheckboxDay = this.handelCheckboxDay.bind(this);
  }
  componentWillMount() {
    const { buttonGroupDayInWeek } = this.state;
    const dayInWeek = buttonGroupDayInWeek;
    const index = buttonGroupDayInWeek.findIndex(
      day =>
        day.value ===
        moment()
          .locale('en')
          .format('ddd'),
    );
    for (let i = 0; i < dayInWeek.length; i++) {
      if (i === index) {
        dayInWeek[index].checked = true;
      } else {
        dayInWeek[i].checked = false;
      }
    }
    this.setState({
      buttonGroupDayInWeek: dayInWeek,
      daySelected: moment()
        .locale('en')
        .format('ddd'),
    });
  }
  async componentDidMount() {
    const { id, roleId } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
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
  handelHideModal(evt) {
    evt.preventDefault();
    this.setState({ isShowAddTime: false });
  }
  async handelCheckboxDay(day) {
    const { buttonGroupDayInWeek } = this.state;
    const dayInWeek = buttonGroupDayInWeek;
    dayInWeek[day.id].checked = !dayInWeek[day.id].checked;
    await this.setState({ buttonGroupDayInWeek: dayInWeek });
  }
  async handleInputChange(evt) {
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    const { buttonGroupDayInWeek } = this.state;
    if (name === 'daySelected') {
      const dayInWeek = buttonGroupDayInWeek;
      const index = buttonGroupDayInWeek.findIndex(day => day.value === value);
      for (let i = 0; i < dayInWeek.length; i++) {
        if (i === index) {
          dayInWeek[index].checked = true;
        } else {
          dayInWeek[i].checked = false;
        }
      }
      this.setState({ buttonGroupDayInWeek: dayInWeek });
    }
    if (value === '5 vs 5') {
      this.setState({ fieldTypeId: 1 });
    } else if (value === '7 vs 7') {
      this.setState({ fieldTypeId: 2 });
    }
    await this.setState({ [name]: value });
    console.log(this.state);
  }
  async handelTimeStartDayInputChange(evt) {
    await this.setState({ startDay: evt });
  }
  async handelTimeEndDayInputChange(evt) {
    await this.setState({ endDay: evt });
  }
  disableDayClick(timeEnable) {
    const { buttonGroupDayInWeek } = this.state;
    const tmpButtonGroupDayInWeek = buttonGroupDayInWeek;
    for (let i = 0; i < tmpButtonGroupDayInWeek.length; i++) {
      debugger;
      const filterDay = timeEnable.filter(
        day => day.dateInWeek === tmpButtonGroupDayInWeek[i].value,
      );
      if (filterDay.length > 0) {
        tmpButtonGroupDayInWeek[i].checked = false;
        tmpButtonGroupDayInWeek[i].disable = true;
      } else {
        tmpButtonGroupDayInWeek[i].disable = false;
      }
    }
    this.setState({ buttonGroupDayInWeek: tmpButtonGroupDayInWeek });
  }

  async handelShowModal(evt) {
    evt.preventDefault();
    const { timeEnable } = this.props.timeEnable;
    await this.disableDayClick(timeEnable);
    this.setState({ isShowAddTime: true });
  }

  async handleSubmitTimeInWeek(evt) {
    evt.preventDefault();
    const { id } = this.props.auth.user.data;
    const {
      startDay,
      endDay,
      peakPrice,
      idelPrice,
      isShowAddTime,
      fieldTypeId,
    } = this.state;
    const priceRegex = '^\\d+$';
    if (
      startDay !== null &&
      endDay !== null &&
      peakPrice !== undefined &&
      idelPrice !== undefined
    ) {
      if (startDay.hour() < endDay.hours() && endDay.hours() > 17) {
        if (peakPrice.match(priceRegex) && idelPrice.match(priceRegex)) {
          const dayAdd = this.state.buttonGroupDayInWeek.filter(
            data => data.checked === true,
          );
          if (dayAdd.length > 0) {
            for (let i = 0; i < dayAdd.length; i++) {
              await fetchUpdateTimeEnableInWeek(
                id,
                dayAdd[i].value,
                startDay.format('HH:mm'),
                '17:00',
                idelPrice,
                fieldTypeId,
              );
              await fetchUpdateTimeEnableInWeek(
                id,
                dayAdd[i].value,
                '17:00',
                endDay.format('HH:mm'),
                peakPrice,
                fieldTypeId,
              );
            }
            await this.setState({ isShowAddTime: !isShowAddTime });
            const data = await fetchGetTimeEnableInWeek(id);
            this.props.getAllTimeEnableInWeek(data.body);
          } else {
            this.setState({ message: 'Chưa chọn ngày' });
          }
        } else {
          this.setState({ message: 'Giá không hợp lệ' });
        }
      } else {
        this.setState({ message: 'Thời gian không hợp lệ' });
      }
    } else {
      this.setState({ message: 'Vui lòng điền giá tiền', isShowAddTime: true });
    }
  }

  handleOptimizeClick() {
    const { isOptimze } = this.state;
    this.setState({ isOptimze: !isOptimze });
  }

  handleSelectChange(evt) {
    const value = evt.target.value;
    const name = evt.target.name;
    this.setState({ [name]: parseInt(value) });
  }

  render() {
    const { timeEnable } = this.props.timeEnable;
    const { daySelected, fieldType } = this.state;
    const dayAfterFilter =
      timeEnable &&
      timeEnable.filter(
        data =>
          data.dateInWeek === daySelected &&
          data.fieldTypeId.name === fieldType,
      );

    if (!dayAfterFilter) {
      return <div className="loader" />;
    }
    const popoverRightPeak = (
      <Popover id="popover-positioned-right" title="Giờ thấp điểm">
        Giờ cao điểm sẽ sau
        <strong> 17:00</strong> đến giờ đóng cửa
      </Popover>
    );
    const popoverRightIdle = (
      <Popover id="popover-positioned-right" title="Giờ cao điểm">
        Giờ thấp điểm sẽ từ lúc mở cửa tới
        <strong> 17:00</strong>
      </Popover>
    );
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-md-4">
                <h2 className="page-header">Thiết lập giờ</h2>
              </div>
            </div>
          </div>
          <div className="col-md-10 col-md-offset-1">
            <div className="panel panel-dafault">
              <div className="panel panel-heading">
                <div className="row">
                  <div className="col-sm-8">
                    <div className="col-sm-6">
                      <select
                        className="form-control"
                        id="sel1"
                        value={this.state.daySelected}
                        onChange={this.handleInputChange.bind(this)}
                        name="daySelected"
                      >
                        {this.state.buttonGroupDayInWeek.map(day => (
                          <option key={day.id} value={day.value}>
                            {day.text}
                          </option>
                        ))}
                      </select>
                    </div>

                    <div className="col-sm-6">
                      <select
                        className="form-control"
                        id="sel1"
                        value={this.state.fieldType}
                        onChange={this.handleInputChange.bind(this)}
                        name="fieldType"
                      >
                        {this.state.buttonGroupFieldType.map(field => (
                          <option key={field.id} value={field.value}>
                            {field.text}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                  <div className="col-sm-4">
                    <button
                      className="btn btn-warning"
                      name="isShowUpdate"
                      onClick={this.handelShowModal.bind(this)}
                    >
                      <i className="glyphicon glyphicon-plus" /> Thêm mới khung
                      giờ
                    </button>
                  </div>
                </div>
              </div>
              <div className="panel-body">
                <div className="table-responsive">
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Từ</th>
                        <th>Đến</th>
                        <th>Giá</th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {dayAfterFilter.map((data, index) => (
                        <tr key={index}>
                          <td>{index + 1}</td>
                          <td>
                            {moment(
                              '10-10-2017 ' + data.startTime,
                              'DD-MM-YYYY HH:mm',
                            ).format('HH:mm')}
                          </td>
                          <td>
                            {moment(
                              '10-10-2017 ' + data.endTime,
                              'DD-MM-YYYY HH:mm',
                            ).format('HH:mm')}
                          </td>
                          <td>{data.price} nghìn đồng</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.isShowAddTime}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>
              Thêm mới khung giờ{' '}
              {this.state.fieldType === '5 vs 5'
                ? 'sân 5 người'
                : 'sân 7 người'}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form
              className="form-horizontal"
              onSubmit={this.handleSubmitTimeInWeek.bind(this)}
            >
              <p className="text-danger text-center">
                {this.state.message ? this.state.message : null}
              </p>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-4 control-label">
                  Giờ mỏ cửa
                </label>
                <div className="col-sm-8">
                  <div className="row">
                    <div className="col-sm-6">
                      <TimePicker
                        showSecond={false}
                        name="startDay"
                        defaultValue={this.state.startDay}
                        onChange={this.handelTimeStartDayInputChange.bind(this)}
                        disabledMinutes={this.configTimeDiable.bind(this)}
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-4 control-label">
                  Giá giờ cao điểm
                </label>
                <div className="col-sm-8">
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="input-group">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          name="peakPrice"
                          value={this.state.peakPrice}
                          onChange={this.handleInputChange.bind(this)}
                        />
                        <span className="input-group-addon">nghìn đồng</span>
                      </div>
                    </div>
                    <div className="col-sm-6 question-mark-style-div">
                      <OverlayTrigger
                        trigger="click"
                        placement="right"
                        overlay={popoverRightPeak}
                      >
                        <label className="btn btn-sm btn-default">
                          <i className="glyphicon glyphicon-question-sign question-mark-style-i " />
                        </label>
                      </OverlayTrigger>
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-4 control-label">
                  Giá giờ thấp điểm
                </label>
                <div className="col-sm-8">
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="input-group">
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          name="idelPrice"
                          value={this.state.idelPrice}
                          onChange={this.handleInputChange.bind(this)}
                        />
                        <span className="input-group-addon">nghìn đồng</span>
                      </div>
                    </div>
                    <div className="col-sm-6 question-mark-style-div">
                      <OverlayTrigger
                        trigger="click"
                        placement="right"
                        overlay={popoverRightIdle}
                      >
                        <label className="btn btn-sm btn-default">
                          <i className="glyphicon glyphicon-question-sign question-mark-style-i " />
                        </label>
                      </OverlayTrigger>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-offset-4">
                <label className="checkbox-inline optimize-time">
                  <input
                    type="checkbox"
                    checked={this.state.isOptimze}
                    onChange={this.handleOptimizeClick.bind(this)}
                  />
                  <strong>Tối ưu giờ cao điểm</strong>
                </label>
              </div>
              {!this.state.isOptimze ? (
                <div className="form-group">
                  <label
                    htmlFor="inputEmail3"
                    className="col-sm-4 control-label"
                  >
                    Giờ đóng cửa
                  </label>
                  <div className="col-sm-8">
                    <div className="row">
                      <div className="col-sm-6">
                        <TimePicker
                          showSecond={false}
                          name="endDay"
                          defaultValue={this.state.endDay}
                          onChange={this.handelTimeEndDayInputChange.bind(this)}
                          disabledMinutes={this.configTimeDiable.bind(this)}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              ) : (
                <div>
                  <div className="form-group">
                    <label
                      htmlFor="inputEmail3"
                      className="col-sm-4 control-label"
                    >
                      Thời gian đá bắt buộc
                    </label>
                    <div className="col-sm-8">
                      <div className="row">
                        <div className="col-sm-6">
                          <select
                            value={this.state.optimizeTime}
                            onChange={this.handleSelectChange.bind(this)}
                            className="form-control"
                            id="sel1"
                          >
                            <option value="1">1 tiếng</option>
                            <option value="1.5">1.5 tiếng</option>
                            <option value="2">2 tiếng</option>
                          </select>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="form-group">
                    <label
                      htmlFor="inputEmail3"
                      className="col-sm-4 control-label"
                    >
                      Số trận có thể đá ở 1 sân
                    </label>
                    <div className="col-sm-8">
                      <div className="row">
                        <div className="col-sm-6">
                          <select
                            value={this.state.optimizeNumOfMatch}
                            onChange={this.handleSelectChange.bind(this)}
                            className="form-control"
                            id="sel1"
                          >
                            <option value="1">1 trận</option>
                            <option value="2">2 trận</option>
                            <option value="3">3 trận</option>
                            <option value="4">4 trận</option>
                            <option value="5">5 trận</option>
                          </select>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="col-sm-12 text-center">
                    <h4>
                      Thời gian đóng cửa lúc{' '}
                      {this.state.endTimeWithOptize.format('HH:mm')}
                    </h4>
                  </div>
                </div>
              )}

              {this.state.buttonGroupDayInWeek.map(day => (
                <label className="checkbox-inline" key={day.id}>
                  <input
                    type="checkbox"
                    value={day.value}
                    checked={day.checked}
                    onChange={() => this.handelCheckboxDay(day)}
                    disabled={day.disable}
                  />{' '}
                  {day.text}
                </label>
              ))}
            </form>
          </Modal.Body>
          <Modal.Footer>
            <button
              className="btn btn-primary"
              type="submit"
              onClick={this.handleSubmitTimeInWeek.bind(this)}
            >
              Cập nhật
            </button>
            <button
              onClick={this.handelHideModal.bind(this)}
              className="btn btn-danger"
            >
              Đóng
            </button>
          </Modal.Footer>
        </Modal>
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
