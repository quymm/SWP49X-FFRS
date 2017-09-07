import { StackNavigator } from 'react-navigation';
import MainNavigator from './MainNavigator';
import {
  LoginScreen,
  SignupSuccess,
  TeacherSignUpScreen,
  SearchScreen,
  SignupScreen,
  CourseScreen,
  CreateCourseScreen,
  CreateLessonScreen,
  SetPriceScreen,
  RequestDetailScreen,
  UserProfileScreen,
  CourseInfoScreen,
  TeacherProfileScreen,
  CreateCourseChapter,
  SetTimeSlotScreen,
  SettingScreen,
  GeneralSettingScreen,
  LanguageSettingScreen,
  PasswordSettingScreen,
  CourseInfoToRegistScreen,
  SelectTimeSlotScreen,
  SearchFilterScreen,
  LanguageFilterScreen,
  PriceFilterScreen,
  RegisterLanguageScreen,
  RatingFilterScreen,
  CourseRegistedScreen,
  RatingScreen,
  LocalSettingScreen,
  VideoCallScreen,
  CallWaitingScreen,
  RevenueScreen,
  PolicyScreen,
} from '../screens';

import MessageScreen from '../screens/message/MessageScreen';
export default StackNavigator({
  Login: {
    screen: LoginScreen
  },
  Main: {
    screen: MainNavigator
  },
  Signup: {
    screen: SignupScreen
  },
  TeacherSignUp: {
    screen: TeacherSignUpScreen
  },
  SignupSuccess: {
    screen: SignupSuccess
  },
  SearchScreen: {
    screen: SearchScreen
  },
  MessageDetail: {
    screen: MessageScreen
  },
  CreateCourse: {
    screen: CreateCourseScreen
  },
  CourseScreen: {
    screen: CourseScreen
  },
  CreateLesson: {
    screen: CreateLessonScreen
  },
  SetPrice: {
    screen: SetPriceScreen
  },
  CourseInfo: {
    screen: CourseInfoScreen
  },
  RequestDetail: {
    screen: RequestDetailScreen
  },
  UserProfile: {
    screen: UserProfileScreen
  },
  TeacherProfile: {
    screen: TeacherProfileScreen
  },
  SettingScreen: {
    screen: SettingScreen
  },
  CreateCourseChapter: {
    screen: CreateCourseChapter
  },
  SetTimeSlot: {
    screen: SetTimeSlotScreen
  },
  GeneralSetting: {
    screen: GeneralSettingScreen
  },
  LanguageSetting: {
    screen: LanguageSettingScreen
  },
  PasswordSetting: {
    screen: PasswordSettingScreen
  },
  CourseInfoToRegist: {
    screen: CourseInfoToRegistScreen
  },
  SelectTimeSlot: {
    screen: SelectTimeSlotScreen
  },
  SearchFilter: {
    screen: SearchFilterScreen
  },
  LanguageFilter: {
    screen: LanguageFilterScreen
  },
  PriceFilter: {
    screen: PriceFilterScreen
  },
  RegisterLanguage: {
    screen: RegisterLanguageScreen
  },
  RatingFilter: {
    screen: RatingFilterScreen
  },
  CourseRegisted: {
    screen: CourseRegistedScreen
  },
  Rating: {
    screen: RatingScreen
  },
  LocalSetting: {
    screen: LocalSettingScreen
  },
  VideoCall: {
    screen: VideoCallScreen
  },
  CallWaiting: {
    screen: CallWaitingScreen
  },
  Revenue: {
    screen: RevenueScreen
  },
  Policy: {
    screen: PolicyScreen
  }
},
  {
    initialRouteName: 'Login',
  }
);
