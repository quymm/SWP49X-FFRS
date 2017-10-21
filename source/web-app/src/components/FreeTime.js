import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  doLoginSuccessful,
  accessDenied,
} from '../redux/guest/guest-action-creators';
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
import moment from 'moment';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

class FreeTime extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
    };
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

  render() {
    const myStyle = { padding: 20 };
    const { freeTime5vs5, freeTime7vs7 } = this.props;
    const { dateSelected } = this.state;
    return (
      <div id="page-wrapper">
        <div className="container-fluid">
          <div className="row">
            <div className="col-lg-9">
              <h2 className="page-header">Thời gian trống</h2>
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
          </div>
          
          <div className="col-lg-12">
            <div className="row" style={myStyle}>
              <div className="col-lg-6">
                <div className="panel panel-green">
                  <div className="panel-heading">
                    {' '}
                    <h4 className="text-center">Loại sân 5 người</h4>
                  </div>
                  <div className="panel-body">
                    {freeTime5vs5.length > 0
                      ? freeTime5vs5.map(freeTime => (
                          <div key={freeTime.id}>
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
                        ))
                      : 'Không có thời gian rảnh'}
                  </div>
                </div>
              </div>
              <div className="col-lg-6">
                <div className="panel panel-green">
                <div className="panel-heading">
                    {' '}
                    <h4 className="text-center">Loại sân 7 người</h4>
                  </div>
                  <div className="panel-body">
                    {freeTime7vs7.length > 0
                      ? freeTime7vs7.map(freeTime => (
                          <div key={freeTime.id}>
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
                        ))
                      : <h4>Không có thời gian rảnh</h4>}
                  </div>
                </div>
              </div>
            </div>
          </div>
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
