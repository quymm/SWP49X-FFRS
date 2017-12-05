import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  doLoginSuccessful,
  accessDenied,
  doLogout,
} from '../redux/guest/guest-action-creators';
import {
  fetchGetStandarPrice,
  fetchUpdateStandardPrice,
} from '../apis/staff-api';
import moment from 'moment';
import { Modal } from 'react-bootstrap';
import { toast } from 'react-toastify';
class ManagePrice extends Component {
  constructor(props) {
    super(props);
    this.state = {
      standardRush: [],
      standardWithoutRush: [],
      isShowUpdatePrice: false,
      maxPrice: undefined,
      minPrice: undefined,
      priceSelected: undefined,
      message: undefined,
    };
    this.handleShowUpdateModal = this.handleShowUpdateModal.bind(this);
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
          const resStandardPriceRush = await fetchGetStandarPrice(true);
          if ((resStandardPriceRush.status = 200)) {
            this.setState({ standardRush: resStandardPriceRush.body });
          }
          const resStandardWithoutRush = await fetchGetStandarPrice(false);
          if (resStandardWithoutRush.status === 200) {
            this.setState({ standardWithoutRush: resStandardWithoutRush.body });
          }
        }
      }
    } else {
    }
  }

  handleHideUpdateModal() {
    this.setState({
      isShowUpdatePrice: false,
      maxPrice: undefined,
      minPrice: undefined,
    });
  }

  handleShowUpdateModal(standPrice) {
    this.setState({
      isShowUpdatePrice: true,
      priceSelected: standPrice,
      maxPrice: standPrice.maxPrice,
      minPrice: standPrice.minPrice,
    });
  }

  async updatePriceSubmit() {
    const { maxPrice, minPrice, priceSelected } = this.state;
    const { id } = this.props.auth.user.data;
    const priceRegex = '^\\d+$';
    if (
      (this.state.maxPrice === undefined || this.state.maxPrice === '',
      this.state.minPrice === undefined ||
        this.state.minPrice === '' ||
        this.state.minPrice.length < 1 ||
        this.state.maxPrice.length < 1)
    ) {
      this.setState({ message: 'Vui lòng điền đầu đủ các trường' });

    } else {
      if (this.state.maxPrice < this.state.minPrice) {
        this.setState({message: 'Giá tối đa phải cao hơn giá tối thiểu'});
      }else{
        if (String(maxPrice).match(priceRegex) && String(minPrice).match(priceRegex)) {
          const resUpdate = await fetchUpdateStandardPrice(
            priceSelected.id,
            maxPrice,
            minPrice,
            id,
          );
          if (resUpdate.status === 201) {
            toast.success('Cập nhật thành công');
          }
          else{
            toast.error('Cập nhật thất bại');
          }
          const resStandardPriceRush = await fetchGetStandarPrice(true);
          if ((resStandardPriceRush.status = 200)) {
            this.setState({ standardRush: resStandardPriceRush.body });
          }
          const resStandardWithoutRush = await fetchGetStandarPrice(false);
          if (resStandardWithoutRush.status === 200) {
            this.setState({ standardWithoutRush: resStandardWithoutRush.body });
          }
          this.setState({ isShowUpdatePrice: false });
        }else{
          this.setState({message: 'Giá phải là kiểu số'});
        }
      
    }
    }
  }

  handleInputChange(evt) {
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    this.setState({ [name]: value });
  }

  render() {
    console.log(this.state);
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Quản lý giá</h2>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body">
                    <div className="col-sm-12">
                      <div className="table-responsive">
                        <div className="panel panel-heading">
                          <h4>Giờ thấp điểm</h4>
                        </div>
                        <table className="table table-striped">
                          <thead>
                            <tr>
                              <th>#</th>
                              <th>Loại sân</th>
                              <th>Giá tối đa</th>
                              <th>Giá tối thiếu</th>
                              <th>Ngày cập nhật</th>
                              <th />
                            </tr>
                          </thead>
                          <tbody>
                            {this.state.standardWithoutRush.length > 0
                              ? this.state.standardWithoutRush.map(
                                  (standard, index) => (
                                    <tr key={index + 1}>
                                      <td>{index + 1}</td>
                                      <td>{standard.fieldTypeId.name} </td>
                                      <td>{standard.maxPrice} nghìn</td>
                                      <td>{standard.minPrice} nghìn</td>
                                      <td>
                                        {moment(
                                          standard.modificationDate,
                                        ).format('DD [tháng] MM, YYYY')}
                                      </td>
                                      <td>
                                        <button
                                          onClick={() =>
                                            this.handleShowUpdateModal(standard)
                                          }
                                          className="btn btn-info"
                                        >
                                          Cập nhật
                                        </button>
                                      </td>
                                    </tr>
                                  ),
                                )
                              : null}
                          </tbody>
                        </table>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body">
                    <div className="col-sm-12">
                      <div className="table-responsive">
                        <div className="panel panel-heading">
                          <h4>Giờ cao điểm</h4>
                        </div>
                        <table className="table table-striped">
                          <thead>
                            <tr>
                              <th>#</th>
                              <th>Loại sân</th>
                              <th>Giá tối đa</th>
                              <th>Giá tối thiếu</th>
                              <th>Ngày cập nhật</th>
                              <th />
                            </tr>
                          </thead>
                          <tbody>
                            {this.state.standardRush.length > 0
                              ? this.state.standardRush.map(
                                  (standard, index) => (
                                    <tr key={index + 1}>
                                      <td>{index + 1}</td>
                                      <td>{standard.fieldTypeId.name} </td>
                                      <td>{standard.maxPrice} nghìn</td>
                                      <td>{standard.minPrice} nghìn</td>
                                      <td>
                                        {moment(
                                          standard.modificationDate,
                                        ).format('DD [tháng] MM, YYYY')}
                                      </td>
                                      <td>
                                        <button
                                          onClick={() =>
                                            this.handleShowUpdateModal(standard)
                                          }
                                          className="btn btn-info"
                                        >
                                          Cập nhật
                                        </button>
                                      </td>
                                    </tr>
                                  ),
                                )
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
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.isShowUpdatePrice}
          onHide={this.handleHideUpdateModal.bind(this)}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>
              Cập nhật giá cho sân{' '}
              {this.state.priceSelected
                ? `${this.state.priceSelected.fieldTypeId.name} giờ ${
                    this.state.priceSelected.rushHour ? 'cao điểm' : 'thấp điểm'
                  }`
                : null}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <form className="form-horizontal">
              <p className="text-center text-danger">
                {this.state.message ? this.state.message : null}
              </p>
              <div>
                <div className="form-group">
                  <label
                    htmlFor="inputEmail3"
                    className="col-sm-3 control-label"
                  >
                    Giá tối đa
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          value={this.state.maxPrice}
                          onChange={this.handleInputChange.bind(this)}
                          className="form-control"
                          name="maxPrice"
                          type="text"
                          id="inputEmail3"
                        />
                      </div>
                    </div>
                  </div>
                </div>
                <div className="form-group">
                  <label
                    htmlFor="inputEmail3"
                    className="col-sm-3 control-label"
                  >
                    Giá tối thiểu
                  </label>
                  <div className="col-sm-9">
                    <div className="row">
                      <div className="col-sm-6">
                        <input
                          value={this.state.minPrice}
                          onChange={this.handleInputChange.bind(this)}
                          className="form-control"
                          name="minPrice"
                          type="text"
                          id="inputEmail3"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.updatePriceSubmit.bind(this)}
              className="btn btn-primary"
            >
              Cập nhật
            </button>
            <button
              onClick={this.handleHideUpdateModal.bind(this)}
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
export default connect(mapPropsToState, {
  doLoginSuccessful,
  accessDenied,
  doLogout,
})(ManagePrice);
