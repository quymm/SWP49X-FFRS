export function fetchLogin(username, password){
    return {
        type: 'LOGIN',
        username, 
        password
    };
}

export function fetchRegister(username, password){
    return {
        type: 'REGISTER',
        username, 
        password
    };
}
