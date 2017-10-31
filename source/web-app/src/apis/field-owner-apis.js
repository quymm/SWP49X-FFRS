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
} from './base-URL';

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
  ).then(res => res.status);
}
//Field
export function fetchGetAllField(fieldOwnerId) {
  return fetch(
    BASE_URL + GET_ALL_FIELD + '?&field-owner-id=' + fieldOwnerId,
  ).then(res => res.json());
}

export function fetchDeleteField(fieldId) {
  return fetch(BASE_URL + DETELE_FIELD + '?&field-id=' + fieldId, {
    method: 'DELETE',
  }).then(res => res.json());
}

export function fetchAddField(paramFieldName, paramFieldType, fieldOwnerId) {
  return fetch(BASE_URL + ADD_FIELD, {
    method: 'POST',
    headers: { 'content-type': 'application/json' },
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
  ).then(res => res.json());
}

export function fetchUpdateTimeEnableInWeek(
  paramfieldOwnerId,
  paramDayInWeek,
  startday,
  endDay,
  paramPrice,
  paramFieldTypeId,
) {
  return fetch(BASE_URL + UPDATE_TIME_ENABLE_IN_WEEK, {
    method: 'POST',
    headers: { 'content-type': 'application/json' },
    body: JSON.stringify([
      {
        dayInWeek: paramDayInWeek,
        endTime: endDay,
        fieldOwnerId: paramfieldOwnerId,
        fieldTypeId: paramFieldTypeId,
        price: paramPrice,
        startTime: startday,
      },
    ]),
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
    headers: { 'content-type': 'application/json' },
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
    },
  ).then(res => res.json());
}

export function fetchGetFriendlyMatch(matchId) {
  return fetch(
    BASE_URL + GET_FRIENDLY_MATCH + '?friendly-match-id=' + matchId,
  ).then(res => res.json());
}

export function fetchGetTourMatch(matchId) {
  return fetch(
    BASE_URL + GET_TOUR_MATCH + '?tour-match-id=' + matchId,
  ).then(res => res.json());
}

export function fetchGetOvercome(fieldownerid) {
  return fetch(
    BASE_URL + GET_OVERCONE + '?field-owner-id=' + fieldownerid,
  ).then(res => res.json());
}
//
