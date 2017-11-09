import { BASE_URL, GET_USER_SUGGESTION, GET_ALL_REPORT } from './base-URL';

export function fetchGetUserOrFieldOwnerSuggestion(nameSuggest, role) {
  return fetch(
    BASE_URL + GET_USER_SUGGESTION + '?name=' + nameSuggest + '&role=' + role,
  ).then(res => res.json());
}

export function fetchGetAllReport(){
  return fetch(BASE_URL + GET_ALL_REPORT).then(res => res.json());
}