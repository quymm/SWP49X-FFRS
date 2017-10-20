import React, { Component } from 'react';
import {
  fetchRegister,
  fechGetAddressByLocationGoogleMap,
} from '../apis/guest-apis';
import { Link } from 'react-router-dom';
class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: undefined,
      password: undefined,
      confirmPassword: undefined,
      address: undefined,
      avatarUrl: '',
      creditCard: undefined,
      latitude: undefined,
      longitude: undefined,
      name: undefined,
      phone: undefined,
      message: undefined,
      messageSuccess: false,
    };
  }

  async componentDidMount() {
    // navigator.geolocated.getCurrentPosition(s => console.log(s), e => console.log(e));
    // console.log(navigator.geolocation.getCurrentPosition(e => console.log(e)));
    navigator.geolocation.getCurrentPosition(async location => {
      const address = await fechGetAddressByLocationGoogleMap(
        location.coords.latitude,
        location.coords.longitude,
      );

      await this.setState({
        latitude: location.coords.latitude,
        longitude: location.coords.longitude,
        address: address,
      });
      console.log(this.state);
    });
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
    const { avatarUrl } = this.state;
    let avatarUrlAfter = avatarUrl.slice(
      avatarUrl.indexOf('fakepath') + 9,
      avatarUrl.length,
    );
    console.log(avatarUrlAfter);
    const {
      username,
      password,
      address,
      creditCard,
      latitude,
      longitude,
      name,
      phone,
      message,
      confirmPassword,
    } = this.state;
    if (
      username !== undefined &&
      password !== undefined &&
      address !== undefined &&
      confirmPassword !== undefined &&
      avatarUrl !== ''
    ) {
      if (password === confirmPassword) {
        // try {
        const registerRes = await fetchRegister(
          username,
          password,
          address,
          avatarUrlAfter,
          '123456',
          latitude,
          longitude,
          name,
          phone,
        );
        debugger;
        if (registerRes.status === 201) {
          this.setState({ messageSuccess: true, message: '' });
        } else if (registerRes.status === 400) {
          this.setState({ message: 'Tên đăng nhập đã tồn tại' });
        } else {
          this.setState({ message: 'Đăng kí không thành công' });
        }
      } else {
        this.setState({ message: 'Mật khẩu không giống nhau' });
      }
    } else {
      this.setState({ message: 'Vui lòng điền đủ các trường' });
    }
  }

  render() {
    console.log(this.state);
    const { message, address, messageSuccess } = this.state;
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
                        <i>Chúng tôi sẽ lấy vị trí của bạn làm địa chỉ</i>
                      </label>
                      <input
                        value={address ? address : 'Đang lấy địa chỉ'}
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
                      <label htmlFor="exampleInputEmail1">Hình ảnh</label>
                      <input
                        value={this.state.avatarUrl}
                        onChange={this.handleInputChange.bind(this)}
                        name="avatarUrl"
                        type="file"
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
                    <p className="text-center text-danger">
                      <i>{message === null ? null : message}</i>
                    </p>
                    {messageSuccess ? (
                      <p className="text-center text-success">
                        <i>
                          Đăng kí thành công. Nhấn vào đây để{' '}
                          <Link to="/login">
                            <strong>đăng nhập</strong>
                          </Link>
                        </i>
                      </p>
                    ) : null}
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
