import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  doLoginSuccessful,
  accessDenied,
} from '../redux/guest/guest-action-creators';
import { fetchGetFreeTime, fetchBookMatch } from '../apis/field-owner-apis';
import {
  getAllFreeTime5vs5,
  getAllFreeTime7vs7,
} from '../redux/field-owner/field-owner-action-creator';
import TimePicker from 'rc-time-picker';
import moment from 'moment';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Modal } from 'react-bootstrap';
import 'notyf/dist/notyf.min.css';
import { toast } from 'react-toastify';
class FreeTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      isShowBookMatch: false,
      fieldTypeSelected: undefined,
      startTime: undefined,
      endTime: undefined,
      messageBookMatch: undefined,
      timeUpperLimit: undefined,
      timeLowerLimit: undefined,
    };
    this.handelShowModalBookMatch = this.handelShowModalBookMatch.bind(this);
  }

  hoursToPick() {
    const hours = [];
    for (var i = 5; i < 24; i++) {
      hours.push({ value: i, text: i, id: i });
    }
    return hours;
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
  
  configHoursDisable(){
    const {timeLowerLimit, timeUpperLimit} = this.state;
    const lower = moment('10-10-2017 ' + timeLowerLimit).hours();
    const upper = moment('10-10-2017 ' + timeUpperLimit).hours();
    const hours =  [];
    for (let i = 0; i < lower; i++) {
      hours.push(i);
    }
    for (let i = upper + 1; i < 24 ; i++) {
      hours.push(i);
    }
    return hours;
  }

  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (
        authLocalStorage === null ||
        authLocalStorage.roleId.roleName !== 'owner'
      ) {
        this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const idLocal = authLocalStorage.id;
        await this.props.doLoginSuccessful(authLocalStorage);
        try {
          const data5vs5 = await fetchGetFreeTime(
            idLocal,
            1,
            this.state.dateSelected.format('DD-MM-YYYY'),
          );
          const data7vs7 = await fetchGetFreeTime(
            idLocal,
            2,
            this.state.dateSelected.format('DD-MM-YYYY'),
          );
          await this.props.getAllFreeTime5vs5(data5vs5.body);
          await this.props.getAllFreeTime7vs7(data7vs7.body);
        } catch (error) {
          console.log('error: ', error);
        }
      }
    } else {
      try {
        const data5vs5 = await fetchGetFreeTime(
          id,
          1,
          this.state.dateSelected.format('DD-MM-YYYY'),
        );
        const data7vs7 = await fetchGetFreeTime(
          id,
          2,
          this.state.dateSelected.format('DD-MM-YYYY'),
        );
        await this.props.getAllFreeTime5vs5(data5vs5.body);
        await this.props.getAllFreeTime7vs7(data7vs7.body);
      } catch (error) {
        console.log('error: ', error);
      }
    }
  }

  async handelShowModalBookMatch(freetime) {
    // freetime.preventDefault();
    // console.log(freetime);
    await this.setState({
      isShowBookMatch: true,
      fieldTypeSelected: freetime.fieldTypeId.id,
      startTime: moment('10-10-2017 ' + freetime.startTime),
      endTime: moment('10-10-2017 ' + freetime.endTime),
      timeLowerLimit: freetime.startTime,
      timeUpperLimit: freetime.endTime,
    });
    console.log(this.state);
  }

  handleHideModalBookMatch(evt) {
    evt.preventDefault();
    this.setState({ isShowBookMatch: false, messageBookMatch: undefined });
  }

  async handleDateChange(date) {
    const { id } = this.props.auth.user.data;
    await this.setState({
      dateSelected: date,
    });
    const data5vs5 = await fetchGetFreeTime(
      id,
      1,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    const data7vs7 = await fetchGetFreeTime(
      id,
      2,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    await this.props.getAllFreeTime5vs5(data5vs5.body);
    await this.props.getAllFreeTime7vs7(data7vs7.body);
  }

  async handelEndTimeInputChange(evt) {
    await this.setState({ endTime: evt });
    console.log(this.state);
  }

  async handelTimeStartDayInputChange(evt) {
    await this.setState({ startTime: evt });
    console.log(this.state);
  }

  async handelBookMatchSubmit(evt) {
    evt.preventDefault();
    const { id } = this.props.auth.user.data;
    const {
      endTime,
      startTime,
      messageBookMatch,
      timeLowerLimit,
      timeUpperLimit,
    } = this.state;
    // debugger;
    // console.log(
    //   endTime.hours() >= startTime.hours(),
    //   endTime.minutes() - startTime.minutes() >= 30,
    // );
    if (
      endTime.hours() > startTime.hours()
      // endTime.minutes() - startTime.minutes() >= 30
      // startTime >= timeLowerLimit &&
      // endTime <= timeUpperLimit
    ) {
      const bookMatchRes = await fetchBookMatch(
        this.state.dateSelected.format('DD-MM-YYYY'),
        this.state.endTime.format('HH:mm'),
        id,
        this.state.fieldTypeSelected,
        this.state.startTime.format('HH:mm'),
      );
      if (bookMatchRes.status === 201 && bookMatchRes.body !== null) {
        this.setState({ isShowBookMatch: false });
        toast.success('Đặt sân thành công!');
        const data5vs5 = await fetchGetFreeTime(
          id,
          1,
          this.state.dateSelected.format('DD-MM-YYYY'),
        );
        const data7vs7 = await fetchGetFreeTime(
          id,
          2,
          this.state.dateSelected.format('DD-MM-YYYY'),
        );
        await this.props.getAllFreeTime5vs5(data5vs5.body);
        await this.props.getAllFreeTime7vs7(data7vs7.body);
      } else {
        this.setState({ isShowBookMatch: false });
        toast.error('Đặt sân thất bại!');
      }
    } else {
      this.setState({ messageBookMatch: 'Thời gian không hợp lệ' });
    }
  }

  render() {
    const myStyle = { padding: 20 };
    const { freeTime5vs5, freeTime7vs7 } = this.props;
    const { dateSelected } = this.state;
    const hours = this.hoursToPick();
    // console.log(moment('20-10-2017 '+this.state.timeLowerLimit));
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
            <div className="col-sm-4">
              <h2 className="page-header">Thời gian trống</h2>
            </div>
            <div className="col-sm-4">
              <div className="page-header">
            <h4>{this.state.dateSelected.format("dddd, Do MMMM YYYY")}</h4>
              </div>
            </div>
            <div className="col-sm-4">
              <div className="page-header">
                <form>
                  <div className="form-group">
                    <DatePicker
                      selected={this.state.dateSelected}
                      onChange={this.handleDateChange.bind(this)}
                      className="form-control"
                      todayButton={'Hôm nay'}
                    />
                  </div>
                </form>
              </div>
            </div>
          </div>

          <div className="col-lg-12">
            <div className="row" style={myStyle}>
              <div className="col-lg-6">
                <div className="panel panel-success">
                  <div className="panel-heading">
                    {' '}
                    <h4 className="text-center loginHeader">Loại sân 5 người</h4>
                  </div>
                  <div className="panel-body">
                    {freeTime5vs5.length > 0 ? (
                      freeTime5vs5.map(freeTime => (
                        <div key={freeTime.id} className="row">
                          <div className="col-sm-9">
                            <h4>
                              {moment(
                                '10-10-2017 ' + freeTime.startTime,
                              ).format('HH:mm')}{' '}
                              -{' '}
                              {moment('10-10-2017 ' + freeTime.endTime).format(
                                'HH:mm',
                              )}
                            </h4>
                          </div>
                          <div>
                            <h4>
                            <button
                              onClick={() =>
                                this.handelShowModalBookMatch(freeTime)}
                              className="btn btn-primary"
                            >
                              Đặt sân
                            </button>
                            </h4>
                          </div>
                        </div>
                      ))
                    ) : (
                      <h4>Không có thời gian rảnh</h4>
                    )}
                  </div>
                </div>
              </div>
              <div className="col-lg-6">
                <div className="panel panel-success">
                  <div className="panel-heading">
                    {' '}
                    <h4 className="text-center loginHeader">Loại sân 7 người</h4>
                  </div>
                  <div className="panel-body">
                    {freeTime7vs7.length > 0 ? (
                      freeTime7vs7.map(freeTime => (
                        <div key={freeTime.id} className="row">
                          <div className="col-sm-9">
                            <h4>
                              {moment(
                                '10-10-2017 ' + freeTime.startTime,
                              ).format('HH:mm')}{' '}
                              -{' '}
                              {moment('10-10-2017 ' + freeTime.endTime).format(
                                'HH:mm',
                              )}
                            </h4>
                          </div>
                          <div className="col-sm-3">
                            <button
                              onClick={() =>
                                this.handelShowModalBookMatch(freeTime)}
                              className="btn btn-primary"
                            >
                              Đặt sân
                            </button>
                          </div>
                        </div>
                      ))
                    ) : (
                      <h4>Không có thời gian rảnh</h4>
                    )}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.isShowBookMatch}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Đặt sân</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form
              className="form-horizontal"
              onSubmit={this.handelBookMatchSubmit.bind(this)}
            >
              <p className="text-center text-danger">
                {this.state.messageBookMatch
                  ? this.state.messageBookMatch
                  : null}
              </p>
              <div>
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
                          onChange={this.handelTimeStartDayInputChange.bind(
                            this,
                          )}
                          defaultValue={moment(
                            '10-10-2017 ' + this.state.timeLowerLimit,
                          )}
                          disabledMinutes={this.configTimeDiable.bind(this)}
                          disabledHours={this.configHoursDisable.bind(this)}
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
                          onChange={this.handelEndTimeInputChange.bind(this)}
                          defaultValue={moment(
                            '10-10-2017 ' + this.state.timeUpperLimit,
                          )}
                          disabledMinutes={this.configTimeDiable.bind(this)}
                          disabledHours={this.configHoursDisable.bind(this)}
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <div className="col-sm-offset-3 col-sm-9">
                  <button
                    className="btn btn-primary"
                    type="submit"
                    name="isShowUpdate"
                  >
                    Đặt sân
                  </button>
                </div>
              </div>
            </form>
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.handleHideModalBookMatch.bind(this)}
              className="btn btn-danger"
            >
              Huỷ
            </button>
          </Modal.Footer>
        </Modal>
      </div>
      </div>
    );
  }
}

function mapPropsToState(state) {
  return {
    auth: state.auth,
    freeTime5vs5: state.freeTime.freeTime5vs5,
    freeTime7vs7: state.freeTime.freeTime7vs7,
  };
}

export default connect(mapPropsToState, {
  getAllFreeTime5vs5,
  getAllFreeTime7vs7,
  doLoginSuccessful,
  accessDenied,
})(FreeTime);
