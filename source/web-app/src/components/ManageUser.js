import React, { Component } from 'react';
import {
  doLoginSuccessful,
  accessDenied,
  doLogout,
} from '../redux/guest/guest-action-creators';
import { connect } from 'react-redux';
import Autosuggest from 'react-autosuggest';
import { Modal, Pagination } from 'react-bootstrap';
import {
  fetchGetUserOrFieldOwnerSuggestion,
  fetchGetAllReportFieldOwner,
  fetchGetAllReportUser,
  fetchGetListReport,
  fetchRequestLockAccount,
} from '../apis/staff-api';
import moment from 'moment';
import { toast } from 'react-toastify';

const getSuggestionValue = suggestion => suggestion.profileId.name;
const renderSuggestion = suggestion => <div>{suggestion.profileId.name}</div>;
class ManageUser extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: '',
      suggestions: [],
      showModelUser: false,
      result: undefined,
      isSearch: undefined,
      listReported: [],
      userTarget: 'user',
      listReportWithTargetUser: [],
      pageActive: 1,
    };
    this.handelShowModalUser = this.handelShowModalUser.bind(this);
  }
  async handeSelectUserTarget(evt) {
    await this.setState({ userTarget: evt.target.value, pageActive: 1 });
  }
  onChange = async (event, { newValue }) => {
    await this.setState({
      value: newValue,
    });
  };
  getSuggestionValue = (event, { suggestion }) => {
    this.setState({ result: suggestion });
  };

  onSuggestionsFetchRequested = async ({ value }) => {
    if (value.length > 0) {
      const dataUser = await fetchGetUserOrFieldOwnerSuggestion(
        value,
        this.state.userTarget,
      );
      this.setState({
        suggestions: dataUser.body,
      });
    }
  };

  // Autosuggest will call this function every time you need to clear suggestions.
  onSuggestionsClearRequested = async () => {
    console.log(this.state.value);
    if (
      this.state.value === '' ||
      this.state.value === null ||
      this.state.value.length < 2 ||
      this.state.value === undefined
    ) {
      await this.setState({ result: undefined });
    }
    this.setState({
      suggestions: [],
    });
  };
  async handelShowModalUser(evt) {
    const data = await fetchGetAllReportUser(evt.id);
    this.setState({ listReportWithTargetUser: data.body });
    this.setState({ showModelUser: true, isSearch: evt });
  }

  handelHideModalUser(evt) {
    evt.preventDefault();
    this.setState({ showModelUser: false });
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
          const data = await fetchGetListReport();
          this.setState({ listReported: data.body });
        }
      }
    } else {
      const data = await fetchGetListReport();
      this.setState({ listReported: data.body });
    }
  }
  async handelLockAccount(evt) {
    evt.preventDefault();
    const { id } = this.props.auth.user.data;
    const resLock = await fetchRequestLockAccount(this.state.result.id, id);
    if (resLock.status === 200) {
      toast.success('Gửi yêu cầu thành công!');
    } else {
      toast.error('Gửi yêu cầu thất bại');
    }
    const data = await fetchGetListReport();
    this.setState({ listReported: data.body, showModelUser: false });
  }

  handleSelectPage(eventKey) {
    this.setState({ pageActive: eventKey });
  }

  render() {
    const { value, suggestions, result, listReported } = this.state;
    console.log(this.state.listReportWithTargetUser);
    // Autosuggest will pass through all these props to the input.
    const inputProps = {
      placeholder: 'Tìm kiếm theo tên',
      value,
      onChange: this.onChange,
    };
    const imgStyle = {
      width: 200,
      height: 200,
    };
    console.log(listReported);
    let afterFilterUser = [];
    let pageSize = 1;
    if (this.state.userTarget === 'user') {
      afterFilterUser = listReported.filter(
        reported => reported.roleId.roleName === 'user',
      );
      pageSize = Math.ceil(afterFilterUser.length / 10);
    } else {
      afterFilterUser = listReported.filter(
        reported => reported.roleId.roleName === 'owner',
      );
      pageSize = Math.ceil(afterFilterUser.length / 10);
    }
    console.log(listReported);

    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-6">
                <h2 className="page-header">Quản lý người dùng</h2>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body">
                    <div className="col-sm-12">
                      <div className="col-sm-2">
                        <p className="search-user">
                          <strong>Tìm kiếm</strong>
                        </p>
                      </div>
                      <div className="col-sm-4 text-center">
                        <Autosuggest
                          suggestions={suggestions}
                          onSuggestionsFetchRequested={
                            this.onSuggestionsFetchRequested
                          }
                          onSuggestionsClearRequested={
                            this.onSuggestionsClearRequested
                          }
                          getSuggestionValue={getSuggestionValue}
                          renderSuggestion={renderSuggestion}
                          inputProps={inputProps}
                          onSuggestionSelected={this.getSuggestionValue}
                        />
                      </div>
                      <div className="col-sm-5">
                        <div className="form-group">
                          <div className="col-sm-9">
                            <select
                              value={this.state.target}
                              onChange={this.handeSelectUserTarget.bind(this)}
                              className="form-control"
                              id="sel1"
                            >
                              <option value="user">Người chơi</option>
                              <option value="owner">Chủ sân</option>
                            </select>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="table-responsive">
                      <div className="panel panel-heading">
                        <h4 />
                      </div>
                      <table className="table table-striped">
                        <thead>
                          <tr>
                            <th>#</th>
                            <th>Tên đăng nhập</th>
                            <th>Tên đội</th>
                            <th>Quyền</th>
                            <th>Trạng thái</th>
                            <th />
                          </tr>
                        </thead>
                        <tbody>
                          {this.state.result ? (
                            <tr>
                              <td>1</td>
                              <td>{this.state.result.username}</td>
                              <td>{this.state.result.profileId.name}</td>
                              <td>
                                {this.state.result.roleId.roleName === 'user'
                                  ? 'Người chơi'
                                  : 'Chủ sân'}
                              </td>
                              <td>
                                {this.state.result.status ? (
                                  <span className="label label-success">
                                    Đang hoạt động
                                  </span>
                                ) : (
                                  <span className="label label-danger">
                                    Đã khoá
                                  </span>
                                )}
                              </td>
                              <td>
                                <button
                                  className="btn btn-primary"
                                  onClick={() =>
                                    this.handelShowModalUser(this.state.result)}
                                >
                                  Chi tiết
                                </button>
                              </td>
                            </tr>
                          ) : afterFilterUser.length > 0 ? (
                            afterFilterUser
                              .slice(
                                (this.state.pageActive - 1) * 10,
                                this.state.pageActive * 10,
                              )
                              .map((reported, index) => (
                                <tr key={index}>
                                  <td>
                                    {(this.state.pageActive - 1) * 10 +
                                      index +
                                      1}
                                  </td>
                                  <td>{reported.username}</td>
                                  <td>{reported.profileId.name}</td>
                                  <td>
                                    {reported.roleId.roleName === 'user'
                                      ? 'Người chơi'
                                      : 'Chủ sân'}
                                  </td>
                                  <td>
                                    {reported.requestLock &&
                                    !reported.lockStatus ? (
                                      <span className="label label-warning">
                                        Đang chờ khoá
                                      </span>
                                    ) : !reported.lockStatus ? (
                                      <span className="label label-success">
                                        Đang hoạt động
                                      </span>
                                    ) : (
                                      <span className="label label-danger">
                                        Đã khoá
                                      </span>
                                    )}
                                  </td>
                                  <td>
                                    <button
                                      className="btn btn-primary"
                                      onClick={() =>
                                        this.handelShowModalUser(reported)}
                                    >
                                      Chi tiết
                                    </button>
                                  </td>
                                </tr>
                              ))
                          ) : null}
                        </tbody>
                      </table>
                    </div>
                    <div className="col-sm-12 text-center">
                      <Pagination
                        bsSize="medium"
                        items={result? 0 : pageSize <= 1 ? 0 : pageSize}
                        activePage={this.state.pageActive}
                        onSelect={this.handleSelectPage.bind(this)}
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <Modal
          /* {...this.props} */
          show={this.state.showModelUser}
          onHide={this.hideModal}
          dialogClassName="custom-modal"
          bsSize="large"
        >
          <Modal.Header>
            <Modal.Title>
              Thông tin{' '}
              {this.state.userTarget === 'user' ? 'người dùng' : 'chủ sân'}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {result !== undefined || this.state.isSearch !== undefined ? (
              <div>
                <div className="row">
                  <div className="col-sm-3">
                    <img
                      src={
                        result !== undefined
                          ? result.profileId.avatarUrl
                            ? result.profileId.avatarUrl
                            : require('../resource/images/user.png')
                          : this.state.isSearch.profileId.avatarUrl
                            ? this.state.isSearch.profileId.profileId.avatarUrl
                            : require('../resource/images/user.png')
                      }
                      style={imgStyle}
                    />
                  </div>
                  <div className="col-sm-5">
                    <div className="table-responsive">
                      <table className="table ">
                        <tbody>
                          <tr>
                            <td>
                              <strong>Tên đăng nhập</strong>
                            </td>
                            <td>
                              {result
                                ? result.username
                                : this.state.isSearch.username}
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <strong>Tên đội</strong>
                            </td>
                            <td>
                              {result
                                ? result.profileId.name
                                : this.state.isSearch.profileId.name}
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <strong>Ngày tạo</strong>
                            </td>
                            <td>
                              {moment(
                                result
                                  ? result.profileId.creationDate
                                  : this.state.isSearch.profileId.creationDate,
                              ).format('DD [tháng] MM, YYYY HH:mm')}
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <strong>Tiền </strong>
                            </td>
                            <td>
                              {(result
                                ? result.profileId.balance
                                : this.state.isSearch.profileId.balance * 1000
                              ).toLocaleString('vi') + ' VND'}
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <strong>Số điện thoại </strong>
                            </td>
                            <td>
                              {result
                                ? result.profileId.phone
                                : this.state.isSearch.profileId.phone}
                            </td>
                          </tr>
                          {this.state.userTarget === 'user' ? (
                            <tr>
                              <td>
                                <strong>Điểm đội </strong>
                              </td>
                              <td>
                                {result
                                  ? result.profileId.ratingScore
                                  : this.state.isSearch.profileId.ratingScore}
                              </td>
                            </tr>
                          ) : null}
                          <tr>
                            <td>
                              <strong>Điểm thưởng </strong>
                            </td>
                            <td>
                              {result
                                ? result.profileId.bonusPoint
                                : this.state.isSearch.profileId.bonusPoint}
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                  <div className="col-sm-4">
                    <p>
                      {result ? (
                        !result.requestLock ? (
                          <button
                            onClick={this.handelLockAccount.bind(this)}
                            className="btn btn-warning"
                          >
                            <i className="pe-7s-lock" /> Yêu cầu khoá tài khoản
                          </button>
                        ) : null
                      ) : !this.state.isSearch.requestLock ? (
                        <button
                          onClick={this.handelLockAccount.bind(this)}
                          className="btn btn-warning"
                        >
                          <i className="pe-7s-lock" /> Yêu cầu khoá tài khoản
                        </button>
                      ) : null}
                    </p>
                  </div>
                </div>
                <div className="table-responsive">
                  <div className="panel panel-heading">
                    <h4 />
                  </div>
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Người tố cáo</th>
                        <th>Trận ngày</th>
                        <th>Nội dung</th>
                        <th />
                      </tr>
                    </thead>
                    <tbody>
                      {this.state.listReportWithTargetUser.length > 0
                        ? this.state.listReportWithTargetUser.map(
                            (report, index) => (
                              <tr key={index}>
                                <td>{index + 1}</td>
                                <td>{report.accuserId.username}</td>
                                <td>
                                  {moment(report.accuserId.creationDate).format(
                                    'DD [tháng] MM, YYYY | HH:mm',
                                  )}
                                </td>
                                <td>{report.reason}</td>
                              </tr>
                            ),
                          )
                        : null}
                    </tbody>
                  </table>
                </div>
              </div>
            ) : null}
          </Modal.Body>
          <Modal.Footer>
            {/* <button
              onClick={this.handeSubmitPromotion.bind(this)}
              className="btn btn-primary"
            >
              Thêm mới
            </button> */}
            <button
              onClick={this.handelHideModalUser.bind(this)}
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
})(ManageUser);
