import {
  BASE_URL,
  GET_USER_SUGGESTION,
  GET_ALL_REPORT_USER,
  GET_ALL_REPORT_FIELD_OWNER,
  GET_ALL_VOUCHER,
  ADD_NEW_VOUCHER,
  GET_ALL_REPORT,
  BLOCK_ACCOUNT,
} from './base-URL';

//get account user save at localStorage
const auth = JSON.parse(localStorage.getItem('auth'));

export function fetchGetUserOrFieldOwnerSuggestion(nameSuggest, role) {
  return fetch(
    BASE_URL + GET_USER_SUGGESTION + '?name=' + nameSuggest + '&role=' + role, {headers: { Authorization: auth.tokenValue }}
  ).then(res => res.json());
}

export function fetchGetListReport() {
  return fetch(BASE_URL + GET_ALL_REPORT, {headers: { Authorization: auth.tokenValue }}).then(res => res.json());
}

export function fetchGetAllReportUser(id) {
  return fetch(BASE_URL + GET_ALL_REPORT_USER + '?user-id=' + id, {headers: { Authorization: auth.tokenValue }}).then(res =>
    res.json(),
  );
}

export function fetchGetAllReportFieldOwner(id) {
  return fetch(
    BASE_URL + GET_ALL_REPORT_FIELD_OWNER + '?field-owner-id=' + id, {headers: { Authorization: auth.tokenValue }}
  ).then(res => res.json());
}
export function fetchGetAllVoucher() {
  return fetch(BASE_URL + GET_ALL_VOUCHER).then(res => res.json());
}

export function fetchAddNewVoucher(point, value) {
  return fetch(BASE_URL + ADD_NEW_VOUCHER, {
    method: 'POST',
    headers: { 'content-type': 'application/json', 
    // Authorization: auth.tokenValue
   },
    body: JSON.stringify({
      bonusPointTarget: point,
      voucherValue: value,
    }),
  }).then(res => res.json());
}

export function fetchRequestLockAccount(id, staffId) {
  return fetch(BASE_URL + BLOCK_ACCOUNT + '?user-id=' + id + '&staff-id='+ staffId, {
    method: 'PUT',
    headers: { 'content-type': 'application/json', Authorization: auth.tokenValue },
  }).then(res => res.json());
}


