import React, { Component } from 'react';
import moment from 'moment';
// import { Link } from 'react-router-dom';
export default props => {
  const { listMatch } = props;
  return (
    <div>
      {listMatch.length > 0
        ? listMatch.map(listMatch => (
            <div key={listMatch.id} className="col-lg-4">
              <div className="panel panel-green">
                <div className="panel-heading">
                  <div className="row">
                    <div className="col-lg-6">Sân</div>
                    <div className="col-lg-6 text-right">
                      <i>{new Date(listMatch.date).toDateString()}</i>
                    </div>
                  </div>
                </div>
                <div className="panel-body">
                  <div className="row">
                    <div className="col-lg-4 text-center">
                      <h4>thanhth</h4>
                    </div>
                    <div className="col-lg-4 text-center">
                      <h3>
                        {moment('10-10-2017 ' + listMatch.startTime).format(
                          'HH:mm',
                        )}
                      </h3>
                      <h4>
                        {moment('10-10-2017 ' + listMatch.endTime).hour() * 60 +
                          moment('10-10-2017 ' + listMatch.endTime).minute() -
                          (moment('10-10-2017 ' + listMatch.startTime).hour() *
                            60 +
                            moment(
                              '10-10-2017 ' + listMatch.startTime,
                            ).minute())}{' '}
                        phút
                      </h4>
                      <h4>{listMatch.fieldTypeId.name}</h4>
                    </div>
                    <div className="col-lg-4 text-center">
                      <h4>quymm</h4>
                    </div>
                  </div>
                </div>
                <a href="#">
                  <div className="panel-footer">
                    <span className="pull-left">Cập nhật sân</span>
                    <span className="pull-right">
                      <i className="fa fa-arrow-circle-right" />
                    </span>
                    <div className="clearfix" />
                  </div>
                </a>
              </div>
            </div>
          ))
        : 'Không có trận đấu nào'}
    </div>
  );
};
