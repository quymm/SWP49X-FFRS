import React, { Component } from 'react';
import { doLoginSuccessful } from '../redux/guest/guest-action-creators';
import { connect } from 'react-redux';
import Autosuggest from 'react-autosuggest';
import { Modal } from 'react-bootstrap';
import {
  fetchGetUserOrFieldOwnerSuggestion,
  fetchGetAllReport,
} from '../apis/staff-api';

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
      listReported: [],
    };
    this.handelShowModalUser = this.handelShowModalUser.bind(this);
  }
  onChange = async (event, { newValue }) => {
    console.log(newValue);
    await this.setState({
      value: newValue,
    });
  };
  getSuggestionValue = (event, { suggestion }) => {
    this.setState({ result: suggestion });
  };

  onSuggestionsFetchRequested = async ({ value }) => {
    if (value.length > 0) {
      const dataUser = await fetchGetUserOrFieldOwnerSuggestion(value, 'user');
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
  handelShowModalUser(evt) {
    debugger;
    this.setState({ showModelUser: true, result: evt });
  }

  handelHideModalUser(evt) {
    evt.preventDefault();
    this.setState({ showModelUser: false });
  }
  async componentDidMount() {
    const { id } = this.props.auth.user.data;
    if (id === undefined) {
      const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
      const idLocal = authLocalStorage.id;
      await this.props.doLoginSuccessful(authLocalStorage);
    } else {
    }
    const dataListReported = await fetchGetAllReport();
    this.setState({ listReported: dataListReported.body });
  }
  render() {
    const { value, suggestions, result, listReported } = this.state;
    console.log(result);
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
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-4">
                <h2 className="page-header">Quản lý người dùng</h2>
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel panel-body">
                    <div className="col-sm-12 text-center">
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
                    <div className="table-responsive">
                      <div className="panel panel-heading">
                        <h4 />
                      </div>
                      <table className="table table-striped">
                        <thead>
                          <tr>
                            <th>Tên đăng nhập</th>
                            <th>Tên đội</th>
                            {/* <th>Ngày tạo</th> */}
                            <th>Trạng thái</th>
                            <th />
                          </tr>
                        </thead>
                        <tbody>
                          {this.state.result ? (
                            <tr>
                              <td>{this.state.result.username}</td>
                              <td>{this.state.result.profileId.name}</td>
                              <td>
                                {this.state.result.status ? (
                                  <span className="label label-success">
                                    Đang hoạt động
                                  </span>
                                ) : (
                                  <span className="label label-danger">
                                    Bị khoá
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
                          ) : listReported > 0 ? (
                            listReported.map(reported => (
                              <tr>
                                <td>{reported.username}</td>
                                <td>{reported.profileId.name}</td>
                                <td>
                                  {reported.status ? (
                                    <span className="label label-success">
                                      Đang hoạt động
                                    </span>
                                  ) : (
                                    <span className="label label-danger">
                                      Bị khoá
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
            <Modal.Title>Thông tin người dùng</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {result ? (
              <div>
                <div className="row">
                  <div className="col-sm-4">
                    <img
                      src={result.profileId.avatarUrl? result.profileId.avatarUrl : require('../resource/images/user.png')}
                      style={imgStyle}
                    />
                  </div>
                  <div className="col-sm-4">
                    <div className="table-responsive">
                      <table className="table ">
                        <tbody>
                        <tr>
                          <td><strong>Tên đăng nhập</strong></td>
                          <td>{result.username}</td>
                        </tr>
                        <tr>
                          <td><strong>Tên đội</strong></td>
                          <td>{result.profileId.name}</td>
                        </tr>
                        <tr>
                          <td><strong>Tiền </strong></td>
                          <td>{result.profileId.balance.toLocaleString('vi')+ ' VND'}</td>
                        </tr>
                        <tr>
                          <td><strong>Số điện thoại </strong></td>
                          <td>{result.profileId.phone}</td>
                        </tr>
                        <tr>
                          <td><strong>Điểm đội </strong></td>
                          <td>{result.profileId.ratingScore}</td>
                        </tr><tr>
                          <td><strong>Điểm thưởng </strong></td>
                          <td>{result.profileId.bonusPoint}</td>
                        </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                  <div className="col-sm-4">
                    <p>
                      <button className="btn btn-warning">
                        <i className="pe-7s-lock" /> Yêu cầu khoá tài khoản
                      </button>
                    </p>
                  </div>
                </div>
                <div className="table-responsive">
                  <div className="panel panel-heading">
                    <h4></h4>
                  </div>
                  <table className="table table-striped">
                    <tbody>
                      <tr>
                        <th>Người tố cáo</th>
                        <th>Trận ngày</th>
                        <th>Nội dung</th>
                        <th />
                      </tr>
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
export default connect(mapPropsToState, { doLoginSuccessful })(ManageUser);
