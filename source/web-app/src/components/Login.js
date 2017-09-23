import React, { Component } from 'react';
import { Link } from 'react-router-dom';


class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        }
    }


    render() {
        return (
            <div className="container" >
                <div className="row">
                    <div className="col-md-4 col-md-offset-4">
                        <div className="login-panel panel panel-default">
                            <div className="panel-heading">
                                <h3 className="panel-title">Please Sign In</h3>
                            </div>
                            <div className="panel-body">
                                <form>
                                    <fieldset>
                                        <div className="form-group">
                                            <input onChange={(text) => this.setState({ username: text })} className="form-control" placeholder="Username" name="email" type="text"/>
                                        </div>
                                        <div className="form-group">
                                            <input onChange={(text) => this.setState({ password: text })} className="form-control" placeholder="Password" name="password" type="password" value="" />
                                        </div>
                                        <div className="checkbox">
                                            <label>
                                                <Link to="/register">Register</Link>
                                            </label>
                                        </div>
                                        <a href="index.html" className="btn btn-lg btn-success btn-block">Login</a>
                                    </fieldset>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        );
    }
}

export default Login;