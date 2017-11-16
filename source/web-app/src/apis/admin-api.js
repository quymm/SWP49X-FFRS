import {
  BASE_URL,
  ACCEPT_LOCK_ACCOUNT,
  GET_ALL_REQUESTED_LOCK,
} from './base-URL';

export function fetchAcceptLockAccount(id) {
  return fetch(BASE_URL + ACCEPT_LOCK_ACCOUNT + '?account-id=' + id, {
    method: 'PUT',
    headers: { 'content-type': 'application/json' },
  }).then(res => res.json());
}

export function fetchGetAllRequestedLock() {
  return fetch(BASE_URL + GET_ALL_REQUESTED_LOCK).then(res => res.json());
}
