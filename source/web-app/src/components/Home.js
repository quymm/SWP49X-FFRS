import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  fetchGetMatchByFieldOwnerAndDay,
  fetchGetFreeTime,
  fetchBookMatch,
} from '../apis/field-owner-apis';
import {
  GetMatchByFieldOwnerAndDay,
  getAllFreeTime5vs5,
  getAllFreeTime7vs7,
} from '../redux/field-owner/field-owner-action-creator';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
import { Link } from 'react-router-dom';
import Clock from './Clock';
import TimePicker from 'rc-time-picker';
import FreeTime from '../containts/FreeTime';
import MatchByDate from '../containts/MatchByDate';
class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      currentTime: new Date().toLocaleTimeString([], { hour12: false }),
      currentShowPage: '',
      openedTab: 3,
      bookMatchStartTime: undefined,
      bookMatchEndTime: 60,
      bookMatchMessage: undefined,
      bookMatchFieldType: 1,
      buttonGroupTab: [
        {
          id: 1,
          text: 'Các trân đang đá',
          value: 1,
        },
        {
          id: 2,
          text: 'Các trân trong ngày',
          value: 2,
        },
        {
          id: 3,
          text: 'Thời gian rảnh',
          value: 3,
        },
        {
          id: 4,
          text: 'Đặt sân',
          value: 4,
        },
      ],
    };
    this.timer = setInterval(() => {
      this.setState({
        currentTime: new Date().toLocaleTimeString([], { hour12: false }),
      });
    }, 1000);
  }
  componentWillUnmount() {
    clearInterval(this.timer);
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    console.log('user id: ', id);
    try {
      fetchGetMatchByFieldOwnerAndDay(
        1,
        this.state.dateSelected.format('DD-MM-YYYY'),
        1,
      ).then(data => {
        this.props.GetMatchByFieldOwnerAndDay(data);
      });
      const data5vs5 = await fetchGetFreeTime(
        1,
        1,
        this.state.dateSelected.format('DD-MM-YYYY'),
      );
      const data7vs7 = await fetchGetFreeTime(
        1,
        2,
        this.state.dateSelected.format('DD-MM-YYYY'),
      );
      await this.props.getAllFreeTime5vs5(data5vs5);
      await this.props.getAllFreeTime7vs7(data7vs7);
    } catch (error) {
      console.log('error: ', error);
    }
  }
  async handelEndTimeInputChange(evt) {
    await this.setState({ bookMatchEndTime: evt.format('HH:mm') });
  }

  async handelTimeStartDayInputChange(evt) {
    await this.setState({ bookMatchStartTime: evt.format('HH:mm') });
  }

  async handleSubmitBookMatch(evt) {
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
      await fetchBookMatch(
        dateSelected.format('DD-MM-YYYY'),
        1,
        bookMatchEndTime,
        bookMatchFieldType,
        bookMatchStartTime,
        bookMatchEndTime,
      );
      this.setState({ openedTab: 2 });
      debugger;
    }
  }
  async handleDateChange(date) {
    const { id } = this.props.auth.user.data;
    await this.setState({
      dateSelected: date,
    });
    fetchGetMatchByFieldOwnerAndDay(
      1,
      this.state.dateSelected.format('DD-MM-YYYY'),
      1,
    ).then(data => {
      this.props.GetMatchByFieldOwnerAndDay(data);
    });
    const data5vs5 = await fetchGetFreeTime(
      1,
      1,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    const data7vs7 = await fetchGetFreeTime(
      1,
      2,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    await this.props.getAllFreeTime5vs5(data5vs5);
    await this.props.getAllFreeTime7vs7(data7vs7);
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
    const { listMatch, freeTime5vs5, freeTime7vs7 } = this.props;
    const { openedTab, buttonGroupTab } = this.state;
    console.log(this.props);
    const renerBookMatch = (
      <form
        className="form-horizontal"
        onSubmit={this.handleSubmitBookMatch.bind(this)}
      >
        <div>
          <p>
            {this.state.bookMatchMessage ? this.state.bookMatchMessage : null}
          </p>
          <div className="form-group">
            <label htmlFor="inputEmail3" className="col-sm-3 control-label">
              Từ
            </label>
            <div className="col-sm-9">
              <div className="row">
                <div className="col-sm-6">
                  <TimePicker
                    showSecond={false}
                    onChange={this.handelTimeStartDayInputChange.bind(this)}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className="form-group">
            <label htmlFor="inputEmail3" className="col-sm-3 control-label">
              Thời gian
            </label>
            <div className="col-sm-9">
              <div className="row">
                <div className="col-sm-6">
                  <TimePicker
                    showSecond={false}
                    onChange={this.handelEndTimeInputChange.bind(this)}
                  />
                  {/* <select
                    value={this.state.bookMatchDuration}
                    onChange={this.handleInputChange.bind(this)}
                    className="form-control"
                    id="sel1"
                    type="checkbox"
                    name="bookMatchDuration"
                  >
                    <option value="60">60 phút</option>
                    <option value="90">90 phút</option>
                    <option value="120">120 phút</option>
                    <option value="150">150 phút</option>
                    <option value="180">180 phút</option>
                    <option value="210">210 phút</option>
                    <option value="240">240 phút</option>
                  </select> */}
                </div>
              </div>
            </div>
          </div>
          <div className="form-group">
            <label htmlFor="sel1" className="col-sm-3 control-label">
              Loại sân
            </label>
            <div className="col-sm-2">
              <select
                value={this.state.bookMatchFieldType}
                onChange={this.handleInputChange.bind(this)}
                className="form-control"
                id="sel1"
                name="bookMatchFieldType"
                type="checkbox"
              >
                <option value="1">5 vs 5</option>
                <option value="2">7 vs 7</option>
              </select>
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
    );
    const renderFreeTime = (
      <FreeTime freeTime5vs5={freeTime5vs5} freeTime7vs7={freeTime7vs7} />
    );
    const renderMatch = <MatchByDate listMatch={listMatch} />;
    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-3">
              <h2 className="page-header">Trang chủ</h2>
            </div>
            <div className="col-lg-3">
              <Clock time={this.state.currentTime} />
            </div>

            <div className="col-lg-3">
              <div className="page-header">
                <form className="navbar-form navbar-left">
                  <div className="form-group">
                    <DatePicker
                      selected={this.state.dateSelected}
                      onChange={this.handleDateChange.bind(this)}
                      className="form-control"
                      todayButton={'Today'}
                      withPortal
                    />
                  </div>
                </form>
              </div>
            </div>

            <div className="col-lg-3">
              <div className="page-header">
                <form className="navbar-form navbar-left">
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      placeholder="Search"
                    />
                  </div>
                </form>
              </div>
            </div>
          </div>
          <div className="col-lg-12">
            <div className="row" style={myStyle}>
              {buttonGroupTab.map(tab => (
                <div className="col-lg-3" key={tab.id}>
                  <button
                    className={`btn btn-lg btn-primary btn-block ${tab.value ==
                    this.state.openedTab
                      ? 'active'
                      : ''}`}
                    onClick={this.handleInputChange.bind(this)}
                    name="openedTab"
                    value={tab.value}
                  >
                    {tab.text}
                  </button>
                </div>
              ))}
            </div>
            {/* <div className="btn-group btn-group-justified">
              <a className="btn btn-primary"><button>Các trận đang đá</button></a>
              <a className="btn btn-primary active">Các trận trong ngày</a>
              <a className="btn btn-primary">Thời gian rảnh</a>
              <a className="btn btn-primary">Đặt sân</a>
            </div> */}
          </div>
          <div className="col-lg-12">
            <div className="row">
              {openedTab === 1
                ? 'tab cac tran dang da'
                : openedTab === 2
                  ? renderMatch
                  : openedTab === 3 ? renderFreeTime : renerBookMatch}
              {}
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
    freeTime5vs5: state.freeTime.freeTime5vs5,
    freeTime7vs7: state.freeTime.freeTime7vs7,
    auth: state.auth,
  };
}

export default connect(mapStateToProps, {
  GetMatchByFieldOwnerAndDay,
  getAllFreeTime5vs5,
  getAllFreeTime7vs7,
})(Home);
