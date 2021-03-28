import {
    LOGIN_USER_FAILURE,
    LOGIN_USER_REQUEST,
    LOGIN_USER_SUCCESS,
    SET_USER_LOGIN_EMAIL
} from '../../actions/authActions/authTypes';

const initialState = {
    loading: false,
    isLoggedIn: false,
    userEmail: '',
    error: ''
}

const LoginReducer = (state = initialState, action) => {
    switch (action.type) {
        case LOGIN_USER_REQUEST:
            return {
                ...state,
                loading: true
            }

        case LOGIN_USER_SUCCESS:
            return {
                ...state,
                loading: false,
                isLoggedIn: action.payload,
                error: ''
            }

        case LOGIN_USER_FAILURE:
            return {
                ...state,
                loading: false,
                isLoggedIn: false,
                error: action.payload
            }

        case SET_USER_LOGIN_EMAIL:
            return {
                ...state,
                userEmail: action.payload
            }

        default: return state;
    }
}

export default LoginReducer;