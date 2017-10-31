import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetOvercome } from '../apis/field-owner-apis';
import { doLoginSuccessful } from '../redux/guest/guest-action-creators';
import moment from 'moment';
import DatePicker from 'react-datepicker';
class OverCome extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      overcome: undefined,
    };
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      const idLocal = authLocalStorage.id;
      await this.props.doLoginSuccessful(authLocalStorage);
      const dataOvercome = await fetchGetOvercome(idLocal);
      this.setState({ overcome: dataOvercome.body });
    } else {
      const dataOvercome = await fetchGetOvercome(id);
      this.setState({ overcome: dataOvercome.body });
    }
  }
  async handleDateChange(date) {
    const { id } = this.props.auth.user.data;
    await this.setState({
      dateSelected: date,
    });
  }
  render() {
    const { overcome } = this.state;
    console.log(overcome);
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Thu nhập</h2>
              </div>
              <div className="col-sm-4">
                <div className="page-header">
                  <h4>
                    {this.state.dateSelected.format('dddd, Do MMMM YYYY')}
                  </h4>
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
                        todayButton={'Today'}
                      />
                    </div>
                  </form>
                </div>
              </div>
            </div>

            <div className="col-sm-12">
              <div className="panel panel-default">
                <div className="table-responsive">
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Người trả</th>
                        <th>Số tiền</th>
                        <th>Ngày giờ</th>
                      </tr>
                    </thead>
                    <tbody>{}</tbody>
                  </table>
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
  return { auth: state.auth };
}
export default connect(mapPropsToState, { doLoginSuccessful })(OverCome);
