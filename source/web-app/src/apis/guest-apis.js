import {
  BASE_URL,
  LOGIN,
  REGISTER,
  GOOGLE_MAP_KEY,
  GOOGLE_MAP_URL,
} from './base-URL';

export async function fetchLogin(argUsername, argPassword) {
  return fetch(
    BASE_URL + LOGIN + '?username=' + argUsername + '&password=' + argPassword,
  ).then(res => res.json());
}

export function fetchRegister(
  argUsername,
  argPassword,
  argAddress,
  argAvatarUrl,
  argCreditCard,
  argLatitude,
  argLongitute,
  argName,
  argPhone,
) {
  return fetch(BASE_URL + REGISTER, {
    method: 'POST',
    headers: { 'content-type': 'application/json' },
    body: JSON.stringify({
      address: argAddress,
      avatarUrl: argAvatarUrl,
      creditCard: '123456',
      latitude: argLatitude,
      longitute: argLongitute,
      name: argName,
      password: argPassword,
      phone: argPhone,
      username: argUsername,
    }),
  }).then(res => res.json());
}

export function fechGetAddressByLocationGoogleMap(argLatitude, argLongitute) {
  return fetch(
    GOOGLE_MAP_URL +
      'latlng=' +
      argLatitude +
      ',' +
      argLongitute +
      '&key=' +
      GOOGLE_MAP_KEY,
  )
    .then(res => res.json())
    .then(resJSON => resJSON.results[0].formatted_address);
}
