import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetMatchByFieldOwnerAndDay } from '../apis/field-owner-apis';
import { GetMatchByFieldOwnerAndDay } from '../redux/field-owner/field-owner-action-creator';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
import { Link } from 'react-router-dom';
import Clock from './Clock';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      currentTime: new Date().toLocaleTimeString(),
      currentShowPage: '',
    };
    this.timer = setInterval(() => {
      this.setState({ currentTime: new Date().toLocaleTimeString() });
    }, 1000);
  }
  componentWillUnmount() {
    clearInterval(this.timer);
  }
  componentDidMount() {
    console.log('didmount: ', this.state.dateSelected.format('MMM DD YYYY'));
    fetchGetMatchByFieldOwnerAndDay(
      1,
      this.state.dateSelected.format('DD-MM-YYYY'),
      1,
    ).then(data => {
      this.props.GetMatchByFieldOwnerAndDay(data);
    });
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
  }

  render() {
    const { listMatch } = this.props;
    console.log(listMatch);
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
                <h3>{moment('10-10-2017 ' + listMatch.startTime).format('HH:mm')}</h3>
                <h4>
                  {moment('10-10-2017 ' + listMatch.endTime).hour() * 60 +
                    moment('10-10-2017 ' + listMatch.endTime).minute() -
                    (moment('10-10-2017 ' + listMatch.startTime).hour() * 60 +
                      moment('10-10-2017 ' + listMatch.startTime).minute())} phút
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
            {/* <div className="row">
              <div className="col-lg-3"> <button className="btn btn-primary btn-block">Các trận đang đá</button> </div>
              <div className="col-lg-3"> <button className="btn btn-default btn-block">Các sắp diễn ra</button> </div>
              <div className="col-lg-3"> <button className="btn btn-default btn-block">Thời gian rảnh</button> </div>
              <div className="col-lg-3"> <button className="btn btn-default btn-block">Đặt sân</button> </div>
            </div> */}
            <div className="btn-group btn-group-justified">
              <a className="btn btn-primary">Các trận đang đá</a>
              <a className="btn btn-primary active">Các trận trong ngày</a>
              <a className="btn btn-primary">Thời gian rảnh</a>
              <a className="btn btn-primary">Đặt sân</a>
            </div>
          </div>
          <div className="col-lg-12">
            <div className="row">
              {listMatch.length > 0 ? renderMatch : 'There is no match today'}
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
  };
}

export default connect(mapStateToProps, { GetMatchByFieldOwnerAndDay })(Home);
