import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { fetchLogin } from '../apis/guest-apis';
import { doLogin, doLoginError } from '../redux/guest/guest-action-creators';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
    };
  }

  handleUsernameChange(usename) {
    this.setState({ username: usename.target.value });
  }

  handlePasswordChange(password) {
    this.setState({ password: password.target.value });
  }

  async handleLogin(evt){
    evt.preventDefault();
    const { username, password } = this.state;
    const data = await fetchLogin(username, password);
    if (data.lenght > 0) {
      doLogin(data);
      if (data.user.role === 1) {
        this.props.history.push('/index');
        window.location.reload();
      }
      else if (data.user.role === 2) {
        
      }     
    }
    else{
      doLoginError(data);
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
                <h3 className="panel-title">Please Sign In</h3>
              </div>
              <div className="panel-body">
                <form onSubmit={this.handleLogin.bind(this)}>
                  <fieldset>
                    <p className="text-center text-danger"><i>{message===null? null: message}</i></p>
                    <div className="form-group">
                      <input
                        value={this.state.username}
                        onChange={this.handleUsernameChange.bind(this)}
                        className="form-control"
                        placeholder="Username"
                        name="email"
                        type="text"
                      />
                    </div>
                    <div className="form-group">
                      <input
                        onChange={this.handlePasswordChange.bind(this)}
                        className="form-control"
                        placeholder="Password"
                        name="password"
                        type="password"
                        value={this.state.password}
                      />
                    </div>
                    <div className="checkbox">
                      <label>
                        <Link to="/register">Register</Link>
                      </label>
                    </div>
                    <a
                      href="index.html"
                      className="btn btn-lg btn-success btn-block"
                    >
                      Login
                    </a>
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
function mapStateToProps(state){
  return {auth: state.auth};
}
export default connect( mapStateToProps, {doLogin} )(Login);
