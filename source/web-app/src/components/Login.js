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
    const loginRes = await fetchLogin(username, password);

    console.log(loginRes);

    if (username !== undefined && password !== undefined) {
      if (loginRes.status === 200) {
        const dataLogin = loginRes.body;
        if (dataLogin !== null) {
          await this.props.doLoginSuccessful(dataLogin);
          console.log(this.props);
          debugger
          if (dataLogin.roleId.roleName === 'owner') {
            this.props.history.push('/app/index');
          }
        } else if (dataLogin.user.role === 'staff') {
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
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-4 col-md-offset-4">
            <div className="login-panel panel panel-default">
              <div className="panel-heading">
                <h3 className="panel-title">Đăng nhập</h3>
              </div>
              <div className="panel-body">
                <form onSubmit={this.handleLogin.bind(this)}>
                  <fieldset>
                    <p className="text-center text-danger">
                      <i>{message === null ? null : message}</i>
                    </p>
                    <div className="form-group">
                      <input
                        value={this.state.username}
                        onChange={this.handleUsernameChange.bind(this)}
                        className="form-control"
                        placeholder="Tên đăng nhập"
                        name="username"
                        type="text"
                      />
                    </div>
                    <div className="form-group">
                      <input
                        onChange={this.handlePasswordChange.bind(this)}
                        className="form-control"
                        placeholder="Mật khẩu"
                        name="password"
                        type="password"
                        value={this.state.password}
                      />
                    </div>
                    <div className="checkbox">
                      <label>
                        <Link to="/register">Đăng kí trở thành chủ sân</Link>
                      </label>
                    </div>
                    {/* <a
                      href="index.html"
                      className="btn btn-lg btn-success btn-block"
                    >
                      Login
                    </a> */}

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
