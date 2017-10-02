export function Login(username, password){
    const account = {username, password};
    return {
        type: 'LOGIN',
        payloads: account
    };
}

export function Register(username, password){
    const account = {username, password};
    return {
        type: 'REGISTER',
        payloads: account
    };
}
