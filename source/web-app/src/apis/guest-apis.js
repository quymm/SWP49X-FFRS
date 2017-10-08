import { BASE_URL, LOGIN, REGISTER } from './base-URL';

export function fetchLogin(argUsername, argPassword){
    return fetch(BASE_URL + LOGIN + '?username=' + argUsername + '&password=' + argPassword,
    ).then(res => res.json());
}

export function fetchRegister(argUsername, argPassword, argAddress, argAvatarUrl, argCreditCard, argLatitude, argLongitute, argName, argPhone){
    return fetch(BASE_URL + REGISTER, {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({
            username: argUsername,
            password: argPassword,
            address:  argAddress,
            avatarUrl: argAvatarUrl,
            creditCard: argCreditCard,
            latitude: argLatitude,
            longitute: argLongitute,
            name: argName,
            phone: argPhone
        })
    }).then(res => res.json());
}