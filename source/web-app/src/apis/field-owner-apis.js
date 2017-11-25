import {
  BASE_URL,
  GET_MATCH_BY_DAY,
  ADD_FIELD,
  REMOVE_FIELD,
  CHECK_TIME_SLOT,
  GET_ALL_FIELD,
  DETELE_FIELD,
  GET_TIME_ENABLE_IN_WEEK,
  UPDATE_TIME_ENABLE_IN_WEEK,
  GET_FREE_TIME,
  BOOK_MATCH,
  GET_FREE_FIELD,
  SET_FIELD,
  GET_FRIENDLY_MATCH,
  GET_TOUR_MATCH,
  GET_OVERCONE,
  UPDATE_PROFILE,
  GET_ALL_PROMOTION,
  ADD_PROMOTION,
} from './base-URL';
//get account user save at localStorage
const auth = JSON.parse(localStorage.getItem('auth'));

export function fetchGetMatchByFieldOwnerAndDay(
  fieldOwnerId,
  day,
  fieldTypeId,
) {
  return fetch(
    BASE_URL +
      GET_MATCH_BY_DAY +
      '?&field-owner-id=' +
      fieldOwnerId +
      '&field-type-id=' +
      fieldTypeId +
      '&date=' +
      day,
    { headers: { Authorization: auth.tokenValue } },
  ).then(res => res.json());
}

export function fetchCheckTimeSlotStatus(
  paramFieldOwnerId,
  paramDay,
  paramduration,
  paramFieldType,
) {
  return fetch(
    BASE_URL +
      CHECK_TIME_SLOT +
      '&fieldOwnerId' +
      paramFieldOwnerId +
      '&day' +
      paramDay +
      '&duration' +
      paramduration +
      '&fieldType' +
      paramFieldType,
    { headers: { Authorization: auth.tokenValue } },
  ).then(res => res.status);
}
//Field
export function fetchGetAllField(fieldOwnerId) {
  return fetch(BASE_URL + GET_ALL_FIELD + '?&field-owner-id=' + fieldOwnerId, {
    headers: { Authorization: auth.tokenValue },
  }).then(res => res.json());
}

export function fetchDeleteField(fieldId) {
  return fetch(BASE_URL + DETELE_FIELD + '?&field-id=' + fieldId, {
    method: 'DELETE',
    headers: { Authorization: auth.tokenValue },
  }).then(res => res.json());
}

export function fetchAddField(paramFieldName, paramFieldType, fieldOwnerId) {
  return fetch(BASE_URL + ADD_FIELD, {
    method: 'POST',
    headers: {
      'content-type': 'application/json',
      Authorization: auth.tokenValue,
    },
    body: JSON.stringify({
      fieldName: paramFieldName,
      fieldOwnerId: fieldOwnerId,
      fieldTypeId: paramFieldType,
    }),
  }).then(res => res.status);
}
export function fetchGetTimeEnableInWeek(fieldOwnerId) {
  return fetch(
    BASE_URL + GET_TIME_ENABLE_IN_WEEK + '?field-owner-id=' + fieldOwnerId,
    { headers: { Authorization: auth.tokenValue } },
  ).then(res => res.json());
}

export function fetchUpdateTimeEnableInWeek(
  timeEnable
) {
  debugger
  return fetch(BASE_URL + UPDATE_TIME_ENABLE_IN_WEEK, {
    method: 'POST',
    headers: {
      'content-type': 'application/json',
      Authorization: auth.tokenValue,
    },
    body: JSON.stringify(timeEnable)
    // body: JSON.stringify([
    //   {
    //     dayInWeek: paramDayInWeek,
    //     endTime: endDay,
    //     fieldOwnerId: paramfieldOwnerId,
    //     fieldTypeId: paramFieldTypeId,
    //     price: paramPrice,
    //     startTime: startday,
    //     optimal: optimize,
    //   },
    // ]),
  }).then(res => res.json());
}

