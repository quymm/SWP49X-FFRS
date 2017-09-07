import { combineReducers } from 'redux';
// import { reducer as form } from 'redux-form';
import SearchReducer from '../screens/search/reducer';
import LoginReducer from '../screens/login/reducer'; 
import TeacherSignUpReducer from '../screens/home/teacher-sign-up/reducers';
import UserProfileReducer from '../screens/profile/reducer';
import SearchFilterReducer from '../screens/search/filter/reducers';
import UpcomingSessionReducer from '../screens/home/reducer';
export default combineReducers({
  search: SearchReducer,
  auth: LoginReducer,
  teacherSignUp: TeacherSignUpReducer,
  profile: UserProfileReducer,
  searchFilter: SearchFilterReducer,
  upcoming: UpcomingSessionReducer,
});