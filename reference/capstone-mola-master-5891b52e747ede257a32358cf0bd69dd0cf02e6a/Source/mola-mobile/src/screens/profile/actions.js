import UserAPI from '../../apis/user'; 
const user = new UserAPI(); 

export const GET_USER_PROFILE = 'GET_USER_PROFILE';
export const getUserProfile = ({username}) => ({
    type: GET_USER_PROFILE,
    payload: user.getUserProfile({username})
});