import { BASE_URL, GET_USER_SUGGESTION, GET_ALL_REPORT_USER, GET_ALL_REPORT_FIELD_OWNER } from './base-URL';

export function fetchGetUserOrFieldOwnerSuggestion(nameSuggest, role) {
  return fetch(
    BASE_URL + GET_USER_SUGGESTION + '?name=' + nameSuggest + '&role=' + role,
  ).then(res => res.json());
}

export function fetchGetAllReportUser(id){
  return fetch(BASE_URL + GET_ALL_REPORT_USER + '?user-id=' + id).then(res => res.json());
}

export function fetchGetAllReportFieldOwner(id){
  return fetch(BASE_URL + GET_ALL_REPORT_FIELD_OWNER + '?field-owner-id=' + id).then(res => res.json());
}
