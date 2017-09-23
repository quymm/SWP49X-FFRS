const defaultState = {
    username: '',
    password: '',
    isLoading: '',
    error: ''
}

export const guestReducer = (state = defaultState, action) => {
    switch (action.type) {
        case 'LOGIN':
            return({});
        case 'REGISTER':
            return({});
        default:
            return state;
    }
}