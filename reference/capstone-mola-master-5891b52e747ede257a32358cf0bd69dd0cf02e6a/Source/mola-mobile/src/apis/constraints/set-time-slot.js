import moment from 'moment';

class SetTimeSlotConstraint {

  _compareDate(start, end){
    return moment(start, 'DD-MM-YYYY') - (moment(end, 'DD-MM-YYYY'));
  }
  _inRange(date, {dateFrom, timeFrom, dateTo, timeTo}){
    const from = `${dateFrom} ${timeFrom}`;
    const to = `${dateTo} ${timeTo}`;
    const mDate = moment(date).format('DD-MM-YYYY hh:mm');
    const mFrom = moment(from, 'DD-MM-YYYY hh:mm');
    const mTo = moment(to, 'DD-MM-YYYY hh:mm');
    // debugger
    return moment(date).isBetween(mFrom, mTo);
  }
  _isValidTimeSlot(schedule, {dateFrom, timeFrom, dateTo, timeTo}){
    const {startTimeLong, endTimeLong} = schedule;
    const duration = moment.duration(moment(startTimeLong).diff(moment(endTimeLong)));
  }
}

export default SetTimeSlotConstraint;