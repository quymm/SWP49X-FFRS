import {
  BASE_URL,
  GET_MATCH_BY_DAY,
  ADD_FIELD,
  REMOVE_FIELD,
  CHECK_TIME_SLOT,
  GET_ALL_FIELD,
  DETELE_FIELD,
  GET_TIME_ENABLE_IN_WEEK,
} from './base-URL';

export function fetchGetMatchByFieldOwnerAndDay(fieldOwnerId, day) {
  return fetch(
    BASE_URL +
      GET_MATCH_BY_DAY +
      '?&fieldOwnerId=' +
      fieldOwnerId +
      '&targetDate=' +
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
  return fetch(BASE_URL + DETELE_FIELD + '?&fieldId=' + fieldId).then(res =>
    res.json(),
  );
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
//
