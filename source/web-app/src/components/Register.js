import React, { Component } from 'react';
import { fetchRegister, fechGetAddressByLocationGoogleMap } from '../apis/guest-apis';
import { geolocated } from 'react-geolocated';
class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: undefined,
      password: undefined,
      confirmPassword: undefined,
      address: undefined,
      avatarUrl: undefined,
      creditCard: undefined,
      latitude: undefined,
      longitude: undefined,
      name: undefined,
      phone: undefined,
      message: undefined,
    };
  }

   componentDidMount() {
    // navigator.geolocated.getCurrentPosition(s => console.log(s), e => console.log(e));
    // console.log(navigator.geolocation.getCurrentPosition(e => console.log(e)));
    navigator.geolocation.getCurrentPosition(async location => {
        const address = await fechGetAddressByLocationGoogleMap(location.coords.latitude, location.coords.longitude);
       this.setState({
        latitude: location.coords.latitude,
        longitude: location.coords.longitude,
        address : address,
      })},
    );
    
  }

  async handleInputChange(evt) {
    const target = evt.target;
    const value = target.value;
    const name = target.name;
    await this.setState({ [name]: value });
    console.log(this.state);
  }

  async handelSetLocation(evt) {
    await this.setState({ address: evt.target.value });
    console.log(this.state);
  }

  async handleSubmit(event) {
    event.preventDefault();
    const {
      username,
      password,
      address,
      avatarUrl,
      creditCard,
      latitude,
      longitute,
      name,
      phone,
      message,
      confirmPassword,
    } = this.state;
    if (
      username !== undefined &&
      password !== undefined &&
      address !== undefined &&
      confirmPassword !== undefined
    ) {
      if (password === confirmPassword) {
        const data = await fetchRegister(
          username,
          password,
          address,
          avatarUrl,
          creditCard,
          latitude,
          longitute,
          name,
          phone,
          message,
        );
      } else {
        this.setState({ message: 'Mật khẩu không giống nhau' });
      }
    } else {
      this.setState({ message: 'Vui lòng điền đủ các trường' });
    }
  }

  render() {
    console.log(this.state);
    const { message, address } = this.state;
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-4 col-md-offset-4">
            <div className="login-panel panel panel-default">
              <div className="panel-heading">
                <h3 className="panel-title">Đăng kí</h3>
              </div>
              <div className="panel-body">
                <form onSubmit={this.handleSubmit.bind(this)}>
                  <fieldset>
                    <p className="text-center text-danger">
                      <i>{message === null ? null : message}</i>
                    </p>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">Tên đăng nhập</label>
                      <input
                        value={this.state.username}
                        onChange={this.handleInputChange.bind(this)}
                        className="form-control"
                        name="username"
                        type="text"
                        id="exampleInputPassword1"
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">Tên sân</label>
                      <input
                        value={this.state.name}
                        onChange={this.handleInputChange.bind(this)}
                        className="form-control"
                        name="name"
                        type="text"
                        id="exampleInputPassword1"
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">
                        <i>Hãy chắc chắn rằng đây là địa chỉ của bạn</i>
                      </label>
                      <input
                        value={
                          address? address : 'Đang lấy địa chỉ'
                        }
                        onChange={this.handelSetLocation.bind(this)}
                        className="form-control"
                        name="address"
                        type="text"
                        readOnly
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">Số điện thoại</label>
                      <input
                        value={this.state.phone}
                        onChange={this.handleInputChange.bind(this)}
                        className="form-control"
                        name="phone"
                        type="text"
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">Mật khẩu</label>
                      <input
                        value={this.state.password}
                        onChange={this.handleInputChange.bind(this)}
                        className="form-control"
                        name="password"
                        type="password"
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="exampleInputEmail1">
                        Xác nhận mật khẩu
                      </label>
                      <input
                        value={this.state.confirmPassword}
                        onChange={this.handleInputChange.bind(this)}
                        className="form-control"
                        name="confirmPassword"
                        type="password"
                      />
                    </div>
                    <button
                      type="submit"
                      className="btn btn-lg btn-success btn-block"
                    >
                      Đăng kí
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

// export default geolocated({
//   positionOptions: {
//     enableHighAccuracy: true,
//   },
//   userDecisionTimeout: null,
// })(Register);
export default Register;
