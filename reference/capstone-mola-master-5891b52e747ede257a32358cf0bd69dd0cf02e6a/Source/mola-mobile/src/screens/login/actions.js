import UserAPI from '../../apis/user'; 
const user = new UserAPI(); 

export const USER_LOGIN_USERNAME_PASSWORD = 'USER_LOGIN_USERNAME_PASSWORD'; 
export const loginUsernamePassword = ({username, password}) => ({ 
  type: USER_LOGIN_USERNAME_PASSWORD, 
  payload: user.loginUsernamePassword({username, password}) 
});

export const REGISTER_USING_REGISTERAPI = 'REGISTER_USING_REGISTERAPI';
export const apiRegister = ({username, password, email, firstName, lastName}) => ({
  type: REGISTER_USING_REGISTERAPI,
  payload: user.registerUsingRegisterApi({username, password, email, firstName, lastName})
});

export const USER_LOGIN_FACEBOOK = 'USER_LOGIN_FACEBOOK'; 
export const loginWithFacebook = ({accessToken}) => ({ 
  type: USER_LOGIN_FACEBOOK, 
  payload: user.loginWithFacebook({accessToken}) 
});

export const USER_RELOGIN_USERNAME_TOKEN = 'USER_RELOGIN_USERNAME_TOKEN';
export const reloginUsernameToken = ({username, token}) => ({
  type: USER_RELOGIN_USERNAME_TOKEN,
  payload: user.reloginWithToken({username, token})
})

export const UPDATE_USER_PROFILE = 'UPDATE_USER_PROFILE';
export const updateUserProfile = (profile) => ({
  type: UPDATE_USER_PROFILE,
  payload: profile
})
export const LOGOUT_USER = 'LOGOUT_USER';
export const logout = () => ({
  type: LOGOUT_USER,
})


