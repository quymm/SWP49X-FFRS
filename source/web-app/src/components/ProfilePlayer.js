import React, { Component } from 'react';

class ProfilePlayer extends Component {
    render(){
        return(
            <div id="page-wrapper">
            <div className="container-fluid">
                <div className="row">
                    <div className="col-lg-4">
                        <h2 className="page-header">Player Profile</h2>
                    </div>
                </div>
                        <div className="col-sm-6 col-sm-offset-3">
                                <div className="thumbnail">
                                  <img src="ronaldo.jpg" alt="..." width="200" height="300" />
                                  <div className="caption">
                                    <h3 className="text-center">thanhth</h3>
                                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Eum aut sunt nam, ullam libero nulla sapiente necessitatibus vel eius nihil.</p>
                                    <p className="text-center"><a href="#" class="btn btn-danger" role="button">Report</a></p>
                                  </div>
                                </div>                            
                </div>
                <div class="col-lg-12">
                    <div class="media">
                        <div class="media-left">
                          <img src="ronaldo.jpg" class="media-object" style="width:60px" />
                        </div>
                        <div class="media-body">
                          <h4 class="media-heading">John Doe <small><i>Posted on February 20, 2016</i></small></h4>
                          <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Sapiente, vel?</p>
                        </div>
                      </div>
                      <div class="media">
                        <div class="media-left">
                          <img src="ronaldo.jpg" class="media-object" style="width:60px" />
                        </div>
                        <div class="media-body">
                          <h4 class="media-heading">John Doe <small><i>Posted on February 20, 2016</i></small></h4>
                          <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Sapiente, vel?</p>
                        </div>
                      </div>
                </div>
            </div>
        </div>
        );
    }
}