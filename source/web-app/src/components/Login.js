import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { fetchLogin } from '../apis/guest-apis';
import {
  doLoginSuccessful,
  doLoginError,
} from '../redux/guest/guest-action-creators';
class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: undefined,
      password: undefined,
    };
  }

  handleUsernameChange(usename) {
    this.setState({ username: usename.target.value });
  }

  handlePasswordChange(password) {
    this.setState({ password: password.target.value });
  }

  async handleLogin(evt) {
    evt.preventDefault();
    const { username, password } = this.state;
    if (username !== undefined && password !== undefined) {
      const loginRes = await fetchLogin(username, password);
      debugger
      if (loginRes.status === 200) {
        const dataLogin = loginRes.body;
        if (dataLogin !== null) {
          localStorage.setItem('auth', JSON.stringify(dataLogin));
          await this.props.doLoginSuccessful(dataLogin);
          if (dataLogin.roleId.roleName === 'owner') {
            this.props.history.push('/app/index');
          } else if (dataLogin.roleId.roleName === 'staff') {
            this.props.history.push('/app/staff-manage-user');
          } else if (dataLogin.roleId.roleName === 'admin') {
            this.props.history.push('/app/admin-manage-account');
          }
        } else {
          this.props.doLoginError('Sai tên đăng nhập hoặc mật khẩu');
        }
      } else {
        this.props.doLoginError('Sai tên đăng nhập hoặc mật khẩu');
      }
    } else {
      this.props.doLoginError('Điền đầy đủ các trường');
    }
  }
  render() {
    const { message } = this.props.auth.user.status;
    console.log(this.props);
    const styleLogo = {
      width: 200,
      height: 80,
    };
    return (
      <div className="container-fluid backGroundLogin">
        <div className="neon-text">
          <img alt="logo"
            style={styleLogo}
            src={require('../resource/images/ffrs.png')}
          />Hệ Thống Quản Lý Sân Bóng{' '}
        </div>
        <div className="row">
          <div className="col-md-4 col-md-offset-4 login-padding">
            <div className="login-panel panel panel-default">
              <div className="panel-body">
                <h4 className="text-center loginHeader">
                  <strong>Đăng nhập</strong>
                </h4>
                <form onSubmit={this.handleLogin.bind(this)}>
                  <fieldset>
                    <p className="text-center text-danger">
                      <i>{message === null ? null : message}</i>
                    </p>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">
                        Tên đăng nhập <span />
                      </label>
                      <input
                        value={this.state.username}
                        onChange={this.handleUsernameChange.bind(this)}
                        className="form-control"
                        name="username"
                        type="text"
                        id="exampleInputEmail1"
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">Mật khẩu</label>
                      <input
                        onChange={this.handlePasswordChange.bind(this)}
                        className="form-control"
                        name="password"
                        type="password"
                        value={this.state.password}
                        id="exampleInputEmail1"
                      />
                    </div>
                    <div className="checkbox">
                      <label>
                        <Link to="/register">Đăng kí trở thành chủ sân</Link>
                      </label>
                    </div>
                    <button
                      type="submit"
                      className="btn btn-lg btn-success btn-block"
                    >
                      Đăng nhập
                    </button>
                  </fieldset>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
function mapStateToProps(state) {
  return { auth: state.auth };
}
export default connect(mapStateToProps, { doLoginSuccessful, doLoginError })(
  Login,
);
