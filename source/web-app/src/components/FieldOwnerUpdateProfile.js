import React, { Component } from 'react';
import { fetchFieldOwnerUpdateProfile } from '../apis/field-owner-apis';
import {updateOwnerField} from '../'
class FieldOwnerUpdateProfile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            address:  '',
            avatarUrl: '',
            creditCard: '',
            latitude: '',
            longitute: '',
            name: '',
            phone: ''
        }
    }

    componentDidMount(){

    }

    handleChangeUsername(event) {
        this.setState({username: event.target.value});
        const model ={
            field:"username",
            value: event.target.value
        }
    }

    handleChangePassword(event) {
        this.setState({password: event.target.value});
    }

    handleChangeAddress(event) {
        this.setState({address: event.target.value});
    }

    handleChangeAvatarUrl(event) {
        this.setState({avatarUrl: event.target.value});
    }

    handleChangeCreditCard(event) {
        this.setState({creditCard: event.target.value});
    }

    handleChangeLatitude(event) {
        this.setState({latitude: event.target.value});
    }

    handleChangeLongitute(event) {
        this.setState({longitute: event.target.value});
    }

    handleChangeName(event) {
        this.setState({name: event.target.value});
    }

    handleChangePhone(event) {
        this.setState({phone: event.target.value});
    }
    
    // handleSubmit(event) {
    //     alert('A name was submitted: ' + this.state.username + this.state.password);
    //     event.preventDefault();
    // }

    handleSubmit(event) {
        // evt.preventDefault();
        const { username, password, address, avatarUrl, creditCard, latitude, longitute, name, phone} = this.state;
        fetchFieldOwnerUpdateProfile(username, password, address, avatarUrl, creditCard, latitude, longitute, name, phone);
        // .then(
        //   fetchGetAllField(1).then(data => getAllField(data)),
        // );
      }


    render() {
        return (
            <div className="container" >
                <div className="row">
                    <div className="col-md-4 col-md-offset-4">
                        <div className="login-panel panel panel-default">
                            <div className="panel-heading">
                                <h3 className="panel-title">Update Profile</h3>
                            </div>
                            <div className="panel-body">
                                <form onSubmit={this.handleSubmit.bind(this)} >
                                    <fieldset>
                                        <div className="form-group">
                                            <input value={this.state.username} onChange={this.handleChangeUsername.bind(this)} className="form-control" placeholder="Username" name="username" type="text" />
                                        </div>
                                        <div className="form-group">
                                            <input value={this.state.password} onChange={this.handleChangePassword.bind(this)}  className="form-control" placeholder="Password" name="password" type="password" />
                                        </div>
                                        <div className="form-group">
                                            <input value={this.state.address} onChange={this.handleChangeAddress.bind(this)} className="form-control" placeholder="Address" name="address" type="text" />
                                        </div>
                                        <div className="form-group">
                                            <input value={this.state.avatarUrl} onChange={this.handleChangeAvatarUrl.bind(this)} className="form-control" placeholder="AvatarUrl" name="avatarUrl" type="text" />
                                        </div>
                                        <div className="form-group">
                                            <input value={this.state.creditCard} onChange={this.handleChangeCreditCard.bind(this)} className="form-control" placeholder="CreditCard" name="creditCard" type="text" />
                                        </div>
                                        <div className="form-group">
                                            <input value={this.state.name} onChange={this.handleChangeName.bind(this)} className="form-control" placeholder="Name" name="name" type="text" />
                                        </div>
                                        <div className="form-group">
                                            <input value={this.state.phone} onChange={this.handleChangePhone.bind(this)} className="form-control" placeholder="Phone" name="phone" type="text" />
                                        </div>
                                        <button type="submit" className="btn btn-lg btn-success btn-block">
                                            Update Profile
                                        </button>
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

export default FieldOwnerUpdateProfile;