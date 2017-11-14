import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetOvercome } from '../apis/field-owner-apis';
import { doLoginSuccessful, doLogout } from '../redux/guest/guest-action-creators';
import { Modal, Pagination } from 'react-bootstrap';
import moment from 'moment';
class OverCome extends Component {
  constructor(props) {
    super(props);
    this.state = {
      dateSelected: moment(),
      overcome: [],
      isShowModalField: false,
      detailMatch: undefined,
      itemSize: 0,
      activePage: 1,
      index: 1,
    };
    this.handleShowModal = this.handleShowModal.bind(this);
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
        const idLocal = authLocalStorage.id;
        await this.props.doLoginSuccessful(authLocalStorage);
        const dataOvercome = await fetchGetOvercome(idLocal);
        const page = Math.ceil(dataOvercome.body.length / 20);
        this.setState({ overcome: dataOvercome.body, itemSize: page });
      }
    } else {
      const dataOvercome = await fetchGetOvercome(id);
      const page = Math.ceil(dataOvercome.body.length / 20);
      this.setState({ overcome: dataOvercome.body, itemSize: page });
    }
  }
  async handleDateChange(date) {
    await this.setState({
      dateSelected: date,
    });
  }
  async handleShowModal(match) {
    console.log('match: ', match);
    const detail = (await !match.friendlyMatchId)
      ? match.tourMatchId
      : match.friendlyMatchId;
    await this.setState({ isShowModalField: true, detailMatch: detail });
  }
  handleHideModalField(evt) {
    evt.preventDefault();
    this.setState({ isShowModalField: false });
  }
  handleSelectPage(eventKey) {
    this.setState({ activePage: eventKey });
  }
  render() {
    const { overcome } = this.state;
    console.log(overcome);
    const renderOvercome =
      overcome.length > 0
        ? overcome
            .slice((this.state.activePage - 1) * 20, this.state.activePage * 20)
            .map((overcome, index) => (
              <tr key={index}>
                <td>{(this.state.activePage - 1) * 20 + index + 1}</td>
                <td>{overcome.userId.profileId.name}</td>
                <td>{overcome.price} nghìn đồng</td>
                <td>
                  {moment(overcome.dateCharge).format(
                    'DD [tháng] MM YYYY | HH:mm',
                  )}
                </td>
                <td>
                  <button
                    onClick={() => this.handleShowModal(overcome)}
                    className="btn btn-primary float-right"
                  >
                    Xem trận đấu
                  </button>
                </td>
              </tr>
            ))
        : null;
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Thu nhập</h2>
              </div>
              {/* <div className="col-sm-4">
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
              </div> */}
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
                        <th />
                      </tr>
                    </thead>
                    <tbody>{overcome.length > 0 ? renderOvercome : null}</tbody>
                  </table>
                </div>
              </div>
            </div>
            <div className="col-sm-12 text-center">
              <Pagination
                bsSize="medium"
                items={this.state.itemSize <= 1 ? 0 : this.state.itemSize}
                activePage={this.state.activePage}
                onSelect={this.handleSelectPage.bind(this)}
              />
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.isShowModalField}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Chi tiết trận đấu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {this.state.detailMatch !== undefined ? (
              <div>
                <h3 className="text-center text-primary">
                  <strong>
                    {moment(
                      '10-10-2017 ' +
                        this.state.detailMatch.timeSlotId.startTime,
                    ).format('HH:mm')}
                  </strong>
                </h3>
                <p className="text-center">
                  <strong>
                    {moment(
                      '10-10-2017 ' + this.state.detailMatch.timeSlotId.endTime,
                    ).hour() *
                      60 +
                      moment(
                        '10-10-2017 ' +
                          this.state.detailMatch.timeSlotId.endTime,
                      ).minute() -
                      (moment(
                        '10-10-2017 ' +
                          this.state.detailMatch.timeSlotId.startTime,
                      ).hour() *
                        60 +
                        moment(
                          '10-10-2017 ' +
                            this.state.detailMatch.timeSlotId.startTime,
                        ).minute())}{' '}
                    phút
                  </strong>
                </p>
                <p className="text-center">
                  <strong>
                    {moment(this.state.detailMatch.timeSlotId.date).format(
                      'DD/MM/YYYY',
                    )}
                  </strong>
                </p>
              </div>
            ) : null}
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.handleHideModalField.bind(this)}
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
function mapPropsToState(state) {
  return { auth: state.auth };
}
export default connect(mapPropsToState, { doLoginSuccessful, doLogout })(OverCome);
