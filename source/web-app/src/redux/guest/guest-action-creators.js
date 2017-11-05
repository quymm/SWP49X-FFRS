export function doLoginSuccessful(data){
    return {
        type: 'LOGIN_SUCCESSFUL',
        payloads: data
    };
}

export function doRegister(data){
    return {
        type: 'REGISTER',
        payloads: data
    };
}

export function doLoginError(data){
    return {
        type: 'LOGIN_ERROR',
        payloads: data
    }
}

export function accessDenied(){
    return {
        type: 'ACCESS_DENIED',
    }
}
