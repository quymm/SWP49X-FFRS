import React, { Component } from 'react';

class Home extends Component {
    render() {
        return (
            <div id="page-wrapper">
                <div className="container-fluid">
                    <div className="row">
                        <div className="col-lg-4">
                            <h2 className="page-header">Home</h2>
                        </div>
                        <div className="col-lg-4">
                            <div className="page-header">
                                <form className="navbar-form navbar-left">
                                    <div className="form-group">
                                        <input type="text" className="form-control" placeholder="20/09/2017" />
                                    </div>
                                    <button className="btn btn-default" type="button">
                                        <i className="glyphicon glyphicon-calendar"></i>
                                    </button>
                                </form>
                            </div>
                        </div>

                        <div className="col-lg-4">
                            <div className="page-header">
                                <form className="navbar-form navbar-left">
                                    <div className="form-group">
                                        <input type="text" className="form-control" placeholder="Search" />
                                    </div>
                                    <button className="btn btn-default" type="button">
                                        <i className="glyphicon glyphicon-search"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-lg-4">
                            <div className="panel panel-green">
                                <div className="panel-heading">
                                    <div className="row">
                                        <div className="col-lg-6">
                                            FIELD 1
                                    </div>
                                        <div className="col-lg-6 text-right"><i>20/09/2017</i></div>
                                    </div>
                                </div>
                                <div className="panel-body">
                                    <div className="row">
                                        <div className="col-lg-4 text-center">
                                            <a href="profile.html">
                                                <img src="ronaldo.jpg" alt="..." className="img-circle" width="80" height="80" />
                                                <h4>quymm</h4>
                                            </a>
                                        </div>
                                        <div className="col-lg-4 text-center">
                                            <h3>9:00</h3>
                                            <h4>5 vs 5</h4>
                                        </div>
                                        <div className="col-lg-4 text-center">
                                            <a href="#">
                                                <img src="ronaldo.jpg" alt="..." className="img-circle" width="80" height="80" />
                                                <h4>thanhth</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <a href="#">
                                    <div className="panel-footer">
                                        <span className="pull-left">Feedback</span>
                                        <span className="pull-right"><i className="fa fa-arrow-circle-right"></i></span>
                                        <div className="clearfix"></div>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


        );
    }
}
export default Home;