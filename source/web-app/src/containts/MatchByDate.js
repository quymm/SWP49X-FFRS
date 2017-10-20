import React, { Component } from 'react';
import moment from 'moment';
// import { Link } from 'react-router-dom';
export default props => {
  const { listMatch } = props;
  console.log(listMatch);
  if (listMatch === undefined) {
    return <div className="loader"></div>;
  }
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
                      <i>
                        {new Date(listMatch.timeSlotEntity.date).toDateString()}
                      </i>
                    </div>
                  </div>
                </div>
                <div className="panel-body">
                  <h4 className="text-center">
                    <strong>{listMatch.user.profileId.name}</strong> vs{' '}
                    <strong>{listMatch.opponent.profileId.name}</strong>
                  </h4>
                  <div className="row">
                    <div className="col-lg-12">
                      <h4 className="text-center">
                        {moment(
                          '10-10-2017 ' + listMatch.timeSlotEntity.startTime,
                        ).format('HH:mm')}
                      </h4>
                      <h4 className="text-center">
                        {moment(
                          '10-10-2017 ' + listMatch.timeSlotEntity.endTime,
                        ).hour() *
                          60 +
                          moment(
                            '10-10-2017 ' + listMatch.timeSlotEntity.endTime,
                          ).minute() -
                          (moment(
                            '10-10-2017 ' + listMatch.timeSlotEntity.startTime,
                          ).hour() *
                            60 +
                            moment(
                              '10-10-2017 ' +
                                listMatch.timeSlotEntity.startTime,
                            ).minute())}{' '}
                        phút
                      </h4>
                      <h4 className="text-center">{listMatch.timeSlotEntity.fieldTypeId.name}</h4>
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
