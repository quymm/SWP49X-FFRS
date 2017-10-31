import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  fetchGetMatchByFieldOwnerAndDay,
  fetchBookMatch,
  fetchGetFreeFieldByTime,
  fetchSetFieldToMatch,
} from '../apis/field-owner-apis';
import {
  GetMatchByFieldOwnerAndDay,
  getAllFreeField,
} from '../redux/field-owner/field-owner-action-creator';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
import { Link } from 'react-router-dom';
import TimePicker from 'rc-time-picker';
import MatchByDate from '../containts/MatchByDate';
import {
  doLoginSuccessful,
  accessDenied,
} from '../redux/guest/guest-action-creators';
import { Modal } from 'react-bootstrap';
import { toast } from 'react-toastify';
class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      bookMatchStartTime: undefined,
      bookMatchEndTime: 60,
      bookMatchMessage: undefined,
      bookMatchFieldType: 1,
      isShowUpdateField: false,
      fieldSelected: undefined,
      timeSlotSelected: undefined,
      filterName: 'ans',
      messages: {},
    };
    this.handleShowModalField = this.handleShowModalField.bind(this);
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
          const match = await fetchGetMatchByFieldOwnerAndDay(
            idLocal,
            this.state.dateSelected.format('DD-MM-YYYY'),
            1,
          );
          await this.props.GetMatchByFieldOwnerAndDay(match.body);
        } catch (error) {
          console.log('error: ', error);
        }
      }
    } else {
      try {
        const match = await fetchGetMatchByFieldOwnerAndDay(
          id,
          this.state.dateSelected.format('DD-MM-YYYY'),
          1,
        );
        await this.props.GetMatchByFieldOwnerAndDay(match.body);
      } catch (error) {
        console.log('error: ', error);
      }
    }
  }

  async handelSetFieldSubmit(evt) {
    evt.preventDefault();
    const { timeSlotSelected, fieldSelected } = this.state;
    const { id } = this.props.auth.user.data;
    const setFieldRes = await fetchSetFieldToMatch(
      timeSlotSelected,
      fieldSelected,
    );
    if (setFieldRes.status === 200) {
      this.setState({ isShowUpdateField: false });
      const match = await fetchGetMatchByFieldOwnerAndDay(
        id,
        this.state.dateSelected.format('DD-MM-YYYY'),
        1,
      );
      await this.props.GetMatchByFieldOwnerAndDay(match.body);
      toast.success('Cập nhật sân thành công!');
    }
  }

  async handleShowModalField(match) {
    //evt.preventDefault();
    const { id } = this.props.auth.user.data;
    console.log(typeof match);
    const fieldTypeId = parseInt(match.timeSlotEntity.fieldTypeId.id);
    const time = match.timeSlotEntity.startTime;
    const freeField = await fetchGetFreeFieldByTime(
      id,
      fieldTypeId,
      this.state.dateSelected.format('DD-MM-YYYY'),
      time,
    );
    await this.props.getAllFreeField(freeField.body);
    if (freeField.body.length > 0) {
      this.setState({
        fieldSelected: freeField.body[0].id,
        timeSlotSelected: match.timeSlotEntity.id,
      });
    }
    this.setState({ isShowUpdateField: true });
  }
  handleHideModalField(evt) {
    evt.preventDefault();
    this.setState({ isShowUpdateField: false });
  }

  async handleSubmitBookMatch(evt) {
    const { id } = this.props.auth.user.data;
    evt.preventDefault();
    const {
      bookMatchMessagem,
      bookMatchEndTime,
      bookMatchFieldType,
      dateSelected,
      bookMatchStartTime,
    } = this.state;
    if (
      (bookMatchEndTime !== undefined && bookMatchStartTime !== undefined,
      bookMatchFieldType !== undefined)
    ) {
      const bookMatchRes = await fetchBookMatch(
        dateSelected.format('DD-MM-YYYY'),
        bookMatchEndTime,
        id,
        bookMatchFieldType,
        bookMatchStartTime,
        bookMatchEndTime,
      );
      if (bookMatchRes.status === 200 && bookMatchRes.body.length > 0) {
        this.setState({ openedTab: 2 });
      } else {
        this.setState({ bookMatchMessage: 'Đặt sân thất bại' });
      }
    }
  }
  async handleDateChange(date) {
    const { id } = this.props.auth.user.data;
    await this.setState({
      dateSelected: date,
    });
    const match = await fetchGetMatchByFieldOwnerAndDay(
      id,
      this.state.dateSelected.format('DD-MM-YYYY'),
      1,
    );
    await this.props.GetMatchByFieldOwnerAndDay(match.body);
  }

  async handleInputChange(evt) {
    const target = evt.target;
    const value =
      target.type === 'checkbox' ? target.checked : parseInt(target.value);
    const name = target.name;
    await this.setState({ [name]: value });
    console.log(this.state);
  }

  render() {
    const myStyle = { padding: 20 };
    const { listMatch, freeField } = this.props;
    const { isShowUpdateField, filterName, messages } = this.state;
    console.log(this.props);
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="row">
                <div className="col-md-4">
                  <h2 className="page-header">Trận trong ngày</h2>
                </div>
                <div className="col-sm-4">
                  <div className="page-header">
                    <h4>
                      {this.state.dateSelected.format('dddd, Do MMMM YYYY')}
                    </h4>
                  </div>
                </div>
                <div className="col-md-4">
                  <div className="page-header">
                    <form>
                      <div className="form-group">
                        <DatePicker
                          selected={this.state.dateSelected}
                          onChange={this.handleDateChange.bind(this)}
                          className="form-control"
                          todayButton={'Today'}
                        />
                      </div>
                    </form>
                  </div>
                </div>

                {/* <div className="col-sm-3">
                  <div className="page-header">
                    <form>
                      <div className="form-group">
                        <input
                          type="text"
                          className="form-control"
                          placeholder="Tìm kiếm theo tên"
                        />
                      </div>
                    </form>
                  </div>
                </div> */}
              </div>
              <div className="col-md-12">
                <div className="row">
                  {listMatch.length > 0
                    ? listMatch.map(listMatch => (
                        <div
                          key={listMatch.timeSlotEntity.id}
                          className="col-sm-6"
                        >
                          <div className="panel panel-default">
                            <div className="panel-body padding-top-bot-none">
                              <div className="row">
                                <div className="col-md-3">
                                  <h4 className="text-center">
                                    <strong>
                                      {listMatch.user.profileId.name}
                                    </strong>
                                  </h4>
                                </div>
                                <div className="col-md-6">
                                  <div
                                    className={`alert ${!listMatch.user
                                      .username === listMatch.opponent.username
                                      ? 'tourMatch'
                                      : 'friendlyMatch'} margin-bot-none`}
                                  >
                                    <h3 className="text-center text-primary">
                                      <strong>
                                        {moment(
                                          '10-10-2017 ' +
                                            listMatch.timeSlotEntity.startTime,
                                        ).format('HH:mm')}
                                      </strong>{' '}
                                    </h3>
                                    <p className="text-center">
                                      <strong>
                                        {moment(
                                          '10-10-2017 ' +
                                            listMatch.timeSlotEntity.endTime,
                                        ).hour() *
                                          60 +
                                          moment(
                                            '10-10-2017 ' +
                                              listMatch.timeSlotEntity.endTime,
                                          ).minute() -
                                          (moment(
                                            '10-10-2017 ' +
                                              listMatch.timeSlotEntity
                                                .startTime,
                                          ).hour() *
                                            60 +
                                            moment(
                                              '10-10-2017 ' +
                                                listMatch.timeSlotEntity
                                                  .startTime,
                                            ).minute())}{' '}
                                        phút
                                      </strong>
                                    </p>
                                    <p className="text-center">
                                      <strong>
                                        {
                                          listMatch.timeSlotEntity.fieldTypeId
                                            .name
                                        }
                                      </strong>
                                    </p>
                                    <p className="text-center">
                                      <strong>
                                        {listMatch.timeSlotEntity.fieldId
                                          ? 'Sân: ' +
                                            listMatch.timeSlotEntity.fieldId
                                              .name
                                          : 'Chưa xếp sân'}
                                      </strong>
                                    </p>
                                    <p className="text-center">
                                      <button
                                        onClick={() =>
                                          this.handleShowModalField(listMatch)}
                                        className="btn btn-md btn-primary"
                                      >
                                        Cập nhật sân
                                      </button>
                                    </p>
                                  </div>
                                </div>
                                <div className="col-md-3">
                                  <h4 className="text-center ">
                                    <strong>
                                      {listMatch.opponent.profileId.name}
                                    </strong>
                                  </h4>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      ))
                    : null}
                </div>
              </div>

              <Modal
                /* {...this.props} */
                show={this.state.isShowUpdateField}
                onHide={this.hideModal}
                dialogClassName="custom-modal"
              >
                <Modal.Header>
                  <Modal.Title>Thiết lập sân</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  {freeField.length > 0 ? (
                    <form
                      className="form-horizontal"
                      onSubmit={this.handelSetFieldSubmit.bind(this)}
                    >
                      <div className="form-group">
                        <label
                          htmlFor="inputEmail3"
                          className="col-md-3 control-label"
                        >
                          Tên sân
                        </label>
                        <div className="col-md-9">
                          <div className="row">
                            <div className="col-md-6">
                              <select
                                value={this.state.fieldSelected}
                                onChange={this.handleInputChange.bind(this)}
                                className="form-control"
                                id="sel1"
                                name="fieldSelected"
                                type="checkbox"
                              >
                                {freeField.map(freeField => (
                                  <option
                                    key={freeField.id}
                                    value={freeField.id}
                                  >
                                    {freeField.name}
                                  </option>
                                ))}
                              </select>
                            </div>
                            <div className="col-md-3">
                              <button type="submit" className="btn btn-primary">
                                Cập nhật sân
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </form>
                  ) : (
                    <h3>Không có sân trống</h3>
                  )}
                </Modal.Body>
                <Modal.Footer>
                  <button
                    onClick={this.handleHideModalField.bind(this)}
                    className="btn btn-danger"
                  >
                    Huỷ
                  </button>
                </Modal.Footer>
              </Modal>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
function mapStateToProps(state) {
  return {
    listMatch: state.listMatch.listMatch,
    auth: state.auth,
    freeField: state.freeField.freeField,
    notify: state.notify,
  };
}

export default connect(mapStateToProps, {
  GetMatchByFieldOwnerAndDay,
  getAllFreeField,
  doLoginSuccessful,
  accessDenied,
})(Home);
