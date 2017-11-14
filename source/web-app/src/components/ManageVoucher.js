import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal } from 'react-bootstrap';
import {
  doLoginSuccessful,
  accessDenied,
  doLogout,
} from '../redux/guest/guest-action-creators';
import { fetchGetAllVoucher, fetchAddNewVoucher } from '../apis/staff-api';
import { toast } from 'react-toastify';
import moment from 'moment';
class ManageFieldOwner extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showModalVoucher: false,
      listVoucher: [],
      message: undefined,
      bonusPointTarget: undefined,
      voucherValue: undefined,
    };
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
        if (authLocalStorage.roleId.roleName !== 'staff') {
          this.props.accessDenied();
          this.props.history.push('/login');
        } else {
          await this.props.doLoginSuccessful(authLocalStorage);
          const data = await fetchGetAllVoucher();
          this.setState({ listVoucher: data.body });
        }
      }
    }
  }
  handelHideModal(evt) {
    evt.preventDefault();
    this.setState({ showModalVoucher: false });
  }
  handelShowModal(evt) {
    evt.preventDefault();
    this.setState({ showModalVoucher: true });
  }
  async handeSubmitVoucher(evt) {
    evt.preventDefault();
    const { bonusPointTarget, voucherValue } = this.state;
    const res = await fetchAddNewVoucher(bonusPointTarget, voucherValue);
    if (res.status === 201) {
      toast.success('Thêm mới thành công');
    } else {
      toast.error('Thêm mới thất bại');
    }
    this.setState({ showModalVoucher: false });
  }
  handelInputChange(evt) {
    evt.preventDefault();
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    this.setState({ [name]: value });
  }
  render() {
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Quản lý voucher</h2>
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
                          voucher
                        </button>
                      </div>
                      <table className="table table-striped">
                        <thead>
                          <tr>
                            <th>#</th>
                            <th>Điểm thưởng yêu cầu</th>
                            <th>Giá trị</th>
                            <th>Ngày tạo</th>
                            <th>Trạng thái</th>
                            <th />
                          </tr>
                        </thead>
                        <tbody>
                          {this.state.listVoucher.length > 0
                            ? this.state.listVoucher.map((voucher, index) => (
                                <tr key={index}>
                                  <td>{index + 1}</td>
                                  <td>{voucher.bonusPointTarget}</td>
                                  <td>{voucher.voucherValue}</td>
                                  <td>
                                    {moment(voucher.modificationDate).format(
                                      'DD [tháng] MM, YYYY',
                                    )}
                                  </td>
                                  <td>
                                    {voucher.status ? (
                                      <label className="label label-success">
                                        Đang hoạt động
                                      </label>
                                    ) : (
                                      <label className="label label-danger">
                                        Hết ngày sử dụng
                                      </label>
                                    )}
                                  </td>
                                  <td />
                                  <td>
                                    <button className="btn btn-primary">
                                      Cập nhật
                                    </button>
                                  </td>
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
          show={this.state.showModalVoucher}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Thêm mới voucher</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form className="form-horizontal">
              <p className="text-danger text-center">
                {this.state.message ? this.state.message : null}
              </p>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-6 control-label">
                  Điểm đổi thưởng yêu cầu
                </label>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6">
                      {/* <div className="input-group"> */}
                      <input
                        type="text"
                        className="form-control"
                        id="inputPassword3"
                        name="bonusPointTarget"
                        value={this.state.bonusPointTarget}
                        onChange={this.handelInputChange.bind(this)}
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-6 control-label">
                  Giá trị đổi ra
                </label>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-9">
                      <div className="input-group">
                      <input
                        type="text"
                        className="form-control"
                        id="inputPassword3"
                        name="voucherValue"
                        value={this.state.voucherValue}
                        onChange={this.handelInputChange.bind(this)}
                      />
                       <span className="input-group-addon">nghìn đồng</span>
                       </div>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.handeSubmitVoucher.bind(this)}
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
export default connect(mapPropsToState, { doLoginSuccessful, doLogout, accessDenied })(
  ManageFieldOwner,
);
