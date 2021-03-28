import axios from 'axios';
import {
    CHECK_USER_REQUEST,
    CHECK_USER_SUCCESS,
    CHECK_USER_FAILURE,
    SET_USER_LOGIN_EMAIL,
    SET_USER_REGISTER_EMAIL
} from './authTypes';
import { baseUrl } from '../../urlConstants';

export const checkUserRequest = () => {
    return {
        type: CHECK_USER_REQUEST
    }
}

export const checkUserSuccess = response => {
    return {
        type: CHECK_USER_SUCCESS,
        payload: response
    }
}

export const checkUserFailure = error => {
    return {
        type: CHECK_USER_FAILURE,
        payload: error
    }
}

export const setUserLoginEmail = response => {
    return {
        type: SET_USER_LOGIN_EMAIL,
        payload: response
    }
}

export const setUserRegisterEmail = response => {
    return {
        type: SET_USER_REGISTER_EMAIL,
        payload: response
    }
}
