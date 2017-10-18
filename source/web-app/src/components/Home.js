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
import {
  doLoginSuccessful,
  accessDenied,
} from '../redux/guest/guest-action-creators';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      currentTime: new Date().toLocaleTimeString([], { hour12: false }),
      currentShowPage: '',
      openedTab: 2,
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
    debugger;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const idLocal = authLocalStorage.id;
        await this.props.doLoginSuccessful(authLocalStorage);
        try {
          fetchGetMatchByFieldOwnerAndDay(
            idLocal,
            this.state.dateSelected.format('DD-MM-YYYY'),
            1,
          ).then(data => {
            this.props.GetMatchByFieldOwnerAndDay(data);
          });
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
          await this.props.getAllFreeTime5vs5(data5vs5);
          await this.props.getAllFreeTime7vs7(data7vs7);
        } catch (error) {
          console.log('error: ', error);
        }
      }
    } else {
      try {
        fetchGetMatchByFieldOwnerAndDay(
          id,
          this.state.dateSelected.format('DD-MM-YYYY'),
          1,
        ).then(data => {
          this.props.GetMatchByFieldOwnerAndDay(data);
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
        await this.props.getAllFreeTime5vs5(data5vs5);
        await this.props.getAllFreeTime7vs7(data7vs7);
      } catch (error) {
        console.log('error: ', error);
      }
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
      const bookMatchRes = await fetchBookMatch(
        dateSelected.format('DD-MM-YYYY'),
        bookMatchEndTime,
        1,
        bookMatchFieldType,
        bookMatchStartTime,
        bookMatchEndTime,
      );
      if (bookMatchRes.status === 200) {
        this.setState({ openedTab: 2 });
        debugger;
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
    fetchGetMatchByFieldOwnerAndDay(
      id,
      this.state.dateSelected.format('DD-MM-YYYY'),
      1,
    ).then(data => {
      this.props.GetMatchByFieldOwnerAndDay(data);
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
    const { openedTab, buttonGroupTab, bookMatchMessage } = this.state;
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
              <p className="text-center text-danger">
                {bookMatchMessage ? bookMatchMessage : null}
              </p>
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
      <FreeTime
        freeTime5vs5={freeTime5vs5.body}
        freeTime7vs7={freeTime7vs7.body}
      />
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
                    className={`${tab.value == this.state.openedTab
                      ? 'btn btn-lg btn-primary btn-block'
                      : 'btn btn-lg btn-default btn-block'}`}
                    onClick={this.handleInputChange.bind(this)}
                    name="openedTab"
                    value={tab.value}
                  >
                    {tab.text}
                  </button>
                </div>
              ))}
            </div>
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
  doLoginSuccessful,
  accessDenied,
})(Home);
