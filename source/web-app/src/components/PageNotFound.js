import React, { Component } from 'react';
import { Link } from 'react-router-dom';
class PageNotFound extends Component {
  render() {
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-12">
            <div className="error-template">
              <h1>Oops!</h1>
              <h2>404 Not Found</h2>
              <div className="error-details">
                Xin lỗi, có lỗi vừa xảy ra, Yêu cầu trang không tìm thấy!
              </div>
              <div className="error-actions">
                <Link to="/login" className="btn btn-success btn-lg">
                  <span className="glyphicon glyphicon-log-in" />
                  {'  '}Đăng nhập{' '}
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default PageNotFound;
