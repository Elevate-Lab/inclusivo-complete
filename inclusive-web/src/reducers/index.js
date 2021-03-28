import { combineReducers } from 'redux';
import LoginReducer from './authReducers/loginReducers';
import RegisterReducer from './authReducers/registerReducers';
import MarginReducer from './marginReducer/marginReducer';
import checkUser from './authReducers/checkUser';
import UserStatusReducer from './authReducers/userStatus';
import UserDetailsReducer from './userDetails/user.reducer';

export default combineReducers({
    userLogin: LoginReducer,
    userRegister: RegisterReducer,
    marginDetails: MarginReducer,
    checkUser: checkUser,
    userStatus: UserStatusReducer,
    user: UserDetailsReducer,
});
