import React, { Component } from 'react';
import moment from 'moment';
export default props => {
  const { freeTime5vs5, freeTime7vs7 } = props;
  return (
    <div>
      <div className="col-lg-6">
        <h3 className="text-center">Loại sân 5 người</h3>
        {freeTime5vs5.length > 0
          ? freeTime5vs5.map(freeTime => (
              <div key={freeTime.id}>
                <h4>
                  {moment('10-10-2017 ' + freeTime.startTime).format('HH:mm')} -{' '}
                  {moment('10-10-2017 ' + freeTime.endTime).format('HH:mm')}
                </h4>
              </div>
            ))
          : 'Không có thời gian rảnh'}
      </div>
      <div className="col-lg-6">
        <h3 className="text-center">Loại sân 7 người</h3>
        {freeTime7vs7.length > 0
          ? freeTime7vs7.map(freeTime => (
              <div key={freeTime.id}>
                <h4>
                  {moment('10-10-2017 ' + freeTime.startTime).format('HH:mm')} -{' '}
                  {moment('10-10-2017 ' + freeTime.endTime).format('HH:mm')}
                </h4>
              </div>
            ))
          : 'Không có thời gian rảnh'}
      </div>
    </div>
  );
};
