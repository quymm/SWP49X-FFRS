import { BASE_URL, ACCEPT_LOCK_ACCOUNT } from './base-URL';

export function fetchAcceptLockAccount(){
    return fetch(BASE_URL + ACCEPT_LOCK_ACCOUNT).then(res => res.json());
}