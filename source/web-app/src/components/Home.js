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

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      currentTime: new Date().toLocaleTimeString([], { hour12: false }),
      currentShowPage: '',
      openedTab: 4,
      bookMatchStartTime: undefined,
      bookMatchEndTime: 60,
      bookMatchMessage: undefined,
      bookMatchFieldType: undefined,
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
    console.log('didmount: ', this.state.dateSelected.format('MMM DD YYYY'));
    const {} = this.props.auth;
    fetchGetMatchByFieldOwnerAndDay(
      2,
      this.state.dateSelected.format('DD-MM-YYYY'),
      1,
    ).then(data => {
      this.props.GetMatchByFieldOwnerAndDay(data);
    });
    const data5vs5 = await fetchGetFreeTime(
      2,
      1,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    const data7vs7 = await fetchGetFreeTime(
      2,
      2,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    await this.props.getAllFreeTime5vs5(data5vs5);
    await this.props.getAllFreeTime7vs7(data7vs7);
  }
  async handelEndTimeInputChange(evt) {
    await this.setState({ bookMatchEndTime: evt.format('HH:mm') });
    console.log(this.state);
  }

  async handelTimeStartDayInputChange(evt) {
    await this.setState({ bookMatchStartTime: evt.format('HH:mm') });
    console.log(this.state);
  }

  async handleSubmitBookMatch(evt) {
    evt.preventDefault();
    const {
      
      bookMatchMessagem,
      bookMatchEndTime,
      bookMatchFieldType,
      dateSelected,
      bookMatchStartTime
    } = this.state;
    if (
      (bookMatchEndTime !== undefined && bookMatchStartTime !== undefined,
      bookMatchFieldType !== undefined)
    ) {
      await fetchBookMatch(
        dateSelected.format('DD-MM-YYYY'),
        2,
        bookMatchFieldType,
        
        bookMatchStartTime,
        bookMatchEndTime
      );
      this.setState({openedTab: 2})
    }
  }
  async handleDateChange(date) {
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
      2,
      1,
      this.state.dateSelected.format('DD-MM-YYYY'),
    );
    const data7vs7 = await fetchGetFreeTime(
      2,
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
    const { openedTab } = this.state;
    console.log(this.props);
    console.log(freeTime5vs5.length);
    if (freeTime5vs5.length < 1) {
      console.log('loading........');
      return <h2>loading...</h2>;
    }

    const renerBookMatch = (
      <form
        className="form-horizontal"
        onSubmit={this.handleSubmitBookMatch.bind(this)}
      >
        <div>
          <p>{this.state.bookMatchMessage? this.state.bookMatchMessage : null}</p>
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
              >
                <option value="1">5 vs 5</option>
                <option value="2">7 vs 7</option>
              </select>
            </div>
          </div>
        </div>
        <div className="form-group">
          <div className="col-sm-offset-3 col-sm-9">
            <button className="btn btn-primary" name="isShowUpdate">
              Đặt sân
            </button>
          </div>
        </div>
      </form>
    );
    const renderFreeTime = (
      <div>
        <div className="col-lg-6">
          <h3 className="text-center">5 vs 5</h3>
          {freeTime5vs5.map(freeTime5vs5 => (
            <div>
              <h4>{freeTime5vs5.startTime}</h4> <h4>{freeTime5vs5.endTime}</h4>{' '}
            </div>
          ))}
        </div>
        <div className="col-lg-6">
          <h3 className="text-center">7 vs 7</h3>
          {freeTime7vs7.map(freeTime7vs7 => (
            <div>
              <h4>{freeTime7vs7.startTime}</h4> <h4>{freeTime7vs7.endTime}</h4>{' '}
            </div>
          ))}
        </div>
      </div>
    );
    const renderMatch = listMatch.map(listMatch => (
      <div key={listMatch.id} className="col-lg-4">
        <div className="panel panel-green">
          <div className="panel-heading">
            <div className="row">
              <div className="col-lg-6">Sân</div>
              <div className="col-lg-6 text-right">
                <i>{new Date(listMatch.date).toDateString()}</i>
              </div>
            </div>
          </div>
          <div className="panel-body">
            <div className="row">
              <div className="col-lg-4 text-center">
                <Link to="/player">
                  <img
                    src={require('../resource/images/ronaldo.jpg')}
                    alt="..."
                    className="img-circle"
                    width="80"
                    height="80"
                  />
                  <h4 />
                </Link>
              </div>
              <div className="col-lg-4 text-center">
                <h3>
                  {moment('10-10-2017 ' + listMatch.startTime).format('HH:mm')}
                </h3>
                <h4>
                  {moment('10-10-2017 ' + listMatch.endTime).hour() * 60 +
                    moment('10-10-2017 ' + listMatch.endTime).minute() -
                    (moment('10-10-2017 ' + listMatch.startTime).hour() * 60 +
                      moment(
                        '10-10-2017 ' + listMatch.startTime,
                      ).minute())}{' '}
                  phút
                </h4>
                <h4>{listMatch.fieldTypeId.name}</h4>
              </div>
              <div className="col-lg-4 text-center">
                <a href="#">
                  <img
                    src={require('../resource/images/ronaldo.jpg')}
                    alt="..."
                    className="img-circle"
                    width="80"
                    height="80"
                  />
                </a>
              </div>
            </div>
          </div>
          <a href="#">
            <div className="panel-footer">
              <span className="pull-left">Cập nhật sân</span>
              <span className="pull-right">
                <i className="fa fa-arrow-circle-right" />
              </span>
              <div className="clearfix" />
            </div>
          </a>
        </div>
      </div>
    ));

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
              <div className="col-lg-3">
                {' '}
                <button
                  className="btn btn-primary btn-block"
                  onClick={this.handleInputChange.bind(this)}
                  name="openedTab"
                  value="1"
                >
                  Các trận đang đá
                </button>{' '}
              </div>
              <div className="col-lg-3">
                {' '}
                <button
                  className="btn btn-primary btn-block"
                  onClick={this.handleInputChange.bind(this)}
                  name="openedTab"
                  value="2"
                >
                  Các trận trong ngày
                </button>{' '}
              </div>
              <div className="col-lg-3">
                {' '}
                <button
                  className="btn btn-primary btn-block"
                  onClick={this.handleInputChange.bind(this)}
                  name="openedTab"
                  value="3"
                >
                  Thời gian rảnh
                </button>{' '}
              </div>
              <div className="col-lg-3">
                {' '}
                <button
                  className="btn btn-primary btn-block"
                  onClick={this.handleInputChange.bind(this)}
                  name="openedTab"
                  value="4"
                >
                  Đặt sân
                </button>{' '}
              </div>
            </div>
            {/* <div className="btn-group btn-group-justified">
              <a className="btn btn-primary">Các trận đang đá</a>
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
                  ? listMatch.length > 0
                    ? renderMatch
                    : 'There is no match today'
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
  console.log('show state in match: ', state);
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
