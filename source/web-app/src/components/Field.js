import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator';
import FormCreateField from '../containts/Form-Create-Field';
import { withRouter } from 'react-router-dom';
import {
  accessDenied,
  doLoginSuccessful,
  doLogout,
} from '../redux/guest/guest-action-creators';
import { Modal } from 'react-bootstrap';
import moment from 'moment';
import DatePicker from 'react-datepicker';
class Field extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isCreateShowed: true,
      isShowConfirmDelete: false,
      disableDateTo: moment().add(7, 'days'),
      disableDateFrom: moment(),
      fieldIdDelete: undefined,
      currentDate: moment(),
    };
  }

  async componentDidMount() {
    const { id, roleId } = this.props.auth.user.data;

    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      if (authLocalStorage === null) {
        this.props.doLogout();
        this.props.history.push('/login');
      } else {
        if (authLocalStorage.roleId.roleName !== 'owner') {
          await this.props.accessDenied();
          this.props.history.push('/login');
        } else {
          const idLocal = authLocalStorage.id;
          await this.props.doLoginSuccessful(authLocalStorage);

          const data = await fetchGetAllField(idLocal);

          await this.props.getAllField(data.body);
        }
      }
    } else {
      if (roleId.roleName !== 'owner') {
        this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        const data = await fetchGetAllField(id);
        await this.props.getAllField(data.body);
      }
    }
  }

  async deleteField(evt) {
    debugger;
    const fieldId = this.state.fieldIdDelete;
    const { id } = this.props.auth.user.data;
    await fetchDeleteField(
      fieldId,
      this.state.disableDateFrom.format('DD-MM-YYYY'),
      this.state.disableDateTo.format('DD-MM-YYYY'),
    );
    const data = await fetchGetAllField(id);
    await this.props.getAllField(data.body);
    this.setState({ isShowConfirmDelete: false });
  }
  handleShowComfirmDeleteField(evt) {
    // evt.preventDefault();
    debugger;
    this.setState({ isShowConfirmDelete: true, fieldIdDelete: evt.id });
  }
  handleHideComfirmDeleteField(evt) {
    // evt.preventDefault();
    this.setState({ isShowConfirmDelete: false });
  }
  async handelTimeStartDayInputChange(evt) {
    await this.setState({ disableDateFrom: evt });
  }
  async handelEndTimeInputChange(evt) {
    await this.setState({ disableDateTo: evt });
  }
  render() {
    const { listField } = this.props;
    const renderField =
      listField.length > 0
        ? listField
            .map((listField, index) => {
              return (
                <tr key={listField.id}>
                  <td>{index + 1}</td>
                  <td>{listField.name}</td>
                  <td>{listField.fieldTypeId.name}</td>
                  <td>
                    <span className="label label-success">Đang hoạt động</span>
                  </td>
                  {listField.dateTo ? null : (
                    <td>
                      <button
                        value={listField.id}
                        onClick={() =>
                          this.handleShowComfirmDeleteField(listField)
                        }
                        className="btn btn-danger"
                      >
                        Ngưng hoạt động
                      </button>
                    </td>
                  )}
                </tr>
              );
            })
        : 'Hiện tại chưa có sân';
    const { isCreateShowed } = this.state;
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-md-4">
                <h2 className="page-header">Quản lý sân</h2>
              </div>
            </div>
            {isCreateShowed ? <FormCreateField /> : null}
            <div className="col-md-8 col-md-offset-2">
              <div className="panel panel-dafault">
                <div className="panel-body">
                  <div className="table-responsive">
                    <table className="table table-striped">
                      <thead>
                        <tr>
                          <th>#</th>
                          <th>Tên sân</th>
                          <th>Loại sân</th>
                          <th>Trang thái</th>
                          <th />
                        </tr>
                      </thead>
                      <tbody>
                        {listField == null ? (
                          <tr>
                            <td>Chưa có sân</td>
                          </tr>
                        ) : (
                          renderField
                        )}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.isShowConfirmDelete}
          onHide={this.handleHideComfirmDeleteField.bind(this)}
          dialogClassName="custom-modal"
        >
          <Modal.Header>
            <Modal.Title>Ngưng hoạt động sân</Modal.Title>
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
                  <div className="col-sm-6">
                    <DatePicker
                      selected={this.state.disableDateFrom}
                      onChange={this.handelTimeStartDayInputChange.bind(this)}
                      className="form-control"
                      name="endDate"
                    />
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="inputEmail3" className="col-sm-3 control-label">
                  Đến
                </label>
                <div className="col-sm-9">
                  <div className="col-sm-6">
                    <DatePicker
                      selected={this.state.disableDateTo}
                      onChange={this.handelEndTimeInputChange.bind(this)}
                      className="form-control"
                      name="endDate"
                    />
                  </div>
                </div>
              </div>
            </form>
          </Modal.Body>
          <Modal.Footer>
            <button
              onClick={this.deleteField.bind(this)}
              className="btn btn-primary"
            >
              Xác nhận
            </button>
            <button
              onClick={this.handleHideComfirmDeleteField.bind(this)}
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
function mapStateToProps(state) {
  console.log('state in field: ', state);
  return {
    listField: state.listField.listField,
    auth: state.auth,
  };
}

export default withRouter(
  connect(mapStateToProps, {
    getAllField,
    accessDenied,
    doLoginSuccessful,
    doLogout,
  })(Field),
);
