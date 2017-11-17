import React, { Component } from 'react';
import moment from 'moment';
// import { Link } from 'react-router-dom';
export default props => {
  const { listMatch } = props;
  console.log(listMatch);
  if (listMatch === undefined) {
    return <div className="loader" />;
  }
  return (
    <div>
      {listMatch.length > 0
        ? listMatch.map(listMatch => (
            <div key={listMatch.timeSlotEntity.id} className="col-sm-6">
              <div className="panel panel-green">
                <div className="panel-body">
                  <div className="row">
                    <div className="col-sm-3">
                      <h4 className="text-center match">
                        <strong>{listMatch.user.profileId.name}</strong>
                      </h4>
                    </div>
                    <div className="col-sm-6">
                      <h3 className="text-center text-primary">
                        <strong>
                          {moment(
                            '10-10-2017 ' + listMatch.timeSlotEntity.startTime,
                          ).format('HH:mm')}
                        </strong>{' '}
                      </h3>
                      <p className="text-center">
                        <strong>
                          {moment(
                            '10-10-2017 ' + listMatch.timeSlotEntity.endTime,
                          ).hour() *
                            60 +
                            moment(
                              '10-10-2017 ' + listMatch.timeSlotEntity.endTime,
                            ).minute() -
                            (moment(
                              '10-10-2017 ' +
                                listMatch.timeSlotEntity.startTime,
                            ).hour() *
                              60 +
                              moment(
                                '10-10-2017 ' +
                                  listMatch.timeSlotEntity.startTime,
                              ).minute())}{' '}
                          phút
                        </strong>
                      </p>
                      <p className="text-center">
                        <strong>
                          {listMatch.timeSlotEntity.fieldTypeId.name}
                        </strong>
                      </p>
                      <p className="text-center"><button className="btn btn-xs btn-primary">Cập nhật sân</button></p>          
                    </div>
                    <div className="col-sm-3">
                      <h4 className="text-center match">
                        <strong>{listMatch.opponent.profileId.name}</strong>
                      </h4>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))
        : 'Không có trận đấu nào'}
    </div>
  );
};
