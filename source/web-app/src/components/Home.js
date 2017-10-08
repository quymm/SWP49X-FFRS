import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetMatchByFieldOwnerAndDay } from '../apis/field-owner-apis';
import { GetMatchByFieldOwnerAndDay } from '../redux/field-owner/field-owner-action-creator';
import DatePicker from 'react-datepicker';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
import { Link } from 'react-router-dom';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
    };
  }
  componentDidMount() {
    // const date = dateSelected.format('LL');
    // console.log(date);
    console.log('didmount: ', this.state.dateSelected.format('MMM DD YYYY'));
    fetchGetMatchByFieldOwnerAndDay(
      1,
      this.state.dateSelected.format('MMM DD YYYY'),
    ).then(data => {
      this.props.GetMatchByFieldOwnerAndDay(data);
    });
    // debugger
  }
  async handleDateChange(date) {
    // console.log("date:", date.format('MMM DD YYYY'));
    await this.setState({
      dateSelected: date,
    });
    // console.log();
    fetchGetMatchByFieldOwnerAndDay(
      1,
      this.state.dateSelected.format('MMM DD YYYY'),
    ).then(data => {
      this.props.GetMatchByFieldOwnerAndDay(data);
    });
  }

  render() {
    const { listMatch } = this.props;
    console.log(listMatch);
    const renderMatch = listMatch.map(listMatch => (
      <div key={listMatch.timeSlotId.id} className="col-lg-4">
        <div className="panel panel-green">
          <div className="panel-heading">
            <div className="row">
              <div className="col-lg-6">
                FIELD {listMatch.timeSlotId.fieldId.name}
              </div>
              <div className="col-lg-6 text-right">
                <i>{new Date(listMatch.timeSlotId.date).toDateString()}</i>
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
                  <h4>{listMatch.userId.username}</h4>
                </Link>
              </div>
              <div className="col-lg-4 text-center">
                <h3>
                  {new Date(listMatch.timeSlotId.startTime).getHours()} :{' '}
                  {new Date(listMatch.timeSlotId.startTime).getMinutes() ===
                  0 ? (
                    '00'
                  ) : (
                    new Date(listMatch.timeSlotId.startTime).getMinutes()
                  )}
                </h3>
                <h4>
                  {(new Date(listMatch.timeSlotId.endTime).getHours() -
                    new Date(listMatch.timeSlotId.startTime).getHours()) *
                    60 +
                    (new Date(listMatch.timeSlotId.endTime).getMinutes() -
                      new Date(listMatch.timeSlotId.startTime).getMinutes()) +
                    'min'}
                </h4>
                <h4>{listMatch.timeSlotId.fieldId.fieldTypeId.name}</h4>
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
                  <h4>{listMatch.opponentId.username}</h4>
                </a>
              </div>
            </div>
          </div>
          <a href="#">
            <div className="panel-footer">
              <span className="pull-left">Feedback</span>
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
            <div className="col-lg-4">
              <h2 className="page-header">Home</h2>
            </div>
            <div className="col-lg-4">
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
                  <button className="btn btn-default" type="button">
                    <i className="glyphicon glyphicon-calendar" />
                  </button>
                </form>
              </div>
            </div>

            <div className="col-lg-4">
              <div className="page-header">
                <form className="navbar-form navbar-left">
                  <div className="form-group">
                    <input
                      type="text"
                      className="form-control"
                      placeholder="Search"
                    />
                  </div>
                  <button className="btn btn-default" type="button">
                    <i className="glyphicon glyphicon-search" />
                  </button>
                </form>
              </div>
            </div>
          </div>
          <div className="row">
            {listMatch.length > 0 ? renderMatch : 'There is no match today'}
          </div>
        </div>
      </div>
    );
  }
}
function mapStateToProps(state) {
  console.log("show state in match: ", state);
  return {
    listMatch: state.listMatch.listMatch,
  };
}

export default connect(mapStateToProps, { GetMatchByFieldOwnerAndDay })(Home);
