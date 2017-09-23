import { BASE_URL } from './base-URL';

export function fetchLogin(argUsername, argPassword){
    return fetch(BASE_URL, {
        method: 'POST',
        body: JSON.stringify({
            username: argPassword, 
            password: argPassword
        })     
    }).then(res => res.json());
}

export function fetchRegister(argUsername, argPassword){
    return fetch(BASE_URL, {
        method: 'POST',
        body: JSON.stringify({
            username: argUsername,
            password: argPassword
        })
    }).then(res => res.json());
}