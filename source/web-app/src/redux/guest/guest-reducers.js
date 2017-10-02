const defaultState = {
    username: '',
    password: '',
    isLoading: false,
    error: true,
    loginMessage: '',
    registerMessage: ''
}

export const guestReducer = (state = defaultState, action) => {
    switch (action.type) {
        case 'LOGIN_SUCCESSFUL':
            return{ userAccount: action.payloads };
        case 'REGISTER_SUCCESSFUL':
            return{ registerAccount: action.payloads };
        case 'ERROR':
            return {  };
        default:
            return state;
    }
}