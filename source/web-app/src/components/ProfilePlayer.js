import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  doLoginSuccessful,
  accessDenied,
  doLogout
} from '../redux/guest/guest-action-creators';
import { fetchUpdateProfile } from '../apis/field-owner-apis';
import { toast } from 'react-toastify';
import { withRouter } from 'react-router-dom';
class ProfilePlayer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: undefined,
      fieldName: undefined,
      address: undefined,
      phone: undefined,
      longitude: undefined,
      latitude: undefined,
      avatarUrl: undefined,
      creditCard: undefined,
      password: undefined,
    };
  }
  async componentDidMount() {
    const authLocalStorage = JSON.parse(localStorage.getItem('auth'));
    console.log(authLocalStorage);
    if (authLocalStorage === null) {
      this.props.doLogout();
      this.props.history.push('/login');
    } else {
      if (authLocalStorage.roleId.roleName !== 'owner') {
        await this.props.accessDenied();
        this.props.history.push('/login');
      } else {
        await this.props.doLoginSuccessful(authLocalStorage);
        this.setState({
          username: authLocalStorage.username,
          fieldName: authLocalStorage.profileId.name,
          address: authLocalStorage.profileId.address,
          phone: authLocalStorage.profileId.phone,
          longitude: authLocalStorage.profileId.longitude,
          latitude: authLocalStorage.profileId.longitude,
          avatarUrl: authLocalStorage.profileId.avatarUrl,
          creditCard: authLocalStorage.profileId.creaditCard,
          password: authLocalStorage.password,
        });
      }
    }
  }
  async handelSumitUpdate(evt) {
    evt.preventDefault();
    debugger;
    const { id } = this.props.auth.user.data;
    const data = this.state;
    const auth = await fetchUpdateProfile(id, data);
    if (auth.status === 200) {
      localStorage.setItem('auth', JSON.stringify(auth.body));
      this.props.doLoginSuccessful(auth.body);
      toast.success('Cập nhật thông tin thành công');
      this.props.history.push('/app/index');
    } else {
      toast.error('Cập nhật thất bại');
    }
  }
  handleInputChange(evt) {
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    this.setState({ [name]: value });
  }
  render() {
    const imageStyle = { width: 700, height: 300 };
    const { data } = this.props.auth.user;
    if (data.username === undefined) {
      return <div className="loader"> </div>;
    }
    console.log(this.state);
    return (
      <div className="main-panel">
        <div className="content">
          <div className="container-fluid">
            <div className="row">
              <div className="col-md-4">
                <h2 className="page-header">Thông tin chủ sân</h2>
              </div>
            </div>
            <div className="col-sm-10 col-sm-offset-1">
              <div className="thumbnail">
                <img
                  src={data.profileId.avatarUrl}
                  alt="..."
                  style={imageStyle}
                />
              </div>
              <div className="col-sm-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <form onSubmit={this.handelSumitUpdate.bind(this)}>
                      <fieldset>
                        <div className="form-group">
                          <label htmlFor="exampleInputEmail1">
                            Tên đăng nhập
                          </label>
                          <input
                            onChange={this.handleInputChange.bind(this)}
                            value={this.state.username}
                            className="form-control"
                            name="username"
                            type="text"
                            id="exampleInputPassword1"
                            readOnly
                          />
                        </div>
                        <div className="form-group">
                          <label htmlFor="exampleInputEmail1">Tên sân</label>
                          <input
                            onChange={this.handleInputChange.bind(this)}
                            value={this.state.fieldName}
                            className="form-control"
                            name="fieldName"
                            type="text"
                            id="exampleInputPassword1"
                          />
                        </div>
                        <div className="form-group">
                          <label htmlFor="exampleInputEmail1">Địa chỉ</label>
                          <input
                            onChange={this.handleInputChange.bind(this)}
                            value={this.state.address}
                            className="form-control"
                            name="address"
                            type="text"
                          />
                        </div>
                        <div className="form-group">
                          <label htmlFor="exampleInputEmail1">
                            Số điện thoại
                          </label>
                          <input
                            onChange={this.handleInputChange.bind(this)}
                            value={this.state.phone}
                            className="form-control"
                            name="phone"
                            type="text"
                          />
                        </div>

                        <button
                          type="submit"
                          className="btn btn-lg btn-primary btn-block"
                        >
                          Cập nhật
                        </button>
                      </fieldset>
                    </form>
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
  };
}
export default withRouter(
  connect(mapPropsToState, { accessDenied, doLoginSuccessful, doLogout })(ProfilePlayer),
);
