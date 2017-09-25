import React, { Component } from 'react';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
class ProfilePlayer extends Component {
  render() {
    const divStyle = {
      width: 60,
    };
    return (
      <div>
        <Header />
        <Navigation />
        <div id="page-wrapper">
          <div className="container-fluid">
            <div className="row">
              <div className="col-lg-4">
                <h2 className="page-header">Player Profile</h2>
              </div>
            </div>
            <div className="col-sm-6 col-sm-offset-3">
              <div className="thumbnail">
                <img
                  src={require('../resource/images/ronaldo.jpg')}
                  alt="..."
                  width="200"
                  height="300"
                />
                <div className="caption">
                  <h3 className="text-center">thanhth</h3>
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit. Eum
                    aut sunt nam, ullam libero nulla sapiente necessitatibus vel
                    eius nihil.
                  </p>
                  <p className="text-center">
                    <button className="btn btn-danger">Report</button>
                  </p>
                </div>
              </div>
            </div>
            <div className="col-lg-12">
              <div className="media">
                <div className="media-left">
                  <img
                    src={require('../resource/images/ronaldo.jpg')}
                    className="media-object"
                    style={divStyle}
                  />
                </div>
                <div className="media-body">
                  <h4 className="media-heading">
                    John Doe{' '}
                    <small>
                      <i>Posted on February 20, 2016</i>
                    </small>
                  </h4>
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Sapiente, vel?
                  </p>
                </div>
              </div>
              <div className="media">
                <div className="media-left">
                  <img
                    src={require('../resource/images/ronaldo.jpg')}
                    className="media-object"
                    style={divStyle}
                  />
                </div>
                <div className="media-body">
                  <h4 className="media-heading">
                    John Doe{' '}
                    <small>
                      <i>Posted on February 20, 2016</i>
                    </small>
                  </h4>
                  <p>
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Sapiente, vel?
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default ProfilePlayer;
