export function Login(username, password){
    return {
        type: 'LOGIN',
        username, 
        password
    };
}

export function Register(username, password){
    return {
        type: 'REGISTER',
        username, 
        password
    };
}
