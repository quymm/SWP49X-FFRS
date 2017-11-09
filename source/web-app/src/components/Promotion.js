import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  fetchGetAllPromotion,
  fetchAddPromotion,
} from '../apis/field-owner-apis';
import { doLoginSuccessful } from '../redux/guest/guest-action-creators';
import { Modal } from 'react-bootstrap';
import moment from 'moment';
import DatePicker from 'react-datepicker';
import TimePicker from 'rc-time-picker';
import { toast } from 'react-toastify';
class Promotion extends Component {
  constructor(props) {
    super(props);
    this.state = {
      promotion: [],
      showModalPromotion: false,
      startDate: moment(),
      endDate: moment().add(30, 'days'),
      saleOff: undefined,
      startTime: moment('10-10-2017 06:00:00'),
      endTime: moment('10-10-2017 22:00:00'),
      decription: undefined,
      fieldStyle: 1,
      message: undefined,
    };
  }
  handelShowModal(evt) {
    evt.preventDefault();
    this.setState({ showModalPromotion: true });
  }
  handelHideModal(evt) {
    evt.preventDefault();
    this.setState({ showModalPromotion: false });
  }

  async handleStartTimeChange(evt) {
    await this.setState({ startTime: evt });
    console.log(this.state);
  }
  handleEndTimeChange(evt) {
    this.setState({ endTime: evt });
  }
  handleStartDateChange(evt) {
    this.setState({ startDate: evt });
  }
  handleEndDateChange(evt) {
    this.setState({ endDate: evt });
  }
  async handelInputChange(evt) {
    evt.preventDefault();
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    await this.setState({ [name]: value });
    console.log(this.state);
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      const idLocal = authLocalStorage.id;
      await this.props.doLoginSuccessful(authLocalStorage);
      const dataPromotion = await fetchGetAllPromotion(idLocal);
      this.setState({ promotion: dataPromotion.body });
    } else {
      const dataPromotion = await fetchGetAllPromotion(id);
      this.setState({ promotion: dataPromotion.body });
    }
    this.setState({ showModalPromotion: false });
  }
  async handeSubmitPromotion(evt) {
    evt.preventDefault();
    const {
      decription,
      endDate,
      endTime,
      fieldStyle,
      message,
      promotion,
      saleOff,
      startDate,
      startTime,
    } = this.state;
    const { id } = this.props.auth.user.data;
    const dataAdd = await fetchAddPromotion(id, this.state);
    const dataPromotion = await fetchGetAllPromotion(id);
    this.setState({ promotion: dataPromotion.body, showModalPromotion: false });
    if (dataAdd.status === 201) {
      toast.success('Thêm mới Khuyến mãi thành công');
    } else {
      toast.error('Thêm mới Khuyến mãi thất bại');
    }
  }
  render() {
    console.log(this.state.promotion);
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Khuyến mãi</h2>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  
                  <div className="panel panel-body">
                    <div className="table-responsive">
                      <div className="panel panel-heading">
                        <button
                          className="btn btn-warning"
                          name="isShowUpdate"
                          onClick={this.handelShowModal.bind(this)}
                        >
                          <i className="glyphicon glyphicon-plus" /> Thêm mới
                          Khuyến mãi
                        </button>
                      </div>
                      <table className="table table-striped">
                        <thead>
                          <tr>
                            <th>#</th>
                            <th>Từ ngày</th>
                            <th>Tới ngày</th>
                            <th>Khung giờ</th>
                            <th>Khuyến mãi</th>
                            <th>Giảm giá</th>
                            <th>Loại sân</th>
                            <th />
                          </tr>
                        </thead>
                        <tbody>
                          {this.state.promotion.length > 0
                            ? this.state.promotion.map((promotion, index) => (
                                <tr key={index}>
                                  <td>{index + 1}</td>
                                  <td>
                                    {moment(promotion.dateFrom).format(
                                      'DD [tháng] MM, YYYY',
                                    )}
                                  </td>
                                  <td>
                                    {moment(promotion.dateTo).format(
                                      'DD [tháng] MM, YYYY',
                                    )}
                                  </td>
                                  <td>
                                    {moment(
                                      '10-10-2017 ' + promotion.startTime,
                                    ).format('HH:mm') +
                                      ' - ' +
                                      moment(
                                        '10-10-2017 ' + promotion.endTime,
                                      ).format('HH:mm')}
                                  </td>
                                  <td>
                                    <textarea
                                      readOnly
                                      cols="20"
                                      rows="5"
                                      className="form-control"
                                    >
                                      {promotion.freeServices}
                                    </textarea>
                                  </td>
                                  <td>{promotion.saleOff} %</td>
                                  <td>{promotion.fieldTypeId.name}</td>
                                </tr>
                              ))
                            : null}
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.showModalPromotion}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Thêm mới khuyễn mãi</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form className="form-horizontal">
              <p className="text-danger text-center">
                {this.state.message ? this.state.message : null}
              </p>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Từ
                </label>
                <div className="col-sm-9">
                  <div className="row">
                    <div className="col-sm-6">
                      <DatePicker
                        selected={this.state.startDate}
                        onChange={this.handleStartDateChange.bind(this)}
                        className="form-control"
                        todayButton={'Hôm nay'}
                        name="startDate"
                      />
                    </div>
                    <div className="col-sm-6">
                      <TimePicker
                        showSecond={false}
                        name="startTime"
                        defaultValue={moment('10-10-2017 06:00:00')}
                        onChange={this.handleStartTimeChange.bind(this)}
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Đến
                </label>
                <div className="col-sm-9">
                  <div className="row">
                    <div className="col-sm-6">
                      <DatePicker
                        selected={this.state.endDate}
                        onChange={this.handleEndDateChange.bind(this)}
                        className="form-control"
                        name="endDate"
                      />
                    </div>
                    <div className="col-sm-6">
                      <TimePicker
                        showSecond={false}
                        name="endTime"
                        defaultValue={moment('10-10-2017 22:00:00')}
                        onChange={this.handleEndTimeChange.bind(this)}
                      />
                    </div>
                  </div>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Khuyễn mãi
                </label>
                <div className="col-sm-9">
                  <div className="row">
                    <div className="col-sm-12">
                      <textarea
                        className="form-control"
                        rows="5"
                        id="inputEmail3"
                        value={this.state.decription}
                        name="decription"
                        onChange={this.handelInputChange.bind(this)}
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Giảm giá (%)
                </label>
                <div className="col-sm-9">
                  <div className="row">
                    <div className="col-sm-3">
                      {/* <div className="input-group"> */}
                      <input
                        type="text"
                        className="form-control"
                        id="inputPassword3"
                        name="saleOff"
                        value={this.state.saleOff}
                        onChange={this.handelInputChange.bind(this)}
                      />
                      {/* <span className="input-group-addon">%</span> */}
                      {/* </div> */}
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="sel1" className="col-sm-3 control-label">
                  Loại sân
                </label>
                <div className="col-sm-3">
                  <select
                    value={this.state.fieldStyle}
                    onChange={this.handelInputChange.bind(this)}
                    className="form-control"
                    name="fieldStyle"
                    id="sel1"
                  >
                    <option value="1">5 vs 5</option>
                    <option value="2">7 vs 7</option>
                  </select>
                </div>
              </div>
            </form>
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.handeSubmitPromotion.bind(this)}
              className="btn btn-primary"
            >
              Thêm mới
            </button>
            <button
              onClick={this.handelHideModal.bind(this)}
              className="btn btn-danger"
            >
              Huỷ
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
export default connect(mapPropsToState, { doLoginSuccessful })(Promotion);
