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
import { Tabs, Tab, Modal, Checkbox } from 'react-bootstrap';
import moment from 'moment';
class SettingTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fieldType: '5 vs 5',
      fieldTypeId: 1,
      daySelected: 'Mon',
      startDay: moment('10-10-2017 06:00:00'),
      endDay: moment('10-10-2017 22:00:00'),
      price: undefined,
      isShowAddTime: false,
      buttonGroupDayInWeek: [
        {
          id: 0,
          value: 'Mon',
          text: 'Thứ hai',
          checked: false,
        },
        {
          id: 1,
          value: 'Tue',
          text: 'Thứ ba',
          checked: false,
        },
        {
          id: 2,
          value: 'Wed',
          text: 'Thứ tư',
          checked: false,
        },
        {
          id: 3,
          value: 'Thu',
          text: 'Thứ năm',
          checked: false,
        },
        {
          id: 4,
          value: 'Fri',
          text: 'Thứ sáu',
          checked: false,
        },
        {
          id: 5,
          value: 'Sat',
          text: 'Thứ bảy',
          checked: false,
        },
        {
          id: 6,
          value: 'Sun',
          text: 'Chủ nhật',
          checked: false,
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
  handelHideModal(evt) {
    evt.preventDefault();
    this.setState({ isShowAddTime: false });
  }
  async handelCheckboxDay(day) {
    const { buttonGroupDayInWeek } = this.state;
    const dayInWeek = buttonGroupDayInWeek;
    dayInWeek[day.id].checked = !dayInWeek[day.id].checked;
    await this.setState({ buttonGroupDayInWeek: dayInWeek });
    console.log(this.state);
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
    console.log(this.state);
  }
  async handelTimeEndDayInputChange(evt) {
    await this.setState({ endDay: evt });
    console.log(this.state);
  }

  handelShowModal(evt) {
    evt.preventDefault();
    this.setState({ isShowAddTime: true });
  }

  async handleSubmitTimeInWeek(evt) {
    evt.preventDefault();
    const { id } = this.props.auth.user.data;
    const {
      startDay,
      endDay,
      price,
      daySelected,
      isShowAddTime,
      fieldTypeId,
    } = this.state;

    if (startDay !== null && endDay !== null && price !== null) {
      const dayAdd = this.state.buttonGroupDayInWeek.filter(
        data => data.checked === true,
      );
      for (let i = 0; i < dayAdd.length; i++) {
        await fetchUpdateTimeEnableInWeek(
          id,
          dayAdd[i].value,
          startDay.format('HH:mm'),
          endDay.format('HH:mm'),
          price,
          fieldTypeId,
        );
      }
      await this.setState({ isShowAddTime: !isShowAddTime });
      const data = await fetchGetTimeEnableInWeek(id);
      this.props.getAllTimeEnableInWeek(data.body);
      this.props.history.push('/app/setting-time');
    }
    // await this.setState({ isShowAddTime: !isShowAddTime });
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
      return <div className="loader" />;
    }
    const { buttonGroupDayInWeek, buttonGroupFieldType } = this.state;
    // console.log(dayAfterFilter);
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
                        <th>Nghìn đồng/giờ</th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {dayAfterFilter.map((data, index) => (
                        <tr key={index}>
                          <td>{index + 1}</td>
                          <td>{data.startTime}</td>
                          <td>{data.endTime}</td>
                          <td>{data.price}</td>
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
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Từ
                </label>
                <div className="col-sm-9">
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
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Đến
                </label>
                <div className="col-sm-9">
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
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Giá
                </label>
                <div className="col-sm-9">
                  <div className="row">
                    <div className="col-sm-5">
                      <div className="input-group">
                        
                        <input
                          type="text"
                          className="form-control"
                          id="inputPassword3"
                          name="price"
                          value={this.state.price}
                          onChange={this.handleInputChange.bind(this)}
                        />
                        <span className="input-group-addon">
                          nghìn đồng
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              {this.state.buttonGroupDayInWeek.map(day => (
                <label className="checkbox-inline" key={day.id}>
                  <input
                    type="checkbox"
                    value={day.value}
                    checked={day.checked}
                    onChange={() => this.handelCheckboxDay(day)}
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