export function fetchGetFreeTime(fieldownerid, fieldTypeId, argDate) {
  return fetch(
    BASE_URL +
      GET_FREE_TIME +
      '?field-owner-id=' +
      fieldownerid +
      '&field-type-id=' +
      fieldTypeId +
      '&date=' +
      argDate,
    { headers: { Authorization: auth.tokenValue } },
  ).then(res => res.json());
}

export function fetchBookMatch(
  argDate,
  argDuration,
  argFieldOwnerId,
  argFieldTypeId,
  argStartTime,
) {
  return fetch(BASE_URL + BOOK_MATCH, {
    method: 'POST',
    headers: {
      'content-type': 'application/json',
      Authorization: auth.tokenValue,
    },
    body: JSON.stringify({
      date: argDate,
      endTime: argDuration,
      fieldOwnerId: argFieldOwnerId,
      fieldTypeId: argFieldTypeId,
      startTime: argStartTime,
    }),
  }).then(res => res.json());
}

export function fetchGetFreeFieldByTime(fieldownerid, fieldTypeId, date, time) {
  return fetch(
    BASE_URL +
      GET_FREE_FIELD +
      '?field-owner-id=' +
      fieldownerid +
      '&field-type-id=' +
      fieldTypeId +
      '&date=' +
      date +
      '&time=' +
      time,
    { headers: { Authorization: auth.tokenValue } },
  ).then(res => res.json());
}

export function fetchSetFieldToMatch(timeSlotId, fieldId) {
  return fetch(
    BASE_URL +
      SET_FIELD +
      '?time-slot-id=' +
      timeSlotId +
      '&field-id=' +
      fieldId,
    {
      method: 'PUT',
      headers: { Authorization: auth.tokenValue },
    },
  ).then(res => res.json());
}

export function fetchGetFriendlyMatch(matchId) {
  return fetch(
    BASE_URL + GET_FRIENDLY_MATCH + '?friendly-match-id=' + matchId,
    { headers: { Authorization: auth.tokenValue } },
  ).then(res => res.json());
}

export function fetchGetTourMatch(matchId) {
  return fetch(BASE_URL + GET_TOUR_MATCH + '?tour-match-id=' + matchId, {
    headers: { Authorization: auth.tokenValue },
  }).then(res => res.json());
}

export function fetchGetOvercome(fieldownerid) {
  return fetch(BASE_URL + GET_OVERCONE + '?field-owner-id=' + fieldownerid, {
    headers: { Authorization: auth.tokenValue },
  }).then(res => res.json());
}
export function fetchUpdateProfile(id, data) {
  return fetch(BASE_URL + UPDATE_PROFILE + '?field-owner-id=' + id, {
    method: 'PUT',
    headers: {
      'content-type': 'application/json',
      Authorization: auth.tokenValue,
    },
    body: JSON.stringify({
      address: data.address,
      avatarUrl: data.avatarUrl,
      creditCard: data.creditCard,
      latitude: data.latitude,
      longitute: data.longitude,
      name: data.fieldName,
      password: data.password,
      phone: data.phone,
      username: data.username,
    }),
  }).then(res => res.json());
}
export function fetchGetAllPromotion(id) {
  return fetch(BASE_URL + GET_ALL_PROMOTION + '?field-owner-id=' + id, {
    // headers: { Authorization: auth.tokenValue },
  }).then(res => res.json());
}
export function fetchAddPromotion(id, data) {
  return fetch(BASE_URL + ADD_PROMOTION, {
    method: 'POST',
    headers: { 'content-type': 'application/json', 
    // Authorization: auth.tokenValue 
  },
    body: JSON.stringify({
      dateFrom: data.startDate.format('DD-MM-YYYY'),
      dateTo: data.endDate.format('DD-MM-YYYY'),
      endTime: data.endTime.format('Hh:mm'),
      fieldOwnerId: id,
      fieldTypeId: data.fieldStyle,
      freeServices: data.decription,
      saleOff: data.saleOff,
      startTime: data.startTime.format('Hh:mm'),
    }),
  });
}

//
